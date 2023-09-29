package spring.app.modules.commons.service;

import jakarta.persistence.Lob;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.dao.BaseDao;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.commons.exception.SMBusinessLogicException;
import spring.app.modules.commons.util.ReflectUtils;
import spring.app.modules.commons.util.convert.SimpleEntityConverter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Service class for base operations
 *
 * @author Ivan Krylosov
 */

public abstract class BaseService<ENTITY, ID, DAO extends BaseDao<ENTITY, ID>> {

    private final DAO baseDao;

    public BaseService(DAO baseDao) {
        this.baseDao = baseDao;
    }

    protected <DTO> int CREATE(DTO dto, ENTITY e) {
        ENTITY converted = SimpleEntityConverter.convert(dto, e);
        return baseSave(converted, baseDao::save);
    }

    protected <DTO> int UPDATE(DTO dto, ENTITY e, ID id) {
        ENTITY converted = SimpleEntityConverter.convert(dto, e);
        return baseSave(converted, entity -> baseDao.save(updateContent(entity, getById(id))));
    }

    protected <DTO> DTO READ_ID(ID id, Class<DTO> dtoClass) {
        ENTITY byId = getById(id);
        return toSendDto(byId, ReflectUtils.newInstanceOf(dtoClass));
    }

    protected void DELETE(ID id) {
        baseDao.deleteById(id);
    }

    /**
     * {@link BaseService#READ_ID(Object, Class)}
     */
    protected <DTO> List<DTO> READ_ALL(int page, Class<DTO> dtoClass) {
        if (page < 0) {
            throw new SMBusinessLogicException("Page not valid: " + page);
        }
        List<ENTITY> entities = baseDao.findAll(PageRequest.of(page, SystemConstants.PAGE_ELEMENTS_AMOUNT)).get().toList();
        return entities.stream().map(e -> toSendDto(e, ReflectUtils.newInstanceOf(dtoClass))).collect(Collectors.toList());
    }

    /**
     * FIXME: Reduce the number of calls to db. Provide a unique field that is distinct for every entity in the table and check for exception.
     */
    @Deprecated
    private boolean isExist(ENTITY e) {
        if (ReflectUtils.isNull(e)) {
            return false;
        }
        ExampleMatcher ignoreNulls = withExclude(e, ExampleMatcher.matching().withIgnoreNullValues());
        Example<ENTITY> entityExample = Example.of(e, ignoreNulls);
        return baseDao.findOne(entityExample).isPresent();
    }

    // Problem with PostgresQL OIDs: every one of them is distinct
    private ExampleMatcher withExclude(ENTITY e, ExampleMatcher matcher) {
        List<Field> lobs = ReflectUtils.annotated(e, Lob.class);
        if (!lobs.isEmpty()) {
            matcher = matcher.withIgnorePaths(lobs.stream().map(Field::getName).toArray(String[]::new));
        }
        return matcher;
    }

    protected ENTITY updateContent(ENTITY from, ENTITY to) {
        return SimpleEntityConverter.convert(from, to);
    }

    protected int baseSave(ENTITY e, Consumer<ENTITY> daoAction) {
        daoAction.accept(e);
        return HttpStatus.CREATED.value();
    }

    protected ENTITY getById(ID id) {
        return baseDao.findById(id).orElseThrow(() -> new NotFoundException("ENTITY not found with id: " + id));
    }

    private <DTO> DTO toSendDto(ENTITY e, DTO dto) {
        return SimpleEntityConverter.convert(e, dto);
    }
}
