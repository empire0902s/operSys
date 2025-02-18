layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
    // 初始化编辑表单
	form.render();
	//自定义验证
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post( rootPath + '/auth/role/save', data.field, function(msg) {
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
	    url: common.getUrl('/auth/role/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'name', title: '角色名', width: 120},
	      {field:'code', title: '编码', width: 120},
	      {field:'remark', title: '备注', width: 120},
	      {field:'type', title: '分类', width: 80},
	      {field:'isDisabled', title:'是否禁用', width:120, templet: '#isDisabledTpl', unresize: true},
	      {fixed: 'right',title: '操作', width:200, align:'center', toolbar:'#editBar'}
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
        			common.post( rootPath + '/auth/role/disabled', {id:obj.value}, function(msg) {
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
      			common.post( rootPath + '/auth/role/enable', {id:obj.value}, function(msg) {
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
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form');
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
			var aIndex = layer.open({
				type: 2, 
				title: '角色授权菜单',
				content: rootPath + '/auth/role/grant?id='+data.id,
				shade: 0.3,
				offset: ['100px', '30%'],
				area: ['450px', '485px'],
				success: function(layero, index) {//当前层DOM当前,层索引
					var body = layer.getChildFrame('body', index);
				   
				},
				end: function() {
					addBoxIndex = -1;
				}
			});
			break;
		case 'look':
			layer.open({
				title:'详细',
				type: 2,
				area: ['55%','42%'],
				shadeClose: true,
				content:  rootPath + '/auth/role/roleDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
});