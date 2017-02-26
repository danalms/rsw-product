package com.rsw.product.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by DAlms on 12/11/16.
 *
 * This is necessary to convert between Java 8 LocalDate and java.sql.Date because the JPA 2.1 spec doesn't
 * handle Java 8 date/times.
 * There is another option to use a certain Hibernate Jar which is now deprecated and its substitution could
 * conflict with Spring Boot's version of Hibernate.
 * Writing a custom converter feels more right, esp since JPA provides the @Convert annotation
 */
@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        if (attribute == null) {
            return null;
        }
        return Date.valueOf(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.toLocalDate();
    }
}
