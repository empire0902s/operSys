layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	var lrd = '';
	
    // 初始化编辑表单
	form.render();
	$("select[url]").loadSelect();
	
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post( rootPath + '/coupons/couponsAreaRule/save', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				if(codeArray!=""){
					codeArray.splice(0,codeArray.length);//清空数组 
				}
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
	    url: common.getUrl('/coupons/couponsAreaRule/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'name', title: '区域规则名称', width: 200},
	      {field:'lev', title: '区域级别',width:100},
	      {field:'levRangeDetail', title: '区域明细', width: 280},
	      {field:'levType', title: '区域类型', width: 210,templet:function(val,row){
	    	  if(val.levType==1){
	    		  return "包含";
	    	  }else if(val.levType==0){
	    		  return "排除";
	    	  }
	      }},	      
	      {fixed: 'right',title: '操作', width:200, align:'center', toolbar:'#editBar'}
	    ]],
    });

    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		
		$("#openLoad").hide();
		$("#areaTable").html("");
		
		$("#areaLoad").hide();
		$("#areaLoadTab").html("");
		lrd = '';
		
		
		
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['500px'],
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
				area: ['500px'],
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
				common.post( rootPath + '/coupons/couponsAreaRule/disabled', {id:data.id}, function(msg) {
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
				content: rootPath + '/coupons/couponsAreaRule/couponsAreaRuleDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
    
    var codeArray=new Array();
    $("#open").click(function(){
    	$("#openLoad").show();
    	var sLev = $("#sLev").val();
    	var name = $("#name").val();
    	$("#pCode").val(sLev);
    	$.ajax({
    		url:  rootPath + "/coupons/couponsAreaRule/selectArea",
  	 		type:"post",
  	 		data:{"name":name,"sLev":sLev},
  	 		dataType:"json",
  	 		success:function(data){ 
  	 			if(data==""){
  	 				layer.alert("区域名称与级别不匹配");
  	 				return ;
  	 			}
  	 			var html = "<tr><td>区域范围明细</td><td>编码</td><td>操作</td></tr>";
  	 			for(var i=0;i<data.length;i++){  
  	 				html+="<tr>"+
  	 							"<td>" + data[i].code + " </td>" +
  	 							"<td>" + data[i].name + "</td>"  +
  	 							"<td><input type='button' value='确定' id='select' /></td>"
  	 					 +"</tr>";
  	 				codeArray.push(parseInt(data[i].code));
  	 			}
  	 			$("#areaTable").prepend(html);
  	 		}
    	 })
    });
    
    
    $(document).on("click","#select",function(){
    	console.info(codeArray)
    	var tr = $(this).closest("tr");
    	var code = tr.find("td:eq(0)").text();
    	var name = tr.find("td:eq(1)").text();
    	//console.info(code)
    	var index = $.inArray(parseInt(code),codeArray);
    	//console.log(index)
        if(index < 0){
        	layer.alert("不能重复添加该项");
    		return ;
        }
    	for (var i = 0; i < codeArray.length; i++) {
    		if(codeArray[i]==code){
    			delete codeArray[i];
    			break;
        	}
		}
    	//删除已经选中的code
    	
    	var trHTML = "<tr>"+
    					"<td>" + code + " </td>" +
    					"<td>" + name + "</td>"  +
    					"<td><input type='button' value='删除' id='deleteArea' /></td>"
    	             "</tr>"
    	$("#areaLoadTab").append(trHTML);//在table最后面添加一行
    	lrd +=code;
    	$("#lrd").val(lrd);
    	$("#areaLoad").show();
    });
    
    $(document).on("click","#deleteArea",function(){
    	var code = $(this).closest("tr").find("td:eq(0)").text();
    	//将删除的值重新放回codeArray
    	codeArray.push(parseInt(code));
    	var t1 = new Array();
    	
    	var a = $("#lrd").val();
    	t1 = a.split(" ");
    	
    	var city = "";
    	for(var i=0;i<t1.length-1;i++){
    		if(code.trim() != t1[i].trim()){
    			city+=t1[i]+ " ";
    		}
    	}
    	$("#lrd").val(city);
    	var link = $(this).parents("tr");  
        link.remove();  
    })
});