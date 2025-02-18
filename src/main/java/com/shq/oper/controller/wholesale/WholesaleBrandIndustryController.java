package com.shq.oper.controller.wholesale;

import com.github.pagehelper.PageInfo;
import com.shq.oper.controller.BaseController;
import com.shq.oper.dao.mongo.WholesaleIndustryMongo;
import com.shq.oper.model.domain.mongo.wholesalebrand.WholesaleIndustry;
import com.shq.oper.model.dto.Data;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.model.dto.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 11:17 2018/11/15
 */
@RestController
@Slf4j
@RequestMapping("/wholesale/brandindustry")
public class WholesaleBrandIndustryController extends BaseController {

    @Autowired
    private WholesaleIndustryMongo wholesaleIndustryMongo;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        return toPage(request);
    }


    /**
     * 保存行业类型
     *@author ltg
     *@date 2018/11/15 13:57
     * @params:[request, entity]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/save")
    public Msg<String> saveIndustry(HttpServletRequest request,WholesaleIndustry entity){

        String admin=this.getAdmin(request).getName();
        LocalDateTime now=LocalDateTime.now();

        try{
            if (StringUtils.isEmpty(entity.getId())){
                entity.setId(System.currentTimeMillis()+"");
                entity.setCreateAdmin(admin);
                entity.setCreateTime(now.toString());
                wholesaleIndustryMongo.insert(entity);
            }else {
                Query query=new Query();
                query.addCriteria(Criteria.where("_id").is(entity.getId()));
                Update update=new Update();
                update.set("name",entity.getName());
                update.set("sort",entity.getSort());
                update.set("isShow",entity.getIsShow());
                wholesaleIndustryMongo.updateFirst(query,update,WholesaleIndustry.class);

            }
            return Msg.ok("操作成功");

        }catch (Exception e){
            return Msg.error("操作失败");
        }
    }

    /**
     * 获取行业类型列表
     *@author ltg
     *@date 2018/11/15 14:09
     * @params:[entity, page]
     * @return: com.shq.oper.model.dto.Data<com.shq.oper.model.domain.mongo.wholesalebrand.WholesaleIndustry>
     */
    @RequestMapping("/list")
    public Data<WholesaleIndustry> getWholesaleIndustryList(WholesaleIndustry entity, PageDto page){

        try{
            Query query=new Query();
            if (!StringUtils.isEmpty(entity.getIsShow())){
                query.addCriteria(Criteria.where("isShow").is(entity.getIsShow()));
            }
            if (!StringUtils.isEmpty(entity.getName())){
                query.addCriteria(Criteria.where("name").is(entity.getName()));
            }

            query.with(new Sort(Sort.Direction.ASC,"sort"));
            PageInfo<WholesaleIndustry> pageInfo=wholesaleIndustryMongo.findByQuery(query,page,WholesaleIndustry.class);

            return Data.ok(pageInfo);

        }catch (Exception e){
            return Data.ok(new  PageInfo<WholesaleIndustry>());
        }
    }


    /**
     * 删除行业
     *@author ltg
     *@date 2018/11/15 14:14
     * @params:[entity]
     * @return: com.shq.oper.model.dto.Msg<java.lang.String>
     */
    @RequestMapping("/delete")
    public Msg<String> deleteWholesaleIndustry(WholesaleIndustry entity){

        try{
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(entity.getId()));
            wholesaleIndustryMongo.deleteOne(query,WholesaleIndustry.class);
            return Msg.ok("操作成功");

        }catch (Exception e){
            return Msg.error("操作失败");
        }
    }

}
