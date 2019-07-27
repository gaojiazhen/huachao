//与新增、修改页共用的数据字典列表
var codeList;
layui.use(['form', 'layer', 'table', 'common', 'laypage'], function() {
	var common = layui.common,
	layer = parent.layer === undefined ? layui.layer : top.layer,
		form = layui.form,
		
		$ = layui.jquery,
		laypage = layui.laypage,
		table = layui.table;
	var user = JSON.parse(sessionStorage.getItem("user"));
	var codeParam = {code:BASE_CODE_USER_NATURE + "," + BASE_CODE_USER_RANK+","+BASE_CODE_SEX+","+BASE_CODE_IS_AVAILABLE_YES+","+BASE_CODE_EDUCATION};
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		codeList = codeRes.data;
        Action();
	});
	
	Action();
	var userParam = {};
	//初始方法
	function Action() {
		//1、查单位下拉框
		var unitParam = {
			'parent_id': BASE_DEPARTMENT,
			'department_id': user['unit_id']
		};
		common.ajax(listBaseDepartment, 'post', 'json', unitParam, function(dres) {
			var unitHtml = getDepartmentHtml(dres.data, $('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if (dres.data.length > 1) {
				unitHtml = "<option value=''>全部单位</option>" + unitHtml;
			}
			$('#unit_id').html(unitHtml);
			form.render('select');
			//2、查部门下拉框
			showDepartment($('#unit_id').val());
			//3、加载列表数据
			userParam['unit_id'] = $("#unit_id").val();
			userParam['department_id'] = $("#department_id").val();
			common.ajax(listRetireContactByPaging, 'post', 'json', userParam, function(ures) {

				var tableIns = table.render({
					elem: '#userList',
					data: ures.data,
					cellMinWidth: 60,
					height: "full-160",
					limit: ures.pageSize,
					id: "userListTable",
					method: 'post',
					cols: [[
						{title: '序号', width: 60, minWidth: 60, align: "center", templet: function(d) {						
							return d.LAY_TABLE_INDEX + 1 + (ures.pageNum * ures.pageSize) - ures.pageSize;
						}},
						{field: 'user_name', title: '姓名', width: 90, minWidth: 90, align: "center", sort: true},					
						{field: 'birth_date', title: '出生年月', width: 105, minWidth: 105, align: "center", sort: true},
						{field: 'office_duty', title: '处室/职务', minWidth: 100, align: "center"},
						{title: '人员性质', width: 100, minWidth: 100,align: "center",templet: function(d){					
							return getCodeValue(BASE_CODE_USER_NATURE,d.user_nature_id);
						}},
						{title: '人员职级', width: 90, minWidth: 90, align: "center",templet: function(d){
							return getCodeValue(BASE_CODE_USER_RANK,d.user_rank_id);
						}},
						{field: 'work_seniority', title: '从事离退办工作年限', width: 180, minWidth: 180, align: "center", sort: true},
						{field: 'dial_directly', title: '办公电话-直拨', width: 140, minWidth: 140, align: "center", sort: true},
						{field: 'system_number', title: '办公电话-系统号', width: 150, minWidth: 150, align: "center", sort: true},
						{field: 'phone', title: '手机', width: 120, minWidth: 120, align: "center", sort: true},
						{field: 'modified_user_id', title: '修改人', width: 90, minWidth: 90, align: "center"},
						{field: 'gmt_modified', title: '修改时间', width: 145, minWidth: 145, align: "center", sort: true},
						{title: '操作', width: 115, minWidth: 115, templet: '#userListBar', fixed:"right", align: "center"}
					]]
				});
				laypage.render({
					elem: 'pagediv',
					count: ures.total, //数据总数，从服务端得到
					limit: ures.pageSize,
					curr: ures.pageNum,
					layout: ['prev', 'page', 'next', 'count', 'limit', 'refresh', 'skip'],
					jump: function(obj, first) {
						//首次加载列表不执行
						if (!first) {
							userParam['pageNum'] = obj.curr; //得到当前页，以便向服务端请求对应页的数据。
							userParam['pageSize'] = obj.limit; //得到每页显示的条数
							Action();
						}
					}
				});
				//监听表格排序
				table.on('sort(userList)', function(obj) {
					userParam['sortField'] = obj.field; //当前排序的字段名
					userParam['sortType'] = obj.type; //当前排序类型：desc（降序）、asc（升序）、null（空对象，默认排序）
					Action();
				});
				form.render();
			});
		});
		form.render();
	}

	//单位下拉框事件
	form.on('select(unit_id)', function(data) {
		$("#unit_id").val(data.value);
		showDepartment(data.value);
	});
	//构造部门下拉框
	function showDepartment(unit_id){
		if(unit_id!=null && unit_id!=""){
			var departmentParam = {
				'parent_id': unit_id,
				'department_id': BASE_DEPARTMENT_PROVINCIAL
			};
			common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function(dres) {
				var departmentHtml = getDepartmentHtml(dres.data, $('#department_id').val());
				if (dres.data.length > 1) {
					departmentHtml = "<option value=''>全部部门</option>" + departmentHtml;
				}
				$('#department_id').html(departmentHtml);
				form.render('select');
			});
			$('#department_div').show();
		}else{
			$('#department_id').html("<option value=''>全部部门</option>");  //为空的话IE8关闭填写页时会卡死
			$('#department_div').hide();
		}
	}
	//根据编码获取数据字典名称
	function getCodeValue(code,value){
		var typeCodeList = codeList[code];
		for(var i=0;i<typeCodeList.length;i++){
			if(value == typeCodeList[i].value){
				return typeCodeList[i].name;
			}
		}	
	}
	//搜索
	$(".search_btn").on("click", function() {
		userParam = getformObj($("#formid").serializeArray());
		Action();
	});
	//添加
	$("#addUser").click(function(){
	    editUser();
	});
	//添加、修改的弹窗
	function editUser(obj){
		var open_title = "添加";
		if(obj != null){
			open_title = "修改";
		}
	    var index = layui.layer.open({
	        title : open_title,
	        type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
	        content : "retireContactEdit.html",
	        success : function(layero, index){
	            var body = layui.layer.getChildFrame('body', index);
				//有值为编辑否则为修改
				body.find("input[name='modified_user_id']").val(user['id']);
				if(obj){
					body.find("input[name='id']").val(obj.id);
					body.find("#unit_id").val(obj.unit_id);
					body.find("#department_id").val(obj.department_id);
					body.find("input[name='user_name']").val(obj.user_name);
					body.find("#idcard").val(obj.idcard);
					body.find("#birth_date").val(obj.birth_date);
					body.find("#office_duty").val(obj.office_duty);
					body.find("#user_nature_id").val(obj.user_nature_id);
					body.find("#user_nature_special_mark").val(obj.user_nature_special_mark);
					body.find("#user_rank_id").val(obj.user_rank_id);
					body.find("#work_seniority").val(obj.work_seniority);
					body.find("input[name='dial_directly']").val(obj.dial_directly);
					body.find("input[name='system_number']").val(obj.system_number);
					body.find("input[name='phone']").val(obj.phone);
					body.find("input[name='sortnum']").val(obj.sortnum);
					body.find("#education_code").val(obj.education_code);
					body.find("#sex_code").val(obj.sex_code);
				}else{
					body.find("input[name='create_user_id']").val(user['id']);
					body.find("#unit_id").val($("#unit_id").val());
					body.find("#department_id").val($("#department_id").val());
				}
				form.render();
	            setTimeout(function(){
	                layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
	                    tips: 3
	                });
	            },500)
	        },
			end: function(index, layero){
				Action();
			}
	    })
	    layui.layer.full(index);
	    //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
	    $(window).on("resize",function(){
	        layui.layer.full(index);
	    })
	}
	//列表操作
	table.on('tool(userList)', function(obj){
	    var layEvent = obj.event,
			data = obj.data;
	    if(layEvent === 'del'){ //删除
	        layer.confirm("确定删除【" + data.user_name + "】的通讯录信息？",{icon:3, title:'提示信息'},function(index){
				var dparam= {'ids':data.id} ;
				common.ajax(removeRetireContact, 'post', 'json', dparam, function (res) {
					if(res.data){
						obj.del();
						layer.msg("删除成功!");
					}else{
						layer.msg("网络异常!");
					}
				})
	         })
	    }else if(layEvent === 'update'){ //修改
			editUser(data);
		}
	});
	//弹出Excel导入界面
	$("#impExcel").click(function(){
	    var index = layui.layer.open({
	        title : "导入Excel",
			area: ['600px', '400px'],
	        type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
	        content : "retireContactExp.html",
	        success : function(layero, index){
	            var body = layui.layer.getChildFrame('body', index);
	    		body.find("#unit_id").val($("#unit_id").val());
	    		form.render();
	            setTimeout(function(){
	                layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {tips: 3});
	            },500)
	        },
	    	end: function(index, layero){
	    		Action();
	    	}
	    });
	});
})
