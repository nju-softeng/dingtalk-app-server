package com.softeng.dingtalk.convertor;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LiXiaoKang
 * @CreateTime: 2023-02-08
 * @Description: 处理两个需要转换类的信息，调用方法进行转换
 * @Version: 1.0
 */

public class CommonConvertorHelper<FROM, TO> {

    /**
     * 用户指定的转换
     */
    private final List<Triple<Field, Field, FieldConvertor<?, ?>>> fieldTriples = new ArrayList<>();

    private final Class<FROM> fromClass;
    private final Class<TO> toClass;

    public CommonConvertorHelper(Class<FROM> fromClass, Class<TO> toClass) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        // 存储可以直接转换的字段 名字相同 && 类型相同
        Field[] srcFields = fromClass.getDeclaredFields();
        Field[] tgtFields = toClass.getDeclaredFields();
        for (Field srcField : srcFields) {
            for (Field tgtField : tgtFields) {
                if (srcField.getName().equals(tgtField.getName()) && srcField.getType().equals(tgtField.getType())) {
                    fieldTriples.add(Triple.of(srcField, tgtField, FieldConvertor.SAME_FIELD_CONVERTOR));
                    break;
                }
            }
        }
    }

    @SneakyThrows
    public <FIELD1, FIELD2> CommonConvertorHelper<FROM, TO> register(Field srcField, Field tgtField, FieldConvertor<FIELD1, FIELD2> fieldConvertor) {
        // 1. 参数校验
        assert fromClass.getDeclaredField(srcField.getName()).getType().equals(srcField.getType())
                && toClass.getDeclaredField(tgtField.getName()).getType().equals(tgtField.getType());
        ParameterizedType parameterizedType = (ParameterizedType) fieldConvertor.getClass().getGenericInterfaces()[0];
        assert parameterizedType.getActualTypeArguments()[0].equals(srcField.getType())
                && parameterizedType.getActualTypeArguments()[1].equals(tgtField.getType());
        // 2. 加入列表
        this.fieldTriples.add(Triple.of(srcField, tgtField, fieldConvertor));
        return this;
    }

    @SneakyThrows
    public <FIELD1, FIELD2> CommonConvertorHelper<FROM, TO> register(String srcFieldName, String tgtFieldName, FieldConvertor<FIELD1, FIELD2> fieldConvertor) {
        return register(fromClass.getDeclaredField(srcFieldName), toClass.getDeclaredField(tgtFieldName), fieldConvertor);
    }

    @SneakyThrows
    public TO convert(FROM from) {
        if (from == null) {
            return null;
        }
        TO to = toClass.getDeclaredConstructor().newInstance();
        for (Triple<Field, Field, FieldConvertor<?, ?>> triple : fieldTriples) {
            Field srcField = triple.getLeft();
            Field tgtField = triple.getMiddle();
            FieldConvertor fieldConvertor = triple.getRight();
            boolean srcAcc = srcField.canAccess(from);
            boolean tgtAcc = tgtField.canAccess(to);
            srcField.setAccessible(true);
            tgtField.setAccessible(true);
            tgtField.set(to, fieldConvertor.convert(srcField.get(from)));
            srcField.setAccessible(srcAcc);
            tgtField.setAccessible(tgtAcc);
        }
        return to;
    }

    @FunctionalInterface
    public interface FieldConvertor<FROM, TO> {
        TO convert(FROM from);

        /**
         * 不能写成lambda，否则获取不到泛型参数
         */
        FieldConvertor<?, ?> SAME_FIELD_CONVERTOR = from -> from;
//        FieldConvertor<String, ClassifierEnum> STRING_CLASSIFIER_ENUM_FIELD_CONVERTOR = new FieldConvertor<String, ClassifierEnum>() {
//            @Override
//            public ClassifierEnum convert(String s) {
//                return ClassifierEnum.fromString(s);
//            }
//        };
//        FieldConvertor<ClassifierEnum, String> CLASSIFIER_ENUM_STRING_FIELD_CONVERTOR = new FieldConvertor<ClassifierEnum, String>() {
//            @Override
//            public String convert(ClassifierEnum classifierEnum) {
//                return classifierEnum.toString();
//            }
//        };

    }

}
