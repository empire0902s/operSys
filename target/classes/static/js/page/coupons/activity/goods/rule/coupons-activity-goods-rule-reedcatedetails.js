layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl,
	laytpl = layui.laytpl, laydate = layui.laydate,
	table = layui.table, common = layui.common;
	
	
	$(".disabledTable").find("input,textarea,select").not("").attr("readonly","readonly")

    var catetype=0;
    var Ohref=window.location.href;
    if (Ohref.indexOf("?catetype=") !== -1) {
        var　arrhref=Ohref.split("?catetype=");
        //截取上个页面传来的规则id
        catetype=arrhref[1];
    }
    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    $(".search").click(function(){
        var id=$("#id").val();
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/coupons/activity/goods/rule/SearchCategory?id='+id+"&type="+catetype,
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
                            if ("."+k===".status") {
                                var status=v===0?  "未审核":v===1? "已审核":"";
                                var spanDiv = $("."+k);
                                if(spanDiv){
                                    spanDiv.text(status);
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