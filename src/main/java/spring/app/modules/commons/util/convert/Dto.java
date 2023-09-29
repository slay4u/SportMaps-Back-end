package spring.app.modules.commons.util.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Dto {
    String property() default "";

    ConvertType value() default ConvertType.ORDINAL;
}
