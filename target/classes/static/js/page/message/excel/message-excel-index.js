layui.use(['table', 'laytpl', 'laydate', 'form', 'common','upload'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl, laydate = layui.laydate,upload= layui.upload;
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
        url: common.getUrl('/message/excel/list'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50},
            {field:'batch', title: '发送批次标题', width: 120},
            {field:'phone', title: '接受用户手机号',width:150},
            // {field:'receiveTypeDetail', title: 'receiveTypeDetail',width:120},
            {field:'isMsg', title: '是否发送短信',width: 150,templet:function(val,row){
                    if(val.isMsg===0){
                        return "否";
                    }else if (val.isMsg===1){
                        return "是";
                    }
                }},
            {field:'isApp', title: '是否发送推送',width: 150,templet:function(val,row){
                    if(val.isApp===0){
                        return "否";
                    }else if (val.isApp===1){
                        return "是";
                    }
                }},
            // {field:'isEmail', title: '是否发送Email',width: 150,templet:function(val,row){
            //         if(val.isEmail===0){
            //             return "否";
            //         }else if (val.isEmail===1){
            //             return "是";
            //         }
            //     }},

            {field:'status', title: '发送状态', width: 150,templet:function(val,row){
                    if(val.status===0){
                        return "待发送";
                    }else if (val.status===1){
                        return "已发送";
                    }else if(val.status===2){
                        return "失败";
                    }
                }},
            {field:'createAdmin', title: '发送人', width: 100},
            {field:'createTime', title: '发送时间', width: 200},
            {field:'msgContent', title: '发送内容', width: 400},

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
        var batch = $('#batch').val();
        if (batch===null||batch==='undefined'||batch===''){
            layer.alert("请输入发送批次标题");
            return;
        }
        // editForm.find('form')[0].reset();
        common.wait("提交中...");
        $.ajax({
            type: 'POST',
            url: rootPath+'/message/excel/send?batch='+batch,
            // data: data,
            success: function (msg) {
                if (msg.ok){
                    common.hide();
                    layer.alert("发送成功。",function(){
                        common.reloadTable(dataTable);
                        layer.closeAll();
                    });
                }else {
                    common.hide();
                    layer.alert(msg.msg,function(){
                        layer.closeAll();
                    });
                }
            }
        });
    });


    form.on('submit(submit)', function(data) {

        common.wait("提交中...");
        common.post( rootPath + '/message/send', data.field, function(msg) {
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

    //创建一个上传组件
    upload.render({

        elem: '#upload',
        url:  rootPath + '/message/excel/uploadFile',
        // url:"https://dhsapi.520shq.com/api/UploadFile/UploadFile",
        accept: 'file', //普通文件
        multiple: false,

        before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            common.wait("导入中..."); //上传loading
    },
        // auto: false, //选择文件后不自动上传
        // bindAction: '#upload', //指向一个按钮触发上传
        done: function(msg){ //上传后的回调
            common.hide();
            if (msg.ok) {

                $('#batch').val(msg.value);
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("导入成功。",function(){
                    layer.closeAll();
                    common.reloadTable(dataTable);
                });
            }else{

                layer.alert(msg.msg);
            }

        }
        //,accept: 'file' //允许上传的文件类型
        //,size: 50 //最大允许上传的文件大小
        //,……
    })


    $('#adddetail').click(function () {

        var receiveType = $("input[name='receiveType']:checked").val();
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

});