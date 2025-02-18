layui.use(['table', 'laytpl', 'form', 'common'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl,
        table = layui.table, common = layui.common;

    // 初始化编辑表单
    form.render();
    $("select[url]").loadSelect();

    form.on('submit(submit)', function (data) {
        common.wait("提交中...");
        common.post( rootPath + '/coupons/category/save', data.field, function (msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。", function () {
                    layer.closeAll();
                    common.reloadTable(dataTable);
                });
            } else {
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
        url: common.getUrl('/coupons/category/list'),
        where: $(".data-list-form").serializeJSON(),
        cols: [[
            {field: 'id', title: 'ID', width: 50, sort: true},
            {field: 'categoryName', title: '品类名称', width: 200},
            {field: 'categoryRange', title: '品类范围', width: 200},
            {field: 'categoryRangeDetail', title: '品类明细', width: 200},
            {field: 'categoryDesc', title: '备注', width: 250},

            {field: 'isDisabled', title: '是否禁用', templet: '#disabledTpl',width: 120},
            {fixed: 'right', title: '操作', width: 175, align: 'center', toolbar: '#editBar'}
        ]],
    });

    var editForm = $('.edit-form');

    // 添加事件
    form.on('select()', function(data){
        var inputcategoryRangeDetail= $("#categoryRangeDetail");
        var categoryRange= $("#categoryRange");
        categoryRange.val(data.id);
        var index=0;
        $("#added").find("tr").each(function(){
            if (index>0){
                $(this).remove();
            }
         index++;
        });
        inputcategoryRangeDetail.val("");
    });

    form.on( 'click',function () {
        parent.layer.msg('Hi, man', {shade: 0.3});
    });


    $("#add").click(function () {

        var idx = layer.getFrameIndex(window.name);
        // console.log(idx);
        parent.layer.open({
            title: '范围明细',
            type: 2,
            area: ['50%', '80%'],
            shadeClose: true,
            content: rootPath + "/coupons/category/scope?idx="+idx,
            success:function (layero,index) {

            }

        });
    });

    $(document).on("click","#deleteCategory",function() {

        var inputcategoryRangeDetail= $("#categoryRangeDetail");
        // var code=$(this).parents('tr').find('td').eq(0).find('.idCss').text();
        $(this).parent().parent().remove();

        var detail="";
        $("#added").find("tr").each(function(){
            var tdArr = $(this).children();
            var detailspan=tdArr.eq(1).find('.idCss').text();
            detail+=detailspan+",";

        });
        inputcategoryRangeDetail.val(detail);

    });

    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            id:"if1",
            area: ['400px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            },
        });
    });

    $('.btn-close').click(function(){
        layer.closeAll();
    });

    var editForm = $('.edit-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'detail':
                layer.open({
                    title:'详细',
                    type: 2,
                    area: ['55%','38.5%'],
                    shadeClose: true,
                    content:  rootPath + '/coupons/category/details?id='+data.id
                });
                break;
            case 'edit':
                layer.open({
                    title:'编辑',
                    type: 1,
                    id:"if1",
                    area: ['400px'],
                    content: editForm,
                    success: function(){
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });
                break;
            case 'disabled':
                layer.confirm("确定是否禁用["+data.categoryName+"]?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post( rootPath + '/coupons/category/disabled', {id:data.id,isDisabled:true}, function(msg) {
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

            case 'disabledfalse':
                layer.confirm(" 确定是否启用["+data.categoryName+"]?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post( rootPath + '/coupons/category/disabled', {id:data.id,isDisabled:false}, function(msg) {
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

            default:
                break;
        }
    });
});