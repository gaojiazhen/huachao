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
			userParam['unit_id'] = $("#unit_id").val();
			common.ajax(listQuitCadreBasic, 'post', 'json', userParam , function (ures) {
				
				console.log("ures  :"+JSON.stringify(ures));
				var tableIns = table.render({
					elem : '#userList',
					data : ures.data,
					height : "full-110",
					id : "userListTable",
					method: 'post',
					page:false,
					limit:ures['data'].length,  //不设置会只显示10条
					cols : [[
						{field: 'DEPARTMENT_NAME', title: '项目', width:160,minWidth:160, align:"center", rowspan:3},
						{title: '编号', width:60,minWidth:60, align:"center", templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}, rowspan:3},
						{field: 'TOTAL', title: '总数', width:60,minWidth:60, align:"center", rowspan:3},
						{field: 'COMMUNIST', title: '中共党员', width:70,minWidth:70, align:"center", rowspan:3},
						{field: 'CANNOT_CARE_ONESELF', title: '生活不能自理', width:70,minWidth:70, align:"center", rowspan:3},
						{field: 'SEVENTY_AGE', title: '70岁至79岁', width:110,minWidth:110, align:"center", rowspan:3},
						{field: 'EIGHTY_AGE', title: '80岁及以上', width:110,minWidth:110, align:"center", rowspan:3},
						{title: '参加革命工作时间', width:680,minWidth:680, align:"center" , colspan:10}
						],[
						{title: '1927年7月底以前', width:140,minWidth:140, align:"center", colspan:2},
						{title: '1927年8月1日至1937年7月6日', width:140,minWidth:140, align:"center", colspan:2},
						{title: '1937年7月7日至1942年12月底', width:140,minWidth:140, align:"center", colspan:2},
						{title: '1943年1月1日至1945年9月2日', width:140,minWidth:140, align:"center", colspan:2},
						{title: '1945年9月3日至9月底', width:120,minWidth:120, align:"center", colspan:2} 
						],[ 
						{field: 'WORK_TIME_19270731', title: '', width:70,minWidth:70, align:"center"},
						{field: 'BEIJING_19270731', title: '在京', width:70,minWidth:70, align:"center"},
						{field: 'WORK_TIME_19370706', title: '', width:70,minWidth:70, align:"center"},
						{field: 'BEIJING_19370706', title: '在京', width:70,minWidth:70, align:"center"},
						{field: 'WORK_TIME_19421231', title: '', width:70,minWidth:70, align:"center"},
						{field: 'BEIJING_19421231', title: '在京', width:70,minWidth:70, align:"center"},
						{field: 'WORK_TIME_19450902', title: '', width:70,minWidth:70, align:"center"},
						{field: 'BEIJING_19450902', title: '在京', width:70,minWidth:70, align:"center"},
						{field: 'WORK_TIME_19450930', title: '', width:60,minWidth:60, align:"center"},
						{field: 'BEIJING_19450930', title: '在京', width:60,minWidth:60, align:"center"}
					]]
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
	    $("#formid").attr("action",excelQuitCadreBasic).trigger("submit");
	});
})