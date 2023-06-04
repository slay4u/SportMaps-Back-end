package spring.app.modules.smap.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.app.modules.smap.domain.SMap;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class TypeConverter implements AttributeConverter<SMap.Type, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SMap.Type type) {
        if (type == null) {
            return null;
        }
        return type.getCode();
    }

    @Override
    public SMap.Type convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(SMap.Type.values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
