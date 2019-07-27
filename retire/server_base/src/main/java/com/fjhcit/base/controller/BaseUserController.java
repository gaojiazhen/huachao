package com.fjhcit.base.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.fjhcit.common.kit.Aes;
import com.fjhcit.common.kit.CommonUtils;
import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.model.ResultVO;
import com.fjhcit.utils.RedisStringUtils;
import com.fjhcit.entity.BaseUserDO;
import com.fjhcit.base.dao.BaseSysCodeKindDAO;
import com.fjhcit.base.enums.AccountEnum;
import com.fjhcit.base.service.AuthService;
import com.fjhcit.base.service.BaseUserService;

/**
 * @description 基础管理_登录人员表_控制器
 * @author 陈麟
 * @date 2019年06月03日 上午08:14:20
 */
@RestController
@RequestMapping("/baseUser")
public class BaseUserController {
	@Autowired
	private BaseUserService baseUserService;	// 基础管理_登录人员表_业务接口
	@Autowired
	private RedisStringUtils redisString;	// Redis工具类
	@Autowired
	private AuthService authService;		// 认证令牌_业务接口
	@Autowired
	private BaseSysCodeKindDAO baseSysCodeKindService;

	/**
	 * @description 验证登录信息
	 * @param userName
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResultVO saveLogin(String userName, String pwd) throws Exception {
		ResultVO result = new ResultVO();
		String pwdDecrypt = Aes.aesDecrypt(pwd);
		Map<String, String> login_param = new HashMap<String, String>();
		login_param.put("login_name", userName);
		login_param.put("login_password", pwdDecrypt);
		BaseUserDO userDO = this.baseUserService.getLoginUser(login_param);
		if (userDO != null) {
			// 账号状态（1启用 -13锁定（连续登录错误） -15锁定（越权） -17锁定（管理员） -41休眠 -99注销）
			if ((AccountEnum.ACCSTATUS_LOCKED.getKey() + "").equals(userDO.getState())
					|| (AccountEnum.ACCSTATUS_ULOCKED.getKey() + "").equals(userDO.getState())
					|| (AccountEnum.ACCSTATUS_SLOCKED.getKey() + "").equals(userDO.getState())) {
				result.setData(userDO);
				result.setMessage("用户已锁定！");
				result.setSuccess(false);
			} else if ((AccountEnum.ACCSTATUS_SLEEP.getKey() + "").equals(userDO.getState())) {
				result.setData(userDO);
				result.setMessage("用户已休眠！");
				result.setSuccess(false);
			} else if ((AccountEnum.ACCSTATUS_CANCEL.getKey() + "").equals(userDO.getState())) {
				result.setData(userDO);
				result.setMessage("用户已注销！");
				result.setSuccess(false);
			} else {
				// 生成token
				String compactJws = this.authService.generateJwt(userDO);
				// 查登录人员权限菜单
				Map<String, String> param = new HashMap<String, String>();
				param.put("id", userDO.getId());
				List<Map<String, Object>> muneList = this.baseUserService.listMenuByLoginUser(param);
				// 将token存在redis里
				String UserNameToken = userDO.getLogin_name() + "token";
				redisString.delKey(UserNameToken);
				redisString.setKey(UserNameToken, compactJws);
				// 设置redis里面的数据失效时间为半小时
				redisString.expire(UserNameToken, 1800, TimeUnit.SECONDS);
				// 将菜单数据导入redis
				String userNamemune = userDO.getLogin_name() + "menu";
				Gson gs = new Gson();
				redisString.delKey(userNamemune);
				redisString.setKey(userNamemune, gs.toJson(muneList));

				// 返回信息
				userDO.setToken(compactJws);
				result.setData(userDO);
				result.setMessage("登录成功！");
				result.setSuccess(true);

				// 密码使用最长天数
				String updatePasswordTime = userDO.getUpdate_password_time();
				if (StringUtils.isEmpty(updatePasswordTime)) {
					// 如果是第一次登录需要重置密码
					userDO.setTimeFlag(true);
				} else {
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date updatePasswordDate = StringUtils.toDate(updatePasswordTime, "yyyy-MM-dd");
					//System.out.println(format.format(new Date()));
					//System.out.println(format.format(updatePasswordDate));
					//System.out.println((new Date().getTime() - updatePasswordDate.getTime()));
					long updatePasswordTimeDay = (new Date().getTime() - updatePasswordDate.getTime()) / 3600 / 24
							/ 1000;
					Map<String,String> paramMap = new HashMap<String, String>();
			    	paramMap.put("kdCode", "LIMIT_LOGIN_TIME");
			    	List<Map<String, Object>> list = baseSysCodeKindService.getSysCodeInfo(paramMap);
			    	String limitLoginTime = (String) list.get(0).get("CODE");
					//if (updatePasswordTimeDay > Integer.parseInt(limitLoginTime)) {
					if (updatePasswordTimeDay > Integer.parseInt(limitLoginTime)) {
						// 修改密码日期超过预期，需要重置密码
						userDO.setTimeFlag(true);
					}
				}
				// 修改登录时间
				BaseUserDO uDO = new BaseUserDO();
				uDO.setOnline_time("sysdate");
				uDO.setLast_login_time("sysdate");
				uDO.setId(userDO.getId());
				uDO.setModified_user_id(userDO.getId());
				this.baseUserService.updateBaseUser(uDO);
			}
			// 清除登录错误次数限制
			redisString.delKey(userName + "errorIndex");
		} else {
			// 用户登录错误次数限制
			String redisUserIndex = redisString.getValue(userName + "errorIndex");
			if (redisUserIndex != null) {
				Integer index = Integer.parseInt(redisUserIndex) + 1;
				//Integer errorIndex = Integer.parseInt(userErrorIndex);
				if (index > 3) {
					// 根据用户名锁定用户（多次用户密码错误）
					BaseUserDO uDO = new BaseUserDO();
					uDO.setState(AccountEnum.ACCSTATUS_LOCKED.getKey() + "");
					uDO.setError_count(index + "");
					uDO.setLocked_time("sysdate");
					uDO.setModified_user_id("-1");
					this.baseUserService.updateBaseUserByLoginName(uDO);
				}
				redisString.setKey(userName + "errorIndex", index.toString());
			} else {
				redisString.setKey(userName + "errorIndex", "1");
			}
			result.setData(userDO);
			result.setMessage("用户名或者密码错误！");
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * @description 查登录人员的权限菜单列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/listMenuByLoginUser", method = RequestMethod.POST)
	public ResultVO listMenuByLoginUser(@RequestParam Map<String, String> params) {
		ResultVO result = new ResultVO();
		result.setData(this.baseUserService.listMenuByLoginUser(params));
		result.setMessage("成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * @description 修改登录人员的密码
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/updatePasswordByLoginUser", method = RequestMethod.POST)
	public ResultVO updatePasswordByLoginUser(@RequestParam Map<String, String> paramMap) {
		// 1成功2异常3密码不可与原密码相同
		try {
			String oldPwd = Aes.aesDecrypt(paramMap.get("oldPwd"));
			String pwd = Aes.aesDecrypt(paramMap.get("pwd"));
			String pwd2 = Aes.aesDecrypt(paramMap.get("pwd2"));
			String userid = CommonUtils.getCurrentUserId();
			BaseUserDO u = this.baseUserService.getBaseUserDOById(userid);
			if(!oldPwd.equals(u.getLogin_password())) {
				return new ResultVO("-2", false, "旧密码错误！");
			}else if(pwd.equals(u.getLogin_password())) {
				return new ResultVO("-3", false, "新密码不能与原密码相同！");
			}else if (pwd.equals(pwd2) && !pwd.equals(u.getLogin_password())) {
				BaseUserDO userDO = new BaseUserDO();
				userDO.setId(userid);
				userDO.setLogin_password(pwd);
				userDO.setUpdate_password_time("sysdate");
				userDO.setModified_user_id(userid);
				this.baseUserService.updateBaseUser(userDO);
				return new ResultVO("1", true, "修改成功！");
			}else {
				return new ResultVO("-9", false, "未知错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVO("-1", false, "数据异常！");
		}
	}
	
	/**
	 * @description 验证登录人员旧密码是否正确（修改密码）
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/verifyOldPasswordByLoginUser", method = RequestMethod.POST)
	public ResultVO verifyOldPasswordByLoginUser(@RequestParam Map<String, String> paramMap) {
		try {
			String userid = CommonUtils.getCurrentUserId();
			BaseUserDO userDO = this.baseUserService.getBaseUserDOById(userid);
			if(paramMap.get("oldPwd").equals(userDO.getLogin_password())) {
				return new ResultVO("1", true, "旧密码正确！");
			}else {
				return new ResultVO("2", true, "旧密码错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVO("-1", false, "数据异常！");
		}
	}

	/**
	 * @description 查基础管理_登录人员表列表数据
	 */
	@RequestMapping(value = "/listBaseUser", method = RequestMethod.POST)
	public ResultVO listBaseUser(@RequestParam Map<String, String> param) {
		ResultVO result = new ResultVO();
		result.setData(this.baseUserService.listBaseUser(param));
		result.setMessage("成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * @description 查基础管理_登录人员表分页列表数据
	 */
	@RequestMapping(value = "/listBaseUserByPaging", method = RequestMethod.POST)
	public ResultVO listBaseUserByPaging(@RequestParam Map<String, String> param) {
		ResultVO result = new ResultVO();
		if (StringUtils.isEmpty(param.get("pageNum"))) {
			param.put("pageNum", "1");
		}
		if (StringUtils.isEmpty(param.get("pageSize"))) {
			param.put("pageSize", "10");
		}
		int page = Integer.parseInt(param.get("pageNum")); // 当前页
		int pageSize = Integer.parseInt(param.get("pageSize"));// 每页条数
		PageHelper.startPage(page, pageSize);
		List<BaseUserDO> personList = this.baseUserService.listBaseUser(param);
		PageInfo<BaseUserDO> personPageInfo = new PageInfo<BaseUserDO>(personList);
		result.setData(personPageInfo);
		result.setMessage("成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * @description 保存基础管理_登录人员表数据（新增、修改）
	 */
	@RequestMapping(value = "/saveBaseUser", method = RequestMethod.POST)
	public ResultVO saveBaseUser(@RequestBody BaseUserDO baseUserDO) {
		ResultVO result = new ResultVO();
		baseUserDO.setModified_user_id("-1");
		if (StringUtils.isEmpty(baseUserDO.getId() + "")) {
			this.baseUserService.insertBaseUser(baseUserDO);
		} else {
			this.baseUserService.updateBaseUser(baseUserDO);
		}
		result.setData(null);
		result.setMessage("成功");
		result.setSuccess(true);
		return result;
	}

	/**
	 * @description 删除基础管理_登录人员表数据
	 */
	@RequestMapping(value = "/removeBaseUser", method = RequestMethod.POST)
	public ResultVO removeBaseUser(@RequestParam Map<String, Object> param) {
		ResultVO result = new ResultVO();
		String ids = (String) param.get("ids");
		String[] idArr = ids.split(",");
		param.put("idArr", idArr);
		this.baseUserService.removeBaseUserByIds(param);
		result.setData(null);
		result.setMessage("成功");
		result.setSuccess(true);
		return result;
	}
}