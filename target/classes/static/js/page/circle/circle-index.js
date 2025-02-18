layui.use(['table', 'laytpl', 'laydate', 'form', 'common','layer','upload'], function() {
    var form = layui.form,
        table = layui.table,
        laytpl = layui.laytpl,
        laydate = layui.laydate,
       upload=layui.upload,
        common = layui.common;

    $("select[url]").loadSelect();

    var ue_Add = null;

    
    //默认名称复选框监听事件
    form.on('checkbox(headImg)', function(data){
    	if(data.elem.checked)
    	{
    		$("#name").val("");
    	}
    });  
    
    //默认头像复选框监听事件
    form.on('checkbox(defaultHeadImg)', function(data){
    	if(data.elem.checked)
    	{
    		$("#headImg").val("");
    	    $('#contentImgDisplay').hide();
            $('#contentImgDisplay').attr('src',""); //图片链接（base64）
    	}
    });
    
    //小编名称输入框点击事件
    $('#name').click(function(){
    		$("#aizhijia").attr("checked",false);
     	   form.render();
    });
    
/*    $('#file').click(function(){
        //去除默认头像复选框勾选
		$('#defaultHeadImg').attr("checked",false);
		form.render();
    });*/

    // 初始化数据表格
    var dataTable = common.renderTable({
        url: common.getUrl( rootPath + '/circle/list'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50, sort: true},
            {field:'createTime', title: '发布时间', width:170},
            {field:'type', title: '发布类型', width: 170 ,templet:function(val,row){
                    if(val.type===1){
                        return "每日爆款";
                    }  else if (val.type===2) {
                        return  "宣传推广";
                    }
                } },
            {field:'goodsType', title: '发布类型', width: 170 ,templet:function(val,row){
                    if(val.goodsType===1){
                        return "普通商品";
                    }  else if (val.goodsType===2) {
                        return  "秒杀商品";
                    }
                } },
            {field:'isShow', title: '是否展示详情', width: 170 ,templet:function(val,row){
                    if(val.isShow===1){
                        return "展示";
                    }  if (val.isShow===2) {
                        return  "不展示";
                    }else {
                        return  "";
                    }
                } },
            {field:'reedPeopleNum', title: '查看人数', width: 160},
            {field:'reedNum', title: '查看次数', width: 160},
            {field:'clickNum', title: '点击商品次数', width: 160},
            {field:'clickPeopleNum', title: '点击商品人数', width: 160},
            {field:'shareNum', title: '分享次数', width: 160,templet:function(val){

                        return val.shareNum+"\t\t<a class=\"layui-btn layui-btn-warm layui-btn-xs\" lay-event=\"updateNum\" >修改</a>"
                    }},
            {field:'realShare', title: '真实分享次数', width: 160},
            {field:'createAdmin', title: '创建人', width: 160},
            {field:'updateAdmin', title: '最后编辑人', width: 160},
            {fixed: 'right',title: '操作', width:188, align:'center', toolbar:'#editBar'}
        ]],
    });
    form.on('switch(isDisabledFilter)', function(obj){
        var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
        var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
        if(flag){
            layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {
                btn: ['确定','取消'] //按钮
                ,cancel: function(index, layero){
                    obj.elem.checked=false;
                    form.render();
                }
            }, function(confirmIndex){
                //是  		common.wait("提交中...");
                common.post( rootPath + '/coupons/activity/disabled', {id:obj.value}, function(msg) {
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
        }else{
            layer.confirm("确定是否启用["+tmp.innerText+"]?", {
                btn: ['确定','取消'] //按钮
                ,cancel: function(index, layero){
                    //取消操作，点击右上角的X
                    obj.elem.checked=true;
                    form.render();
                }
            }, function(confirmIndex){
                //是  		common.wait("提交中...");
                common.post( rootPath + '/coupons/activity/enableBy', {id:obj.value}, function(msg) {
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
        }
    });
    laydate.render({
        elem: '#timeRange'
        ,range: true
    });

    form.verify({
        pwd: [/.{6,}/, '请输入6-32位密码']
    });
    var index;
    // 初始化编辑表单
    form.render();

    form.verify({
        sendTimeEnd:function (value,item) {
            var $startTime=$(" input[ name='sendTimeStart' ] ").val();
            if (value<=$startTime){
                return '活动结束时间不能小于活动开始时间';
            }
        }
    });


    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };
//
//头像图片上传
    var uploadInst = upload.render({
        elem: '#file'
        ,url: rootPath+'/coupons/coupons/upload'
        ,size:"1024"//限制上传图片大小，单位是kb
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#contentImgDisplay').show();
                $('#contentImgDisplay').attr('src', result); //图片链接（base64）
            });
            bFlag=1;
        }
        ,done: function(res){
            //上传成功
            if(res.code ==0){
                layer.msg('上传成功');
                var imgPathVal=document.getElementById('headImg').value;
                document.getElementById('headImg').value=res.data.src;
                $("#checkBigImg").attr("href",document.getElementById('headImg').value);
            	$('#defaultHeadImg').attr("checked",false);
        		form.render();
                bFlag=0;
            }
            //如果上传失败
            if(res.code > 0){
                $('#contentImgDisplay').hide();
                $("#contentImg").val("");
                return layer.msg('上传失败');
            }

        }
    });

    var pitrue=0;
    //图片上传
    var uploadInst2 = upload.render({
        elem: '#file2'
        ,url: rootPath+'/coupons/coupons/upload'
        ,size:"1024"//限制上传图片大小，单位是kb
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#contentImgDisplay2').show();
                $('#contentImgDisplay2').attr('src', result); //图片链接（base64）
            });
            pitrue=1;
        }
        ,done: function(res){
            //上传成功
            if(res.code ==0){
                layer.msg('上传成功');
                var imgPathVal=document.getElementById('headImg2').value;
                document.getElementById('headImg2').value=res.data.src;
                $("#checkBigImg2").attr("href",document.getElementById('headImg2').value);
                pitrue=2;
            }
            //如果上传失败
            if(res.code > 0){
                $('#contentImgDisplay2').hide();
                $("#contentImg").val("");
                return layer.msg('上传失败');
            }
        }
    });

    form.verify({
        number: function(value, item){ //value：表单的值、item：表单的DOM对象

            if(!/^\d+\d+\d$/.test(value)){
                return "只能填数字";
            }

            if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(value)) {
                //     layer.alert("排序必须是正整数",{icon: 2});
                return "只能填数字";
            }
        }

    });


    var GetLength = function (str) {
        ///<summary>获得字符串实际长度，中文2，英文1</summary>
        ///<param name="str">要获得长度的字符串</param>
        var realLength = 0, len = str.length, charCode = -1;
        for (var i = 0; i < len; i++) {
            charCode = str.charCodeAt(i);
            if (charCode >= 0 && charCode <= 128) realLength += 1;
            else realLength += 2;
        }
        return realLength;
    };

    form.on('submit(submit)', function(data) {

        if (pitrue===1){
            layer.alert("请等待图片上传完毕");
            return false;
        }

        if ( data.field.content===''|| data.field.content===null){
            layer.alert("请输入内容");
            return false;
        }

       var leng=GetLength(data.field.content);
        var length1=GetLength(data.field.comment);

        if (data.field.type==="2"){
            data.field.isShow="1";
        }

        if (leng>4500){
            layer.alert("内容太长");
            return false;
        }

        if(length1>1800){
            layer.alert("评论内容太长");
            return false;
        }

        if (data.field.type==="1"){
            data.field.picture='';
            if (data.field.commoditySku===null||data.field.commoditySku==="") {
                layer.alert("请输入商品code");
                return false;
            }
            if (data.field.goodsType===""){
                layer.alert("请选择商品类型");
                return false;
            }
        }
        if (data.field.type==="2"){
            data.field.goodsType="";
        }

        if (data.field.headImg===""||data.field.headImg===null) {
            layer.alert("请添加头像");
            return false;
        }
        if (data.field.name===""||data.field.name===null) {
            layer.alert("请添加名称");
            return false;
        }


        common.wait("提交中...");
        common.post( rootPath + '/circle/save', data.field, function(msg) {
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


    form.on('submit(submit1)', function(data) {

        common.wait("提交中...");
        common.post( rootPath + '/circle/updateShareNum', data.field, function(msg) {
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


    var editForm = $('.edit-form');
    // 添加事件
    $('.btn-add').click(function(){
        $('#piture').hide();

        $('#contentImgDisplay').hide();
        $('#contentImgDisplay').attr('src', ''); //图片链接（base64）

        $("#comment").show()// 显示;
        $("#sku").show()// 显示;
        $("#isShow").show();
        $("#isShow").show();
        $("#headImg").val("");
        $('#goodsType').show();
        editForm.find('form')[0].reset();
        var time = new Date().Format("yyyyMMddHHmmssS");
        $(" input[ name='batch' ] ").val(time);
        layer.open({
            title:'添加',
            type: 1,
            area:  ['800px', '500px'],
            content: editForm,
            success: function(){
                $("select[url]").loadSelect();
                editForm.loadData({id:''});
                editForm.find("input,select").not("input[name='name'],input[name='isDisabled']").removeAttr("disabled");
                $("#actid").hide();

                $('#contentImgDisplay2').hide();
                $('#contentImgDisplay2').val('');
                ue_Add = ueditor.getEditor('activityDesc',{
                    toolbars:[['undo', 'redo', '|', 'emotion',
                        'bold', 'italic', 'underline','|', 'cleardoc']],
                    autoClearinitialContent:false,
                    wordCount:false,
                    elementPathEnabled:false,
                    pasteplain:true,
                    retainOnlyLabelPasted:true,
                    initialFrameHeight:230,
                    initialFrameWidth:500
                });

            },
            end: function(){
                if(ue_Add){
                    ue_Add.destroy();
                }

            }
        });
    });

    $('.btn-close').click(function(){
        layer.closeAll();
    });


    var editForm = $('.edit-form'), editForm1 = $('.edit-form1'),grantForm=$('.grant-form');

    form.on('select(type)', function(data) {
        if (data.value == 1) {
            //指定时间
            // $("#comment").find('input').attr('lay-verify','required').removeAttr("disabled").val("");
            // $("#comment").show()// 显示;
            $("#sku").find('input').attr('lay-verify','required').removeAttr("disabled").val("");
            $("#sku").show()// 显示;
            $("#isShow").show();
            $('#piture').hide();
            $('#goodsType').show();
            // $('#headImg2').val('');

        } else {
            // $("#comment").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
            // $("#comment").hide()// 隐藏;
            $("#sku").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
            $("#sku").hide()// 隐藏;
            $("#isShow").hide();
            $('#piture').show();
            $('#goodsType').hide();
            $('#goods').val("");
        }
    });


    //监听工具条
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'edit':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area:  ['800px', '500px'],
                    content: editForm,
                    success: function(){
                        editForm.loadData(data);
                        $("#aizhijia").val("爱之家商城");
                        $("#defaultHeadImg").val("http://192.168.1.36:8019/UploadFile/YY/20181102/2018110214400518519.jpeg");
                        if (data.type===1){
                            // $("#comment").find('input').attr('lay-verify','required').removeAttr("disabled").val("");
                            $("#comment").show()// 显示;
                            // $("#sku").find('input').attr('lay-verify','required').removeAttr("disabled").val("");
                            $("#sku").show()// 显示;
                            $("#isShow").show();
                            $('#piture').hide();
                        } else {
                            $("#comment").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
                            $("#comment").hide()// 隐藏;
                            $("#sku").find('input').attr('disabled',true).removeAttr('lay-verify').val("");
                            $("#sku").hide()// 隐藏;
                            $("#isShow").hide();
                            $('#piture').show();
                        }


                        $('#contentImgDisplay').show();
                        $('#contentImgDisplay').attr('src', data.headImg); //图片链接（base64）


                        if (data.picture!==null){
                            $('#contentImgDisplay2').show();
                            $('#contentImgDisplay2').attr('src', data.picture); //图片链接（base64）
                        }

                        //移除多余选项
                        form.render();

                        ue_Add = ueditor.getEditor('activityDesc',{
                            toolbars:[[ 'undo', 'redo', '|', 'emotion',
                                'bold', 'italic', 'underline','|', 'cleardoc']],
                            autoClearinitialContent:false,
                            wordCount:false,
                            elementPathEnabled:false,
                            pasteplain:true,
                            retainOnlyLabelPasted:true,
                            initialFrameHeight:230,
                            initialFrameWidth:500
                        });
                    },
                    end: function(){
                        if(ue_Add){
                            ue_Add.destroy();
                        }
                    }
                });
                break;
            case 'delete':
                layer.confirm("确定删除?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post( rootPath + '/circle/delete', {id:data.id}, function(msg) {
                        common.hide();
                        if (msg.ok) {
                            // 提交成功刷新数据表格，关闭弹出层
                            layer.alert("删除成功。",function(){
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
            case 'disabled':
                layer.confirm("确定是否禁用["+data.name+"]?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post( rootPath + '/coupons/activity/disabled', {id:data.id}, function(msg) {
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
            case 'enableBy':
                layer.confirm("确定是否启用["+data.name+"]?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post(rootPath + '/coupons/activity/enableBy', {id:data.id}, function(msg) {
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
            case 'details':
                layer.open({
                    title:'详细信息',
                    type: 2,
                    area: ['55%','37%'],
                    shadeClose: true,
                    content:  rootPath + '/coupons/activity/couponsActivityDetails?id='+data.id
                });
                break;

            case 'updateNum':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area:  ['500px'],
                    content: editForm1,
                    success: function(){
                        // 加载表单，重新渲染
                        editForm1.loadData(data);
                        form.render();
                    }
                });
                break;
            default:
                break;
        }
    });
});