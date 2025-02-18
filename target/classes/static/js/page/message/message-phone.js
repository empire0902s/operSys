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
        url: common.getUrl('/message/shoplist'),
        where:  $(".data-list-form").serializeJSON(),
        id:'suplist',
        cols: [[
            {type:'checkbox'},
            {field:'seq', title: '用户seq', width:150, sort: true},
            {field:'mobile', title: '手机号码', width: 150},
            // {field:'shopname', title: '供应商名', width: 150},
            {field:'username', title: '用户名', width: 230},
            {field:'adressdetail', title: '详细地址', width: 320},

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
        if (supIdsArray.length >20){
            layer.alert("不能选择超过20条数据");
            return;
        }
        //
        // if (supIdsArray.length>1){
        //     layer.alert("只能选择一个");
        //     return;
        // }

        var seqsstr="";var mobile="";

        for ( var i = 0; i <supIdsArray.length; i++){
            seqsstr +=supIdsArray[i].seq+",";
            mobile +=supIdsArray[i].mobile+",";
        }
        mobile=mobile.substr(0,mobile.length-1);
        seqsstr=seqsstr.substr(0,seqsstr.length-1);

        var pareseqs=parent.$('.seqs').val();


        var pareMobile= parent.$('.detail').val();
        if ((pareMobile!==null|pareMobile!=="")&pareMobile.indexOf("手机号为")>-1){
            parent.$('.detail').val(pareMobile+","+mobile);
            parent.$('.seqs').val(pareseqs+","+seqsstr);
        } else  {
            parent.$('.detail').val("手机号为:"+mobile);
            parent.$('.seqs').val(seqsstr);
        }

        parent.layer.close(index);

    });

    $(".off").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });


});