layui.use(['form','layer','laydate','common'],function(){
    var common = layui.common,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laydate = layui.laydate,
        $ = layui.jquery;
    var	user = JSON.parse(sessionStorage.getItem("user"));
    var codeParam = {code:BASE_CODE_SEX + "," + BASE_CODE_MARITAL_STATUS + "," + BASE_CODE_EDUCATION + "," + BASE_CODE_POLITICS_STATUS + "," + BASE_CODE_NATURE};
    common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
        codeList = codeRes.data;
    });
    //查离退休人员信息、中共党员登记信息
    var user_id = getQueryString("user_id");
    $("#user_id").val(user_id);
    var userParam = {
        "user_id":user_id
    };

    //列表页已经查询过的数据字典
    var parentCodeList = parent.codeList;

    //根据编码获取数据字典名称
    function getCodeValue(code,value){
        var typeCodeList = parentCodeList[code];
        for(var i=0;i<typeCodeList.length;i++){
            if(value == typeCodeList[i].value){
                return typeCodeList[i].name;
            }
        }
    }

    common.ajax(getRetireCommunistByUserid, 'post', 'json', userParam, function (userRes){
        //1、离退休人员信息
        var retireUserDO = userRes.data.retireUserDO;
        $("#user_name").html(retireUserDO.user_name);
        // $("#sex_name").html(retireUserDO.sex_name);
        $("#sex_name").html(getCodeValue(BASE_CODE_SEX,retireUserDO.sex_id));
        $("#idcard").html(retireUserDO.idcard);
        $("#unit_id").val(retireUserDO.unit_id);
        //2、中共党员信息
        var retireCommunistDO = userRes.data.retireCommunistDO;
        $("input[name='modified_user_id']").val(user['id']);
        var work_unit_post = "";
        var place_detailed = "";
        var is_missing = "";
        var is_flow_communist = "";
        if (null!=retireCommunistDO && null!=retireCommunistDO.id && ""!=retireCommunistDO.id) {
            $("input[name='id']").val(retireCommunistDO.id);
            $("input[name='category_personnel'][value='"+retireCommunistDO.category_personnel+"']").attr("checked","checked");
            $("input[name='party_branch']").val(retireCommunistDO.party_branch);
            $("#gmt_party").val(retireCommunistDO.gmt_party);
            $("#gmt_become").val(retireCommunistDO.gmt_become);
            $("input[name='operating_post'][value='"+retireCommunistDO.operating_post+"']").attr("checked","checked");
            $("#membership_credentials_id").val(retireCommunistDO.membership_credentials_id);
            $("#membership_credentials_mark").val(retireCommunistDO.membership_credentials_mark);
            if(null!==retireCommunistDO.work_unit_post && ""!=retireCommunistDO.work_unit_post){
                work_unit_post = retireCommunistDO.work_unit_post;
            }
            if(null!=retireCommunistDO.place_detailed && ""!=retireCommunistDO.place_detailed){
                place_detailed = retireCommunistDO.place_detailed;
            }
            $("input[name='party_membership'][value='"+retireCommunistDO.party_membership+"']").attr("checked","checked");
            $("input[name='party_post']").val(retireCommunistDO.party_post);
            $("input[name='party_membership_dues']").val(retireCommunistDO.party_membership_dues);
            $("#gmt_paid_until").val(retireCommunistDO.gmt_paid_until);
            is_missing = retireCommunistDO.is_missing;
            $("input[name='is_missing'][value='"+is_missing+"']").attr("checked","checked");
            $("#gmt_missing").val(retireCommunistDO.gmt_missing);
            is_flow_communist = retireCommunistDO.is_flow_communist;
            $("input[name='is_flow_communist'][value='"+is_flow_communist+"']").attr("checked","checked");
            $("input[name='go_out_flow_direction']").val(retireCommunistDO.go_out_flow_direction);
        } else {
            $("input[name='create_user_id']").val(user['id']);
        }

        //2、查数据字典列表
        var codeParam = {
            code:BASE_CODE_PARTY_LOCATED
        };
        common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
            //组织关系所在单位
            var list = codeRes.data[BASE_CODE_PARTY_LOCATED];
            var partyLocatedHtml = "";
            var checkedValue = $("#membership_credentials_id").val();
            for (var i = 0; i < list.length; i++) {
                var checked = "";
                if (checkedValue == list[i].value) {
                    checked = "checked";
                }
                var special_mark = list[i].special_mark;
                partyLocatedHtml += '<div class="layui-input-inline" style="width:80px;"><input type="radio" lay-verify="partyRadio" name="membership_credentials_id" value="' + list[i].value + '" title="' +
                    list[i].name + '" special_mark="' + special_mark + '" lay-filter="membership_credentials_id" '+checked+' ></div>';
                if(1==special_mark){
                    partyLocatedHtml += '<div class="layui-input-inline"><input type="text" id="place_detailed" name="place_detailed" class="layui-input" '+
                        'placeholder="请输入地方详细" maxlength="150" autocomplete="off" value="'+place_detailed+'"></div>';
                }else{
                    partyLocatedHtml += '<div class="layui-input-inline"><input type="text" id="work_unit_post" name="work_unit_post" class="layui-input" '+
                        'placeholder="请输入工作单位及职务" maxlength="150" autocomplete="off" value="'+work_unit_post+'"></div>&nbsp;&nbsp;&nbsp;&nbsp;';
                }
            }
            $("#partyLocatedDiv").html(partyLocatedHtml);
            form.render();
            //修改时根据原先选择内容判断元素显示
            var membership_credentials_mark = $("#membership_credentials_mark").val();
            showParty(membership_credentials_mark);
        });

        //组织关系所在单位
        /*		var codeParam = {
                    "parent_id":BASE_CODE_PARTY_LOCATED
                };*/
        // common.ajax(listBaseCode, 'post', 'json', codeParam, function (codeRes){
        // 	var list = codeRes.data;
        // 	var partyLocatedHtml = "";
        // 	var checkedValue = $("#membership_credentials_id").val();
        // 	for (var i = 0; i < list.length; i++) {
        // 		var checked = "";
        // 		if (checkedValue == list[i].id) {
        // 			checked = "checked";
        // 		}
        // 		var special_mark = list[i].special_mark;
        // 		partyLocatedHtml += '<div class="layui-input-inline" style="width:80px;"><input type="radio" lay-verify="partyRadio" name="membership_credentials_id" value="' + list[i].id + '" title="' +
        // 			list[i].code_name + '" special_mark="' + special_mark + '" lay-filter="membership_credentials_id" '+checked+' ></div>';
        // 		if(1==special_mark){
        // 			partyLocatedHtml += '<div class="layui-input-inline"><input type="text" id="place_detailed" name="place_detailed" class="layui-input" '+
        // 				'placeholder="请输入地方详细" maxlength="150" autocomplete="off" value="'+place_detailed+'"></div>';
        // 		}else{
        // 			partyLocatedHtml += '<div class="layui-input-inline"><input type="text" id="work_unit_post" name="work_unit_post" class="layui-input" '+
        // 				'placeholder="请输入工作单位及职务" maxlength="150" autocomplete="off" value="'+work_unit_post+'"></div>&nbsp;&nbsp;&nbsp;&nbsp;';
        // 		}
        // 	}
        // 	$("#partyLocatedDiv").html(partyLocatedHtml);
        // 	form.render();
        // 	//修改时根据原先选择内容判断元素显示
        // 	var membership_credentials_mark = $("#membership_credentials_mark").val();
        // 	showParty(membership_credentials_mark);
        // });

        showMissing(is_missing);
        showFlowCommunist(is_flow_communist);
    });
    //1、加入党组织日期
    laydate.render({
        elem: '#gmt_party',
        theme: 'grid',
        trigger: 'click',
        max : 0
    });
    //2、转为正式党员日期
    laydate.render({
        elem: '#gmt_become',
        theme: 'grid',
        trigger: 'click',
        max : 0
    });

    form.on('radio(membership_credentials_id)', function(data){
        var special_mark = data.elem.getAttribute("special_mark");
        showParty(special_mark);
    });
    //4、党费缴至年月
    laydate.render({
        elem: '#gmt_paid_until',
        theme: 'grid',
        trigger: 'click',
        max : 0
    });
    //5、失去联系日期
    laydate.render({
        elem: '#gmt_missing',
        theme: 'grid',
        trigger: 'click',
        max : 0
    });
    //6、是否为失联党员
    form.on('radio(is_missing)', function(data){
        showMissing(data.value);
    });
    // $(".is_missing").hide();
    // $(".is_flow_communist").hide();
    //显示失联党员填写内容
    function showMissing(data_value){
        if(data_value=="1"){
            $(".is_missing").show();
            $(".is_missing").find("input").attr("lay-verify","required");
        }else{
            $(".is_missing").hide();
            $(".is_missing").find("input").attr("lay-verify","").val("");
        }
    }
    //7、是否为流动党员
    form.on('radio(is_flow_communist)', function(data){
        showFlowCommunist(data.value);
    });
    //显示流动党员填写内容
    function showFlowCommunist(data_value){
        if(data_value=="1"){
            $(".is_flow_communist").show();
            $(".is_flow_communist").find("input").attr("lay-verify","required");
        }else{
            $(".is_flow_communist").hide();
            $(".is_flow_communist").find("input").attr("lay-verify","").val("");
        }
    }
    //监听保存
    form.on('submit(saveUser)', function(data){
        //实体类传参必须使用application/json;charset=UTF-8 + JSON.stringify(data.field)
        common.ajax(saveRetireCommunist, 'post', 'json', JSON.stringify(data.field), function (res){
            layer.msg(res.message);
            if(res.data){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
                layer.close(index);
            }
        },"application/json;charset=UTF-8");
        return false;
    });
    //关闭
    $("#closeWindow").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
        layer.close(index);
    });
    //自定义校验
    form.verify({
        partyRadio: function (value, item) {
            var sex_id = $("input[name='membership_credentials_id']:checked").val();
            if (typeof (sex_id) == "undefined") {
                return '组织关系所在单位不能为空';
            }
        },
        money: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value) || value==''){

            }else{
                return '金额格式不正确';
            }
        },
        integer: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(value!="" && !/^[+]{0,1}(\d+)$/.test(value)){
                return '请输入正整数';
            }else{

            }
        }
    });
    //显示党组织关系所在地_填写项目
    function showParty(special_mark){
        if("1"==special_mark){  //地方
            $("#work_unit_post").attr("lay-verify","").val("").hide();
            $("#place_detailed").attr("lay-verify","required").show();
        }else{
            $("#work_unit_post").attr("lay-verify","required").show();
            //隐藏时清空所有文本框的值
            $("#place_detailed").attr("lay-verify","").val("").hide();
        }
    }
});