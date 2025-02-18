package com.shq.oper.controller.mongo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.mongodb.BasicDBObject;
import com.shq.oper.dao.mongo.GoodsMongo;
import com.shq.oper.enums.ProductTypeEnum;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.mongo.*;
import com.shq.oper.model.domain.mongo.channel.Goods;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.*;
import com.shq.oper.model.dto.api.res.ResBaseDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.ProductService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.HomePageModuleMongo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ltg
 * @date: Created in 15:20 2018/8/15
 */
@RestController
@Slf4j
@RequestMapping("/mongo/homepage")
public class HomePageController  extends BaseController {

    @Autowired
    private DistributionProductMapper distributionProductMapper;

    @Autowired
    private HomePageModuleMongo homePageModuleMongo;

    @Autowired
    private GoodsMongo goodsMongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ShqApi shqApi;

    @Autowired
    private ProductService productService;


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/homepagemain")
    public ModelAndView homepagemain(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/goods")
    public ModelAndView goods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/groupitem")
    public ModelAndView groupitem(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/addgood")
    public ModelAndView addgoods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/vipgoods")
    public ModelAndView vipgoods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/vipandgoods")
    public ModelAndView vipandgoods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/businessgoods")
    public ModelAndView businessgoods(HttpServletRequest request) {
        return toPage(request);
    }

    @RequestMapping("/businessandgoods")
    public ModelAndView businessandgoods(HttpServletRequest request,String code) {
        try{
//            if (!StringUtils.isEmpty(code)){
//                Query pquery=new Query();
//                pquery.addCriteria(Criteria.where("goodsCode").is(code));
////                pquery.addCriteria(Criteria.where("companyState").is(0));
//                Product distributionProductResult=mongoTemplate.findOne(pquery,Product.class,"distributionGoods");


            List<String> codeList=new ArrayList<>();
            codeList.add(code);
            List<DistributionProduct> list=distributionProductMapper.queryDiatributionProductByCodeList(codeList);
            //        DistributionProduct distributionProduct= productService.searchGoods(code);

            DistributionProduct distributionProductResult= null;
            if (null==list||list.size()<=0){
               return toPage(request);
            }
            distributionProductResult=list.get(0);
            request.setAttribute("distributionProductResult",distributionProductResult);

            return toPage(request);
        }catch (Exception e){
            return toPage(request);
        }


    }

    @RequestMapping("/goodsdetailsSetting")
    public ModelAndView detailsSetting(HttpServletRequest request,String id) {

        try{
            Goods goods=goodsMongo.findById(id,Goods.class);

            DistributionProduct distributionProductResult=null;
            DistributionProduct temp=new DistributionProduct();
            temp.setProductCode(goods.getGoodsCode());
            List<DistributionProduct> select = distributionProductMapper.select(temp);
            if(select.size()>0) {
                distributionProductResult=select.get(0);
                distributionProductResult.setProductDetails("");
            }
            request.setAttribute("distributionProductResult",distributionProductResult);
            request.setAttribute("goods",goods);
            return toPage(request);

        }catch (Exception e){
            e.printStackTrace();
            return toPage(request);
        }

    }

    /**
     * 获取爱之家首页模块 banner列表
     *@author ltg
     *@date 2018/8/16 15:44
     * @params:[id]
     * @return: java.util.List<com.shq.oper.model.domain.mongo.HomePageBanner>
     */
    @RequestMapping("/bannerList")
    public Data<HomePageBanner> getList(HomePageBanner entity){
        List<HomePageBanner> listBanner=new ArrayList<>();
        PageInfo<HomePageBanner> page=new PageInfo<>(listBanner);
        if (StringUtil.isEmpty(entity.getModuleId())){
            return Data.ok(page);
        }

        try{
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(entity.getModuleId(),HomePageModule.class);

            if (homePageModule==null||homePageModule.getListBanner()==null||homePageModule.getListBanner().size()<=0){
                return Data.ok(page);
            }

            if (StringUtils.isEmpty(entity.getImgTitle())){
                homePageModule.getListBanner().forEach(
                        p->{
                            p.setModuleId(entity.getModuleId());
                            listBanner.add(p);
                        }
                );
//                PageInfo<HomePageBanner> pageInfo=new PageInfo<>(listBanner);
//                return Data.ok(pageInfo);
            }else {
                homePageModule.getListBanner().forEach(
                        p->{
                            if (p.getImgTitle().trim().contains(entity.getImgTitle().trim())){
                                p.setModuleId(entity.getModuleId());
                                listBanner.add(p);
                            }
                        }
                );
            }
            Collections.sort(listBanner, new Comparator<HomePageBanner>() {

                @Override
                public int compare(HomePageBanner o1, HomePageBanner o2) {
                    return o1.getSortNum() - o2.getSortNum();
                }
            });

            PageInfo<HomePageBanner> pageInfo=new PageInfo<>(listBanner);
            return Data.ok(pageInfo);

        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }


    /**
     * 获取爱之家首页模块 商品列表
     *@author ltg
     *@date 2018/8/16 15:44
     * @params:[id]
     * @return: java.util.List<com.shq.oper.model.domain.mongo.HomePageGoods>
     */
    @RequestMapping("/goodsList")
    public  Data<HomePageGoods> goodsList(HomePageGoods entity,PageDto pageDto){
        List<HomePageGoods> listGoods=new ArrayList<>();
        PageInfo<HomePageGoods> page=new PageInfo<>(listGoods);
        if (StringUtil.isEmpty(entity.getModuleId())){
            return Data.ok(page);
        }

        try{
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(entity.getModuleId(),HomePageModule.class);

            if (homePageModule==null||homePageModule.getListGoods()==null||homePageModule.getListGoods().size()<=0){
                return Data.ok(page);
            }

            Collections.sort(homePageModule.getListGoods(), new Comparator<HomePageGoods>() {

                @Override
                public int compare(HomePageGoods o1, HomePageGoods o2) {
                    return o1.getSortNum() - o2.getSortNum();
                }
            });

            int start=(pageDto.getPage()-1)*pageDto.getLimit();
            List<HomePageGoods> listGoods1=new ArrayList<>();
            if (homePageModule.getListGoods().size()>=start+pageDto.getLimit()){
                listGoods1=homePageModule.getListGoods().subList(start,start+pageDto.getLimit());
            }else if(homePageModule.getListGoods().size()>=start){
                listGoods1=homePageModule.getListGoods().subList(start,homePageModule.getListGoods().size());
            }

            if (listGoods1.size()<=0){
                page.setTotal(homePageModule.getListGoods().size());
                return Data.ok(page);
            }

            List<String> codeList=new ArrayList<>();
            listGoods1.forEach(
                    p->{
                        codeList.add(p.getGoodsCode());
                    }
            );

            Map<String,DistributionProduct> map=new HashMap<>();
            Page<DistributionProduct> distributionProducts=distributionProductMapper.queryDiatributionProductByCodeList(codeList);
            if (null!=distributionProducts&&distributionProducts.size()>0){
                distributionProducts.forEach(
                        p->{
                            map.put(p.getProductCode(),p);
                        }
                );
            }

            if (StringUtils.isEmpty(entity.getGoodsCode())){
                listGoods1.forEach(
                        p->{
                            p.setModuleId(entity.getModuleId());
                            DistributionProduct distributionProduct=map.get(p.getGoodsCode());
                            if (null!=distributionProduct){
                                p.setCompanyName(distributionProduct.getCompanyName());
                                p.setProductName(distributionProduct.getProductName());
                                p.setIsSale(distributionProduct.getIsSale());
                            }
                            listGoods.add(p);
                        }
                );
//                Collections.sort(listGoods, new Comparator<HomePageGoods>() {
//
//                    @Override
//                    public int compare(HomePageGoods o1, HomePageGoods o2) {
//                        return o1.getSortNum() - o2.getSortNum();
//                    }
//                });

                PageInfo<HomePageGoods> pageInfo=new PageInfo<>(listGoods);
                pageInfo.setTotal(homePageModule.getListGoods().size());
                return Data.ok(pageInfo);
            }else {
                listGoods1.forEach(
                        p->{
                            p.setModuleId(entity.getModuleId());
                            DistributionProduct distributionProduct=map.get(p.getGoodsCode());
                            if (null!=distributionProduct){
                                p.setCompanyName(distributionProduct.getCompanyName());
                                p.setProductName(distributionProduct.getProductName());
                                p.setIsSale(distributionProduct.getIsSale());
                            }
                            if (p.getGoodsCode().trim().contains(entity.getGoodsCode().trim())){
                                listGoods.add(p);
                            }
                        }
                );

                PageInfo<HomePageGoods> pageInfo=new PageInfo<>(listGoods);
                pageInfo.setTotal(homePageModule.getListGoods().size());
                return Data.ok(pageInfo);
            }


        }catch (Exception e){
            e.printStackTrace();
            return Data.error("内部错误");
        }
    }


    /**
     * 获取爱之家首页模块 组合Banner和Goods表列表
     *@author ltg
     *@date 2018/8/16 15:44
     * @params:[id]
     * @return: java.util.List<com.shq.oper.model.domain.mongo.HomePageGroupItem>
     */
    @RequestMapping("/groupItem")
    public  String groupItem(String mId,HttpServletRequest request){

        String time=LocalDateTime.now().toString();
        String admin=this.getAdmin(request).getName();

        List<HomePageItemDto> list=new ArrayList<>();
        HomePageGroupItemRepDto repDto=new HomePageGroupItemRepDto();
        repDto.setCode(0);
        repDto.setMsg("ok");
        repDto.setData(list);
        String result=JsonUtils.toDefaultJson(repDto);

        if (StringUtil.isEmpty(mId)){
            return result;
        }

        try{
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(mId,HomePageModule.class);

            if (homePageModule==null){
                return result;
            }

            if (homePageModule.getListGroupItem()==null||homePageModule.getListGroupItem().size()<=0){
                List<HomePageGroupItem> list1=new ArrayList<>();
                HomePageGroupItem homePageGroupItem=new HomePageGroupItem();
                String id=String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99));
                homePageGroupItem.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
                homePageGroupItem.setModuleId(mId);
                homePageGroupItem.setGroupName("滑块1");
                homePageGroupItem.setCreateTime(time);
                homePageGroupItem.setCreateAdmin(admin);
                homePageGroupItem.setSortNum(1000);
                list1.add(homePageGroupItem);

                BasicDBObject obj = new BasicDBObject();
                obj.put("_id", new ObjectId(mId));
                Query query=new BasicQuery(obj);

                Update update=Update.update("listGroupItem",list1);
                homePageModuleMongo.updateFirst(query,update,HomePageModule.class);

                HomePageItemDto homePageItemDto=new HomePageItemDto();
                homePageItemDto.setId(1);
                homePageItemDto.setName("滑块1");
                homePageItemDto.setPid(-1);
                homePageItemDto.setModuleId(mId);
                homePageItemDto.setSortNum(1000);
                homePageItemDto.setDelete(false);
                homePageItemDto.setAdd(true);
                homePageItemDto.setModify(true);
//                homePageItemDto.setDataId(item.getId());
                homePageItemDto.setModeType(1);
                homePageItemDto.setGroupItemId(id);
                list.add(homePageItemDto);

                repDto.setData(list);
                return JsonUtils.toDefaultJson(repDto);
            }

            int i=1;
            List<HomePageGroupItem> listGroupItem= homePageModule.getListGroupItem();

            Collections.sort(listGroupItem, new Comparator<HomePageGroupItem>() {

                @Override
                public int compare(HomePageGroupItem o1, HomePageGroupItem o2) {
                    return o1.getSortNum() - o2.getSortNum();
                }
            });

            for (HomePageGroupItem item:listGroupItem) {
                HomePageItemDto homePageItemDto = new HomePageItemDto();
//                homePageItemDto.setCreateAdmin(item.getCreateAdmin());
//                homePageItemDto.setCreateTime(item.getCreateTime());
                homePageItemDto.setId(i);
                homePageItemDto.setName(item.getGroupName());
                i++;
                homePageItemDto.setPid(-1);
                homePageItemDto.setModuleId(item.getModuleId());
                homePageItemDto.setSortNum(item.getSortNum());
                homePageItemDto.setDelete(false);
                homePageItemDto.setAdd(true);
                homePageItemDto.setModify(true);
//                homePageItemDto.setDataId(item.getId());
                homePageItemDto.setModeType(1);
                homePageItemDto.setGroupItemId(item.getId());
                homePageItemDto.setDataId(item.getId());
                if (homePageModule.getStyleType()==10){
                    homePageItemDto.setStyleType(10);
                }
                list.add(homePageItemDto);

                if (null != item.getItemListBanner() && item.getItemListBanner().size() > 0) {
                    for (HomePageBanner banner : item.getItemListBanner()) {
                        HomePageItemDto homePageItemDto2 = new HomePageItemDto();
                        homePageItemDto2.setImgUrl(banner.getImgUrl());
//                                homePageItemDto2.setCreateTime(banner.getCreateTime());
                        homePageItemDto2.setId(i);
//                        i++;
                        homePageItemDto2.setPid(homePageItemDto.getId());
                        homePageItemDto2.setModuleId(banner.getModuleId());
                        homePageItemDto2.setName("banner");
                        homePageItemDto2.setSortNum(banner.getSortNum());
                        homePageItemDto2.setDelete(true);
                        homePageItemDto2.setAdd(false);
                        homePageItemDto2.setModify(true);
                        homePageItemDto2.setDataId(banner.getId());
                        homePageItemDto2.setModeType(4);
                        homePageItemDto2.setGroupItemId(item.getId());
                        homePageItemDto2.setGoodsItemId(item.getId());
                        homePageItemDto2.setImgTarget(banner.getImgTarget());
                        homePageItemDto2.setImgTitle(banner.getImgTitle());
                        list.add(homePageItemDto2);
                    }
                }

                if (null != item.getItemListGoods() && item.getItemListGoods().size() > 0) {
                    Collections.sort(item.getItemListGoods(), new Comparator<HomePageGoods>() {

                        @Override
                        public int compare(HomePageGoods o1, HomePageGoods o2) {
                            return o1.getSortNum() - o2.getSortNum();
                        }
                    });
                    for (HomePageGoods goods : item.getItemListGoods()) {
                        HomePageItemDto homePageItemDto2 = new HomePageItemDto();
//                                homePageItemDto2.setCreateAdmin(goods.getCreateAdmin());
                        homePageItemDto2.setGoodsCode(goods.getGoodsCode());
                        homePageItemDto2.setId(i);
//                        i++;
                        homePageItemDto2.setPid(homePageItemDto.getId());
                        homePageItemDto2.setModuleId(goods.getModuleId());
                        homePageItemDto2.setName("商品");
                        homePageItemDto2.setSortNum(goods.getSortNum());
                        homePageItemDto2.setDelete(true);
                        homePageItemDto2.setAdd(false);
                        homePageItemDto2.setModify(true);
                        homePageItemDto2.setDataId(goods.getId());
                        homePageItemDto2.setModeType(3);
                        homePageItemDto2.setGroupItemId(item.getId());
                        homePageItemDto2.setGoodsItemId(item.getId());
                        list.add(homePageItemDto2);
                    }
                }
            }

                List<String> codeList = new ArrayList<>();
                list.forEach(
                        p -> {
                            if (!StringUtils.isEmpty(p.getGoodsCode())) {
                                codeList.add(p.getGoodsCode());
                            }
                        }
                );
                Page<DistributionProduct> distributionProducts = null;
                Map<String, DistributionProduct> map = new HashMap<>();
                if (codeList.size() > 0) {
                    distributionProducts = distributionProductMapper.queryDiatributionProductByCodeList(codeList);
                    if (null != distributionProducts && distributionProducts.size() > 0) {
                        distributionProducts.forEach(
                                p -> {
                                    map.put(p.getProductCode(), p);
                                }
                        );
                    }
                }

                list.forEach(
                        p -> {
                            if (!StringUtils.isEmpty(p.getGoodsCode())) {
                                DistributionProduct distributionProduct = map.get(p.getGoodsCode());
                                if (null != distributionProduct) {
                                    p.setCompanyName(distributionProduct.getCompanyName());
                                    p.setProductName(distributionProduct.getProductName());
                                    p.setIsSale(distributionProduct.getIsSale());
                                }
                            }
                        }
                );

                repDto.setData(list);
                return JsonUtils.toDefaultJson(repDto);
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
    }

    /**
     *@author ltg
     *@date 2018/8/20 18:37
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/addBanner")
    public Msg<String> addBanner(HomePageBanner entity,HttpServletRequest request){

        String time=LocalDateTime.now().toString();
        String admin=this.getAdmin(request).getName();
        if (StringUtil.isEmpty(entity.getId())){
            return addBanner(entity, time, admin);
        }else {
            return updateBanner(entity, time, admin);
        }

    }

    /**
     *@author ltg
     *@date 2018/8/21 10:20
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/addgoods")
    public Msg<String> addgoods(HomePageGoods entity,HttpServletRequest request){

        String time=LocalDateTime.now().toString();
        String admin=this.getAdmin(request).getName();

        if (StringUtil.isEmpty(entity.getId())){
            return addGoods(entity, time, admin);
        }else {
            return updateGoods(entity, time, admin);
        }
    }

    /**
     *@author ltg
     *@date 2018/8/20 18:46
     * @params:[bId, moduleId]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */

    @RequestMapping("/deleteBanner")
    public Msg<String> deleteBanner(HomePageBanner entity){

        try{
            String moduleId=entity.getModuleId();
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(moduleId,HomePageModule.class);

            List<HomePageBanner> listBanner=homePageModule.getListBanner();
            List<HomePageBanner> newBannerList=new ArrayList<>();
            String bid=entity.getId().trim();
            listBanner.forEach(
                    p->{
                        if (!p.getId().equals(bid)){
                            newBannerList.add(p);
                        }
                    }
            );

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(moduleId));
            Query query=new BasicQuery(obj);

            Update update=Update.update("listBanner",newBannerList);
            homePageModuleMongo.updateFirst(query,update,HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }

    }

    @RequestMapping("/deleteGoods")
    public Msg<String> deleteGoods(HomePageBanner entity){

        try{
            String moduleId=entity.getModuleId();
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(moduleId,HomePageModule.class);

            List<HomePageGoods> listGoods=homePageModule.getListGoods();
            List<HomePageGoods> newlistGoods=new ArrayList<>();
            String bid=entity.getId().trim();
            listGoods.forEach(
                    p->{
                        if (!p.getId().equals(bid)){
                            newlistGoods.add(p);
                        }
                    }
            );

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(moduleId));
            Query query=new BasicQuery(obj);

            Update update=Update.update("listGoods",newlistGoods);
            homePageModuleMongo.updateFirst(query,update,HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }

    @RequestMapping("/additem")
    public Msg<String> additem(HomePageGroupItem entity,HttpServletRequest request) {

        String time = LocalDateTime.now().toString();
        String admin = this.getAdmin(request).getName();

        try{

            String moduleId = entity.getModuleId();
            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);

            if (homePageModule == null) {
                return Msg.error("查不到模板");
            }
            List<HomePageGroupItem> listGroud = new ArrayList<>();

            int i=1;
            if (null != homePageModule.getListGroupItem() && homePageModule.getListGroupItem().size() > 0) {
                listGroud.addAll(homePageModule.getListGroupItem());
                i=listGroud.size()+1;
            }
            if (StringUtils.isEmpty(entity.getId())){

                HomePageGroupItem homePageGroupItem=new HomePageGroupItem();
                homePageGroupItem.setSortNum(entity.getSortNum());
                homePageGroupItem.setModuleId(entity.getModuleId());
                homePageGroupItem.setCreateAdmin(admin);
                homePageGroupItem.setCreateTime(time);
                homePageGroupItem.setId(System.currentTimeMillis()+"");
                homePageGroupItem.setGroupName(entity.getGroupName());

                listGroud.add(homePageGroupItem);

            }else {
                listGroud.forEach(
                        p->{
                            if (p.getId().equals(entity.getId())){
                                p.setSortNum(entity.getSortNum());
                                p.setGroupName(entity.getGroupName());
                            }
                        }
                );

            }

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query = new BasicQuery(obj);
            Update update = Update.update("listGroupItem", listGroud);
            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }


    }

    /**
     * 添加组合明细
     *@author ltg
     *@date 2018/8/22 17:08
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */

//    @RequestMapping("/adddetail")
//    public Msg<String> adddetail(HomePageGroupItemDetail entity,HttpServletRequest request) {
//
//        String time = LocalDateTime.now().toString();
//        String admin = this.getAdmin(request).getName();
//
//        try{
//            String moduleId = entity.getModuleId();
//            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);
//
//            if (homePageModule == null) {
//                return Msg.error("查不到模板");
//            }
//            List<HomePageGroupItem> listGroud = new ArrayList<>();
//
//            if (null != homePageModule.getListGroupItem() && homePageModule.getListGroupItem().size() > 0) {
//                listGroud.addAll(homePageModule.getListGroupItem());
//            }
//
//            if (StringUtil.isEmpty(entity.getId())) {
//                entity.setCreateTime(time);
//                entity.setUpdateTime(time);
//                entity.setCreateAdmin(admin);
//                entity.setUpdateAdmin(admin);
//                entity.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
//
//                List<HomePageGroupItemDetail> listGoodsItem = new ArrayList<>();
//                listGroud.forEach(
//                        p -> {
//                            if (p.getId().equals(entity.getGroupItemId())) {
//                                if (null != p.getListGoodsItem()) {
//                                    listGoodsItem.addAll(p.getListGoodsItem());
//
//                                }
//                                listGoodsItem.add(entity);
//                                p.setListGoodsItem(listGoodsItem);
//                            }
//                        }
//                );
//
//            }else {
//                listGroud.forEach(
//                        p -> {
//                            if (p.getId().equals(entity.getGroupItemId())) {
//                                p.getListGoodsItem().forEach(
//                                        item->{
//                                            if (item.getId().equals(entity.getId())){
//                                                item.setUpdateTime(time);
//                                                item.setUpdateAdmin(admin);
//                                                item.setSortNum(entity.getSortNum());
//                                            }
//                                        }
//                                );
//                            }
//                        }
//                );
//            }
//
//            BasicDBObject obj = new BasicDBObject();
//            obj.put("_id", new ObjectId(entity.getModuleId()));
//            Query query = new BasicQuery(obj);
//            Update update = Update.update("listGroupItem", listGroud);
//            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);
//
//            return Msg.ok("操作成功");
//
//        }catch (Exception e){
//            e.printStackTrace();
//            return Msg.error("内部错误");
//        }
//
//
//    }

    /**
     * 添加
     *@author ltg
     *@date 2018/8/22 17:50
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/addItemBanner")
    public Msg<String> addItemBanner(HomePageBanner entity,HttpServletRequest request) {

        String time = LocalDateTime.now().toString();
        String admin = this.getAdmin(request).getName();

        try{
            String moduleId = entity.getModuleId();
            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);

            if (homePageModule == null) {
                return Msg.error("查不到模板");
            }
            List<HomePageGroupItem> listGroud = new ArrayList<>();

            if (null != homePageModule.getListGroupItem() && homePageModule.getListGroupItem().size() > 0) {
                listGroud.addAll(homePageModule.getListGroupItem());

            }

            if (StringUtil.isEmpty(entity.getId())) {
                entity.setCreateTime(time);
                entity.setUpdateTime(time);
                entity.setCreateAdmin(admin);
                entity.setUpdateAdmin(admin);
                entity.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

                List<HomePageBanner> bannerList = new ArrayList<>();
                listGroud.forEach(
                        p -> {
                            if (p.getId().equals(entity.getGroupItemId())) {
                                if (null!=p.getItemListBanner()){
                                    bannerList.addAll(p.getItemListBanner());
                                }
                                bannerList.add(entity);
                                p.setItemListBanner(bannerList);
                            }
                        }
                );
            }else {
                listGroud.forEach(
                        p -> {
                            if (p.getId().equals(entity.getGroupItemId())) {
                                p.getItemListBanner().forEach(

                                        banner-> {
                                            if (banner.getId().equals(entity.getId())) {
                                                banner.setUpdateTime(time);
                                                banner.setUpdateAdmin(admin);
                                                banner.setSortNum(entity.getSortNum());
                                                banner.setImgTarget(entity.getImgTarget());
                                                banner.setImgUrl(entity.getImgUrl());
                                                banner.setImgTitle(entity.getImgTitle());
                                            }
                                        }
                                );
                            }
                        }
                );
            }

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query = new BasicQuery(obj);
            Update update = Update.update("listGroupItem", listGroud);
            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);

            return Msg.ok("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }

    }


    /**
     * 添加明细商品
     *@author ltg
     *@date 2018/8/22 18:41
     * @params:[entity, request]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/addItemGoods")
    public Msg<String> addItemGoods(HomePageGoods entity,HttpServletRequest request) {

        String time = LocalDateTime.now().toString();
        String admin = this.getAdmin(request).getName();
        String moduleId = entity.getModuleId();
        try{
            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(entity.getGoodsCode());
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);

            if (homePageModule == null) {
                return Msg.error("查不到模板");
            }

            if (homePageModule.getActityType()==5){
                Query query=new Query();
                query.addCriteria(Criteria.where("goodsCode").is(entity.getGoodsCode()));
                query.addCriteria(Criteria.where("activityState").is(0));
                query.addCriteria(Criteria.where("companyState").is(0));
                long count =mongoTemplate.count(query,"seckillGoods");
                if (count>0){
                    return Msg.error("不能设置秒杀商品");
                }
            }

            List<HomePageGroupItem> listGroud = new ArrayList<>();

            if (null != homePageModule.getListGroupItem() && homePageModule.getListGroupItem().size() > 0) {
                listGroud.addAll(homePageModule.getListGroupItem());

            }

            if (StringUtil.isEmpty(entity.getId())) {
                entity.setCreateTime(time);
                entity.setUpdateTime(time);
                entity.setCreateAdmin(admin);
                entity.setUpdateAdmin(admin);
                entity.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

                List<HomePageGoods> goodsList = new ArrayList<>();

                for (HomePageGroupItem p:listGroud){
                    if (p.getId().equals(entity.getGroupItemId())) {
                        if (null!=p.getItemListGoods()){
                            for (HomePageGoods homePageGoods:p.getItemListGoods()){
                                if (homePageGoods.getGoodsCode().equals(entity.getGoodsCode())){
                                    return Msg.error("该商品已存在");
                                }
                            }
                            goodsList.addAll(p.getItemListGoods());
                        }
                        goodsList.add(entity);
                        p.setItemListGoods(goodsList);
                    }
                }
            }else {
                for (HomePageGroupItem p:listGroud){
                    if (p.getId().equals(entity.getGroupItemId())) {

                        for (HomePageGoods goods:p.getItemListGoods()){
                            if (!goods.getId().equals(entity.getId())&&goods.getGoodsCode().equals(entity.getGoodsCode())){
                                return Msg.error("该商品已存在");
                            }
                            if (goods.getId().equals(entity.getId())){
                                goods.setUpdateTime(time);
                                goods.setUpdateAdmin(admin);
                                goods.setGoodsCode(entity.getGoodsCode());
                                goods.setSortNum(entity.getSortNum());
                            }
                        }
                    }
                }
            }

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query = new BasicQuery(obj);

            Update update = Update.update("listGroupItem", listGroud);
            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }


    /**
     * 删除组合
     *@author ltg
     *@date 2018/8/23 10:13
     * @params:[entity]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/deleteItem")
    public Msg<String> deleteItem(HomePageGroupItem entity){

        try{
            String moduleId = entity.getModuleId();
            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);
            if (null==homePageModule){
                return Msg.error("查不到模块");
            }

            List<HomePageGroupItem> listGoodsItem=new ArrayList<>();

            List<HomePageGroupItem> listGroupItem= homePageModule.getListGroupItem();
            listGroupItem.forEach(
                    p->{
                        if (!p.getId().equals(entity.getId())){
                            listGoodsItem.add(p);
                        }
                    }
            );

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query = new BasicQuery(obj);

            Update update = Update.update("listGroupItem", listGoodsItem);
            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);

            return Msg.ok("操作成功");


        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }

    /**
     * 删除组合banner
     *@author ltg
     *@date 2018/8/23 10:13
     * @params:[entity]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/deleteItemBanner")
    public Msg<String> deleteItemBanner(HomePageBanner entity){

        try{
            String moduleId = entity.getModuleId();
            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);
            if (null==homePageModule){
                return Msg.error("查不到模块");
            }

            List<HomePageGroupItem> listGroupItem= homePageModule.getListGroupItem();
            listGroupItem.forEach(
                    p->{
                        if (p.getId().equals(entity.getGroupItemId())){
                            List<HomePageBanner> itemListBanner=new ArrayList<>();
                            p.getItemListBanner().forEach(
                                    banner->{
                                        if (!banner.getId().equals(entity.getId())){
                                            itemListBanner.add(banner);
                                        }
                                    }
                            );
                            p.setItemListBanner(itemListBanner);
                        }
                    }
            );

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query = new BasicQuery(obj);

            Update update = Update.update("listGroupItem", listGroupItem);
            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }
    }

    /**
     * 删除组合商品
     *@author ltg
     *@date 2018/8/23 10:13
     * @params:[entity]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/deleteItemGoods")
    public Msg<String> deleteItemGoods(HomePageGoods entity){

        try{
            String moduleId = entity.getModuleId();
            HomePageModule homePageModule = homePageModuleMongo.findOneByObjectId(moduleId, HomePageModule.class);
            if (null==homePageModule){
                return Msg.error("查不到模块");
            }

            List<HomePageGroupItem> listGroupItem= homePageModule.getListGroupItem();
            listGroupItem.forEach(
                    p->{
                        if (p.getId().equals(entity.getGroupItemId())){
                            List<HomePageGoods> itemListGoods=new ArrayList<>();
                            p.getItemListGoods().forEach(
                                    goods->{
                                        if (!goods.getId().equals(entity.getId())){
                                            itemListGoods.add(goods);
                                        }
                                    }
                            );
                            p.setItemListGoods(itemListGoods);

                        }
                    }
            );

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query = new BasicQuery(obj);

            Update update = Update.update("listGroupItem", listGroupItem);
            homePageModuleMongo.updateFirst(query, update, HomePageModule.class);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("内部错误");
        }

    }

    /**
     *@author ltg
     *@date 2018/8/20 18:44
     * @params:[entity, time, admin]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    private Msg<String> addBanner(HomePageBanner entity, String time, String admin) {

        entity.setCreateTime(time);
        entity.setUpdateTime(time);
        entity.setCreateAdmin(admin);
        entity.setUpdateAdmin(admin);
        entity.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

        try{
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(entity.getModuleId(),HomePageModule.class);
            if (homePageModule==null){
                return Msg.error("查不到模板");
            }
            List<HomePageBanner> listBanner=new ArrayList<>();

            if (null!=homePageModule.getListBanner()&&homePageModule.getListBanner().size()>0){
                listBanner.addAll(homePageModule.getListBanner());

            }
            listBanner.forEach(
                    p->{
                        if (p.getSortNum().equals(entity.getSortNum())){
                            p.setSortNum(1000);
                        }
                    }
            );
            listBanner.add(entity);

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query=new BasicQuery(obj);

            Update update=Update.update("listBanner",listBanner);
            homePageModuleMongo.updateFirst(query,update,HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }



    /**
     *@author ltg
     *@date 2018/8/20 19:50
     * @params:[entity, time, admin]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    private Msg<String> addGoods(HomePageGoods entity, String time, String admin) {

        entity.setCreateTime(time);
        entity.setUpdateTime(time);
        entity.setCreateAdmin(admin);
        entity.setUpdateAdmin(admin);
        entity.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));

        try{
            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(entity.getGoodsCode());
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(entity.getModuleId(),HomePageModule.class);
            if (homePageModule==null){
                return Msg.error("查不到模板");
            }

            if (homePageModule.getActityType()==5){
                Query query=new Query();
                query.addCriteria(Criteria.where("goodsCode").is(entity.getGoodsCode()));
                query.addCriteria(Criteria.where("activityState").is(0));
                query.addCriteria(Criteria.where("companyState").is(0));
                long count =mongoTemplate.count(query,"seckillGoods");
                if (count>0){
                    return Msg.error("不能设置秒杀商品");
                }
            }

            List<HomePageGoods> listgoods=new ArrayList<>();

            if (null!=homePageModule.getListGoods()&&homePageModule.getListGoods().size()>0){
                listgoods.addAll(homePageModule.getListGoods());
            }

            for (HomePageGoods homePageGoods:listgoods){
                if (homePageGoods.getSortNum().equals(entity.getSortNum())){
                    homePageGoods.setSortNum(1000);
                }
                if (entity.getGoodsCode().equals(homePageGoods.getGoodsCode())){
                    return Msg.error("该商品已存在");
                }
            }

            listgoods.add(entity);

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query=new BasicQuery(obj);

            Update update=Update.update("listGoods",listgoods);
            homePageModuleMongo.updateFirst(query,update,HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }

    /**
     *@author ltg
     *@date 2018/8/20 18:45
     * @params:[entity, time, admin]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    private Msg<String> updateBanner(HomePageBanner entity, String time, String admin) {

        try{
            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(entity.getModuleId(),HomePageModule.class);
            if (homePageModule==null){
                return Msg.error("查不到模板");
            }
            List<HomePageBanner> listBanner=homePageModule.getListBanner();

            listBanner.forEach(
                    p->{
                        if (p.getSortNum().equals(entity.getSortNum())){
                            p.setSortNum(1000);
                        }

                        if (p.getId().equals(entity.getId())){
                            p.setUpdateAdmin(admin);
                            p.setUpdateTime(time);
                            p.setImgUrl(entity.getImgUrl());
                            p.setImgTitle(entity.getImgTitle());
                            p.setImgTarget(entity.getImgTarget());
                            p.setSortNum(entity.getSortNum());
                        }
                    }
            );

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query=new BasicQuery(obj);

            Update update=Update.update("listBanner",listBanner);
            homePageModuleMongo.updateFirst(query,update,HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }


    /**
     *@author ltg
     *@date 2018/8/20 19:55
     * @params:[entity, time, admin]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    private Msg<String> updateGoods(HomePageGoods entity, String time, String admin) {

        try{
            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(entity.getGoodsCode());
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            HomePageModule homePageModule=homePageModuleMongo.findOneByObjectId(entity.getModuleId(),HomePageModule.class);
            if (homePageModule==null){
                return Msg.error("查不到模板");
            }

            if (homePageModule.getActityType()==5){
                Query query=new Query();
                query.addCriteria(Criteria.where("goodsCode").is(entity.getGoodsCode()));
                query.addCriteria(Criteria.where("activityState").is(0));
                query.addCriteria(Criteria.where("companyState").is(0));
                long count =mongoTemplate.count(query,"seckillGoods");
                if (count>0){
                    return Msg.error("不能设置秒杀商品");
                }
            }

            List<HomePageGoods> listGoods=homePageModule.getListGoods();

            for (HomePageGoods homePageGoods:listGoods){

                if (homePageGoods.getSortNum().equals(entity.getSortNum())){
                    homePageGoods.setSortNum(1000);
                }
                if (!homePageGoods.getId().equals(entity.getId())&&homePageGoods.getGoodsCode().equals(entity.getGoodsCode())){
                    return Msg.error("该商品已存在");
                }
                if (homePageGoods.getId().equals(entity.getId())){
                    homePageGoods.setUpdateAdmin(admin);
                    homePageGoods.setUpdateTime(time);
                    homePageGoods.setGoodsCode(entity.getGoodsCode());
                    homePageGoods.setSortNum(entity.getSortNum());
                }
            }

            BasicDBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId(entity.getModuleId()));
            Query query=new BasicQuery(obj);

            Update update=Update.update("listGoods",listGoods);
            homePageModuleMongo.updateFirst(query,update,HomePageModule.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }


    /**
     *
     *@author ltg
     *@date 2018/10/31 14:20
     * @params:[request, code, sort]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/saveVipGoods")
    public Msg<String> saveVipGoods(HttpServletRequest request,String code,Integer sort){

        String admin=this.getAdmin(request).getName();
        LocalDateTime now=LocalDateTime.now();

        try{
            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(code);
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (null==distributionProduct){
                return Msg.error("查询不到该商品");
            }
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            Goods goods=new Goods();

            goods.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
            goods.setIsSale(1);
            goods.setGoodsCode(code);
            goods.setProductName(distributionProduct.getProductName());
            goods.setCompanyName(distributionProduct.getCompanyName());
            goods.setSortNum(sort);
            goods.setFrom(ProductTypeEnum.ADVAPP.getCode());
            goods.setCreateAdmin(admin);
            goods.setCreateTime(now.toString());
            goods.setModelName(ProductTypeEnum.ADVAPP.getKey());

            Query query=new Query();
            query.addCriteria(Criteria.where("goodsCode").is(code))
                    .addCriteria(Criteria.where("from").is(ProductTypeEnum.ADVAPP.getCode()));

            long count=goodsMongo.selectCount(query,Goods.class);
            if (count>0){
                return Msg.error("商品已存在");
            }
            goodsMongo.insert(goods);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }


    /**
     *
     *@author ltg
     *@date 2018/10/31 17:06
     * @params:[request, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.channel.Goods>
     */
    @RequestMapping("/vipGoodsList")
    public Data<Goods> vipGoodsList(Goods goods,PageDto page){

        Query query = new Query();
        query.addCriteria(Criteria.where("from").is(ProductTypeEnum.ADVAPP.getCode()));
        if (!StringUtils.isEmpty(goods.getGoodsCode())){
            query.addCriteria(Criteria.where("goodsCode").is(goods.getGoodsCode()));
        }
        if (!StringUtils.isEmpty(goods.getProductName())){
            Pattern pattern=Pattern.compile("^.*"+goods.getProductName()+".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("productName").regex(pattern));
//            query.addCriteria(Criteria.where("productName").is(goods.getProductName()));
        }
        query.with(new Sort(Sort.Direction.ASC, "sortNum"));

        PageInfo<Goods> entitys=goodsMongo.findByQueryPage(query,page,goods);
        return Data.ok(entitys);

    }

    @RequestMapping("/deveteVipGoods")
    public Msg<String> deveteVipGoods(String id){

        try{
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            goodsMongo.remove(query,Goods.class);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }

    @RequestMapping("/updateVipSort")
    public Msg<String> updateSort(Goods goods){

        try{
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(goods.getId()));
            Update update=Update.update("sortNum",goods.getSortNum());
            goodsMongo.updateFirst(query,update,Goods.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }

    /**
     * 查询百业惠盟商品列表
     * @param goods
     * @param page
     * @return
     */
    @RequestMapping("/businnessGoodsList")
    public Data<Goods> businnessGoodsList(Goods goods,PageDto page){

        Query query = new Query();
        query.addCriteria(Criteria.where("from").is(ProductTypeEnum.BUSINESS.getCode()));
        if (!StringUtils.isEmpty(goods.getGoodsCode())){
            query.addCriteria(Criteria.where("goodsCode").is(goods.getGoodsCode()));
        }
        if (!StringUtils.isEmpty(goods.getProductName())){
            Pattern pattern=Pattern.compile("^.*"+goods.getProductName()+".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("productName").regex(pattern));
        }
        query.with(new Sort(Sort.Direction.ASC, "sortNum"));

        PageInfo<Goods> entitys=goodsMongo.findByQueryPage(query,page,goods);
        return Data.ok(entitys);

    }

    @RequestMapping("/syncBusinnessGoods")
    public Msg syncBusinnessGoods(Goods goods,PageDto page) {

        Query query = new Query();
        query.addCriteria(Criteria.where("from").is(ProductTypeEnum.BUSINESS.getCode()));
        //所有百业商品
        List<Goods> goodsList = mongoTemplate.find(query, Goods.class);
        if (null == goodsList || goodsList.size() <= 0) {
            return Msg.ok();
        }
        List<String> codeList = new ArrayList<>();
        goodsList.forEach(
                p -> {
                    codeList.add(p.getGoodsCode());
                }
        );
        List<DistributionProduct> list=distributionProductMapper.queryDiatributionProductByCodeList(codeList);
        if (null==list||list.size()<=0){
            return Msg.ok("同步完成");
        }

        List<String> upList=new ArrayList<>();
        List<String> downList=new ArrayList<>();

        if (null != list && list.size() > 0) {
            list.forEach(
                    p -> {
                        if (p.getIsSale()==1){
                            upList.add(p.getProductCode());
                        }
                        if (p.getIsSale()==2){
                            downList.add(p.getProductCode());
                        }
                    }
            );
        }
        if (upList.size()>0){
            Query query3 = new Query();
            query3.addCriteria(Criteria.where("from").is(ProductTypeEnum.BUSINESS.getCode()));
            query3.addCriteria(Criteria.where("goodsCode").in(upList));
            Update update = new Update();
            update.set("isSale", 1);
            mongoTemplate.updateMulti(query3, update, Goods.class);

            Query query1=new Query();
            query1.addCriteria(Criteria.where("goodsCode").in(upList));
            Update update1 = new Update();
            update1.set("ifWYFcommodity", 1);
            mongoTemplate.updateMulti(query1,update1,"distributionGoods");
        }

        if (downList.size()>0){
            Query query4 = new Query();
            query4.addCriteria(Criteria.where("from").is(ProductTypeEnum.BUSINESS.getCode()));
            query4.addCriteria(Criteria.where("goodsCode").in(downList));
            Update update1 = new Update();
            update1.set("isSale", 2);
            mongoTemplate.updateMulti(query4, update1, Goods.class);
        }

        return Msg.ok("操作成功");
    }

    @RequestMapping("/saveBusinessGoods")
    public Msg<String> saveBusinessGoods(HttpServletRequest request,String code,Integer sort){
        String admin=this.getAdmin(request).getName();
        LocalDateTime now=LocalDateTime.now();

        try{
            if (StringUtils.isEmpty(code)){
                return Msg.error("请输入商品code");
            }

            DistributionProduct distributionProductParam=new DistributionProduct();
            distributionProductParam.setProductCode(code);
            DistributionProduct distributionProduct=distributionProductMapper.selectOne(distributionProductParam);
            if (null==distributionProduct){
                return Msg.error("查询不到该商品");
            }
            if (distributionProduct.getIsSale()!=1){
                return Msg.error("未审核或者已下架的商品不能设置");
            }

            Query queryProduct=new Query();
            queryProduct.addCriteria(Criteria.where("goodsCode").is(code));
            queryProduct.addCriteria(Criteria.where("activityState").is(0));
            queryProduct.addCriteria(Criteria.where("companyState").is(0));
            long count1 =mongoTemplate.count(queryProduct,"seckillGoods");
            if (count1>0){
                return Msg.error("不能设置秒杀商品");
            }

            Goods goods=new Goods();
            goods.setId(String.valueOf(System.nanoTime()) + String.format("%02d", new Random().nextInt(99)));
            goods.setIsSale(1);
            goods.setGoodsCode(code);
            goods.setProductName(distributionProduct.getProductName());
            goods.setCompanyName(distributionProduct.getCompanyName());
            goods.setSortNum(sort);
            goods.setFrom(ProductTypeEnum.BUSINESS.getCode());
            goods.setCreateAdmin(admin);
            goods.setCreateTime(now.toString());
            goods.setModelName(ProductTypeEnum.BUSINESS.getKey());
            /**
             * 0：平台商品，1品牌商品
             */
            goods.setType(distributionProduct.getIsstandard()==1? 1:0);

            Query query=new Query();
            query.addCriteria(Criteria.where("goodsCode").is(code))
                    .addCriteria(Criteria.where("from").is(ProductTypeEnum.BUSINESS.getCode()));

            long count=goodsMongo.selectCount(query,Goods.class);
            if (count>0){
                return Msg.error("商品已存在");
            }
            goodsMongo.insert(goods);

            String url=shqApi.getGetBannerUrl()+"localQuickPurchase/goodsMongo/synchroPushGoods";
//            String url="http://192.168.0.50:10000/"+"localQuickPurchase/goodsMongo/synchroPushGoods";

            String requestBody=String.format("{\"goodsCode\":\"%s\",\"is_sale\":%s,\"sort\":%s}",code,5,sort);

            String reqBody=HttpClientUtil.doPostJson(url,requestBody);

            ResBaseDto resBaseDto=JsonUtils.parse(reqBody,ResBaseDto.class);

            if (null==resBaseDto||resBaseDto.getCode()!=200){
                Query query1=new Query();
                query.addCriteria(Criteria.where("_id").is(goods.getId()));
                goodsMongo.deleteOne(query1,Goods.class);
                return Msg.error("操作失败，请求分销接口失败");
            }

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }

    @RequestMapping("/deveteBusinessGoods")
    public Msg<String> deveteBusinessGoods(String id,String code){

        try{
            if (StringUtils.isEmpty(id)){
                return Msg.error("请输入id");
            }
            if (StringUtils.isEmpty(code)){
                return Msg.error("请输入code");
            }

            String url=shqApi.getGetBannerUrl()+"localQuickPurchase/goodsMongo/synchroPushGoods";
//            String url="http://192.168.0.50:10000/"+"localQuickPurchase/goodsMongo/synchroPushGoods";

            String requestBody=String.format("{\"goodsCode\":\"%s\",\"is_sale\":%s}",code,6);

            String reqBody=HttpClientUtil.doPostJson(url,requestBody);

            ResBaseDto resBaseDto=JsonUtils.parse(reqBody,ResBaseDto.class);

            if (null==resBaseDto||resBaseDto.getCode()!=200){
                return Msg.error("操作失败，请求"+url+"失败");
            }
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            goodsMongo.deleteOne(query,Goods.class);

            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }


    @RequestMapping("/updateBusinessSort")
    public Msg<String> updateBusinessSort(Goods goods){

        try{
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(goods.getId()));
            Update update=Update.update("sortNum",goods.getSortNum());

            String url=shqApi.getGetBannerUrl()+"localQuickPurchase/goodsMongo/synchroPushGoods";
//            String url="http://192.168.0.50:10000/"+"localQuickPurchase/goodsMongo/synchroPushGoods";

            String requestBody=String.format("{\"goodsCode\":\"%s\",\"is_sale\":%s,\"sort\":%s}",goods.getGoodsCode(),5,goods.getSortNum());

            String reqBody=HttpClientUtil.doPostJson(url,requestBody);

            ResBaseDto resBaseDto=JsonUtils.parse(reqBody,ResBaseDto.class);

            if (null==resBaseDto||resBaseDto.getCode()!=200){
                return Msg.error("操作失败，请求"+url+"失败");
            }

            goodsMongo.updateFirst(query,update,Goods.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }
    }

    @RequestMapping("/searchGoods")
    public Data<DistributionProduct> searchGoods(String code){

        List<DistributionProduct> Productlist=new ArrayList<>();
        try{
            if (StringUtils.isEmpty(code))
                return Data.error("商品Code不能为空");

            List<String> codeList=new ArrayList<>();
            codeList.add(code);
            List<DistributionProduct> list=distributionProductMapper.queryDiatributionProductByCodeList(codeList);
            if (null==list||list.size()<=0){
                return new Data<DistributionProduct>("false",Productlist,1,null);
            }
            return new Data<DistributionProduct>("true",list,1,null);

        }catch (Exception e){
            e.printStackTrace();
            return new Data<DistributionProduct>("false",Productlist,1,null);
        }
    }




}
