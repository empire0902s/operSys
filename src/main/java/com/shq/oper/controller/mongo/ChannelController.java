package com.shq.oper.controller.mongo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.mongodb.BasicDBObject;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.ChannelBannerMongo;
import com.shq.oper.dao.mongo.ChannelTemplateMongo;
import com.shq.oper.dao.mongo.GoodsMongo;
import com.shq.oper.dao.mongo.HomePageModuleMongo;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.*;
import com.shq.oper.model.domain.mongo.channel.ChannelBanner;
import com.shq.oper.model.domain.mongo.channel.ChannelTemplate;
import com.shq.oper.model.domain.mongo.channel.Goods;
import com.shq.oper.model.domain.mongo.channel.Model;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.*;
import com.shq.oper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author: ltg
 * @date: Created in 15:20 2018/9/18
 */
@RestController
@Slf4j
@RequestMapping("/mongo/channel")
public class ChannelController extends BaseController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DistributionProductMapper distributionProductMapper;

    @Autowired
    private ChannelTemplateMongo channelTemplateMongo;

    @Autowired
    private GoodsMongo goodsMongo;

    @Autowired
    private ChannelBannerMongo channelBannerMongo;


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/main")
    public ModelAndView main(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/goods")
    public ModelAndView goods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/banner")
    public ModelAndView banner(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/addgood")
    public ModelAndView addgood(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/columnsetting")
    public ModelAndView columnsetting(HttpServletRequest request) {
        return toPage(request);
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:29
     * @params:[entity, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.ChannelTemplate>
     */
    @RequestMapping("/list")
    public  Data<ChannelTemplate> getChannelTemplate(ChannelTemplate entity,PageDto page){

        Query query = new Query();
        if (!StringUtils.isEmpty(entity.getChannelNum())){
            query.addCriteria(Criteria.where("channelNum").is(entity.getChannelNum()));
        }
        if (!StringUtils.isEmpty(entity.getName())){
            query.addCriteria(Criteria.where("name").is(entity.getName()));
        }
        if (!StringUtils.isEmpty(entity.getState())){
            query.addCriteria(Criteria.where("state").is(entity.getState()));
        }
        PageInfo<ChannelTemplate> entitys=channelTemplateMongo.findByQueryPage(query,page,entity);
        return Data.ok(entitys);
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:28
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/save")
    public Msg<String> saveChannel(ChannelTemplate entity,HttpServletRequest request){

        String time=LocalDateTime.now().toString();
        String admin=this.getAdmin(request).getName();
        try{



            if (StringUtils.isEmpty(entity.getId())){

                Query queryCount=new Query();
                queryCount.addCriteria(Criteria.where("name").is(entity.getName()));
                queryCount.addCriteria(Criteria.where("state").is(1));
                long count=mongoTemplate.count(queryCount,ChannelTemplate.class);
                if (count>0){
                    return Msg.error("该激活的渠道名称已存在");
                }

                entity.setId(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
                entity.setChannelNum(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
                entity.setCreateAdmin(admin);
                entity.setUpdateAdmin(admin);
                entity.setCreateTime(time);
                entity.setUpdateTime(time);
                entity.setModels(new ArrayList<>());
                mongoTemplate.insert(entity);
                return Msg.ok("添加成功");
            }else {
                Query queryCount=new Query();
                queryCount.addCriteria(Criteria.where("name").is(entity.getName()));
                queryCount.addCriteria(Criteria.where("state").is(1));
                queryCount.addCriteria(Criteria.where("_id").nin(entity.getId()));
                long count=mongoTemplate.count(queryCount,ChannelTemplate.class);
                if (count>0){
                    return Msg.error("该激活的渠道名称已存在");
                }

                Query query=new Query();
                query.addCriteria(Criteria.where("_id").is(entity.getId()));

                Update update=new Update();
                update.set("updateAdmin",admin);
                update.set("updateTime",time);
                update.set("name",entity.getName());
                update.set("description",entity.getDescription());
                update.set("templateType",entity.getTemplateType());
//                update.set("brandDescImg",entity.getBrandDescImg());
//                update.set("loveDescImg",entity.getLoveDescImg());
                update.set("qrCodeUrl",entity.getQrCodeUrl());
                update.set("iosLoadUrl",entity.getIosLoadUrl());
                update.set("androndLoadUrl",entity.getAndrondLoadUrl());
                update.set("state",entity.getState());

                channelTemplateMongo.updateFirst(query,update,ChannelTemplate.class);
                return Msg.ok("操作成功");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:28
     * @params:[id]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/delete")
    public Msg<String> deleteChannel(String id){

        try{
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            channelTemplateMongo.deleteOne(query,ChannelTemplate.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:28
     * @params:[id]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.Model>
     */
    @RequestMapping("/listModule")
    public Data<Model> getModelList(String id){

        try{
            PageInfo<Model> entitys =new PageInfo<>();
            ChannelTemplate channelTemplate=channelTemplateMongo.findById(id,ChannelTemplate.class);
            if (null==channelTemplate||null==channelTemplate.getModels()||channelTemplate.getModels().size()<=0){
                return Data.ok(entitys);
            }
            entitys.setList(channelTemplate.getModels());
            return Data.ok(entitys);
        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:28
     * @params:[model]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.Model>
     */
    @RequestMapping("/moduleList")
    public Data<Model> getModelList(Model model){

        try{
            PageInfo<Model> entitys =new PageInfo<>();
            ChannelTemplate channelTemplate=channelTemplateMongo.findById(model.getId(),ChannelTemplate.class);
            if (null==channelTemplate||null==channelTemplate.getModels()||channelTemplate.getModels().size()<=0){
                return Data.ok(entitys);
            }
            List<Model> modelList=new ArrayList<>();
            String name=model.getModelName();
            if (!StringUtils.isEmpty(name)){
                channelTemplate.getModels().forEach(
                        p->{
                            if (p.getModelName().contains(name)){
                                modelList.add(p);
                            }
                        }
                );
                entitys.setList(modelList);
            }else{
                entitys.setList(channelTemplate.getModels());
            }
            return Data.ok(entitys);


        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }

    /**
     *@author ltg
     *@date 2018/9/20 15:28
     * @params:[goods, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.Goods>
     */
    @RequestMapping("/goodsList")
    public Data<Goods> getModelGoodsList(Goods goods, PageDto page){

        try{
            PageInfo<Goods> goodsPageInfo=new PageInfo<>();
//            Goods goods=new Goods();
            if (StringUtils.isEmpty(goods.getUniqeKey())){
                return Data.ok(goodsPageInfo);
            }
            Query query=new Query();
            query.addCriteria(Criteria.where("uniqeKey").is(goods.getUniqeKey()));
            if (!StringUtils.isEmpty(goods.getGoodsCode())){
                query.addCriteria(Criteria.where("goodsCode").is(goods.getGoodsCode()));
            }
            query.with(new Sort(Sort.Direction.ASC,"sortNum"));
            goodsPageInfo=goodsMongo.findByQuery(query,page,Goods.class);
            return Data.ok(goodsPageInfo);

        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }

    /**
     *@author ltg
     *@date 2018/9/20 15:28
     * @params:[banner, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.ChannelBanner>
     */
    @RequestMapping("/bannerList")
    public Data<ChannelBanner> getModelBannerList(ChannelBanner banner, PageDto page){

        try{
            PageInfo<ChannelBanner> PageInfo=new PageInfo<>();
//            Goods goods=new Goods();
            if (StringUtils.isEmpty(banner.getUniqeKey())){
                return Data.ok(PageInfo);
            }
            Query query=new Query();
            query.addCriteria(Criteria.where("uniqeKey").is(banner.getUniqeKey()));
            if (!StringUtils.isEmpty(banner.getImgTitle())){
                query.addCriteria(Criteria.where("imgTitle").is(banner.getImgTitle()));
            }
            query.with(new Sort(Sort.Direction.ASC,"sortNum"));
            PageInfo=channelBannerMongo.findByQuery(query,page,ChannelBanner.class);
            return Data.ok(PageInfo);

        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }

    /**
     *@author ltg
     *@date 2018/9/20 15:27
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/addGoods")
    public Msg<String> addGoods(Goods entity,HttpServletRequest request){

        try{
            String time = LocalDateTime.now().toString();
            String admin = this.getAdmin(request).getName();
            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(entity.getGoodsCode());
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            Query query=new Query();
            query.addCriteria(Criteria.where("goodsCode").is(entity.getGoodsCode()));
            query.addCriteria(Criteria.where("modelId").is(entity.getModelId()));
            long l=goodsMongo.selectCount(query,Goods.class);

            if (l>0){
                return Msg.error("该商品已存在");
            }
            entity.setId(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
            entity.setCreateTime(time);
            entity.setCreateAdmin(admin);
            entity.setProductName(distributionProduct.getProductName());
            entity.setCompanyName(distributionProduct.getCompanyName());
            entity.setIsSale(distributionProduct.getIsSale());

            mongoTemplate.insert(entity);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:27
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/updateGoods")
    public Msg<String> updateGoods(Goods entity,HttpServletRequest request){

        try{
            if (StringUtils.isEmpty(entity.getId())){
                return Msg.error("id不能为空");
            }

            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(entity.getGoodsCode());
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            Query query=new Query();
            query.addCriteria(Criteria.where("goodsCode").is(entity.getGoodsCode()));
            query.addCriteria(Criteria.where("modelId").is(entity.getModelId()));
            query.addCriteria(Criteria.where("_id").nin(entity.getId()));
            long l=goodsMongo.selectCount(query,Goods.class);

            if (l>0){
                return Msg.error("该商品已存在");
            }

            Update update=new Update();
            update.set("productName",distributionProduct.getProductName());
            update.set("companyName",distributionProduct.getCompanyName());
            update.set("isSale",distributionProduct.getIsSale());
            update.set("goodsCode",entity.getGoodsCode());
            update.set("sortNum",entity.getSortNum());

            Query query1=new Query();
            query1.addCriteria(Criteria.where("_id").is(entity.getId()));

            mongoTemplate.updateFirst(query1,update,Goods.class);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    /**
     *@author ltg
     *@date 2018/9/20 15:27
     * @params:[model, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/saveModel")
    public Msg<String> saveModel(Model model,HttpServletRequest request){

        try{
            String mid=model.getMId();
            ChannelTemplate channelTemplate=channelTemplateMongo.findById(mid,ChannelTemplate.class);
            List<Model> modelList=new ArrayList<>();
            modelList.addAll(channelTemplate.getModels());
            String admin=this.getAdmin(request).getName();
            if (StringUtils.isEmpty(model.getId())){
                model.setId(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
                model.setBannerUniqeKey(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
                model.setGoodUniqeKey(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
                model.setCreateTime(LocalDateTime.now().toString());
                model.setCreateAdmin(admin);
                modelList.add(model);
            }else {
                modelList.forEach(
                        p->{
                            if (p.getId().equals(model.getId())){
                                p.setModelName(model.getModelName());
                                p.setBannerNum(model.getBannerNum());
                                p.setSort(model.getSort());
                                p.setGoodsType(model.getGoodsType());
                                p.setIsShow(model.getIsShow());
                            }
                        }
                );

                if (!StringUtils.isEmpty(model.getGoodUniqeKey())){
                    Query query=new Query();
                    query.addCriteria(Criteria.where("uniqeKey").is(model.getGoodUniqeKey()));
                    Update update=Update.update("modelName",model.getModelName());
                    goodsMongo.updateMulti(query,update,Goods.class);
                }
            }

            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(mid));
            Update update=Update.update("models",modelList);

            channelTemplateMongo.updateFirst(query,update,ChannelTemplate.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }

    /**
     *@author ltg
     *@date 2018/9/20 15:27
     * @params:[model]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/deleteModel")
    public Msg<String> deleteModel(Model model){
        try{

            String bannerUniqeKey="";
            String goodsUniqeKey="";
            String mid=model.getMId();
            ChannelTemplate channelTemplate=channelTemplateMongo.findById(mid,ChannelTemplate.class);
            List<Model> modelList=new ArrayList<>();

            for (Model p:channelTemplate.getModels()){
                if (!p.getId().equals(model.getId())){
                    modelList.add(p);
                }else {
                    bannerUniqeKey=p.getBannerUniqeKey();
                    goodsUniqeKey=p.getGoodUniqeKey();
                }
            }

            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(mid));
            Update update=Update.update("models",modelList);

            channelTemplateMongo.updateFirst(query,update,ChannelTemplate.class);
            if (!StringUtils.isEmpty(goodsUniqeKey)){
                Query goodsQuery=new Query();
                goodsQuery.addCriteria(Criteria.where("uniqeKey").is(goodsUniqeKey));
                goodsMongo.remove(goodsQuery,Goods.class);
            }
           if (!StringUtils.isEmpty(bannerUniqeKey)){
               Query bannerQuery=new Query();
               bannerQuery.addCriteria(Criteria.where("uniqeKey").is(bannerQuery));
               channelBannerMongo.remove(bannerQuery,ChannelBanner.class);
           }
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    @RequestMapping("/addBanner")
    public Msg<String> addBanner(ChannelBanner entity,HttpServletRequest request) {

        try {
            String admin = this.getAdmin(request).getName();
            String time = LocalDateTime.now().toString();
            if (StringUtils.isEmpty(entity.getId())) {

                entity.setCreateAdmin(admin);
                entity.setCreateTime(time);
                entity.setUpdateAdmin(admin);
                entity.setId(System.currentTimeMillis() + String.format("%02d", new Random().nextInt(99)));
                channelBannerMongo.insert(entity);
                return Msg.ok("操作成功");
            } else {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(entity.getId()));
                Update update = new Update();
                update.set("updateTime", time);
                update.set("updateAdmin", admin);
                update.set("sortNum", entity.getSortNum());
                update.set("imgTitle", entity.getImgTitle());
                update.set("imgTarget", entity.getImgTarget());
                update.set("imgUrl", entity.getImgUrl());

                channelBannerMongo.updateFirst(query, update, ChannelBanner.class);
                return Msg.ok("操作成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    @RequestMapping("/deleteBanner")
    public Msg<String> deleteBanner(ChannelBanner entity,HttpServletRequest request) {

        try {
          if (StringUtils.isEmpty(entity.getId())){
              return Msg.error("请输入id");
          }
          Query query=new Query();
          query.addCriteria(Criteria.where("_id").is(entity.getId()));

          channelBannerMongo.remove(query,ChannelBanner.class);
          return Msg.ok("操作成功");

        } catch (Exception e) {
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    @RequestMapping("/deleteGoods")
    public Msg<String> deleteGoods(Goods entity,HttpServletRequest request) {

        try {
            if (StringUtils.isEmpty(entity.getId())){
                return Msg.error("请输入id");
            }
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(entity.getId()));

            goodsMongo.remove(query,Goods.class);
            return Msg.ok("操作成功");

        } catch (Exception e) {
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }

}
