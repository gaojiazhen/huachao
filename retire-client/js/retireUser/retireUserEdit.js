layui.use(['form', 'layer', 'laydate', 'common', 'formSelects'],function(){
	var common = layui.common;
	var form = layui.form,
	    layer = parent.layer === undefined ? layui.layer : top.layer,
		laydate = layui.laydate,
	    $ = layui.jquery;
		formSelects = layui.formSelects;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	//修改时根据原先选择内容判断元素显示
	var id = $("input[name='id']").val();
	var retireFamilyList = [];
	if(id!=null && id!=""){
		//查离退休人员信息、中共党员登记信息
		var userParam = {
			"user_id":id
		};
		common.ajax(getRetireCommunistByUserid, 'post', 'json', userParam, function (userRes){
			//1、离退休人员信息
			var retireUserDO = userRes.data.retireUserDO;
			retireFamilyList = userRes.data.retireFamilyList;
		});
	}
	$("#saveUserEditQuitRetire").hide();
	$("#saveUserEditCommunist").hide();
	shwoSystemEmployees($("#is_system_employees").val());
	shwoModelPersonnel($("#is_model_personnel").val());
	shwoSoldierCadre($("#is_soldier_cadre").val());
	showToReceive($("#archives_area_id").val());
	//1、查单位列表
	var departmentParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
	common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
		var departmentHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
		//多条数据才加“全部”选项，地市级单位只看自己公司的数据
		if(dres.data.length > 1){
			departmentHtml = "<option value=''>请选择单位</option>" + departmentHtml;
		}
		$("select[name='unit_id']").html(departmentHtml);
		form.render('select');
	});

    //配偶退休单位选择
    departmentParam.department_id = '101';
    common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
        var  spouseHtml = getDepartmentHtml(dres.data,$('#spouse_unit').val());
        //多条数据才加“全部”选项，地市级单位只看自己公司的数据
        if(dres.data.length > 1){
            spouseHtml = "<option value=''>请选择配偶退休单位</option>" + spouseHtml;
        }
        $("select[name='spouse_unit']").html(spouseHtml);
        form.render('select');
    });

	//列表页已经查询过的数据字典
	var parentCodeList = parent.codeList;
	//称谓列表
	var appellationData;
	//2、查数据字典列表
	var codeParam = {
		code:BASE_CODE_NATION  + "," + BASE_CODE_TYPE1 + "," + BASE_CODE_RETIREMENT_RANK + "," +
			BASE_CODE_NOW_TREATMENT_LEVEL + "," + BASE_CODE_ARCHIVES_AREA + "," + BASE_CODE_RECEIVE_AREA + "," +
			BASE_CODE_HEALTH_STATUS + "," + BASE_CODE_PLAY_A_ROLE + "," + BASE_CODE_AWARD_LEVEL + "," + BASE_CODE_SPECIAL_CROWD + "," +
			BASE_CODE_APPELLATION+","+BASE_DEPARTMENT
	};
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		//2.1、性别单选项
		$("#sexDiv").html(getDepartmentHtmlByRadio(parentCodeList[BASE_CODE_SEX],"sex_id","sexRadio"));
		//2.2、民族下拉框
		var nationHtml = getCodeHtmlByOption(codeRes.data[BASE_CODE_NATION],$("#nation_id").val());
		if(codeRes.data[BASE_CODE_NATION].length > 1){
			nationHtml = "<option value=''>请选择民族</option>" + nationHtml;
		}
		$("select[name='nation_id']").html(nationHtml);


		//2.3、婚姻状况下拉框
		var maritalStatusHtml = getCodeHtmlByOption(parentCodeList[BASE_CODE_MARITAL_STATUS],$("#marital_status_id").val());
		if(parentCodeList[BASE_CODE_MARITAL_STATUS].length > 1){
			maritalStatusHtml = "<option value=''>请选择婚姻状况</option>" + maritalStatusHtml;
		}
		$("select[name='marital_status_id']").html(maritalStatusHtml);
		//2.4、最高学历
		var educationHtml = getCodeHtmlByOption(parentCodeList[BASE_CODE_EDUCATION],$("#education_id").val());
		if(parentCodeList[BASE_CODE_EDUCATION].length > 1){
			educationHtml = "<option value=''>请选择最高学历</option>" + educationHtml;
		}
		$("select[name='education_id']").html(educationHtml);
		//2.5、政治面貌
		var politics_status_id = $("#politics_status_id").val();
		var politics_status_special_mark = $("#politics_status_special_mark").val();
		var politicsStatusHtml = getCodeHtmlByOption(parentCodeList[BASE_CODE_POLITICS_STATUS],politics_status_id);
		if(parentCodeList[BASE_CODE_POLITICS_STATUS].length > 1){
			politicsStatusHtml = "<option value=''>请选择政治面貌</option>" + politicsStatusHtml;
		}
		$("select[name='politics_status_id']").html(politicsStatusHtml);
		//根据ID查特殊标识
		for(var i=0;i<parentCodeList[BASE_CODE_POLITICS_STATUS].length;i++){
			if(politics_status_id == parentCodeList[BASE_CODE_POLITICS_STATUS][i].value){
				politics_status_special_mark = parentCodeList[BASE_CODE_POLITICS_STATUS][i].special_mark;
				$("#politics_status_special_mark").val(politics_status_special_mark);
			}
		}
		showEditCommunist(politics_status_special_mark);
		//2.6、离退休类型
		$("#retireTypeDiv").html(getDepartmentHtmlByRadio(codeRes.data[BASE_CODE_TYPE1],"retire_type_id","typeId1Radio"));

		var retire_type_id = $("#retire_type_id").val();
		var retire_type_id_special_mark = $("#retire_type_id_special_mark").val();
		//根据ID查特殊标识
		for(var i=0;i<codeRes.data[BASE_CODE_TYPE1].length;i++){
			if(retire_type_id == codeRes.data[BASE_CODE_TYPE1][i].value){
				retire_type_id_special_mark = codeRes.data[BASE_CODE_TYPE1][i].special_mark;
				$("#retire_type_id_special_mark").val(retire_type_id_special_mark);
			}
		}
		showRetireCadres(retire_type_id_special_mark,retire_type_id);
		//2.7、离退休职级
		var retirementRankHtml = getCodeHtmlByOption(codeRes.data[BASE_CODE_RETIREMENT_RANK],$("#retirement_rank_id").val());
		if(codeRes.data[BASE_CODE_RETIREMENT_RANK].length > 1){
			retirementRankHtml = "<option value=''>请选择离退休职级</option>" + retirementRankHtml;
		}
		$("select[name='retirement_rank_id']").html(retirementRankHtml);
		//2.8、现享受待遇级别
		var nowTreatmentHtml = getCodeHtmlByOption(codeRes.data[BASE_CODE_NOW_TREATMENT_LEVEL],$("#now_treatment_level_id").val());
		if(codeRes.data[BASE_CODE_NOW_TREATMENT_LEVEL].length > 1){
			nowTreatmentHtml = "<option value=''>请选择现享受待遇级别 </option>" + nowTreatmentHtml;
		}
		$("select[name='now_treatment_level_id']").html(nowTreatmentHtml);
		//2.9、人事档案存放地
		var archivesAreaHtml = getDepartmentHtmlByRadio(codeRes.data[BASE_CODE_ARCHIVES_AREA],"archives_area_id","recordIdRadio");
		$("#archives_area_div").html(archivesAreaHtml);
		//2.10接收地
		var receiveAreaHtml = getDepartmentHtmlByRadio(codeRes.data[BASE_CODE_RECEIVE_AREA],"receive_area_id","");
		$("#receive_area_div").html(receiveAreaHtml);
		//2.11健康状况
		var health_status = $("#health_status").val();
		var health_status_special_mark = $("#health_status_special_mark").val();
		var healthStatusHtml = getCodeHtmlByOption(codeRes.data[BASE_CODE_HEALTH_STATUS],health_status);
		if(codeRes.data[BASE_CODE_RECEIVE_AREA].length > 1){
			healthStatusHtml = "<option value=''>请选择健康状况</option>" + healthStatusHtml;
		}
		$("select[name='health_status']").html(healthStatusHtml);
		//根据ID查特殊标识
		for(var i=0;i<codeRes.data[BASE_CODE_HEALTH_STATUS].length;i++){
			if(health_status == codeRes.data[BASE_CODE_HEALTH_STATUS][i].value){
				health_status_special_mark = codeRes.data[BASE_CODE_HEALTH_STATUS][i].special_mark;
				$("#health_status_special_mark").val(health_status_special_mark);
			}
		}
		showHealthStatusDetails(health_status_special_mark);
		//2.12发挥作用情况
		var rdata = codeRes.data[BASE_CODE_PLAY_A_ROLE];
		var dataArr = [];
		if(rdata!=null && rdata!=""){
			for (var i = 0; i < rdata.length; i++) {
				var baseCode = rdata[i];
				dataArr[i] = {"name":rdata[i].name,"value": rdata[i].value,"special_mark":rdata[i].special_mark}
			}
		}
		var data = new Object();
		data['arr'] = dataArr;
		formSelects.data('play_a_role_ids', 'local', data);
		formSelects.on('play_a_role_ids', function(id, vals, val, isAdd, isDisabled) {
			var play_a_role_ids_special_mark = "0";
			if(vals!=null){
				for(var i=0;i<vals.length;i++){
					if(vals[i].special_mark=='1'){
						play_a_role_ids_special_mark = '1';
					}
				}
			}
			showPlayARoleSpecific(play_a_role_ids_special_mark);
		}, true);
		//赋初值
		var play_a_role_ids = $("#play_a_role_ids").val();
		if(play_a_role_ids!=null && play_a_role_ids!=""){
			formSelects.value('play_a_role_ids', play_a_role_ids.split(','));
		}
		//根据ID查特殊标识
		var roleidArr = play_a_role_ids.split(',');
		var play_a_role_ids_special_mark = "";
		for(var i=0;i<codeRes.data[BASE_CODE_PLAY_A_ROLE].length;i++){
			for(var j=0;j<roleidArr.length;j++){
				if(roleidArr[j] == codeRes.data[BASE_CODE_PLAY_A_ROLE][i].value){
					play_a_role_ids_special_mark = codeRes.data[BASE_CODE_PLAY_A_ROLE][i].special_mark + ",";
				}
			}
		}
		if(null!=play_a_role_ids_special_mark && ""!=play_a_role_ids_special_mark){
			play_a_role_ids_special_mark = play_a_role_ids_special_mark.substring(0,play_a_role_ids_special_mark.length-1);
		}
		$("#play_a_role_ids_special_mark").val(play_a_role_ids_special_mark);
		showPlayARoleSpecific(play_a_role_ids_special_mark);
		//2.13奖项级别 
		var awardLevelHtml = getCodeHtmlByOption(codeRes.data[BASE_CODE_AWARD_LEVEL],$("#award_level_id").val());
		if(codeRes.data[BASE_CODE_AWARD_LEVEL].length > 1){
			awardLevelHtml = "<option value=''>请选择奖项级别</option>" + awardLevelHtml;
		}
		$("select[name='award_level_id']").html(awardLevelHtml);

		//2.14特殊人员标识
		var rdata = codeRes.data[BASE_CODE_SPECIAL_CROWD];
		var dataArr = [];
		if(rdata!=null && rdata!=""){
			for (var i = 0; i < rdata.length; i++) {
				var baseCode = rdata[i];
				dataArr[i] = {"name":rdata[i].name,"value": rdata[i].value,"special_mark":rdata[i].special_mark}
			}
		}
		var data = new Object();
		data['arr'] = dataArr;
		formSelects.data('special_crowd_ids', 'local', data);
		//赋初值
		var special_crowd_ids = $("#special_crowd_ids").val();
		if(special_crowd_ids!=null && special_crowd_ids!=""){
			formSelects.value('special_crowd_ids', special_crowd_ids.split(','));
		}
		//2.15称谓
		appellationData = codeRes.data[BASE_CODE_APPELLATION];
		if(retireFamilyList[0]){
			for(var i=0;i<retireFamilyList.length;i++){
				addFamily(retireFamilyList[i],appellationData);
			}
		}else{
			$("#familyTbody").append("<tr align='center' id='noDataMessage'><td style='color:red;' colspan='7'>没有数据</td></tr>");
		}
		form.render();
	});
	//政治面貌
	form.on('select(politics_status_id)', function(data){
		var special_mark = $(data.elem[data.elem.selectedIndex]).attr('special_mark');
		showEditCommunist(special_mark);
	});
	//离退休类型
	form.on('radio(retire_type_id)', function(data){
		var specialSign = data.elem.getAttribute("special_mark");
		showRetireCadres(specialSign,data.value);
	}); 
	//健康状况
	form.on('select(health_status)', function(data){
		var specialMark = $(data.elem[data.elem.selectedIndex]).attr('special_mark');
		showHealthStatusDetails(specialMark);
	});
    //配偶退休单位
    form.on('select(spouse_unit)', function(data){
    	$("#spouse_unit").val(data.value);
    });
	
	//3、出生年月
	laydate.render({
	    elem: '#birth_date',
		theme: 'grid',
	    trigger: 'click',
	    max : 0
	});
	//7、身份证提取出生年月
	 $("#idcard").blur(function(){
		var idCard = this.value;
		if(idCard.length == 18){  
			$("#birth_date").val(idCard.substr(6,8).replace(/(.{4})(.{2})/,"$1-$2-"));
        }else{
			$("#birth_date").val("");
		}
	});
	//8、参加工作时间
	laydate.render({
	    elem: '#work_time',
		theme: 'grid',
	    trigger: 'click',
	    max : 0
	});
	//9、离（退）休时间
	laydate.render({
	    elem: '#retire_time',
		theme: 'grid',
	    trigger: 'click',
	    max : 0
	});
	//14、待遇批准时间
	laydate.render({
	    elem: '#treatment_approval_time',
		theme: 'grid',
	    trigger: 'click',
	    max : 0
	});
	//18、已故时间
	laydate.render({
	    elem: '#deceased_time',
		theme: 'grid',
	    trigger: 'click',
	    max : 0
	});
	//21、是否电力系统双职工
	form.on('radio(is_system_employees)', function(data){
		shwoSystemEmployees(data.value);
	});	
	//22、是否先模人员
	form.on('radio(is_model_personnel)', function(data){
		shwoModelPersonnel(data.value);
	});
	//23、是否军转干部
	form.on('radio(is_soldier_cadre)', function(data){
		shwoSoldierCadre(data.value);
	});
	// 是否为地方
    form.on('radio(archives_area_id)',function (data) {
        showToReceive(data.value);
    })
	//26、新增家庭主要成员
	$("#addFamily").click(function(){
		addFamily(null);
	});
	function addFamily(obj){
		if($("#noDataMessage")[0]) {
			$("#familyList tr:eq(1)").remove();
		}
		//修改时赋值
		var appellation_id = "";
		var user_name = "";
		var age = "";
		var work_unit_post = "";
		var contact = "";
		var remark = "";
		if(obj!=null){
			appellation_id = obj.appellation_id || "";
			user_name = obj.user_name || "";
			age = obj.age || "";
			work_unit_post = obj.work_unit_post || "";
			contact = obj.contact || "";
			remark = obj.remark || "";
		}
		var appellationHtml = getCodeHtmlByOption(appellationData,appellation_id);
		if(appellationData.length > 1){
			appellationHtml = "<option value=''>请选择</option>" + appellationHtml;
		}
		var cell_1 = '<td><select name="appellation_id" lay-verify="required" >' + appellationHtml + '</select></td>';
		var cell_2 = '<td><input type="text" lay-verify="required" name="family_user_name" value="' + user_name + '"' + 
			'class="layui-input" placeholder="请输入姓名" maxlength="10" autocomplete="off"></td>';
		var cell_3 = '<td><input type="text" lay-verify="integer" name="family_age" value="' + age + '"' + 
			'class="layui-input" placeholder="请输入年龄" maxlength="3" autocomplete="off"></td>';	
		var cell_4 = '<td><input type="text" name="family_work_unit_post" value="' + work_unit_post + '"' + 
			'class="layui-input" placeholder="请输入工作单位及职务" maxlength="50" autocomplete="off"></td>';
		var cell_5 = '<td><input type="text" lay-verify="required" name="contact" value="' + contact + '"' + 
			'class="layui-input" placeholder="请输入联系方式" maxlength="50" autocomplete="off"></td>';
		var cell_6 = '<td><input type="text" name="remark" value="' + remark + '"' + 
			'class="layui-input" placeholder="请输入备注" maxlength="200" autocomplete="off"></td>';
		var opcell = '<td><a class="layui-btn layui-btn-xs layui-btn-danger" id="delFamily">移除</a></td>';
		$("#familyTbody").append('<tr class="familyTr">' + cell_1 + cell_2 + cell_3 + cell_4 + cell_5 + cell_6 + opcell + '</tr>');
		form.render();
	}
	//移除家庭主要成员
	$("#familyList").on('click','#delFamily',function(){
		var obj = this;
		layer.confirm("确定移除家庭成员信息？", {
			icon: 3,
			title: '提示信息'
		}, function(index) {
			$(obj).parent().parent().remove();
			if($("#familyList tr").length == 1){
				$("#familyTbody").append("<tr align='center' id='noDataMessage'><td style='color:red;' colspan='6'>没有数据</td></tr>");
			}
			layer.close(index);
		})
	});

	//显示【保存并中共党员登记】按钮
	function showEditCommunist(special_mark){
		if("1"==special_mark){
			$("#saveUserEditCommunist").show();
		}else{
			$("#saveUserEditCommunist").hide();
		}
	}
	
	//监听【保存】
	form.on('submit(saveUser)', function(data){
		//获取家庭主要成员
		var appellation_id_arr = [];
		var user_name_arr = [];
		var age_arr = [];
		var work_unit_post_arr = [];
		var contact_arr = [];
		var remark_arr = [];
		$("#familyTbody select[name='appellation_id']").each(function(index) { // 遍历name=standard的多选框
			appellation_id_arr.push($(this).val());
			user_name_arr.push($("#familyTbody input[name='family_user_name']")[index].value);
			age_arr.push($("#familyTbody input[name='family_age']")[index].value);
			work_unit_post_arr.push($("#familyTbody input[name='family_work_unit_post']")[index].value);
			contact_arr.push($("#familyTbody input[name='contact']")[index].value);
			remark_arr.push($("#familyTbody input[name='remark']")[index].value);
		});
		data.field.appellation_id_arr = appellation_id_arr;
		data.field.user_name_arr = user_name_arr;
		data.field.age_arr = age_arr;
		data.field.work_unit_post_arr = work_unit_post_arr;
		data.field.contact_arr = contact_arr;
		data.field.remark_arr = remark_arr;
		//实体类传参必须使用application/json;charset=UTF-8 + JSON.stringify(data.field)
		common.ajax(saveRetireUser, 'post', 'json', JSON.stringify(data.field), function (res){
			layer.msg(res.message);
			if(Number(res.data) > 0){
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭  
				layer.close(index);
			}
		},"application/json;charset=UTF-8");
		return false;
    });
	//监听【保存并填写离休信息】
	form.on('submit(saveUserEditQuitRetire)', function(data){
		//实体类传参必须使用application/json;charset=UTF-8 + JSON.stringify(data.field)
		common.ajax(saveRetireUser, 'post', 'json', JSON.stringify(data.field), function (res){
			layer.msg(res.message);
			if(Number(res.data) > 0){
				window.location.href = "../retireQuit/quitUserEdit.html?user_id=" + res.data;
			}
		},"application/json;charset=UTF-8");
		return false;
	});
	//监听【保存并中共党员登记】
	form.on('submit(saveUserEditCommunist)', function(data){
		//实体类传参必须使用application/json;charset=UTF-8 + JSON.stringify(data.field)
		common.ajax(saveRetireUser, 'post', 'json', JSON.stringify(data.field), function (res){
			layer.msg(res.message);
			if(Number(res.data) > 0){
				window.location.href = "../retireCommunist/retireCommunistEdit.html?user_id=" + res.data;
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
		retireNature: function (value, item) {
			var sex_id = $("input[name='retire_nature_id']:checked").val();
            if (typeof (sex_id) == "undefined") {
                return '退休性质不能为空';
            }
        },
		sexRadio: function (value, item) {
            var sex_id = $("input[name='sex_id']:checked").val();
            if (typeof (sex_id) == "undefined") {
                return '性别不能为空';
            }
        },
        recordIdRadio: function (value, item) {
            var archives_area_id = $("input[name='archives_area_id']:checked").val();
            if (typeof (archives_area_id) == "undefined") {
                return '人事档案存放地不能为空';
            }
        },
		typeId1Radio: function (value, item) {
			var retire_type_id = $("input[name='retire_type_id']:checked").val();
		    if (typeof (retire_type_id) == "undefined") {
		        return '离退休类型不能为空';
		    }
		},
		money: function(value, item){ //value：表单的值、item：表单的DOM对象
			if(/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value) || value==''){
					
			}else{
				return '金额格式不正确';
			}
		},
		integer: function(value, item){ //value：表单的值、item：表单的DOM对象
			if(value!="" && !/^[+]{0,1}(\d+)$/.test(value)){
				return '请输入正整数';
			}else{
				
			}
		}
	});

	//显示离休干部填写项目
	function showRetireCadres(special_mark,retire_type_id){
		//离休
		if(special_mark=='1'){ 
			$("#saveUserEditQuitRetire").show();
		}else{
			$("#saveUserEditQuitRetire").hide();
		}
		if(retire_type_id!=null && retire_type_id!=""){
			var natureData = new Array();
			var datalength = 0;
			var natureList = parentCodeList[BASE_CODE_NATURE];
			for(var i=0;i<natureList.length;i++){
				if(retire_type_id==natureList[i].special_mark){
					natureData[datalength] = natureList[i];
					datalength++;
				}
			}
			$("#retireNatureDiv").html(getDepartmentHtmlByRadio(natureData,"retire_nature_id","retireNature"));
			form.render();
		}
	}
	//显示健康状况详情_填写项目
	function showHealthStatusDetails(specialMark){
		if(specialMark=='1'){
			$("#deceased_time_div").hide();
			$("#deceased_time_div").find("input").attr("lay-verify","").val("");
			$("#health_status_details_div").show();
			$("#health_status_details_div").find("input").attr("lay-verify","required");
		}else if(specialMark=='9'){
			$("#deceased_time_div").show();
			$("#deceased_time_div").find("input").attr("lay-verify","required");
			$("#health_status_details_div").hide();
			$("#health_status_details_div").find("input").attr("lay-verify","").val("");
		}else{
			$("#deceased_time_div").hide();
			$("#deceased_time_div").find("input").attr("lay-verify","").val("");
			$("#health_status_details_div").hide();
			$("#health_status_details_div").find("input").attr("lay-verify","").val("");
		}
	}
	//显示发挥作用具体情况
	function showPlayARoleSpecific(special_mark){
		if(special_mark=='1'){
			$("#play_a_role_specific_div").show();
			$("#play_a_role_specific_div").find("input").attr("lay-verify","required"); 
		}else{
			$("#play_a_role_specific_div").hide();
			$("#play_a_role_specific_div").find("input").attr("lay-verify","").val(""); 
		}
	}
	//显示系统双职工配偶信息
	function shwoSystemEmployees(value){
		if(value=='1'){
			$(".spouse_info").show();
			$(".spouse_info").find("select").attr("lay-verify","required");
			$(".spouse_info").find("input").attr("lay-verify","required");
		}else{
			$(".spouse_info").hide();
            $(".spouse_info").find("select").attr("lay-verify","");
			$(".spouse_info").find("input").attr("lay-verify",""); 
		}
	}
	//显示先模人员荣誉信息
	function shwoModelPersonnel(value){
		if(value=='1'){
			$(".honor_info").show();
			$(".honor_info").find("input").attr("lay-verify","required"); 
			$(".honor_info").find("select").attr("lay-verify","required");
		}else{
			$(".honor_info").hide();
			$(".honor_info").find("input").attr("lay-verify",""); 
			$(".honor_info").find("select").attr("lay-verify",""); 
		}
	}
	//显示是否军转干部信息
	function shwoSoldierCadre(value){
		if(value=='1'){
			$(".soldier_cadre").show();
			$(".soldier_cadre").find("input").attr("lay-verify","required");
		}else{
			$(".soldier_cadre").hide();
			$(".soldier_cadre").find("input").attr("lay-verify","");
		}
	}

	//当为地方的时候显示接收地
    function showToReceive(value) {
        if (value == "code_11202"){
            $(".companies_place").show();
            $(".companies_place").find("input").attr("lay-verify","required");
        }else{
            $(".companies_place").hide();
            $(".companies_place").find("input").attr("lay-verify","");
        }
    }
});