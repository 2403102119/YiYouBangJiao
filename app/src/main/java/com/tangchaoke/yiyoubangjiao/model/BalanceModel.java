package com.tangchaoke.yiyoubangjiao.model;

/**
 * Created by Administrator on 2018/3/24.
 */

public class BalanceModel {

//    {
//        balance=用户余额
//        "integral":0,
//        status=状态
//        message= 提示信息
//    }

    private String integral;
    private String balance;
    private String status;
    private String message;

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
