 $(function () { 
	 layui.use(['table', 'laytpl', 'form', 'common','upload'], function() {
			var form = layui.form, laytpl = layui.laytpl, 
			laytpl = layui.laytpl, upload = layui.upload,
			table = layui.table, common = layui.common;
			
			
	//回显
			var homePageModuleAttachedJson =JSON.parse(homePageModuleAttached);
			if(homePageModuleAttachedJson!=null){
				if(homePageModuleAttachedJson.id!=null){
					$("input[name=id]").val(homePageModuleAttachedJson.id);
				}
				if(homePageModuleAttachedJson.logoURL!=null&&homePageModuleAttachedJson.logoURL!=''){
					$('#imgDisplay').attr("src",homePageModuleAttachedJson.logoURL);
			    	$("#checkBigImg").attr("href",homePageModuleAttachedJson.logoURL);
			    	$("#logoURL").val(homePageModuleAttachedJson.logoURL);
			    	$('#imgDisplay').show();
			    	$('#cleanLogoImg').show();
				}
				if(homePageModuleAttachedJson.logoBannerURL!=null&&homePageModuleAttachedJson.logoBannerURL!=''){
					$('#logoBannerImgDisplay').attr("src",homePageModuleAttachedJson.logoBannerURL);
			    	$("#bannerCheckBigImg").attr("href",homePageModuleAttachedJson.logoBannerURL);
			    	$("#logoBannerURL").val(homePageModuleAttachedJson.logoBannerURL);
			    	$('#logoBannerImgDisplay').show();
			    	$('#cleanBannerImg').show();
				}
				if(homePageModuleAttachedJson.activeBannerURL!=null&&homePageModuleAttachedJson.activeBannerURL!=''){
					$('#activeBannerImgDisplay').attr("src",homePageModuleAttachedJson.activeBannerURL);
			    	$("#activityCheckBigImg").attr("href",homePageModuleAttachedJson.activeBannerURL);
			    	$("#activeBannerURL").val(homePageModuleAttachedJson.activeBannerURL);
			    	$('#activeBannerImgDisplay').show();
			    	$('#cleanActivityImg').show();
				}
				if(homePageModuleAttachedJson.logoURLTarget!=null&&homePageModuleAttachedJson.logoURLTarget!=''){
					$('#logoURLTarget').val(homePageModuleAttachedJson.logoURLTarget);
					$('#logoURLTarget').text(homePageModuleAttachedJson.logoURLTarget);
				}
				if(homePageModuleAttachedJson.logoBannerURLTarget!=null&&homePageModuleAttachedJson.logoBannerURLTarget!=''){
					$('#logoBannerURLTarget').val(homePageModuleAttachedJson.logoBannerURLTarget);
					$('#logoBannerURLTarget').text(homePageModuleAttachedJson.logoBannerURLTarget);
				}
				if(homePageModuleAttachedJson.activeBannerURLTarget!=null&&homePageModuleAttachedJson.activeBannerURLTarget!=''){
					$('#activeBannerURLTarget').val(homePageModuleAttachedJson.activeBannerURLTarget);
					$('#activeBannerURLTarget').text(homePageModuleAttachedJson.activeBannerURLTarget);
				}
			}
		//清空图片
			$('#cleanLogoImg').click(function(){
				$('#imgDisplay').attr("src","");
		    	$("#checkBigImg").attr("href","");
		    	$("#logoURL").val("");
		    	$('#imgDisplay').hide();
		    	$('#cleanLogoImg').hide();
			})
			$('#cleanBannerImg').click(function(){
				$('#logoBannerImgDisplay').attr("src","");
		    	$("#bannerCheckBigImg").attr("href","");
		    	$("#logoBannerURL").val("");
		    	$('#logoBannerImgDisplay').hide();
		    	$('#cleanBannerImg').hide();
			})
			$('#cleanActivityImg').click(function(){
				$('#activeBannerImgDisplay').attr("src","");
		    	$("#activityCheckBigImg").attr("href","");
		    	$("#activeBannerURL").val("");
		    	$('#activeBannerImgDisplay').hide();
		    	$('#cleanActivityImg').hide();
			})
			
			
			
			
			
			
			
			
			
			
			
			
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
					    	var imgPathVal=document.getElementById('logoURL').value;
					    	document.getElementById('logoURL').value=res.data.src;
					    	$("#checkBigImg").attr("href",document.getElementById('logoURL').value);
					    	bFlag=0;
					    	$('#cleanLogoImg').show();
				    	}
				      //如果上传失败
				      if(res.code > 0){
				    	  $('#imgDisplay').hide();
				    	  $("#logoURL").val("");
				        return layer.msg('上传失败');
				      }
					     
					    }
					  });
				  var uploadInst = upload.render({
					    elem: '#logoBannerFile'
					    ,url: rootPath+"/auth/bannerAuxiliary/upload"
					    ,size:"1024"//限制上传图片大小，单位是kb
					    ,before: function(obj){
					      //预读本地文件示例，不支持ie8
					      obj.preview(function(index, file, result){
					    	  $("#logoBannerImgDisplay").show();
					        $("#logoBannerImgDisplay").attr('src', result); //图片链接（base64）
					      });
					      bFlag2=1;
					    }
				  ,done: function(res){
				    	 //上传成功
				    	if(res.code ==0){
				    		layer.msg('上传成功');
					    	var imgPathVal=document.getElementById('logoBannerURL').value;
					    	document.getElementById('logoBannerURL').value=res.data.src;
					    	console.info("imgPathVal:"+document.getElementById('logoBannerURL').value)
					    	$("#bannerCheckBigImg").attr("href",document.getElementById('logoBannerURL').value);
					    	bFlag2=0;
					    	$('#cleanBannerImg').show();
				    	}
				      //如果上传失败
				      if(res.code > 0){
				    	  $('#logoBannerImgDisplay').hide();
				    	  $("#logoBannerURL").val("");
				        return layer.msg('上传失败');
				      }
					    }
					  });
				  var uploadInst = upload.render({
					    elem: '#activeBannerFile'
					    ,url: rootPath+"/auth/bannerAuxiliary/upload"
					    ,size:"1024"//限制上传图片大小，单位是kb
					    ,before: function(obj){
					      //预读本地文件示例，不支持ie8
					      obj.preview(function(index, file, result){
					    	  $("#activeBannerImgDisplay").show();
					        $("#activeBannerImgDisplay").attr('src', result); //图片链接（base64）
					      });
					      bFlag3=1;
					    }
				  ,done: function(res){
				    	 //上传成功
				    	if(res.code ==0){
				    		layer.msg('上传成功');
					    	var imgPathVal=document.getElementById('activeBannerURL').value;
					    	document.getElementById('activeBannerURL').value=res.data.src;
					    	console.info("imgPathVal:"+document.getElementById('activeBannerURL').value)
					    	$("#activityCheckBigImg").attr("href",document.getElementById('activeBannerURL').value);
					    	bFlag3=0;
					    	$('#cleanActivityImg').show();
				    	}
				      //如果上传失败
				      if(res.code > 0){
				    	  $('#activeBannerImgDisplay').hide();
				    	  $("#activeBannerURL").val("");
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
				common.post(rootPath + '/mongo/homePageModuleAttached/save', data.field, function(msg) {
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
