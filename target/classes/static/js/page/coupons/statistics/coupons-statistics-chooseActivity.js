layui.use(['table', 'laytpl', 'laydate', 'form', 'common','layer'], function() {
	var form = layui.form, 
	table = layui.table, 
	laytpl = layui.laytpl, 
	laydate = layui.laydate, 
	layer = layui.layer,
	common = layui.common;

    $("select[url]").loadSelect();
    
    var ue_Add = null;
	var activityIdsArray;
	table.on('checkbox(data-list)', function(obj){
		var checkStatus = table.checkStatus('chooseActivityList')
	      ,data = checkStatus.data;
		activityIdsArray=JSON.stringify(data);//将数据转为json字符串
		activityIdsArray=JSON.parse(activityIdsArray);//返回的是一个json数组
		});
	var couponsIds="";
	//保存活动
	$('.btn-check').click(function(){
		if(activityIdsArray==null||activityIdsArray.length==0){
			layer.alert("请选择一个活动");
			return;
		}
		couponsIds="";
		if(activityIdsArray.length>1){
			layer.alert("只能选择一个活动");
			return false;
		}
		window. parent.setValue(activityIdsArray[0].name,activityIdsArray[0].id,$("#timeRange").val()); //这是父页面函数
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index);
	});
	$('.btn-close').click(function(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index);
	})
	

 // 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl( rootPath + '/coupons/statistics/chooseActivityList'),
	    where:  $(".data-list-form").serializeJSON(),
	    id:'chooseActivityList',
	    cols: [[
	      {type:'checkbox'},
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'name', title: '活动名称', width: 170},
	      {field:'batch', title: '活动批次', width: 170},
            // {field:'activityGoodsRuleId', title: '活动商品规则Id', width: 170 },

            {field:'useType', title: '活动类型', width: 170 ,templet:function(val,row){
                    if(val.useType===1){
                        return "优惠券";
                    }  else if (val.useType===2) {
                        return  "商品活动";
                    }
                } },
	      {field:'sendTimeStart', title: '开始时间', width: 160},
	      {field:'sendTimeEnd', title: '结束时间', width: 160},
	    ]],
    });
	laydate.render({
		elem : "input[name='sendTimeStart']"
		,type: 'datetime'
	});

    var et =laydate.render({
		elem : "input[name='sendTimeEnd']"
		,type: 'datetime',
        done:function(value,date){
           var strTime=value.substr(11,8);
           if ("00:00:00"===strTime){
               et.config.dateTime={
                   year:date.year,
                   month:date.month-1,
                   date: date.date,
                   hours: 23,
                   minutes: 59,
                   seconds : 59
               };
           }
        }
	});

	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	var index;
	 // 初始化编辑表单
	form.render();


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

    var editForm = $('.edit-form');
    // 添加事件

	$('.btn-close').click(function(){
		layer.closeAll();
	});


    form.on('select(type)', function(data){
    	if (data.value==="1"){
			$("#activityGoodsRuleId").val("");
            $("#actid").hide();
		}else {

            $("#actid").show();
		}
    });



    var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
});

