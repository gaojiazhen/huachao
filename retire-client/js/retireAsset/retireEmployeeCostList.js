layui.use(['form','layer','laydate','table','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	//选择统计年度
	laydate.render({
	    elem: '#year',
		theme: 'grid',
		type: 'year',
	    max : 0,
		value: new Date(),
		format: 'yyyy年',
		// 点击即选中
        ready:function(date){
            $("#layui-laydate1").off('click').on('click','.laydate-year-list li',function(){
                $("#layui-laydate1").remove();
            });
        },
		change: function(value){
			$('#year').val(value);
		}
	});
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
		var departmentParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
			console.log("dres :"+JSON.stringify(dres));

			var departmentHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				departmentHtml = "<option value=''>全部</option>" + departmentHtml;
			}
			$('#unit_id').html(departmentHtml);
			form.render('select');
			//1、查人员性质
//			var codeParam = {"parent_id":BASE_CODE_USER_NATURE, "is_available":"1"};
			common.ajax(getCode, 'post', 'json', codeParam , function (natureRes) {
				console.log("natureRes :"+JSON.stringify(natureRes.data[BASE_CODE_USER_NATURE]));
				//2、查人员职级
//				codeParam.parent_id = BASE_CODE_USER_RANK;
				common.ajax(getCode, 'post', 'json', codeParam , function (rankRes) {
					console.log("rankRes :"+JSON.stringify(rankRes.data[BASE_CODE_USER_RANK]));
					var editable = "background-color:#5FB878";
					//拼接表格列属性
					var tableCols = [];
					tableCols[0] = [{field: 'DEPARTMENT_NAME', title: '企业名称', width:160,minWidth:160, align:"center", rowspan:3},
						{title: '企业退休人员管理服务从业人员', align:"center", colspan:(natureRes.data[BASE_CODE_USER_NATURE].length + rankRes.data[BASE_CODE_USER_RANK].length)},
						{title: '运行成本费用（不含给退休人员个人的各种货币和实物类支出）', align:"center", colspan:15}];
					tableCols[1] = [];
					tableCols[2] = [];
					for(var i=0;i<natureRes.data[BASE_CODE_USER_NATURE].length;i++){
						console.log("12123"+JSON.stringify(natureRes.data[BASE_CODE_USER_NATURE]));
						var code_id = natureRes.data[BASE_CODE_USER_NATURE][i].value;
						var code_name = natureRes.data[BASE_CODE_USER_NATURE][i].name;
						if("专职"==code_name){
							tableCols[1][i] = {field: 'NATURE_' + code_id.toUpperCase(), title: code_name + "（人）", width:100, minWidth:100, 
								align:"center", colspan:rankRes.data[BASE_CODE_USER_RANK].length+1};
							tableCols[2][0] = {field: 'NATURE_' + code_id.toUpperCase(), title: "小计", width:100, minWidth:100, align:"center"};
							for(var j=0;j<rankRes.data[BASE_CODE_USER_RANK].length;j++){
								var rank_id = rankRes.data[BASE_CODE_USER_RANK][j].value;
								var rank_name = rankRes.data[BASE_CODE_USER_RANK][j].name;
								tableCols[2][j+1] = {field: 'RANK_' + code_id.toUpperCase() + "_" + rank_id.toUpperCase(), title: rank_name, width:100, minWidth:100, align:"center"};
							}
						}else{
							tableCols[1][i] = {field: 'NATURE_' + code_id.toUpperCase(), title: code_name + "（人）", width:100, minWidth:100, align:"center",rowspan:2};
						}
					}
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length] = {field: 'TOTAL', title: "合计", width:100, minWidth:100, align:"center", rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+1] = {field: 'UTILITIES', title: "水电气<br>暖费用", width:100, minWidth:100, align:"center", 
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+2] = {field: 'EMOLUMENT', title: "薪酬及<br>附加", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+3] = {field: 'DEPRECIATION', title: "折旧及<br>摊销", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+4] = {field: 'CHUMMAGE', title: "房 租", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+5] = {field: 'OFFICE_ALLOWANCE', title: "办公费", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+6] = {field: 'TRAVEL_EXPENSE', title: "交通费<br>（差旅费）", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+7] = {field: 'CONVENTION_EXPENSE', title: "会议费", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+8] = {field: 'PUBLICITY_EXPENSE', title: "宣传费", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+9] = {title: "培训情况", align:"center", colspan:4};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+10] = {field: 'OTHER', title: "其他", width:100, minWidth:100, align:"center",  
						style:editable,event:"nine_number", edit:'text', rowspan:2};
					tableCols[1][natureRes.data[BASE_CODE_USER_NATURE].length+11] = {field: 'REMARK', title: "备注", width:100, minWidth:100, align:"center", 
						style:editable,event:"one_hundred_varchar", edit:'text',rowspan:2};
						
					tableCols[2][rankRes.data[BASE_CODE_USER_RANK].length+1] = {field: 'TRAIN_EXPENSE', title: "培训费", width:100, minWidth:100, align:"center", style:editable,event:"nine_number", edit:'text'};
					tableCols[2][rankRes.data[BASE_CODE_USER_RANK].length+2] = {field: 'TRAIN_COUNT', title: '次数', width:60, minWidth:60, align:"left", style:editable, event:"nine_number", edit:'text'};
					tableCols[2][rankRes.data[BASE_CODE_USER_RANK].length+3] = {field: 'TRAIN_MAN_TIME', title: '人次', width:60, minWidth:60, align:"left", style:editable, event:"nine_number", edit:'text'};
					tableCols[2][rankRes.data[BASE_CODE_USER_RANK].length+4] = {field: 'TRAIN_OTHER', title: '参加其他<br>培训人次', width:100, minWidth:100, align:"left", style:editable, event:"nine_number", edit:'text'};
					
					//加载列表
					userParam['unit_id'] = $("#unit_id").val();
					userParam['year'] = $('#year').val().replace("年",'');
					common.ajax(listRetireEmployeeCost, 'post', 'json', userParam, function (ures) {
										console.log("ures :"+JSON.stringify(ures));

						var tableIns = table.render({
							elem : '#costList',
							data : ures.data,
							cellMinWidth : 60,
							height : "full-110",
							id : "costListTable",
							method: 'post',
							page:false,
							limit:ures['data'].length,  //不设置会只显示10条
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
	    $("#formid").attr("action",excelRetireEmployeeCost).trigger("submit");
	});
	
	//修改
	table.on('edit(costList)', function(obj){
		//console.log(obj);
		var inputElem = $(this);
		var tdElem = inputElem.closest('td');
		var valueOld = inputElem.prev().text();
		var data = {};
		if(obj.field=='TRAIN_COUNT' || obj.field=='TRAIN_MAN_TIME' || obj.field=='TRAIN_OTHER'){
			if(obj.value!="" && !/^[+]{0,1}(\d+)$/.test(obj.value)){
				$(tdElem).find("input").val(valueOld);
				$(tdElem).find("div").html(valueOld);
				layer.msg("请输入正整数", {icon: 2});
				return false;
			}
		}else if(obj.field!='REMARK'){
			if(obj.value!="" && !/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(obj.value)){
				$(tdElem).find("input").val(valueOld);
				$(tdElem).find("div").html(valueOld);
				layer.msg("金额格式不正确", {icon: 2});
				return false;
			}else{
				var trElem = inputElem.closest('tr');
				//修改合计值
				//点击搜索后这个值会改变，.laytable-cell-1（每次搜索一次加1）-1-5
				//var UTILITIES = trElem.find(".laytable-cell-1-1-5").text();
				//console.log(trElem.find("td:eq(10)").text());
				//console.log(trElem.find("td:eq(10)").find("div:eq(0)").text());
				var UTILITIES = trElem.find("td:eq(9)").text();
				if(obj.field=='UTILITIES'){
					UTILITIES = obj.value;
				}
				//var EMOLUMENT = trElem.find(".laytable-cell-1-1-6").text();
				var EMOLUMENT = trElem.find("td:eq(10)").text();
				if(obj.field=='EMOLUMENT'){
					EMOLUMENT = obj.value;
				}
				var DEPRECIATION = trElem.find("td:eq(11)").text();
				if(obj.field=='DEPRECIATION'){
					DEPRECIATION = obj.value;
				}
				var CHUMMAGE = trElem.find("td:eq(12)").text();
				if(obj.field=='CHUMMAGE'){
					CHUMMAGE = obj.value;
				}
				var OFFICE_ALLOWANCE = trElem.find("td:eq(13)").text();
				if(obj.field=='OFFICE_ALLOWANCE'){
					OFFICE_ALLOWANCE = obj.value;
				}
				var TRAVEL_EXPENSE = trElem.find("td:eq(14)").text();
				if(obj.field=='TRAVEL_EXPENSE'){
					TRAVEL_EXPENSE = obj.value;
				}
				var CONVENTION_EXPENSE = trElem.find("td:eq(15)").text();
				if(obj.field=='CONVENTION_EXPENSE'){
					CONVENTION_EXPENSE = obj.value;
				}
				var PUBLICITY_EXPENSE = trElem.find("td:eq(16)").text();
				if(obj.field=='PUBLICITY_EXPENSE'){
					PUBLICITY_EXPENSE = obj.value;
				}
				var TRAIN_EXPENSE = trElem.find("td:eq(17)").text();
				if(obj.field=='TRAIN_EXPENSE'){
					TRAIN_EXPENSE = obj.value;
				}
				var OTHER = trElem.find("td:eq(21)").text();
				if(obj.field=='OTHER'){
					OTHER = obj.value;
				}
				var total = Number(UTILITIES) + Number(EMOLUMENT) + Number(DEPRECIATION) + 
					Number(CHUMMAGE) + Number(OFFICE_ALLOWANCE) + Number(TRAVEL_EXPENSE) + 
					Number(CONVENTION_EXPENSE) + Number(PUBLICITY_EXPENSE) + Number(OTHER);
				inputElem.closest('tr').find("td:eq(8)").find("div").html(total.toFixed(2));
			}
		}
		//修改值到数据库
 		var CostParam = {};
		CostParam.id = obj.data.ID;
		CostParam.unit_id = obj.data.UNIT_ID;
		CostParam.year = $("#year").val().replace("年",'');
		CostParam[obj.field.toLowerCase()] = obj.value;
		CostParam.modified_user_id = user['id'];
		common.ajax(saveRetireEmployeeCost, 'post', 'json', JSON.stringify(CostParam), function (res) {
			layer.msg(res.message);
		},"application/json;charset=UTF-8");
	});
	
	//监听单元格事件（修改文本框maxLength）
	table.on('tool(costList)', function(obj){
		var data = obj.data;
		var tr = obj.tr;
		if(obj.event === 'nine_number'){
			$(tr).find("input").attr("maxLength","9");
		}else if(obj.event === 'one_hundred_varchar'){
			$(tr).find("input").attr("maxLength","100");
		}
	});
})