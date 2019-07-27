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
			common.ajax(listQuitCadreBiennialChange, 'post', 'json', userParam, function (ures) {
				var tableIns = table.render({
					//title : "离休干部两年数字变化情况统计表",
					elem : '#userList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-110",
					id : "userListTable",
					method: 'post',
					page:false,
					limit:ures['data'].length,  //不设置会只显示10条
					//toolbar: true,
					//defaultToolbar : ['filter', 'print', 'exports'],
					cols : [[
						{field: 'DEPARTMENT_NAME', title: '项目', minWidth:160,align:"left"},
						{title: '编号', width:90, minWidth:60, align:"center", templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}},
						{field: 'LAST_YEAR_CADRE', title: '上年12月底离休干部总数', width:190,minWidth:190, align:"center"},
						{field: 'THIS_YEAR_CADRE_DEMISE', title: '本年度内去世离休干部数', width:190,minWidth:190, align:"center"},
						{title: '本年12月底应有离休干部数', width:210,minWidth:210, align:"center", templet: function(d){
							return Number(d.LAST_YEAR_CADRE) - Number(d.THIS_YEAR_CADRE_DEMISE);
						}},
						{title: '本年12月底实有离休干部数', width:210,minWidth:210, align:"center", templet: function(d){
							return Number(d.LAST_YEAR_CADRE) - Number(d.THIS_YEAR_CADRE_DEMISE);
						}},
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
	    $("#formid").attr("action",excelQuitCadreBiennialChange).trigger("submit");
	});
})