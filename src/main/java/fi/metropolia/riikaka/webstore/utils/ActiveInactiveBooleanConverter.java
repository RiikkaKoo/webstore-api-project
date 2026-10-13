package fi.metropolia.riikaka.webstore.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ActiveInactiveBooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {

        if (attribute == null) {
            return "INACTIVE";
        }

        return attribute ? "ACTIVE" : "INACTIVE";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {

        if (dbData == null) {
            return null;
        }

        return dbData.toLowerCase().equals("active") ? true : false;
    }
}
