package com.fjhcit.base.controller;


import com.fjhcit.common.kit.StringUtils;
import com.google.gson.Gson;
import com.fjhcit.base.service.AuthService;
import com.fjhcit.base.service.BaseService;
import com.fjhcit.model.CacheModel;
import com.fjhcit.model.ResultVO;
import com.fjhcit.utils.RedisStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author sunguoliang
 * @create 2018-09-25 下午2:12
 */
@RestController
@RequestMapping("/cache")
public class BaseCacheController {

    @Autowired
    private BaseService baseService;
    @Autowired
	private RedisStringUtils redisString;
    @Autowired
    private AuthService authService;
    

    /**
     * 获取数据字典数据
     * 
     * @author cz
     *
     */
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public ResultVO getCode(@RequestParam Map<String, String> params){
    	ResultVO result=new ResultVO();
    	Map<String,List<CacheModel>> map=new HashMap<String,List<CacheModel>>();
    	String temp=params.get("code");

    	if(temp==null||temp.isEmpty()) {
    		 //return result;
    		temp="EDUCATION,ISCHECK";
    	}

    	String[] st = temp.split(",");
    	for (String code : st) {
    		map.put(code, getCodeList(code));
    		//list.add(getCodeList(code));
		}
    	result.setData(map);

         return result;
    }

    /**
     * 获取级联数据
     * 
     * @author cz
     *
     */
    @RequestMapping(value = "/getCategoryCode", method = RequestMethod.POST)
    public ResultVO getCategoryCode(@RequestParam Map<String, String> params){
    	ResultVO result=new ResultVO();
    	Map<String,List<CacheModel>> map=new HashMap<String,List<CacheModel>>();
    	String temp=params.get("code");
    	
    	if(temp==null||temp.isEmpty()) {
    		 //return result;
    		temp="COMPANY,DEPARTMENT,CLASS";
    	}
    	String[] st = temp.split(",");
    	//for (String code : st) {
    		map.put(st[0], getCategoryCodeList(st));
    		//list.add(getCodeList(code));
		//}
    	
    	result.setData(map);
      
         return result;
    }
    /**
     * 递归获取子集
     * 
     * @author cz
     *
     */
	private List<CacheModel> getCategoryCodeList(String[] st) {
		redisString.delKey(st[0]);
		String temp = redisString.getValue(st[0]);
		Map<String,ArrayList<CacheModel>> tmp=new HashMap<String,ArrayList<CacheModel>>();

		Gson gson =new Gson();
		ArrayList<CacheModel> list = new ArrayList<CacheModel>();
		if(temp!=null) {   		
    		// list = gson.fromJson(temp,list.getClass());
    		 list.addAll(gson.fromJson(temp,list.getClass()));
    		 //map.put(key, value)    	 			
		}else {
			for (String code : st) {
				tmp.put(code,baseService.getCode(code));
			}
			list=generateCategoryCodeList(tmp,st);
			if(list!=null&&list.size()>0) {
    			redisString.setKey(st[0], gson.toJson(list));
    		} 
			
		}
		return list;
	}
	 /**
     * 递归获取子集
     * 
     * @author cz
     *
     */
	private ArrayList<CacheModel> generateCategoryCodeList(	Map<String,ArrayList<CacheModel>> tmp,String[] st) {
		ArrayList<CacheModel> temp=new ArrayList<CacheModel>();
		
			ArrayList<CacheModel> a=tmp.get(st[0]);
			for (CacheModel cacheModel : a) {
				cacheModel.setChildren(getChildren(tmp,cacheModel.getValue()));
				temp.add(cacheModel);
			}
			
		
		return temp;
	}
	
	private List<Object> getChildren(Map<String, ArrayList<CacheModel>> tmp, String value) {
		List<Object> children=new ArrayList<Object>(); 
		for (String code : tmp.keySet()) {
			ArrayList<CacheModel> list= tmp.get(code);
			for (CacheModel cacheModel : list) {
				if(value.equals(cacheModel.getCode_superior())) {
					cacheModel.setChildren(getChildren(tmp,cacheModel.getValue()));
					children.add(cacheModel);
					
				}
			}
			
			
		}
		
		return children;
	}
	private List<CacheModel> getCodeList(String code) {
		redisString.delKey(code);
    	String temp = redisString.getValue(code);
    	//Map<String, List<CacheModel>> map=new HashMap<String, List<CacheModel>>();
    	ArrayList<CacheModel> list = new ArrayList<CacheModel>();
//    	CacheModel all=new CacheModel();
//    	all.setKd_code(code);
//    	all.setName("全部");
//    	all.setValue("");
//    	list.add(all);
    	Gson gson =new Gson();
    	if(temp!=null) {
    		
    		// list = gson.fromJson(temp,list.getClass());
    		 list.addAll(gson.fromJson(temp,list.getClass()));
    		 //map.put(key, value)
    	}else {
    		list.addAll(baseService.getCode(code));
    		if(list!=null&&list.size()>0) {
    			redisString.setKey(code, gson.toJson(list));
    		}    		    		
    	}
		return list;
	}
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResultVO addUser(@RequestParam Map<String, String> params){
    	ResultVO result=new ResultVO();
    	if(params.get("id")==null||"".equals(params.get("id"))) {
        	baseService.addUser(params);

    	}else {
        	baseService.updateUser(params);

    	}
    	
    	result.setData(null);
    	result.setMessage("成功");
    	  return result;
    }
    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public ResultVO removeUser(@RequestParam Map<String, String> params){
    	ResultVO result=new ResultVO();
    	String id=params.get("ids");
    	
    	String[] tmp = id.split(",");
    	baseService.removeUser(tmp);
    	
    	result.setData(null);
    	result.setMessage("成功");
    	  return result;
    }
  
  
   
    

}