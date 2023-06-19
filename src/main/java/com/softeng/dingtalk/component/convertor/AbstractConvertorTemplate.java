package com.softeng.dingtalk.component.convertor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: LiXiaoKang
 * @CreateTime: 2023-02-08
 * @Description: 对象转换器的模版类，实现init方法register特殊字段逻辑就可以使用
 * @Version: 1.0
 */

public abstract class AbstractConvertorTemplate<REQ, RESP, ENTITY> implements CommonConvertorInterface<REQ, RESP, ENTITY> {

    private final CommonConvertorHelper<REQ, ENTITY> req2entityHelper;
    private final CommonConvertorHelper<ENTITY, RESP> entity2respHelper;

    public AbstractConvertorTemplate() {
        // 被继承后可以获取自身类型参数
        Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        Class<REQ> reqClass = (Class<REQ>) types[0];
        Class<RESP> respClass = (Class<RESP>) types[1];
        Class<ENTITY> entityClass = (Class<ENTITY>) types[2];
        req2entityHelper = new CommonConvertorHelper<>(reqClass, entityClass);
        entity2respHelper = new CommonConvertorHelper<>(entityClass, respClass);
        req2EntityLogicRegister(req2entityHelper);
        entity2RespLogicRegister(entity2respHelper);
    }

    /**
     * 留四个钩子，在这把四个helper的例外转换逻辑register进去
     * 没有特殊逻辑就不用管了
     */
    protected void req2EntityLogicRegister(CommonConvertorHelper<REQ, ENTITY> helper) {}
    protected void entity2RespLogicRegister(CommonConvertorHelper<ENTITY, RESP> helper) {}


    @Override
    public ENTITY req2Entity(REQ req) {
        return req2entityHelper.convert(req);
    }


    @Override
    public RESP entity2Resp(ENTITY entity) {
        return entity2respHelper.convert(entity);
    }
}
