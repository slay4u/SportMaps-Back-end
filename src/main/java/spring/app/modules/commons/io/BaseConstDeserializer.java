package spring.app.modules.commons.io;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import spring.app.modules.commons.constant.BaseConst;
import spring.app.modules.commons.util.JsonUtils;
import spring.app.modules.commons.util.ValidUtils;

import java.io.IOException;

/**
 * Deserializer for all const classes.
 * @param <CONST> constant class to be deserialized
 * @author Ivan Krylosov
 */
public class BaseConstDeserializer<CONST extends BaseConst> extends StdDeserializer<CONST> implements ContextualDeserializer {
    private final Class<CONST> deserializedFieldClazz;

    protected BaseConstDeserializer(Class<CONST> deserializedFieldClazz) {
        super(BaseConst.class);
        this.deserializedFieldClazz = deserializedFieldClazz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CONST deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        Integer code = JsonUtils.readSingleNode(node, "code", JsonNode::asInt, ValidUtils::isInteger);
        if (code != null) {
            return (CONST) BaseConst.getConst(code, deserializedFieldClazz);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty property) throws JsonMappingException {
        Class<CONST> rawClass = (Class<CONST>) property.getType().getRawClass();
        return new BaseConstDeserializer<>(rawClass);
    }
}
