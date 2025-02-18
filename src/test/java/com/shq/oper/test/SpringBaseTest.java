package com.shq.oper.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.mssqlshq.ShopperMapper;
import com.shq.oper.mapper.primarydb.AdminMapper;
import com.shq.oper.mapper.primarydb.DictMapper;
import com.shq.oper.mapper.primarydb.ResourceMaper;
import com.shq.oper.mapper.shq520new.DistrFourCategotyMapper;
import com.shq.oper.model.domain.mssqlshq.Shopper;
import com.shq.oper.model.domain.primarydb.Admin;
import com.shq.oper.model.domain.shq520new.DistrFourCategoty;
import com.shq.oper.model.dto.PageDto;
import com.shq.oper.model.dto.api.BaseResponseResultDto;
import com.shq.oper.model.dto.api.goods.Distribution;
import com.shq.oper.model.dto.yml.ShqApi;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.util.HttpClientUtil;
import com.shq.oper.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * SpringBaseTest
 *
 * @author tjun
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringBaseTest {
	
	@Autowired
	private DistrFourCategotyMapper distrFourCategotyMapper;
	@Autowired
	private ShopperMapper shopperMapper;
	@Autowired
	private DictMapper dictMapper;
	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private AdminService adminService;
	
	@Autowired
    private ResourceMaper resourceMaper;
	
	@Autowired
	private ShqApi ShqApi;
	
	@Test
	public void testPage() {
		
		System.out.println("=====================");
//		System.out.println(JsonUtils.toDefaultJson(dictMapper.selectAll()));
//		System.out.println(JsonUtils.toDefaultJson(shopperMapper.selectByIds("5757774")));
//		System.out.println(JsonUtils.toDefaultJson(distrFourCategotyMapper.selectAll()));
		
		Shopper queryBySupplier = new Shopper();
		queryBySupplier.setShopname("自己专用一");
		shopperMapper.queryBySupplier(queryBySupplier);
		
		Shopper shop = new Shopper();
		shop.setPageNumKey(3);
		shop.setPageSizeKey(7);
		Page<Shopper> listshop = (shopperMapper.queryBy(shop));
//		System.out.println(JsonUtils.toDefaultJson(listshop));
		
		DistrFourCategoty dis = new DistrFourCategoty();
		dis.setPageNumKey(3);
		dis.setPageSizeKey(7);
		Page<DistrFourCategoty> list_dis = (distrFourCategotyMapper.queryByPage(dis));

//		System.out.println("=====================");
//		Resource resource = new Resource();
//		resource.setAdminId(1L);
//		Page<Resource> resourceList = resourceMaper.queryResourceeByAdmin(resource);
//		System.out.println(JsonUtils.toDefaultJson(resourceList));
//		System.out.println("=====================");
//		Admin entity = new Admin();
//		entity.setIsDisabled(false);
//		entity.setName("");
//		entity.setPhone("");
//		System.out.println(adminService.selectPageAndCount(entity , page.getPage(), page.getLimit()));
		
//		System.out.println("=====================");
//		adminService.insertDict();
		

	}
	@Test
	public void testDisabled() {

		Admin admin = new Admin();
		admin.setId(2L);
		LocalDateTime dtNow = LocalDateTime.now();
		admin.setUpdateAdmin("1-admin");
		admin.setUpdateTime(dtNow);
		admin.setIsDisabled(true);
		int result = adminService.disabledById(admin);
		System.out.println("========"+result);
		
	}
	  
	
	
	@Test
	public void testAPI() {
		
		System.out.println(ShqApi.getTestNum());
		String url = ShqApi.getNetGoodsApi().getBaseUrl().concat(ShqApi.getNetGoodsApi().getGetGoodsByCode()).replace("{strCode}", "3728815201803280936193619131477");
		log.debug("====url===="+url);
		String resultJson = HttpClientUtil.doGet(url);
		log.debug("======="+resultJson);
		BaseResponseResultDto dto = JsonUtils.parse(resultJson, BaseResponseResultDto.class);
		log.debug(""+dto.isSuccessCodeBy200());
		log.debug("===>>>>>DTO===="+JsonUtils.toDefaultJson(dto));

		Map<String,Object> map = JsonUtils.parse( dto.getResult().toString(),HashMap.class);
		Distribution disDto = JsonUtils.parse(map.get("Distribution").toString(),Distribution.class);
		log.debug("--map--------"+disDto);
		
	}
	
	
	
	@Test
	public void testBase64API() {
		
		System.out.println(ShqApi.getTestNum());
		String url = "http://192.168.1.36:8022/api/UploadFile/UploadFileByYY";
		log.debug("====url===="+url);
		Map<String,Object> map = new HashMap<>();
		String resultJson = HttpClientUtil.doPostJson(url, JsonUtils.toDefaultJson(map));
		log.debug("======="+resultJson);
		
		String subStr = ",~/";
		if(resultJson.indexOf(subStr) >0) {
			int indexOf = resultJson.indexOf(subStr);
			String fileName = resultJson.substring(0,indexOf);
			String filePath = resultJson.substring(indexOf+3,resultJson.length());
			StringBuilder sb = new StringBuilder();
			sb.append(filePath);
			sb.append(fileName);
			log.debug(sb.toString());
		}
		
	}
	
	
	
	
	@Test
	public void testBaiduTongji_GetList(){
		String urlStr = "https://api.baidu.com/json/tongji/v1/ReportService/getSiteList";
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("username", "uucall0123");// 用户名
		headerMap.put("password", "qw0123");// 用户密码
		headerMap.put("token", "3d49f9dcfcea9e74f6109466a0be0c89");// 申请到的token
		headerMap.put("account_type", "1");

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("header", headerMap);
		
        String postParamString = null;
        postParamString = JsonUtils.toDefaultJson(paramsMap);
        String resultJson = null;
//		String resultJson = HttpsUtilByBaiduAPI.doPostSSL(urlStr, postParamString);

/*

{
    "header":{
        "desc":"success",
        "failures":[

        ],
        "oprs":1,
        "succ":1,
        "oprtime":0,
        "quota":1,
        "rquota":1999,
        "status":0
    },
    "body":{
        "data":[
            {
                "list":[
                    {
                        "status":0,
                        "create_time":"2018-05-22 10:48:52",
                        "domain":"nfxts.520shq.com/localQuickPurchase",
                        "site_id":12057860,
                        "sub_dir_list":[

                        ]
                    }
                ]
            }
        ]
    }
}         

*/

        System.out.println(resultJson);
        System.out.println("=======Map====");
        
		
	}
	
	
	@Test
	public void testBaiduTongji_GetData() {
//		Map<String, String> bodMapy = new HashMap<String, String>();
//		bodMapy.put("siteId", "12057860");
//		bodMapy.put("start_date", "20180522");
//		bodMapy.put("end_date", "20180523");
//		bodMapy.put("method", "overview/getTimeTrendRpt");// 基础数据
//		bodMapy.put("metrics", "pv_count,visitor_count,ip_count,avg_visit_time");// 指标,数据单位
		
		//受访页面
		Map<String, String> bodMapy = new HashMap<String, String>();
		bodMapy.put("siteId", "12057860");
		bodMapy.put("start_date", "20180522");
		bodMapy.put("end_date", "20180525");
		bodMapy.put("method", "visit/toppage/a");// 受访页面
		bodMapy.put("metrics", "pv_count,visitor_count,ip_count,avg_visit_time");// 指标,数据单位

		String urlStr = "https://api.baidu.com/json/tongji/v1/ReportService/getData";

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("username", "uucall0123");// 用户名
		headerMap.put("password", "qw0123");// 用户密码
		headerMap.put("token", "3d49f9dcfcea9e74f6109466a0be0c89");// 申请到的token
		headerMap.put("account_type", "1");

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("header", headerMap);
		paramsMap.put("body", bodMapy);
		
        String postParamString = null;
        postParamString = JsonUtils.toDefaultJson(paramsMap);
		String resultJson = null;//HttpsUtilByBaiduAPI.doPostSSL(urlStr, postParamString);

        System.out.println(resultJson);
        System.out.println("=======testBaiduTongji_GetData====");
        
		
	}
	
	 public static void main(String[] args) {
		 System.out.println("=======main====");

			String resultJson = "201805241412151466.jpeg,~/UploadFile/YY/20180524/";
			HttpHeaders headers = new HttpHeaders();
	        List<String> cookies = new ArrayList<>();
	        
	        headers.set("cookie", "UM_distinctid=163867df03aa2d-0677f9767b457f-4446062d-1fa400-163867df03bcd3; cna=+3dlE/YD/WUCAXFB5x9MjQrh; uc_session_id=f23e6f47-c069-4f02-97d9-7457423da349; umplus_uc_token=17ci0LB8KCoPRgcE-SZ9JGg_f5eefde5d91d49efb0343e633444ce82; umplus_uc_loginid=871827928%40qq.com; l=AqeniGZAbCUFCcCfjj1o5fu1t9BxRnsO; um_lang=zh; cn_1263430556_dplus=1%5B%7B%22server_rt%22%3A43%2C%22server_httpcode%22%3A200%2C%22traceId%22%3A%22%22%2C%22server_code%22%3A200%2C%22server_status%22%3Atrue%2C%22server_msg%22%3A%22%E6%88%90%E5%8A%9F%22%2C%22userName%22%3A%22871827928%40qq.com%22%2C%22pid%22%3A%22b515f51b2eb7cb7303a7%22%7D%2Cnull%2Cnull%2Cnull%2Cnull%2Cnull%2C%22163867df03aa2d-0677f9767b457f-4446062d-1fa400-163867df03bcd3%22%2C%221526964557%22%2C%22https%3A%2F%2Fudplus.umeng.com%2F%3Fspm%3Da211g2.181323.0.0.5c633604VBmXU7%22%2C%22udplus.umeng.com%22%5D; cn_1262440121_dplus=1%5B%7B%22userid%22%3A%22871827928%40qq.com%22%2C%22dplus_project_version%22%3A%22%E4%B8%93%E4%B8%9A%E7%89%88%22%2C%22dplus_project_name%22%3A%22Web_DEMO%22%2C%22dplus_project_token%22%3A%22b515f51b2eb7cb7303a7%22%7D%2C0%2C1526971566%2C0%2C1526971566%2Cnull%2C%22163867df03aa2d-0677f9767b457f-4446062d-1fa400-163867df03bcd3%22%2C%221526964557%22%2C%22http%3A%2F%2Fwww.umeng.com%2F%22%2C%22www.umeng.com%22%5D; cn_1259864772_dplus=1%5B%7B%22%E6%98%AF%E5%90%A6%E7%99%BB%E5%BD%95%22%3Atrue%2C%22UserID%22%3A%22871827928%40qq.com%22%7D%2C0%2C1527045272%2C0%2C1527045272%2Cnull%2C%22163867df03aa2d-0677f9767b457f-4446062d-1fa400-163867df03bcd3%22%2C%221526967631%22%2C%22http%3A%2F%2Fmobile.umeng.com%2Fapps%22%2C%22mobile.umeng.com%22%5D; frame=; frontvar=siteShowHis%3Dopen%26siteListSortId%3D0%26cmenu%3D0%26districtnetInfo%3Dcity; isg=BN7eYmb7CQxXVl3uDtBLK9eoL3TgN6Igm86xhIhnAyEcq3-F8C1vKCVKp7enk5ox; CNZZDATA1255696692=969836292-1526968800-https%253A%252F%252Fwww.umeng.com%252F%7C1527062760; cn_1258498910_dplus=%7B%22distinct_id%22%3A%20%22163867df03aa2d-0677f9767b457f-4446062d-1fa400-163867df03bcd3%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201527067527%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201527067527%7D%2C%22initial_view_time%22%3A%20%221527062760%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.umeng.com%2F%22%2C%22initial_referrer_domain%22%3A%20%22www.umeng.com%22%7D; CNZZDATA30086426=cnzz_eid%3D745156949-1526967133-https%253A%252F%252Fweb.umeng.com%252F%26ntime%3D1527065997; _cnzz_CV30069868=%E6%98%AF%E5%90%A6%E7%99%BB%E5%BD%95%7C%E6%AD%A3%E5%B8%B8%E7%99%BB%E5%BD%95%7C1527096327564; CNZZDATA33222=cnzz_eid%3D288583960-1526965660-https%253A%252F%252Fweb.umeng.com%252F%26ntime%3D1527067774; CNZZDATA30069868=cnzz_eid%3D105676094-1526967375-https%253A%252F%252Fweb.umeng.com%252F%26ntime%3D1527063833; CNZZDATA30001831=cnzz_eid%3D1094533019-1526969953-https%253A%252F%252Fweb.umeng.com%252F%26ntime%3D1527068013; cn_ea1523f470091651998a_dplus=%7B%22distinct_id%22%3A%20%22163867df03aa2d-0677f9767b457f-4446062d-1fa400-163867df03bcd3%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201527073072%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201527073072%7D%2C%22initial_view_time%22%3A%20%221526968800%22%2C%22initial_referrer%22%3A%20%22https%3A%2F%2Fwww.umeng.com%2F%22%2C%22initial_referrer_domain%22%3A%20%22www.umeng.com%22%7D; edtoken=cnzz_5b0765167ed0f; PHPSESSID=vhag12gbpukqgnk0hoeng81sm7");
			String url = "https://web.umeng.com/main.php?c=site&a=overview&ajax=module%3Dsummary&siteid=1273759192";
			Map<String, String> map = new HashMap<String, String>();
			String json = HttpClientUtil.doPostHeader(headers,url,JsonUtils.toDefaultJson(map));
			 System.out.println(json);
			 System.out.println("=======main===end=");
			 
			 
			 String aa = null;
			Assert.isTrue(!StringUtils.isEmpty(aa),"aaaa");
			 System.out.println("----");
			 
	}
	
}