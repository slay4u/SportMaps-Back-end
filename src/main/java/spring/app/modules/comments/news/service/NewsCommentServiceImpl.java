package spring.app.modules.comments.news.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.comments.news.dao.NewsCommentDao;
import spring.app.modules.comments.news.domain.NewsComment;
import spring.app.modules.comments.news.dto.NewsCommentCreateDto;
import spring.app.modules.comments.news.dto.NewsCommentDto;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.news.dao.NewDao;
import spring.app.modules.news.domain.New;
import spring.app.modules.security.dao.UserDao;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class NewsCommentServiceImpl implements NewsCommentService, NewsCommentGeneralHandler {
    private final NewsCommentDao newsCommentDao;
    private final UserDao userDao;
    private final NewDao newDao;

    public NewsCommentServiceImpl(NewsCommentDao newsCommentDao, UserDao userDao, NewDao newDao) {
        this.newsCommentDao = newsCommentDao;
        this.userDao = userDao;
        this.newDao = newDao;
    }

    @Override
    public int createNewsComment(NewsCommentCreateDto commentDto) {
        validateNewsComment(commentDto);
        User user = getUser(commentDto);
        New aNew = getNew(commentDto);
        NewsComment comment = convertToEntity(commentDto, user, aNew, new NewsComment());
        newsCommentDao.save(comment);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateNewsComment(Long id, NewsCommentCreateDto commentDto) {
        validateNewsComment(commentDto);
        User user = getUser(commentDto);
        New aNew = getNew(commentDto);
        NewsComment comment = convertToEntity(commentDto, user, aNew, new NewsComment());
        newsCommentDao.save(updateContent(comment, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public NewsCommentDto getNewsCommentById(Long id) {
        NewsComment byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        newsCommentDao.deleteById(id);
    }

    @Override
    public List<NewsCommentDto> getAllNewsComments() {
        List<NewsComment> comments = newsCommentDao.getAllNewsComments();
        return listToDto(comments);
    }

    private User getUser(NewsCommentCreateDto commentDto) {
        return userDao.findByEmail(commentDto.getEmailUser()).orElseThrow(() ->
                new NotFoundException("User by email " + commentDto.getEmailUser() + " was not found."));
    }

    private New getNew(NewsCommentCreateDto commentDto) {
        return newDao.getNewById(commentDto.getIdNew()).orElseThrow(() ->
                new NotFoundException("News by id " + commentDto.getIdNew() + " was not found."));
    }

    private void validateNewsComment(NewsCommentCreateDto newsCommentCreateDto) {
        if (newsCommentCreateDto.getText().isBlank() || Objects.isNull(newsCommentCreateDto.getText())) {
            throw new IllegalArgumentException("NewsComment's text is not valid");
        }
        if (newsCommentCreateDto.getCreatedDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Create date is not valid");
        }
        if (newsCommentCreateDto.getEmailUser().isBlank() || Objects.isNull(newsCommentCreateDto.getEmailUser())) {
            throw new IllegalArgumentException("NewsComment's user email is not valid");
        }
        if (newsCommentCreateDto.getIdNew() == null || Objects.isNull(newsCommentCreateDto.getIdNew())) {
            throw new IllegalArgumentException("NewsComment's idNew is not valid");
        }
    }

    private NewsComment updateContent(NewsComment newsComment, NewsComment resultNewsComment) {
        resultNewsComment.setText(newsComment.getText());
        resultNewsComment.setCreatedDate(newsComment.getCreatedDate());
        return resultNewsComment;
    }

    private NewsComment getById(Long id) {
        Optional<NewsComment> resultNewsComment = newsCommentDao.getNewsCommentById(id);
        if (resultNewsComment.isEmpty()) {
            throw new NotFoundException("NewsComment by id was not found!");
        }
        return resultNewsComment.get();
    }

    private NewsComment convertToEntity(NewsCommentCreateDto newsCommentCreateDto, User user, New aNew,  NewsComment newsComment) {
        newsComment.setCreatedDate(newsCommentCreateDto.getCreatedDate());
        newsComment.setText(newsCommentCreateDto.getText());
        newsComment.setCreatedBy(user);
        newsComment.setNews(aNew);
        return newsComment;
    }

    @Override
    public List<NewsCommentDto> listToDto(List<NewsComment> newsComments) {
        return NewsCommentGeneralHandler.super.listToDto(newsComments);
    }

    @Override
    public NewsCommentDto allInfoDto(NewsComment newsComment) {
        return NewsCommentGeneralHandler.super.allInfoDto(newsComment);
    }
}
