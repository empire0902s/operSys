layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl, laydate = layui.laydate,
        table = layui.table, common = layui.common;

    $("select[url]").loadSelect();
    var dataTable = common.renderTable({
        page: {
            limit: 10,
            limits: [ 10, 15, 50]
        },
        url: common.getUrl('/message/shoprolelist'),
        where:  $(".data-list-form").serializeJSON(),
        id:'suplist',
        cols: [[
            {type:'checkbox'},
            {field:'usertype', title: '角色', width:300, templet:function(val,row){
                    if(val.usertype==="2"){
                        return "商家";
                    }else if (val.usertype==="3"){
                        return "普通用户";
                    } else if (val.usertype==="4") {
                        return "代理商";
                    }else if (val.usertype==="5"){
                        return "供应商";
                    }
                }},
            // {field:'mobile', title: '手机号码', width: 150},
            // {field:'shopname', title: '供应商名', width: 150},
            // {field:'username', title: '用户名', width: 230},
            // {field:'adressdetail', title: '详细地址', width: 220},

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

    var index = parent.layer.getFrameIndex(window.name);

    $('.btn-check').click(function() {
        if (supIdsArray == null || supIdsArray.length == 0) {
            layer.alert("请选择需要添加到列表的数据");
            return;
        }

        if (supIdsArray.length>1){
            layer.alert("只能选择一个");
            return;
        }

        var seqsstr="";

        for ( var i = 0; i <supIdsArray.length; i++){
            seqsstr +=supIdsArray[i].usertype;
        }

        parent.$('.seqs').val(seqsstr);

        var role="";
        if(supIdsArray[0].usertype==="2"){
            role="商家";
        }else if(supIdsArray[0].usertype==="3"){
            role="普通用户";
        }else if (supIdsArray[0].usertype==="4") {
            role="代理商";
        }else if (supIdsArray[0].usertype==="5") {
            role="供应商";
        }
        parent.$('.detail').val("角色为:"+role);

        parent.layer.close(index);

    });

    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });


});