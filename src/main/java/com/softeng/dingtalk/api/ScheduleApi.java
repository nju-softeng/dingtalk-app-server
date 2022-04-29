package com.softeng.dingtalk.api;

import com.aliyun.dingtalkcalendar_1_0.models.CreateEventHeaders;
import com.aliyun.dingtalkcalendar_1_0.models.CreateEventRequest;
import com.aliyun.dingtalkcalendar_1_0.models.CreateEventResponse;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;

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

    private String get_ISO0861_Time(LocalDate localDate){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -8);
        return DateFormatUtils.format(calendar.getTime(), pattern);
    }

    public String creatSchedule(DingTalkSchedule dingTalkSchedule) throws Exception {
        com.aliyun.dingtalkcalendar_1_0.Client client = ScheduleApi.createCalenderClient();
        CreateEventHeaders createEventHeaders = new CreateEventHeaders();
        createEventHeaders.xAcsDingtalkAccessToken = getAccessToken();
        java.util.Map<String, String> extra = TeaConverter.buildMap(
                new TeaPair("key", "{\"noChatNotification\": \"false\", \"noPushNotification\": \"false\"   }")
        );
        CreateEventRequest.CreateEventRequestOnlineMeetingInfo onlineMeetingInfo = new CreateEventRequest.CreateEventRequestOnlineMeetingInfo()
                .setType("dingtalk");
        CreateEventRequest.CreateEventRequestLocation location = new CreateEventRequest.CreateEventRequestLocation()
                .setDisplayName(dingTalkSchedule.getLocation());
        CreateEventRequest.CreateEventRequestReminders reminder = new CreateEventRequest.CreateEventRequestReminders()
                .setMethod("dingtalk")
                .setMinutes(60);
        CreateEventRequest.CreateEventRequestEnd end = new CreateEventRequest.CreateEventRequestEnd()
                .setDateTime(get_ISO0861_Time(dingTalkSchedule.getEnd()))
                .setTimeZone("Asia/Shanghai");
        CreateEventRequest.CreateEventRequestStart start = new CreateEventRequest.CreateEventRequestStart()
                .setDateTime(get_ISO0861_Time(dingTalkSchedule.getStart()))
                .setTimeZone("Asia/Shanghai");
        CreateEventRequest createEventRequest = new CreateEventRequest()
                .setSummary("test event")
                .setDescription("something about this event")
                .setStart(start)
                .setEnd(end)
                .setIsAllDay(false)
                .setLocation(location);
        if(dingTalkSchedule.isOnline()){
            createEventRequest.setOnlineMeetingInfo(onlineMeetingInfo);
        }
        try {
            CreateEventResponse createEventResponse=client.createEventWithOptions(userService.getUserUnionId(dingTalkSchedule.getUser().getId()), "primary", createEventRequest, createEventHeaders, new RuntimeOptions());
            return createEventResponse.getBody().getId();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
