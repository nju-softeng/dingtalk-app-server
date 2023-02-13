package com.softeng.dingtalk.api;

import com.aliyun.dingtalkcalendar_1_0.models.*;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.softeng.dingtalk.po.DingTalkSchedulePo;
import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScheduleApi extends BaseApi{
    @Autowired
    UserService userService;
    private static com.aliyun.dingtalkcalendar_1_0.Client createCalenderClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcalendar_1_0.Client(config);
    }

    private String get_ISO0861_Time(LocalDateTime localDateTime){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DateFormatUtils.format(calendar.getTime(), pattern);
    }

    public String creatSchedule(DingTalkSchedulePo dingTalkSchedulePo) throws Exception {
        com.aliyun.dingtalkcalendar_1_0.Client client = ScheduleApi.createCalenderClient();
        CreateEventHeaders createEventHeaders = new CreateEventHeaders();
        createEventHeaders.xAcsDingtalkAccessToken = getAccessToken();
        java.util.Map<String, String> extra = TeaConverter.buildMap(
                new TeaPair("key", "{\"noChatNotification\": \"false\", \"noPushNotification\": \"false\"   }")
        );
        CreateEventRequest.CreateEventRequestOnlineMeetingInfo onlineMeetingInfo = new CreateEventRequest.CreateEventRequestOnlineMeetingInfo()
                .setType("dingtalk");
        CreateEventRequest.CreateEventRequestLocation location = new CreateEventRequest.CreateEventRequestLocation()
                .setDisplayName(dingTalkSchedulePo.getLocation());
        CreateEventRequest.CreateEventRequestReminders reminder = new CreateEventRequest.CreateEventRequestReminders()
                .setMethod("dingtalk")
                .setMinutes(60);
        CreateEventRequest.CreateEventRequestEnd end = new CreateEventRequest.CreateEventRequestEnd()
                .setDateTime(get_ISO0861_Time(dingTalkSchedulePo.getEnd()))
                .setTimeZone("Asia/Shanghai");
        CreateEventRequest.CreateEventRequestStart start = new CreateEventRequest.CreateEventRequestStart()
                .setDateTime(get_ISO0861_Time(dingTalkSchedulePo.getStart()))
                .setTimeZone("Asia/Shanghai");
        CreateEventRequest createEventRequest = new CreateEventRequest()
                .setSummary(dingTalkSchedulePo.getSummary())
                .setDescription("请准时参与（由系统创建，未签到者会被扣除AC）")
                .setStart(start)
                .setEnd(end)
                .setIsAllDay(false)
                .setLocation(location)
                .setExtra(extra)
                .setAttendees(dingTalkSchedulePo.getDingTalkScheduleDetailList().stream().map(detail -> new CreateEventRequest.CreateEventRequestAttendees()
                        .setId(userService.getUserUnionId(detail.getUser().getId()))).collect(Collectors.toList()))
                .setReminders(Collections.singletonList(reminder));
        if(dingTalkSchedulePo.isOnline()){
            createEventRequest.setOnlineMeetingInfo(onlineMeetingInfo);
        }
        try {
            CreateEventResponse createEventResponse=client.createEventWithOptions(userService.getUserUnionId(dingTalkSchedulePo.getOrganizer().getId()), "primary", createEventRequest, createEventHeaders, new RuntimeOptions());
            return createEventResponse.getBody().getId();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    public void updateSchedule(DingTalkSchedulePo dingTalkSchedulePo) throws Exception {
        com.aliyun.dingtalkcalendar_1_0.Client client = ScheduleApi.createCalenderClient();
        PatchEventHeaders patchEventHeaders = new PatchEventHeaders();
        patchEventHeaders.xAcsDingtalkAccessToken = getAccessToken();
        java.util.Map<String, String> extra = TeaConverter.buildMap(
                new TeaPair("key", "{\"noChatNotification\": \"false\", \"noPushNotification\": \"false\"   }")
        );
        PatchEventRequest.PatchEventRequestLocation location = new PatchEventRequest.PatchEventRequestLocation()
                .setDisplayName(dingTalkSchedulePo.getLocation());
        PatchEventRequest.PatchEventRequestReminders reminder = new PatchEventRequest.PatchEventRequestReminders()
                .setMethod("dingtalk")
                .setMinutes(60);
        PatchEventRequest.PatchEventRequestEnd end = new PatchEventRequest.PatchEventRequestEnd()
                .setDateTime(get_ISO0861_Time(dingTalkSchedulePo.getEnd()))
                .setTimeZone("Asia/Shanghai");
        PatchEventRequest.PatchEventRequestStart start = new PatchEventRequest.PatchEventRequestStart()
                .setDateTime(get_ISO0861_Time(dingTalkSchedulePo.getStart()))
                .setTimeZone("Asia/Shanghai");
        PatchEventRequest patchEventRequest = new PatchEventRequest()
                .setSummary(dingTalkSchedulePo.getSummary())
                .setDescription("请准时参与（由系统创建，未签到者会被扣除AC）")
                .setId(dingTalkSchedulePo.getScheduleId())
                .setStart(start)
                .setEnd(end)
                .setIsAllDay(false)
                .setLocation(location)
                .setExtra(extra)
                .setAttendees(dingTalkSchedulePo.getDingTalkScheduleDetailList().stream().map(detail -> new PatchEventRequest.PatchEventRequestAttendees()
                        .setId(userService.getUserUnionId(detail.getUser().getId()))).collect(Collectors.toList()))
                .setReminders(Collections.singletonList(reminder));
        try {
            client.patchEventWithOptions(userService.getUserUnionId(dingTalkSchedulePo.getOrganizer().getId()),
                    "primary",
                    dingTalkSchedulePo.getScheduleId(),
                    patchEventRequest,
                    patchEventHeaders,
                    new RuntimeOptions());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    public void deleteSchedule(DingTalkSchedulePo dingTalkSchedulePo) {
        try {
            com.aliyun.dingtalkcalendar_1_0.Client client = ScheduleApi.createCalenderClient();
            DeleteEventHeaders deleteEventHeaders = new DeleteEventHeaders();
            deleteEventHeaders.xAcsDingtalkAccessToken = getAccessToken();
            client.deleteEventWithOptions(userService.getUserUnionId(dingTalkSchedulePo.getOrganizer().getId()),
                    "primary",
                    dingTalkSchedulePo.getScheduleId(),
                    deleteEventHeaders,
                    new RuntimeOptions());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
    public List<String> getAbsentList(DingTalkSchedulePo dingTalkSchedulePo)  {
        try {
            com.aliyun.dingtalkcalendar_1_0.Client client = createCalenderClient();
            GetSignInListHeaders getSignInListHeaders = new GetSignInListHeaders();
            getSignInListHeaders.xAcsDingtalkAccessToken = getAccessToken();
            GetSignInListRequest getSignInListRequest = new GetSignInListRequest()
                    .setMaxResults(500L)
                    .setType("not_yet_sign_in");
            GetSignInListResponse getSignInListResponse=client.getSignInListWithOptions(userService.getUserUnionId(dingTalkSchedulePo.getOrganizer().getId()),
                    "primary",
                    dingTalkSchedulePo.getScheduleId(),
                    getSignInListRequest,
                    getSignInListHeaders,
                    new RuntimeOptions());
            return getSignInListResponse.getBody().getUsers().stream().map(GetSignInListResponseBody.GetSignInListResponseBodyUsers::getUserId).collect(Collectors.toList());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
