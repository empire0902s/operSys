layui.use(['table', 'laytpl', 'form', 'common','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common;
	
	var adminDtoJson=JSON.parse(adminDto);
	if(adminDtoJson!=null){
		if(adminDtoJson.name=='admin'){
			$("#dataInitialization").attr("style","background-color:#d9534f;");
		}else{
			$("#dataInitialization").attr("style","display: none;");
		}
	}
	
	var bFlag=0;
	//图片上传
	  var uploadInst = upload.render({
	    elem: '#file'
	    ,url: rootPath+'/auth/banner/app/adv/upload/'
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
		    	var imgPathVal=document.getElementById('imgPath').value;
		    	document.getElementById('imgPath').value=res.data.src;
		    	console.info("imgPathVal:"+document.getElementById('imgPath').value)
		    	$("#checkBigImg").attr("href",document.getElementById('imgPath').value);
		    	bFlag=0;
	    	}
	      //如果上传失败
	      if(res.code > 0){
	    		layer.msg('上传失败,稍后请重新上传');
	    	  $('#imgDisplay').hide();
	    	  $("#imgPath").val("");
	        return layer.msg('上传失败');
	      }
	     
	    }
	  });
    // 初始化编辑表单
	form.render();
	$("select[url]").loadSelect();
	form.on('submit(submit)', function(data) {
		//没有上传图片前
		if(data.field.imgPath==""){
			layer.alert('请上传图片', {icon: 2}); 
			return false;
		}
		if(bFlag==1){
			layer.alert('请等待图片上传完成', {icon: 2}); 
			return false;
		}
		if(data.field.id==null||data.field.id==""){
    		layer.confirm("该操作会新增一条app启动页广告数据(同时会将正在使用的app启动页广告设置为禁止状态！)", {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    			  return false;
    			  }  
    			}, function(confirmIndex){  
    				common.wait("提交中...");
    				common.post(rootPath + '/mongo/homepageappadv/save', data.field, function(msg) {
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
    			}
    			)
			
		}else{
			common.wait("提交中...");
			common.post(rootPath + '/mongo/homepageappadv/save', data.field, function(msg) {
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
		}

		return false;
	});
	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/mongo/homepageappadv/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'bannerName', title: '启动页广告名称', width: 190},
	      {field:'imgPath', title: '图片', width: 120,templet:function(val){
	    	  return "<img style='width: 50px; height: 30px;'" +
	    	  				"src='"+val.imgPath+"'>";
	      }},
	      //{field:'sort', title: '排序', width: 120},
	      {field:'isDisabled', title: '是否禁用',width: 120, templet:'#isDisabledTpl'},
	      {fixed: 'right',title: '操作',width: 200, align:'center', toolbar:'#editBar'}
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
        			common.post( rootPath + '/auth/banner/app/adv/disabled', {id:obj.value}, function(msg) {
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
    		layer.confirm("确定是否启用["+tmp.innerText+"]?,同时会将正在使用的app启动页广告设置为禁止状态!", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=true;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/auth/banner/app/adv/enableBy', {id:obj.value}, function(msg) {
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
			area: ['420px','350px'],
			content: editForm,
			success: function(){
				$('#imgDisplay').hide();
				$("#imgDisplay").attr('src','');
				editForm.loadData({id:''});	
				document.getElementById('imgPath').value="";
			}
		});
	});
	//数据初始化事件
	$('#dataInitialization').click(function(){
		layer.confirm("该操作会将已存在的数据清空,同时初始化app启动页数据.", function(confirmIndex){
			common.post(rootPath + '/auth/banner/app/adv/dataInitialization', 
					null,
					 function(msg) {
						common.hide();
						if (msg.ok) {
							// 提交成功刷新数据表格，关闭弹出层
							layer.alert("操作成功。",function(){
							window.location.reload();
							layer.closeAll();
							});
						}else{
							layer.alert(msg.msg);
						}
					});
		})
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
				area: ['420px','350px'],
				content: editForm,
				success: function(){
					$("#imgDisplay").attr('src',data.imgPath);
					$("#imgDisplay").show();
					$("#checkBigImg").attr("href",data.imgPath);
					console.info(data)
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
				area: ['660px','350px'],
				shadeClose: true,
				content:rootPath + '/mongo/homepageappadv/appAdvDetails?id='+data.id
			});
			break;
		case 'jump':
			window.open(data.url);
		break;
		default:
			break;
		}
   });
});