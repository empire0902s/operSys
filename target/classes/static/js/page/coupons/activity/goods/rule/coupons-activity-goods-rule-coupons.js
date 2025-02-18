layui.use(['table', 'laytpl', 'form', 'common', 'laydate' ], function() {
    var form = layui.form, laytpl = layui.laytpl,
        table = layui.table,layer=layui.layer,
        common = layui.common,laydate = layui.laydate;
    // 初始化编辑表单
    form.render();

    $("select[url]").loadSelect();

    //截取上个页面传过来的参数
    var Ohref=window.location.href;
    var arrhref=Ohref.split("?categoryRuleId=");

    //优惠券id
    var couponid=arrhref[1];

    $('.btn-close').click(function(){
        layer.closeAll();
    });

    // 初始化数据表格
    var dataTable = common.renderTable({
        height: 315 ,
        url: common.getUrl('/coupons/activity/goods/rule/couponsList?categoryRuleId='+couponid),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:80, sort: true},
            {field:'name', title: '优惠券名称', width: 180},
            {field:'activityName', title: '活动名称', width: 180},
            {field:'couponsTypeDesc', title: '金额规则', width: 160},
            {field:'categoryRuleName', title: '活动商品规则', width: 160},
            {field:'finanStatus', title: '财务审核', width: 90,templet:function(val,row){
                    if(val.finanStatus== "1"){
                        return "审核通过";
                    }  else{
                        return "<span style='color:red'>未审核</span>";
                    }
                } },
            {field:'sendNum', title: '发放总数量', width: 100,templet:function(val,row){
                    if(val.sellNum=="0"){
                        return "不限数量";
                    }  else{
                        return val.sendNum;
                    }
                } },
            {field:'sellNum', title: '已发数量', width: 90,templet:function(val,row){
                    if(val.sellNum==null){
                        return 0;
                    }  else{
                        return val.sellNum;
                    }
                } },
            {field:'receiveNumRule', title: '限制用户领取次数', width: 100,templet:function(val,row){

                    if(val.receiveNumRule=="0"){
                        return "无限制领取次数";
                    }  else{
                        return val.receiveNumRule;
                    }
                } },
            {field:'sendTimeStart', title: '发放开始时间', width: 200},
            {field:'sendTimeEnd', title: '发放结束时间', width: 200},
            {field:'isDisabled', title:'是否禁用', width:120, templet:function(val,row){

                    if(val.isDisabled===false){
                        return "否";
                    }  else{
                        return "是";
                    }
                } },
            // {fixed: 'right', title: '操作', width: 180,align:'center', toolbar:'#editBar'}
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
                common.post( rootPath + '/coupons/coupons/disabled', {id:obj.value}, function(msg) {
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
                common.post( rootPath + '/coupons/coupons/enableBy', {id:obj.value}, function(msg) {
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
    //toolbar事件监听
    table.on('tool(data-list)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            case 'details':
                layer.open({
                    title:'详情',
                    type: 2,
                    area: ['68%','83%'],
                    shadeClose: true,
                    content: rootPath + '/coupons/coupons/couponsDetails?id='+data.id
                });
                break;
            case 'edit':
                layer.open({
                    title:'编辑',
                    type: 2,
                    area: ['800px','500px'],
                    shadeClose: true,
                    content:  rootPath + '/coupons/coupons/add?type=2&&id='+data.id,
                    success: function (layero, index) {
                        /* var body = layer.getChildFrame('body',index);//建立父子联系
                         var iframeWin = window[layero.find('form')[0]['addForm']];
                      // 加载表单，重新渲染
                      iframeWin.loadData(data);*/
                        form.render();
                    }
                });
                break;
            default:
                break;
        }
    });

});