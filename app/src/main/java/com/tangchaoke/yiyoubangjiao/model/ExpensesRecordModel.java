package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class ExpensesRecordModel {

//    {
//        model =[
//          {
//              .........
//	        }
//        ]
//        status=状态
//        message= 提示信息
//    }

    private List<BalanceModelModel> model;
    private String status;
    private String message;

    public List<BalanceModelModel> getModel() {
        return model;
    }

    public void setModel(List<BalanceModelModel> model) {
        this.model = model;
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

    public class BalanceModelModel{

//        oid = 余额明细表oid
//        consumptionContent = 消费内容
//        amountMoney = 金额
//        integral = 积分值
//        type = 消费类型:0充值 1提现 2消费
//        operationTime = 操作时间
//        balanceStatus = 提现状态：0提现中 1提现失败 2提现成功

        private String oid;
        private String consumptionContent;
        private String type;
        private String operationTime;
        private String balanceStatus;
        private String amountMoney;
        private String integral;

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getBalanceStatus() {
            return balanceStatus;
        }

        public void setBalanceStatus(String balanceStatus) {
            this.balanceStatus = balanceStatus;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getConsumptionContent() {
            return consumptionContent;
        }

        public void setConsumptionContent(String consumptionContent) {
            this.consumptionContent = consumptionContent;
        }

        public String getAmountMoney() {
            return amountMoney;
        }

        public void setAmountMoney(String amountMoney) {
            this.amountMoney = amountMoney;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOperationTime() {
            return operationTime;
        }

        public void setOperationTime(String operationTime) {
            this.operationTime = operationTime;
        }
    }

}
