layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post( rootPath + '/coupons/activity/goods/rule/save', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功。",function(){
					layer.closeAll();
					common.reloadTable(dataTable);
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		return false;
	});
	
	
	
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/coupons/activity/goods/rule/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'name', title: '规则名称', width: 180},
	      {field:'type', title: '规则类型', width: 100},
	      {field:'ruleDes', title: '规则说明'},
	      {field:'isDisabled', title: '是否禁用', templet:'#disabledTpl',width: 100},
	      {field:'createTime', title: '创建时间', width: 160},
	      {field:'updateTime', title: '更新时间', width: 160},
	      {fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
	    ]],
    });

    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['400px', '400px'],
			content: editForm,
			success: function(){
				editForm.loadData({id:''});	
			}
		});
	});
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area: ['400px', '400px'],
				content: editForm,
				success: function(){
					// 加载表单，重新渲染
					editForm.loadData(data);
					form.render();
				}
			});
			break;
		case 'disabled':
			layer.confirm("确定是否禁用["+data.name+"]?", function(confirmIndex){
				common.wait("提交中...");
				common.post( rootPath + '/coupons/activity/goods/rule/disabled', {id:data.id}, function(msg) {
					common.hide();
					if (msg.ok) {
						// 提交成功刷新数据表格，关闭弹出层
						layer.alert("操作成功。",function(){
							layer.closeAll();
							common.reloadTable(dataTable);
						});
					}else{
						layer.alert(msg.msg);
					}
				});
				layer.close(confirmIndex);
			});
			break;
		case 'details':
			window.location.href= rootPath + "/coupons/activity/goods/rule/setting?ruleId"+data.id;
			break;
		case 'setting':
			window.location.href= rootPath + "/coupons/activity/goods/rule/setting?ruleId"+data.id;
			break;
		default:
			break;
		}
   });
});