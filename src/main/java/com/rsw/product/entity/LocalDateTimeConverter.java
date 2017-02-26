package com.rsw.product.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by DAlms on 12/11/16.
 *
 * This is necessary to convert between Java 8 LocalDateTime and java.sql.Timestamp because the JPA 2.1 spec doesn't
 * handle Java 8 date/times.
 * There is another option to use a certain Hibernate Jar which is now deprecated and its substitution could
 * conflict with Spring Boot's version of Hibernate.
 * Writing a custom converter feels more right, esp since JPA provides the @Convert annotation
 */
@Converter
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return Timestamp.valueOf(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toLocalDateTime();
    }
}
