package com.bwf.p1_landz.entity;

import com.bwf.framework.base.BaseBean;

import java.util.List;

/**
 *
 * 获取筛选豪宅类型对象
 */
public class OnlineTypeBean extends BaseBean{

    public List<OnLineTypeResultBean> result;

    @Override
    public String toString() {
        return "OnlineTypeBean{" +
                "result=" + result +
                '}';
    }
}

