layui.use(['table', 'laytpl', 'form', 'common', 'laydate' ], function() {
    var form = layui.form, laytpl = layui.laytpl,
        table = layui.table,layer=layui.layer,
        common = layui.common,laydate = layui.laydate;
    // 初始化编辑表单
    form.render();

    $("select[url]").loadSelect();

    //截取上个页面传过来的参数
    var Ohref=window.location.href;
    var arrhref=Ohref.split("?couponsRuleId=");

    //优惠券id
    var couponsRuleId=arrhref[1];

    $('.btn-close').click(function(){
        layer.closeAll();
    });

    // 初始化数据表格
    var dataTable = common.renderTable({
        height: 315 ,
        url: common.getUrl('/coupons/couponsTypeRule/couponsList?couponsRuleId='+couponsRuleId),
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