package com.shq.oper.service.impl.primarydb;

import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.DeductibleMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduceDetail;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.api.req.BusinessParamDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsBusinessDto;
import com.shq.oper.model.dto.api.req.ReqUserCouponsDeductible;
import com.shq.oper.service.primarydb.DeductibleIntroduceDetailService;
import com.shq.oper.service.primarydb.DeductibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 14:28 2018/11/17
 */
@Service("deductibleIntroduceDetailService")
public class DeductibleIntroduceDetailServiceImpl extends BaseServiceImpl<DeductibleIntroduceDetail, Long> implements DeductibleIntroduceDetailService {


}
