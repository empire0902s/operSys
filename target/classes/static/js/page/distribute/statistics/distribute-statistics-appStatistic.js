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
        url: common.getUrl('/distribute/statistics/queryAppStaticsList'),
        where:  $(".data-list-form").serializeJSON(),
        cols: [[
            {field:'createTime', title: '日期', width: 150,templet:function(val){
                    return val.createTime.split("T")[0];
                }},
            {field:'installationCounts', title: '安装次数', width: 150},
            {field:'startupCounts', title: '启动次数', width: 150},
            {field:'instaCounts520', title: '渠道520下载启动数', width: 150},
            {field:'instaCounts360', title: '渠道360下载启动数', width: 150},
            {field:'instaCountIOS', title: '渠道IOS下载启动数', width: 150},
            {field:'instaCountBaidu', title: '渠道百度下载启动数', width: 150},
            {field:'instaCountsTencent', title: '渠道腾讯下载启动数', width: 150},
            {field:'instaCountSugou', title: '渠道搜狗下载启动数', width: 150},
            {field:'instaCountHuawei', title: '渠道华为下载启动数', width: 150},
            {field:'instaCountXiaomi', title: '渠道小米下载启动数', width: 150},
            {field:'instaCountVivo', title: '渠道vivo下载启动数', width: 150},
            {field:'instaCountWandoujia', title: '渠道豌豆荚下载启动数', width: 150},
            {field:'instaCountOPPO', title: '渠道oppo下载启动数', width: 150},
            {field:'instaCountJinli', title: '渠道金立下载启动数', width: 150},

        ]],
        page: {
            limit: 10,
            limits: [10,15,25,50]
        }
    });


})