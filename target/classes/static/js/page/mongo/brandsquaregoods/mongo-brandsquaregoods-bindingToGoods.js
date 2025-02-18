layui.use(['table', 'laytpl', 'form', 'common'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl,
        table = layui.table, common = layui.common;

    $("select[url]").loadSelect();
    // 初始化编辑表单
    form.render();
    form.on('submit(submit)', function(data) {

        if(data.field.goodsCode==''){
            layer.alert("请完善品牌商品信息",{icon: 2});
            return false;
        }

        if(data.field.sortNum.indexOf("+")!=-1){
            layer.alert("排序不能有特殊字符(加号)",{icon: 2});
            return false;
        }

        if(data.field.sortNum==0){
            layer.alert("排序不能为0",{icon: 2});
            return false;
        }

        if(data.field.reductionGoodsPrice==0){
            layer.alert("砍价价格不能为0",{icon: 2});
            return false;
        }
        if(data.field.brokerageMoney==0){
            layer.alert("佣金不能为0",{icon: 2});
            return false;
        }
        common.wait("提交中...");
        common.post( rootPath + '/mongo/brandsquaregoods/save', data.field, function(msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    parent.location.reload();
                    layer.closeAll();
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });

    $('.btn-close').click(function(){
        layer.closeAll();
        //关闭当前内嵌页面窗口
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    var editForm=$("#editForm");
    $(".search").click(function(){
        var code=$("#code").val();
        if(code==""){
            return;
        }
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/brandsquaregoods/findByCode?productCode='+code,
            dataType: "json",
            success: function (msg) {
                if(!msg.ok){
                    layer.alert(msg.msg,{icon: 2});
                }
                console.info(msg)
                var data=msg.value;
                console.info("data------"+data.goodsCode)
                //手动绑定数据到页面
                $(".product_Code").text(data.goodsCode);
                $("#goodsCode").val(data.goodsCode);
                $(".product_Name").text(data.goodsName);
                $(".company_name").text(data.companyName);
                $(".distributionPrice").text(data.distributionPrice);
                $(".goodsPrice").text(data.goodsPrice);
                editForm.loadData(data);
                form.render();
            },
        });
    });

    var operateTypeTmp=operateType;
    var productJson=JSON.parse(product);
    var tmp=JSON.stringify(productJson);
    var temp=$.parseJSON(tmp);
    var priceReductionGoodsTempJson=JSON.parse(priceReductionGoodsTemp);
    if(operateTypeTmp==2){
        $(".product_Code").text(priceReductionGoodsTempJson.goodsCode);
        $(".product_Name").text(temp.productName);
        $(".company_name").text(temp.companyName);
        $("input[name='productCode']").attr("disabled","disabled");
        $("input[name='productCode']").val(priceReductionGoodsTempJson.goodsCode);
        if(priceReductionGoodsTempJson.financeState==1){
            $("#editForm").find("input,textarea,select").not("").attr("readonly","readonly")
            document.getElementById("saveToList").setAttribute("disabled", true);
            $("#saveToList").css("background-color","#d2d2d2");
        }



        editForm.loadData(temp);
        editForm.loadData(priceReductionGoodsTempJson);
        form.render();
    }
});