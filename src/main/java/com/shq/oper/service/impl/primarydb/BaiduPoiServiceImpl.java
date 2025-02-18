package com.shq.oper.service.impl.primarydb;

import com.github.pagehelper.util.StringUtil;
import com.shq.oper.mapper.primarydb.BaiduPoiMapper;
import com.shq.oper.model.domain.primarydb.ActivityCoupons;
import com.shq.oper.model.domain.primarydb.BaiduPoi;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.baidu.ResBaiduGeoCoderDto;
import com.shq.oper.model.dto.api.baidu.ResCreateBaiduPoiDto;
import com.shq.oper.service.primarydb.ActivityCouponsService;
import com.shq.oper.service.primarydb.BaiduPoiService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("baiduPoiService")
public class BaiduPoiServiceImpl extends BaseServiceImpl<BaiduPoi, Long> implements BaiduPoiService {

    @Autowired
    private BaiduPoiMapper baiduPoiMapper;

    @Transactional
    @Override
    public Msg<String> saveBaiduPoi(BaiduPoi baiduPoi) {
        if (StringUtil.isEmpty(baiduPoi.getLatitude())||StringUtil.isEmpty(baiduPoi.getLongitude())){
            ResBaiduGeoCoderDto resBaiduGeoCoderDto = getResBaiduGeoCoderDto(baiduPoi);
            if (resBaiduGeoCoderDto==null){
                return Msg.error("输入的店铺地址有误");
            }
            baiduPoi.setLongitude(resBaiduGeoCoderDto.getLng()+"");
            baiduPoi.setLatitude(resBaiduGeoCoderDto.getLat()+"");
        }

        ResCreateBaiduPoiDto resCreateBaiduPoiDto = getResCreateBaiduPoiDto(baiduPoi);
        if (resCreateBaiduPoiDto==null){
            return Msg.error("创建百度云麻点有误");
        }

        if (resCreateBaiduPoiDto.getStatus()!=0){
            return Msg.error("创建云麻点错误，"+"原因："+resCreateBaiduPoiDto.getMessage());
        }
        baiduPoi.setGeoId(resCreateBaiduPoiDto.getId());
        baiduPoiMapper.insert(baiduPoi);

        return Msg.ok();
    }

    private ResCreateBaiduPoiDto getResCreateBaiduPoiDto(BaiduPoi baiduPoi) {
        Map<String,String> reqMap=new HashMap<>();
        reqMap.put("geotable_id","191846");
        reqMap.put("ak","EmLj55R3CSaF7Tikx0dc9vQsHAGeG2ko");
        reqMap.put("latitude",baiduPoi.getLatitude());
        reqMap.put("longitude",baiduPoi.getLongitude());
        reqMap.put("address",baiduPoi.getAddress());
        reqMap.put("coord_type","3");
        reqMap.put("title",baiduPoi.getShopName());
        Object o1=HttpClientUtil.postFileMultiPart("http://api.map.baidu.com/geodata/v3/poi/create",reqMap);
        if (o1==null){
            return null;
        }
        return JsonUtils.parse(o1.toString(),ResCreateBaiduPoiDto.class);
    }

    private ResBaiduGeoCoderDto getResBaiduGeoCoderDto(BaiduPoi baiduPoi) {
        String url="http://api.map.baidu.com/geocoder/v2/?address=%s&output=json&ak=EmLj55R3CSaF7Tikx0dc9vQsHAGeG2ko";
        url=String.format(url,baiduPoi.getAddress());
        Object o=HttpClientUtil.doGet(url);
        String str=o.toString();
        if (str.contains("location")){
            str=str.substring(str.indexOf("lng")-2,str.indexOf("precise")-2);
        }else {
            return null;
        }
        return JsonUtils.parse(str,ResBaiduGeoCoderDto.class);
    }

    @Transactional
    @Override
    public Msg<String> deleteBaiduPoi(BaiduPoi baiduPoi) {

        String url="http://api.map.baidu.com/geodata/v3/geotable/delete";

        Map<String,String> reqMap=new HashMap<>();
        reqMap.put("id",baiduPoi.getGeoId()+"");
        reqMap.put("ak","EmLj55R3CSaF7Tikx0dc9vQsHAGeG2ko");
        reqMap.put("geotable_id","191846");
        Object o= HttpClientUtil.postFileMultiPart(url,reqMap);
        if (o==null){
            return Msg.error("创建云麻点错误");
        }
        ResCreateBaiduPoiDto resCreateBaiduPoiDto=JsonUtils.parse(o.toString(),ResCreateBaiduPoiDto.class);
        if (resCreateBaiduPoiDto.getStatus()!=0){
            return Msg.error("删除云麻点失败，"+"原因："+resCreateBaiduPoiDto.getMessage());
        }

        baiduPoiMapper.delete(baiduPoi);

        return Msg.ok();
    }

    @Transactional
    @Override
    public Msg<String> updateBaiduPoi(BaiduPoi baiduPoi) {
        LocalDateTime localDateTime=LocalDateTime.now();
        if (StringUtil.isEmpty(baiduPoi.getLatitude())||StringUtil.isEmpty(baiduPoi.getLongitude())){
            ResBaiduGeoCoderDto resBaiduGeoCoderDto = getResBaiduGeoCoderDto(baiduPoi);
            if (resBaiduGeoCoderDto==null){
                return Msg.error("输入的店铺地址有误");
            }
            baiduPoi.setLongitude(resBaiduGeoCoderDto.getLng()+"");
            baiduPoi.setLatitude(resBaiduGeoCoderDto.getLat()+"");
        }

        Map<String,String> reqMap=new HashMap<>();
        reqMap.put("id",baiduPoi.getGeoId()+"");
        reqMap.put("geotable_id","191846");
        reqMap.put("ak","EmLj55R3CSaF7Tikx0dc9vQsHAGeG2ko");
        reqMap.put("latitude",baiduPoi.getLatitude());
        reqMap.put("longitude",baiduPoi.getLongitude());
        reqMap.put("address",baiduPoi.getAddress());
        reqMap.put("coord_type","3");
        reqMap.put("title",baiduPoi.getShopName());
        Object o1=HttpClientUtil.postFileMultiPart("http://api.map.baidu.com/geodata/v3/poi/update",reqMap);
        if (o1==null){
            return Msg.error("修改百度地图云麻点失败");
        }
        ResCreateBaiduPoiDto resCreateBaiduPoiDto= JsonUtils.parse(o1.toString(),ResCreateBaiduPoiDto.class);

        if (resCreateBaiduPoiDto.getStatus()!=0){
            return Msg.error("修改云麻点错误，"+"原因："+resCreateBaiduPoiDto.getMessage());
        }

        baiduPoi.setUpdateTime(localDateTime);
        baiduPoiMapper.updateByPrimaryKeySelective(baiduPoi);
        return Msg.ok();
    }


}