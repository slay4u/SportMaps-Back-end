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
import sport_maps.commons.domain.Image;
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
public class CustomObjectMapper {
    public CommentDto toCommentDto(Comment comment) {
        if (comment instanceof EventComment com)
            return new CommentDto(com.getId(), String.valueOf(com.getDate()),
                    com.getText(), com.getCreatedBy().getEmail(), com.getEvent().getId());
        else if (comment instanceof ForumComment com)
            return new CommentDto(com.getId(), String.valueOf(com.getDate()),
                    com.getText(), com.getCreatedBy().getEmail(), com.getForum().getId());
        else if (comment instanceof NewsComment com)
            return new CommentDto(com.getId(), String.valueOf(com.getDate()),
                    com.getText(), com.getCreatedBy().getEmail(), com.getNews().getId());
        else return null;
    }

    public List<CommentDto> toListCommentDto(List<? extends Comment> comments) {
        return comments.stream().map(this::toCommentDto).collect(Collectors.toList());
    }

    public EventComment convertToEntity(CommentSaveDto dto, User user, Event event, EventComment comment) {
        comment.setDate(dto.date());
        comment.setText(dto.text());
        comment.setCreatedBy(user);
        comment.setEvent(event);
        return comment;
    }

    public ForumComment convertToEntity(CommentSaveDto dto, User user, Forum forum, ForumComment comment) {
        comment.setDate(dto.date());
        comment.setText(dto.text());
        comment.setCreatedBy(user);
        comment.setForum(forum);
        return comment;
    }

    public NewsComment convertToEntity(CommentSaveDto dto, User user, News news, NewsComment comment) {
        comment.setDate(dto.date());
        comment.setText(dto.text());
        comment.setCreatedBy(user);
        comment.setNews(news);
        return comment;
    }

    public EventDto toEventDto(Event event) {
        return new EventDto(event.getId(), event.getName(), String.valueOf(event.getDate()), event.getText(), String.valueOf(event.getSportType()),
                event.getCreatedBy().getEmail(), fetchImage(event.getImageList()), toListCommentDto(event.getCommentList()));
    }

    public List<EventDto> toListEventDto(List<Event> events) {
        return events.stream().map(this::toEventDto).collect(Collectors.toList());
    }

    public ForumDto toForumDto(Forum forum) {
        return new ForumDto(forum.getId(), forum.getName(), String.valueOf(forum.getDate()), forum.getText(), forum.getCreatedBy().getEmail(),
                toListCommentDto(forum.getCommentList()));
    }

    public List<ForumDto> toListForumDto(List<Forum> forums) {
        return forums.stream().map(this::toForumDto).collect(Collectors.toList());
    }

    public NewsDto toNewsDto(News news) {
        return new NewsDto(news.getId(), news.getName(), String.valueOf(news.getDate()), news.getText(),
                fetchImage(news.getImageList()), news.getCreatedBy().getEmail(), toListCommentDto(news.getCommentList()));
    }

    public List<NewsDto> toListNewsDto(List<News> news) {
        return news.stream().map(this::toNewsDto).collect(Collectors.toList());
    }

    public Forum convertToEntity(ForumSaveDto dto, User user, Forum forum) {
        forum.setName(dto.name());
        forum.setDate(dto.date());
        forum.setText(dto.text());
        forum.setCreatedBy(user);
        return forum;
    }

    public Event convertToEntity(EventSaveDto dto, SportType sportType, User user, Event event) {
        event.setName(dto.name());
        event.setDate(dto.date());
        event.setText(dto.text());
        event.setSportType(sportType);
        event.setCreatedBy(user);
        return event;
    }

    public News convertToEntity(NewsSaveDto dto, User user, News news) {
        news.setName(dto.name());
        news.setDate(dto.date());
        news.setText(dto.text());
        news.setCreatedBy(user);
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

    public List<CoachDto> toListCoachDto(List<Coach> coaches) {
        return coaches.stream().map(this::toCoachDto).collect(Collectors.toList());
    }
}
