layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
    form.render();
    
    

    var bannerDetails=$("#bannerDetails").attr("href");
    if(bannerDetails==""){
    	$("#bannerDetails").hide();
    }
   
});