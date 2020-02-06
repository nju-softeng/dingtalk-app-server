package com.softeng.dingtalk.vo;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 7:46 PM
 */
public class VoteResultVO {
    private int amount;
    private int accept;
    private int reject;

    public VoteResultVO(int amount, int accept) {
        this.amount = amount;
        this.accept = accept;
    }
}
