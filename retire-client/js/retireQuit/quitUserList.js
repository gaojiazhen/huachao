var codeList;
layui.use(['form','layer','laydate','table','common','laypage'],function(){
    var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laypage = layui.laypage,
        table = layui.table;
    var	user = JSON.parse(sessionStorage.getItem("user"));

    var codeParam = {code:BASE_CODE_SEX + "," + BASE_CODE_MARITAL_STATUS + "," + BASE_CODE_EDUCATION + "," + BASE_CODE_POLITICS_STATUS + "," + BASE_CODE_NATURE};
    common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
        codeList = codeRes.data;
        Action();
    });

    //监听下拉，为了后续取值
    form.on('select(unit_id)', function(data){
        $("#unit_id").val(data.value);
    });
    //选择离休开始日期
    laydate.render({
        elem: '#start_retire_time',
        theme: 'grid',
        trigger: 'click',
        max : 0,
        done: function(value, date, endDate){
            var startdate = new Date($("#start_retire_time").val().replace(/\-/g,'/'));
            var enddate = new Date($("#end_retire_time").val().replace(/\-/g,'/'));
            if(startdate > enddate){
                $("#start_retire_time").val("");
                layer.msg("开始日期超出了结束日期<br>建议重新选择");
                return;
            }
        }
    });
    //选择离休结束日期
    laydate.render({
        elem: '#end_retire_time',
        theme: 'grid',
        trigger: 'click',
        max : 0,
        done: function(value, date, endDate){
            var startdate = new Date($("#start_retire_time").val().replace(/\-/g,'/'));
            var enddate = new Date($("#end_retire_time").val().replace(/\-/g,'/'));
            if(startdate > enddate){
                $("#end_retire_time").val("");
                layer.msg("开始日期超出了结束日期<br>建议重新选择");
                return;
            }
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
            common.ajax(listRetireQuitUser, 'post', 'json', userParam, function (ures) {
                var tableIns = table.render({
                    elem : '#userList',
                    data : ures.data,
                    cellMinWidth : 60,
                    height : "full-160",
                    limit : ures.pageSize,
                    id : "userListTable",
                    method: 'post',
                    cols : [[
                        {title: '序号', width:60,minWidth:60,align:"center", templet: function(d){
                                return d.LAY_TABLE_INDEX + 1 + (ures.pageNum * ures.pageSize) - ures.pageSize;
                            }},
                        {field: 'user_name', title: '姓名', width:90,minWidth:90,align:"center",sort:true},
                        // {field: 'sex_name', title: '性别', width:60,minWidth:60, align:"center"},
                        {title: '性别', width:60,minWidth:60, align:"center",templet: function (d) {
                                return getCodeValue(BASE_CODE_SEX,d.sex_id);
                            }},

                        {field: 'idcard', title: '身份证号码', width:175,minWidth:175, align:"center",sort:true},

                        {field: 'monthly_income', title: '月收入总额', width:100, minWidth:100, align:"center"},
                        {field: 'basic_expenses', title: '基本离休费', width:100, minWidth:100, align:"center"},
                        {field: 'province_subsidy', title: '省（部）规定补贴', width:150, minWidth:150, align:"center"},
                        {field: 'other_subsidy', title: '其它各项补贴', width:120, minWidth:120, align:"center"},
                        {field: 'children_number', title: '子女总人数', width:100, minWidth:100, align:"center"},
                        {field: 'modified_user_name', title: '修改人', width:90,minWidth:90,align:"center"},
                        {field: 'gmt_modified', title: '修改时间', width:145,minWidth:145, align:"center",sort:true},
                        {title: '操作', width:115,minWidth:115, templet:'#userListBar',fixed:"right",align:"center"}
                    ]]
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
                //监听表格排序
                table.on('sort(userList)', function(obj){
                    userParam['sortField'] = obj.field; //当前排序的字段名
                    userParam['sortType'] = obj.type; 	//当前排序类型：desc（降序）、asc（升序）、null（空对象，默认排序）
                    Action();
                });
                form.render();
            });;
        });
        form.render();
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
    $(".search_btn").on("click",function(){
        userParam = getformObj($("#formid").serializeArray());
        Action();
    });
    //跳转到导入页
    $("#impExcel").click(function(){
        var index = layui.layer.open({
            title : "导入Excel",
            area: ['600px', '400px'],
            type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            content : "quitUserExp.html",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {tips: 3});
                },500)
            },
            end: function(index, layero){
                Action();
            }
        });
    });
    //登记、修改的弹窗
    function editUser(obj,layEvent){
        var open_title = "登记";
        if (layEvent === 'update') {
            open_title = "修改";
        }
        var index = layui.layer.open({
            title : open_title,
            type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            content : "quitUserEdit.html?user_id=" + obj.user_id,
            success : function(layero, index){
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
        if (layEvent === 'add') { //登记
            editUser(data,layEvent);
        } else if (layEvent === 'update') { //修改
            editUser(data,layEvent);
        } else if(layEvent === 'del'){ //删除
            layer.confirm("确定删除【" + data.user_name + "】的离休信息？",{icon:3, title:'提示信息'},function(index){
                var dparam= {'ids':data.id} ;
                common.ajax(removeRetireQuit, 'post', 'json', dparam, function (res) {
                    if(res.data){
                        Action();
                        layer.msg("删除成功!");
                    }else{
                        layer.msg("网络异常!");
                    }
                })
            })
        }
    });
})