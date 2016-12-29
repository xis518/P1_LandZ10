package com.bwf.p1_landz.request;

import android.text.TextUtils;

import com.bwf.framework.tools.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Description: 在线房源请求参数
 */
public class OnLineHouseRequest implements Serializable{

    public String resblockName;//楼盘名称

    public String districtId;//城区ID

    public String circleTypeCode;//商圈ID

    public String totalPricesBegin;//总价开始

    public String totalPricesEnd;//总价结束
    /**
     * 排序规则： 0默认排序 1面积从大到小（降序） 2面积从小到大（升序） 3总价从低到高（升序） 4总价从高到低（降序）
     * 5关注度从高到低（降序）6单价从高到低（降序）
     */
    public String sort;

    public String totalShowing;//关注度：1关注度降序
    public String bedroomAmount;//室数
    public String buildSizeBegin;//建筑面积开始
    public String buildSizeEnd;//建筑面积结束
    public String feature;//特色类型：1今日可看房(二手房源时间戳) 2是否钥匙房(二手房源)  1,2
    public String buildingType;//类型编码(一手楼盘类型、二手建筑类型)
    public String onlyLook;//只看房源:1 一手 2 二手
    public String longitude;//经度
    public String latitude;//纬度
    public String brokerId;//经济人ID
    public String circleTypeName;//商圈名称

    public int pageNo;

    public int pageSize = Constants.PAGE_SIZE;


    public Map<String, String> getRequestMap() {
        Map<String, String> map = new HashMap<>();

        map.put("pageNo", "" + pageNo);
        map.put("pageSize", "" + pageSize);
        if (!TextUtils.isEmpty(resblockName))
            map.put("resblockName", "" + resblockName);
        if (!TextUtils.isEmpty(districtId))
            map.put("districtId", "" + districtId);
        if (!TextUtils.isEmpty(totalPricesBegin))
            map.put("totalPricesBegin", "" + totalPricesBegin);
        if (!TextUtils.isEmpty(totalPricesEnd))
            map.put("totalPricesEnd", "" + totalPricesEnd);
        if (!TextUtils.isEmpty(sort) && !"-1".equals(sort))
            map.put("sort", "" + sort);
        if (!TextUtils.isEmpty(bedroomAmount))
            map.put("bedroomAmount", "" + bedroomAmount);
        if (!TextUtils.isEmpty(buildSizeBegin) && !"0.0".equals(buildSizeBegin))
            map.put("buildSizeBegin", "" + buildSizeBegin);
        if (!TextUtils.isEmpty(buildSizeEnd) && !"0.0".equals(buildSizeEnd))
            map.put("buildSizeEnd", "" + buildSizeEnd);
        if (!TextUtils.isEmpty(feature) && !"0".equals(feature))
            map.put("feature", "" + feature);
        if (!TextUtils.isEmpty(buildingType))
            map.put("buildingType", "" + buildingType);
        if (!TextUtils.isEmpty(onlyLook) && !"0".equals(onlyLook))
            map.put("onlyLook", "" + onlyLook);
        if (!TextUtils.isEmpty(longitude))
            map.put("longitude", "" + longitude);
        if (!TextUtils.isEmpty(latitude))
            map.put("latitude", "" + latitude);
        if (!TextUtils.isEmpty(circleTypeCode))
            map.put("circleTypeCode", "" + circleTypeCode);
        if (!TextUtils.isEmpty(circleTypeName))
            map.put("circleTypeName", "" + circleTypeName);
        if (!TextUtils.isEmpty(brokerId))
            map.put("brokerId", "" + brokerId);
        if (!TextUtils.isEmpty(totalShowing))
            map.put("totalShowing", "" + totalShowing);

        return map;
    }


    @Override
    public String toString() {
        return "OnLineHouseRequest{" +
                "resblockName='" + resblockName + '\'' +
                ", districtId='" + districtId + '\'' +
                ", circleTypeCode='" + circleTypeCode + '\'' +
                ", totalPricesBegin='" + totalPricesBegin + '\'' +
                ", totalPricesEnd='" + totalPricesEnd + '\'' +
                ", sort='" + sort + '\'' +
                ", totalShowing='" + totalShowing + '\'' +
                ", bedroomAmount='" + bedroomAmount + '\'' +
                ", buildSizeBegin='" + buildSizeBegin + '\'' +
                ", buildSizeEnd='" + buildSizeEnd + '\'' +
                ", feature='" + feature + '\'' +
                ", buildingType='" + buildingType + '\'' +
                ", onlyLook='" + onlyLook + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", brokerId='" + brokerId + '\'' +
                ", circleTypeName='" + circleTypeName + '\'' +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
