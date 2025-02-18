layui.use(['table', 'laytpl', 'form', 'common','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, element = layui.element,
	table = layui.table, common = layui.common;
	
	 $("select[url]").loadSelect();
    // 初始化编辑表单
	form.render();
	
	form.on('submit(submit)', function(data) {

		if(data.field.goodsCode==''){
			  layer.alert("请完善品牌商品信息",{icon: 2});
		      return false;
		}
	 
	 if(data.field.sortNum.indexOf("+")!=-1){
		  layer.alert("排序不能有特殊字符(加号)",{icon: 2});
	      return false;
	  }
	  if(data.field.sortNum==0){
    	  layer.alert("排序不能为0",{icon: 2});
	      return false;
	    }
		common.wait("提交中...");
		common.post( rootPath + '/mongo/brandsquaregoods/save', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功。",function(){
					layer.closeAll();
					document.getElementById('reload').click();
					//common.reloadTable(dataTable);
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		$("#columnId").val(data.field.columnId);
		$("#columnIdReal").val(data.field.columnId);
		return false;
	});
	
	// 初始化数据表格
    var dataTable = common.renderTable({
		height:'full-number',  //容器高度
	    url: common.getUrl("/mongo/brandsquaresetting/goodsList"),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'sortNum', title: '排序', width: 120},
	      {field:'goodsCode', title: '商品code', width: 300},
	      {field:'goodsName', title: '商品名称', width: 180},
	      {field:'fabulousNum', title: '关注数', width: 150},
	      {field:'showState', title: '上下架状态', width: 180,templet:'#isShowStateTpl'},
	      {fixed: 'right',title: '操作',width: 150, align:'center', toolbar:'#editBar'}
	    ]],
    });
    
    
    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['1000px','550px'],
			content: editForm,
			success: function(){
		    	$("#columnIdReal").val($("#columnId").val());
//				editForm.loadData({id:''});	
				$("#search").css("pointer-events","auto");
				$("#search").css("background-color","#1E9FFF");
			  	$(".product_Code").text("");
            	
            	$(".product_Name").text("");
            	$(".company_name").text("");
            	$(".distributionPrice").text("");
            	$(".goodsPrice").text("");
            	
            	$("#code").val("");
            	
             	$("#sortNum").val("100");
             	$("#id").val("");
             	$("#goodsCode").val("");
             	$("#fabulousNum").val("0");
             	$("#activateStatus").val("2");
             	$("#columnId").val("");
             	
				$("#saveToList").css("pointer-events","auto");
        		$("#saveToList").css("background-color","#1E9FFF");
             	form.render();
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
				area: ['1000px','550px'],
				content: editForm,
				success: function(){
					console.info("data--------"+JSON.stringify(data))
					//$("#columnId").val(data.columnId);
			        $.ajax({
			            type: 'GET',
			            url:  rootPath + '/mongo/brandsquaregoods/findByCode?productCode='+data.goodsCode,
			            dataType: "json",
			            success: function (msg) {
			            	if(!msg.ok){
			            		layer.alert(msg.msg,{icon: 2});
			            		$("#saveToList").css("background-color","#d2d2d2");
			            		$("#saveToList").css("pointer-events","none");
			            		editForm.loadData(data);
			            		$("#code").val(data.goodsCode);
			            		$(".product_Code").text("");
					            $(".product_Name").text("");
					            $(".company_name").text("");
					            $(".distributionPrice").text("");
					            $(".goodsPrice").text("");
			                  	form.render();
			                  	return false;
			            	}
			            	var dataTmp=msg.value;
			            	//手动绑定数据到页面
			            	$(".product_Code").text(dataTmp.goodsCode);
			            	$("#goodsCode").val(dataTmp.goodsCode);
			            	$("#code").val(dataTmp.goodsCode);
			            	$(".product_Name").text(dataTmp.goodsName);
			            	$(".company_name").text(dataTmp.companyName);
			            	$(".distributionPrice").text(dataTmp.distributionPrice);
			            	$(".goodsPrice").text(dataTmp.goodsPrice);
			             	$("#sortNum").val(data.sortNum);
			             	$("#id").val(data.id);
			            	$("#activateStatus").val("2");
			            	form.render();
			            },
			        });
					$("#search").css("background-color","#d2d2d2");
		      		$("#search").attr("disabled",true);
		      		$("#search").css("pointer-events","none");
		      		
					$("#saveToList").css("background-color","#1E9FFF");
		      		$("#saveToList").css("pointer-events","auto");
			        editForm.loadData(data);
					// 加载表单，重新渲染
					form.render();
				}
			});
			break;
		case 'delete':
	   		layer.confirm("确定是否删除["+data.goodsName+"]?",  {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
    		    	form.render();
  			  }  
  			}, function(confirmIndex){  
      			common.post( rootPath + '/mongo/brandsquaregoods/delete', {id:data.id}, function(msg) {
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
  		    	form.render();
  			}); 
			break;
		case 'look':
			layer.open({
				title:'详细',
				type: 2,
				area: ['65%','45%'],
				shadeClose: true,
				content:  rootPath + '/sys/dict/dictDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
    
    form.on('switch(isShowStateFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[3];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(!flag){
    		layer.confirm("确定是否下架["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=true;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/mongo/brandsquaregoods/updateShowState', {id:obj.value,activateStatus:1}, function(msg) {
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
    		layer.confirm("确定是否上架["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=false;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/mongo/brandsquaregoods/updateShowState', {id:obj.value,activateStatus:2}, function(msg) {
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
    $(".search").click(function(){
        var code=$("#code").val();
        if(code==""){
        	return;
        }
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/brandsquaregoods/findByCode?productCode='+code,
            dataType: "json",
            success: function (msg) {
            	if(!msg.ok){
            		layer.alert(msg.msg,{icon: 2});
            	}
            	console.info(msg)
            	var data=msg.value;
            	//手动绑定数据到页面
            	$(".product_Code").text(data.goodsCode);
            	$("#goodsCode").val(data.goodsCode);
            	$(".product_Name").text(data.goodsName);
            	$(".company_name").text(data.companyName);
            	$(".distributionPrice").text(data.distributionPrice);
            	$(".goodsPrice").text(data.goodsPrice);
            	//editForm.loadData(data);
            	$("#sortNum").val("100");
               	$("#fabulousNum").val("0");
            	$("#activateStatusTmp").val("2");
               	$("#id").val("");
    	    	//$("#columnIdReal").val($("#columnId").val());
           //    	$("#columnId").val("");
            	//form.render();
            },
        });
    });
    
});