layui.use(['form','table','common'],function(){	
	var common = layui.common,
		table = layui.table,
		form = layui.form,
		$ = layui.jquery;
	
	common.ajax(listQuitCadreByRegion, 'post', 'json', null , function (ures) {
		var tableIns = table.render({
			elem : '#userList',
			data : ures.data,
			cellMinWidth : 60,
			height : "full-160",
			id : "userListTable",
			method: 'post',
			page:false,
			limit:ures['data'].length,  //不设置会只显示10条
			cols : [[
				{title: '项目', minWidth:60, align:"center", templet: function(d){
					return "总计";
				}, rowspan:2},
				{title: '编号', minWidth:60, align:"center", templet: function(d){
					return d.LAY_TABLE_INDEX + 1;
				}, rowspan:2},
				{title: '总数', minWidth:60, align:"center", templet: function(d){
					return Number(d.FZ)  + Number(d.XM) + Number(d.PT) + Number(d.QZ) + Number(d.LY) +
						Number(d.ZZ) + Number(d.NP) + Number(d.ND) + Number(d.SM);
				}, rowspan:2},
				{title: '分布在各地', align:"center" , colspan:10}
				],[
				{title: '小计', minWidth:60, align:"center", templet: function(d){
					return Number(d.FZ)  + Number(d.XM) + Number(d.PT) + Number(d.QZ) + Number(d.LY) +
						Number(d.ZZ) + Number(d.NP) + Number(d.ND) + Number(d.SM);
				}},
				{field: 'FZ', title: '福州', minWidth:60, align:"center"},
				{field: 'XM', title: '厦门', minWidth:60, align:"center"},
				{field: 'PT', title: '莆田', minWidth:60, align:"center"},
				{field: 'QZ', title: '泉州', minWidth:60, align:"center"},
				{field: 'LY', title: '龙岩', minWidth:60, align:"center"},
				{field: 'ZZ', title: '漳州', minWidth:60, align:"center"},
				{field: 'NP', title: '南平', minWidth:60, align:"center"},
				{field: 'ND', title: '宁德', minWidth:60, align:"center"},
				{field: 'SM', title: '三明', minWidth:60, align:"center"}
			]]
		});
		form.render();
	});
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action",excelQuitCadreByRegion).trigger("submit");
	});
})