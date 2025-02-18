layui.use(['table', 'laytpl', 'form', 'common','laydate','element'], function() {
    var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
        laytpl = layui.laytpl, element = layui.element,
        table = layui.table, common = layui.common;

    // 初始化编辑表单
    form.render();

    var editForm = $('.edit-form');
    // 添加事件
    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area: ['420px','275px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
                // $(".type_t").find("option[value =1]").attr("selected","selected");
                $("#sortNum").attr("disabled",false);
                $(".type_t").attr("disabled",false);
                form.render();
            }
        });
    });
    $('.btn-close').click(function(){
        layer.closeAll();
    });
    //保存
    form.on('submit(submit)', function(data) {
        if(data.field.sortNum.indexOf("+")!=-1){
            layer.alert("排序不能有特殊字符",{icon: 2});
            return false;
        }
        if(data.field.sortNum==0){
            layer.alert("排序不能为0",{icon: 2});
            return false;
        }
        common.wait("提交中...");
        common.post(rootPath + '/mongo/brandsquareclassificationmodular/save', data.field, function(msg) {
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

        url: common.getUrl(rootPath + '/mongo/brandsquareclassificationmodular/list'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:100, sort: true},
            {field:'modularName', title: '模块名称', width: 150},
            {field:'sortNum', title: '排序', width: 100},
            {field:'columnType', title: '模块类型', width: 180,templet:function(val){
                    return '品牌广场分类推荐模块';
                }},
            {field:'columnType', title: '模块状态', width: 180,templet:'#modularStatusTpl'},
            {fixed: 'right',title: '操作',width: 320, align:'center', toolbar:'#editBar'}
        ]],
    });


    $('.btn-close').click(function(){
        layer.closeAll();
    });

    var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'details':
                layer.open({
                    title:'详细信息',
                    type: 2,
                    area: ['55%','50%'],
                    shadeClose: true,
                    content: rootPath + '/mongo/brandsquareclassificationmodular/details?id='+data.id
                });
                break;
            case 'edit':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area: ['420px','275px'],
                    content: editForm,
                    success: function(){
                        //$("#sortNum").attr("disabled",true);
                        $(".type_t").attr("disabled",true);
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });
                break;
            case 'delete':
                layer.confirm("确定是否删除["+data.modularName+"]?",  {
                    btn: ['确定','取消'] //按钮
                    ,cancel: function(index, layero){
                        form.render();
                    }
                }, function(confirmIndex){
                    common.post( rootPath + '/mongo/brandsquareclassificationmodular/delete', {id:data.id}, function(msg) {
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
                }, function(){  //取消
                    form.render();
                });
                break;
            default:
                break;
        }
    });

    //三个禁用启用按钮的操作
    form.on('switch(modularStatusFilter)', function(obj){
        var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
        var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
        if(!flag){
            layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {
                btn: ['确定','取消'] //按钮
                ,cancel: function(index, layero){
                    obj.elem.checked=true;
                    form.render();
                }
            }, function(confirmIndex){
                common.post( rootPath + '/mongo/brandsquareclassificationmodular/updateModularStatus', {id:obj.value,modularStatus:1}, function(msg) {
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
            }, function(){  //取消
                obj.elem.checked=true;
                form.render();
            });
        }else{
            layer.confirm("确定是否启用["+tmp.innerText+"]?", {
                btn: ['确定','取消'] //按钮
                ,cancel: function(index, layero){
                    //取消操作，点击右上角的X
                    obj.elem.checked=false;
                    form.render();
                }
            }, function(confirmIndex){
                //是  		common.wait("提交中...");
                common.post( rootPath + '/mongo/brandsquareclassificationmodular/updateModularStatus', {id:obj.value,modularStatus:2}, function(msg) {
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
            }, function(){  //取消
                obj.elem.checked=false;
                form.render();
            });
        }
    });
});