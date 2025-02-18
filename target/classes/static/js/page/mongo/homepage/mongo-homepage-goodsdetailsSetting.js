layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl,
	laytpl = layui.laytpl, laydate = layui.laydate,
	table = layui.table, common = layui.common;
	
	
	//$(".disabledTable").find("input,textarea,select").not("").attr("readonly","readonly")


    var ruleid=0;
    var Ohref=window.location.href;
    if (Ohref.indexOf("?ruleId=") !== -1) {
       var arrhref=Ohref.split("?ruleId=");
        //截取上个页面传来的规则id
        ruleid=arrhref[1].split("&id=")[0];
    }
    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
    $("._submit").click(function(){
    		var code=$("#code").val();
	    	var tmpText=$('.productCode').text();
	    	if(tmpText==''){
				  layer.alert("先查询商品，确认无误后再保存",{icon: 2});
			      return false;
			}
    		if(tempIsSale==2){
    			  layer.alert("该商品已下架,请另选商品!",{icon: 2});
    		      return false;
    		}
    		if(code!=tmpText){
    			  layer.alert("查询的商品Code与商品详细的Code不一致!",{icon: 2});
    		      return false;
    		}
    		
    /*		if(tempIsSale==0){
  			  layer.alert("该商品未通过审核,请另选商品!",{icon: 2});
  		      return false;
  		}*/
              var activityGoodsRuleId=$("#activityGoodsRuleId").val();
              if(activityGoodsRuleId==null||activityGoodsRuleId==''){
            	  layer.alert("请选择邀请页栏目");
            	  return false;
              }
              if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test($("#sort").val())||$("#sort").val()==0){
            	  layer.alert("排序必须是正整数",{icon: 2});
    		      return false;
    		    }
              
              $.ajax({
                    type: 'GET',
                    url:  rootPath + '/mongo/homepagemodule/activity/goods/rule/saveDetails?ruleId='+activityGoodsRuleId+"&code="+code+"&sort="+$("#sort").val(),
                    dataType: "json",
                    success: function (msg) {
						if (msg.ok){
                            //common.hide();
                            layer.alert("保存成功。",function(){
                                     layer.closeAll();
                                     window.parent.location.reload();
                                     //刷新当前页面
                                   //  window.location.reload();
                                     //刷新父页面
                              /*       window.parent.document.getElementById("refSignValue").value="";
                                     window.parent.document.getElementById("refSignName").value="";
                                	 window.parent.document.getElementById("reload").click();*/
                             });
           
						}else {
							console.info(msg)
                          //  common.hide();
                            layer.alert(msg.msg,function(){
                                layer.closeAll();
                            });
						}
                    },

                });
                return false;
            });

    var tempIsSale=3;
    $(".search").click(function(){
        var code=$("#code").val();
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/homepagemodule/activity/goods/rule/searchGoods?ruleId='+ruleid+"&code="+code,
            dataType: "json",
            success: function (msg) {
            	if(msg.msg==null){
            		$('.productCode').text('');
                	$('.productName').text('');
                	$('.companyName').text('');
            	}
                if (msg.code==="true"){
                    common.hide();
                    if (msg.data.length<=0) {
                        return true;
                    }
                    var product=msg.data[0];
                    tempIsSale=product.isSale;
              		if(product.isSale==$("#isSale1").val()){
              			$("#isSale1").attr("checked",true);
              		}	
              		if(product.isSale==$("#isSale2").val()){
              			$("#isSale2").attr("checked",true);
              		}	
              		if(product.isSale==$("#isSale3").val()){
              			$("#isSale3").attr("checked",true);
              		}	
              		form.render();
                    //自动绑定 属性值
                  $.each(product, function(k, v){
                        if(v instanceof Array){
                        }else{
                            if ("."+k===".is_sale") {
                                var is_sale=v==="0"? "待审核":v==="1"?  "上架":v==="2"? "下架":"";
                                var spanDiv = $("."+k);
                                if(spanDiv){
                                    spanDiv.text(is_sale);
                                }
                            }else if ("."+k===".marketAudit") {
                              var marketAudit=v==="0"? "待审核":v==="1"? "审核通过":v==="2"? "不通过":v==="3"? "审核异常":v==="4"? "下架":"";
                                var spanDiv = $("."+k);
                                if(spanDiv){
                                    spanDiv.text(marketAudit);
                                }
                            }else if ("."+k===".activityState") {
                                var activityState=v==="0"?  "未设置":v==="1"? "已设置":v==="2"? "已上架":v==="3"? "已下架":""
                                var spanDiv = $("."+k);
                                if(spanDiv){
                                    spanDiv.text(activityState);
                                }
                            }else {
                                var spanDiv = $("."+k);
                                if(spanDiv){
                                    spanDiv.text(v);
                                }
                            }
                        }
                    });
                }else {
                    common.hide();
                    if (msg.msg==="商品Code不能为空") {
                    }else {
                        layer.alert(msg.msg,function(){
                            layer.closeAll();
                        });
                    }
                }
            },

        });
        return false;
    });

    var editopen=function () {
        $("#code").val(code);
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/homepagemodule/activity/goods/rule/searchGoods?ruleId='+ruleid+"&code="+code,
            dataType: "json",
            success: function (msg) {
                if (msg.code==="true"){
                    common.hide();
                    if (msg.data.length<=0) {
                        return true;
                    }
                    var product=msg.data[0];
                    //自动绑定 属性值
                    $.each(product, function(k, v){
                        if(v instanceof Array){
                        }else{
                            var spanDiv = $("."+k);
                            if(spanDiv){
                                spanDiv.text(v);
                            }
                        }
                    });
                }else {
                    common.hide();
                    layer.alert(msg.msg,function(){
                        layer.closeAll();
                    });
                }
            },

        });

        return false;
    }

    // var operTypeTmp=JSON.parse(operType);
   // var productJson=JSON.parse(product);

    // if(operTypeTmp==2){
    	var temp=JSON.parse(distributionProductResult)
        var goodstem=JSON.parse(goods);
    	// console.info(activityGoodsRuleDetailsResultJson)
     	if(temp.productCode!=null&&temp.productCode!=''){
     	 	$(".productCode").text(temp.productCode);
        	$("#code").val(temp.productCode);
        	$("#sort").val(goodstem.sortNum);
        	$(".productName").text(temp.productName);
        	$(".companyName").text(temp.companyName);
        	$("input[name='Product_Code']").attr("disabled","disabled");
        	$("input[name='sort']").attr("disabled","disabled");
        	if(temp.isSale==$("#isSale1").val()){
      			$("#isSale1").attr("checked",true);
      		}	
      		if(temp.isSale==$("#isSale2").val()){
      			$("#isSale2").attr("checked",true);
      		}	
		if(temp.isSale==$("#isSale3").val()){
      			$("#isSale3").attr("checked",true);
      		}
     	}else{
     		layer.alert("商品不存在");
     	}
    	// if(goods!=null&&goods!=''){
        	// $("#activityGoodsRuleId").val(goods.activityGoodsRuleId);
      	// 	$("select[name='activityGoodsRuleId']").attr("disabled","disabled");
      	// 	document.getElementById("saveToList").setAttribute("disabled", true);
      	// 	$("#saveToList").css("background-color","#d2d2d2");
      	// 	$("#search").css("background-color","#d2d2d2");
      	// 	$("#search").attr("disabled",true);
      	// 	$("#search").css("pointer-events","none");
    	// }
    	form.render();
    // }
    


});