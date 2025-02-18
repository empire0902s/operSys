package com.shq.oper.controller.mongo;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.ActivityTemplateMongo;
import com.shq.oper.dao.mongo.GoodsMongo;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.channel.ActivityTemplate;
import com.shq.oper.model.domain.mongo.channel.ChannelTemplate;
import com.shq.oper.model.domain.mongo.channel.Goods;
import com.shq.oper.model.domain.mongo.channel.Model;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: ltg
 * @date: Created in 16:28 2018/9/25
 */

@RestController
@Slf4j
@RequestMapping("/mongo/activity")
public class ActivityTemplateController extends BaseController {

    @Autowired
    private ActivityTemplateMongo activityTemplateMongo;

    @Autowired
    private GoodsMongo goodsMongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DistributionProductMapper distributionProductMapper;

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
     *@date 2018/9/25 15:29
     * @params:[entity, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.ChannelTemplate>
     */
    @RequestMapping("/list")
    public Data<ActivityTemplate> getChannelTemplate(ActivityTemplate entity, PageDto page){

        Query query = new Query();
        if (!StringUtils.isEmpty(entity.getActivityName())){
            query.addCriteria(Criteria.where("activityName").is(entity.getActivityName()));
        }
        if (!StringUtils.isEmpty(entity.getState())){
            query.addCriteria(Criteria.where("state").is(entity.getState()));
        }
        PageInfo<ActivityTemplate> entitys=activityTemplateMongo.findByQueryPage(query,page,entity);
        return Data.ok(entitys);
    }

    /**
     *@author ltg
     *@date 2018/9/26 15:28
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/save")
    public Msg<String> saveChannel(ActivityTemplate entity, HttpServletRequest request){

        String time=LocalDateTime.now().toString();
        String admin=this.getAdmin(request).getName();
        try{
            if (StringUtils.isEmpty(entity.getId())){

                entity.setId(System.currentTimeMillis()+ String.format("%02d", new Random().nextInt(99)));
                entity.setCreateAdmin(admin);
                entity.setUpdateAdmin(admin);
                entity.setCreateTime(time);
                entity.setUpdateTime(time);
                entity.setModels(new ArrayList<>());
                activityTemplateMongo.insert(entity);
                return Msg.ok("添加成功");
            }else {
                Query query=new Query();
                query.addCriteria(Criteria.where("_id").is(entity.getId()));

                Update update=new Update();
                update.set("updateAdmin",admin);
                update.set("updateTime",time);
                update.set("activityName",entity.getActivityName());
                update.set("description",entity.getDescription());
                update.set("bannerImg",entity.getBannerImg());
                update.set("state",entity.getState());
                activityTemplateMongo.updateFirst(query,update,ActivityTemplate.class);
                return Msg.ok("操作成功");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }



    @RequestMapping("/moduleList")
    public Data<Model> getModelList(Model model){

        try{
            PageInfo<Model> entitys =new PageInfo<>();
            ActivityTemplate activityTemplate=activityTemplateMongo.findById(model.getId(),ActivityTemplate.class);
            if (null==activityTemplate||null==activityTemplate.getModels()||activityTemplate.getModels().size()<=0){
                return Data.ok(entitys);
            }
            List<Model> modelList=new ArrayList<>();
            String name=model.getModelName();
            if (!StringUtils.isEmpty(name)){
                activityTemplate.getModels().forEach(
                        p->{
                            if (p.getModelName().contains(name)){
                                modelList.add(p);
                            }
                        }
                );
                entitys.setList(modelList);
            }else{
                entitys.setList(activityTemplate.getModels());
            }
            return Data.ok(entitys);


        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }

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

    @RequestMapping("/saveModel")
    public Msg<String> saveModel(Model model,HttpServletRequest request){

        try{
            String mid=model.getMId();
            ActivityTemplate activityTemplate=activityTemplateMongo.findById(mid,ActivityTemplate.class);
            List<Model> modelList=new ArrayList<>();
            modelList.addAll(activityTemplate.getModels());
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

            activityTemplateMongo.updateFirst(query,update,ActivityTemplate.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }

    @RequestMapping("/listModule")
    public Data<Model> getModelList(String id){

        try{
            PageInfo<Model> entitys =new PageInfo<>();
            ActivityTemplate activityTemplate=activityTemplateMongo.findById(id,ActivityTemplate.class);
            if (null==activityTemplate||null==activityTemplate.getModels()||activityTemplate.getModels().size()<=0){
                return Data.ok(entitys);
            }
            entitys.setList(activityTemplate.getModels());
            return Data.ok(entitys);
        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }

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
