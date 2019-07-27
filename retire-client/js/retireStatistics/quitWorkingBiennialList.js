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
    //初始方法
	function Action(){
		var unitParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', unitParam, function (dres) {
			var unitHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				unitHtml = "<option value=''>全部</option>" + unitHtml;
			}
			$('#unit_id').html(unitHtml);
			form.render('select');
			//加载列表
			userParam['unit_id'] = $("#unit_id").val();
			common.ajax(listQuitWorkingBiennialChange, 'post', 'json', userParam, function (ures) {
				var tableIns = table.render({
					elem : '#userList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-110",
					id : "userListTable",
					method: 'post',
					page:false,
					limit:ures['data'].length,  //不设置会只显示10条
					cols : [[
						{field: 'DEPARTMENT_NAME', title: '项目', minWidth:160,align:"left", rowspan:2},
						{title: '编号', width:60,minWidth:60, align:"center", rowspan:2, templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}},
						{field: 'PREVIOUS_YEAR_CADRE', title: '上年12月底<br>退职人员总数', width:130,minWidth:130, align:"center", rowspan:2},
						{field: 'THIS_YEAR_RETIRE', title: '本年度内办理<br>退职手续人员数', width:130,minWidth:130, align:"center", rowspan:2},
						{field: 'THIS_YEAR_DECEASED', title: '本年度内<br>去世退职人员数', width:130,minWidth:130, align:"center", rowspan:2},
						{title: '本年12月底<br>应有退职人员数', width:130,minWidth:130, align:"center", rowspan:2, templet: function(d){
							return Number(d.PREVIOUS_YEAR_CADRE) + Number(d.THIS_YEAR_RETIRE) - Number(d.THIS_YEAR_DECEASED);
						}},
						{title: '本年12月底<br>实有退职人员数', width:130,minWidth:130, align:"center", rowspan:2, templet: function(d){
							return Number(d.PREVIOUS_YEAR_CADRE) + Number(d.THIS_YEAR_RETIRE) - Number(d.THIS_YEAR_DECEASED);
						}},
						{title: '其中', width:150,minWidth:150, align:"center", colspan:2},
						],[
						{field: 'IN_BEIJING', title: '在京', width:60,minWidth:60, align:"center"},
						{field: 'COMMUNIST', title: '中共党员', width:90,minWidth:90, align:"center"}
					]]
				});
				form.render();
			});
		});
		form.render();
	}
    //搜索
    $(".search_btn").on("click",function(){
		userParam = getformObj($("#formid").serializeArray());
		Action();
    });
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action",excelQuitWorkingBiennialChange).trigger("submit");
	});
})