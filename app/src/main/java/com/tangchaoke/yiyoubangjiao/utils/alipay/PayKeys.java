package com.tangchaoke.yiyoubangjiao.utils.alipay;

/**
 * Created by Administrator on 2015/9/28.
 * 提示：如何获取安全校验码和合作身份者id
 * 1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 * 2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 * 3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */
public class PayKeys {
    //
    // 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
    // 这里签名时，只需要使用生成的RSA私钥。
    // Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。\

    //合作身份者id，以2088开头的16位纯数字 此id用来支付时快速登录
    public static final String DEFAULT_PARTNER = "2088521288268813";
    //收款支付宝账号
    public static final String DEFAULT_SELLER = "wanglu1921@126.com";
    //商户私钥，自助生成，在压缩包中有openssl，用此软件生成商户的公钥和私钥，写到此处要不然服务器返回错误。公钥要传到淘宝合作账户里详情请看淘宝的sdk文档
    public static final String PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOohSVTqKN4xtvr1wgBlxXms0sVSSaKmFi9E49JaR+p80LgjwKH6Sbu0Sun1tlZ6+WauT/GUY37d6BxIl99ws5hJQ20e+NLmIUV4y2BQDX+zdcTQPTc6zZs1MPJLf/Ra8Oj/MgWy1c1lAfNOJCgbJFp8z6eF5ZkuGDF/9TX0ZGIsSyJmvflNbkaLKoJ2UNbDI9di9mj5G/aAUhsGu5h/C16dONluNCEGjNlz5OdGpiYfWoWWhea/giZBDjiU9hGlu/ExstS7KcviXIPqOcuDnZuYE+VKdZkamCJTVSTTZZbWk5y+hFJPsQXUu1SRt3zee3Ht6uthNpbwFisiaU03STAgMBAAECggEBALAZp+sbSiQcMiy7yYqtgRNUJZmrsncEuVypEui/5PHgNjqhsmN6VkI2ljEwNKc2d4l87k6ODq7ptWDoy7ijy1goqlwSDNNIfWQSvSP/HZn0SlkKdcFG6z/ZuWbFh14x2CBUBANGUWG2cDA4VMATYohEmse3v8kdCb48vGgkYXKw8zV/g/PBSzrEHW8UaYK/v5tU1/1jX2kCjFiPkgG4dwE8ihE7aRrj4BfyfQbpRyQnFIT6AcO4BNwN0aLjaQyqYtBt8V5VbBXadzN4NyXBGB4csCELOX1pvvKjICA37PmQaZnKGTDEagE8FaNQO6vYEImBwhw2wVtBlxpsVw6fzOkCgYEA/FD9TcGWOxiP0gYjWO+licBKPnefqVBwJdfh8vk7tpcZptsFigBeR7I+7BxFYzE1+q5vyB3vHRtR/DjQsrtGq67SB09Gtulpk6ftb2x+NQS4Wh8Zg6dTYfX/TrrYdvKUCUeSZmcKNyJBEgU0FXFKAwdij/21dnFkBOuXwdAlxd0CgYEA0aZakjSvp2pCV4uW8viAixhIdT0KzydoKSjQrtInG1pb55IiDTFWaOcnsf0PXFihOg7hKCz46wOvVzX9RunuOB183qlDydciQganCkJP1jQcuKJjwpxvINtRXLlh8NrkMhHE9rgMxelqmAuyaRQIdX+Otn9seR32TdEoKis9FS8CgYBUtwyVMs4yAUkohw8PKUc3POywlJFy06MT0av3XF8EKy9dqmPttbnx8JLXy5Ywe7FX/YFie0pxR177hEft+pr7wbKa6a8gcrYT8Wny40Zsnq0W9SIn5eJFHcces/VB2qiBhRpOb4IqiMG4Py8BlAv5gF1DYzeuENw4GrHVTQGhsQKBgCMQUJudZjd45SmhNhjjCRS6dfvtvq4Q6DHiGqzyhwnjHfXoEfvk4Y/gAVqaeYQ3ape8P1op02PzFNNgO0EuBOKrw+O1qZnLenZSiIOFvbaUtq0Gs+qroUZ8h+obM09GPN5G7ItbEv/S3zoY0rkSWavVLrhFLY5APZ6cObx7yNaPAoGBAPB9svm8gDz6vhUq7f21RdGLHwrQiJFEaLKq5FNjyaE/h02PIFP6o+pXBp0qPCHq4WEL410u/t7LthRqz9WmULgj34ERY0maFnwaNOVVIF8u89inut1PcOErprxvRxQCxIOtPfPbPBcSPEVz8LIxwIVvDYrbZbi9bDtu0YRpjUDm";
    //公钥
    //public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+dY2sUIcVvT51qVvp8+sie+rafXhhV5nxp7sTCvj5sf6mr3bHsFziwXyNjkgkd/U88cDGLwplK567dH1Umu5m5WzaLNhZzDPYo58fG7AAIZvouwSobmhzLpySh+vS/g++d8x9z9WCIdJcuQ5wK91j6h8nad/OKgb2/DUvU2stBwIDAQAB";
}

