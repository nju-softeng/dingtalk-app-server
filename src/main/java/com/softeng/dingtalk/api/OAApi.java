package com.softeng.dingtalk.api;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessInstanceTerminateRequest;
import com.dingtalk.api.request.OapiProcessWorkrecordCreateRequest;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.response.OapiProcessInstanceTerminateResponse;
import com.dingtalk.api.response.OapiProcessWorkrecordCreateResponse;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.softeng.dingtalk.entity.AbsentOA;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.UserService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class OAApi extends BaseApi{
    @Autowired
    UserService userService;
    @Value("${OA.askForLeaveProcessCode}")
    private String  absentOAProcessCode;
    public String createAbsentOA(AbsentOA absentOA) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/create");
            OapiProcessinstanceCreateRequest request= new OapiProcessinstanceCreateRequest();
            request.setAgentId(BaseApi.AGENTID);
            //设置process code
            request.setProcessCode(absentOAProcessCode);
            request.setOriginatorUserId(userService.getUserid(absentOA.getDingTalkSchedule().getOrganizer().getId()));
            request.setDeptId(-1L);
            //设置表单内容
            List<OapiProcessinstanceCreateRequest.FormComponentValueVo> form = new ArrayList<>();
            OapiProcessinstanceCreateRequest.FormComponentValueVo type = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
            form.add(type);
            type.setName("请假类型");
            type.setValue(absentOA.getType());
            OapiProcessinstanceCreateRequest.FormComponentValueVo start = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
            form.add(start);
            type.setName("开始时间");
            type.setValue(absentOA.getStart().toString());
            OapiProcessinstanceCreateRequest.FormComponentValueVo end = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
            form.add(end);
            type.setName("结束时间");
            type.setValue(absentOA.getEnd().toString());
            OapiProcessinstanceCreateRequest.FormComponentValueVo dayNum = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
            form.add(dayNum);
            type.setName("结束时间");
            type.setValue(absentOA.getDayNum().toString());
            OapiProcessinstanceCreateRequest.FormComponentValueVo reason = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
            form.add(reason);
            type.setName("请假缘由");
            type.setValue(absentOA.getReason());
            request.setFormComponentValues(form);
            //设置审批人
            List<OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo> processInstanceApproverVoList = new ArrayList<>();
            OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo processInstanceApproverVo = new OapiProcessinstanceCreateRequest.ProcessInstanceApproverVo();
            processInstanceApproverVoList.add(processInstanceApproverVo);
            request.setApprovers(absentOA.getDingTalkSchedule().getOrganizer().getUserid());
            OapiProcessinstanceCreateResponse rsp = client.execute(request, getAccessToken());
            return rsp.getProcessInstanceId();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
    public boolean getOAOutCome(String processInstanceId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
            OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
            req.setProcessInstanceId(processInstanceId);
            OapiProcessinstanceGetResponse rsp = client.execute(req, getAccessToken());
            if(rsp.getProcessInstance().getStatus()=="COMPLETED" && rsp.getProcessInstance().getResult()=="agree") return true;
            return false;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }
    public boolean deleteAbsentOA(String processInstanceId, User user){
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/process/instance/terminate");
            OapiProcessInstanceTerminateRequest req = new OapiProcessInstanceTerminateRequest();
            OapiProcessInstanceTerminateRequest.TerminateProcessInstanceRequestV2 processInstanceRequestV2 = new OapiProcessInstanceTerminateRequest.TerminateProcessInstanceRequestV2();
            processInstanceRequestV2.setProcessInstanceId(processInstanceId);
            processInstanceRequestV2.setIsSystem(false);
            processInstanceRequestV2.setRemark("取消请假");
            processInstanceRequestV2.setOperatingUserid(user.getUserid());
            req.setRequest(processInstanceRequestV2);
            OapiProcessInstanceTerminateResponse rsp = client.execute(req,getAccessToken());
            return rsp.getResult();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
