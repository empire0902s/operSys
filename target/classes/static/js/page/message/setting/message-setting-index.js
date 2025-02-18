layui.use(['table', 'laytpl', 'laydate', 'form', 'common','upload'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl, laydate = layui.laydate,
        table = layui.table, common = layui.common;


    // 初始化编辑表单
    form.render();
    $("select[url]").loadSelect();

    form.on('submit(submit)', function(data) {
        common.wait("提交中...");
        common.post( rootPath + '/message/save', data.field, function(msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    common.reloadTable(dataTable);
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });
    form.verify({
        pwd: [/.{6,}/, '请输入6-32位密码']
    });

    // 初始化数据表格
    var dataTable = common.renderTable({
        url: common.getUrl('/message/setting/list'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50},
            {field:'msgDesc', title: '发送批次标题', width: 200},
            {field:'receiveTypeDetail', title: '接受信息类别明细',width:200},
            // {field:'receiveTypeDetail', title: 'receiveTypeDetail',width:120},
            {field:'receiveType', title: '接受信息类别',width: 200,templet:function(val,row){
                    if(val.receiveType===1){
                        return "手机号";
                    }else if (val.receiveType===2){
                        return "角色";
                    } else if (val.receiveType===3) {
                        return "区域";
                    }else if (val.receiveType===4){
                        return "平台";
                    }
                }},
            {field:'sendingPlatform', title: '发送平台',width: 200,templet:function(val,row){
                    if(val.sendingPlatform===1){
                        return "爱之家";
                    }else if (val.sendingPlatform===2){
                        return "生活圈";
                    } else if (val.sendingPlatform===3) {
                        return "520批发网";
                    }else if (val.sendingPlatform==4){
                        return "520拼价";
                    }
                }},
            {field:'messageNum', title: '发送用户数量', width: 120},
            {field:'jumpUrl', title: '跳转链接', width: 200},
            {field:'msgTypeJson', title: '消息类型', width: 200},
            {field:'msgContent', title: '消息内容', width: 200},
            {field:'createAdmin', title: '创建人', width: 100},
            {field:'createTime', title: '创建时间', width: 200},


            // {fixed: 'right',title: '操作', width:170, align:'center', toolbar:'#editBar'}
        ]],
    });

    var editForm = $('.edit-form');
    // 添加事件
    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area: ['600px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            }
        });
    });

    $('.btn-close').click(function(){
        layer.closeAll();
    });

    $('.send').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            id:"if1",
            area:['80%', '80%'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            },
        });
    });

    $("#seq").hide();
    form.on('submit(submit)', function(data) {

        common.wait("提交中...");
        common.post( rootPath + '/message/save', data.field, function(msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    common.reloadTable(dataTable);
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });

    $('#deletedetail').click(function () {
        $("#seqs").val("");
        $("#detail").val("");

    });

    $('#adddetail').click(function () {
        // console.log(111);
        // alert("1");var mobile=$("input[name='mobile']").val();
        var receiveType = $("input[name='receiveType']:checked").val();
        // var receiveType=$("input[name='receiveType']").val();
        if (receiveType==="1"){
            layer.open({
                title: '添加',
                type: 2,
                area: ['95%', '95%'],
                shadeClose: true,
                content: rootPath + "/message/phone",
                success:function (layero,index) {

                }
            });
        }else if (receiveType==="2") {
            layer.open({
                title: '添加',
                type: 2,
                area: ['80%', '80%'],
                shadeClose: true,
                content: rootPath + "/message/role",
                success:function (layero,index) {

                }
            });
        }else if (receiveType==="3") {
            layer.open({
                title: '添加',
                type: 2,
                area: ['80%', '80%'],
                shadeClose: true,
                content: rootPath + "/message/eare",
                success:function (layero,index) {

                }
            });
        }

    });

    $('.sync').click(function(){
        common.wait("提交中...");
        // $.get(rootPath + '/message/sync',function(data,status){
        //     alert("Data: " + data + "\nStatus: " + status);
        // });
        //

        common.post( rootPath + '/message/sync',null, function(msg) {
            console.log(msg);
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    common.reloadTable(dataTable);
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });

    $('.historysync').click(function(){
        common.wait("提交中...");
        common.post( rootPath + '/message/historysync',null, function(msg) {
            console.log(msg);
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    common.reloadTable(dataTable);
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });

    var editForm = $('.edit-form'), roleForm = $('.role-form');
    //监听工具条
    // table.on('tool(data-list)', function(obj){
    //     var data = obj.data;
    //     switch (obj.event) {
    //         case 'send':
    //             alert("1");
    //             layer.open({
    //                 title:'发送',
    //                 type: 1,
    //                 area: ['600px'],
    //                 content: editForm,
    //                 success: function(){
    //                     // 加载表单，重新渲染
    //                     editForm.loadData(data);
    //                     form.render();
    //                 }
    //             });
    //             break;
    //
    //         default:
    //             break;
    //     }
    // });
});