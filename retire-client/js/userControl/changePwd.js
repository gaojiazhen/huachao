layui.use(['form', 'layer', 'laydate', 'table', 'laytpl', 'common'], function() {
	var common = layui.common;
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate,
		laytpl = layui.laytpl,
		table = layui.table;
	//获取url中的参数
	var targetId = getQueryString('userId');
	var	user=JSON.parse(sessionStorage.getItem("user"));
	$("#userName").val(user['user_name']);
	//添加验证规则
	form.verify({
		oldPwd: function(value, item) {
			if (value.length < 8) {
				return "旧密码长度不能小于8位！";
			}
			if (passwordLevel(value) != 4) {
				return "旧密码需由特殊字符+数字+大小写字母组成！";
			}
		},
		newPwd: function(value, item) {
			if (value.length < 8) {
				return "新密码长度不能小于8位！";
			}
			if (passwordLevel(value) != 4) {
				return "新密码需由特殊字符+数字+大小写字母组成！";
			};
		},
		confirmPwd: function(value, item) {
			if ($("#newPwd").val() != value) {
				return "两次输入密码不一致，请重新输入！";
			}
		}
	});
	//监听提交
	form.on('submit(formDemo)', function(data) {
		var oldPwd = doubleEncrypt(data.field.oldPwd);
		var pwd = doubleEncrypt(data.field.newPwd);
		var pwd2 = doubleEncrypt(data.field.newPwd2);
		var para = {
			"oldPwd": oldPwd,
			"pwd": pwd,
			"pwd2": pwd2
		}
		common.ajax(updatePasswordByLoginUser, 'post', 'json', para, function(res) {
			var data = res.data;
			var message = res.message;
			layer.msg(message);
			if (data == 1) {
				$("#oldPwd").val("");
				$("#newPwd").val("");
				$("#newPwd2").val("");
				setTimeout(function() {
					if (targetId) {
						window.location.href = "/index.html";
					}
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭  
					layer.close(index);
				}, 500)
			}
		})
		return false;
	});
	
	//密码组合校验
	function passwordLevel(password) {
		var Modes = 0;
		for (i = 0; i < password.length; i++) {
			Modes |= CharMode(password.charCodeAt(i));
		}
		return bitTotal(Modes);
		//CharMode函数
		function CharMode(iN) {
			if (iN >= 48 && iN <= 57) //数字
				return 1;
			if (iN >= 65 && iN <= 90) //大写字母
				return 2;
			if ((iN >= 97 && iN <= 122) || (iN >= 65 && iN <= 90)) //大小写
				return 4;
			else
				return 8; //特殊字符
		}
		//bitTotal函数
		function bitTotal(num) {
			modes = 0;
			for (i = 0; i < 4; i++) {
				if (num & 1) modes++;
				num >>>= 1;
			}
			return modes;
		}
	}
})
