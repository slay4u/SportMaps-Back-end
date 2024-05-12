package sport_maps.commons.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import sport_maps.commons.util.mapper.Mapper;

import java.lang.reflect.Field;

public abstract class AbstractService<E, DAO extends JpaRepository<E, Long>, MAP extends Mapper> {
    protected DAO dao;
    protected MAP mapper;
    protected abstract void setDao(DAO dao);
    protected abstract void setMapper(MAP mapper);

    public void save(E e) {
        dao.save(e);
    }

    public void update(E e, Long id) {
        dao.findById(id).ifPresent(entity -> merge(e, entity));
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

    private void merge(E from, E to) {
        try {
            Field[] allClassFields = from.getClass().getDeclaredFields();
            for (Field field : allClassFields) {
                if (field.getName().equals("id") || field.getName().equals("author") || field.getName().equals("comments")
                        || field.getName().equals("imageList") || field.getName().equals("date") || field.getName().equals("event")
                        || field.getName().equals("forum") || field.getName().equals("news")) continue;
                Field toField = to.getClass().getDeclaredField(field.getName());
                field.setAccessible(true);
                toField.setAccessible(true);
                toField.set(to, field.get(from));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Exception in merge: " + e.getMessage());
        }
        if (from.getClass().getSuperclass() != null) {
            try {
                Field[] allSuperClassFields = from.getClass().getSuperclass().getDeclaredFields();
                for (Field field : allSuperClassFields) {
                    if (field.getName().equals("id") || field.getName().equals("author") || field.getName().equals("comments")
                            || field.getName().equals("imageList") || field.getName().equals("date") || field.getName().equals("event")
                            || field.getName().equals("forum") || field.getName().equals("news")) continue;
                    Field toField = to.getClass().getSuperclass().getDeclaredField(field.getName());
                    field.setAccessible(true);
                    toField.setAccessible(true);
                    toField.set(to, field.get(from));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Exception in merge: " + e.getMessage());
            }
        }
    }
}
