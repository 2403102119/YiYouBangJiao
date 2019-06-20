package com.tangchaoke.yiyoubangjiao.model;

import java.util.List;

/**
 * Created by Administrator on 2018/4/7.
 */

public class CalculationModel {

//    {
//　　"money":[{
//         ...........
// }],
//　　"status":1,
//　　"message":"获取问题起始金额成功!"
//    }

    private List<CalculationMoneyModel> money;
    private String status;
    private String message;

    public List<CalculationMoneyModel> getMoney() {
        return money;
    }

    public void setMoney(List<CalculationMoneyModel> money) {
        this.money = money;
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

    public class CalculationMoneyModel{

//        　"money":8

        private String money;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }

}
