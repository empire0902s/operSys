layui.use(['table', 'laytpl', 'form', 'common','laydate'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	$('.btn-close').click(function(){
		//当你在iframe页面关闭自身时
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭  
	});
	
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post(rootPath + '/order/evaluate/replySave', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功。",function(){
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭  
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		return false;
	});
});