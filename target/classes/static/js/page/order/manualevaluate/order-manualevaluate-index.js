layui.use(['table', 'laytpl', 'form', 'common','laydate','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl,upload = layui.upload,$ = layui.jquery,
	table = layui.table, common = layui.common;
	 
	form.render();
	
	var bFlag=0;
	var bFlag2=0;
	 var uploadInst2 = upload.render({
		    elem: '#file2'
		    ,url: rootPath+'/auth/banner/upload/'
		    ,size:"1024"//限制上传图片大小，单位是kb
		    ,before: function(obj){
		      //预读本地文件示例，不支持ie8
		      obj.preview(function(index, file, result){
		    	  $('#imgDisplay2').show();
		        $('#imgDisplay2').attr('src', result); //图片链接（base64）
		      });
		      bFlag=1;
		    }
		    ,done: function(res){
		    	 //上传成功
		    	if(res.code ==0){
		    		layer.msg('上传成功');
			    	var imgPathVal=document.getElementById('evaluatieImg').value;
			    	document.getElementById('evaluatieImg').value=res.data.src;
			    	console.info("imgPathVal:"+document.getElementById('evaluatieImg').value)
			    	$("#checkBigImg2").attr("href",document.getElementById('evaluatieImg').value);
			    	bFlag=0;
		    	}
		      //如果上传失败
		      if(res.code > 0){
		    	  $('#imgDisplay2').hide();
		    	  $("#evaluatieImg").val("");
		        return layer.msg('上传失败');
		      }
		     
		    }
		  });
	
	  var uploadInst = upload.render({
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
			    	var imgPathVal=document.getElementById('userIcon').value;
			    	document.getElementById('userIcon').value=res.data.src;
			    	console.info("imgPathVal:"+document.getElementById('userIcon').value)
			    	$("#checkBigImg").attr("href",document.getElementById('userIcon').value);
			    	bFlag=0;
		    	}
		      //如果上传失败
		      if(res.code > 0){
		    	  $('#imgDisplay').hide();
		    	  $("#userIcon").val("");
		        return layer.msg('上传失败');
		      }
		     
		    }
		  });
    // 初始化编辑表单
	form.render();
	 var end=getDay(1);//当天日期  
	laydate.render({
		elem : '#evaluateTime',
		type : 'datetime'
	    ,max:end
	});
	   function diy_time(time1,time2){
		    time1 = Date.parse(new Date(time1));
		    time2 = Date.parse(new Date(time2));
		    return time3 =Math.abs(parseInt((time2 - time1)/1000/3600/24));
		}
	 function getDay(day){    
	        var today = new Date();    
	            
	        var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;            
	        today.setTime(targetday_milliseconds); //注意，这行是关键代码  
	        var tYear = today.getFullYear();    
	        var tMonth = today.getMonth();    
	        var tDate = today.getDate();    
	        tMonth = doHandleMonth(tMonth + 1);    
	        tDate = doHandleMonth(tDate);    
	        return tYear+"-"+tMonth+"-"+tDate;    
	 }    
	 function doHandleMonth(month){    
	        var m = month;    
	        if(month.toString().length == 1){    
	           m = "0" + month;    
	        }    
	        return m;    
	 }  
	var editForm = $('.edit-form')
    // 添加事件
	$('.btn-add').click(function(){
		editForm.find('form')[0].reset();
		layer.open({
			title:'添加',
			type: 1,
			area: ['850px','650px'],
			content: editForm,
			success: function(){
				editForm.loadData({id:''});	
				$("#imgDisplay").attr('src',"");
				$("#imgDisplay").hide();
				$("#checkBigImg").attr("href","");
				$("#imgDisplay2").attr('src',"");
				$("#imgDisplay2").hide();
				$("#checkBigImg2").attr("href","");
				$("#userIcon").val("");
				$("#evaluatieImg").val("");
				
				
			}
		});
	});
    /*var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/order/manualevaluate/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    id:'orderEvaluateList',
	    cols: [[
	      {field:'id', title: 'ID', width:100, sort: true},
	      {field:'goodsCode', title: '商品货号code', width: 120},
	     // {field:'goodsSku', title: '商品sku', width: 120},
	      {field:'evaluateTime', title: '评价时间', width: 180},
	      {field:'nickName', title: '评价用户昵称', width: 150},
	      {field:'evaluateContent', title: '评价内容', width: 180},
	      {field:'evaluateLev', title: '评价等级', width: 120},
	      {field:'isAnonymous', title: '是否匿名', width: 120,templet:function(val){
	    	  if(val.isAnonymous==0){
	    		  return "公开";
	    	  }else{
	    		  return "匿名";
	    	  }
	      }},
	      {field:'auditStats', title: '审核状态', width: 120,templet:function(val){
	    	  if(val.auditStats==0){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>待审核</a>";
	    	  }else if(val.auditStats==1){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5cb85c '>审核通过</a>";
	    	  }else if(val.auditStats==2){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#d9534f '>禁用</a>";
	    	  }else if(val.auditStats==3){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#d9534f '>审核不通过</a>";
	    	  }
	      }},
	      {field:'auditAdmin', title: '审核人', width: 120},
	      {field:'auditTime', title: '审核时间', width: 120},
	      {fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
	    ]],
    });*/
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
			if(bFlag2==1){
				layer.alert('请等待图片上传完成', {icon: 2}); 
				return false;
			}
			common.wait("提交中...");
			common.post(rootPath + '/order/manualevaluate/manualEvaluateSave', data.field, function(msg) {
				common.hide();
				if (msg.ok) {
					// 提交成功刷新数据表格，关闭弹出层
					layer.alert("操作成功。",function(){
						history.go(0);
					});
				}else{
					layer.alert(msg.msg);
				}
			});
			return false;
		});
    
		//根据商品code搜索商品
		$('.search').click(function(){
			var productCode=$("#goodsCode").val();
			if(productCode==null||productCode==""){
				return false;
			}
			common.post(rootPath + '/order/manualevaluate/findByCode', {"productCode":productCode}, function(msg) {
				common.hide();
				if (msg.ok) {
					var dataTmp=msg.value;
					$("#goodsName").val(dataTmp.productName);
				}else{
					layer.alert(msg.msg,{icon:2});
				}
			});
		});
		
  	
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'details':
			layer.open({
				title:'详细信息',
				type: 2,
				area: ['66.5%','90%'],
				shadeClose: true,
				content: rootPath + '/order/evaluate/details?id='+data.id
			});
			break;
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area: ['550px','650px'],
				content: editForm,
				success: function(){
					$("#imgDisplay").attr('src',data.userIcon);
					$("#imgDisplay").show();
					$("#checkBigImg").attr("href",data.userIcon);
					if(data.evaluatieImg!=""&&data.evaluatieImg!=null){
						$("#imgDisplay2").attr('src',data.evaluatieImg);
						$("#imgDisplay2").show();
						$("#checkBigImg2").attr("href",data.evaluatieImg);
					}
					// 加载表单，重新渲染
					editForm.loadData(data);
					common.post(rootPath + '/order/manualevaluate/findByCode', {"productCode":data.goodsCode}, function(msg) {
						common.hide();
						if (msg.ok) {
							var dataTmp=msg.value;
							$("#goodsName").val(dataTmp.productName);
						}else{
							$("#goodsName").val("商品不存在");
						}
					});
					form.render();
				}
			});
			break;
		case 'delete':
	   		layer.confirm("真的要删除该商品评论吗?",  {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
    		    	form.render();
  			  }  
  			}, function(confirmIndex){  
      			common.post( rootPath +'/order/manualevaluate/delete', {id:data.id}, function(msg) {
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
    
    
    /*
    上传参数设定
    */
    var upurl = "/upload/upimg";//上传图片地址
    var duotu = true;//是否为多图上传true false
    
    
    upload.render({
		elem: '#upload_img',
		url: rootPath+'/auth/banner/upload/',
		multiple: duotu,
		before: function(obj) {
			layer.msg('图片上传中...', {
				icon: 16,
				shade: 0.01,
				time: 0
			})
		},
		done: function(res) {
			layer.close(layer.msg());//关闭上传提示窗口
			if (duotu == true) {//调用多图上传方法,其中res.imgid为后台返回的一个随机数字
			$('#upload_img_list').append('<dd class="item_img" id="' + res.imgid + '"><div class="operate"><i onclick=UPLOAD_IMG_DEL("' + res.imgid + '") class="close layui-icon"></i></div><img style="width:80px;height:80px;" src="' + res.data.src + '" class="img" ><input type="hidden" name="evaluatieImgs[]" value="' + res.data.src + '" /></dd>');
			}else{//调用单图上传方法,其中res.imgid为后台返回的一个随机数字
				$('#upload_img_list').html('<dd class="item_img" id="' + res.imgid + '"><div class="operate"><i onclick=UPLOAD_IMG_DEL("' + res.imgid + '") class="close layui-icon"></i></div><img src="' + res.tolink + '" class="img" ><input type="hidden" name="evaluatieImg" value="' + res.data.src + '" /></dd>');
			}
		}
	})
});


/*
删除上传图片
*/
function UPLOAD_IMG_DEL(divs) {
	$("#"+divs).remove();
}

/*
多图上传变换左右位置
*/
$(".toleft").on("click", function() {
	var item = $(this).parent().parent(".item_img");
	var item_left = item.prev(".item_img");
	if ($("#upload_img_list").children(".item_img").length >= 2) {
		if (item_left.length == 0) {
			item.insertAfter($("#upload_img_list").children(".item_img:last"))
		} else {
			item.insertBefore(item_left)
		}
	}
});
$(".toright").on("click", function() {
	var item = $(this).parent().parent(".item_img");
	var item_right = item.next(".item_img");
	if ($("#upload_img_list").children(".item_img").length >= 2) {
		if (item_right.length == 0) {
			item.insertBefore($("#upload_img_list").children(".item_img:first"))
		} else {
			item.insertAfter(item_right)
		}
	}
	
});

