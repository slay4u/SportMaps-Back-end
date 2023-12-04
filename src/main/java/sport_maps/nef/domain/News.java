package sport_maps.nef.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import org.springframework.lang.NonNull;
import sport_maps.comments.domain.NewsComment;
import sport_maps.image.domain.NewsImage;

import java.util.List;

@Entity
public class News extends NewsEventForum {
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<NewsImage> imageList;
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<NewsComment> comments;

    @NonNull
    public List<NewsImage> getImageList() {
        return imageList;
    }

    @NonNull
    public List<NewsComment> getComments() {
        return comments;
    }
}
