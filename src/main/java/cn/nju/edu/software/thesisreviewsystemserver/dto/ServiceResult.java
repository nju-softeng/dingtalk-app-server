package cn.nju.edu.software.thesisreviewsystemserver.dto;

import lombok.Data;

/**
 * @Author: LiXiaoKang
 * @CreateTime: 2023-02-10
 * @Description: 通用返回消息
 * @Version: 1.0
 */

@Data
public class ServiceResult<T> {
    private T data;
    private Integer code;
    private String message;

    public static final int SUCCESS_CODE = 0;
    public static final int UNKNOWN_FAIL_CODE = 1;

    public static final String EMPTY_DATA = "";
    public static final String SUCCESS_EMPTY_DATA = "success";

    public static final String EMPTY_MESSAGE = "";

    public ServiceResult(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> ServiceResult<T> success(T data) {
        return new ServiceResult<>(data, SUCCESS_CODE, EMPTY_MESSAGE);
    }

    public static ServiceResult<String> success() {
        return new ServiceResult<>(SUCCESS_EMPTY_DATA, SUCCESS_CODE, EMPTY_MESSAGE);
    }

    public static ServiceResult<String> fail(String msg) {
        return new ServiceResult<>(EMPTY_DATA, UNKNOWN_FAIL_CODE, msg);
    }

    public static ServiceResult<String> fail(ServiceResult e) {
        return new ServiceResult<>(EMPTY_DATA, e.getCode(), e.getMessage());
    }

    public static ServiceResult<String> fail(Exception e) {
        return fail(e.getMessage());
    }

}
