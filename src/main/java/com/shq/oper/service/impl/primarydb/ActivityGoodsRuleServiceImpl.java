package com.shq.oper.service.impl.primarydb;

import com.shq.oper.mapper.primarydb.ActivityGoodsRuleMapper;
import com.shq.oper.mapper.primarydb.CouponsMapper;
import com.shq.oper.model.domain.primarydb.ActivityGoodsRule;
import com.shq.oper.model.domain.primarydb.Coupons;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.ActivityGoodsRuleService;

import com.shq.oper.util.CacheKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service("activityGoodsRuleService")
public class ActivityGoodsRuleServiceImpl extends BaseServiceImpl<ActivityGoodsRule, Long> implements ActivityGoodsRuleService {

    @Autowired
    private ActivityGoodsRuleMapper activityGoodsRuleMapper;

    @Autowired
    private CouponsMapper couponsMapper;
    /**
     * 商品规则禁用，把该规则关联的优惠券也禁用
     *@author ltg
     *@date 2018/6/15 11:34
     * @params:[activity]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @Transactional
    @Override
    public Msg<String> disabled(ActivityGoodsRule activityGoodsRule){

        activityGoodsRuleMapper.disabledById(activityGoodsRule);
        Coupons coupons=new Coupons();
        coupons.setCategoryRuleId(new BigDecimal(activityGoodsRule.getId()));
        List<Coupons> couponsList=couponsMapper.select(coupons);
        if (couponsList==null||couponsList.size()<=0){
            return Msg.ok("禁用成功");
        }
        couponsList.forEach(
                p->{
                    p.setUpdateAdmin(activityGoodsRule.getUpdateAdmin());
                    p.setUpdateTime( activityGoodsRule.getUpdateTime());
                }
        );
        couponsMapper.updateBath(couponsList);
        SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_GOODS_RULE_QUERY);
        SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONS_QUERY);
        return Msg.ok("禁用成功");
    }

}