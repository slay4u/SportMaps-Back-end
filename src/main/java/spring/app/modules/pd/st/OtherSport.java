package spring.app.modules.pd.st;

import java.util.Map;

public class OtherSport extends SportType {
    public static final OtherSport AEROBICS = new OtherSport(0);
    // TODO: This should go to Olympic Sports
    public static final OtherSport EQUESTRIANISM = new OtherSport(1);
    public static final OtherSport KUNG_FU = new OtherSport(2);
    public static final OtherSport POWERLIFTING = new OtherSport(3);
    public static final OtherSport WRESTLING = new OtherSport(4);
    public static final OtherSport RACING = new OtherSport(5);
    public static final OtherSport BOWLING = new OtherSport(6);
    public static final OtherSport CRICKET = new OtherSport(7);
    public static final OtherSport DODGEBALL = new OtherSport(8);
    public static final OtherSport KICKBALL = new OtherSport(9);
    public static final OtherSport LACROSSE = new OtherSport(10);
    public static final OtherSport POLO = new OtherSport(11);
    public static final OtherSport RACQUETBALL = new OtherSport(12);
    public static final OtherSport ROUNDERS = new OtherSport(13);
    public static final OtherSport RUGBY = new OtherSport(14);
    public static final OtherSport SQUASH = new OtherSport(15);
    public static final OtherSport PARAGLIDING = new OtherSport(16);
    public static final OtherSport PARASAILING = new OtherSport(17);
    public static final OtherSport SKYDIVING = new OtherSport(18);
    public static final OtherSport WINDSURFING = new OtherSport(19);
    public static final OtherSport FITNESS = new OtherSport(20);
    public static final OtherSport BREAK_DANCING = new OtherSport(21);
    public static final OtherSport COMPETITIVE_DANCING = new OtherSport(22);
    public static final OtherSport CHEERLEADING = new OtherSport(23);
    public static final OtherSport DANCESPORT = new OtherSport(24);
    public static final OtherSport DRAGON_DANCE = new OtherSport(25);
    public static final OtherSport LION_DANCE = new OtherSport(26);
    public static final OtherSport FREE_RUNNING = new OtherSport(27);
    public static final OtherSport JOGGING = new OtherSport(28);
    public static final OtherSport HIGH_KICK = new OtherSport(29);
    public static final OtherSport PARKOUR = new OtherSport(30);
    public static final OtherSport STUNT = new OtherSport(31);
    public static final OtherSport TRAMPOLINING = new OtherSport(32);
    public static final OtherSport WINTER_GUARD = new OtherSport(33);
    public static final OtherSport AEROBATICS = new OtherSport(34);
    public static final OtherSport AIR_RACING = new OtherSport(35);
    public static final OtherSport BALLOONING = new OtherSport(36);
    public static final OtherSport HOPPER_BALLOONING = new OtherSport(37);
    public static final OtherSport ULTRALIGHT_AVIATION = new OtherSport(38);
    public static final OtherSport GLIDING = new OtherSport(39);
    public static final OtherSport HANG_GLIDING = new OtherSport(40);
    public static final OtherSport MODEL_AIRCRAFT = new OtherSport(41);
    public static final OtherSport PARACHUTING = new OtherSport(42);
    public static final OtherSport BANZAI_SKYDIVING = new OtherSport(43);
    public static final OtherSport BASE_JUMPING = new OtherSport(44);
    public static final OtherSport SKY_SURFING = new OtherSport(45);
    public static final OtherSport WINGSUIT_FLYING = new OtherSport(46);
    public static final OtherSport PARAMOTORING = new OtherSport(47);
    public static final OtherSport RUN_ARCHERY = new OtherSport(48);
    public static final OtherSport TARGET_ARCHERY = new OtherSport(49);
    public static final OtherSport WORKOUT = new OtherSport(50);
    public static final OtherSport FIELD_ARCHERY = new OtherSport(51);
    public static final OtherSport FLIGHT_ARCHERY = new OtherSport(52);
    public static final OtherSport GUNGDO = new OtherSport(53);
    public static final OtherSport INDOOR_ARCHERY = new OtherSport(54);
    public static final OtherSport MOUNTED_ARCHERY = new OtherSport(55);
    public static final OtherSport POPINJAY = new OtherSport(56);
    public static final OtherSport FRISBEE = new OtherSport(57);
    public static final OtherSport GAGA = new OtherSport(58);
    public static final OtherSport KEEP_AWAY = new OtherSport(59);
    public static final OtherSport KIN_BALL = new OtherSport(60);
    public static final OtherSport NEWCOMB_BALL = new OtherSport(61);
    public static final OtherSport QUIDDITCH = new OtherSport(62);
    public static final OtherSport YUKIGASSEN = new OtherSport(63);
    public static final OtherSport COASTEERING = new OtherSport(64);
    public static final OtherSport HIKING = new OtherSport(65);
    public static final OtherSport ROPE_CLIMBING = new OtherSport(66);
    public static final OtherSport POLE_CLIMBING = new OtherSport(67);
    public static final OtherSport ARTISTIC_CYCLING = new OtherSport(68);
    public static final OtherSport CYCLOCROSS = new OtherSport(69);
    public static final OtherSport CYCLE_POLO = new OtherSport(70);
    public static final OtherSport CYCLE_SPEEDWAY = new OtherSport(71);
    public static final OtherSport SKI_BOBBING = new OtherSport(72);
    public static final OtherSport UNICYCLE_HOCKEY = new OtherSport(73);
    public static final OtherSport UNICYCLE_TRIALS = new OtherSport(74);
    public static final OtherSport MOUNTAIN_UNICYCLING = new OtherSport(75);
    public static final OtherSport STREET_UNICYCLING = new OtherSport(76);
    public static final OtherSport UNICYCLING = new OtherSport(77);
    public static final OtherSport UNICYCLE_BASKETBALL = new OtherSport(78);
    public static final OtherSport FREESTYLE_WRESTLING = new OtherSport(79);
    public static final OtherSport FOLK_WRESTLING = new OtherSport(80);
    public static final OtherSport BOLI_KHELA = new OtherSport(81);
    public static final OtherSport CATCH_WRESTLING = new OtherSport(82);
    public static final OtherSport COLLAR_AND_ELBOW = new OtherSport(83);
    public static final OtherSport COLLEGIATE_WRESTLING = new OtherSport(84);
    public static final OtherSport CORNISH_WRESTLING = new OtherSport(85);
    public static final OtherSport DEVON_WRESTLING = new OtherSport(86);
    public static final OtherSport DUMOG = new OtherSport(87);
    public static final OtherSport GLIMA = new OtherSport(88);
    public static final OtherSport GOUREN = new OtherSport(89);
    public static final OtherSport KURASH = new OtherSport(90);
    public static final OtherSport MUAYTHAI = new OtherSport(91);
    public static final OtherSport PRADALSEREY = new OtherSport(92);
    public static final OtherSport SAVATE = new OtherSport(93);
    public static final OtherSport SHAOLIN_KUNG_FU = new OtherSport(94);
    public static final OtherSport WING_CHUN = new OtherSport(95);
    public static final OtherSport CHESS = new OtherSport(96);
    public static final OtherSport BAJI_QUAN = new OtherSport(97);
    public static final OtherSport BOKATOR = new OtherSport(98);
    public static final OtherSport CAPOEIRA = new OtherSport(99);
    public static final OtherSport CHESS_BOXING = new OtherSport(100);
    public static final OtherSport FUJIAN_WHITE_CRANE = new OtherSport(101);
    public static final OtherSport KICKBOXING = new OtherSport(102);
    public static final OtherSport LETHWEI = new OtherSport(103);
    public static final OtherSport SILAT = new OtherSport(104);
    public static final OtherSport SUBAK = new OtherSport(105);
    public static final OtherSport TAEKKYEON = new OtherSport(106);
    public static final OtherSport TANGSOODO = new OtherSport(107);
    public static final OtherSport VOVINAM = new OtherSport(108);
    public static final OtherSport SHINKICKING = new OtherSport(109);
    public static final OtherSport SIKARAN = new OtherSport(110);
    public static final OtherSport ICE_FOOTBALL = new OtherSport(111);
    public static final OtherSport ICE_BASEBALL = new OtherSport(112);
    public static final OtherSport ICE_YACHTING = new OtherSport(113);
    public static final OtherSport RINGETTE = new OtherSport(114);
    public static final OtherSport RINK_BALL = new OtherSport(115);
    public static final OtherSport BROOM_BALL = new OtherSport(116);
    public static final OtherSport CROKICURL = new OtherSport(117);
    public static final OtherSport KITE_BUGGY = new OtherSport(118);
    public static final OtherSport KITE_FIGHTING = new OtherSport(119);
    public static final OtherSport KITE_LANDBOARDING = new OtherSport(120);
    public static final OtherSport SNOW_KITING = new OtherSport(121);
    public static final OtherSport SPORT_KITE = new OtherSport(122);
    public static final OtherSport JOGA = new OtherSport(123);
    public static final OtherSport FUNCTIONAL_TRAINING = new OtherSport(124);
    public static final OtherSport BODYBUILDING = new OtherSport(125);
    public static final OtherSport HIIT = new OtherSport(126);
    public static final OtherSport YOGA = new OtherSport(127);
    public static final OtherSport ZUMBA = new OtherSport(128);
    public static final OtherSport BALLROOMDANCING = new OtherSport(129);
    public static final OtherSport HIPHOP = new OtherSport(130);
    public static final OtherSport MMA = new OtherSport(131);
    public static final OtherSport KRAV_MAGA = new OtherSport(132);
    public static final OtherSport WAKE_BOARDING = new OtherSport(133);
    public static final OtherSport WATER_SKIING = new OtherSport(134);
    public static final OtherSport CALISTHENICS = new OtherSport(135);
    public static final OtherSport ISOMETRICS = new OtherSport(136);
    public static final OtherSport MOUNTAINEERING = new OtherSport(137);
    public static final OtherSport ROCK_CLIMBING = new OtherSport(138);
    public static final OtherSport CROSSFIT = new OtherSport(139);
    public static final OtherSport BALLET = new OtherSport(140);
    public static final OtherSport SUPER_SQUATS = new OtherSport(141);
    public static final OtherSport ARM_WRESTLING = new OtherSport(142);
    public static final OtherSport JUMPING_ROPE = new OtherSport(143);
    public static final OtherSport POLE_DANCING = new OtherSport(144);
    public static final OtherSport STRETCHING = new OtherSport(145);
    public static final OtherSport WATER_AEROBICS = new OtherSport(146);
    public static final OtherSport TAICHI = new OtherSport(147);
    public static final OtherSport WALKING = new OtherSport(148);
    public static final OtherSport CHECKERS = new OtherSport(149);
    public static final OtherSport UNDEFINED = new OtherSport(UNDEFINED_KEY);

    protected OtherSport(int code) {
        super(code);
    }

    public static OtherSport[] all() {
        Map<Integer, OtherSport> constMap = getConstMap(OtherSport.class);
        return constMap.values().toArray(new OtherSport[0]);
    }

    public static OtherSport[] intellectualGames() {
        return new OtherSport[] {CHESS, CHECKERS};
    }

    public static OtherSport[] wrestling() {
        return new OtherSport[] {
                WRESTLING,
                CATCH_WRESTLING,
                COLLEGIATE_WRESTLING,
                COLLAR_AND_ELBOW,
                CORNISH_WRESTLING,
                DEVON_WRESTLING,
                FOLK_WRESTLING,
                FREESTYLE_WRESTLING
        };
    }

    public static OtherSport[] dancing() {
        return new OtherSport[] {
                DANCESPORT,
                COMPETITIVE_DANCING,
                DRAGON_DANCE,
                BREAK_DANCING,
                POLE_DANCING,
                LION_DANCE
        };
    }

    public static OtherSport[] martialArts() {
        return new OtherSport[] {
                KICKBOXING,
                MUAYTHAI,
                CAPOEIRA
        };
    }

    // TODO
    public static OtherSport[] handicapped() {
        return new OtherSport[] {

        };
    }
}
