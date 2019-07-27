//封装ajax提交模块
layui.define(['jquery', 'layer'], function(exports) {
	var $ = layui.jquery;
	var layer = parent.layer === undefined ? layui.layer : top.layer;
	var obj = {
		ajax: function(url, type, dataType, data, callback, contentType) {
			var temp = new Object();
			var token = sessionStorage.getItem("token");
			if (token) { // 判断是否存在token，如果存在的话，则每个http header都加上token
				temp['token'] = token;
			}
			var timestamp = Date.parse(new Date());
			temp['timestamp'] = timestamp;
			var nonce = Math.round(Math.random() * 1000) + timestamp;
			nonce = doubleEncrypt(nonce);
			temp['nonce'] = nonce;
			var sign = data + '&' + nonce + timestamp;
			sign = doubleEncrypt(sign);
			temp['sign'] = sign;
			//提交类型
			if (null == contentType || "" == contentType) {
				contentType = "application/x-www-form-urlencoded";
			}
			//IE8 默认不支持 CORS 请求，需要手动开启
			$.support.cors = true;
			$.ajax({
				url: url,
				type: type,
				//实体类传参必须使用application/json;charset=UTF-8,默认是application/x-www-form-urlencoded
				contentType: contentType,
				headers: temp,
				beforeSend: function(request) {
					layer.load();
				},
				complete: function() {
					layer.closeAll('loading');
				},
				dataType: dataType,
				data: data,
				success: callback
			});
		},
	};
	//输出接口
	exports('common', obj);
});

/**
 * 获取组装组织机构下拉框html拼接语句
 * @param {Object} list
 * @param {Object} selectedValue
 */
function getDepartmentHtml(list, selectedValue) {
	var proHtml = '';
	if (!selectedValue) {
		selectedValue = "";
	}
	for (var i = 0; i < list.length; i++) {
		if (selectedValue == list[i].id) {
			proHtml += '<option value="' + list[i].id + '" selected >' + list[i].department_name + '</option>';
		} else {
			proHtml += '<option value="' + list[i].id + '" >' + list[i].department_name + '</option>';
		}
	}
	return proHtml;
}

/**
 * 获组装代码数据单选框html拼接语句
 * @param {Object} list
 * @param {Object} selectedValue
 */
function getDepartmentHtmlByRadio(list, radioName, layVerify) {
	var proHtml = '';
	var checkedValue = "";
	var element = document.getElementById(radioName);
	if(element){
		checkedValue = element.value;
	}
	for (var i = 0; i < list.length; i++) {
		if (checkedValue == list[i].value || list.length==1) {
			proHtml += '<input type="radio" lay-verify="' + layVerify + '" name="' + radioName + '" value="' + list[i].value + '" title="' +
				list[i].name + '" special_mark="' + list[i].special_mark + '" lay-filter="' + radioName + '" checked >';
		} else {
			proHtml += '<input type="radio" lay-verify="' + layVerify + '" name="' + radioName + '" value="' + list[i].value + '" title="' + 
				list[i].name + '" special_mark="' + list[i].special_mark + '" lay-filter="' + radioName + '">';
		}
	}
	return proHtml;
}

/**
 * 获组装代码数据下拉框html拼接语句
 * @param {Object} list
 * @param {Object} selectedValue
 */
function getCodeHtmlByOption(list, selectedValue) {
	var proHtml = '';
	if (!selectedValue) {
		selectedValue = "";
	}
	if(list!=null && list!=""){
		for (var i = 0; i < list.length; i++) {
			if (selectedValue == list[i].value) {
				proHtml += '<option value="' + list[i].value + '" special_mark="' + list[i].special_mark + '" selected >' + 
					list[i].name + '</option>';
			} else {
				proHtml += '<option value="' + list[i].value + '" special_mark="' + list[i].special_mark + '" >' + 
					list[i].name + '</option>';
			}
		}
	}
	return proHtml;
}
/**
 * 获组装数据字典多选框html拼接语句
 * @param {Object} list
 * @param {Object} selectedValue
 */
function getDepartmentHtmlByCheckbox(list, inputName, ids) {
	var proHtml = '';
	if (!ids) {
		ids = "";
	}
	for (var i = 0; i < list.length; i++) {
		if (ids != "" && ("," + ids + ",").indexOf("," + list[i].value + ",") != -1) {
			proHtml += '<input type="checkbox" lay-verify="required" name="' + inputName + 
				'" value="' + list[i].value + '" title="' + list[i].name + '" special_mark="' +
				list[i].special_mark + '" lay-filter="' + inputName + '" checked >';
		} else {
			proHtml += '<input type="checkbox" lay-verify="required" name="' + inputName + 
				'" value="' + list[i].value + '" title="' + list[i].name + '" special_mark="' +
				list[i].special_mark + '" lay-filter="' + inputName + '">';
		}
	}
	return proHtml;
}
/**
 * 重新组装数组
 * @param {Object} formArr ：serializeArray() 方法通过序列化表单值来创建对象数组（名称和值）
 */
function getformObj(formArr) {
	var obj = new Object;
	for (var item in formArr) {
		obj[formArr[item].name] = formArr[item].value;
	}
	return obj;
}
/**
 * 特殊字符转义
 * @param {Object} str 
 */
function filter(str) {
	str += ''; // 隐式转换
	str = str.replace(/%/g, '%25');
	str = str.replace(/\+/g, '%2B');
	str = str.replace(/ /g, '%20');
	str = str.replace(/\//g, '%2F');
	str = str.replace(/\?/g, '%3F');
	str = str.replace(/&/g, '%26');
	str = str.replace(/\=/g, '%3D');
	str = str.replace(/#/g, '%23');
	return str;
}
/**
 * 获取某个参数的值
 * @param {Object} name ：字段名称
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg); //search,查询？后面的参数，并匹配正则
	if (r != null) {
		return unescape(r[2]);  //使用unescape对参数解码
	} else {
		return null;
	}
}

function getCodeHtml(list,value){
	var proHtml='';
	if(!value){
		value=""
	}
	for ( var i=0; i<list.length;i++){	
	if(value==list[i].value){
		proHtml+='<option value="'+list[i].value+'" selected >'+list[i].name+'</option>';
	}else{
		proHtml+='<option value="'+list[i].value+'" >'+list[i].name+'</option>';
	}
	}	 
	return proHtml;
}

function formateObjToParamStr(paramObj,) {
  const sdata = [];
  for (let attr in paramObj) {
    sdata.push(`${attr}=${filter(paramObj[attr])}`);
  }
  return sdata.join('&');
};

function getUuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
    var uuid = s.join("");
    return uuid;
}