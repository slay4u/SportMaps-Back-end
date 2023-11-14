package sport_maps.comments.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.lang.NonNull;
import sport_maps.nef.domain.Forum;

@Entity
public class ForumComment extends Comment {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_forum", nullable = false)
    private Forum forum;

    @NonNull
    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }
}
