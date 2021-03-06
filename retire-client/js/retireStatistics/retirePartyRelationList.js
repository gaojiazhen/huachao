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
			BASE_CODE_APPELLATION +","+BASE_CODE_PLAY_A_ROLE+","+BASE_CODE_PARTY_LOCATED+","
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
			//2、党组织关系类别
//			var codeParam = {"parent_id":BASE_CODE_PARTY_LOCATED, "is_available":"1"};
			common.ajax(getCode, 'post', 'json', codeParam , function (codeRes) {
				console.log(JSON.stringify(codeRes))
				var tableCols = [];
				tableCols[0] = [{field: 'DEPARTMENT_NAME', title: '企业名称', minWidth:160, align:"center", rowspan:2},
						{title: '党组织关系管理', align:"center", colspan:3},
						{title: '备注', minWidth:160, align:"center",rowspan:2}];
				tableCols[1] = [];
				tableCols[1][0] = {field: 'TOTAL', title: '合计', minWidth:100, align:"center"};
				for(var i=0;i<codeRes.data[BASE_CODE_PARTY_LOCATED].length;i++){
					var code_id = codeRes.data[BASE_CODE_PARTY_LOCATED][i].value;
					var code_name = codeRes.data[BASE_CODE_PARTY_LOCATED][i].name;
					tableCols[1][i+1] = {field: 'LOCATED_' + code_id.toUpperCase(), title: code_name, minWidth:100, align:"center"};
				}
				//3、构建动态表头及列表
				userParam['unit_id'] = $("#unit_id").val();
				common.ajax(listRetirePartyRelation, 'post', 'json', userParam , function (ures) {
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
	    $("#formid").attr("action",excelRetirePartyRelation).trigger("submit");
	});
})