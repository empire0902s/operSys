layui.use(['table', 'laytpl', 'form', 'common','laydate','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, element = layui.element,
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();

	var editForm = $('.edit-form');
    $('.clearCache').click(function(){
    	layer.confirm("确定要清除首页缓存?",  {  
			  btn: ['确定','取消'] //按钮  
			  ,cancel: function(index, layero){  
				  obj.elem.checked=true;
		    	form.render();
			  }  
			}, function(confirmIndex){ 
		        $.ajax({
		            type: 'GET',
		            url:  rootPath + '/mongo/homepagemodule/clearCache',
		            dataType: "json",
		            success: function (msg) {
		                if (msg.ok){
		                    common.hide();
		                    layer.alert("清除成功。",function(){
		                        layer.closeAll();
		                        // common.reloadTable(dataTable);
		                    });
		                }else {
		                    common.hide();
		                    layer.alert(msg.msg,function(){
		                        layer.closeAll();
		                    });
		                }
		            },
		        });
		    	layer.close(confirmIndex);
			}, function(){  //取消
   			}); 
    });
    $('.inidIndexData').click(function(){
    	layer.confirm("是否获取原旧版数据?",  {  
			  btn: ['确定','取消'] //按钮  
			  ,cancel: function(index, layero){  
				  obj.elem.checked=true;
		    	form.render();
			  }  
			}, function(confirmIndex){ 
				$.ajax({
		    		type: 'GET',
		    		url:  rootPath + '/mongo/homepagemodule/initIndexPageData',
		    		dataType: "json",
		    		success: function (msg) {
		    			if (msg.ok){
		    				common.hide();
		    				layer.alert("初始化成功。",function(){
		    					layer.closeAll();
		    					common.reloadTable(dataTable);
		    				});
		    			}else {
		    				common.hide();
		    				layer.alert(msg.msg,function(){
		    					layer.closeAll();
		    				});
		    			}
		    		},
		    	});
				layer.close(confirmIndex);
			}, function(){  //取消
   			}); 
			
    });

    // 添加事件
		$('.btn-add').click(function(){
			editForm.find('form')[0].reset();
			layer.open({
				title:'添加',
				type: 1,
				area: ['980px','475px'],
				content: editForm,
				success: function(){
					editForm.loadData({id:''});	
					 $("#actityType").find("option[value =0]").attr("selected","selected");
					 $(".type_t").find("option[value =6]").attr("selected","selected");
					 var imgeUrl=rootPath+"/images/homepagemodulestyle/model6-banner_pictures.png";
					 $("#img").attr("src",imgeUrl);
				 	$("#modelText").text("样式6 数据内容为banner");
				 	form.render();
				}
			});
		});
		$('.btn-close').click(function(){
			layer.closeAll();
		});
		//保存
		form.on('submit(submit)', function(data) {
			common.wait("提交中...");
			common.post(rootPath + '/mongo/homepagemodule/save', data.field, function(msg) {
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

	    url: common.getUrl(rootPath + '/mongo/homepagemodule/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:250, sort: true},
	      {field:'moduleName', title: '模块名称', width: 150},
	      {field:'sortNum', title: '排序', width: 100},
	      {field:'actityType', title: '活动类型', width: 120,templet:function(val){
	    	  if(val.actityType=='0'){
	    		  return '普通商品';
	    	  }else  if(val.actityType=='1'){
	    		  return '秒杀活动';
	    	  }else  if(val.actityType=='3'){
	    		  return '预售活动';
	    	  }else  if(val.actityType=='4'){
	    		  return '品牌广场';
	    	  }else  if(val.actityType=='5'){
	    		  return '非秒杀商品广场';
	      }
	      }},
	      {field:'showState', title: '是否展示', width: 100,templet:'#isShowStateTpl'},
	      {field:'showModuleName', title: '显示模块名称', width: 130,templet:'#isShowModuleNameTpl'},
	      {field:'showloadMore', title: '是否显示加载更多', width: 150,templet:'#isShowloadMoreTpl'},
	      {fixed: 'right',title: '操作',width: 320, align:'center', toolbar:'#editBar'}
	    ]],
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
				area: ['55%','50%'],
				shadeClose: true,
				content: rootPath + '/mongo/homepagemodule/details?id='+data.id
			});
			break;
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area: ['980px','475px'],
				content: editForm,
				success: function(){
					$(".imgDiv").attr("style","display:inline");
					var imgUrl=getModelImgUrl(data.styleType);
			    	var modelText1=getModelText(data.styleType);
			    	$("#img").attr("src",imgUrl);
			    	$("#modelText").text(modelText1);
			    	// 加载表单，重新渲染
					editForm.loadData(data);
					form.render();
				}
			});
			break;
		case 'homePageModuleAttachedDetailsSetting':
			layer.open({
				title:'活动设置',
				type: 2,
				area: ['450px','480px'],
				shadeClose: true,
				content:rootPath + '/mongo/homepagemodule/homePageModuleAttachedDetailsSetting?id='+data.id+'&moduleName='+data.moduleName
			});
			break;
	/*	case 'jump':
			window.open(data.loadMoreTarget);
			break;*/
		default:
			break;
		}
   });
    
    //三个禁用启用按钮的操作
    form.on('switch(isShowModuleNameFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(!flag){
    		layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=true;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/mongo/homepagemodule/updateShowModuleName', {id:obj.value,showModuleName:0}, function(msg) {
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
    	}else{
    		layer.confirm("确定是否启用["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=false;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/mongo/homepagemodule/updateShowModuleName', {id:obj.value,showModuleName:1}, function(msg) {
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
    	}
      });
    
    //是否展示
    form.on('switch(isShowStateFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(!flag){
    		layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=true;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/mongo/homepagemodule/updateShowState', {id:obj.value,showState:0}, function(msg) {
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
    	}else{
    		layer.confirm("确定是否启用["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=false;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/mongo/homepagemodule/updateShowState', {id:obj.value,showState:1}, function(msg) {
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
    	}
      });
    
  //是否显示更多
    form.on('switch(isShowloadMoreFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(!flag){
    		layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=true;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/mongo/homepagemodule/updateShowloadMore', {id:obj.value,showloadMore:0}, function(msg) {
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
    	}else{
    		layer.confirm("确定是否启用["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=false;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/mongo/homepagemodule/updateShowloadMore', {id:obj.value,showloadMore:1}, function(msg) {
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
    	}
      });
    //下拉框的时间监听
    form.on('select(type_t)', function(data){
    	$(".imgDiv").attr("style","display:inline");
    	var imgUrl=getModelImgUrl(data.value);
    	var modelText1=getModelText(data.value);
    	$("#img").attr("src",imgUrl);
    	$("#modelText").text(modelText1);
    	});    
    function getModelImgUrl(data){
    	if(data==1){
    		return rootPath+"/images/homepagemodulestyle/model1-broadcast_banner.png";
    	}else if(data==2){
    		return rootPath+"/images/homepagemodulestyle/model2-coupon_banner.png";
    	}else if(data==3){
    		return rootPath+"/images/homepagemodulestyle/model3-advertising_pictures.png";
    	}else if(data==4){
    		return rootPath+"/images/homepagemodulestyle/model4-commodity_sliding_column.png";
    	}else if(data==5){
    		return rootPath+"/images/homepagemodulestyle/model5-logo_banner.png";
    	}else if(data==6){
    		return rootPath+"/images/homepagemodulestyle/model6-banner_pictures.png";
    	}else if(data==7){
    		return rootPath+"/images/homepagemodulestyle/model7-brand_square.png";
    	}else if(data==8){
    		return rootPath+"/images/homepagemodulestyle/model8-seckill.png";
    	}else if(data==9){
    		return rootPath+"/images/homepagemodulestyle/model9-general_merchandise_list.png";
    	}else if(data==10){
    		return rootPath+"/images/homepagemodulestyle/model10-similar_activities_column_in_brand_plaza.png";
    	}else if(data==11){
    		return rootPath+"/images/homepagemodulestyle/model11-activity_banner_collection.png";
    	}
    }
    function getModelText(data){
    	if(data==1){
    		return "样式1 (图片轮播区域) 数据内容为banner";
    	}else if(data==2){
    		return "样式2(优惠券) 数据内容为banner";
    	}else if(data==3){
    		return "样式3(广告图) 用于区分 优惠券,数据内容为banner";
    	}else if(data==4){
    		return "样式4 滑动栏  数据内容为商品";
    	}else if(data==5){
    		return "样式5  数据内容为banner";
    	}else if(data==6){
    		return "样式6 数据内容为banner";
    	}else if(data==7){
    		return "样式7  数据内容为banner + 商品";
    	}else if(data==8){
    		return "样式8   秒杀(只有banner)";
    	}else if(data==9){
    		return "样式9   商品列表  (一般会有10个)";
    	}else if(data==10){
    		return "样式10   品牌广场类似活动栏目  (单个)";
    	}else if(data==11){
    		return "样式11   活动banner集合 (6张banner)";
    	}
    }
    
    
});