package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.NewsCommentDao;
import sport_maps.comments.domain.NewsComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;

@Service("newsCommentServiceImpl")
@Transactional
public class NewsCommentServiceImpl implements CommentService {
    private final NewsCommentDao commentDao;
    private final UserDao userDao;
    private final NewsDao newsDao;
    private final Mapper mapper;

    public NewsCommentServiceImpl(NewsCommentDao commentDao, UserDao userDao, NewsDao newsDao, Mapper mapper) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.newsDao = newsDao;
        this.mapper = mapper;
    }

    @Override
    public void createComment(CommentSaveDto dto) {
        validateComment(dto);
        User user = getUser(dto);
        News news = getNew(dto);
        NewsComment comment = mapper.convertToEntity(dto, user, news, new NewsComment());
        commentDao.save(comment);
    }

    @Override
    public void updateComment(Long id, CommentSaveDto dto) {
        validateComment(dto);
        User user = getUser(dto);
        News news = getNew(dto);
        NewsComment comment = mapper.convertToEntity(dto, user, news, new NewsComment());
        commentDao.save(updateContent(comment, getById(id)));
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        commentDao.deleteById(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(() -> new EntityNotFoundException("User wasn't found."));
    }

    private News getNew(CommentSaveDto dto) {
        return newsDao.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("News wasn't found."));
    }

    private void validateComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Text is not valid.");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date is not valid.");
        }
        if (dto.author().isBlank()) {
            throw new IllegalArgumentException("User email is not valid.");
        }
    }

    private NewsComment updateContent(NewsComment comment, NewsComment resultComment) {
        resultComment.setText(comment.getText());
        resultComment.setDate(comment.getDate());
        return resultComment;
    }

    private NewsComment getById(Long id) {
        return commentDao.findById(id).orElseThrow(() -> new EntityNotFoundException("NewsComment wasn't found."));
    }
}
