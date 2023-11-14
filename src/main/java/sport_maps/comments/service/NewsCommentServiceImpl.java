package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.NewsCommentDao;
import sport_maps.comments.domain.NewsComment;
import sport_maps.comments.dto.CommentDto;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.commons.util.mapper.CustomObjectMapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("newsCommentServiceImpl")
@Transactional
public class NewsCommentServiceImpl implements CommentService {
    private final NewsCommentDao newsCommentDao;
    private final UserDao userDao;
    private final NewsDao newsDao;
    private final CustomObjectMapper mapper;

    public NewsCommentServiceImpl(NewsCommentDao newsCommentDao, UserDao userDao, NewsDao newsDao,
                                  CustomObjectMapper mapper) {
        this.newsCommentDao = newsCommentDao;
        this.userDao = userDao;
        this.newsDao = newsDao;
        this.mapper = mapper;
    }

    @Override
    public int createComment(CommentSaveDto dto) {
        validateNewsComment(dto);
        User user = getUser(dto);
        News aNews = getNew(dto);
        NewsComment comment = mapper.convertToEntity(dto, user, aNews, new NewsComment());
        newsCommentDao.save(comment);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateComment(Long id, CommentSaveDto dto) {
        validateNewsComment(dto);
        User user = getUser(dto);
        News aNews = getNew(dto);
        NewsComment comment = mapper.convertToEntity(dto, user, aNews, new NewsComment());
        newsCommentDao.save(updateContent(comment, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public CommentDto getCommentById(Long id) {
        NewsComment byId = getById(id);
        return mapper.toCommentDto(byId);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<NewsComment> comments = newsCommentDao.findAll();
        return mapper.toListCommentDto(comments);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        newsCommentDao.deleteById(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.createdBy()).orElseThrow(() ->
                new EntityNotFoundException("User by email " + dto.createdBy() + " was not found."));
    }

    private News getNew(CommentSaveDto dto) {
        return newsDao.findById(dto.id()).orElseThrow(() ->
                new EntityNotFoundException("News by id " + dto.id() + " was not found."));
    }

    private void validateNewsComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Text is not valid");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date is not valid");
        }
        if (dto.createdBy().isBlank()) {
            throw new IllegalArgumentException("User email is not valid");
        }
    }

    private NewsComment updateContent(NewsComment newsComment, NewsComment resultNewsComment) {
        resultNewsComment.setText(newsComment.getText());
        resultNewsComment.setDate(newsComment.getDate());
        return resultNewsComment;
    }

    private NewsComment getById(Long id) {
        Optional<NewsComment> resultNewsComment = newsCommentDao.findById(id);
        if (resultNewsComment.isEmpty()) {
            throw new EntityNotFoundException("NewsComment by id was not found!");
        }
        return resultNewsComment.get();
    }
}
