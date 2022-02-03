package com.softeng.dingtalk.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * @Description 用于枚举类型 LongitudinalLevel 存入数据库的类型转换
 * @Author Jerrian Zhao
 * @Data 01/28/2022
 */

@Converter(autoApply = true)
public class LongitudinalLevelConverter implements AttributeConverter<LongitudinalLevel, String> {


    @Override
    public String convertToDatabaseColumn(LongitudinalLevel longitudinalLevel) {
        if (longitudinalLevel == null) {
            return null;
        }
        return longitudinalLevel.getTitle();
    }

    @Override
    public LongitudinalLevel convertToEntityAttribute(String title) {
        if (title == null) {
            return null;
        }
        return Stream.of(LongitudinalLevel.values()).filter(c -> c.getTitle().equals(title)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
