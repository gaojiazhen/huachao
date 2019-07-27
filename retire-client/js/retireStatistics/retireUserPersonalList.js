layui.use(['form','layer','table','common','laypage'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
		laypage = layui.laypage,
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
			BASE_CODE_APPELLATION +","+BASE_CODE_PLAY_A_ROLE+","
			+BASE_DEPARTMENT +","+BASE_CODE_USER_NATURE+","+BASE_CODE_USER_RANK+","+BASE_CODE_PLAY_A_ROLE
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
			//2、档案接收地
//			var codeParam = {"parent_id":BASE_CODE_RECEIVE_AREA, "is_available":"1"};
			common.ajax(getCode, 'post', 'json', codeParam , function (codeRes) {
				var tableCols = [];
				tableCols[0] = [{title: '基础信息', width:1975,minWidth:1975,align:"center", colspan:(18 + codeRes.data[BASE_CODE_RECEIVE_AREA].length)},
						{title: '党组织关系', width:360,minWidth:360,align:"center", colspan:4},
						{title: '其他信息', width:750,minWidth:750,align:"center", colspan:10}];
				//增加此序号行，会导致layui表格尾巴的数据不跟着表头横向拖动，所以不增加此行
				/* tableCols[1] = [];
				for(var i=0;i<(32 + codeRes.data[BASE_CODE_RECEIVE_AREA].length);i++){
					tableCols[1][i] = {title: (i+1), width:90,align:"center"};
				} */
				/* 
				tableCols[1] = [{title: '1', width:60,minWidth:60, align:"center"},
						{title: '2', width:90,minWidth:90, align:"center"},
						{title: '3', width:175,minWidth:175, align:"center"},
						{title: '4', width:90,minWidth:90, align:"center"},
						{title: '5', width:90,minWidth:90, align:"center"},
						{title: '6', width:90,minWidth:90, align:"center"},
						{title: '7', width:90,minWidth:90, align:"center"},
						{title: '8', width:90,minWidth:90, align:"center"},
						{title: '9', width:90,minWidth:90, align:"center"},
						{title: '10', width:90,minWidth:90, align:"center"},
						{title: '11', width:90,minWidth:90, align:"center"},
						{title: '12', width:90,minWidth:90, align:"center"},
						{title: '13', width:90,minWidth:90, align:"center"},
						{title: '14', width:120,minWidth:120, align:"center"},
						{title: '15', width:90,minWidth:90, align:"center"},
						{title: '16', width:90,minWidth:90, align:"center"},
						{title: '17', width:90,minWidth:90, align:"center"},
						{title: '18', width:90,minWidth:90, align:"center"},
						{title: '19', width:90,minWidth:90, align:"center"},
						{title: '20', width:90,minWidth:90, align:"center"},
						{title: '21', width:90,minWidth:90, align:"center"},
						
						{title: '22', width:90,minWidth:90, align:"center"},
						{title: '23', width:90,minWidth:90, align:"center"},
						{title: '24', width:90,minWidth:90, align:"center"},
						{title: '25', width:90,minWidth:90, align:"center"},
						
						{title: '26', width:90,minWidth:90, align:"center"},
						{title: '27', width:90,minWidth:90, align:"center"},
						{title: '28', width:90,minWidth:90, align:"center"},
						{title: '29', width:120,minWidth:120, align:"center"},
						{title: '30', width:90,minWidth:90, align:"center"},
						{title: '31', width:90,minWidth:90, align:"center"},
						{title: '32', width:90,minWidth:90, align:"center"},
						{title: '33', width:90,minWidth:90, align:"center"},
						{title: '34', width:90,minWidth:90, align:"center"},
						{title: '35', width:90,minWidth:90, align:"center"}]; */
				tableCols[2] = [{title: '序号', width:60,minWidth:60, align:"center", templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}, rowspan:2},
						{field: 'USER_NAME', title: '姓名', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'IDCARD', title: '身份证号码', width:175,minWidth:175, align:"center", rowspan:2},
						{field: 'SEX_ID', title: '性别', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'NATIVE_PLACE', title: '籍贯', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'NATION_ID', title: '民族', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'BIRTH_DATE', title: '出生年月', width:105, align:"center", rowspan:2},
						{field: 'POLITICS_STATUS_ID', title: '政治面貌', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'EDUCATION_ID', title: '最高学历', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'NOW_TREATMENT_LEVEL_ID', title: '资格职称', width:110,minWidth:110, align:"center", rowspan:2},
						{field: 'WORK_TIME', title: '参加工作时间', width:105, align:"center", rowspan:2},
						{field: 'RETIRE_TIME', title: '退休年月', width:105, align:"center", rowspan:2},
						{field: 'RETIREMENT_POST', title: '退休时单位及岗位', width:150, align:"center", rowspan:2},
						{field: 'RETIREMENT_DUTY', title: '退休时职务（职级）', width:150, align:"center", rowspan:2},
						{field: 'PHONE', title: '联系电话', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'SOCIAL_SECURITY_AREA', title: '社保关系所在地', width:150, align:"center", rowspan:2},
						{field: 'REGISTERED_PERMANENT_RESIDENCE', title: '户口所在地址', width:150, align:"center", rowspan:2},
						{field: 'RESIDENCE_ADDRESS', title: '现居住地详细地址', width:150, align:"center", rowspan:2},
						{title: '接收地', width:270,minWidth:270, align:"center", colspan:codeRes.data[BASE_CODE_RECEIVE_AREA].length},
						
						{field: 'GMT_PARTY', title: '入党时间', width:105, align:"center", rowspan:2},
						{field: 'MEMBERSHIP_CREDENTIALS_ID', title: '党组织关系所在地', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'PARTY_MEMBERSHIP_DUES', title: '党费月缴额(元)', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'GMT_PAID_UNTIL', title: '党费缴至年月', width:105, align:"center", rowspan:2},
						
						{field: 'MARITAL_STATUS_ID', title: '婚姻状况', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'AWARD_LEVEL_ID', title: '劳模情况标识', width:150, align:"center", rowspan:2},
						{field: 'SPECIAL_CROWD_IDS', title: '特殊人群标示', width:150, align:"center", rowspan:2},
						{field: 'CHILD_WORKING_SYS', title: '是否有子女在系统工作', width:120,minWidth:120, align:"center", rowspan:2},
						{field: 'ARCHIVES_AREA_ID', title: '档案存放地', width:90,minWidth:90, align:"center", rowspan:2},
						{field: 'ARCHIVES_BOOK_NUMBER', title: '档案册数', width:90,minWidth:90, align:"center", rowspan:2},
						{title: '其他联系人', width:270,minWidth:270, align:"center", colspan:3},
						{field: 'SPECIAL_REMARK', title: '备注', width:150, align:"center", rowspan:2}];
				tableCols[3] = [];
				for(var i=0;i<codeRes.data[BASE_CODE_RECEIVE_AREA].length;i++){
					tableCols[3][i] = {field:'RECEIVE_AREA_ADDRESS_' + codeRes.data[BASE_CODE_RECEIVE_AREA][i].value.toUpperCase(), title: codeRes.data[BASE_CODE_RECEIVE_AREA][i].name, 
						width:150, align:"center"};
				}	
				tableCols[3][codeRes.data[BASE_CODE_RECEIVE_AREA].length] = {field: 'SOCIALIZED_PENSION', title: '姓名', width:90,minWidth:90, align:"center", rowspan:2};
				tableCols[3][codeRes.data[BASE_CODE_RECEIVE_AREA].length+1] = {field: 'MEDICAL_INSURANCE', title: '关系', width:90,minWidth:90, align:"center", rowspan:2};
				tableCols[3][codeRes.data[BASE_CODE_RECEIVE_AREA].length+2] = {field: 'PARTY_TRANSFER_LOCAL', title: '电话', width:90,minWidth:90, align:"center", rowspan:2};
				//3、构建动态表头及列表
				userParam['unit_id'] = $("#unit_id").val();
				common.ajax(listRetireUserPersonalInformation, 'post', 'json', userParam , function (ures) {
					var tableIns = table.render({
						elem : '#userList',
						data : ures.data,
						height : "full-160",
						id : "userListTable",
						method: 'post',
						limit : ures.pageSize,
						cols : tableCols
					});
					laypage.render({
						elem: 'pagediv',
						count: ures.total, //数据总数，从服务端得到
						limit: ures.pageSize,
						curr: ures.pageNum,
						layout: ['prev', 'page', 'next', 'count', 'limit', 'refresh', 'skip'],
						jump: function(obj, first){
							//首次加载列表不执行
							if(!first){
								userParam['pageNum'] = obj.curr;    //得到当前页，以便向服务端请求对应页的数据。
								userParam['pageSize'] = obj.limit;  //得到每页显示的条数
								Action();
							}
						}
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
	    $("#formid").attr("action",excelRetireUserPersonalInformation).trigger("submit");
	});
})

