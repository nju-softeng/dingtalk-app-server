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
public interface CommonConvertorInterface<REQ, RESP, ENTITY_PO> {

    ENTITY_PO req2Entity_PO(REQ req);
    RESP entity_PO2Resp(ENTITY_PO entity);

    default <FROM, TO> List<TO> _batchTransfer(List<FROM> froms, Function<FROM, TO> transferLogic) {
        return CollectionUtils.isEmpty(froms)
                ? new ArrayList<>()
                : froms.stream().map(transferLogic).collect(Collectors.toList());
    }

    default List<ENTITY_PO> batchReq2Entity_PO(List<REQ> reqList) {
        return _batchTransfer(reqList, this::req2Entity_PO);
    }

    default List<RESP> batchEntity_PO2Resp(List<ENTITY_PO> entities) {
        return _batchTransfer(entities, this::entity_PO2Resp);
    }

}
