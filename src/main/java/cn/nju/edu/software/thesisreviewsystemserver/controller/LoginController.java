package cn.nju.edu.software.thesisreviewsystemserver.controller;

import cn.nju.edu.software.thesisreviewsystemserver.component.dingApi.DingExpertGroupApi;
import cn.nju.edu.software.thesisreviewsystemserver.component.dingApi.DingStudentGroupApi;
import cn.nju.edu.software.thesisreviewsystemserver.dto.ServiceResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *  第三方企业应用免登
 * @author openapi@dingtalk
 * 2020-11-3
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    @Resource
    private DingStudentGroupApi dingStudentGroupApi;
    @Resource
    private DingExpertGroupApi dingExpertGroupApi;

    /**
     * 欢迎页面，通过 /welcome 访问，判断后端服务是否启动
     *
     * @return 字符串 welcome
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     *
     * @param requestAuthCode
     * @return
     * ServiceResult
     */
    @RequestMapping(value = "/student/login", method = RequestMethod.GET)
    public ServiceResult<?> loginForStudent(@RequestParam("authCode") String requestAuthCode) throws Exception {
        return login(requestAuthCode, dingStudentGroupApi);
    }

    /**
     *
     * @param requestAuthCode
     * @return
     * ServiceResult
     */
    @RequestMapping(value = "/expert/login", method = RequestMethod.GET)
    public ServiceResult<?> loginForExpert (@RequestParam("authCode") String requestAuthCode) throws Exception {
        return login(requestAuthCode, dingExpertGroupApi);
    }

    private ServiceResult<?> login(@RequestParam("authCode") String requestAuthCode, DingExpertGroupApi dingExpertGroupApi) throws Exception {
        String userId = dingExpertGroupApi.getUserId(requestAuthCode);
        if(userId == null) return ServiceResult.fail("登录失败");
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("userId", userId);
        return ServiceResult.success(returnMap);
    }
}