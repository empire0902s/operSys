layui.use(['table',   'form', 'common'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        table = layui.table, common = layui.common;


    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'delete':
                layer.confirm("确定是否删除["+data.shopName+"]?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post( rootPath + '/baidu/setting/delete', {id:data.id,geoId:data.geoId}, function(msg) {
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
                    layer.close(confirmIndex);
                });
                break;

            case 'update':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area: ['600px'],
                    content: editForm,
                    success: function(){
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });
                break;
            default:
                break;
        }
    });

    $('#geo').hide();
    // 初始化编辑表单
    form.render();
    $("select[url]").loadSelect();

    form.on('submit(submit)', function(data) {

        if (data.address===""||data.address===null) {
            layer.alert("店铺地址不能为空");
            return false;
        }

        if (data.shopName===""||data.shopName===null) {
            layer.alert("店铺名不能为空");
            return false;
        }

        common.wait("提交中...");
        common.post( rootPath + '/baidu/setting/save', data.field, function(msg) {
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
        url: common.getUrl('/baidu/setting/list'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50},
            // {field:'userSeq', title: '用户seq', width: 120},
            {field:'shopName', title: '店铺名',width:150},
            // {field:'receiveTypeDetail', title: 'receiveTypeDetail',width:120},
            {field:'address', title: '店铺地址', width: 300},
            {field:'latitude', title: '纬度', width: 200},
            {field:'longitude', title: '经度', width: 200},
            {field:'createAdmin', title: '创建者', width: 100},
            {field:'createTime', title: '创建时间', width: 200},

            {fixed: 'right',title: '操作', width:150, align:'center', toolbar:'#editBar'}
        ]],
    });


    var editForm = $('.edit-form');
    // 添加事件
    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area: ['500px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            }
        });
    });

    $('.look').click(function(){
        layer.open({
            title:'详情',
            type: 2,
            area: ['90%','90%'],
            shadeClose: true,
            content: rootPath + '/comm/open/hotmap'
        });
    });

    $('.btn-close').click(function(){
        layer.closeAll();
    });


});



