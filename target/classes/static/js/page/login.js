layui.use([ 'form', 'common' ], function() {
	var form = layui.form, $ = layui.$, common = layui.common;
	// 监听提交
	form.on('submit(formSubmit)', function(data) {
		common.post('/loginAdmin', data.field, function(msg) {
			if (msg.ok) {
				common.wait('页面跳转中');
				if (self == top) {
					window.location.href = rootPath + '/';
				} else {
					top.location.reload();
				}
			} else {
				var alert = layer.alert(msg.msg,function(){
						layer.close(alert);
				});
			}
		});
		return false;
	});
	
	//使用手机验证码登陆
	$(".phoneLogin").click(function(){
		//清空用户名与密码
		$("input[name=name]").val("");
		$("input[name=pwd]").val("");
		//隐藏用户名与密码输入框
		$("#nameDiv").hide();
		$("#pwdDiv").hide();
		//删除用户名与密码输入框的验证
		$("input[name=name]").removeAttr('lay-verify').val("");
		$("input[name=pwd]").removeAttr('lay-verify').val("");
		//显示手机号码与验证码输入框
		$("#getCode").show();
		$("#phoneDiv").show();
		$("#verificationCodeDiv").show();
		$("input[name=phone]").attr('lay-verify','phone');
		$("input[name=verificationCode]").attr('lay-verify','required');
		$(".phoneLogin").hide();
		$("#accountLogin").show();
	});
	
	
	//点击使用已有账号登陆
	$("#accountLogin").click(function(){
		//清空手机号与验证码
		$("input[name=verificationCode]").val("");
		$("input[name=phone]").val("");
		//
		$("#phoneDiv").hide();
		$("#verificationCodeDiv").hide();
		$("#getCode").hide();
		//
		$("input[name=verificationCode]").removeAttr('lay-verify').val("");
		$("input[name=phone]").removeAttr('lay-verify').val("");
		//显示
		$("#nameDiv").show();
		$("#pwdDiv").show();
		$("input[name=pwd]").attr('lay-verify','required');
		$("input[name=name]").attr('lay-verify','required');
		$(".phoneLogin").show();
		$("#accountLogin").hide();
	});
	
	$("#getCode").click(function(){
		if($("input[name=phone]").val()==""){
			layer.alert('请输入手机号码', {icon: 2}); 
				return  false;
			}
/*		if($("input[name=phone]").val()!=""){
			document.getElementById("formSubmit").click();
			return  false;
		}*/
		common.post(rootPath + '/message/sendVerificationCode', 
				{ "mobile" : $("input[name=phone]").val()},
				 function(msg) {
					common.hide();
					console.info(msg)
					if (msg.ok) {
						// 提交成功刷新数据表格，关闭弹出层
						layer.alert("发送成功。",function(){
						layer.closeAll();
						settime();
						});
					}else{
						layer.alert(msg.msg);
					}
				});
		
	})
	
	
	var countdown=60;  
    var _generate_code = $("#getCode");  
    function settime() {  
      if (countdown == 0) {  
        _generate_code.attr("disabled",false);  
        _generate_code.text("获取验证码");  
        countdown = 60;  
        //恢复鼠标可点击
        $("#getCode").css("pointer-events","auto");
    	$("#getCode").css("background-color","#1E9FFF");
        return false;  
      } else {  
    	//设置鼠标不可点击
		$("#getCode").css("pointer-events","none");
		$("#getCode").css("background-color","#d2d2d2");
        _generate_code.text("重新发送(" + countdown + ")");  
        countdown--;  
      }  
      setTimeout(function() {  
        settime();  
      },1000);  
    } 
	
});