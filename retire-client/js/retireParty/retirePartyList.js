layui.use(['form','layer','laydate','table','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	//选择统计时间
	laydate.render({
	    elem: '#gmt_statistics',
		theme: 'grid',
		type: 'month',
	    max : 0,
		value: new Date(),
		format: 'yyyy年M月',
		// 点击即选中
        ready:function(date){
            $("#layui-laydate1").off('click').on('click','.laydate-month-list li',function(){
                $("#layui-laydate1").remove();
            });
        },
		change: function(value){
			$('#gmt_statistics').val(value);
		}
	});
	Action();
	var userParam = {};
    //初始方法
	function Action(){
		var departmentParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
			var departmentHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				departmentHtml = "<option value=''>全部</option>" + departmentHtml;
			}
			$('#unit_id').html(departmentHtml);
			form.render('select');
			//加载列表
			userParam['unit_id'] = $("#unit_id").val();
			userParam['modified_user_id'] = user['id'];
			userParam['gmt_statistics'] = $('#gmt_statistics').val().replace("年",'-').replace("月",'')
			common.ajax(listRetireParty, 'post', 'json', userParam, function (ures) {
				var editable = "background-color:#5FB878";
				var tableIns = table.render({
					elem : '#partyList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-110",
					id : "partyListTable",
					method: 'post',
					page:false,
					limit:ures['data'].length,  //不设置会只显示10条
					cols : [[
						{field: 'unit_name', title: '单位', width:160,minWidth:160,align:"left",rowspan:2},
						{field: 'party_general_branch', style:editable,event:"five_number", edit:'text', title: '党总支数', width:90,minWidth:90,align:"center",rowspan:2},
						{field: 'party_branch_number', style:editable,event:"five_number", edit:'text', title: '党支部数', width:90,minWidth:90, align:"center",rowspan:2},
						{title: '党员数', width:80,minWidth:80, align:"center",rowspan:2, templet: function(d){
							return Number(d.company_party) + Number(d.place_party);
						}},
						{title: '其中', align:"center",colspan:2},
						{field: 'party_group_number', style:editable,event:"five_number", edit:'text', title: '党小组数', width:90,minWidth:90, align:"center",rowspan:2},
						{field: 'quit_cadre_party_branch', style:editable,event:"five_number", edit:'text', title: '离休干部党支部数', width:145,minWidth:145, align:"center",rowspan:2},
						{field: 'retire_combine_number', style:editable,event:"five_number", edit:'text', title: '离/退休合编数', width:120,minWidth:120, align:"center",rowspan:2},
						{field: 'retire_party_branch', style:editable,event:"five_number", edit:'text', title: '退休人员党支部数',  width:145,minWidth:145, align:"center",rowspan:2},
						{field: 'quit_inservice_combine', style:editable,event:"five_number", edit:'text', title: '离休与在职合编数', width:145,minWidth:145, align:"center",rowspan:2},
						{field: 'retire_inservice_combine', style:editable,event:"five_number", edit:'text', title: '退休与在职合编数', width:145,minWidth:145, align:"center",rowspan:2},
						{title: '人员情况设置',  align:"center",colspan:32}
						/* ,
						{field: 'modified_user_name', title: '修改人', width:90,minWidth:90,align:"center",rowspan:2},
						{field: 'gmt_modified', title: '修改时间', width:145,minWidth:145, align:"center",rowspan:2} */
					],[
						{field: 'company_party', title: '公司管理党员数', width:130,minWidth:130, align:"center"},
						{field: 'place_party', title: '地方管理党员数', width:130,minWidth:130, align:"center"},
						{field: 'party_general_branch_secretary', style:editable,event:"ten_varchar", edit:'text', title: '党总支书记', width:100,minWidth:100, align:"center"},
						{field: 'party_general_branch_member', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary1', style:editable,event:"ten_varchar", edit:'text', title: '第一党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member1', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary2', style:editable,event:"ten_varchar", edit:'text', title: '第二党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member2', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary3', style:editable,event:"ten_varchar", edit:'text', title: '第三党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member3',style:editable, event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary4', style:editable,event:"ten_varchar", edit:'text', title: '第四党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member4', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary5', style:editable,event:"ten_varchar", edit:'text', title: '第五党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member5', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary6', style:editable,event:"ten_varchar", edit:'text', title: '第六党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member6', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary7', style:editable,event:"ten_varchar", edit:'text', title: '第七党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member7', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary8', style:editable,event:"ten_varchar", edit:'text', title: '第八党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member8', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary9', style:editable,event:"ten_varchar", edit:'text', title: '第九党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member9', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary10', style:editable,event:"ten_varchar", edit:'text', title: '第十党支部书记', width:130,minWidth:130, align:"center"},
						{field: 'branch_committee_member10', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},				
						{field: 'party_branch_secretary11', style:editable,event:"ten_varchar", edit:'text', title: '第十一党支部书记', width:150,minWidth:150, align:"center"},
						{field: 'branch_committee_member11', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary12', style:editable,event:"ten_varchar", edit:'text', title: '第十二党支部书记', width:150,minWidth:150, align:"center"},
						{field: 'branch_committee_member12', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary13', style:editable,event:"ten_varchar", edit:'text', title: '第十三党支部书记', width:150,minWidth:150, align:"center"},
						{field: 'branch_committee_member13', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary14', style:editable,event:"ten_varchar", edit:'text', title: '第十四党支部书记', width:150,minWidth:150, align:"center"},
						{field: 'branch_committee_member14', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"},
						{field: 'party_branch_secretary15', style:editable,event:"ten_varchar", edit:'text', title: '第十五党支部书记', width:150,minWidth:150, align:"center"},
						{field: 'branch_committee_member15', style:editable,event:"one_hundred_varchar", edit:'text', title: '支委委员', width:90,minWidth:90, align:"center"}
	
					]]
				});
				form.render();
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
	//修改
	table.on('edit(partyList)', function(obj){
		var inputElem = $(this);
		var tdElem = inputElem.closest('td');
		var valueOld = inputElem.prev().text();
		var data = {};
		if(obj.field=='party_general_branch' || obj.field=='party_branch_number' || 
			obj.field=='party_group_number' || obj.field=='quit_cadre_party_branch' || 
			obj.field=='retire_combine_number' || obj.field=='retire_party_branch' || 
			obj.field=='quit_inservice_combine' || obj.field=='retire_inservice_combine'){
			if(obj.value!="" && !/^[+]{0,1}(\d+)$/.test(obj.value)){
				$(tdElem).find("input").val(valueOld);
				$(tdElem).find("div").html(valueOld);
				layer.msg("请输入正整数", {icon: 2});
				return false;
			}
		}
		//修改值到数据库
		var partyParam = {};
		partyParam.id = obj.data.id;
		partyParam[obj.field] = obj.value;
		partyParam.modified_user_id = user['id'];
		common.ajax(saveRetireParty, 'post', 'json', JSON.stringify(partyParam), function (res) {
			layer.msg(res.message);
		},"application/json;charset=UTF-8");
	});
	
	//监听单元格事件（修改文本框maxLength）
	table.on('tool(partyList)', function(obj){
		var data = obj.data;
		if(obj.event === 'five_number'){
			var tr = obj.tr;
			var input = $(tr).find("input").attr("maxLength","5");
		}else if(obj.event === 'ten_varchar'){
			var tr = obj.tr;
			var input = $(tr).find("input").attr("maxLength","10");
		}else if(obj.event === 'one_hundred_varchar'){
			var tr = obj.tr;
			var input = $(tr).find("input").attr("maxLength","100");
		}
	});
})