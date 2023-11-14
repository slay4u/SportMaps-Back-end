package sport_maps.nef.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import org.springframework.lang.NonNull;
import sport_maps.comments.domain.EventComment;
import sport_maps.commons.domain.EventImage;
import sport_maps.commons.domain.SportType;

import java.util.List;

@Entity
public class Event extends NewsEventForum {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SportType sportType;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EventImage> imageList;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EventComment> commentList;

    @NonNull
    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    @NonNull
    public List<EventImage> getImageList() {
        return imageList;
    }

    @NonNull
    public List<EventComment> getCommentList() {
        return commentList;
    }
}
