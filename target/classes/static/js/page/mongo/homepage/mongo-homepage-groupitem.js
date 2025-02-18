layui.use(['table', 'laytpl', 'form', 'common','laydate','element','upload','treetable'], function() {
    var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
        laytpl = layui.laytpl, element = layui.element, upload = layui.upload,
        table = layui.table, common = layui.common,   treetable = layui.treetable;;
    //图片上传
    var bFlag=1;
    var uploadInst = upload.render({
        elem: '#file'
        ,url: rootPath+'/auth/banner/upload/'
        ,size:"1024"//限制上传图片大小，单位是kb
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#imgDisplay').show();
                $('#imgDisplay').attr('src', result); //图片链接（base64）
            });
            bFlag=1;
        }
        ,done: function(res){
            //上传成功
            if(res.code ===0){
                layer.msg('上传成功');
                var imgPathVal=document.getElementById('imgUrl').value;
                document.getElementById('imgUrl').value=res.data.src;
                $(" input[ name='imgUrl' ] ").val(res.data.src);
                console.info("imgUrlVal:"+document.getElementById('imgUrl').value)
                $("#checkBigImg").attr("href",document.getElementById('imgUrl').value);
                bFlag=0;
            }
            //如果上传失败
            if(res.code > 0){
                $('#imgDisplay').hide();
                $("#imgUrl").val("");
                return layer.msg('上传失败');
            }

        }
    });
    // 初始化编辑表单
    form.render();
    var editForm = $('.edit-form');
    // 添加事件
    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area: ['400px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
                $(" input[ name='moduleId' ] ").val($("#moduleId").val());
                $("#moduleId2").val($("#moduleId").val());
                $("#moduleId2").text($("#moduleId").val());
                form.render();
            }
        });
    });
    $('.btn-close').click(function(){
        layer.closeAll();
    });

    var renderTable1 ;
    $('.btn-search').click(renderTable1=function(){
        var mid= $("#moduleId").val();
        layer.load(2);
        $.getJSON('groupItem',{mId:mid}, function (res) {
            treetable.render({
                treeColIndex: 1,
                treeSpid: -1,
                treeIdName: 'id',
                treePidName: 'pid',
                treeDefaultClose: false,
                treeLinkage: false,
                elem: '#table1',
                data: res.data,
                page: false,
                cols: [[
                    {type: 'numbers'},
                    {field: 'name', title: '名称', width: 140},
                    // {field: 'goodsCode', title: '商品code' ,width: 250},
                    {field: 'sortNum', title: '排序',width: 80},
                    {field: '', title: '属性',templet:function(val){
                        if (val.modeType===1){
                            return "\t\t<a class=\"layui-btn layui-btn-warm layui-btn-xs\" lay-event=\"bannerAdd\">banner添加</a>\t\t<a class=\"layui-btn layui-btn-normal layui-btn-xs\" lay-event=\"goodsAdd\">商品添加</a>\n\n"
                        }
                        if (val.modeType===3) {
                            var productName="",companyName="",isSale="";
                            if (val.productName!==undefined){
                                productName=val.productName;
                                productName="【"+productName+"】";
                                companyName=val.companyName;
                            }
                            if (val.isSale===0){
                                isSale="待审核";
                            } else if (val.isSale===1){
                                isSale="上架";
                            } else if (val.isSale===2){
                                isSale="下架";
                            }
                            return '商品code:'+val.goodsCode+productName+",状态:"+isSale+",供应商:"+companyName;
                        }
                        if (val.modeType===4){
                            return "<img style='width: 50px; height: 30px;'" +
                                "src='"+val.imgUrl+"'>"+",跳转链接："+val.imgTarget;
                        }else {
                            return "";
                        }


                        }},
                    // {field: 'pid', title: 'pid',width: 180},
                    // {field: 'moduleId', title: '模块id',width: 180},
                    // {field: 'id', title: 'id',width: 180},
                    {fixed: 'right',templet: '#oper-col', title: '操作',width: 300}

                ]],
                done: function () {
                    layer.closeAll('loading');
                }
            });
        });
    });

    renderTable1();

    $('#btn-expand').click(function () {
        treetable.expandAll('#table1');
    });

    $('#btn-fold').click(function () {
        treetable.foldAll('#table1');
    });

    $('#btn-refresh').click(function () {
        renderTable();
    });

    $('.btn-close').click(function(){
        layer.closeAll();
    });

    form.on('submit(submit)', function(data) {
        var mid= $("#moduleId").val();
        data.field.moduleId=mid;
        var num=$("#sortNumGroup").val();
        if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(num)){
            layer.alert("排序必须是正整数",{icon: 2});
            return false;
        }else if (num==="0") {
            layer.alert("排序必须大于0",{icon: 2});
            return false;
        }

        common.wait("提交中...");
        common.post(rootPath + '/mongo/homepage/additem', data.field, function(msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    $("#reload").click();
                    // common.reloadTable(dataTable);
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });

    form.on('submit(savegoods)', function(data) {

        var mid= $("#moduleId").val();
        data.field.moduleId=mid;
        common.wait("提交中...");
        common.post(rootPath + '/mongo/homepage/addItemGoods', data.field, function(msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    $("#reload").click();
                    // common.reloadTable(dataTable);
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });


    form.on('submit(savebanner)', function(data) {
        var num=$("#sortNumBanner").val();
        if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(num)){
            layer.alert("排序必须是正整数",{icon: 2});
            return false;
        }else if (num==="0") {
            layer.alert("排序必须大于0",{icon: 2});
            return false;
        }

        var mid= $("#moduleId").val();
        data.field.moduleId=mid;
        // 没有上传图片前
        if(data.field.imgUrl==""){
        	layer.alert('请上传图片', {icon: 2});
        	return false;
        }
        if(bFlag==1){
        	layer.alert('请等待图片上传完成', {icon: 2});
        	return false;
        }
        common.wait("提交中...");
        common.post(rootPath + '/mongo/homepage/addItemBanner', data.field, function(msg) {
            common.hide();
            if (msg.ok) {
                // 提交成功刷新数据表格，关闭弹出层
                layer.alert("操作成功。",function(){
                    layer.closeAll();
                    $("#reload").click();
                });
            }else{
                layer.alert(msg.msg);
            }
        });
        return false;
    });



    var goodseditForm = $('.edit-form-goods'), bannereditForm = $('.edit-form-banner'),detaileditForm=$('.edit-form-item');
    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {

            case 'banneredit':
                bFlag=0;
                var mid= $("#moduleId").val();
                data.moduleId=mid;
                data.id=data.dataId;
                $(" input[ name='groupItemId' ] ").val(data.groupItemId);
                $(" input[ name='goodsItemId' ] ").val(data.goodsItemId);
                $('#imgDisplay').show();
                $('#imgDisplay').attr('src', data.imgUrl); //图片链接（base64）

                layer.open({
                    title:'banner编辑',
                    type: 1,
                    area: ['400px'],
                    content: bannereditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        bannereditForm.loadData(data);
                        form.render();
                    }
                });
                break;

            case 'goodsedit':

                var mid= $("#moduleId").val();
                layer.open({
                    title: '添加商品',
                    type: 2,
                    area: ['95%', '95%'],
                    shadeClose: true,
                    content: rootPath + "/mongo/homepage/addgood",
                    success:function (layero,index) {

                        $("#code", layero.find("iframe")[0].contentWindow.document).val(data.goodsCode);
                        $("#id", layero.find("iframe")[0].contentWindow.document).val(data.dataId);
                        $("#groupItemId", layero.find("iframe")[0].contentWindow.document).val(data.groupItemId);
                        $("#goodsItemId", layero.find("iframe")[0].contentWindow.document).val(data.goodsItemId);
                        $("#moduleId", layero.find("iframe")[0].contentWindow.document).val(mid);
                        $("#isItemGoods", layero.find("iframe")[0].contentWindow.document).val(1);
                        $("#sortNum", layero.find("iframe")[0].contentWindow.document).val(data.sortNum);

                    }
                });

                break;


            case 'groudedit':
                var mid= $("#moduleId").val();
                data.moduleId=mid;
                data.id=data.dataId;
                $(" input[ name='groupItemId' ] ").val(data.groupItemId);
                $(" input[ name='goodsItemId' ] ").val(data.goodsItemId);
                $(" input[ name='groupName' ] ").val(data.name)

                $(" input[ name='id' ] ").val(data.goodsItemId);

                layer.open({
                    title:'组合编辑',
                    type: 1,
                    area: ['400px'],
                    content: detaileditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        detaileditForm.loadData(data);
                        form.render();
                    }
                });
                break;

            case 'delbanner':
                var mid= $("#moduleId").val();
                layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {
                    layer.close(index);
                    common.post(rootPath + '/mongo/homepage/deleteItemBanner', {moduleId:mid,id:data.dataId,groupItemId:data.groupItemId}, function(msg) {
                        common.hide();
                        if (msg.ok) {
                            // 提交成功刷新数据表格，关闭弹出层
                            layer.alert("删除成功。",function(){
                                layer.closeAll();
                                $("#reload").click();
                                // common.reloadTable(dataTable);
                            });
                        }else{
                            layer.alert(msg.msg);
                        }
                    });
                });

                break;

            case 'delgoods':
                var mid= $("#moduleId").val();
                layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {
                    layer.close(index);
                common.post(rootPath + '/mongo/homepage/deleteItemGoods', {moduleId:mid,id:data.dataId,groupItemId:data.groupItemId}, function(msg) {
                    common.hide();
                    if (msg.ok) {
                        // 提交成功刷新数据表格，关闭弹出层
                        layer.alert("删除成功。",function(){
                            layer.closeAll();
                            $("#reload").click();
                            // common.reloadTable(dataTable);
                        });
                    }else{
                        layer.alert(msg.msg);
                    }
                });
                });
                break;
            case 'delgroud':
                var mid= $("#moduleId").val();
                layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {
                    layer.close(index);
                common.post(rootPath + '/mongo/homepage/deleteItem', {moduleId:mid,id:data.dataId,groupItemId:data.groupItemId}, function(msg) {
                    common.hide();
                    if (msg.ok) {
                        // 提交成功刷新数据表格，关闭弹出层
                        layer.alert("删除成功。",function(){
                            layer.closeAll();
                            $("#reload").click();
                            // common.reloadTable(dataTable);
                        });
                    }else{
                        layer.alert(msg.msg);
                    }
                });
                });
                break;

            case 'goodsAdd':
                var mid= $("#moduleId").val();
                // var groupItemId=
                // $('.btn-add').click(function(data){
                    layer.open({
                        title: '添加商品',
                        type: 2,
                        area: ['95%', '95%'],
                        shadeClose: true,
                        content: rootPath + "/mongo/homepage/addgood",
                        success:function (layero,index) {

                            $("#code", layero.find("iframe")[0].contentWindow.document).val(data.goodsCode);
                            $("#groupItemId", layero.find("iframe")[0].contentWindow.document).val(data.groupItemId);
                            $("#goodsItemId", layero.find("iframe")[0].contentWindow.document).val(data.goodsItemId);
                            $("#moduleId", layero.find("iframe")[0].contentWindow.document).val(mid);
                            $("#isItemGoods", layero.find("iframe")[0].contentWindow.document).val(1);
                        }
                    });

                break;

            case 'bannerAdd':
                data.id="";
                $(" input[ name='groupItemId' ] ").val(data.groupItemId);
                $(" input[ name='goodsItemId' ] ").val(data.goodsItemId);
                // $(" input[ name='imgTarget' ] ").val('');
                data.imgTarget='';
                data.sortNum=1000;
                data.imgTitle='';

                $('#imgDisplay').hide();
                $('#imgDisplay').attr('src', ''); //图片链接（base64）

                layer.open({
                    title:'banner添加',
                    type: 1,
                    area: ['400px'],
                    content: bannereditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        bannereditForm.loadData(data);
                        form.render();
                    }
                });
                break;

            case 'groudadd':
                data.id="";
                // $(" input[ name='id' ] ").val("");
                $(" input[ name='groupItemId' ] ").val(data.groupItemId);
                $(" input[ name='goodsItemId' ] ").val(data.goodsItemId);
                layer.open({
                    title:'组合添加',
                    type: 1,
                    area: ['400px'],
                    content: detaileditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        detaileditForm.loadData(data);
                        $(" input[ name='sortNum' ] ").val('1000');
                        $(" input[ name='groupName' ] ").val('');
                        form.render();
                    }
                });
                break;
            case 'banneredit':
                layer.open({
                    title:'商品编辑',
                    type: 1,
                    area: ['400px'],
                    content: goodseditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        // goodseditForm.loadData(data);
                        form.render();
                    }
                });
                break;
            case 'goodsedit':
                layer.open({
                    title:'商品编辑',
                    type: 1,
                    area: ['400px'],
                    content: goodseditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        // goodseditForm.loadData(data);
                        form.render();
                    }
                });
                break;

            case 'groudedit':

                layer.open({
                    title:'组合编辑',
                    type: 1,
                    area: ['400px'],
                    content: detaileditForm,
                    success: function(){
                        // 加载表单，重新渲染
                        // detaileditForm.loadData(data);
                        form.render();
                    }
                });
                break;

            case 'delete':
                layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {
                    layer.close(index);
                    common.post(rootPath + '/mongo/homepage/deleteGoods', {
                        moduleId: data.moduleId,
                        id: data.id
                    }, function (msg) {
                        common.hide();
                        if (msg.ok) {
                            // 提交成功刷新数据表格，关闭弹出层
                            layer.alert("删除成功。", function () {
                                layer.closeAll();
                                $("#reload").click();
                                // common.reloadTable(dataTable);
                            });
                        } else {
                            layer.alert(msg.msg);
                        }
                    });
                });
                break;
            case 'edit':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area: ['400px'],
                    content: editForm,
                    success: function(){
                        // 加载表单，重新渲染
                        // editForm.loadData(data);
                        form.render();
                    }
                });
                break;
            case 'jump':
                window.open(data.loadMoreTarget);
                break;
            default:
                break;
        }
    });
});