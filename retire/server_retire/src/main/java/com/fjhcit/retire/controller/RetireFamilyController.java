package com.fjhcit.retire.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.model.ResultVO;
import com.fjhcit.entity.RetireFamilyDO;
import com.fjhcit.retire.service.RetireFamilyService;

/**
 * @description 离（退）休人员_家庭主要成员表_控制器
 * @author 陈麟
 * @date 2019年06月13日 上午11:02:09
 */
@RestController
@RequestMapping("/retireFamily")
public class RetireFamilyController{
	@Autowired
	private RetireFamilyService		retireFamilyService;  		//离（退）休人员_家庭主要成员表_业务接口

	/**
	 * @description 查离（退）休人员_家庭主要成员表列表数据
	 */
    @RequestMapping(value = "/listRetireFamily", method = RequestMethod.POST)
	public ResultVO listRetireFamily(@RequestParam Map<String, String> param){
    	return new ResultVO(this.retireFamilyService.listRetireFamily(param),true,"查询成功");
	}

	/**
	 * @description 查离（退）休人员_家庭主要成员表分页列表数据
	 */
    @RequestMapping(value = "/listRetireFamilyByPaging", method = RequestMethod.POST)
	public ResultVO listRetireFamilyByPaging(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetireFamilyDO> personList = this.retireFamilyService.listRetireFamily(param);
    	return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}

	/**
	 * @description 保存离（退）休人员_家庭主要成员表数据（新增、修改）
	 */
    @RequestMapping(value = "/saveRetireFamily", method = RequestMethod.POST)
	public ResultVO saveRetireFamily(@RequestBody RetireFamilyDO retireFamilyDO) {
    	ResultVO result;
    	try {
    		if(StringUtils.isEmpty(retireFamilyDO.getId())) {
    			this.retireFamilyService.insertRetireFamily(retireFamilyDO);
    		}else {
    			this.retireFamilyService.updateRetireFamily(retireFamilyDO);
    		}
    		result = new ResultVO(true,true,"保存成功");
		} catch (Exception e) {
    		result = new ResultVO(false,false,"保存失败");
        	e.printStackTrace();
		}
        return result;
	}

	/**
	 * @description 删除离（退）休人员_家庭主要成员表数据
	 */
    @RequestMapping(value = "/removeRetireFamily", method = RequestMethod.POST)
	public ResultVO removeRetireFamily(@RequestParam Map<String, String> param){
    	ResultVO result;
    	try {
    		String ids = (String)param.get("ids");
    		String[] idsArr = ids.split(",");
    		//删除
    		Map<String,Object> delParam = new HashMap<String, Object>();
    		delParam.put("idsArr",idsArr);
			this.retireFamilyService.removeRetireFamilyByIdsArr(delParam);
    		result = new ResultVO(true,true,"删除成功");
    	} catch (Exception e) {
    		result = new ResultVO(false,false,"删除失败");
        	e.printStackTrace();
        }
        return result;
	}
}