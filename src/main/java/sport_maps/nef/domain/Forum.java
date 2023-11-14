package sport_maps.nef.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import org.springframework.lang.NonNull;
import sport_maps.comments.domain.ForumComment;

import java.util.List;

@Entity
public class Forum extends NewsEventForum {
    @OneToMany(mappedBy = "forum", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ForumComment> commentList;

    @NonNull
    public List<ForumComment> getCommentList() {
        return commentList;
    }
}
