package com.fjhcit.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fjhcit.base.service.BaseSysCodeKindService;
import com.fjhcit.model.CacheModel;
import com.fjhcit.utils.RedisStringUtils;

/**
 * 数据字典控制器
 * @author bule
 *
 */
@RestController
@RequestMapping("/sysCodeKind")
public class BaseSysCodeKindController {
	
	@Autowired
	private BaseSysCodeKindService baseSysCodeKindService;
	@Autowired
	private RedisStringUtils redisString;
	
	/**
	 * 数据字典详细
	 * @return
	 */
	@RequestMapping(value = "/sysCodeKindInfo", method = RequestMethod.POST)
	public Object getSysCodeKindInfo(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", baseSysCodeKindService.getSysCodeKindInfo(paramMap));
		result.put("count", baseSysCodeKindService.getSysCodeKindCount(paramMap));
		result.put("code", "0");
		return result;
	}
	/**
	 * 删除数据字典
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/deleteSysCodeKind", method = RequestMethod.POST)
	public String selectSysCodeKind(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常
		try {
			//删除数据字典及对应编码明细
			baseSysCodeKindService.deleteSysCodeKind(paramMap);
			baseSysCodeKindService.deleteSysCode(paramMap);
			//删除原来的拓展属性
			baseSysCodeKindService.deleteKdCodeAddIByKdcode(paramMap.get("kdCode"));
			//删除原来的拓展属性的值
			baseSysCodeKindService.deleteCodeAddiVal(paramMap.get("kdCode"));
			return "1";
		}catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			e.printStackTrace();
			return "2";
		}
	}
	/**
	 *  编辑数据字典
	 * @return
	 */
	@RequestMapping(value = "/editSysCodeKind", method = RequestMethod.POST)
	public String editSysCodeKind(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常
		try {
			if(StringUtils.isBlank(paramMap.get("UpdaFlag"))) {
				//编码验证
				String str = baseSysCodeKindService.checkSysCodeKindCount(paramMap);
				System.out.println("新增方法!");
				if("0".equals(str)) {
					baseSysCodeKindService.addSysCodeKind(paramMap);
				}else {
					return "0";
				}
			}else{
				System.out.println("编辑方法!");
				baseSysCodeKindService.updateSysCodeKind(paramMap);
			}
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
	
	/**
	 * 数据字典明细查询
	 * @return
	 */
	@RequestMapping(value = "/sysCodeInfo", method = RequestMethod.POST)
	public Object getSysCodeInfo(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", baseSysCodeKindService.getSysCodeInfo(paramMap));
		result.put("count", baseSysCodeKindService.getSysCodeCount(paramMap));
		result.put("code", "0");
		return result;
	}
	/**
	 *  保存数据字典明细
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/updateSysCode", method = RequestMethod.POST)
	public String updateSysCode(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常3数据重复
		try {
			String code = paramMap.get("kdCode");
			String codeListStr =paramMap.get("table");
			Gson gson = new Gson();
			List<Map<String, String>> codeList=gson.fromJson(codeListStr, new TypeToken<List<Map<String, String>>>() {}.getType());
			Map<String,String> dele = new HashMap<String, String>();
			dele.put("kdCode", code);
			//删除原来的code
			baseSysCodeKindService.deleteSysCode(dele);
			//删除原来的拓展属性的值
			baseSysCodeKindService.deleteCodeAddiVal(code);
			for (Map<String, String> map : codeList) {
				baseSysCodeKindService.addSysCode(map);
				String CODE = map.get("CODE");
				String KD_CODE = map.get("KD_CODE");
				map.remove("CODE");
				map.remove("KD_CODE");
				map.remove("CODE_VAL");
				map.remove("CODE_SUPERIOR");
				map.remove("FL_AVAILABLE");
				map.remove("SEQ_DISPLAY");
				map.remove("WIDTH");
				map.remove("OPR_CODE");
				map.remove("LAY_TABLE_INDEX");
				if(map.size()>0) {
					for(String key:map.keySet()){
						Map<String, String> insertMap = new HashMap<String, String>();
						insertMap.put("CODE", CODE);
						insertMap.put("KD_CODE", KD_CODE);
						insertMap.put("KD_CODE_ADDI", key);
						insertMap.put("CODE_ADDI_VAL", map.get(key));
						baseSysCodeKindService.addCodeAddiVal(insertMap);
					}
					
				}
			}
			//更新缓存
			ArrayList<CacheModel> list = new ArrayList<CacheModel>();
			CacheModel all=new CacheModel();
	    	all.setKd_code(code);
	    	all.setName("全部");
	    	all.setValue("");
	    	list.add(all);
	    	list.addAll(baseSysCodeKindService.getCode(code));
    		if(list!=null&&list.size()>0) {
    			redisString.setKey(code, gson.toJson(list));
    		}
			return "1";
		}catch (org.springframework.dao.DuplicateKeyException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "3";
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "2";
		}
	}
	
	/**
	 *  添加一条数据字典明细
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@RequestMapping(value = "/addSysCode", method = RequestMethod.POST)
	public String addSysCode(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		try {
			baseSysCodeKindService.addSysCode(paramMap);
			String CODE = paramMap.get("CODE");
			String KD_CODE = paramMap.get("KD_CODE");
			paramMap.remove("CODE");
			paramMap.remove("CODE_SUPERIOR");
			paramMap.remove("CODE_VAL");
			paramMap.remove("FL_AVAILABLE");
			paramMap.remove("KD_CODE");
			paramMap.remove("SEQ_DISPLAY");
			if(paramMap.size()>0) {
				for(String key:paramMap.keySet()){
					Map<String, String> insertMap = new HashMap<String, String>();
					insertMap.put("CODE", CODE);
					insertMap.put("KD_CODE", KD_CODE);
					insertMap.put("KD_CODE_ADDI", key);
					insertMap.put("CODE_ADDI_VAL", paramMap.get(key));
					baseSysCodeKindService.addCodeAddiVal(insertMap);
				}
			}
			//1成功2异常3重复
			return "1";
		}catch (org.springframework.dao.DuplicateKeyException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
			return "3";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
	/**
	 *  添加一条数据字典明细拓展属性
	 * @return
	 */
	@RequestMapping(value = "/addSysCodeAddI", method = RequestMethod.POST)
	public String addSysCodeAddI(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.toString());
		try {
			baseSysCodeKindService.addSysCodeAddI(paramMap);
			//1成功2异常
			return "1";
		}catch (org.springframework.dao.DuplicateKeyException e) {
			e.printStackTrace();
			return "3";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
}
