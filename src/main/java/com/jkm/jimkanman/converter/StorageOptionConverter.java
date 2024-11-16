package com.jkm.jimkanman.converter;

import com.jkm.jimkanman.domain.enums.StorageOption;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** List&lt;StorageOption>과 String 간의 변환을 담당 (List를 DB에 단일 칼럼으로 저장) */
@Converter
public class StorageOptionConverter implements AttributeConverter<List<StorageOption>, String> {
    private static final String SEPERATOR = ",";
    @Override
    // List<StorageOption>을 String으로 변환
    public String convertToDatabaseColumn(List<StorageOption> storageOptions) {
        return storageOptions == null || storageOptions.isEmpty()
                ? ""
                : storageOptions.stream()
                                .map(option -> option.name()) // TODO IllegalArgumentException 시 null 처리? 혹은 RuntimeException?
                                .collect(Collectors.joining(SEPERATOR));
    }

    @Override
    // String을 List<StorageIotion>으로 변환
    public List<StorageOption> convertToEntityAttribute(String s) {
        return StringUtils.isBlank(s)
                ? List.of()
                : Arrays.stream(s.split(SEPERATOR))
                        .map(option -> StorageOption.valueOf(option))
                        .collect(Collectors.toList());
    }
}
