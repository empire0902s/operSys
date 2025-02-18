package com.shq.oper.controller.deductible;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.model.domain.primarydb.deductible.Deductible;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.service.primarydb.DeductibleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: ltg
 * @date: Created in 10:09 2018/11/19
 */

@RestController
@Slf4j
@RequestMapping("/deductible/deductible")
public class DeductibleController extends BaseController {

    @Autowired
    private DeductibleService deductibleService;

    @RequestMapping("index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);
    }



    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Data<Deductible> list(Deductible entity, PageDto page) {
//        PageHelper.startPage(page.getPage(), page.getLimit());
        entity.setType(2);
        PageInfo<Deductible> entitys=deductibleService.selectPageAndCount(entity,page.getPage(),page.getLimit());
        //PageInfo<Deductible> entitys = deductibleIntroduceService.selectPageAndCount(entity, page.getPage(), page.getLimit());
        return Data.ok(entitys);
    }



}
