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
			userParam['year'] = $('#year').val().replace("年",'');
			common.ajax(listRetireArenaServiceCondition, 'post', 'json', userParam, function (ures) {
				var tableIns = table.render({
					elem : '#arenaList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-110",
					id : "arenaListTable",
					method: 'post',
					page: false,
					limit: ures['data'].length,  //不设置会只显示10条
					cols : [[
						{field: 'DEPARTMENT_NAME', title: '企业名称', align:"left", rowspan:2},
						{field: 'SERVICE_CONDITION', title: '活动场所使用情况', align:"left", rowspan:2},
						{field: 'INDEPENDENT_SUM', title: '个数（个）', align:"center", rowspan:2},
						{field: 'INDEPENDENT_ACREAGE', title: '面积（m²）', align:"center", rowspan:2},
						{title: (parseInt($('#year').val()) - 1) + '年末固定资产情况（万元） ', align:"center", colspan:2}
					],[
						{field: 'INDEPENDENT_ORIGINAL_VALUE', title: '原值', align:"center"},
						{field: 'INDEPENDENT_NET_VALUE', title: '净值', align:"center"}
					]], done: function (res, curr, count) {
                        //数据渲染完的回调合并表格内容行
                        mergeRows(0);
						//mergeRows(4);
						//mergeRows(5);
                    }
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
	    $("#formid").attr("action", excelRetireArenaServiceCondition).trigger("submit");
	});
	/**
     * 合并第几列（相同内容合并）
     * @param index 列的下标,从0开始
     * @author CYK
     */
    function mergeRows(index) {
        // 根据选择器找到表格的tr
        var _tr = $('.layui-table-body table tr');
        var mergeStr = '', oldName, newName, countNum = 0;
        for (var i = 0, trLength = _tr.length; i < trLength; i++) {
            newName = $(_tr[i]).find('div:eq(' + index + ')').text();
			if(newName!="" && newName!=null){
				if (i == 0 || newName == oldName) {
					countNum++;
				} else {
					mergeStr += (countNum + (Array(countNum).join(0)));
					countNum = 1;
				}
				oldName = newName;
			}
        }
        // 解释:从左到右,4表示合并4行,0表示设定display:none
        mergeStr += (countNum + (Array(countNum).join(0)));
        var cssNum = 0;
        // 根据合并参考信息进行行合并
        for (var i = 0, length = mergeStr.length; i < length; i++) {
            var e = $(_tr[i]).find('td:eq(' + index + ')');
            if (mergeStr[i] !== '0') {
                // 合并行
                e.attr('rowspan', mergeStr[i]);
            } else {
                e.css('display', 'none');
            }
        }
    }
})