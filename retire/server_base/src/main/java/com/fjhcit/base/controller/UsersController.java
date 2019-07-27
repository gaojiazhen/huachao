package com.fjhcit.base.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.fjhcit.base.dao.BaseSysCodeKindDAO;
import com.fjhcit.base.service.AuthService;
import com.fjhcit.base.service.BaseService;
import com.fjhcit.base.service.BaseSysCodeKindService;
import com.fjhcit.base.service.BaseUserControlService;
import com.fjhcit.common.kit.Aes;
import com.fjhcit.entity.BaseUserDO;
import com.fjhcit.model.CacheModel;
import com.fjhcit.model.ResultVO;
import com.fjhcit.model.Users;
import com.fjhcit.utils.RedisStringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunguoliang
 * @create 2018-09-25 下午2:12
 */
@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private BaseSysCodeKindDAO baseSysCodeKindService;

    @Autowired
    private BaseService baseService;
    @Autowired
	private RedisStringUtils redisString;
    @Autowired
    private AuthService authService;
    

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public List<Users> findAll(){
       
        return baseService.findAll();
    }
    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    public ResultVO findUser(@RequestParam Map<String, String> params){
    	ResultVO result=new ResultVO();
    	if(params.get("pageNum")==null){
    		params.put("pageNum", "1");
    	}
    	if(params.get("pageSize")==null){
    		params.put("pageSize", "10");
    	}
    	 int page=Integer.parseInt(params.get("pageNum"));//当前页
         int pageSize=Integer.parseInt(params.get("pageSize"));//页面接收数据大小
        // params.put("name", "张三");
    	//result.setData(baseService.findUser(params));
         PageHelper.startPage(page , pageSize);
         List<Map> personList = baseService.findUser(params);
         PageInfo<Map> personPageInfo = new PageInfo<>(personList);


     	//Page<Map> page2=new Page<>(page,pageSize);
     //	Page temp = baseService.findUserbypage(params,page2);
    	result.setData(personPageInfo);
    	result.setMessage("成功");
    	result.setSuccess(true);;
      
         return result;
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
    @RequestMapping(value = "/signOut", method = RequestMethod.POST)
    public ResultVO signOut(@RequestParam Map<String, String> params){
    	try {
    		String loginName=	params.get("loginName");
    	    String UserNameToken = loginName+ "token";
    	    redisString.delKey(UserNameToken);
    	    redisString.delKey(loginName + "menu");
    	    redisString.delKey(loginName + "DataRange");
		} catch (Exception e) {
			// TODO: handle exception
		}
    

		return null;
    	
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVO login(String userName,String pwd) throws Exception{
    	ResultVO result=new ResultVO();
    	String pwdDecrypt = Aes.aesDecrypt(pwd);
    	BaseUserDO user= baseService.login(userName,pwdDecrypt);
    	if(user!=null) {
    		if("1".equals(user.getState())) {
    			result.setData(user);
            	result.setMessage("用户已禁用!");
            	result.setSuccess(false);
    		}else if("2".equals(user.getState())) {
    			result.setData(user);
            	result.setMessage("用户已锁定!");
            	result.setSuccess(false);
	    	}else {
		    	List<Map<String,String>> muneList=baseService.getMuneListByUserId(user.getId());
		    	List<String> dataRangeList=baseService.getDataRangetByUserId(user.getId());
		    	String compactJws = authService.generateJwt(user);
		    	//String compactJws ="123";
		    	   //将token存在redis里
		        String UserNameToken = user.getLogin_name() + "token";
		        redisString.delKey(UserNameToken);
		        String temp = redisString.getValue(UserNameToken);
		
		        redisString.setKey(UserNameToken, compactJws);
		        temp = redisString.getValue(UserNameToken);
		        //设置redis里面的数据失效时间为半小时
		
		        redisString.expire(UserNameToken,1800 ,TimeUnit.SECONDS);
		        //将菜单数据导入redis
		        String userNamemune = user.getLogin_name() + "menu";
		        Gson gs = new Gson();
		        System.out.println(gs.toJson(muneList));
		        redisString.delKey(userNamemune);
		        redisString.setKey(userNamemune, gs.toJson(muneList));
		        String userDataRange = user.getLogin_name() + "DataRange";
		        redisString.delKey(userDataRange);
		
		        redisString.setKey(userDataRange, gs.toJson(dataRangeList));
		
		        //stringRedisTemplate.expire(UserNameToken,1800,TimeUnit.SECONDS);
		
		        //redisString.setKey("token",UserNameToken");
		        
		    	user.setToken(compactJws);
		    	result.setData(user);
		    	result.setMessage("成功");
		    	result.setSuccess(true);
		    	
		    	//判断登录时间是否超过预期
		    	String dateuStr =user.getLast_login_time();
		    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		    	Date dateu = sf.parse(dateuStr);
		    	Date toDay=new Date();
		    	if(dateu==null) {
		    		//如果是第一次登录需要重置密码
		    		user.setTimeFlag(true);
		    	}else {
			    	long lastLoginTimeDay = (toDay.getTime()-dateu.getTime())/3600/24/1000;
			    	//改成从数据库取 LIMIT_LOGIN_TIME
			    	Map<String,String> paramMap = new HashMap<String, String>();
			    	paramMap.put("kdCode", "LIMIT_LOGIN_TIME");
			    	List<Map<String, Object>> list = baseSysCodeKindService.getSysCodeInfo(paramMap);
			    	String limitLoginTime = (String) list.get(0).get("CODE");
			    	if(lastLoginTimeDay>Integer.parseInt(limitLoginTime)) {
			    		//日期超过预期,需要重置密码
			    		user.setTimeFlag(true);
			    	}
		    	}
		    	//插入登录时间
		    	Map<String,Object> param = new HashMap<String, Object>();
		    	param.put("toDay", toDay);
		    	param.put("userId", user.getId());
		    	baseService.updateLastLoginTimeByUserId(param);
	    	}
    		//清除登录错误次数限制
	    	redisString.delKey(userName+"errorIndex");
    	}else {
    		//用户登录错误次数限制
    		String redisUserIndex= redisString.getValue(userName+"errorIndex");
    		if(redisUserIndex!=null) {
    			Integer index  = Integer.parseInt(redisUserIndex)+1;
    			//登录错误次数改成从数据库取 USER_PASSWORD_ERRORS_NUMBER
    			Map<String,String> paramMap = new HashMap<String, String>();
		    	paramMap.put("kdCode", "USER_PASSWORD_ERRORS_NUMBER");
		    	List<Map<String, Object>> list = baseSysCodeKindService.getSysCodeInfo(paramMap);
		    	String str = (String) list.get(0).get("CODE");
    			Integer errorsIndex  = Integer.parseInt(str);
    			if(index>errorsIndex) {
    				//根据用户名改变用户状态2为锁定
    				baseService.updateStateByUserName(userName,"2");
    			}
    			redisString.setKey(userName+"errorIndex", index.toString());
    		}else {
    			redisString.setKey(userName+"errorIndex", "1");
    		}
    		//user.setToken(compactJws);
        	result.setData(user);
        	result.setMessage("用户名或者密码错误!");
        	result.setSuccess(false);
    	}
        return result;
    }
    @RequestMapping(value = "/getMuenByUserId", method = RequestMethod.POST)
    public ResultVO getMuenByUserId(@RequestParam Map<String, String> params){
    	ResultVO result=new ResultVO();

    	List<Map> list = baseService.findMenu(params);
      	result.setData(list);
    	result.setMessage("成功");
    	result.setSuccess(true);
		return result;
    
    }
    @RequestMapping(value = "/findRole", method = RequestMethod.POST)
    public List<Map> findRole(){
       
        return baseService.findRole();
    }
  
    private  CharSequence getLast(String uri) {
		String[] temp = uri.split("/");
		
		return temp[temp.length-1];
	}
    

}