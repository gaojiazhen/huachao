layui.use(['form','layer','table','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	Action();
	var userParam = {};
	var parentCodeList = parent.codeList;
	//2、查数据字典列表
	var codeParam = {
		code:BASE_CODE_NATION  + "," + BASE_CODE_EDUCATION + "," + BASE_CODE_RETIREMENT_RANK + "," + 
			BASE_CODE_NOW_TREATMENT_LEVEL + "," + BASE_CODE_ARCHIVES_AREA + "," + BASE_CODE_RECEIVE_AREA + "," + 
			BASE_CODE_HEALTH_STATUS + "," + BASE_CODE_PLAY_A_ROLE + "," + BASE_CODE_AWARD_LEVEL + "," + BASE_CODE_SPECIAL_CROWD + "," +
			BASE_CODE_APPELLATION +","+BASE_DEPARTMENT +","+BASE_CODE_USER_NATURE+","+BASE_CODE_USER_RANK
	};
    //初始方法
	function Action(){
		//1、查单位列表
		var unitParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', unitParam, function (dres) {
			var unitHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				unitHtml = "<option value=''>全部</option>" + unitHtml;
			}
			$('#unit_id').html(unitHtml);
			form.render('select');
			//2、查人事档案存放地类别
//			var codeParam = {"parent_id":BASE_CODE_ARCHIVES_AREA, "is_available":"1"};
			common.ajax(getCode, 'post', 'json', codeParam , function (codeRes) {
				var editable = "background-color:#5FB878";
				var tableCols = [];
				tableCols[0] = [ 
					{field: 'DEPARTMENT_NAME', title: '企业名称', width:160,minWidth:160, align:'center', rowspan:2},
					{title: '退休人员', minWidth:160, align:'center', colspan:2},
					{field: 'RETIRE_BASIC_PENSION', title: '退休人员基本养老金（元/月）', minWidth:120, align:'center', rowspan:2,style:editable,event:"nine_number", edit:'text'},
					{field: 'BASIC_LOCAL_PENSION', title: '地方基本养老金水平（元/月）', minWidth:120, align:'center', rowspan:2,style:editable,event:"nine_number", edit:'text'},
					{title: '人事档案管理', align:'center', colspan:codeRes.data[BASE_CODE_ARCHIVES_AREA].length + 1} ];
				tableCols[1] = [];
				tableCols[1][0] = {field: 'RETIRE_TOTAL', title:"总数", minWidth:90, align:'center'};
				tableCols[1][1] = {field: 'RETIRE_1998', title:"其中：1997年12月31日以前退休人数", minWidth:150, align:'center'};
				tableCols[1][2] = {field: 'ARCHIVES_AREA_TOTAL', title:"合计", minWidth:90, align:'center'};
				for(var i=0;i<codeRes.data[BASE_CODE_ARCHIVES_AREA].length;i++){
					var code_id = codeRes.data[BASE_CODE_ARCHIVES_AREA][i].value;
					var code_name = codeRes.data[BASE_CODE_ARCHIVES_AREA][i].name;
					tableCols[1][i+3] = {field: 'ARCHIVES_AREA_' + code_id.toUpperCase(), title: code_name, minWidth:90, align:'center'};
				}
				//3、查统计数据
				userParam['unit_id'] = $("#unit_id").val();
				common.ajax(listRetireBasicEndowmentInsurance, 'post', 'json', userParam , function (ures) {
					var tableIns = table.render({
						elem : '#userList',
						data : ures.data,
						height : "full-110",
						id : "userListTable",
						method: 'post',
						page:false,
						limit:ures['data'].length,  //不设置会只显示10条
						cols : tableCols 
					});
					form.render();
				});
			});	
		});
	}
    //搜索
    $(".search_btn").on("click",function(){
		userParam = getformObj($("#formid").serializeArray());
		Action();
    });
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action",excelRetireBasicEndowmentInsurance).trigger("submit");
	});

	//修改
	table.on('edit(userList)', function(obj){
		var inputElem = $(this);
		var tdElem = inputElem.closest('td');
		var valueOld = inputElem.prev().text();
		var data = {};
		if(obj.value!="" && !/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(obj.value)){
			$(tdElem).find("input").val(valueOld);
			$(tdElem).find("div").html(valueOld);
			layer.msg("金额格式不正确", {icon: 2});
			return false;
		}
		//修改值到数据库
 		var CostParam = {};
		CostParam.id = obj.data.ID;
		CostParam.unit_id = obj.data.UNIT_ID;
		var date = new Date;
		CostParam.year = date.getFullYear();
		CostParam[obj.field.toLowerCase()] = obj.value;
		CostParam.modified_user_id = user['id'];
		common.ajax(saveRetireEmployeeCost, 'post', 'json', JSON.stringify(CostParam), function (res) {
			layer.msg(res.message);
		},"application/json;charset=UTF-8");
	});
	
	//监听单元格事件（修改文本框maxLength）
	table.on('tool(userList)', function(obj){
		var data = obj.data;
		var tr = obj.tr;
		if(obj.event === 'nine_number'){
			$(tr).find("input").attr("maxLength","9");
		}
	});
})