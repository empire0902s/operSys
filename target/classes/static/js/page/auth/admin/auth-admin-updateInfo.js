layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post(rootPath + '/auth/admin/updateInfoBySms', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功,请重新登录。",function(){
					layer.closeAll();
					if(parent.layer){
						parent.layer.closeAll()
					}
					window.location.href = rootPath + '/';
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		return false;
	});
});