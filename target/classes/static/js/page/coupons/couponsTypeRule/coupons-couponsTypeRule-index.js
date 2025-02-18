layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	$("select[url]").loadSelect();
	
//	form.render('select');
	form.on('select(selectType)', function (data) {	
		form.render();
		var tmpStr="";
		 var typeValue=data.value;
		 //满减
		 if(typeValue==1){
			 $("#minSpendMoneyDiv").find("input").removeAttr('lay-verify').val("");
			 $("#minSpendMoneyDiv").hide();
			 $("#MANJIAN").find("input").attr('lay-verify','number|space|pInteger').removeAttr("disabled").val("");
			 $("#MANJIAN").show();
			 $("#LIJIAN").hide();
			 $("#LIJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
			 $("#ZHEKOU").hide();
			 $("#ZHEKOU").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
		 }
		 //立减
		 if(typeValue==2){
			 $("#LIJIAN").show();
			 $("#LIJIAN").find("input").attr('lay-verify','number|space|pInteger').removeAttr('disabled').val("");
			 $("#minSpendMoneyDiv").hide();
			 $("#minSpendMoneyDiv").val("0");
			 $("#ZHEKOU").hide();
			 $("#ZHEKOU").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
			 $("#MANJIAN").hide();
			 $("#MANJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
		 }
		 //折扣
		 if(typeValue==3){
			 $("#ZHEKOU").show();
			 $("#ZHEKOU").find("input").attr('lay-verify','number|space|pInteger').removeAttr('disabled').val("");
			 $("#minSpendMoneyDiv").show();
			 $("#minSpendMoneyDiv").find("input").attr('lay-verify','number|space|pInteger').val("0");
			 $("#LIJIAN").hide();
			 $("#LIJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
			 $("#MANJIAN").hide();
			 $("#MANJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
		 }	 
    });
	
	$("#monor_name").click(function(){
		 $("#dev_dev").show();
		 $(document).one("click",function() { //对document绑定一个影藏Div方法
             $("#dev_dev").hide();                     
         });
         event.stopPropagation();//阻止事件向上冒泡
		 layui.use('layedit', function(){
		     var layedit = layui.layedit;
		     layedit.build('demo'); //建立编辑器
		 });
	})
	$("#dev_dev").click(function(event) {
        event.stopPropagation(); //阻止事件向上冒泡
    });
	
	form.on('submit(submit)', function(data) {
	 if($("#minSpendMoney").val()!=""){
		 	data.field.minSpendMoney= $("#minSpendMoney").val();
		 }
		var minSpendMoneyVal=$("#minSpendMoney").val();
		var amtFullReduceVal=$("#MANJIAN").find("input[name='amtFullReduce']").val();
		var amtSubVal=$("#LIJIAN").find("input[name='amtSub']").val();
		var amtDiscountVal=$("#ZHEKOU").find("input[name='amtDiscount']").val();
		var realMinSpendMoneyVal=$("#minSpendMoneyDiv").find("input[name='minSpendMoney']").val();

		if(""!=amtDiscountVal){
			if(amtDiscountVal>100){
				layer.alert('折扣必须在100以内', {icon: 2}); 
				return false;
			}
		}

		common.wait("提交中...");
		common.post( rootPath + '/coupons/couponsTypeRule/save', data.field, function(msg) {
	
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
	    url: common.getUrl('/coupons/couponsTypeRule/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:70, sort: true},
	      {field:'name', title: '金额名称', width: 200},
	      {field:'type', title: '优惠类型',width:100,templet:function(val,row){
	    	  if(val.type==1){
	    		  return "满减"
	    	  }
	    	  if(val.type==2){
	    		  return "立减";
	    	  }
	    	  if(val.type==3){
	    		  return "折扣";
	    	  }	    	  
	      }},
	      {field:'minSpendMoney', title: '最低消费金额',width:155},
	      {field:'typeDesc', title: '金额优惠',width:200},     
	      {field:'isDisabled', title: '是否禁用' ,width: 133, templet:'#isDisabledTpl'},
	      {fixed: 'right',title: '操作',align:'center', toolbar:'#editBar'}
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
        			common.post( rootPath + '/coupons/couponsTypeRule/disabled', {id:obj.value}, function(msg) {
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
      			common.post( rootPath + '/coupons/couponsTypeRule/enableBy', {id:obj.value}, function(msg) {
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
		 $("#MANJIAN").show();
		 $("#LIJIAN").hide();
		 $("#ZHEKOU").hide();	
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['370px','400px'],
			content: editForm,
			success: function(){
				//每一次打开添加按钮，将对应的输入框设置为可编辑
				 $("#MANJIAN").find("input").attr('lay-verify','number|space|pInteger').removeAttr("disabled");
				 $("#LIJIAN").find("input").removeAttr("lay-verify");
				 $("#ZHEKOU").find("input").removeAttr('lay-verify');
				 $("#minSpendMoneyDiv").find("input").removeAttr('lay-verify').val("");
				 $("#minSpendMoneyDiv").hide();
				editForm.loadData({id:''});	
			}
		});
	});
	
	$('.btn-close').click(function(){
		 layer.closeAll();
	});	
	$(".model_model_a .model_model_c .model_model_s i").click(function() {
		$(".model_model_a").hide();
	})	
	$(".model_model_a .model_model_c .model_model_s button").click(function() {
		$(".model_model_a").hide();
	})
	 	

	var editForm = $('.edit-form'), roleForm = $('.role-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area: ['370px','400px'],
				content: editForm,
				success: function(){
					// 加载表单，重新渲染		 						 					
					editForm.loadData(data);
					var typeValue = data.type ;
					 //满减
					 if(typeValue==1){
						 $("#minSpendMoneyDiv").find("input").removeAttr('lay-verify').val("");
						 $("#minSpendMoneyDiv").hide();
						 $("#MANJIAN").find("input").attr('lay-verify','number|space|pInteger').removeAttr("disabled");
						 $("#MANJIAN").show();
						 $("#LIJIAN").hide();
						 $("#LIJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
						 $("#ZHEKOU").hide();
						 $("#ZHEKOU").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
					 }
					 //立减
					 if(typeValue==2){
						 $("#LIJIAN").show();
						 $("#LIJIAN").find("input").attr('lay-verify','number|space|pInteger').removeAttr('disabled');
						 $("#minSpendMoneyDiv").hide();
						 $("#minSpendMoneyDiv").val("0");
						 $("#ZHEKOU").hide();
						 $("#ZHEKOU").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
						 $("#MANJIAN").hide();
						 $("#MANJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
					 }
					 //折扣
					 if(typeValue==3){
						 $("#ZHEKOU").show();
						 $("#ZHEKOU").find("input").attr('lay-verify','number|space|pInteger').removeAttr('disabled');
						 $("#minSpendMoneyDiv").show();
						 $("#minSpendMoneyDiv").find("input").attr('lay-verify','number|space|pInteger');
						 $("#LIJIAN").hide();
						 $("#LIJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
						 $("#MANJIAN").hide();
						 $("#MANJIAN").find("input").attr("disabled",true).removeAttr('lay-verify').val("");
					 }	 			
					form.render();
				}
			});
			break;
		case 'delete':
			layer.confirm("确定是否删除["+data.name+"]?", function(confirmIndex){
				common.wait("提交中...");
				common.post( rootPath + '/coupons/couponsTypeRule/deleteOn', {id:data.id}, function(msg) {
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
				content:  rootPath + '/coupons/couponsTypeRule/coupons?id='+data.id
			});
			break;

         case 'bingcoupons':
                layer.open({
                    title: data.name+'绑定的优惠券列表',
                    type: 2,
                    area: ['90%', '90%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/couponsTypeRule/couponsindex?couponsRuleId="+data.id,
                    success:function (layero,index) {

                    }
                });
                break;
		default:
			break;		
		}
   });
});





