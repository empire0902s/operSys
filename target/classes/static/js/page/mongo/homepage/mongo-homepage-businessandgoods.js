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
    		if(tempIsSale!=1){
    			  layer.alert("该商品已下架,请另选商品!",{icon: 2});
    		      return false;
    		}
        code=code.replace(/^\s+|\s+$/g,"");
        tmpText =tmpText.replace(/^\s+|\s+$/g,"");
    		if(code!==tmpText){
    			  layer.alert("查询的商品Code与商品详细的Code不一致!",{icon: 2});
    		      return false;
    		}

          if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test($("#sort").val())||$("#sort").val()==0){
              layer.alert("排序必须是正整数",{icon: 2});
              return false;
            }
              
              $.ajax({
                    type: 'GET',
                    url:  rootPath + '/mongo/homepage/saveBusinessGoods?code='+code+"&sort="+$("#sort").val(),
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


    function checkRate(nubmer) {
        var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/
        // var nubmer = document.getElementById(input).value;

        if (!re.test(nubmer)) {
            layer.alert("商品code必须是数字",{icon: 2});
            return false;
        }
    }

    $(".search").click(function(){
        var code=$("#code").val();
        code=code.replace(/^\s+|\s+$/g,"");
        checkRate(code);
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/homepage/searchGoods?ruleId='+ruleid+"&code="+code,
            dataType: "json",
            success: function (msg) {
            	if(msg.msg==null){
            		$('.productCode').text('');
                	$('.productName').text('');
                	$('.companyName').text('');
            	}

            	if (msg.code==="false"){
                    common.hide();
                    layer.alert("该商品code查询不到商品",{icon: 2});
                    return false;
                }

                if (msg.code==="true"){
                    common.hide();
                    if (msg.data.length<=0) {
                        return true;
                    }
                    var product=msg.data[0];
                    tempIsSale=product.isSale+"";

              		if(tempIsSale==="1"){
                        tempIsSale=1;
              			$("#isSale1").attr("checked",true);
              		}else if(tempIsSale==="2"){
                        tempIsSale=2;
              			$("#isSale2").attr("checked",true);
              		}else {
                        tempIsSale=0;
                        $("#isSale3").attr("checked",true);
                    }

              		var temifBrandProduct=product.isstandard+"";//0平台，1品牌，2平台
                    if(temifBrandProduct==="0"){
                        $("#ifBrandProduct1").attr("checked",true);
                    }else if(temifBrandProduct==="1"){
                        $("#ifBrandProduct2").attr("checked",true);
                    }
              		// if(product.isSale==$("#isSale3").val()){
              		// 	$("#isSale3").attr("checked",true);
              		// }
              		form.render();
                    //自动绑定 属性值
                  $.each(product, function(k, v){
                        if(v instanceof Array){
                        }else{
                            if ("."+k===".isSale") {
                                var is_sale=v==="0"? "待审核":v==="1"?  "上架":v==="2"? "下架":"";
                                var spanDiv = $("."+k);
                                if(spanDiv){
                                    spanDiv.text(is_sale);
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
                    layer.alert(msg.msg,function(){
                        layer.closeAll();
                    });
                }
            },

        });

        return false;
    }

    var operTypeTmp=JSON.parse(operType);
   // var productJson=JSON.parse(product);
    var temp=JSON.parse(distributionProductResult)

    // var goodstem=JSON.parse(goods);
    // console.info(activityGoodsRuleDetailsResultJson)
    if(temp!=null&&temp.productCode!=null&&temp.productCode!==''){
        $(".productCode").text(temp.productCode);
        $("#code").val(temp.productCode);
        // $("#sort").val(temp.sortNum);
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
        // tempIsSale=temp.isSale+"";

        // if(tempIsSale===$("#companyState1").val()){
        //     $("#companyState1").attr("checked",true);
        // }else if(tempIsSale===$("#companyState2").val()){
        //     $("#companyState2").attr("checked",true);
        // }else {
        //     $("#companyState3").attr("checked",true);
        // }

        var temifBrandProduct=temp.Isstandard+"";
        if(temifBrandProduct===$("#ifBrandProduct1").val()){
            $("#ifBrandProduct1").attr("checked",true);
        }else if(temifBrandProduct===$("#ifBrandProduct2").val()){
            $("#ifBrandProduct2").attr("checked",true);
        }else {
            $("#ifBrandProduct1").attr("checked",true);
            // $("#ifBrandProduct3").attr("checked",true);
        }
    }
    form.render();
    


});