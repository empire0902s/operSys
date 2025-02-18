layui.use([ 'table', 'laytpl', 'form', 'common', 'laydate','layedit','upload'],function() {
		var form = layui.form, laytpl = layui.laytpl, table = layui.table,
			common = layui.common, laydate = layui.laydate,
			layedit=layui.layedit,upload = layui.upload
			,rate=layui.rate;
		
		form.render();
		laydate.render({
			elem : '#evaluateTime',
			type : 'datetime'
		});
				form.on('submit(submit)', function(data) {
					common.wait("提交中...");
					common.post('/order/evaluate/save', data.field, function(
							msg) {
						common.hide();
						if (msg.ok) {
							// 提交成功刷新数据表格，关闭弹出层
							layer.alert("操作成功。", function() {
								// 刷新父页面
								parent.location.reload();
								layer.closeAll();
							});
						} else {
							layer.alert(msg.msg);
						}
					});
					return false;
				});

				$('.btn-close').click(function() {
					layer.closeAll();
					//关闭当前内嵌页面窗口
					var index = parent.layer.getFrameIndex(window.name);  
					parent.layer.close(index);
				});
});

