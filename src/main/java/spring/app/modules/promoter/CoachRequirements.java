package spring.app.modules.promoter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import spring.app.modules.commons.domain.Identifier;
import spring.app.modules.pd.EQF;
import spring.app.modules.pd.st.SportType;
import spring.app.modules.pd.domain.Education;

import java.util.List;

@Entity
public class CoachRequirements extends Identifier {
    /**
     * {@link SportType}
     */
    private String sportType;
    private int minAge;
    private int maxAge;
    private int minExperience; // min experience
    /**
     * {@link EQF}
     */
    private String eqf;
    @OneToMany(mappedBy = "coachReq", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Education> education;

    private String sportTitle;

}
