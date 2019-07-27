package com.fjhcit.retire.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.RetireCommunistExcelVO;
import com.fjhcit.entity.RetireCommunistDO;
import com.fjhcit.entity.RetireFamilyDO;
import com.fjhcit.entity.RetireQuitDO;
import com.fjhcit.entity.RetireUserDO;
import com.fjhcit.retire.service.RetireCommunistService;
import com.fjhcit.retire.service.RetireFamilyService;
import com.fjhcit.retire.service.RetireQuitService;
import com.fjhcit.retire.service.RetireUserService;
import com.fjhcit.utils.POIExcelUtils;

/**
 * @description 离（退）休人员_中共党员基本情况登记表_控制器
 * @author 陈麟
 * @date 2019年06月11日 下午20:53:24
 */
@RestController
@RequestMapping("/retireCommunist")
public class RetireCommunistController{
	@Autowired
	private RetireCommunistService	retireCommunistService;  	//离（退）休人员_中共党员基本情况登记表_业务接口
	@Autowired
	private RetireUserService		retireUserService;  		//离（退）休人员基本信息库_业务接口
	@Autowired
	private RetireFamilyService		retireFamilyService;  		//离（退）休人员_家庭主要成员表_业务接口
	@Autowired
	private RetireQuitService		retireQuitService;  		//离（退）休人员_离休人员库_业务接口

	/**
	 * @description 查离（退）休人员_中共党员基本情况登记表列表数据
	 */
    @RequestMapping(value = "/listRetireCommunist", method = RequestMethod.POST)
	public ResultVO listRetireCommunist(@RequestParam Map<String, String> param){
    	return new ResultVO(this.retireCommunistService.listRetireCommunist(param),true,"查询成功");
	}

	/**
	 * @description 查离（退）休人员_中共党员基本情况登记表分页列表数据
	 */
    @RequestMapping(value = "/listRetireCommunistByPaging", method = RequestMethod.POST)
	public ResultVO listRetireCommunistByPaging(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));		//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetireCommunistDO> personList = this.retireCommunistService.listRetireCommunist(param);
        return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}
    
    /**
	 * @description 根据user_id查离（退）休人员基本信息和中共党员基本情况登记表信息
	 */
    @RequestMapping(value = "/getRetireCommunistByUserid", method = RequestMethod.POST)
	public ResultVO getRetireCommunistByUserid(@RequestParam Map<String, String> param){
    	String userid = param.get("user_id");
    	//1、人员基本信息
    	RetireUserDO retireUserDO = this.retireUserService.getRetireUserDOById(userid);
    	//2、离休人员信息
    	RetireQuitDO retireQuitDO = this.retireQuitService.getRetireQuitDOByUserid(userid);
    	//3、中共党员登记信息
    	RetireCommunistDO retireCommunistDO = this.retireCommunistService.getRetireCommunistDOByUserid(userid);
    	//4、家庭主要成员（现在人员基本信息填写页使用）
    	Map<String,String> familyParam = new HashMap<String, String>();
    	familyParam.put("user_id", userid);
    	List<RetireFamilyDO> retireFamilyList = this.retireFamilyService.listRetireFamily(familyParam);
    	//返回结果
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("retireUserDO", retireUserDO);
    	resultMap.put("retireQuitDO", retireQuitDO);
    	resultMap.put("retireCommunistDO", retireCommunistDO);
    	resultMap.put("retireFamilyList", retireFamilyList);
        return new ResultVO(resultMap, true, "查询成功");
	}

	/**
	 * @description 保存离（退）休人员_中共党员基本情况登记表数据（新增、修改）
	 */
	@Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/saveRetireCommunist", method = RequestMethod.POST)
	public ResultVO saveRetireCommunist(@RequestBody RetireCommunistDO retireCommunistDO) {
    	ResultVO result;
    	try {
    		//修改中共党员信息
			if(StringUtils.isEmpty(retireCommunistDO.getId())) {
				this.retireCommunistService.insertRetireCommunist(retireCommunistDO);
			}else {
				this.retireCommunistService.updateRetireCommunist(retireCommunistDO);
			}
			result = new ResultVO(true,true,"保存成功");
		} catch (Exception e) {
			result = new ResultVO(false,false,"保存失败");
			e.printStackTrace();
		}
    	return result;
	}

	/**
	 * @description 删除离（退）休人员_中共党员基本情况登记表数据
	 */
    @RequestMapping(value = "/removeRetireCommunist", method = RequestMethod.POST)
	public ResultVO removeRetireCommunist(@RequestParam Map<String, String> param){
    	ResultVO result;
    	try {
    		String ids = (String)param.get("ids");
    		String[] idsArr = ids.split(",");
    		//删除
			Map<String,Object> delParam = new HashMap<String, Object>();
			delParam.put("idsArr",idsArr);
			this.retireCommunistService.removeRetireCommunistByIdsArr(delParam);
    		result = new ResultVO(true,true,"删除成功");
    	} catch (Exception e) {
    		result = new ResultVO(false,false,"删除失败");
        	e.printStackTrace();
        }
        return result;
	}
    
    /**
	 * @description 中共党员登记_导入Excel
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/saveRetireCommunistByExcel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveRetireCommunistByExcel(@RequestParam("file") MultipartFile file) {
		Map<String,String> retMap = new HashMap<>();
		try {
			//上传附件到Minio服务器
//			String fileName = file.getOriginalFilename();
//			String contentType = file.getContentType();
//			InputStream inputStram = file.getInputStream();
//			return MinioUtil.uploadfile(inputStram, fileName, contentType);
			POIExcelUtils poi = new POIExcelUtils();
			List<Object> dataList = poi.importExcel(file,new RetireCommunistExcelVO(),2,1);
			retMap = this.retireCommunistService.saveRetireCommunistByExcel(dataList);
		} catch (Exception e) {
			if("当前Excel表和模板不匹配！".equals(e.getMessage())) {
				retMap.put("errorMessageHtml", e.getMessage());
			}else {
				retMap.put("errorMessageHtml", "Excel解析错误！");
			}
			e.printStackTrace();
		}
		return retMap;
	}
}