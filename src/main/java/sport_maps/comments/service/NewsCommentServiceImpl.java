package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.NewsCommentDao;
import sport_maps.comments.domain.NewsComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.commons.service.AbstractService;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

@Service("newsCommentServiceImpl")
@Transactional
public class NewsCommentServiceImpl extends AbstractService<NewsComment, NewsCommentDao, Mapper> implements CommentService {
    private final UserDao userDao;
    private final NewsDao newsDao;

    @Override
    @Autowired
    protected void setDao(NewsCommentDao commentDao) {
        this.dao = commentDao;
    }

    @Override
    @Autowired
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public NewsCommentServiceImpl(UserDao userDao, NewsDao newsDao) {
        this.userDao = userDao;
        this.newsDao = newsDao;
    }

    @Override
    public void createComment(CommentSaveDto dto) {
        validateComment(dto);
        save(mapper.convertToEntity(dto, getUser(dto), getNews(dto)));
    }

    @Override
    public void updateComment(Long id, CommentSaveDto dto) {
        validateComment(dto);
        update(mapper.convertToEntity(dto, getUser(dto), getNews(dto)), id);
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(EntityNotFoundException::new);
    }

    private News getNews(CommentSaveDto dto) {
        return newsDao.findById(dto.id()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) throw new IllegalArgumentException("Text is not valid.");
        if (dto.author().isBlank()) throw new IllegalArgumentException("User email is not valid.");
    }
}
