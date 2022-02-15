package com.softeng.dingtalk.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * @author zhanyeye
 * @description 用于枚举类型 Position 存入数据库的类型转换
 * @create 5/28/2020 8:21 AM
 */
@Converter(autoApply = true)
public class PositionConverter implements AttributeConverter<Position, String> {

    @Override
    public String convertToDatabaseColumn(Position position) {
        if (position == null) {
            return null;
        }
        return position.getTitle();
    }

    @Override
    public Position convertToEntityAttribute(String title) {
        if (title == null) {
            return null;
        }
        if (title.equals("硕士生")){
            title = "待定";
        }
        String finalTitle = title;
        return Stream.of(Position.values())
                .filter(c -> c.getTitle().equals(finalTitle))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
