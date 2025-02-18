layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl,
	laytpl = layui.laytpl, laydate = layui.laydate,
	table = layui.table, common = layui.common;

    $("select[url]").loadSelect();
    var dataTable = common.renderTable({
        page: {
            limit: 5,
            limits: [5, 10, 15, 50]
        },
        url: common.getUrl('/coupons/activity/goods/rule/suplist'),
        where:  $(".data-list-form").serializeJSON(),
        id:'suplist',
        cols: [[
            {type:'checkbox'},
            {field:'seq', title: '供应商seq', width:100, sort: true},
            {field:'mobile', title: '手机号码', width: 150},
            {field:'shopname', title: '供应商名', width: 150},
            {field:'adressdetail', title: '详细地址', width: 220},
            {field:'username', title: '用户名', width: 230},
            // {fixed: 'right', title: '操作', width: 170,align:'center', toolbar:'#editBar'}
        ]],
    });

    var supIdsArray;
    table.on('checkbox(data-list)', function(obj){
        var checkStatus = table.checkStatus('suplist')
            ,data = checkStatus.data;
        supIdsArray=JSON.stringify(data);//将数据转为json字符串
        supIdsArray=JSON.parse(supIdsArray);//返回的是一个json数组
    });


    var ruleid=0;
    var Ohref=window.location.href;
    if (Ohref.indexOf("?ruleId=") !== -1) {
        var　arrhref=Ohref.split("?ruleId=");
        //截取上个页面传来的规则id
        ruleid=arrhref[1].split("&id=")[0];
    }

    $('.btn-check').click(function() {
        if (supIdsArray == null || supIdsArray.length == 0) {
            layer.alert("请选择需要添加到列表的数据");
            return;
        }

        var shopperStr=JSON.stringify(supIdsArray);
        shopperStr=encodeURI(shopperStr);
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/coupons/activity/goods/rule/saveSuprDetails?ruleId='+ruleid+"&shopperStr="+shopperStr,
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
    });

    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'reed':
                layer.open({
                    title: '查看供应商详请',
                    type: 2,
                    area: ['95%', '95%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/goods/rule/supplier?ruleId="+ruleid+"&id="+data.id,
                    success:function (layero,index) {
                        $(".submit", layero.find("iframe")[0].contentWindow.document).hide();
                        $(".search", layero.find("iframe")[0].contentWindow.document).hide();
                        $("#seq", layero.find("iframe")[0].contentWindow.document).val(data.refSignValue);
                        $("#shopName", layero.find("iframe")[0].contentWindow.document).val(data.refSignName);
                        $("#seq", layero.find("iframe")[0].contentWindow.document).attr("disabled","disabled");
                        $("#shopName", layero.find("iframe")[0].contentWindow.document).attr("disabled","disabled");
                    }
                });
                break;

            case 'delete':
                layer.confirm(" 确定是删除["+data.refSignName+"]"+"规则明细?", function(confirmIndex){
                    common.wait("提交中...");
                    $.ajax({
                        type: 'GET',
                        url:  rootPath + '/coupons/activity/goods/rule/delete?detailId='+data.id,
                        dataType: "json",
                        success: function (msg) {
                            if (msg.ok){
                                common.hide();
                                layer.alert("操作成功。",function(){
                                    layer.closeAll();
                                    common.reloadTable(dataTable);
                                });
                            }else {
                                common.hide();
                                layer.alert(msg.msg,function(){
                                    layer.closeAll();
                                });
                            }
                        },

                    });

                    layer.close(confirmIndex);
                });
                break;

            default:
                break;
        }
    });


    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });


});