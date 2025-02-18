 $(function () { 
	 layui.use(['table', 'laytpl', 'form', 'common','upload'], function() {
			var form = layui.form, laytpl = layui.laytpl, 
			laytpl = layui.laytpl, upload = layui.upload,
			table = layui.table, common = layui.common;
			
			
	//回显
			var bannerAuxiliaryJson =JSON.parse(bannerAuxiliary);
			if(bannerAuxiliaryJson!=null){
				if(bannerAuxiliaryJson.id!=null){
					$("input[name=id]").val(bannerAuxiliaryJson.id);
				}
				if(bannerAuxiliaryJson.logoPath!=null){
					$('#imgDisplay').attr("src",bannerAuxiliaryJson.logoPath);
			    	$("#checkBigImg").attr("href",bannerAuxiliaryJson.logoPath);
			    	$("#logoPath").val(bannerAuxiliaryJson.logoPath);
			    	$('#imgDisplay').show();
				}
				if(bannerAuxiliaryJson.bannerImgPath!=null){
					$('#bannerImgDisplay').attr("src",bannerAuxiliaryJson.bannerImgPath);
			    	$("#bannerCheckBigImg").attr("href",bannerAuxiliaryJson.bannerImgPath);
			    	$("#bannerImgPath").val(bannerAuxiliaryJson.bannerImgPath);
			    	$('#bannerImgDisplay').show();
				}
				if(bannerAuxiliaryJson.activityImgPath!=null){
					$('#activityImgDisplay').attr("src",bannerAuxiliaryJson.activityImgPath);
			    	$("#activityCheckBigImg").attr("href",bannerAuxiliaryJson.activityImgPath);
			    	$("#activityImgPath").val(bannerAuxiliaryJson.activityImgPath);
			    	$('#activityImgDisplay').show();
				}
			}
			
			var bFlag=0;
			var bFlag2=0;
			var bFlag3=0;
			//图片上传
				  var uploadInst = upload.render({
					    elem: '#logoFile'
					    ,url: rootPath+"/auth/bannerAuxiliary/upload"
					    ,size:"1024"//限制上传图片大小，单位是kb
					    ,before: function(obj){
					      //预读本地文件示例，不支持ie8
					      obj.preview(function(index, file, result){
					    	  $("#imgDisplay").show();
					        $("#imgDisplay").attr('src', result); //图片链接（base64）
					      });
					      bFlag=1;
					    }
				  ,done: function(res){
				    	 //上传成功
				    	if(res.code ==0){
				    		layer.msg('上传成功');
					    	var imgPathVal=document.getElementById('logoPath').value;
					    	document.getElementById('logoPath').value=res.data.src;
					    	console.info("imgPathVal:"+document.getElementById('logoPath').value)
					    	$("#checkBigImg").attr("href",document.getElementById('logoPath').value);
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
				  var uploadInst = upload.render({
					    elem: '#bannerFile'
					    ,url: rootPath+"/auth/bannerAuxiliary/upload"
					    ,size:"1024"//限制上传图片大小，单位是kb
					    ,before: function(obj){
					      //预读本地文件示例，不支持ie8
					      obj.preview(function(index, file, result){
					    	  $("#bannerImgDisplay").show();
					        $("#bannerImgDisplay").attr('src', result); //图片链接（base64）
					      });
					      bFlag2=1;
					    }
				  ,done: function(res){
				    	 //上传成功
				    	if(res.code ==0){
				    		layer.msg('上传成功');
					    	var imgPathVal=document.getElementById('bannerImgPath').value;
					    	document.getElementById('bannerImgPath').value=res.data.src;
					    	console.info("imgPathVal:"+document.getElementById('bannerImgPath').value)
					    	$("#bannerCheckBigImg").attr("href",document.getElementById('bannerImgPath').value);
					    	bFlag2=0;
				    	}
				      //如果上传失败
				      if(res.code > 0){
				    	  $('#bannerImgDisplay').hide();
				    	  $("#bannerImgPath").val("");
				        return layer.msg('上传失败');
				      }
					    }
					  });
				  var uploadInst = upload.render({
					    elem: '#activityFile'
					    ,url: rootPath+"/auth/bannerAuxiliary/upload"
					    ,size:"1024"//限制上传图片大小，单位是kb
					    ,before: function(obj){
					      //预读本地文件示例，不支持ie8
					      obj.preview(function(index, file, result){
					    	  $("#activityImgDisplay").show();
					        $("#activityImgDisplay").attr('src', result); //图片链接（base64）
					      });
					      bFlag3=1;
					    }
				  ,done: function(res){
				    	 //上传成功
				    	if(res.code ==0){
				    		layer.msg('上传成功');
					    	var imgPathVal=document.getElementById('activityImgPath').value;
					    	document.getElementById('activityImgPath').value=res.data.src;
					    	console.info("imgPathVal:"+document.getElementById('activityImgPath').value)
					    	$("#activityCheckBigImg").attr("href",document.getElementById('activityImgPath').value);
					    	bFlag3=0;
				    	}
				      //如果上传失败
				      if(res.code > 0){
				    	  $('#activityImgDisplay').hide();
				    	  $("#activityImgPath").val("");
				        return layer.msg('上传失败');
				      }
					    }
					  });
			
		    // 初始化编辑表单
			form.render();
			$("select[url]").loadSelect();
			form.on('submit(submit)', function(data) {
				if(bFlag==1||bFlag2==1||bFlag3==1){
					layer.alert('请等待图片上传完成', {icon: 2}); 
					return false;
				}
				common.wait("提交中...");
				common.post(rootPath + '/auth/bannerAuxiliary/save', data.field, function(msg) {
					common.hide();
					if (msg.ok) {
						// 提交成功刷新数据表格，关闭弹出层
						layer.alert("操作成功。",function(){
							layer.closeAll();
							// 关闭当前内嵌页面窗口
							var index = parent.layer
									.getFrameIndex(window.name);
							parent.layer.close(index);
						});
					}else{
						layer.alert(msg.msg);
					}
				});
				return false;
			});


			$('.btn-close').click(function(){
				layer.closeAll();
				// 关闭当前内嵌页面窗口
				var index = parent.layer
						.getFrameIndex(window.name);
				parent.layer.close(index);
			});
			var editForm = $('.edit-form'), roleForm = $('.role-form');
		});
	 
 });
