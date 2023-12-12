package sport_maps.commons.util.mapper;

import org.springframework.stereotype.Component;
import sport_maps.coach.domain.Coach;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;
import sport_maps.comments.domain.Comment;
import sport_maps.comments.domain.EventComment;
import sport_maps.comments.domain.ForumComment;
import sport_maps.comments.domain.NewsComment;
import sport_maps.comments.dto.CommentDto;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.image.domain.Image;
import sport_maps.commons.domain.SportType;
import sport_maps.nef.domain.Event;
import sport_maps.nef.domain.Forum;
import sport_maps.nef.domain.News;
import sport_maps.nef.dto.*;
import sport_maps.security.domain.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public CommentDto toCommentDto(Comment comment) {
        return switch (comment) {
            case EventComment com ->
                    new CommentDto(com.getId(), String.valueOf(com.getDate()), com.getText(), com.getAuthor().getEmail()
                            + "|" + com.getAuthor().getFirstName() + "|" + com.getAuthor().getLastName(), com.getEvent().getId());
            case ForumComment com ->
                    new CommentDto(com.getId(), String.valueOf(com.getDate()), com.getText(), com.getAuthor().getEmail()
                            + "|" + com.getAuthor().getFirstName() + "|" + com.getAuthor().getLastName(), com.getForum().getId());
            case NewsComment com ->
                    new CommentDto(com.getId(), String.valueOf(com.getDate()), com.getText(), com.getAuthor().getEmail()
                            + "|" + com.getAuthor().getFirstName() + "|" + com.getAuthor().getLastName(), com.getNews().getId());
            case null, default -> null;
        };
    }

    public List<CommentDto> toListCommentDto(List<? extends Comment> comments) {
        return comments.stream().map(this::toCommentDto).collect(Collectors.toList());
    }

    public EventComment convertToEntity(CommentSaveDto dto, User user, Event event, EventComment comment) {
        comment.setDate(dto.date());
        comment.setText(dto.text());
        comment.setAuthor(user);
        comment.setEvent(event);
        return comment;
    }

    public ForumComment convertToEntity(CommentSaveDto dto, User user, Forum forum, ForumComment comment) {
        comment.setDate(dto.date());
        comment.setText(dto.text());
        comment.setAuthor(user);
        comment.setForum(forum);
        return comment;
    }

    public NewsComment convertToEntity(CommentSaveDto dto, User user, News news, NewsComment comment) {
        comment.setDate(dto.date());
        comment.setText(dto.text());
        comment.setAuthor(user);
        comment.setNews(news);
        return comment;
    }

    public EventDto toEventDto(Event event) {
        return new EventDto(event.getId(), event.getName(), String.valueOf(event.getDate()), event.getText(), event.getAuthor().getEmail() + "|" + event.getAuthor().getFirstName() + "|" + event.getAuthor().getLastName(),
                String.valueOf(event.getSportType()), fetchImage(event.getImageList()), toListCommentDto(event.getComments()));
    }

    public ForumDto toForumDto(Forum forum) {
        return new ForumDto(forum.getId(), forum.getName(), String.valueOf(forum.getDate()), forum.getText(), forum.getAuthor().getEmail() + "|" + forum.getAuthor().getFirstName() + "|" + forum.getAuthor().getLastName(),
                toListCommentDto(forum.getComments()));
    }

    public NewsDto toNewsDto(News news) {
        return new NewsDto(news.getId(), news.getName(), String.valueOf(news.getDate()), news.getText(), news.getAuthor().getEmail() + "|" + news.getAuthor().getFirstName() + "|" + news.getAuthor().getLastName(),
                fetchImage(news.getImageList()), toListCommentDto(news.getComments()));
    }

    public Forum convertToEntity(ForumSaveDto dto, User user, Forum forum) {
        forum.setName(dto.name());
        forum.setDate(dto.date());
        forum.setText(dto.text());
        forum.setAuthor(user);
        return forum;
    }

    public Event convertToEntity(EventSaveDto dto, SportType sportType, User user, Event event) {
        event.setName(dto.name());
        event.setDate(dto.date());
        event.setText(dto.text());
        event.setSportType(sportType);
        event.setAuthor(user);
        return event;
    }

    public News convertToEntity(NewsSaveDto dto, User user, News news) {
        news.setName(dto.name());
        news.setDate(dto.date());
        news.setText(dto.text());
        news.setAuthor(user);
        return news;
    }

    public byte[] fetchImage(List<? extends Image> allByEntityId) {
        if (allByEntityId.isEmpty()) {
            return null;
        }
        Image singleImage = allByEntityId.stream().findFirst().orElseThrow();
        String imagePath = singleImage.getFilePath();
        byte[] image;
        try {
            image = Files.readAllBytes(new File(imagePath).toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't fetch image.");
        }
        return image;
    }

    public Coach convertToEntity(CoachSaveDto dto, SportType sportType,
                                 Coach coach) {
        coach.setFirstName(dto.firstName());
        coach.setLastName(dto.lastName());
        coach.setAge(dto.age());
        coach.setPrice(dto.price());
        coach.setExperience(dto.experience());
        coach.setDescription(dto.description());
        coach.setSportType(sportType);
        return coach;
    }

    public CoachDto toCoachDto(Coach coach) {
        return new CoachDto(coach.getId(), coach.getFirstName(), coach.getLastName(), coach.getAge(), coach.getExperience(), coach.getPrice(), coach.getDescription(), String.valueOf(coach.getSportType()), fetchImage(coach.getImageList()));
    }
}
