var codeList;
layui.use(['form','layer','laydate','table','common'],function(){
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	Action();
	var userParam = {};
	var parentCodeList = parent.codeList;
	var codeParam = {code:BASE_CODE_SEX + "," + BASE_CODE_MARITAL_STATUS + "," + BASE_CODE_EDUCATION + "," +
	BASE_CODE_POLITICS_STATUS + "," + BASE_CODE_NATURE+","+BASE_CODE_ADMINISTRATIVE_DIVISION+","+BASE_CODE_ORGANIZATION_TYPE};

    //初始方法
	function Action(){
		//1、单位列表
		var departmentParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
		
			var departmentHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				departmentHtml = "<option value=''>全部</option>" + departmentHtml;
			}
			$('#unit_id').html(departmentHtml);
			form.render('select');
			//2、查询行政区划类型
//			var codeParam = {"parent_id":BASE_CODE_ADMINISTRATIVE_DIVISION, "is_available":"1"};
			common.ajax(getCode, 'post', 'json', codeParam , function (divisionRes) {
				console.log("divisionRes  :"+JSON.stringify(divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION]));
				var tableCols = [];
				//3、查询机构类型
//				var codeParam = {"parent_id":BASE_CODE_ORGANIZATION_TYPE, "is_available":"1"};
				common.ajax(getCode, 'post', 'json', codeParam , function (typeRes) {
					console.log("typeRes  :"+JSON.stringify(typeRes.data[BASE_CODE_ORGANIZATION_TYPE]));
					tableCols[0] = [];
					tableCols[1] = [];
					tableCols[2] = [];
					var divisionCol = 0;
					var independentCol = 0;
					for(var i=0;i<divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION].length;i++){
						if("1"==divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION][i].special_mark){
							independentCol+=1;
							tableCols[1][i] = {field: 'DEP_TYPE_' + divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION][i].value.toUpperCase(), title: divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION][i].name, 
								width:60, minWidth:60, align:"center", rowspan:2};
						}else{
							tableCols[1][i] = {title: divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION][i].name, width:60, minWidth:60, align:"center", colspan:typeRes.data[BASE_CODE_ORGANIZATION_TYPE].length};
							for(var j=0;j<typeRes.data[BASE_CODE_ORGANIZATION_TYPE].length;j++){
								tableCols[2][(divisionCol*typeRes.data[BASE_CODE_ORGANIZATION_TYPE].length)+j] = {field: 'DEP_TYPE_' + divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION][i].value.toUpperCase() + "_" +
								typeRes.data[BASE_CODE_ORGANIZATION_TYPE][j].value.toUpperCase(),
									title: typeRes.data[BASE_CODE_ORGANIZATION_TYPE][j].name, align:"center"};
							}
							divisionCol+=1;
						}
					}
					tableCols[1][divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION].length] = {field: 'TRAIN_COUNT', title: '次数', width:60, minWidth:60, align:"left", rowspan:2};
					tableCols[1][divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION].length+1] = {field: 'TRAIN_MAN_TIME', title: '人次', width:60, minWidth:60, align:"left", rowspan:2};
					tableCols[1][divisionRes.data[BASE_CODE_ADMINISTRATIVE_DIVISION].length+2] = {field: 'TRAIN_OTHER', title: '参加其他<br>培训人次', align:"left", rowspan:2};
					//第一行，因为要计算长度放在这
					tableCols[0] = [
						{field: 'DEPARTMENT_NAME', title: '项目', minWidth:160,align:"left", rowspan:3},
						{title: '编号', width:60, minWidth:60, align:"center", rowspan:3, templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}},
						{title: '离退休工作机构', align:"center", colspan:((typeRes.data[BASE_CODE_ORGANIZATION_TYPE].length*divisionCol)+independentCol)},
						{title: '培训情况', align:"center", colspan:3}
					];
					//加载列表
					userParam['unit_id'] = $("#unit_id").val();
					common.ajax(listRetireDepartmentAndCost, 'post', 'json', userParam, function (ures) {
						console.log("ures  :"+JSON.stringify(ures));

						var editable = "background-color:#5FB878";
						var tableIns = table.render({
							elem : '#organizationList',
							data : ures.data,
							cellMinWidth : 60,
							height : "full-110",
							id : "organizationListTable",
							method: 'post',
							page: false,
							limit: ures['data'].length,  //不设置会只显示10条
							cols : tableCols
						});
						form.render();
					});
				});
			});
		});
		form.render();
	}
    //搜索
	form.on('submit(search_btn)', function(data){
		userParam = getformObj($("#formid").serializeArray());
		Action();
		return false;
    });
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action", excelRetireDepartmentAndCost).trigger("submit");
	});
})