layui.use(['form','layer','table','laytpl','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
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
			BASE_CODE_APPELLATION +","
			+BASE_DEPARTMENT +","+BASE_CODE_USER_NATURE+","+BASE_CODE_USER_RANK
	};
    //初始方法
	function Action(){
		//1、查单位下拉框数据
		var unitParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', unitParam, function (dres) {
			var unitHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				unitHtml = "<option value=''>全部</option>" + unitHtml;
			}
			$('#unit_id').html(unitHtml);
			form.render('select');
			//2、发挥作用情况类别
//			var codeParam = {"parent_id":BASE_CODE_PLAY_A_ROLE, "is_available":"1","unequal_special_mark":"0"};
			common.ajax(getCode, 'post', 'json', codeParam , function (codeRes) {
				var tableCols = [];
				tableCols[0] = [{field: 'DEPARTMENT_NAME', title: '项目', width:160,minWidth:160, align:"center", rowspan:3},
						{title: '编号', width:60,minWidth:60, align:"center", templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}, rowspan:3},
						{field: 'TOTAL', title: '总数', width:60,minWidth:60, align:"center", rowspan:3},
						{title: '基本管理情况', align:"center", colspan:5},
						{title: '发挥作用情况', align:"center", colspan:(codeRes.data[BASE_CODE_PLAY_A_ROLE].length+1)}];
				tableCols[1] = [{field: 'SOCIALIZED_PENSION', title: '养老金社会化发放', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'MEDICAL_INSURANCE', title: '参加属地离休干部医疗保险', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'PARTY_TRANSFER_LOCAL', title: '党组织关系移交地方管理', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'ARCHIVES_TRANSFER_LOCAL', title: '人事档案移交地方管理', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'CROSS_DISTRICT', title: '异地管理', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'SUBTOTAL', title: '小计', width:60,minWidth:60, align:"center", rowspan:2} ,
						{title: '其中', align:"center", colspan:codeRes.data[BASE_CODE_PLAY_A_ROLE].length}];
				tableCols[2] = [];
				for(var i=0;i<codeRes.data[BASE_CODE_PLAY_A_ROLE].length;i++){
					var code_id = codeRes.data[BASE_CODE_PLAY_A_ROLE][i].value;
					var code_name = codeRes.data[BASE_CODE_PLAY_A_ROLE][i].name;
					tableCols[2][i] = {field: 'PLAY_A_ROLE_' + code_id.toUpperCase(), title: code_name, width:100,minWidth:100, align:"center"};
				}
				//3、构建动态表头及列表
				userParam['unit_id'] = $("#unit_id").val();
				common.ajax(listRetireWorkerCurrentSituation, 'post', 'json', userParam , function (ures) {
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
				form.render();
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
	    $("#formid").attr("action",excelRetireWorkerCurrentSituation).trigger("submit");
	});
})

