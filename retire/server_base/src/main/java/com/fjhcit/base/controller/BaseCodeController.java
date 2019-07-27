package com.fjhcit.base.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.model.ResultVO;
import com.fjhcit.utils.RedisStringUtils;
import com.fjhcit.entity.BaseCodeDO;
import com.fjhcit.base.service.BaseCodeService;

/**
 * @description 基础管理_代码设置_控制器
 * @author 陈麟
 * @date 2019年06月05日 上午09:57:20
 */
@RestController
@RequestMapping("/baseCode")
public class BaseCodeController{
	@Autowired
	private BaseCodeService		baseCodeService;	//基础管理_代码设置_业务接口
    @Autowired
	private RedisStringUtils	redisString;		//redis工具类

	/**
	 * @description 查基础管理_代码设置列表数据
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/listBaseCode", method = RequestMethod.POST)
	public ResultVO listBaseCode(@RequestParam Map<String, String> param){
    	List<BaseCodeDO> baseCodeList = new ArrayList<BaseCodeDO>();
    	String parent_id = param.get("parent_id");
    	String special_mark = param.get("special_mark");
    	String unequal_special_mark = param.get("unequal_special_mark");
    	//this.redisString.delKey("BaseCode_" + parent_id);
    	String baseCodeJson = this.redisString.getValue("BaseCode_" + parent_id);
    	if(!StringUtils.isEmpty(special_mark)) {
    		//this.redisString.delKey("BaseCode_" + parent_id + "_" + special_mark);
    		baseCodeJson = this.redisString.getValue("BaseCode_" + parent_id + "_" + special_mark);
    	}else if(!StringUtils.isEmpty(unequal_special_mark)) {
    		//this.redisString.delKey("BaseCode_" + parent_id + "_unequal_" + unequal_special_mark);
    		baseCodeJson = this.redisString.getValue("BaseCode_" + parent_id + "_unequal_" + unequal_special_mark);
    	}
    	Gson gson = new Gson();
    	if(StringUtils.isNotEmpty(baseCodeJson) && !"[]".contentEquals(baseCodeJson)) {
    		baseCodeList.addAll(gson.fromJson(baseCodeJson,baseCodeList.getClass()));
    	}else {
    		baseCodeList = this.baseCodeService.listBaseCode(param);
    		if(!StringUtils.isEmpty(special_mark)) {
    			this.redisString.setKey("BaseCode_" + parent_id + "_" + special_mark, gson.toJson(baseCodeList));
    		}else if(!StringUtils.isEmpty(unequal_special_mark)) {
    			this.redisString.setKey("BaseCode_" + parent_id + "_unequal_" + unequal_special_mark, gson.toJson(baseCodeList));
    		}else {
    			this.redisString.setKey("BaseCode_" + parent_id, gson.toJson(baseCodeList));
    		}
    	}
        return new ResultVO(baseCodeList,true,"查询成功");
	}

	/**
	 * @description 查基础管理_代码设置分页列表数据
	 */
    @RequestMapping(value = "/listBaseCodeByPaging", method = RequestMethod.POST)
	public ResultVO listBaseCodeByPaging(@RequestParam Map<String, String> param){
    	ResultVO result=new ResultVO();
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int page=Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize=Integer.parseInt(param.get("pageSize"));//每页条数
    	PageHelper.startPage(page , pageSize);
		List<BaseCodeDO> personList = this.baseCodeService.listBaseCode(param);
        PageInfo<BaseCodeDO> personPageInfo = new PageInfo<BaseCodeDO>(personList);
    	result.setData(personPageInfo);
    	result.setMessage("成功");
    	result.setSuccess(true);
        return result;
	}

	/**
	 * @description 保存基础管理_代码设置数据（新增、修改）
	 */
    @RequestMapping(value = "/saveBaseCode", method = RequestMethod.POST)
	public ResultVO saveBaseCode(@RequestBody BaseCodeDO baseCodeDO) {
    	ResultVO result = new ResultVO();
    	baseCodeDO.setModified_user_id("-1");
    	if(StringUtils.isEmpty(baseCodeDO.getKd_code())) {
    		this.baseCodeService.insertBaseCode(baseCodeDO);
    	}else {
    		this.baseCodeService.updateBaseCode(baseCodeDO);
    	}
    	result.setData(null);
    	result.setMessage("成功");
    	result.setSuccess(true);
        return result;
	}

	/**
	 * @description 删除基础管理_代码设置数据
	 */
    @RequestMapping(value = "/removeBaseCode", method = RequestMethod.POST)
	public ResultVO removeBaseCode(@RequestParam Map<String, Object> param){
    	ResultVO result = new ResultVO();
    	String ids = (String)param.get("ids");
    	String[] idsArr = ids.split(",");
    	param.put("idsArr",idsArr);
		this.baseCodeService.removeBaseCodeByIdsArr(param);
    	result.setData(null);
    	result.setMessage("成功");
    	result.setSuccess(true);
        return result;
	}

    /**
	 * @description 查找基础管理_代码设置ID数组
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getBaseCodeIdArray", method = RequestMethod.POST)
	public ResultVO getBaseCodeIdArray(@RequestParam Map<String, String> param){
		//千万不要用List<Integer> 装，redis会转成double  100变成100.0
    	List<String> dataList = new ArrayList<String>();
    	String parent_id = param.get("parent_id");
    	String baseCodeJson = this.redisString.getValue("BaseCodeIDArray_" + parent_id);
    	Gson gson = new Gson();
    	if(StringUtils.isNotEmpty(baseCodeJson) && !"[]".contentEquals(baseCodeJson)) {
    		dataList.addAll(gson.fromJson(baseCodeJson,dataList.getClass()));
    	}else {
    		dataList.addAll(Arrays.asList(this.baseCodeService.getBaseCodeIdArray(param)));
    		this.redisString.setKey("BaseCodeIDArray_" + parent_id, gson.toJson(dataList));
    	}
    	return new ResultVO(dataList, true, "查询成功");
	}
}