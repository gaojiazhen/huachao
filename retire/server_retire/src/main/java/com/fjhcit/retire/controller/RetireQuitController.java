package com.fjhcit.retire.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fjhcit.model.RetireQuitExcelVO;
import com.fjhcit.entity.RetireQuitDO;
import com.fjhcit.retire.service.RetireQuitService;
import com.fjhcit.utils.POIExcelUtils;

/**
 * @description 离（退）休人员_离休人员库_控制器
 * @author 陈麟
 * @date 2019年06月28日 上午09:19:27
 */
@RestController
@RequestMapping("/retireQuit")
public class RetireQuitController{
	@Autowired
	private RetireQuitService		retireQuitService;  		//离（退）休人员_离休人员库_业务接口

	/**
	 * @description 查找信息库管理_离休人员登记_列表数据
	 */
    @RequestMapping(value = "/listRetireQuitUser", method = RequestMethod.POST)
	public ResultVO listRetireQuitUser(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));		//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetireQuitDO> personList = this.retireQuitService.listRetireQuitUser(param);
    	return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}
	
	/**
	 * @description 查离（退）休人员_离休人员库列表数据
	 */
    @RequestMapping(value = "/listRetireQuit", method = RequestMethod.POST)
	public ResultVO listRetireQuit(@RequestParam Map<String, String> param){
    	return new ResultVO(this.retireQuitService.listRetireQuit(param),true,"查询成功");
	}

	/**
	 * @description 查离（退）休人员_离休人员库分页列表数据
	 */
    @RequestMapping(value = "/listRetireQuitByPaging", method = RequestMethod.POST)
	public ResultVO listRetireQuitByPaging(@RequestParam Map<String, String> param){
    	if(StringUtils.isEmpty(param.get("pageNum"))){
    		param.put("pageNum", "1");
    	}
    	if(StringUtils.isEmpty(param.get("pageSize"))){
    		param.put("pageSize", "10");
    	}
    	int pageNum = Integer.parseInt(param.get("pageNum"));		//当前页
    	int pageSize = Integer.parseInt(param.get("pageSize"));//每页条数
    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);
		List<RetireQuitDO> personList = this.retireQuitService.listRetireQuit(param);
    	return new ResultVO(personList, true, "查询成功", p.getTotal(), pageNum, pageSize);
	}

	/**
	 * @description 保存离（退）休人员_离休人员库数据（新增、修改）
	 */
    @RequestMapping(value = "/saveRetireQuit", method = RequestMethod.POST)
	public ResultVO saveRetireQuit(@RequestBody RetireQuitDO retireQuitDO) {
    	ResultVO result;
    	try {
    		if(StringUtils.isEmpty(retireQuitDO.getId())) {
    			retireQuitDO.setCreate_user_id(retireQuitDO.getModified_user_id());
    			this.retireQuitService.insertRetireQuit(retireQuitDO);
    		}else {
    			this.retireQuitService.updateRetireQuit(retireQuitDO);
    		}
    		result = new ResultVO(true,true,"保存成功");
		} catch (Exception e) {
    		result = new ResultVO(false,false,"保存失败");
        	e.printStackTrace();
		}
        return result;
	}

	/**
	 * @description 删除离（退）休人员_离休人员库数据
	 */
    @RequestMapping(value = "/removeRetireQuit", method = RequestMethod.POST)
	public ResultVO removeRetireQuit(@RequestParam Map<String, String> param){
    	ResultVO result;
    	try {
    		String ids = (String)param.get("ids");
    		String[] idsArr = ids.split(",");
    		//删除
    		Map<String,Object> delParam = new HashMap<String, Object>();
    		delParam.put("idsArr",idsArr);
			this.retireQuitService.removeRetireQuitByIdsArr(delParam);
    		result = new ResultVO(true,true,"删除成功");
    	} catch (Exception e) {
    		result = new ResultVO(false,false,"删除失败");
        	e.printStackTrace();
        }
        return result;
	}
    
    /**
	 * @description 离休人员登记_导入Excel
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/saveRetireQuitByExcel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveRetireQuitByExcel(@RequestParam("file") MultipartFile file,@RequestParam Map<String, Object> param) {
		Map<String,String> retMap = new HashMap<>();
		try {
			//上传附件到Minio服务器
//			String fileName = file.getOriginalFilename();
//			String contentType = file.getContentType();
//			InputStream inputStram = file.getInputStream();
//			return MinioUtil.uploadfile(inputStram, fileName, contentType);
			POIExcelUtils poi = new POIExcelUtils();
			List<Object> dataList = poi.importExcel(file,new RetireQuitExcelVO(),2,1);
			param.put("dataList", dataList);
			retMap = this.retireQuitService.saveRetireQuitByExcel(param);
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