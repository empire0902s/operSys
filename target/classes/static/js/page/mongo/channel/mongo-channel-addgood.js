layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl, laydate = layui.laydate,
        table = layui.table, common = layui.common;


    $(".disabledTable").find("textarea,select").not("").attr("readonly","readonly")

    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
    $("._submit").click(function(){

        var productCode=$('.productCode').text();
        if (productCode===null||productCode===''){
            layer.alert("请先查询商品");
            return false;
        }
        var sortNum=$("#sortNum").val();
        var modelId=$("#modelId").val();
        var modelName=$("#modelName").val();
        var uniqekey=$("#uniqekey").val();
        var id=$("#id").val();
        if (uniqekey===null||uniqekey==='') {
            layer.alert("请选择栏目");
            return false;
        }
        if(sortNum==null||sortNum===''){
            layer.alert("请填写排序");
            return false;
        }
        var num=$("#sortNum").val();
        if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(num)){
            layer.alert("排序必须是正整数",{icon: 2});
            return false;
        }if (num==="0"){
            layer.alert("排序必须大于0",{icon: 2});
            return false;
        }

        if (id===""||id===null){
            $.ajax({
                type: 'POST',
                url:  rootPath + '/mongo/channel/addGoods',
                data:{id:id,uniqeKey:uniqekey,modelId:modelId,goodsCode:productCode,sortNum:sortNum,modelName:modelName},
                dataType: "json",
                success: function (msg) {
                    if (msg.ok){
                        common.hide();
                        layer.alert("保存成功。",function(){
                            layer.closeAll();
                            //刷新当前页面
                            window.location.reload();
                            //刷新父页面
                            window.parent.document.getElementById("reload").click();
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
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
        } else {
            $.ajax({
                type: 'POST',
                url:  rootPath + '/mongo/channel/updateGoods',
                data:{id:id,uniqeKey:uniqekey,modelId:modelId,goodsCode:productCode,sortNum:sortNum,modelName:modelName},
                dataType: "json",
                success: function (msg) {
                    if (msg.ok){
                        common.hide();
                        layer.alert("保存成功。",function(){
                            layer.closeAll();
                            //刷新当前页面
                            window.location.reload();
                            //刷新父页面
                            window.parent.document.getElementById("reload").click();
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
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
        }

        return false;
    });

    $(".search").click(function(){
        var code=$("#code").val();
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/homepagemodule/activity/goods/rule/searchGoods?&code='+code,
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
            url:  rootPath + '/mongo/homepagemodule/activity/goods/rule/searchGoods?&code='+code,
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
    };

    setTimeout(function() {
        var code=$("#code").val();
        // IE
        if(document.all) {
            if (code!==null&&code!==''){
                document.getElementById("search").click();
            }
        }
        // 其它浏览器
        else {
            if (code!==null&&code!==''){
                var e = document.createEvent("MouseEvents");
                e.initEvent("click", true, true);
                document.getElementById("search").dispatchEvent(e);
            }
        }
    }, 300);

});