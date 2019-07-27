package com.fjhcit.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fjhcit.base.service.BaseMenuService;

/**
 * 菜单控制器
 * @author bule
 *
 */
@RestController
@RequestMapping("/menus")
public class BaseMenuController {
	@Autowired
	private BaseMenuService baseMenuService;

	/**
	 * 菜单管理页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findMenu", method = RequestMethod.POST)
	public Object findMenu(@RequestParam Map<String, String> paramMap){
		System.out.println(paramMap.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = baseMenuService.getFindMenuParentmenu(paramMap);
		result.put("data", resultList);
		result.put("count", resultList.size());
		result.put("code", 0);//返回码
		return result;
	}
	
	/**
	 * 添加和编辑菜单
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/editMenu", method = RequestMethod.POST)
	public String addMenu(@RequestParam Map paramMap) {
		System.out.println(paramMap.toString());
		//1成功2异常
		try {
			if(StringUtils.isNotBlank((String) paramMap.get("ID"))) {
				System.out.println("编辑方法!");
				baseMenuService.updateMenu(paramMap);
			}else {
				System.out.println("新增方法!");
				baseMenuService.addMenu(paramMap);
			}
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
	
	/**
	 * 删除菜单
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteMenu", method = RequestMethod.POST)
	public String deleteMenu(@RequestParam Map<String, String> paramMap) {
		String i = baseMenuService.getFindMenuCount(paramMap);
		//1成功0失败2异常
		try {
			if("0".equals(i)) {
				baseMenuService.deleteMenu(paramMap);
				return "1";
			}else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}

}
























