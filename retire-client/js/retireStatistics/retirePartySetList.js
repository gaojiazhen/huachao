layui.use(['form','layer','laydate','table','laytpl','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
		laypage = layui.laypage;
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
			userParam['gmt_statistics'] = $('#gmt_statistics').val().replace("年",'-').replace("月",'');
			common.ajax(listRetirePartyOrganization, 'post', 'json', userParam, function (ures) {
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
						{field: 'DEPARTMENT_NAME', title: '项目', minWidth:160, align:"center", rowspan:2},
						{title: '编号', width:60,minWidth:60, align:"center", templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}, rowspan:2},
						{field: 'PARTY_GENERAL_BRANCH', title: '党总支数', width:90,minWidth:90,align:"center",rowspan:2},
						{field: 'PARTY_BRANCH_NUMBER', title: '党支部总数', width:110,minWidth:110, align:"center",rowspan:2},
						{title: '其中', width:90,minWidth:90, align:"center",colspan:5},
						{field: 'PARTY_GROUP_NUMBER', title: '党小组数', width:90,minWidth:90, align:"center",rowspan:2}
					],[
						{field: 'QUIT_CADRE_PARTY_BRANCH', title: '离休干部党支部', width:145,minWidth:145, align:"center"},
						{field: 'RETIRE_COMBINE_NUMBER', title: '离/退休合编数', width:120,minWidth:120, align:"center"},
						{field: 'RETIRE_PARTY_BRANCH', title: '退休人员党支部数',  width:145,minWidth:145, align:"center"},
						{field: 'QUIT_INSERVICE_COMBINE', title: '离休与在职合编数', width:145,minWidth:145, align:"center"},
						{field: 'RETIRE_INSERVICE_COMBINE', title: '退休与在职合编数', width:145,minWidth:145, align:"center"}
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
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action",excelRetirePartyOrganization).trigger("submit");
	});
})