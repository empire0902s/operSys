layui.use(['table', 'laytpl', 'form', 'common','upload','yutons_sug'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common,
	yutons_sug = layui.yutons_sug;
	
	var url=rootPath+"/orderingsystem/brandsquare/getListShopperUserNameByStoresName";
	sessionStorage.setItem("url", url);
	//
	//初始化姓名输入提示框
	yutons_sug.render({
		id: "storesName", //设置容器唯一id
		height: "267",
		width:"320",
		cols: [
			[
				{
				field: 'store_name',
				title: '品牌商名称',
				width:'65%'
			},
			{
				field: 'seq',
				title: '品牌商名称'
			}]
		], //设置表头
		type: 'sugTable', //设置输入框提示类型：sug-下拉框，sugTable-下拉表格
		url: sessionStorage.getItem("url") + "?strUserName="+$("#storesName").val() //设置异步数据接口,url为必填项,params为字段名
	});
	
	
	
	
	var bFlag=0;
    // 初始化编辑表单
	form.render();
	
	//二维码图片上传
	  var upload = upload.render({
		    elem: '#file'
		    ,url: rootPath+'/auth/banner/upload/'
		    ,size:"1024"//限制上传图片大小，单位是kb
		    ,before: function(obj){
		      //预读本地文件示例，不支持ie8
		      obj.preview(function(index, file, result){
		    	  $('#imgDisplay').show();
		        $('#imgDisplay').attr('src', result); //图片链接（base64）
		      });
		      bFlag=1;
		    }
		    ,done: function(res){
		    	 //上传成功
		    	if(res.code ==0){
		    		layer.msg('上传成功');
			    	var imgPathVal=document.getElementById('storesUrl').value;
			    	document.getElementById('storesUrl').value=res.data.src;
			    	console.info("imgPathVal:"+document.getElementById('storesUrl').value)
			    	$("#checkBigImg").attr("href",document.getElementById('storesUrl').value);
			    	bFlag=0;
		    	}
		      //如果上传失败
		      if(res.code > 0){
		    	  $('#imgDisplay').hide();
		    	  $("#storesUrl").val("");
		        return layer.msg('上传失败');
		      }
		     
		    }
		  });
	
	
	$("select[url]").loadSelect();
	form.on('submit(submit)', function(data) {
		
		if(data.field.storesUrl==null||data.field.storesUrl==''){
			layer.alert('请上传图片', {icon: 2}); 
			return false;
		}
		 if(data.field.sort.indexOf("+")!=-1){
			  layer.alert("排序不能有特殊字符(+)",{icon: 2});
		      return false;
		  }
		
		if(bFlag==1){
			layer.alert('请等待图片上传完成', {icon: 2}); 
			return false;
		}
		//判断contentUrl(实质存的是品牌商seq)值是否存在
		 if(data.field.contentUrl==null||data.field.contentUrl==''){
			  layer.alert("手打品牌商无效，请使用搜索功能搜索品牌商，然后选择您需要的品牌商",{icon: 2});
		      return false;
		  }

		common.wait("提交中...");
		common.post(rootPath + '/orderingsystem/brandsquare/save', data.field, function(msg) {
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
		numbercheck: function(value, item){ //value：表单的值、item：表单的DOM对象
				if(value>=100||value<=0){
					  return '抵扣百分比限制在0~100之间';
				}
			  }
	});
	
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/orderingsystem/brandsquare/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'storesName', title: '品牌商名称', width: 160},
	      {field:'sort', title: '排序', width: 150},
	      {field:'storesUrl', title: '品牌商图片', width: 180,templet:function(val){
	    	  return "<img style='width: 50px; height: 30px;'" +
				"src='"+val.storesUrl+"'>";
	      }},
/*	      {field:'isDisabled', title: '启用状态',width: 120, templet:'#isDisabledTpl'},*/
	      {fixed: 'right',title: '操作',width: 280, align:'center', toolbar:'#editBar'}
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
    			//common.wait("提交中...");
        			common.post( rootPath + '/publicenum/publicenum/disabled', {id:obj.value}, function(msg) {
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
      			common.post( rootPath + '/publicenum/publicenum/enableBy', {id:obj.value}, function(msg) {
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
			area: ['600px','400px'],
			content: editForm,
			success: function(){
				$("#contentUrl").val("");
				$('#imgDisplay').hide();
				$("#imgDisplay").attr('src','');
				$('#storesUrl').val("");
				editForm.loadData({id:''});	
			}
		});
	});
	
	
	//点击事件
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
	
	$('.btn-search').click(function(){
		//  $("#storesName").get(0).focus();
		document.getElementById("storesName").click();
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
				area: ['600px','400px'],
				content: editForm,
				success: function(){
					if($('#storesUrl').val()!=null&&$('#storesUrl').val("")!=''){
						$("#imgDisplay").attr('src',data.storesUrl);
						$("#imgDisplay").show();
						$("#checkBigImg").attr("href",data.storesUrl);
					}
					// 加载表单，重新渲染
					editForm.loadData(data);
					form.render();
				}
			});
			break;
		case 'look':
			layer.open({
				title:'详细',
				type: 2,
				area: ['550px','350px'],
				shadeClose: true,
				content:rootPath + '/orderingsystem/brandsquare/details?id='+data.id
			});
			break;
		case 'delete':
	   		layer.confirm("确定是否删除["+data.storesName+"]?",  {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
    		    	form.render();
  			  }  
  			}, function(confirmIndex){  
      			common.post( rootPath + '/orderingsystem/brandsquare/delete', {id:data.id}, function(msg) {
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
		default:
			break;
		}
   });
});