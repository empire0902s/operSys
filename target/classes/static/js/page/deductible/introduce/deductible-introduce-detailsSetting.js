layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl, laydate = layui.laydate,
        table = layui.table, common = layui.common;

    // 初始化编辑表单
    form.render();
    //截取上个页面传过来的参数
    var Ohref=window.location.href;
    var arrhref=Ohref.split("?ruleId=");
    var aray=arrhref[1].split("&type=");
    //规则id
    var ruleid=aray[0]
    //规则类型
    var rtype=aray[1];

    // 初始化数据表格
    var dataTable = common.renderTable({
        url: common.getUrl('/deductible/introduce/detailList?introduceId='+ruleid),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50, sort: true},
            {field:'title', title: '标题', width: 300},
            {field:'content', title: '内容', width: 300},
            {field:'sort', title: '排序', width: 300},
            // {field:'updateTime', title: '更新时间', width: 160},
            // {field:'updateAdmin', title: '更新人', width: 160},
            {fixed: 'right',title: '操作', width:200, align:'center', toolbar:'#editBar'}
        ]],
    });

    var editForm = $('.edit-form');
    // 添加事件
    $('.btn-add').click(function(data){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area: ['550px','400px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            }
        });
    });

    form.on('submit(submit)', function(data) {
        data.field.introduceId=ruleid;
        common.wait("提交中...");
        common.post(rootPath + '/deductible/introduce/saveDetail', data.field, function(msg) {
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


    $('.btn-return').click(function(){
        window.history.go(-1);
    });

    $('.btn-close').click(function(){
        layer.closeAll();
    });

    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'update':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area: ['550px','400px'],
                    content: editForm,
                    success: function(){
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });
                break;
            case 'delete':
                layer.confirm(" 确定是删除["+data.title+"]"+"明细?", function(confirmIndex){
                    common.wait("提交中...");
                    $.ajax({
                        type: 'GET',
                        url:  rootPath + '/deductible/introduce/delateDetail?detailId='+data.id,
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
});