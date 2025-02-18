package com.shq.oper.controller.api;

import com.github.pagehelper.PageInfo;
import com.shq.oper.dao.MongoDao;
import com.shq.oper.dao.mongo.*;
import com.shq.oper.enums.api.ChannelEnums;
import com.shq.oper.enums.api.ResponseResultCodeEnums;
import com.shq.oper.model.domain.mongo.ApiRequestDataLog;
import com.shq.oper.model.domain.mongo.AppLocationData;
import com.shq.oper.model.domain.mongo.HomePageBanner;
import com.shq.oper.model.domain.mongo.channel.*;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseRequestParamDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.req.ReqBrandActivityAddDto;
import com.shq.oper.model.dto.api.req.channel.ReqQuerychannelTemplateDto;
import com.shq.oper.util.CommRequestUtil;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.JsonUtils;
import com.shq.oper.util.MemoryViewUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Api(value = "app接口", tags = { "提供给爱之家的接口" })
@RestController
@Slf4j
@RequestMapping("/api/")
public class ApiChannelController {
    @Autowired
    private ChannelTemplateMongo channelTemplateMongo;

    @Autowired
    private GoodsMongo goodsMongo;

    @Autowired
    private ChannelBannerMongo channelBannerMongo;

    @Autowired
    private ActivityTemplateMongo  activityTemplateMongo;

    @Autowired
    private MongoDao<AppLocationData> mongoDao;

    @Autowired
    private ApiRequestDataLogMongo apiRequestDataLogMongo;

    @RequestMapping(value = "channel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody BaseResponseResultDto messageApi(HttpServletRequest request,
                                     @RequestBody BaseRequestParamDto requestParamDto) {


        String refIp = CommRequestUtil.getRemortIP(request);
        log.debug("===formIp===" + refIp + "===>" + MemoryViewUtil.showMemoryStr());

        ReqQuerychannelTemplateDto dto=null;
        BaseResponseResultDto baseResponseResultDto=new BaseResponseResultDto(ResponseResultCodeEnums.FAIL_CODE);
        try{
            baseResponseResultDto = JsonUtils.parse(JsonUtils.toDefaultJson(requestParamDto.getRequestData()), BaseResponseResultDto.class);

            ChannelEnums enumDtos = ChannelEnums.getByCode(requestParamDto.getCode());
            if (enumDtos == null) {
                baseResponseResultDto.setMsg("请求参数Code[" + requestParamDto.getCode() + "]有误");
                return baseResponseResultDto;
            }

            dto = JsonUtils.parse(JsonUtils.toDefaultJson(requestParamDto.getRequestData()), ReqQuerychannelTemplateDto.class);

            switch (enumDtos) {
                case CHANNEL_CODE_ENUMS:

                    Query query = new Query();

                    query.addCriteria(Criteria.where("state").is(1));
                    if (!StringUtils.isEmpty(dto.getName())) {
                        query.addCriteria(Criteria.where("name").is(dto.getName()));
                    }


                    ChannelTemplate channelTemplate = channelTemplateMongo.findOne(query, ChannelTemplate.class);

                    List<Model> modelList = channelTemplate.getModels();

                    if (null != modelList && modelList.size() > 0) {
                        Collections.sort(modelList, new Comparator<Model>() {

                            @Override
                            public int compare(Model o1, Model o2) {
                                return o1.getSort() - o2.getSort();
                            }
                        });
                    }
                    channelTemplate.setModels(modelList);
                    baseResponseResultDto.setCode("200");
                if (null!=channelTemplate){
                    baseResponseResultDto.setResult(channelTemplate);
                }
                return baseResponseResultDto;

                case CHANNEL_GOOD_ENUMS:
                    if (StringUtils.isEmpty(dto.getGoodUniqeKey())){
                         baseResponseResultDto.setMsg("请输入商品唯一标识");
                         return baseResponseResultDto;
                    }
                    if (StringUtils.isEmpty(dto.getPage())){
                        baseResponseResultDto.setMsg("请输入页数");
                        return baseResponseResultDto;
                    }
                    if (StringUtils.isEmpty(dto.getLimit())){
                        baseResponseResultDto.setMsg("请输入页码大小");
                        return baseResponseResultDto;
                    }
                    Query queryGoods=new Query();
                    queryGoods.addCriteria(Criteria.where("uniqeKey").is(dto.getGoodUniqeKey()));
                    queryGoods.with(new Sort(Sort.Direction.ASC,"sortNum"));

                    PageDto pageDto=new PageDto();
                    pageDto.setPage(dto.getPage());
                    pageDto.setLimit(dto.getLimit());

                    PageInfo<Goods>  goodsPageInfo=goodsMongo.findByQuery(queryGoods,pageDto,Goods.class);

                    baseResponseResultDto.setCode("200");
                    baseResponseResultDto.setResult(goodsPageInfo);
                   return baseResponseResultDto;

                case CHANNEL_BANNER_ENUMS:
                    if (StringUtils.isEmpty(dto.getGoodUniqeKey())){
                        baseResponseResultDto.setMsg("请输入商品唯一标识");
                        return baseResponseResultDto;
                    }
                    if (StringUtils.isEmpty(dto.getPage())){
                        baseResponseResultDto.setMsg("请输入页数");
                        return baseResponseResultDto;
                    }
                    if (StringUtils.isEmpty(dto.getLimit())){
                        baseResponseResultDto.setMsg("请输入页码大小");
                        return baseResponseResultDto;
                    }
                    Query querybanner=new Query();
                    querybanner.addCriteria(Criteria.where("uniqeKey").is(dto.getGoodUniqeKey()));
                    querybanner.with(new Sort(Sort.Direction.ASC,"sortNum"));

                    PageDto pageDto1=new PageDto();
                    pageDto1.setPage(dto.getPage());
                    pageDto1.setLimit(dto.getLimit());

                    PageInfo<ChannelBanner>  channelBannerPageInfo=channelBannerMongo.findByQuery(querybanner,pageDto1,ChannelBanner.class);

                    baseResponseResultDto.setCode("200");
                    baseResponseResultDto.setResult(channelBannerPageInfo);
                    return baseResponseResultDto;

                case ACTIVITY_TEMPLATE_ENUMS:

                    Query queryActivity=new Query();
                    queryActivity.addCriteria(Criteria.where("activityName").is(dto.getName()));

                    ActivityTemplate activityTemplate=activityTemplateMongo.findOne(queryActivity,ActivityTemplate.class);
                    baseResponseResultDto.setCode("200");
                    if (null!=activityTemplate){
                        baseResponseResultDto.setResult(activityTemplate);
                    }
                    return baseResponseResultDto;

                default:break;

            }


        }catch (Exception e){
            baseResponseResultDto.setMsg("内部错误");

        }finally {

            baseResponseResultDto.setResponseTime(DateUtils.now());// 设置成功时间

            String action = request.getRequestURI();
            String referer = request.getHeader("Referer");

            ApiRequestDataLog apiLog = new ApiRequestDataLog();
            apiLog.setAction(action);
            apiLog.setRefAction(referer);
            apiLog.setRefIp(refIp);
            apiLog.setCreateTime(LocalDateTime.now());
            apiLog.setActionCode(requestParamDto.getCode());
            apiLog.setActionSource(requestParamDto.getSource());
            apiLog.setActionDeviceType(requestParamDto.getDeviceType());
            apiLog.setActionPara(JsonUtils.toJson(requestParamDto));
            apiLog.setReturnData(JsonUtils.toJson(baseResponseResultDto));
            apiRequestDataLogMongo.insert(apiLog);
            if (!StringUtils.isEmpty(baseResponseResultDto.getMsg()) && baseResponseResultDto.getMsg().startsWith("调用接口出错,")) {
                baseResponseResultDto.setMsg("获取后台信息服务出错[" + apiLog.getId() + "]");
            }
            return  baseResponseResultDto;
        }
    }


}
