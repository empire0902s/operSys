layui.use( ['common'], function() {
	var common = layui.common;
	
	var surl = rootPath + "/images/424141.gif";//row是table的当前行
	  $("body").css("background-image","url("+surl+")");
	  $("body").css("background-repeat","no-repeat");
	  $("body").css("background-attachment","fixed");
	  $("body").css("background-size","100% 100%");
});