package com.shq.oper.service.impl.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.CircleMapper;
import com.shq.oper.model.domain.primarydb.Circle;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.req.ReqCircleDataDto;
import com.shq.oper.plugin.SpringContextHolder;
import com.shq.oper.service.primarydb.CircleService;
import com.shq.oper.util.CacheKeyConstant;
import com.shq.oper.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("circleService")
public class CircleServiceImpl extends BaseServiceImpl<Circle, Long> implements CircleService {

    @Autowired
    private CircleMapper circleMapper;

    @Transactional
    @Override
    public Msg<String> updateSetCircle(ReqCircleDataDto circle) {

        circleMapper.updateSetCircle(circle);

        //清除CouponsUser-userSeq- 的缓存
        SpringContextHolder.getBean(AdminServiceImpl.class).clearCacheByConstantKey(CacheKeyConstant.BASE_CIRCLE_LIST);

        return Msg.ok("操作成功");
    }

    @Cacheable(value = Constant.CACHEKEY_MINUTE_20, key = "T(com.shq.oper.util.CacheKeyConstant).BASE_CIRCLE_LIST +#circle.type +#page.page+#page.limit",unless = "#result eq null")
    @Override
    public Page<Circle> selectCirclePage(Circle circle, PageDto page) {
        com.github.pagehelper.PageHelper.startPage(page.getPage(), page.getLimit());
        Page<Circle> entitys= circleMapper.queryPageSort(circle);
        return entitys;
    }

}