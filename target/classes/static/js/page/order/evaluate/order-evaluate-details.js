layui.use(['table', 'laytpl', 'form', 'common','carousel'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common,
	carousel = layui.carousel;
	  form.render();
	var tHtml='';
	function removeAllSpace(str) {
		return str.replace(/\s+/g, "");
		}
	var imgsResultJson=JSON.parse(imgsResult);
	if(imgsResultJson.length==0){
		$("#imgs").hide();
		$("#noImg").show();
	}else{
		$("#imgs").show();	
	}
	for(var i=0;i<imgsResultJson.length;i++)
	{
		var tmp=JSON.stringify(imgsResultJson[i].imgUrl);
		var tmp2=tmp.replace(/\//g, '\\/');
		var tmp3=tmp2.slice(1,tmp2.length-1);
		tmp3=removeAllSpace(tmp3);
		console.info(tmp3)
		tHtml+="<div><img src='"+tmp3+"' style='height:130px;width:280px;margin-left:0px;'></div>";
	}
	var imgsHTML=$("#imgsHtml");
	imgsHTML.html(tHtml);
    form.render();
    carousel.render({
        elem: '#imgs'
        ,width: '40%' //设置容器宽度
        ,height:'130px'
        ,autoplay:true
        ,arrow:'hover'
        ,anim: 'fade' //切换动画方式
      });
    
    //商品
    var productJson=JSON.parse(productFlag);
    //订单详情2
    var MongoDistributionOrdersListJson=JSON.parse(MongoDistributionOrdersList);
    //商品图片url
    var imgPathJson=JSON.parse(imgPath);
    //订单详情
    var distributionOrdersDtlResultJson=JSON.parse(distributionOrdersDtlResult);
	var orderEvaluateResultJson=JSON.parse(orderEvaluateResult);
	var shopperResultJson=JSON.parse(shopperResult);
	if(orderEvaluateResultJson.auditStats==0){
		$("#auditStatsTd").text('待审核');
	}else if(orderEvaluateResultJson.auditStats==1){
		$("#auditStatsTd").text('审核通过');
	}else if(orderEvaluateResultJson.auditStats==2){
		$("#auditStatsTd").text('禁用');
	}else if(orderEvaluateResultJson.auditStats==3){
		$("#auditStatsTd").text('审核不通过');
	}
    //商品不存在弹出提示
    if(productJson==false){
		$(".errDiv").show();
		$("#productDiv").show();
    	$("#showMsg").hide();
    	form.render();
    	return ;
	}
    //订单不存在弹出提示
    if(MongoDistributionOrdersListJson.length<=0||MongoDistributionOrdersListJson==null){
		$(".errDiv").show();
		$("#ordernoSpanDiv").show();
    	$("#showMsg").hide();
    	form.render();
    	return ;
	}
    //订单数据异常问题
    if(distributionOrdersDtlResultJson==''||distributionOrdersDtlResultJson==null||imgPathJson==''){
    	$(".errDiv").show();
    	$("#ordernoProductSpanDiv").show();
    	$("#showMsg").hide();
    	return ;
    }
    if(distributionOrdersDtlResultJson!=''){
    	if(distributionOrdersDtlResultJson.goodsName==''||distributionOrdersDtlResultJson.goodsCode==''
    		||distributionOrdersDtlResultJson.price==''){
    		$("#errDiv").show();
        	$("#showMsg").hide();
        	return ;
    	}else{
        	//显示商品信息
        	$("#showMsg").show();
        	$("#goodsCode").text(distributionOrdersDtlResultJson.goodsCode);
        	$("#goodsName").text(distributionOrdersDtlResultJson.goodsName);
        	$("#price").text(distributionOrdersDtlResultJson.price);
        	if(distributionOrdersDtlResultJson.orderSku!=null){
        	 	$("#goodsSku").text(distributionOrdersDtlResultJson.orderSku);
        	}else{
        	 	$("#goodsSku").text("");
        	}
        	$("#img").attr("src",imgPathJson);
        	$(".goodsDiv").show();	
        	 form.render();
    	}
    	console.info(shopperResultJson)
    	if(shopperResultJson!=''&&shopperResultJson!=null){
    		if(shopperResultJson.mobile!=null){
    			 $("#mobile").text(shopperResultJson.mobile);
    			 //如果状态为审核不通过，不可审核与回复
    			 if(orderEvaluateResultJson.auditStats==3){
    				 //不可再编辑审核备注，不能再提交审核
    				 $('.submit').hide();
    				 $("#auditDesc").attr("readonly",'readonly');
    				 $('.unExamine').hide();
    				 $('.submitReply').hide();
    				 $("#auditReply").attr("readonly",'readonly');
    			 }
    			 
    			 //判断是否已经审核通过或已被禁用
    			 if(orderEvaluateResultJson.auditStats==1||orderEvaluateResultJson.auditStats==2){
    				 //不可再编辑审核备注，不能再提交审核
    				 $('.submit').hide();
    				 $('.unExamine').hide();
    				 $("#auditDesc").attr("readonly",'readonly');
    			 }
    			 //判断是否已经审核并回复评价或已被禁用
    			 if(orderEvaluateResultJson.auditStats==1&&orderEvaluateResultJson.auditReply!=''
    				 &&orderEvaluateResultJson.auditReply!=null||orderEvaluateResultJson.auditStats==2){
    				 $('.submitReply').hide();
    				 $('.unExamine').hide();
    				 $("#auditReply").attr("readonly",'readonly');
    			 }
    			 $("#ordersTable").show();	
        		 $("#auditReplyDiv").show();	
        		 $("#auditDescDiv").show();	
        		 if(orderEvaluateResultJson.evaluateContent!=null&&orderEvaluateResultJson.evaluateContent!=''){
     			    $("#evaluateContent").text(orderEvaluateResultJson.evaluateContent);
     		 }else{
     			    $("#evaluateContent").text("暂无评论内容哦~");
     		 }
    		}
    	}
    }
    form.render();
	var flag=false;
    //审核通过并保存
$('.submit').click(function(){
	//判空验证
	if($("#auditDesc").val().trim()==null||$("#auditDesc").val().trim()==''){
		layer.alert('请输入审核备注内容', {icon: 2}); 
		return  false;
	}
	if(/\s/.test($("#auditDesc").val())){
	  	layer.alert('不能有空格', {icon: 2}); 
	      return false;
	  }
	//判断内容输入合法性
    if(/[@#\$%\^&\*\￥\+\-\.\/]+/g.test($("#auditDesc").val())){
    	layer.alert('不能有特殊字符【#@%&*....等】', {icon: 2}); 
	      return false;
	    }
	if(/(^\_)|(\__)|(\_+$)/.test($("#auditDesc").val())){
		
		layer.alert('首尾不能出现下划线\'_\'', {icon: 2}); 
	      return false;
	  }
	if($("#auditDesc").val().trim().length>200){
		layer.alert('审核备注内容限制200字以内', {icon: 2}); 
		return  false;
	}
	layer.confirm("请确认审核【保存后无法修改备注信息】", function(confirmIndex){
		common.post(rootPath + '/order/evaluate/orderExamine', 
				{ "id" : $("#id").val(),"auditDesc":$("#auditDesc").val()},
				 function(msg) {
					common.hide();
					if (msg.ok) {
						// 提交成功刷新数据表格，关闭弹出层
						layer.alert("保存成功。",function(){
						layer.closeAll();
						$('.submit').hide();
						$('.unExamine').hide();
						 $("#auditDesc").attr("readonly",'readonly');
						 //（不关闭窗口）刷新父页面
						 window.parent.document.getElementById("reload").click();
						 flag=true;
						});
					}else{
						layer.alert(msg.msg);
					}
				});
	})
	})
$('.submitReply').click(function(){
	//判空验证
	if($("#auditReply").val().trim()==null||$("#auditReply").val().trim()==''){
		layer.alert('请输入回复内容', {icon: 2}); 
		return  false;
	}
	if(/\s/.test($("#auditReply").val())){
	  	layer.alert('不能有空格', {icon: 2}); 
	      return false;
	  }
	//判断内容输入合法性
    if(/[@#\$%\^&\*\￥\+\-\.\/]+/g.test($("#auditReply").val())){
    	layer.alert('不能有特殊字符【#@%&*....等】', {icon: 2}); 
	      return false;
	    }
	if(/(^\_)|(\__)|(\_+$)/.test($("#auditReply").val())){
		
	  	layer.alert('首尾不能出现下划线\'_\'', {icon: 2}); 
	      return false;
	  }
	if($("#auditReply").val().trim().length>200){
		layer.alert('回复内容限制200字以内', {icon: 2}); 
		return  false;
	}	
	
	//判断是否已经审核通过再回复
	if(flag||orderEvaluateResultJson.auditStats==1){
		layer.confirm("请确认回复(回复后无法修改)", function(confirmIndex){
			common.post(rootPath + '/order/evaluate/orderReply', 
					{ "id" : $("#id").val(),"auditReply":$("#auditReply").val()},
					 function(msg) {
						common.hide();
						if (msg.ok) {
							// 提交成功刷新数据表格，关闭弹出层
							layer.alert("保存成功。",function(){
							layer.closeAll();
							$('.submit').hide();
							$('.unExamine').hide();
							 $("#auditDesc").attr("readonly",'readonly');
							$('.submitReply').hide();
							 $("#auditReply").attr("readonly",'readonly');
							});
							form.render(); 
							 window.parent.document.getElementById("reload").click();
				}else{
							layer.alert(msg.msg);
						}
					});
			})
	}else{
	layer.confirm("请确认回复【如果未审核，回复保存即默认审核通过】", function(confirmIndex){
	common.post(rootPath + '/order/evaluate/orderReply', 
			{ "id" : $("#id").val(),"auditReply":$("#auditReply").val()},
			 function(msg) {
				common.hide();
				if (msg.ok) {
					// 提交成功刷新数据表格，关闭弹出层
					layer.alert("保存成功。",function(){
					layer.closeAll();
					$('.submit').hide();
					$('.unExamine').hide();
					 $("#auditDesc").attr("readonly",'readonly');
					$('.submitReply').hide();
					 $("#auditReply").attr("readonly",'readonly');
					});
					form.render(); 
					 window.parent.document.getElementById("reload").click();
		}else{
					layer.alert(msg.msg);
				}
			});
	})}
})

		//禁用评论
$('.unExamine').click(function(){
	layer.confirm("确认将该评论设置为审核不通过？", function(confirmIndex){
		common.post(rootPath + '/order/evaluate/unExamineOrWaiteForExamine', 
				{ "id" : $("#id").val(),"auditStats":3,auditDesc:$("#auditDesc").val()},
				 function(msg) {
					common.hide();
					if (msg.ok) {
						// 提交成功刷新数据表格，关闭弹出层
						layer.alert("操作成功。",function(){
						layer.closeAll();
						window.parent.document.getElementById("reload").click();
						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
						parent.layer.close(index);
						});
			}else{
						layer.alert(msg.msg);
					}
				});
		})}
)












	$('.btn-close').click(function(){
		//当你在iframe页面关闭自身时
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭  
	});
});