layui.use(['table', 'laytpl', 'form', 'common','carousel'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common,
	carousel = layui.carousel;
	  form.render();
	var homePageModuleResultJson=JSON.parse(homePageModuleResult);
	if(homePageModuleResultJson.actityType=='0'){
		$("#actityType").text("普通商品");
	}else if(homePageModuleResultJson.actityType=='1'){
		$("#actityType").text("秒杀活动");
	}else if(homePageModuleResultJson.actityType=='2'){
		$("#actityType").text("砍价活动");
	}else if(homePageModuleResultJson.actityType=='3'){
		$("#actityType").text("预售活动");
	}
})