package com.softeng.dingtalk.component.dingApi;

import com.dingtalk.api.request.OapiReportListRequest;
import com.dingtalk.api.response.OapiReportListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softeng.dingtalk.constant.DingApiUrlConstant.REPORT_SIMPLE_API_URL;

/**
 * 钉钉日志相关的api
 */
@Slf4j
@Component
public class ReportApi extends BaseApi{
    /**
     * 获取用户指定时间内的周报信息
     * @param userid 钉钉userid
     * @param startTime 查询周报的开始时间
     * @param endTime 查询周报的结束时间
     * @return
     */
    public Map getReport(String userid, LocalDateTime startTime, LocalDateTime endTime) {
        OapiReportListRequest request = new OapiReportListRequest();
        request.setUserid(userid);
        request.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        request.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        request.setCursor(0L);
        request.setSize(1L);

        OapiReportListResponse response = executeRequest(request, "https://oapi.dingtalk.com/topapi/report/list");

        if (!response.isSuccess()) throw new RuntimeException("调用钉钉日志接口失败，或无周报信息");

        List<OapiReportListResponse.ReportOapiVo> dataList = response.getResult().getDataList();
        return dataList.size() == 0 ? Map.of() : Map.of("contents", dataList.get(0).getContents().stream()
                .filter(x -> !x.getValue().isEmpty())
                .collect(Collectors.toList()));
    }

    /**
     * 获取指定时间段内size条日志中的userid集合，用于初筛未提交周报者
     * @param startTime
     * @param endTime
     * @param size
     * @return
     */
    public Set<String> getSimpleReport(LocalDateTime startTime, LocalDateTime endTime, long size) {
        OapiReportListRequest req = new OapiReportListRequest();
        req.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        req.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        req.setCursor(0L);
        req.setSize(size);

        OapiReportListResponse response = executeRequest(req, REPORT_SIMPLE_API_URL);

        if (!response.isSuccess()) {
            throw new RuntimeException("调用钉钉日志接口失败，或无周报信息");
        }

        return response.getResult().getDataList().stream()
                .map(OapiReportListResponse.ReportOapiVo::getCreatorId)
                .collect(Collectors.toSet());
    }
}
