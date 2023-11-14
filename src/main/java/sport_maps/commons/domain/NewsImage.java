package sport_maps.commons.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.lang.NonNull;
import sport_maps.nef.domain.News;

@Entity
public class NewsImage extends Image {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_news", nullable = false)
    private News news;

    @NonNull
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
