package com.softeng.pms.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.aliyun.tea.*;
import com.aliyun.teautil.models.*;
import com.aliyun.dingtalktodo_1_0.models.*;
import com.aliyun.teaopenapi.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用钉钉sdk待办事项API
 * 目前该模块功能不理想，暂时不用
 * @date: 2021/4/23 16:57
 */
@Slf4j
@Component
public class BacklogApi extends BaseApi {

    public com.aliyun.dingtalktodo_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalktodo_1_0.Client(config);
    }

    public void sendBacklog() throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/add");
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        req.setUserid("306147243334957616");
        req.setCreateTime(1699580799000L);
        req.setTitle("学j4习任务");
        req.setUrl("https://oa.dingtalk.com");
        req.setPcUrl("https://oa.dingtalk.com");
        List<OapiWorkrecordAddRequest.FormItemVo> list2 = new ArrayList<OapiWorkrecordAddRequest.FormItemVo>();
        OapiWorkrecordAddRequest.FormItemVo obj3 = new OapiWorkrecordAddRequest.FormItemVo();
        list2.add(obj3);
        obj3.setTitle("新人8cxxcv3学v习2");
        obj3.setContent("产品83vxcvxcv学习");
        req.setFormItemList(list2);
        req.setOriginatorUserId("306147243334957616");
        req.setSourceName("学3xcvxcv习");
        req.setPcOpenType(2L);
        req.setBizId("113j3xcv12");
        OapiWorkrecordAddResponse rsp = client.execute(req, getAccessToken());
        System.out.println(rsp.getBody());
    }
}
