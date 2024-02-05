package sport_maps.security.domain;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.lang.NonNull;

import java.time.Instant;

@MappedSuperclass
public abstract class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private Instant expiryDate;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Token token = (Token) o;
        if (!getId().equals(token.getId())) return false;
        if (!getToken().equals(token.getToken())) return false;
        if (!getExpiryDate().equals(token.getExpiryDate())) return false;
        return getUser().equals(token.getUser());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
