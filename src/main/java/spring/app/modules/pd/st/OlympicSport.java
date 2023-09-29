package spring.app.modules.pd.st;

import java.util.Map;

public class OlympicSport extends SportType {
    public static final OlympicSport KAYAKING = new OlympicSport(0);
    public static final OlympicSport BOB_SLEIGHING = new OlympicSport(1);
    public static final OlympicSport CANOEING = new OlympicSport(2);
    public static final OlympicSport SKIING = new OlympicSport(3);
    public static final OlympicSport SURFING = new OlympicSport(4);
    public static final OlympicSport SNORKELING = new OlympicSport(5);
    public static final OlympicSport SWIMMING = new OlympicSport(6);
    public static final OlympicSport ROWING = new OlympicSport(7);
    public static final OlympicSport ARCHERY = new OlympicSport(8);
    public static final OlympicSport GYMNASTICS = new OlympicSport(9);
    public static final OlympicSport BOXING = new OlympicSport(10);
    public static final OlympicSport RUNNING = new OlympicSport(11);
    public static final OlympicSport CYCLING = new OlympicSport(12);
    public static final OlympicSport DISCUS_THROW = new OlympicSport(13);
    public static final OlympicSport FENCING = new OlympicSport(14);
    public static final OlympicSport FIGURE_SKATING = new OlympicSport(15);
    public static final OlympicSport JUDO = new OlympicSport(16);
    public static final OlympicSport JUJITSU = new OlympicSport(17);
    public static final OlympicSport TAEKWONDO = new OlympicSport(18);
    public static final OlympicSport KARATE = new OlympicSport(19);
    public static final OlympicSport LONG_JUMP = new OlympicSport(20);
    public static final OlympicSport POLE_VAULT = new OlympicSport(21);
    public static final OlympicSport BASEBALL = new OlympicSport(22);
    public static final OlympicSport BASKETBALL = new OlympicSport(23);
    public static final OlympicSport TENNIS = new OlympicSport(24);
    public static final OlympicSport BADMINTON = new OlympicSport(25);
    public static final OlympicSport CURLING = new OlympicSport(26);
    public static final OlympicSport FOOTBALL = new OlympicSport(27);
    public static final OlympicSport GOLF = new OlympicSport(28);
    public static final OlympicSport HANDBALL = new OlympicSport(29);
    public static final OlympicSport HOCKEY = new OlympicSport(30);
    public static final OlympicSport ICE_HOCKEY = new OlympicSport(31);
    public static final OlympicSport HIGH_JUMP = new OlympicSport(32);
    public static final OlympicSport TABLE_TENNIS = new OlympicSport(33);
    public static final OlympicSport VOLLEYBALL = new OlympicSport(34);
    public static final OlympicSport WATER_POLO = new OlympicSport(35);
    public static final OlympicSport KITESURFING = new OlympicSport(36);
    public static final OlympicSport SKATEBOARDING = new OlympicSport(37);
    public static final OlympicSport SNOWBOARDING = new OlympicSport(38);
    public static final OlympicSport JAVELIN = new OlympicSport(39);
    public static final OlympicSport BMX = new OlympicSport(40);
    public static final OlympicSport SPRINTING = new OlympicSport(41);
    public static final OlympicSport MARATHON_RUNNING = new OlympicSport(42);
    public static final OlympicSport HURDLES = new OlympicSport(43);
    public static final OlympicSport DIVING = new OlympicSport(44);
    public static final OlympicSport TRIATHLONS = new OlympicSport(45);
    public static final OlympicSport UNDEFINED = new OlympicSport(UNDEFINED_KEY);

    protected OlympicSport(int code) {
        super(code);
    }

    public static OlympicSport[] all() {
        Map<Integer, OlympicSport> constMap = getConstMap(OlympicSport.class);
        return constMap.values().toArray(new OlympicSport[0]);
    }

    public static OlympicSport[] martialArts() {
        return new OlympicSport[] {
                JUDO,
                JUJITSU,
                TAEKWONDO,
                KARATE
        };
    }
}
