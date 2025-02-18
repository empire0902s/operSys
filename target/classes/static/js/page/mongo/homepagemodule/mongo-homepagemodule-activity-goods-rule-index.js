layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	$('#fromSys').val('homeOfLove');
	form.render('select');
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
	      {field:'type', title: '规则类型', width: 100,templet:function(val,row){
                  if(val.type===0){
                      return "自由选择商品";
                  }else if (val.type===1){
                  	return "一级类目";
				  } else if (val.type===2) {
                      return "二级类目";
                  }else if (val.type===3){
                      return "三级类目";
                  } else if(val.type===4){
                      return "四级类目";
				  }else if(val.type===5){
                  	 return "供应商"
				  }
              }},
	      {field:'ruleDes', title: '规则说明'},
	      {field:'isDisabled', title: '是否禁用', templet:'#isDisabledTpl',width: 100},
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
        			common.post( rootPath + '/coupons/activity/goods/rule/disabled', {id:obj.value}, function(msg) {
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
      			common.post( rootPath + '/coupons/activity/goods/rule/enableBy', {id:obj.value}, function(msg) {
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
			area: ['415px', '415px'],
			content: editForm,
			success: function(){
				editForm.loadData({id:''});
                editForm.find("select").not("").removeAttr("disabled");
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
				area: ['415px', '415px'],
				content: editForm,
				success: function(){
					// 加载表单，重新渲染
					editForm.loadData(data);
					$("select option[selected='false']").remove();
					editForm.find("select").not("").attr("disabled","disabled");
                    form.render();
				}
			});
			break;
            case 'detailsetting':
            	if (data.type===5) {
                    layer.open({
                        title: data.name+'的规则详情列表',
                        type: 2,
                        area: ['90%', '90%'],
                        shadeClose: true,
                        content: rootPath + "/coupons/activity/goods/rule/supdetails?ruleId="+data.id+"&type="+data.type,
                        success:function (layero,index) {

                        }
                    });
				}else if (data.type===1||data.type===2||data.type===3||data.type===4){
                    layer.open({
                        title: data.name+'的规则详情列表',
                        type: 2,
                        area: ['90%', '90%'],
                        shadeClose: true,
                        content: rootPath + "/coupons/activity/goods/rule/catedetails?ruleId="+data.id+"&type="+data.type,
                        success:function (layero,index) {

                        }
                    });
				} else {
                    layer.open({
                        title: data.name+'的规则详情列表',
                        type: 2,
                        area: ['90%', '90%'],
                        shadeClose: true,
                        content: rootPath + "/coupons/activity/goods/rule/details?ruleId="+data.id+"&type="+data.type,
                        success:function (layero,index) {

                        }
                    });
				}
                break;

                // window.location.href= rootPath + "/coupons/activity/goods/rule/rellist?ruleId="+data.id+"&ruleType="+data.type;
                // break;
            case 'pricesetting':
                layer.open({
                    title: '规则与商品关系列表',
                    type: 2,
                    area: ['80%', '90%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/goods/rule/proprice",
                    success:function (layero,index) {

                    }

                });
                break;

            case 'pricesetting':
                layer.open({
                    title: '规则与商品关系列表',
                    type: 2,
                    area: ['80%', '90%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/goods/rule/proprice",
                    success:function (layero,index) {

                    }

                });
                break;

			case 'bingcoupons':
                layer.open({
                    title: data.name+'绑定的优惠券列表',
                    type: 2,
                    area: ['90%', '90%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/goods/rule/coupons?categoryRuleId="+data.id,
                    success:function (layero,index) {

                    }
                });
			break;
		}
   });
});