layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    form.render();

    var roleDetailsJson=JSON.parse(categoryDetails);
    if(roleDetailsJson.isDisabled){
        $("#isDisabled").text("是")
    }else {
        $("#isDisabled").text("否")
    }

    
    
});