layui.use([ 'table', 'laytpl', 'form', 'common', 'laydate','layedit','upload' ],function() {
		var form = layui.form, laytpl = layui.laytpl, table = layui.table,
			common = layui.common, laydate = layui.laydate,
			layedit=layui.layedit,upload = layui.upload;
		
		var bFlag=0;
		//内容图片上传
		  var uploadInst = upload.render({
		    elem: '#file'
		    ,url: rootPath+'/coupons/coupons/upload'
		    ,size:"1024"//限制上传图片大小，单位是kb
		    ,before: function(obj){
		      //预读本地文件示例，不支持ie8
		      obj.preview(function(index, file, result){
		    	  $('#contentImgDisplay').show();
		        $('#contentImgDisplay').attr('src', result); //图片链接（base64）
		      });
		      bFlag=1;
		    }
		    ,done: function(res){
		    	 //上传成功
		    	if(res.code ==0){
		    		layer.msg('上传成功');
			    	var imgPathVal=document.getElementById('contentImg').value;
			    	document.getElementById('contentImg').value=res.data.src;
			    	$("#checkBigImg").attr("href",document.getElementById('contentImg').value);
			    	bFlag=0;
		    	}
		      //如果上传失败
		      if(res.code > 0){
		    	  $('#contentImgDisplay').hide();
		    	  $("#contentImg").val("");
		        return layer.msg('上传失败');
		      }
		     
		    }
		  });
			var bFlag2=0;
			//禁用图片上传
			  var uploadInst2 = upload.render({
			    elem: '#file2'
			    ,url: rootPath+'/coupons/coupons/upload'
			    ,size:"1024"//限制上传图片大小，单位是kb
			    ,before: function(obj){
			      //预读本地文件示例，不支持ie8
			      obj.preview(function(index, file, result){
			    	  $('#contentDisabledImgDisplay').show();
			        $('#contentDisabledImgDisplay').attr('src', result); //图片链接（base64）
			      });
			      bFlag2=1;
			    }
			    ,done: function(res){
			    	 //上传成功
			    	if(res.code ==0){
			    		layer.msg('上传成功');
				    	var imgPathVal=document.getElementById('contentDisabledImg').value;
				    	document.getElementById('contentDisabledImg').value=res.data.src;
				    	$("#checkBigImg2").attr("href",document.getElementById('contentDisabledImg').value);
				    	bFlag2=0;
			    	}
			      //如果上传失败
			      if(res.code > 0){
			    	  $('#contentDisabledImgDisplay').hide();
			    	  $("#contentDisabledImg").val("");
			        return layer.msg('上传失败');
			      }
			    }
			  });
			
		
				//指定时间
				 laydate.render({
				 	elem : 'input[name="sendTimeStart"]',
				 	type : 'datetime'
				 });
				var et= laydate.render({
				 	elem : '#sendTimeEnd',
				 	type : 'datetime',
                     done:function(value,date){
                         var strTime=value.substr(11,8);
                         if ("00:00:00"===strTime){
                             et.config.dateTime={
                                 year:date.year,
                                 month:date.month-1,
                                 date: date.date,
                                 hours: 23,
                                 minutes: 59,
                                 seconds : 59
                             };
                         }
                     }
				 });
				laydate.render({
					elem : '#timeBegin',
					type : 'datetime'
				});
				var et1=laydate.render({
					elem : '#timeEnd',
					type : 'datetime',
                    done:function(value,date){
                        var strTime=value.substr(11,8);
                        if ("00:00:00"===strTime){
                            et1.config.dateTime={
                                year:date.year,
                                month:date.month-1,
                                date: date.date,
                                hours: 23,
                                minutes: 59,
                                seconds : 59
                            };
                        }
                    }
				});
				
				form.render();
				
		        
				$("select[url]").loadSelect();
					var editTypeJson=JSON.parse(editType);
					var couponsDataJson=JSON.parse(couponsData);
					var vali_day_numJson=JSON.parse(vali_day_num);
					var vali_day_startJson=JSON.parse(vali_day_start);
					var vali_day_endJson=JSON.parse(vali_day_end);
					var tmp=JSON.stringify(couponsDataJson);
					var tmpJson=$.parseJSON(tmp);
					if (couponDes!=='null') {
						couponDes=couponDes.substring(1,couponDes.length-1);
						tmpJson.couponDes=couponDes;
					}
					if(editTypeJson=="2"){
						console.info(tmpJson)
						var addForm = $('.addForm');
						var valiDayTypeVal=tmpJson.valiDayType;
						if (valiDayTypeVal == 0) {
								$("#TIME").find('input').attr('lay-verify','required').removeAttr("disabled").val("");
								$("#TIME").find('input[name=timeBegin]').val(vali_day_startJson.replace('T',' '));
								$("#TIME").find('input[name=timeEnd]').val(vali_day_endJson.replace('T',' '));
								$("#TIME").show()// 显示;
								$("#DAYS").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
								$("#DAYS").hide()// 隐藏;
						} else {
							$("#TIME").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
							$("#TIME").hide()// 隐藏;
							$("#DAYS").find('input').attr('lay-verify','number||space').removeAttr("disabled").val("");
							$("#DAYS").find('input').val(vali_day_numJson);
							$("#DAYS").show();// 显示;
						}
						if(couponsDataJson.finanStatus == "1"){ //通过财务审核..不可编辑
							//移除多余选项
							$("select option[selected='false']").remove();
							addForm.find("input,select")
							.not("input[name='receiveNumRule'],input[name='sendNum'],input[name='couponsHrefUrl'],#file,#file2").
							attr("disabled","disabled")
							$("#file").css("background-color","#d2d2d2").hide();
							$("#file2").css("background-color","#d2d2d2").hide();
						}
						if(tmpJson.contentImg){
							$("#contentImgDisplay").attr('src',tmpJson.contentImg);
							$("#contentImgDisplay").show();
							$("#checkBigImg").attr("href",tmpJson.contentImg);
							
						}
						if(tmpJson.contentDisabledImg){
						$("#contentDisabledImgDisplay").attr('src',tmpJson.contentDisabledImg);
						$("#contentDisabledImgDisplay").show();
						$("#checkBigImg2").attr("href",tmpJson.contentDisabledImg);
						}
						addForm.loadData(tmpJson);
						form.render();
					}
					
					ueditor.getEditor('couponDes',{
			            toolbars:[[  'undo', 'redo', '|', 'bold', 'italic', 'underline', 'fontborder',
			                'strikethrough', 'superscript', 'subscript', '|', 'cleardoc', '|']],
			            autoClearinitialContent:false,
			            wordCount:false,
			            elementPathEnabled:false,
			            initialFrameHeight:120,
			            initialFrameWidth:550
			        })


				// 自定义验证
				form.verify({
                    timeEnd:function(value,item){
                    	var $timeBegin=$(" input[ name='timeBegin' ] ").val();
                    	if (value<$timeBegin){
                    		return '结束时间不能小于开始时间';
						}
					},
                    sendTimeEnd:function (value,item) {
                        var $startTime=$(" input[ name='sendTimeStart' ] ").val();
                        if (value<=$startTime){
                            return '发放结束时间不能小于发放开始时间';
                        }
                    },
					username : function(value, item) { // value：表单的值、item：表单的DOM对象
						if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$")
								.test(value)) {
							return '用户名不能有特殊字符';
						}
						if (/(^\_)|(\__)|(\_+$)/.test(value)) {
							return '用户名首尾不能出现下划线\'_\'';
						}
						if (/^\d+\d+\d$/.test(value)) {
							return '用户名不能全为数字';
						}
					}
					// 数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
				});
				form.on('submit(submit)', function(data) {
					//如果图片未上传完成就点击保存，弹出提示
					if(bFlag==1||bFlag2==1){
						layer.alert('请等待图片上传完成', {icon: 2}); 
						return false;
					}
					common.wait("提交中...");
					common.post('/coupons/coupons/save', data.field, function(
							msg) {
						common.hide();
						if (msg.ok) {
							// 提交成功刷新数据表格，关闭弹出层
							layer.alert("操作成功。", function() {
								// 刷新父页面
								parent.location.reload();
								layer.closeAll();
							});
						} else {
							layer.alert(msg.msg);
						}
					});
					return false;
				});

				$('.btn-close').click(function() {
					layer.closeAll();
					//关闭当前内嵌页面窗口
					var index = parent.layer.getFrameIndex(window.name);  
					parent.layer.close(index);
				});

				form.on('select(activity)',function (data) {
					if (data.value==="") {
                        $("#sendTimeStart").val("");
                        $("#sendTimeEnd").val("");
					}
                    $.get(
                    	rootPath + "/coupons/activity/single",
                        {aid:data.value},
                        function(response) {
                        	$("#sendTimeStart").val(response.sendTimeStart).attr("disabled","disabled");
                            $("#sendTimeEnd").val(response.sendTimeEnd).attr("disabled","disabled");
                            $("input[name='batch']").val(response.batch);
                        }
                    )

                });
				var valiDayType = $("#valiDayType");
				form.on('select(valiDayType)', function(data) {
					// 优惠券选择满减时，显示满减的输入框
					if (valiDayType.val() == 0) {
						//指定时间
							$("#TIME").find('input').attr('lay-verify','required').removeAttr("disabled").val("");
							$("#TIME").show()// 显示;
							$("#DAYS").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
							$("#DAYS").hide()// 隐藏;
							
					} else {
						$("#TIME").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
						$("#TIME").hide()// 隐藏;
						$("#DAYS").find('input').attr('lay-verify','number||space').removeAttr("disabled").val("");
						$("#DAYS").show();// 显示;
					}
				});
});

