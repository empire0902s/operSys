layui.use(['table', 'laytpl', 'form', 'common','laydate','element','treetable'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl,element = layui.element,treetable = layui.treetable,
	table = layui.table, common = layui.common;
	  //监听折叠
	  element.on('collapse(test)', function(data){
	    layer.msg('展开状态：'+ data.show);
	  });
    // 初始化编辑表单
	form.render();
	laydate.render({
		elem : '#evaluateTime',
		type : 'datetime'
	});
	table.render({
		  elem: '#orderTable'//指定原始表格元素选择器（推荐id选择器）
		  ,url: common.getUrl(rootPath + '/auth/admin/list')
		  ,height: 'full-number'  //容器高度
		  ,where:  $(".data-list-form").serializeJSON()
		  ,cols: [[
		      {field:'name', title: '名称', width:156.5,event: 'click'},
		    ]]
		});
	//table行监听事件
	 table.on('tool(orderTableFilter)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'click'){
		    	$("#name").val(data.name);
		    document.getElementById("reload").click();
		    }
	});
    // 添加事件
	$('.btn-add').click(function(){
		layer.open({
			title:'添加',
			type: 2,
			area: ['35%','85%'],
			shadeClose: true,
			content:  rootPath + '/order/evaluate/add'
		});
	});
	
	form.on('select(auditStats)',function (data) {
		if (data.value==1) {
            $("#auditReplyDiv").show();
		}else{
			 $("#auditReplyDiv").hide();
			 $("#auditReply").val('');
		}
	}
	)
       var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/auth/admin/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'name', title: '管理员用户名', width: 100},
	      {field:'realName', title: '真实姓名', width: 100},
	      {field:'phone', title: '手机号码', width: 150},
	      {field:'isDisabled', title: '是否禁用', templet:'#isDisabledTpl',width: 120},
	      {fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
	    ]],
	    page: {
	    	limit: 10,
	    	limits: [10,15]
	    }
    });
	var orderEvaluateIdsArray;
	table.on('checkbox(data-list)', function(obj){
		var checkStatus = table.checkStatus('orderEvaluateList')
	      ,data = checkStatus.data;
		orderEvaluateIdsArray=JSON.stringify(data);//将数据转为json字符串
		orderEvaluateIdsArray=JSON.parse(orderEvaluateIdsArray);//返回的是一个json数组
		});
	var orderEvaluateIds="";
    
	$('.btn-check').click(function(){
		if(orderEvaluateIdsArray==null||orderEvaluateIdsArray.length==0){
			layer.alert("请选择要通过审核的评论");
			return;
		}
		orderEvaluateIds="";
		if(orderEvaluateIdsArray.length>0){
			for(var i=0;i<orderEvaluateIdsArray.length;i++){
				orderEvaluateIds+=orderEvaluateIdsArray[i].id+",";
			}
		}
		//拼接字符串，以逗号隔开
		orderEvaluateIds=orderEvaluateIds.substring(0,orderEvaluateIds.length-1);
		var editForm = $('.edit-form');
		editForm.find('form')[0].reset();
		//弹出审核框
		form.render();
		layer.open({
			title:'审核',
			type: 1,
			area: ['400px','300px'],
			content: editForm,
			success: function(){
				editForm.loadData({id:''});
			}
		});
	form.on('submit(submit)', function(data) {
		layer.confirm("是否确定审核通过这"+(orderEvaluateIdsArray.length)+"条评论数据?", function(confirmIndex){
			common.post(rootPath + '/order/evaluate/examine', 
			{'auditDesc':data.field.auditDesc,orderEvaluateIds:orderEvaluateIds},
			 function(msg) {
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
		});
		return false;
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
		case 'details':
			layer.open({
				title:'详细信息',
				type: 2,
				area: ['82%','90%'],
				shadeClose: true,
				content: rootPath + '/order/evaluate/details?id='+data.id
			});
			break;
		case 'reply':
			layer.open({
				title:'后台人员回复评价',
				type: 2,
				area: ['33%','82%'],
				shadeClose: true,
				content: rootPath + '/order/evaluate/reply?id='+data.id
			});
			break;
		case 'disabled':
			layer.confirm("确定禁用该订单评价?", function(confirmIndex){
				common.wait("提交中...");
				common.post(rootPath + '/order/evaluate/disabled', {id:data.id}, function(msg) {
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
		case 'enableBy':
			layer.confirm("确定重新启用该订单评价?", function(confirmIndex){
				common.wait("提交中...");
				common.post(rootPath + '/order/evaluate/enableBy', {id:data.id}, function(msg) {
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
		default:
			break;
		}
   });
});