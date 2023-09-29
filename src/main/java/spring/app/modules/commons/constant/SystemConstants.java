package spring.app.modules.commons.constant;

import java.math.BigDecimal;

public final class SystemConstants {
    public static final String IMG_FOLDER_NAME = "images";
    public static final int PAGE_ELEMENTS_AMOUNT = 15;
    public static final String FIRST_NAME_REGEXP = "^(?=.{2,30}$)[A-Z][a-zA-Z]*(?:\\h+[A-Z][a-zA-Z]*)*$";
    public static final String LAST_NAME_REGEXP = "^(?=.{2,30}$)[A-Z][a-zA-Z]*(?:\\h+[A-Z][a-zA-Z]*)*$";
    public static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[{}:#@!;\\[_'`\\],\".\\/~?*\\-$^+=\\\\<>]).{8,20}$";
    public static final String EMAIL_REGEXP = "^(?=.{1,32}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    // These messages should be moved from here
    public static final String INTERNAL_ERROR = "Internal error while processing.";
    public static final String ENUM_ERROR = "Enum constant not found ";
    public static final String AGE_INVALID = "Age is invalid for ";
    public static final String SERIALIZATION_ERROR = "Serialization error for ";
    public static final String NOT_FOUND_ERROR = "Not found ";

    public static final int MIN_AGE = 18;
    public static final int MAX_AGE = 99;
    public static final int MIN_COACH_AGE = 25;
    public static final int MAX_COACH_AGE = 99;
    public static final int MIN_EXP = 1;
    public static final int MAX_EXP = 74;
    public static final BigDecimal MIN_PRICE = BigDecimal.valueOf(650.0);
    public static final BigDecimal MAX_PRICE = BigDecimal.valueOf(3400.0);
    public static final int MAX_SPORT_TYPES = 4;
    public static final int EXPECTED_EMPLOYMENT_AGE = 21;
    public static final int MIN_EMPLOYMENT_AGE = 18;
}
