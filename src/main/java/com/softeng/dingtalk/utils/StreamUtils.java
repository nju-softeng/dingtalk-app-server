package com.softeng.dingtalk.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: LiXiaoKang
 * @CreateTime: 2023-02-09
 */

public class StreamUtils {

    public static <FROM, TO> List<TO> map(List<FROM> froms, Function<? super FROM, ? extends TO> transferLogic) {
        return CollectionUtils.isEmpty(froms)
                ? new ArrayList<>()
                : froms.stream().map(transferLogic).collect(Collectors.toList());
    }

    public static <T> void apply(List<T> list, Consumer<T> consumer) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(consumer);
    }

}
