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

    // 初始化数据表格
    var dataTable = common.renderTable({
        url: common.getUrl('/message/list'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50},
            // {field:'userSeq', title: '用户seq', width: 120},
            {field:'userPhone', title: '用户手机号',width:120},
            // {field:'receiveTypeDetail', title: 'receiveTypeDetail',width:120},
            {field:'msgType', title: '消息类型',width: 100,templet:function(val,row){
                    if(val.msgType==="ismsg"){
                        return "短信";
                    // }else if (val.msgType==="isEmail"){
                    //     return "邮件";
                    // } else if (val.msgType==="isApp") {
                    } else if (val.msgType==="isApp") {
                        return "推送";
                    }
                }},
            {field:'sendingPlatform', title: '发送平台',width: 100,templet:function(val,row){
                    if(val.sendingPlatform===1){
                        return "爱之家";
                    }
                    if(val.sendingPlatform===2){
                        return "生活圈";
                    }
                    if(val.sendingPlatform===3){
                        return "520批发网";
                    }
                    if(val.sendingPlatform===4){
                        return "520拼价";
                    }
                }},

            {field:'sendStatus', title: '发送状态', width: 100,templet:function(val,row){
                    if(val.sendStatus===0){
                        return "发送中";
                    }else if (val.sendStatus===1){
                        return "发送成功";
                    } else if (val.sendStatus===2) {
                        return "发送失败";
                    }
                }},
            {field:'remark', title: '备注', width: 100},
            {field:'messageReturnType', title: '返回状态', width: 100},
            {field:'createAdmin', title: '发送人', width: 100},
            {field:'createTime', title: '发送时间', width: 200},
            {field:'msgDesc', title: '标题', width: 150},
            {field:'msgContent', title: '消息内容', width: 400},
            {field:'jumpUrl', title: '跳转链接', width: 400},

            // {fixed: 'right',title: '操作', width:170, align:'center', toolbar:'#editBar'}
        ]],
    });

    // $('#plat').hide();

    var editForm = $('.edit-form');
    var editForm1 = $('.edit-form1');
    // 添加事件
    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();

        $(" input[ name='msgType'] ").val("1");
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

    $('.push').click(function(){
        editForm1.find('form')[0].reset();
        $(" input[ name='msgType'] ").val("2");
        layer.open({
            title:'添加',
            type: 1,
            area: ['600px'],
            content: editForm1,
            success: function(){
                editForm1.loadData({id:''});
            }
        });
    });


    $('.sync').click(function(){
        common.wait("提交中...");

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
    $('.type').hide();
    $(".seq").hide();
    form.on('submit(submit)', function(data) {
        if (data.msgDesc===""||data.msgDesc===null) {
            layer.alert("描述不能为空");
            return false;
        }

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

    $('.deletedetail').click(function () {
        $('.seqs').val("");
        $('.detail').val("");
    });


    form.on('radio(type)', function(data){

        if (data.value==="1"){
            $('.adddetail').show();
            $('.deletedetail').show();
            $('.app').hide();
            $('.detail').val("");
        }
        if (data.value==="2"){
            $('.adddetail').show();
            $('.deletedetail').show()
            $('.app').hide();
            $('.detail').val("");
        }
        if (data.value==="3"){
            $('.adddetail').show();
            $('.deletedetail').show();
            $('.app').hide();
            $('.detail').val("");
        }
        if (data.value==="4"){
            $('.adddetail').hide();
            $('.deletedetail').hide();
            $('.app').show();
            $('.detail').val("");
        }

    });

    form.on('radio(app)', function(data){
        if (data.value==="1"){
            $('.detail').val("ios推送");
        }
        if (data.value==="2"){
            $('.detail').val("安卓推送");
        }
        if (data.value==="3"){
            $('.detail').val("ios推送+安卓推送");
        }

    });


    $('.adddetail').click(function () {
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