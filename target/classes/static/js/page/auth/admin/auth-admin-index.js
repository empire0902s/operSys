layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post(rootPath + '/auth/admin/save', data.field, function(msg) {
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
	form.on('submit(submitGrantData)', function(data) {
		common.wait("提交中...");
		common.post(rootPath + '/auth/admin/bindRoleToAdmin', data.field, function(msg) {
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
    });
    form.on('switch(isDisabledFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(flag){
    		layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=false;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/auth/admin/disabled', {id:obj.value}, function(msg) {
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
    			}, function(){  //取消
    				 obj.elem.checked=false;
    		    	form.render();
    			});  
    	}else{
    		layer.confirm("确定是否启用["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=true;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/auth/admin/enableBy', {id:obj.value}, function(msg) {
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
  			}, function(){  //取消
  				 obj.elem.checked=true;
  		    	form.render();
  			}); 
    	}
      });
	
    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['400px'],
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
				area: ['400px'],
				content: editForm,
				success: function(){
					// 加载表单，重新渲染
					editForm.loadData(data);
					form.render();
				}
			});
			break;
		case 'grant':
			layer.open({
				title:'用户授权',
				type: 2,
				area: ['635px','380px'],
				shadeClose: true,
				content: rootPath + '/auth/admin/checkRoles?id='+data.id
			});
			break;
		case 'details':
			layer.open({
				title:'详细信息',
				type: 2,
				area: ['55%','37%'],
				shadeClose: true,
				content: rootPath + '/auth/admin/adminDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
});