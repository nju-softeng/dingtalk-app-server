package com.softeng.pms.dingtalk;

import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserGetDeptMemberRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
/**
 * 钉钉通讯录管理相关的API调用
 */
public class ContactsApi extends BaseApi{

    /**
     * 通过钉钉临时授权码获取钉钉userid
     * @param requestAuthCode 钉钉临时授权码
     * @return java.lang.String
     **/
    public String getUserId(String requestAuthCode) {
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response = executeRequest(request, "https://oapi.dingtalk.com/user/getuserinfo");
        return response.getUserid();
    }

    /**
     * 根据 userid 获取用户详细信息
     * @param userid 钉钉唯一的userid
     * @return
     */
    public OapiUserGetResponse fetchUserDetail(String userid) {
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userid);
        request.setHttpMethod("GET");
        return executeRequest(request, "https://oapi.dingtalk.com/user/get");
    }

    /**
     * 获取钉钉中所有部门id
     * @return
     */
    public List<String> listDepid() {
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setHttpMethod("GET");
        OapiDepartmentListResponse response = executeRequest(request, "https://oapi.dingtalk.com/department/list");
        return response.getDepartment().stream().map(x -> String.valueOf(x.getId())).collect(Collectors.toList());
    }

    /**
     * 获取整个部门的userid
     * @param depid
     * @return
     */
    public List<String> listUserId(String depid) {
        OapiUserGetDeptMemberRequest request = new OapiUserGetDeptMemberRequest();
        request.setDeptId(depid);
        request.setHttpMethod("GET");
        OapiUserGetDeptMemberResponse response = executeRequest(request, "https://oapi.dingtalk.com/user/getDeptMember");
        return response.getUserIds();
    }
}
