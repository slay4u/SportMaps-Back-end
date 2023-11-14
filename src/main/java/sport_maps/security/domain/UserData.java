package sport_maps.security.domain;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;
    private LocalDateTime lastSeen;
    private String destinationUrl;
    @Enumerated(EnumType.ORDINAL)
    private OperatingSystem os;
    @Enumerated(EnumType.ORDINAL)
    private Browser browser;
    private String browserVersion;
}
