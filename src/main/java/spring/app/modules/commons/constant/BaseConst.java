package spring.app.modules.commons.constant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import spring.app.modules.commons.io.BaseConstDeserializer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for all classes with predefined constants.
 * @author Ivan Krylosov
 */
@JsonDeserialize(using = BaseConstDeserializer.class)
public abstract class BaseConst {
    protected int code;
    public static final Map<String, Map<Integer, BaseConst>> ALL_MAPS = new LinkedHashMap<>();
    protected static final int UNDEFINED_KEY = -1;

    protected BaseConst(int code) {
        this.code = code;
        getConstMap(this.getClass()).put(code, this);
    }

    @SuppressWarnings("unchecked")
    public static <CONST extends BaseConst> Map<Integer, CONST> getConstMap(Class<?> clazz) {
        Map<Integer, CONST> res;
        if (clazz == null || !BaseConst.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException(SystemConstants.INTERNAL_ERROR + "While trying to get from: " + clazz);
        }
        if (!ALL_MAPS.containsKey(clazz.getName())) {
            initConst(clazz);
        }
        if ((res = (Map<Integer, CONST>) ALL_MAPS.get(clazz.getName())) == null) {
            res = new HashMap<>();
            ALL_MAPS.put(clazz.getName(), (Map<Integer, BaseConst>) res);
        }
        return res;
    }

    public static BaseConst getConst(int code, Class<? extends BaseConst> clazz) {
        Map<Integer, BaseConst> constMap = getConstMap(clazz);
        BaseConst res = constMap.remove(code);
        return res == null ? constMap.remove(UNDEFINED_KEY) : res;
    }

    /**
     * This is used to force JVM to load the static final instances and therefore put them into ALL_MAPS
     */
    private static void initConst(Class<?> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length > 0) {
                for (Field f: fields) {
                    if ((f.getModifiers() & 25) == 25) {
                        f.get(null);
                        break;
                    }
                }
            } else {
                throw new IllegalStateException("No fields on class " + clazz);
            }
        } catch (Exception e) {
            throw new IllegalStateException(SystemConstants.INTERNAL_ERROR + e.getMessage());
        }
    }

    public int getCode() {
        return code;
    }
    //...
}
