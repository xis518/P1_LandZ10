package com.bwf.p1_landz.entity;

import java.io.Serializable;
import java.util.List;


public class OnLineTypeResultBean implements Serializable{
    public String paramType;
    public List<ParamListBean> paramList;

    @Override
    public String toString() {
        return "OnLineTypeResultBean{" +
                "paramType='" + paramType + '\'' +
                ", paramList=" + paramList +
                '}';
    }
}
