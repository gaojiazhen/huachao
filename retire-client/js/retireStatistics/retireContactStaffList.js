var codeList;
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
			BASE_CODE_APPELLATION 
	};
    //初始方法
	function Action(){
		//1、查单位下拉框数据
		var unitParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', unitParam, function (dres) {
			console.log(JSON.stringify(dres))
			var unitHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				unitHtml = "<option value=''>全部</option>" + unitHtml;
			}
			$('#unit_id').html(unitHtml);
			form.render('select');
			//2、学历类别
			common.ajax(getCode, 'post', 'json', codeParam , function (codeRes) {
				console.log("codeRes:  "+JSON.stringify(codeRes));
				var tableCols = [];
				tableCols[0] = [{field: 'CODE_VAL', title: '项目', width:160,minWidth:160, align:"center", rowspan:2},
						{title: '编号', width:60,minWidth:60, align:"center", rowspan:2, templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}},
						{field: 'TOTAL', title: '总数', width:60,minWidth:60, align:"center", rowspan:2},
						{title: '学历', align:"center", colspan:codeRes.data[BASE_CODE_EDUCATION].length},
						{title: '年龄', align:"center", colspan:3},
						{title: '从事离退休工作年限', align:"center", colspan:3}];
				tableCols[1] = [];
				for(var i=0;i<codeRes.data[BASE_CODE_EDUCATION].length;i++){					
					var code_code = codeRes.data[BASE_CODE_EDUCATION][i].value.toUpperCase();
					var code_val = codeRes.data[BASE_CODE_EDUCATION][i].name;
					tableCols[1][i] = {field: 'EDUCATION_' + code_code, title: code_val, width:100,minWidth:100, align:"center"};
				}
				tableCols[1][codeRes.data[BASE_CODE_EDUCATION].length] = {field: 'AGE_40', title: '40岁及以下', width:110,minWidth:110, align:"center"};
				tableCols[1][codeRes.data[BASE_CODE_EDUCATION].length+1] = {field: 'AGE_41_50', title: '41岁至50岁', width:110,minWidth:110, align:"center"};
				tableCols[1][codeRes.data[BASE_CODE_EDUCATION].length+2] = {field: 'AGE_51_60', title: '51岁至60岁', width:110,minWidth:110, align:"center"};
				tableCols[1][codeRes.data[BASE_CODE_EDUCATION].length+3] = {field: 'WORK_SENIORITY_5', title: '5年<br>及以下', width:90,minWidth:90, align:"center"};
				tableCols[1][codeRes.data[BASE_CODE_EDUCATION].length+4] = {field: 'WORK_SENIORITY_6_10', title: '6年至<br>不满10年', width:90,minWidth:90, align:"center"};
				tableCols[1][codeRes.data[BASE_CODE_EDUCATION].length+5] = {field: 'WORK_SENIORITY_10', title: '10年及以上', width:105,minWidth:105, align:"center"};
				//3、构建动态表头及列表
				userParam['unit_id'] = $("#unit_id").val();
				common.ajax(listRetireContactStaff, 'post', 'json', userParam , function (ures) {
					console.log("ures:  "+JSON.stringify(ures));
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
	    $("#formid").attr("action",excelRetireContactStaff).trigger("submit");
	});
})