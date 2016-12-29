package com.bwf.framework.base;


public class BaseBean<T> {

    public String resultStatus;//接口返回码 200表示请求成功，其他表示失败

    public String resultMsg;//返回msg

    @Override
    public String toString() {
        return "BaseBean{" +
                "resultStatus='" + resultStatus + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
