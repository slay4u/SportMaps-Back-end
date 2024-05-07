package sport_maps.commons.util.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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
import sport_maps.image.domain.NewsImage;
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
    public CommentDto convertToDto(Comment comment) {
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

    public List<CommentDto> convertToListDto(List<? extends Comment> comments) {
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public EventComment convertToEntity(CommentSaveDto dto, User user, Event event) {
        EventComment comment = new EventComment();
        comment.setText(dto.text());
        comment.setAuthor(user);
        comment.setEvent(event);
        return comment;
    }

    public ForumComment convertToEntity(CommentSaveDto dto, User user, Forum forum) {
        ForumComment comment = new ForumComment();
        comment.setText(dto.text());
        comment.setAuthor(user);
        comment.setForum(forum);
        return comment;
    }

    public NewsComment convertToEntity(CommentSaveDto dto, User user, News news) {
        NewsComment comment = new NewsComment();
        comment.setText(dto.text());
        comment.setAuthor(user);
        comment.setNews(news);
        return comment;
    }

    public EventDto convertToDto(Event event) {
        return new EventDto(event.getId(), event.getName(), String.valueOf(event.getDate()), event.getText(), event.getAuthor().getEmail() + "|" + event.getAuthor().getFirstName() + "|" + event.getAuthor().getLastName(),
                String.valueOf(event.getSportType()), fetchImage(event.getImageList()), convertToListDto(event.getComments()));
    }

    public ForumDto convertToDto(Forum forum) {
        return new ForumDto(forum.getId(), forum.getName(), String.valueOf(forum.getDate()), forum.getText(), forum.getAuthor().getEmail() + "|" + forum.getAuthor().getFirstName() + "|" + forum.getAuthor().getLastName(),
                convertToListDto(forum.getComments()));
    }

    public NewsDto convertToDto(News news) {
        return new NewsDto(news.getId(), news.getName(), String.valueOf(news.getDate()), news.getText(), news.getAuthor().getEmail() + "|" + news.getAuthor().getFirstName() + "|" + news.getAuthor().getLastName(),
                fetchImage(news.getImageList()), convertToListDto(news.getComments()));
    }

    public Forum convertToEntity(ForumSaveDto dto, User user) {
        Forum forum = new Forum();
        forum.setName(dto.name());
        forum.setText(dto.text());
        forum.setAuthor(user);
        return forum;
    }

    public Event convertToEntity(EventSaveDto dto, SportType sportType, User user) {
        Event event = new Event();
        event.setName(dto.name());
        event.setText(dto.text());
        event.setSportType(sportType);
        event.setAuthor(user);
        return event;
    }

    public News convertToEntity(NewsSaveDto dto, User user) {
        News news = new News();
        news.setName(dto.name());
        news.setText(dto.text());
        news.setAuthor(user);
        return news;
    }

    public NewsImage convertToEntity(News news, MultipartFile file, String fileName, String filePath) {
        NewsImage image = new NewsImage();
        image.setName(fileName);
        image.setType(file.getContentType());
        image.setFilePath(filePath);
        image.setNews(news);
        return image;
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

    public Coach convertToEntity(CoachSaveDto dto, SportType sportType) {
        Coach coach = new Coach();
        coach.setFirstName(dto.firstName());
        coach.setLastName(dto.lastName());
        coach.setAge(dto.age());
        coach.setPrice(dto.price());
        coach.setExperience(dto.experience());
        coach.setDescription(dto.description());
        coach.setSportType(sportType);
        return coach;
    }

    public CoachDto convertToDto(Coach coach) {
        return new CoachDto(coach.getId(), coach.getFirstName(), coach.getLastName(), coach.getAge(), coach.getExperience(), coach.getPrice(), coach.getDescription(), String.valueOf(coach.getSportType()), fetchImage(coach.getImageList()));
    }
}
