package sport_maps.commons.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;

public abstract class AbstractService<E, DAO extends JpaRepository<E, Long>> {
    protected DAO dao;
    protected abstract void setDao(DAO dao);

    public void save(E e) {
        dao.save(e);
    }

    public void update(E e, Long id) {
        dao.findById(id).ifPresent(entity -> dao.save(merge(e, entity)));
    }

    public E getById(Long id) {
        return dao.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(Long id) {
        dao.deleteById(id);
    }

    public Page<E> getAll(int page, int size) {
        return dao.findAll(PageRequest.of(page, size));
    }

    private E merge(E from, E to) {
        try {
            Field[] allFields = from.getClass().getDeclaredFields();
            for (Field field : allFields) {
                Field toField = to.getClass().getDeclaredField(field.getName());
                field.setAccessible(true);
                toField.setAccessible(true);
                toField.set(to, field.get(from));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Exception in merge: " + e.getMessage());
        }
        return to;
    }
}
