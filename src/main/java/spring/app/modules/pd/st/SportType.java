package spring.app.modules.pd.st;

import spring.app.modules.commons.constant.BaseConst;

import static spring.app.modules.pd.st.OlympicSport.*;
import static spring.app.modules.pd.st.OtherSport.*;

public class SportType extends BaseConst {
    private boolean supported;

    protected SportType(int code) {
        super(code);
    }

    protected SportType(int code, boolean supported) {
        super(code);
        this.supported = supported;
    }

    public boolean isSupported() {
        return supported;
    }

    public static SportType[] paralympics() {
        return new SportType[] {
                ARCHERY,
                BADMINTON,
                FOOTBALL,
                CANOEING,
                CYCLING,
                EQUESTRIANISM,
                JUDO,
                POWERLIFTING,
                ROWING,
                VOLLEYBALL,
                SWIMMING,
                TABLE_TENNIS,
                TAEKWONDO,
                TRIATHLONS,
                BASKETBALL,
                FENCING,
                RUGBY,
                TENNIS,
                SKIING,
                ICE_HOCKEY,
                SNOWBOARDING,
                CURLING
        };
    }

    public static SportType[] deaflympics() {
        return new SportType[] {
                BADMINTON,
                BASKETBALL,
                VOLLEYBALL,
                BOWLING,
                CYCLING,
                FOOTBALL,
                GOLF,
                HANDBALL,
                JUDO,
                KARATE,
                BMX,
                SWIMMING,
                TABLE_TENNIS,
                TAEKWONDO
        };
    }
}


