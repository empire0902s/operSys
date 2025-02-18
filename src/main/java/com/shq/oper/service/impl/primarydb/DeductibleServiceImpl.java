package com.shq.oper.service.impl.primarydb;

import com.shq.oper.dao.MongoDao;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.DeductibleMapper;
import com.shq.oper.model.domain.mongo.*;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.req.BusinessParamDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsBusinessDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsDeductible;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.DeductibleService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.shq.oper.util.CacheKeyConstant.BASE_ORDER_RETURN;

/**
 * @author: ltg
 * @date: Created in 14:28 2018/11/17
 */
@Service("deductibleService")
public class DeductibleServiceImpl extends BaseServiceImpl<Deductible, Long> implements DeductibleService {

    @Autowired
    private ShopperMapper shopperMapper;

    @Autowired
    private DeductibleMapper deductibleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private MongoDao<AdminActionLog> mongoDao;

    @Override
    public Msg<String> giftUserDeductible(ReqUserCouponsBusinessDto dto) {
        List<BusinessParamDto> businessParamList=dto.getBusinessParam();
        for (BusinessParamDto businessParamDto:businessParamList){
            if (StringUtils.isEmpty(businessParamDto.getSeq())){
                return Msg.error("参数错误，请输入seq");
            }
            if (StringUtils.isEmpty(businessParamDto.getCouponType())){
                return Msg.error("参数错误，请输入优惠券类型");
            }

            BigDecimal big_decimal=new BigDecimal(businessParamDto.getAmount());
            int r=big_decimal.compareTo(BigDecimal.ZERO); //和0，Zero比较
            if (r<=0){
                return Msg.error("金额参数错误，必须大于0");
            }
        }

        LocalDateTime now=LocalDateTime.now();
        String time="-"+now.toString().split("T")[0];
        List<Deductible> list=new ArrayList<>();
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        List<DeductibleUseDetail> deductibleUseDetailList=new ArrayList<>();

        for (BusinessParamDto businessParamDto:businessParamList) {

            if (businessParamDto.getCouponType().equals("1")){
                Shopper shopperParam = new Shopper();
                shopperParam.setSeq(Integer.parseInt(businessParamDto.getSeq()));
                Shopper shopper = shopperMapper.selectOne(shopperParam);
                if (null == shopper) {
                    return Msg.error("查询不到seq" + businessParamDto.getSeq() + "为用户");
                }

                Deductible deductible = new Deductible();
                deductible.setAmount(new BigDecimal(businessParamDto.getAmount()));
                deductible.setBalance(new BigDecimal(businessParamDto.getAmount()));
//                deductible.setBalance(BigDecimal.ZERO);
                deductible.setCreateAdmin(dto.getFromSysCode());
                deductible.setCreateTime(now);
                deductible.setStatus((short) 1);
                deductible.setUpdateAdmin(dto.getFromSysCode());
                deductible.setUpdateTime(begin);
                deductible.setUsedBalance(BigDecimal.ZERO);
                deductible.setUserSeq(businessParamDto.getSeq());
                deductible.setValidayStart(begin);
                deductible.setValidayEnd(begin.plusYears(100));
                deductible.setType(1);
                deductible.setIsLocking(1);


                andDeductibleDetail(deductibleUseDetailList, deductible, 1);
//                mongoTemplate.insert(deductibleUseDetail);


                //清除CouponsUser-userSeq- 的缓存
                SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+deductible.getUserSeq());
                Deductible deductibleParam = new Deductible();
                deductibleParam.setUserSeq(businessParamDto.getSeq());
                deductibleParam.setType(1);
                Deductible deductible1=deductibleMapper.selectOne(deductibleParam);
                if (null==deductible1){
                    list.add(deductible);
                }else {
                    deductible1.setUpdateTime(now);
                    deductible1.setStatus((short)2);
                    deductible1.setBalance(deductible1.getBalance().add(new BigDecimal(businessParamDto.getAmount())));
                    deductible1.setAmount(deductible1.getAmount().add(new BigDecimal(businessParamDto.getAmount())));
                    deductibleMapper.updateByPrimaryKey(deductible1);
                    //清除CouponsUser-userSeq- 的缓存
                    SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+deductible.getUserSeq());
                    continue;
                }



            }
            if (businessParamDto.getCouponType().equals("2")){
                Shopper shopperParam = new Shopper();
                shopperParam.setSeq(Integer.parseInt(businessParamDto.getSeq()));
                Shopper shopper = shopperMapper.selectOne(shopperParam);
                if (null == shopper) {
                    return Msg.error("查询不到seq" + businessParamDto.getSeq() + "为用户");
                }

                Deductible deductible = new Deductible();
                deductible.setAmount(new BigDecimal(businessParamDto.getAmount()));
                deductible.setBalance(new BigDecimal(businessParamDto.getAmount()));
                deductible.setCreateAdmin(dto.getFromSysCode());
                deductible.setCreateTime(now);
                deductible.setStatus((short) 1);
                deductible.setUpdateAdmin(dto.getFromSysCode());
                deductible.setUpdateTime(begin);
                deductible.setUsedBalance(BigDecimal.ZERO);
                deductible.setUserSeq(businessParamDto.getSeq());
                deductible.setValidayStart(begin);
                deductible.setValidayEnd(begin.plusYears(1));
                deductible.setType(2);
                deductible.setIsLocking(1);

                Deductible deductibleParam = new Deductible();
                deductibleParam.setUserSeq(businessParamDto.getSeq());
                deductibleParam.setType(2);

                int i=deductibleMapper.selectCount(deductibleParam);
                if (i==0){
                    andDeductibleDetail(deductibleUseDetailList, deductible, 2);

                    list.add(deductible);
                    String key=CacheKeyConstant.BASE_DEDUCTIBLE_SEQ_LIST+time;
                    redisTemplate.opsForSet().add(key,deductible.getUserSeq());
                    //清除CouponsUser-userSeq- 的缓存
                    SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+deductible.getUserSeq());
                }
            }
        }

       if (list.size()>0){
           deductibleMapper.insertList(list);
           mongoTemplate.insertAll(deductibleUseDetailList);
           return Msg.ok("赠送券成功");
       }else {
           return Msg.ok("已赠送过券");
       }
    }

    private void andDeductibleDetail(List<DeductibleUseDetail> deductibleUseDetailList, Deductible deductible, int i2) {
        DeductibleUseDetail deductibleUseDetail = new DeductibleUseDetail();
        deductibleUseDetail.setCreateTime(LocalDateTime.now().toString());
        deductibleUseDetail.setDeductibleAmount(deductible.getAmount().doubleValue());
        deductibleUseDetail.setDeducitbleType(i2);
        deductibleUseDetail.setUserSeq(deductible.getUserSeq());
        deductibleUseDetailList.add(deductibleUseDetail);
    }

    @Override
    public Deductible getDeductible(String userSeq) {
        return deductibleMapper.selectDeductible(userSeq);

    }

    @Override
    public Msg<String> usedDeductible(ReqUserCouponsDeductible dto) {
        LocalDateTime now=LocalDateTime.now();
        String time="-"+now.toString().split("T")[0];
        Deductible deductibleParam=new Deductible();
        deductibleParam.setId(dto.getCouponsUserId());
        Deductible deductible= deductibleMapper.selectByPrimaryKey(dto.getCouponsUserId());
        if (null==deductible){
            return Msg.error("查询不到该抵扣券");
        }
        dto.setCouponsType(deductible.getType());

        String  returnKey=CacheKeyConstant.BASE_ORDER_RETURN+dto.getUseOrderNo();
        boolean isreturn=false;
        if (dto.getType()==1){
            if (deductible.getBalance().compareTo(dto.getDeductibleAmount())==-1){
                return Msg.error("抵扣金额超过限制");
            }

            switch (deductible.getStatus()){
                case 3:return Msg.error("该券已用完");
                case 4:return Msg.error("该券已过期");
                case 5:return Msg.error("该券升级清空");
            }

            if (deductible.getValidayStart().isAfter(now)){
                return Msg.error("该券没到使用期");
            }
            if (deductible.getValidayEnd().isBefore(now)){
                return Msg.error("该券已过期");
            }

            //已抵扣金额
            BigDecimal usedBalance=deductible.getUsedBalance().add(dto.getDeductibleAmount());
            int r=usedBalance.compareTo(deductible.getAmount());
            if (r==1){
                return Msg.error("抵扣金额超过抵扣券金额");
            }
            if (r==0){
                deductibleParam.setStatus((short)3);
            }
            if (r==-1){
                deductibleParam.setStatus((short)2);
            }
            deductibleParam.setUsedBalance(usedBalance);
            deductibleParam.setBalance(deductible.getAmount().subtract(usedBalance));

            redisTemplate.opsForValue().append(returnKey,"true");
            redisTemplate.expire(returnKey,3000 , TimeUnit.SECONDS);

        }else if(dto.getType()==2){
            returnKey=CacheKeyConstant.BASE_ORDER_RETURN+dto.getUseOrderNo();
            String str=redisTemplate.opsForValue().get(returnKey).toString();
            if (null==str){
                return Msg.error("还原失败");
            }
            if (!"true".equals(str)){
                return Msg.ok("还原成功");
            }{
                isreturn=true;
            }

            if (deductible.getStatus()==4){
                return Msg.error("该券已过期");
            }
            if (deductible.getStatus()==5){
                return Msg.ok("该券已升级清空");
            }

            deductibleParam.setStatus((short)2);
            deductibleParam.setBalance(deductible.getBalance().add(dto.getDeductibleAmount()));
            deductibleParam.setUpdateTime(now);
            deductibleParam.setUsedBalance(deductible.getUsedBalance().subtract(dto.getDeductibleAmount()));

        }

        int i=deductibleMapper.updateUserDeductible(deductibleParam);

        if (i<=0){
            return Msg.error("操作失败");
        }else {
            //缓存key
            String key="";
            String setkey="";
            if (deductible.getType()==1){
                key=CacheKeyConstant.BASE_DEDUCTIBLE_USEDBALANCE_1+time;
                setkey=CacheKeyConstant.BASE_DEDUCTIBLE_USERSEQ_LIST_1+time;
            }
            if (deductible.getType()==2){
                key=CacheKeyConstant.BASE_DEDUCTIBLE_USEDBALANCE_2+time;
                setkey=CacheKeyConstant.BASE_DEDUCTIBLE_USERSEQ_LIST_2+time;
            }

            if (dto.getType()==1){
                redisTemplate.opsForValue().increment(key, dto.getDeductibleAmount().longValue());
            }
            if (dto.getType()==2){
                redisTemplate.opsForValue().increment(key, -dto.getDeductibleAmount().longValue());
            }

            redisTemplate.opsForSet().add(setkey, dto.getUserSeq());

            if (isreturn){
                redisTemplate.delete(returnKey);
            }

            DeductibleUseDetail deductibleUseDetail=new DeductibleUseDetail();
            deductibleUseDetail.setCreateTime(LocalDateTime.now().toString());
            deductibleUseDetail.setDeducitbleId(dto.getCouponsUserId());
            deductibleUseDetail.setDeductibleAmount(dto.getDeductibleAmount().doubleValue());
            deductibleUseDetail.setType(dto.getType());
            deductibleUseDetail.setUseOrderNo(dto.getUseOrderNo());
            deductibleUseDetail.setUserSeq(dto.getUserSeq());
            mongoTemplate.insert(deductibleUseDetail);

            //清除CouponsUser-userSeq- 的缓存
            SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_COUPONSUSER_USERSEQ+deductible.getUserSeq());
            return Msg.ok("操作成功");
        }
    }

    /**定时任务，抵扣券券过期 状态4过期**/
    @Override
    public Msg<String> updateDetuctibleStatusExpiredByJob(String expiredDay) {
        int num = deductibleMapper.updateDetuctibleStatusExpiredByJob(expiredDay);

        return Msg.ok("过期优惠券["+num+"]条记录", "");
    }


    /**
     *保存抵扣券代金券日统计
     * @param localDateTime
     * @return
     */
    @Override
    public Msg<String> saveDeducitbleStatisticsDay(LocalDateTime localDateTime) {

        try{
            localDateTime=DateUtils.addDaysFormatStart(localDateTime,0);
            String timeStr=localDateTime.toString().split("T")[0];
            String time="-"+timeStr;
            LocalDateTime endtime=localDateTime.plusDays(1);
            String beTime=DateUtils.to(localDateTime,DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);
            String endTime=DateUtils.to(endtime,DateUtils.DateFormat.LONG_DATE_PATTERN_LINE);

            //代金券
            DeducitbleStatisticsDay deducitbleStatisticsDay1=getCashStatisisDay(beTime, endTime,time);

            //抵扣券
            DeducitbleStatisticsDay deducitbleStatisticsDay2 = getDeducitbleStatisticsDay(localDateTime, endtime, beTime, endTime,time);

            List<DeducitbleStatisticsDay> list=new ArrayList<>();
            list.add(deducitbleStatisticsDay1);
            list.add(deducitbleStatisticsDay2);

            Query query1=new Query();
            query1.addCriteria(Criteria.where("statisticsTime").regex(".*?" +timeStr+ ".*"));
            query1.addCriteria(Criteria.where("type").is(1));
            long l=mongoTemplate.count(query1,DeducitbleStatisticsDay.class);
            if (l>0){
                mongoTemplate.remove(query1,DeducitbleStatisticsDay.class);
//                mongoTemplate.remove(query1);
            }
            Query query2=new Query();
            query2.addCriteria(Criteria.where("statisticsTime").regex(".*?" +timeStr+ ".*"));
            query2.addCriteria(Criteria.where("type").is(2));
            long l2=mongoTemplate.count(query2,DeducitbleStatisticsDay.class);
            if (l2>0){
//                mongoTemplate.remove(query2);
                mongoTemplate.remove(query2,DeducitbleStatisticsDay.class);
            }
            mongoTemplate.insert(list,DeducitbleStatisticsDay.class);

            return Msg.ok("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return Msg.error("操作异常");
        }

    }

    private DeducitbleStatisticsDay getDeducitbleStatisticsDay(LocalDateTime betime, LocalDateTime endtime, String beTime, String endTime,String time) throws ParseException {
        DeducitbleStatisticsDay deducitbleStatisticsDay2=deductibleMapper.getDeducitbleStatisticsDay(beTime,endTime,2);
        if (null==deducitbleStatisticsDay2){
            deducitbleStatisticsDay2=new DeducitbleStatisticsDay();
        }

        //抵扣券key
        String key2=CacheKeyConstant.BASE_DEDUCTIBLE_USEDBALANCE_2+time;
        //抵扣券使用seqlist key
        String setkey=CacheKeyConstant.BASE_DEDUCTIBLE_USERSEQ_LIST_2+time;

        SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String start=beTime+" 00:00:00";
        String etime=endTime+ " 00:00:00";
        Query queryUo=new Query();
        queryUo.addCriteria(Criteria.where("createTime").gte(format.parse(start)).lte(format.parse(etime)));
        queryUo.addCriteria(Criteria.where("everyProfession").is(1));

        long loseNum=0;

        List<UpgradeOrder> list1=mongoTemplate.find(queryUo,UpgradeOrder.class,"upgradeOrder");
//        mongoTemplate.f

        if (list1!=null&&list1.size()>0){
            List<Long> list2=new ArrayList<>();
            list1.forEach(
                    p->{
                        list2.add(p.getUserSeq());
                    }
            );

            Query query=new Query();
            query.addCriteria(Criteria.where("everyProfession").ne(1));
            query.addCriteria(Criteria.where("distributorSeq").in(list2));
            loseNum=mongoTemplate.count(query,"distributorApplication");
        }

        Object o=redisTemplate.opsForValue().get(key2);
        double usedAmount=0;
        if (!StringUtils.isEmpty(o)){
            usedAmount=Double.parseDouble(o.toString());
        }
        long l=redisTemplate.opsForSet().size(setkey);

        deducitbleStatisticsDay2.setUsedAmount(usedAmount);
        deducitbleStatisticsDay2.setUsedNum(l);
        deducitbleStatisticsDay2.setCreateTime(LocalDateTime.now().toString());
        deducitbleStatisticsDay2.setLoseNum(loseNum);
        deducitbleStatisticsDay2.setType(2);
        deducitbleStatisticsDay2.setStatisticsTime(beTime);

        return deducitbleStatisticsDay2;
    }

    private DeducitbleStatisticsDay getCashStatisisDay(String beTime, String endTime,String time) {
        DeducitbleStatisticsDay deducitbleStatisticsDay1=null;

        //使用代金券金额key
        String key=CacheKeyConstant.BASE_DEDUCTIBLE_USEDBALANCE_1+time;
        //代金券使用用户seq key
        String setkey1=CacheKeyConstant.BASE_DEDUCTIBLE_USERSEQ_LIST_1+time;
        Object o=redisTemplate.opsForValue().get(key);
        long l=redisTemplate.opsForSet().size(setkey1);

        Double usedAmount=0.0;
        if (!StringUtils.isEmpty(o)){
            usedAmount=Double.parseDouble(o.toString());
        }
        //代金券
        deducitbleStatisticsDay1=deductibleMapper.getDeducitbleStatisticsDay(beTime,endTime,1);

        if (null==deducitbleStatisticsDay1){
            deducitbleStatisticsDay1=new DeducitbleStatisticsDay();
        }
        deducitbleStatisticsDay1.setUsedNum(l);
        deducitbleStatisticsDay1.setUsedAmount(usedAmount);
        deducitbleStatisticsDay1.setCreateTime(LocalDateTime.now().toString());
        deducitbleStatisticsDay1.setType(1);
        deducitbleStatisticsDay1.setStatisticsTime(beTime);

        return deducitbleStatisticsDay1;
    }
}
