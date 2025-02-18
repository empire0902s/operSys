layui.use(['table', 'laytpl', 'form', 'common','upload','laydate','element'], function() {
    var form = layui.form, laytpl = layui.laytpl,
        laytpl = layui.laytpl, upload = layui.upload,
        table = layui.table, common = layui.common,element = layui.element,
        laydate=layui.laydate;

    form.render();
    $(".pcite").hover(
        function(e){
            layer.tips('仅包含下单购买次数大于一次的用户数量统计', '.divcite', {
                tips: 3
                ,type:4
            });
        },
        function(e){
        }
    );
    //获取最近30天日期
    var end=getDay(0);//当天日期
    var start=getDay(-6);//6天前日期
    var time=laydate.render({
        elem: '#statisticsTime'
        ,range: true
        ,max:end
    });
    $("#statisticsTime").val(start+" - "+end);
    $("#reload").click(function(){
        if($("#statisticsTime").val()==""){
            layer.alert('必须选择统计日期范围', {icon: 2});
            return false;
        }
        var timeArrs= $("#statisticsTime").val().split(" - ");
        var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
        if(tangeDays>30){
            layer.alert('查询日期范围最大长度为31天', {icon: 2});
            $("#statisticsTime").val("");
            return false;
        }

    });
    function diy_time(time1,time2){
        time1 = Date.parse(new Date(time1));
        time2 = Date.parse(new Date(time2));
        return time3 =Math.abs(parseInt((time2 - time1)/1000/3600/24));
    }
    function getDay(day){
        var today = new Date();

        var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;
        today.setTime(targetday_milliseconds); //注意，这行是关键代码
        var tYear = today.getFullYear();
        var tMonth = today.getMonth();
        var tDate = today.getDate();
        tMonth = doHandleMonth(tMonth + 1);
        tDate = doHandleMonth(tDate);
        return tYear+"-"+tMonth+"-"+tDate;
    }
    function doHandleMonth(month){
        var m = month;
        if(month.toString().length == 1){
            m = "0" + month;
        }
        return m;
    }

    // 初始化数据表格
    var dataTable = common.renderTable({
        url: common.getUrl('/distribute/statistics/queryGiveDayCouponslist'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'date', title: '日期', width: 150,templet:function(val){
                    return val.date.split(" ")[0];
                }},
            {field:'frequency', title: '日赠送次数', width: 150},
            {field:'num', title: '日赠送人数', width: 150},
            {field:'giveNum', title: '日发起赠送人数', width: 150},
            {field:'total', title: '日赠送金额(元)', width: 210},
        ]],
        page: {
            limit: 10,
            limits: [10,15,25,50]
        }
    });


})