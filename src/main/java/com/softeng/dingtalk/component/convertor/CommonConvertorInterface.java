package com.softeng.dingtalk.component.convertor;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: LiXiaokang
 * @CreateTime: 2023-02-08
 * @Description: 微服务里四层对象转换器的接口
 * @Version: 1.0
 */
public interface CommonConvertorInterface<REQ, RESP, ENTITY> {

    ENTITY req2Entity(REQ req);
    RESP entity2Resp(ENTITY entity);

    default <FROM, TO> List<TO> _batchTransfer(List<FROM> froms, Function<FROM, TO> transferLogic) {
        return CollectionUtils.isEmpty(froms)
                ? new ArrayList<>()
                : froms.stream().map(transferLogic).collect(Collectors.toList());
    }

    default List<ENTITY> batchReq2Entity(List<REQ> reqList) {
        return _batchTransfer(reqList, this::req2Entity);
    }

    default List<RESP> batchEntity2Resp(List<ENTITY> entities) {
        return _batchTransfer(entities, this::entity2Resp);
    }

}
