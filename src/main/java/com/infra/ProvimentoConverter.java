package com.infra;

import com.domains.enums.Provimento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ProvimentoConverter implements AttributeConverter<Provimento, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Provimento provimento) {
        return provimento == null ? null : provimento.getId();
    }
    @Override
    public Provimento convertToEntityAttribute(Integer dbValue) {
        return Provimento.toEnum(dbValue);
    }
}
