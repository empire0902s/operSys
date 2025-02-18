layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl,
	laytpl = layui.laytpl, laydate = layui.laydate,
	table = layui.table, common = layui.common;

    var ruleid=0;var type=0;
    var Ohref=window.location.href;
    if (Ohref.indexOf("?ruleId=") !== -1) {
        var　arrhref=Ohref.split("?ruleId=");
        //截取上个页面传来的规则id
        ruleid=arrhref[1].split("&type=")[0];
        type=arrhref[1].split("&type=")[1];
    }
    
    $("select[url]").loadSelect();
    var dataTable="";
    if (type==="1"){
        dataTable = common.renderTable({
            page: {
                limit: 5,
                limits: [5, 10, 15, 50]
            },
            url: common.getUrl('/coupons/activity/goods/rule/distrFirstCategotyList'),
            where:  $(".data-list-form").serializeJSON(),
            id:'catlist',
            cols: [[
                {type:'checkbox'},
                {field:'id', title: '类目id', width:100, sort: true},
                {field:'name', title: '类目名称', width: 150},
                {field:'status', title: '审核状态', width: 200,templet:function(val,row){
                        if(val.status===0){
                            return "未审核";
                        }else if (val.status===1){
                            return "已审核";
                        }
                    }},
                {field:'createuser', title: '创建人', width: 220},
                {field:'createtime', title: '创建时间', width: 220},
                {field:'checkMark', title: '审核备注', width: 220},
                // {fixed: 'right', title: '操作', width: 170,align:'center', toolbar:'#editBar'}
            ]],
        });
    } else if (type==="2"){
        dataTable = common.renderTable({
            page: {
                limit: 5,
                limits: [5, 10, 15, 50]
            },
            url: common.getUrl('/coupons/activity/goods/rule/distrDistrSecondCategotyList'),
            where:  $(".data-list-form").serializeJSON(),
            id:'catlist',
            cols: [[
                {type:'checkbox'},
                {field:'id', title: '类目id', width:100, sort: true},
                {field:'name', title: '类目名称', width: 150},
                {field:'status', title: '审核状态', width: 200,templet:function(val,row){
                        if(val.status===0){
                            return "未审核";
                        }else if (val.status===1){
                            return "已审核";
                        }
                    }},
                {field:'createuser', title: '创建人', width: 220},
                {field:'createtime', title: '创建时间', width: 220},
                {field:'checkMark', title: '审核备注', width: 220},
                // {fixed: 'right', title: '操作', width: 170,align:'center', toolbar:'#editBar'}
            ]],
        });
    } else if (type==="3"){
        dataTable = common.renderTable({
            page: {
                limit: 5,
                limits: [5, 10, 15, 50]
            },
            url: common.getUrl('/coupons/activity/goods/rule/distrDistrThirdCategotyList'),
            where:  $(".data-list-form").serializeJSON(),
            id:'catlist',
            cols: [[
                {type:'checkbox'},
                {field:'id', title: '类目id', width:100, sort: true},
                {field:'name', title: '类目名称', width: 150},
                {field:'status', title: '审核状态', width: 200,templet:function(val,row){
                        if(val.status===0){
                            return "未审核";
                        }else if (val.status===1){
                            return "已审核";
                        }
                    }},
                {field:'createuser', title: '创建人', width: 220},
                {field:'createtime', title: '创建时间', width: 220},
                {field:'checkMark', title: '审核备注', width: 220},
                // {fixed: 'right', title: '操作', width: 170,align:'center', toolbar:'#editBar'}
            ]],
        });
    } else if (type==="4") {
        dataTable = common.renderTable({
            page: {
                limit: 5,
                limits: [5, 10, 15, 50]
            },
            url: common.getUrl('/coupons/activity/goods/rule/distrDistrFourCategotyList'),
            where:  $(".data-list-form").serializeJSON(),
            id:'catlist',
            cols: [[
                {type:'checkbox'},
                {field:'id', title: '类目id', width:100, sort: true},
                {field:'name', title: '类目名称', width: 150},
                {field:'status', title: '审核状态', width: 200,templet:function(val,row){
                        if(val.status===0){
                            return "未审核";
                        }else if (val.status===1){
                            return "已审核";
                        }
                    }},
                {field:'createuser', title: '创建人', width: 220},
                {field:'createtime', title: '创建时间', width: 220},
                {field:'checkMark', title: '审核备注', width: 220},
                // {fixed: 'right', title: '操作', width: 170,align:'center', toolbar:'#editBar'}
            ]],
        });
    }


    var categoryArray;
    table.on('checkbox(data-list)', function(obj){
        var checkStatus = table.checkStatus('catlist')
            ,data = checkStatus.data;
        categoryArray=JSON.stringify(data);//将数据转为json字符串
        categoryArray=JSON.parse(categoryArray);//返回的是一个json数组
    });


    $('.btn-check').click(function() {
        if (categoryArray == null || categoryArray.length == 0) {
            layer.alert("请选择需要添加到列表的数据");
            return;
        }

        var categorystr=JSON.stringify(categoryArray);
        categorystr=encodeURI(categorystr);
        common.wait("提交中...");
        $.ajax({
            type: 'GET',
            url:  rootPath + '/coupons/activity/goods/rule/saveCategoryDetails?ruleId='+ruleid+"&categorystr="+categorystr,
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
                    title: '查看类目详请',
                    type: 2,
                    area: ['95%', '95%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/goods/rule/supplier?catetype="+type,
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