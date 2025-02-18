layui.use(['table', 'laytpl', 'form', 'common','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common;
	$("select[url]").loadSelect();
	var imageUrlJson=JSON.parse(imageUrl);
	var bFlag=0;
	//图片上传
	  var uploadInst = upload.render({
	    elem: '#file'
	    ,url: rootPath+'/mongo/brandsquarebanner/upload/'
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
		if(data.field.brandLogoImg==""){
			layer.alert('请上传图片', {icon: 2}); 
			return false;
		}
		if(bFlag==1){
			layer.alert('请等待图片上传完成', {icon: 2}); 
			return false;
		}
		common.wait("提交中...");
		common.post(rootPath + '/mongo/brandrecommend/editSave', data.field, function(msg) {
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
	    url: common.getUrl('/mongo/brandrecommend/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:100, sort: true},
	      {field:'brandName', title: '品牌名称', width: 250},
	      {field:'sortNum', title: '排序', width: 120},
	      {field:'brandLogoImg', title: '图片', width: 120,templet:function(val){
	    	  return "<img style='width: 50px; height: 30px;'" +
	    	  				"src='"+val.brandLogoImg+"'>";
	      }},
	      {field:'jumpTarget', title: '跳转链接', width: 250},
	      {fixed: 'right',title: '操作',width: 350, align:'center', toolbar:'#editBar'}
	    ]],
    });
    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		$("select[url]").loadSelect();
		layer.open({
			title:'添加',
			type: 2,
			area: ['600px','750px'],
			shadeClose: true,
			content:rootPath + '/mongo/brandrecommend/addbrandrecommendwindow'
		});
		editForm.find('form')[0].reset();
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
				area: ['420px','580px'],
				content: editForm,
				success: function(){
					$("#imgDisplay").attr('src',data.brandLogoImg);
					$("#imgDisplay").show();
					$("#checkBigImg").attr("href",data.brandLogoImg);
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
				area: ['600px','350px'],
				shadeClose: true,
				content:rootPath + '/mongo/brandrecommend/brandrecommendDetails?id='+data.id
			});
			break;
		case 'jump':
			window.open(data.jumpTarget);
		break;
		case 'delete':
	   		layer.confirm("确定是否删除["+data.brandName+"]?",  {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
    		    	form.render();
  			  }  
  			}, function(confirmIndex){  
      			common.post( rootPath + '/mongo/brandrecommend/delete', {id:data.id}, function(msg) {
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