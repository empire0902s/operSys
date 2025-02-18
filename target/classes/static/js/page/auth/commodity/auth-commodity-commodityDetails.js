layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    form.render();

    Date.prototype.format = function(fmt) { 
        var o = { 
           "M+" : this.getMonth()+1,                 // 月份
           "d+" : this.getDate(),                    // 日
           "h+" : this.getHours(),                   // 小时
           "m+" : this.getMinutes(),                 // 分
           "s+" : this.getSeconds(),                 // 秒
           "q+" : Math.floor((this.getMonth()+3)/3), // 季度
           "S"  : this.getMilliseconds()             // 毫秒
       }; 
       if(/(y+)/.test(fmt)) {
               fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
       }
        for(var k in o) {
           if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
       return fmt; 
   }   
    
	var commodityDetailsJson=JSON.parse(commodityDetails);
    var createTime=commodityDetailsJson.createTime;
    var updateTime=commodityDetailsJson.updateTime;
    createTime = (new Date(createTime)).getTime();
    updateTime = (new Date(updateTime)).getTime();
    var createTimeForTable = new Date(createTime).format("yyyy-MM-dd");
    var updateTimeForTable = new Date(updateTime).format("yyyy-MM-dd");
    $("#createTime").text(createTimeForTable);
    $("#updateTime").text(updateTimeForTable);
    
    
});