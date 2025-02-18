layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	$("select[url]").loadSelect();
	
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post(rootPath + '/auth/commodity/save', data.field, function(msg) {
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
	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/auth/commodity/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:60, sort: true},
	      {field:'commodityName', title: '商品名称', width: 143},
	      {field:'commodityCode', title: '商品编码',width:143},
	      {field:'createTime', title: '创建时间', width: 143},
	      {field:'createAdmin', title: '创建人', width: 143},	      
	      {field:'updateTime', title: '更新时间',width: 143},
	      {field:'updateAdmin', title: '更新人',width: 143},
	      {field:'isDisabled', title: '是否禁用' ,width: 143, templet:'#disabledTpl'},
	      {fixed: 'right',title: '操作', width:150, align:'center', toolbar:'#editBar'}
	    ]],
    });

    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['350px'],
			content: editForm,
			success: function(){
				editForm.loadData({id:''});	
			}
		});
	});
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area: ['350px'],
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
				common.post(rootPath + '/auth/commodity/disabled', {id:data.id}, function(msg) {
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
		case 'look':
			layer.open({
				title:'详细',
				type: 2,
				area: ['55%','38.5%'],
				shadeClose: true,
				content: rootPath + '/auth/commodity/commodityDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
});