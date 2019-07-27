layui.use(['form','layer','laydate','common'],function(){
	var common = layui.common;
	var form = layui.form,
	layer = parent.layer === undefined ? layui.layer : top.layer,		laydate = layui.laydate,
	    $ = layui.jquery;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	var parentCodeList = parent.codeList;
	//4、出生年月
	laydate.render({
	    elem: '#birth_date',
		theme: 'grid',
	    trigger: 'click',
	    max : 0
	});
	//人员职级
	$("#user_rank_div").hide();
	//1、查单位下拉框
	var unitParam = {
		'parent_id': BASE_DEPARTMENT,
		'department_id': user['unit_id']
	};
	common.ajax(listBaseDepartment, 'post', 'json', unitParam, function(dres) {
		var unitHtml = getDepartmentHtml(dres.data, $('#unit_id').val());
		//多条数据才加“全部”选项，地市级单位只看自己公司的数据
		if (dres.data.length > 1) {
			unitHtml = "<option value=''>请选择单位</option>" + unitHtml;
		}
		$('select[name="unit_id"]').html(unitHtml);
		form.render('select');
		//2、查部门下拉框
		showDepartment($('#unit_id').val());
	});	
	//2、构造部门下拉框
	function showDepartment(unit_id){
		if(unit_id!=null && unit_id!=""){
			var departmentParam = {
				'parent_id': unit_id,
				'department_id': BASE_DEPARTMENT_PROVINCIAL
			};
			common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function(dres) {
				var departmentHtml = getDepartmentHtml(dres.data, $('#department_id').val());
				if (dres.data.length > 1) {
					departmentHtml = "<option value=''>请选择部门</option>" + departmentHtml;
				}
				$('select[name="department_id"]').html(departmentHtml);
				form.render('select');
			});
		}else{
			$('select[name="department_id"]').html("");
		}
	}
	//单位下拉框事件
	form.on('select(unit_id)', function(data) {
		showDepartment(data.value);
		findSortnum(data.value);
	});
	//3、性别
	var codeParam = {
		code:BASE_CODE_SEX+","+BASE_CODE_IS_AVAILABLE_YES
	};
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		var sex = getCodeHtmlByOption(parentCodeList[BASE_CODE_SEX],$("#sex_code").val());
		if(codeRes.data.length > 1){
			sex = "<option value=''>请选择性别</option>" + sex;
		}
		$("select[name='sex_code']").html(sex);
		form.render();
	});
	//4、学历
		var codeParam = {
		code:BASE_CODE_EDUCATION
	};
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		var educationHtml = getCodeHtmlByOption(parentCodeList[BASE_CODE_EDUCATION],$("#education_code").val());
		if(codeRes.data.length > 1){
			educationHtml = "<option value=''>请选择学历</option>" + educationHtml;
		}
		$("select[name='education_code']").html(educationHtml);
		form.render();
	});
	//5、身份证提取出生年月
	$("#idcard").blur(function(){
		var idCard = this.value;
		if(idCard.length == 18){  
			$("#birth_date").val(idCard.substr(6,8).replace(/(.{4})(.{2})/,"$1-$2-"));
	    }else{
			$("#birth_date").val("");
		}
	});
	//6、人员性质
		var codeParam = {
		code:BASE_CODE_USER_NATURE
	};
	var nature_id=$("#user_nature_id").val();
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		var userNatureHtml = getCodeHtmlByOption(parentCodeList[BASE_CODE_USER_NATURE],$("#user_nature_id").val());
		if(codeRes.data.length > 1){
			userNatureHtml = "<option value=''>请选择性质</option>" + userNatureHtml;
		}
		$("select[name='user_nature_id']").html(userNatureHtml);
		//部分人才有人员职级
		 var larr =parentCodeList[BASE_CODE_USER_NATURE]
		for (var i = 0; i < larr.length; i++) {
			if(nature_id == larr[i].value){
				showUserRank(larr[i].special_mark);
			}	
		}
		form.render();
	});
	form.on('select(user_nature_id)', function(data){
		var special_mark = $(data.elem[data.elem.selectedIndex]).attr('special_mark');
		showUserRank(special_mark);
	});
	//显示人员职级

	function showUserRank(special_mark){
		if("1"==special_mark){
			$("#user_rank_div").show();
			$("select[name='user_rank_id']").attr("lay-verify","required");
		}else{
			$("#user_rank_div").hide();
			$("select[name='user_rank_id']").attr("lay-verify","");
		}
	}
	//7、人员职级
	var codeParam = {
		code:BASE_CODE_USER_RANK
	};
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		var userRank = getCodeHtmlByOption(parentCodeList[BASE_CODE_USER_RANK],$("#user_rank_id").val());
		if(codeRes.data.length > 1){
			userRank = "<option value=''>请选择人员职级</option>" + userRank;
		}
		$("select[name='user_rank_id']").html(userRank);
		form.render();
	});
	//8、查排序号
	function findSortnum(unit_id){
		var sortnumParam = {};
		sortnumParam.unit_id = unit_id;
		common.ajax(getRetireContactNextSortnum, 'post', 'json', sortnumParam, function (res){
			$("#sortnum").val(res.data);
		});
	}
	//监听保存
	form.on('submit(saveUser)', function(data){

		//实体类传参必须使用application/json;charset=UTF-8 + JSON.stringify(data.field)
		common.ajax(saveRetireContact, 'post', 'json', JSON.stringify(data.field), function (res){
			layer.msg(res.message);
			if(Number(res.data) > 0){
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭  
				layer.close(index);
			}
		},"application/json;charset=UTF-8");
		return false;
    });
	//关闭
	$("#closeWindow").click(function(){
	    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	    parent.layer.close(index); //再执行关闭  
	    layer.close(index);
	});
	//自定义校验
	form.verify({
		integer: function(value,item){ //value：表单的值、item：表单的DOM对象
			if(value!="" && !/^[+]{0,1}(\d+)$/.test(value)){
				return '请输入正整数';
			}
		},
		identity: function(value,item){
			 var reg=/^(^[1-9]\d{5}[1-9]\d{3}(((0[2])([0|1|2][0-8])|(([0-1][1|4|6|9])([0|1|2][0-9]|[3][0]))|(((0[1|3|5|7|8])|(1[0|2]))(([0|1|2]\d)|3[0-1]))))((\d{4})|\d{3}[Xx])$)$/;

			if(value!="" && !reg.test(value)){
				return '请输入正确的身份证';
			}
		},
		sexRadio: function (value, item) {
			var sex_id = $("input[name='sex_code']:checked").val();
            if (typeof (sex_id) == "undefined") {
                return '性别不能为空';
            }
        }
	});
	//修改时
	var id = $("input[name='id']").val();
	if(id!=null && id!=""){
		

		var user_rank_id = $("#user_nature_id").val();
	}else{
		var unit_id = $("#unit_id").val();
		if(unit_id!=null && unit_id!=""){
			findSortnum(unit_id);
		}
	}
	
	
})