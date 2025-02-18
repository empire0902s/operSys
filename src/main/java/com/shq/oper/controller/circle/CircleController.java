package com.shq.oper.controller.circle;

import com.github.pagehelper.Page;
import com.shq.oper.controller.BaseController;
import com.shq.oper.mapper.primarydb.CircleMapper;
import com.shq.oper.mapper.shq520new.DistributionProductMapper;
import com.shq.oper.model.domain.primarydb.Circle;
import com.shq.oper.model.domain.shq520new.DistributionProduct;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.res.ResBaseDto;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.impl.primarydb.AdminServiceImpl;
import com.shq.oper.service.primarydb.CircleService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 15:56 2018/10/24
 */

@RestController
@Slf4j
@RequestMapping("/circle")
public class CircleController extends BaseController {


    @Autowired
    private ShqApi shqApi;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleService circleService;

    @Autowired
    private DistributionProductMapper distributionProductMapper;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);

    }

    /**
     *@author ltg
     *@date 2018/10/25 10:57
     * @params:[request, circle]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @Transactional
    @RequestMapping("/save")
    public Msg<String> save(HttpServletRequest request, Circle circle){

        String admin=this.getAdmin(request).getName();
        LocalDateTime now=LocalDateTime.now();

        if (!StringUtils.isEmpty(circle.getCommoditySku())){
            String url="";
            if (circle.getGoodsType()==1){
                url=shqApi.getVerificationSku()+"/localQuickPurchase/propagandaCircleAction/checkGoodsIsExist?goodsSku="+circle.getCommoditySku();
            }else if (circle.getGoodsType()==2){
                url=shqApi.getVerificationSku()+"/localQuickPurchase/sgMongoAction/findSeckillGoodsByCode?goodsCode="+circle.getCommoditySku();
            }

            String result=HttpClientUtil.doGet(url);
            ResBaseDto  resBaseDto=JsonUtils.parse(result,ResBaseDto.class);
            if (null==resBaseDto||resBaseDto.getCode()!=200){
                return Msg.error("请选择适合的商品类型以及正确的商品code");
            }
        }

        if (StringUtils.isEmpty(circle.getId())){
            circle.setCreateAdmin(admin);
            circle.setCreateTime(now);
            circle.setUpdateAdmin(admin);
            circle.setUpdateTime(now);
            circle.setRealShare(0);
            circle.setShareNum(0);
            circle.setClickPeopleNum(0);
            circle.setRealShare(0);
            circleService.insert(circle);
        }else {
            circle.setUpdateAdmin(admin);
            circle.setUpdateTime(now);
            circleService.update(circle);
        }

        //清除的缓存
        SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_CIRCLE_LIST);
        return Msg.ok("操作成功");
    }


    /**
     *@author ltg
     *@date 2018/10/26 13:48
     * @params:[circle]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/delete")
    public Msg<String> delete( Circle circle){

        circleService.delete(circle);

        //清除的缓存
        SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_CIRCLE_LIST);
        return Msg.ok("操作成功");
    }


    @RequestMapping("/list")
    public Data<Circle> list(Circle entity, PageDto page) {

        if (!StringUtils.isEmpty(entity.getTimeRange())) {
            String[] strings = entity.getTimeRange().split(" - ");
            // 拼接start:xx-xx-xx :00:00:00 到 end：xx-xx-xx 23：59：59
            entity.setBeginTime(DateUtils.parse(strings[0] + " 00:00:00"));
            entity.setEndTime(DateUtils.parse(strings[1] + " 23:59:59"));
        }

        com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
        Page<Circle> entitys = circleMapper.queryPageSort(entity);
        return Data.ok(entitys);
    }


    /**
     *@author ltg
     *@date 2018/10/26 13:48
     * @params:[circle]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/updateShareNum")
    public Msg<String> updateShareNum( Circle circleParam){

        if (StringUtils.isEmpty(circleParam.getId())||StringUtils.isEmpty(circleParam.getShareNum())){
            return Msg.error("参数错误");
        }
        Circle circle=new Circle();
        circle.setId(circleParam.getId());
        circle.setShareNum(circleParam.getShareNum());
        circleService.update(circle);

        //清除的缓存
        SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_CIRCLE_LIST);
        return Msg.ok("操作成功");
    }



}
