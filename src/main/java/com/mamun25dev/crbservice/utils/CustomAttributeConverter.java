package com.mamun25dev.crbservice.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Convert
public class CustomAttributeConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return (CollectionUtils.isEmpty(strings))
                ? StringUtils.EMPTY
                : String.join(",", strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return (StringUtils.isBlank(s))
                ? Collections.emptyList()
                : Arrays.stream(s.split(",")).distinct().collect(Collectors.toList());
    }
}
