layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl,
	laytpl = layui.laytpl, laydate = layui.laydate,
	table = layui.table, common = layui.common;
	
	
	$(".disabledTable").find("input,textarea,select").not("").attr("readonly","readonly")


    var ruleid=0;
    var Ohref=window.location.href;
    if (Ohref.indexOf("?ruleId=") !== -1) {
        var　arrhref=Ohref.split("?ruleId=");
        //截取上个页面传来的规则id
        ruleid=arrhref[1].split("&id=")[0];
    }
    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    $("._submit").click(function(){
              var code=$("#code").val();
                common.wait("提交中...");
                $.ajax({
                    type: 'GET',
                    url:  rootPath + '/coupons/activity/goods/rule/saveDetails?ruleId='+ruleid+"&code="+code,
                    dataType: "json",
                    success: function (msg) {
						if (msg.ok){
                            common.hide();
                            layer.alert("保存成功。",function(){
                                            layer.closeAll();
                                        });
                            window.parent.location.reload();
						}else {
                            common.hide();
                            layer.alert(msg.msg,function(){
                                layer.closeAll();
                            });
						}
                    },

                });

                return false;

            });

    $(".search").click(function(){
        var code=$("#code").val();
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/coupons/activity/goods/rule/searchGoods?ruleId='+ruleid+"&code="+code,
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
        // console.log("15315");
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/coupons/activity/goods/rule/searchGoods?ruleId='+ruleid+"&code="+code,
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

    setTimeout(function() {
        // IE
        if(document.all) {
            document.getElementById("search").click();
        }
        // 其它浏览器
        else {
            var e = document.createEvent("MouseEvents");
            e.initEvent("click", true, true);
            document.getElementById("search").dispatchEvent(e);
        }
    }, 300);


});