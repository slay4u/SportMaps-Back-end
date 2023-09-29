package spring.app.modules.pd.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import spring.app.modules.promoter.CoachRequirements;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Type type;
    // TODO: needs rework
    private String qualification;
    private Degree degree;
    private String issuingAuthority;
    private LocalDate dateOfIssuing;
    @ManyToOne
    private CoachRequirements coachReq;

    public enum Type {
        HIGHER(0, Set.of(Degree.BACHELOR, Degree.MASTER)),
        SECONDARY(1),
        VOCATIONAL(2),
        UNDEFINED(-1);

        private final int code;
        private final Set<Degree> requiredDegree;

        Type(int code) {
            this.code = code;
            requiredDegree = null;
        }

        Type(int code, Set<Degree> requiredDegree) {
            this.code = code;
            this.requiredDegree = requiredDegree;
        }

        public int getCode() {
            return code;
        }

        public Set<Degree> getRequiredDegree() {
            return requiredDegree;
        }

        @JsonCreator
        public static Type deserialize(int code) {
            for (Type type : Type.values()) {
                if (type.getCode() == code) {
                    return type;
                }
            }
            return UNDEFINED;
        }

        @JsonValue
        public Integer serialize() {
            return this.code;
        }
    }

    public enum Degree {
        BACHELOR(0),
        MASTER(1),
        UNDEFINED(-1);

        private final int code;

        Degree(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @JsonCreator
        public static Degree deserialize(int code) {
            for (Degree degree : Degree.values()) {
                if (degree.getCode() == code) {
                    return degree;
                }
            }
            return UNDEFINED;
        }

        @JsonValue
        public Integer serialize() {
            return this.code;
        }
    }
}
