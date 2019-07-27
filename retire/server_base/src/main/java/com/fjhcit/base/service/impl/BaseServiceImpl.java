package com.fjhcit.base.service.impl;




import com.baomidou.mybatisplus.plugins.Page;
import com.fjhcit.base.dao.BaseMapper;
import com.fjhcit.base.service.BaseService;
import com.fjhcit.entity.BaseUserDO;
import com.fjhcit.model.CacheModel;
import com.fjhcit.model.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author sunguoliang
 * @create 2018-09-25 下午2:15
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseMapper baseMapper;

    @Override
    public List<Users> findAll() {
        return baseMapper.findAll();
    }

	@Override
	public BaseUserDO login(String userName, String pwd) {
		// TODO Auto-generated method stub
	Map map=new HashMap();
	map.put("userName", userName);
	map.put("pwd", pwd);
		return baseMapper.login(map);
	}

	@Override
	public List<Map<String, String>> getMuneListByUserId(String id) {
		return baseMapper.getMuneListByUserId(id);
	}

	@Override
	public  List<Map> findRole() {
		// TODO Auto-generated method stub
		return baseMapper.findRole();
	}

	@Override
	public List<Map> findMenu(Map<String, String> paramMap) {
		List<Map> temp2=baseMapper.findMenu(paramMap);
		List<Map> temp=new ArrayList<Map>();

		for (Map map : temp2) {
			temp.add(transferToLowerCase(map));
		}
		
		List<Map> stemp=new ArrayList<Map>();

		for (Map map : temp) {
			String pid=(String)map.get("pid");
			if("0".equals(pid)) {
				map.put("children", getChildren(map,temp));	
				stemp.add(map);
			}
			
		}
		
		return stemp;
	}

	public static Map<String, String> transferToLowerCase(Map<String, String> orgMap) {
			Map<String, String> resultMap = new HashMap<>();

			if (orgMap == null || orgMap.isEmpty()) {
			return resultMap;
			}

			Set<Entry<String,String>> entrySet = orgMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = entry.getValue();
			resultMap.put(key.toLowerCase(), value);
			}

			return resultMap;
	}
	
	private List<Map> getChildren(Map map, List<Map> temp) {
		List<Map> stemp=new ArrayList<Map>();
		for (Map map2 : temp) {
			if(map2.get("pid").equals(map.get("id"))) {
				List<Map> ttmp = getChildren(map2,temp);
				if(ttmp.size()>0) {
					map2.put("children", ttmp);
				}
				stemp.add(map2);
			}
			
		}
		
		return stemp;
	}

	@Override
	public List<Map> findUser(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return baseMapper.findUser(paramMap);
	}

	@Override
	public Page findUserbypage(Map<String, String> paramMap, Page<Map> page2) {
	
		page2.setRecords(baseMapper.findUser(paramMap));
		return page2;
	}

	@Override
	public void addUser(Map<String, String> params) {
		 baseMapper.addUser(params);
		
	}
	@Override
	public void updateUser(Map<String, String> params) {
		 baseMapper.updateUser(params);
		
	}

	@Override
	public void removeUser(String[] tmp) {
		for (String id : tmp) {
			 baseMapper.removeUser(id);
		}
		
	}

	@Override
	public List<String> getDataRangetByUserId(String id) {
		// TODO Auto-generated method stub
		return baseMapper.getDataRangetByUserId(id);
	}

	@Override
	public void updateStateByUserName(String userName,String updateState) {
		// TODO Auto-generated method stub
		baseMapper.updateStateByUserName(userName,updateState);
	}

	@Override
	public void updateLastLoginTimeByUserId(Map<String, Object> params) {
		// TODO Auto-generated method stub
		baseMapper.updateLastLoginTimeByUserId(params);
	}
	
	@Override
	public ArrayList<CacheModel> getCode(String code) {
		
		return  baseMapper.getCode(code);
	}
	
}
