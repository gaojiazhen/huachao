package com.fjhcit.constant;

/**
 * @description 系统代码ParentId设置类
 * @author 陈 麟
 * @date 2019-06-05
 */
public class ParentIdContant {
	/**菜单*/
	public static final String BASE_MENU = "0";
	
	/**单位*/
	public static final String BASE_DEPARTMENT = "0";
	/**省公司本部（可查看所有单位数据）*/
	public static final String BASE_DEPARTMENT_PROVINCIAL = "101";
	
	/**数据字典*/
	public static final String BASE_CODE = "0";
	
	/**数据字典_性别*/
	public static final String BASE_CODE_SEX = "code_116";
	/**数据字典_民族*/
	public static final String BASE_CODE_NATION = "code_105";
	/**数据字典_婚姻状况*/
	public static final String BASE_CODE_MARITAL_STATUS = "code_114";
	/**数据字典_学历*/
	public static final String BASE_CODE_EDUCATION = "code_100";
	/**数据字典_政治面貌*/
	public static final String BASE_CODE_POLITICS_STATUS = "code_106";
	/**数据字典_政治面貌_共产党员*/
	public static final String BASE_CODE_POLITICS_STATUS_COMMUNIST = "code_10601";
	/**数据字典_离退休类型*/
	public static final String BASE_CODE_TYPE1 = "code_101";
	/**数据字典_离退休类型_退休*/
	public static final String BASE_CODE_TYPE1_RETIRE = "code_10102";
	/**数据字典_退休性质*/
	public static final String BASE_CODE_NATURE = "code_102";
	/**数据字典_退休性质_离休干部*/
	public static final String BASE_CODE_NATURE_QUIT = "code_10201";
	/**数据字典_离退休职级*/
	public static final String BASE_CODE_RETIREMENT_RANK = "code_108";
	/**数据字典_现享受待遇级别*/
	public static final String BASE_CODE_NOW_TREATMENT_LEVEL = "code_109";
	/**数据字典_人事档案存放地*/
	public static final String BASE_CODE_ARCHIVES_AREA = "code_112";
	/**数据字典_接收地*/
	public static final String BASE_CODE_RECEIVE_AREA = "code_113";
	/**数据字典_健康状况*/
	public static final String BASE_CODE_HEALTH_STATUS = "code_111";
	/**数据字典_健康状况_生活不能自理*/
	public static final String BASE_CODE_HEALTH_STATUS_CANNOT_CARE_ONESELF = "code_11104";
	/**数据字典_发挥作用情况*/
	public static final String BASE_CODE_PLAY_A_ROLE = "code_119";
	/**数据字典_奖项级别（先模人员）*/
	public static final String BASE_CODE_AWARD_LEVEL = "code_104";
	/**数据字典_特殊人员标识*/
	public static final String BASE_CODE_SPECIAL_CROWD = "code_120";
	/**数据字典_称谓*/
	public static final String BASE_CODE_APPELLATION = "code_121";
	
	/**数据字典_党组织关系所在地*/
	public static final String BASE_CODE_PARTY_LOCATED = "code_107";
	public static final String BASE_CODE_PARTY_LOCATED_COMPANY = "code_10701";  	//企业
	public static final String BASE_CODE_PARTY_LOCATED_PLACE = "code_10702";     //地方
	/**数据字典_人员性质*/
	public static final String BASE_CODE_USER_NATURE = "code_117";
	/**数据字典_人员级别*/
	public static final String BASE_CODE_USER_RANK = "code_118";
	
	
	/**离/退休人员类别2   （系统改版被弃用）*/   
	public static final String BASE_CODE_TYPE2 = "code_103";
	/**城市（省）*/
	public static final String BASE_CODE_CITY = "code_110";
	/**组织机构_机构类型*/
	public static final String BASE_CODE_ORGANIZATION_TYPE = "code_115";

	/**BASE_CODE启用标识*/
	public static final String BASE_CODE_IS_AVAILABLE_YES = "1";
	/**BASE_CODE禁用标识*/
	public static final String BASE_CODE_IS_AVAILABLE_NO = "0";
	/**BASE_COD 小计ID标识*/
	public static final String BASE_CODE_ID_SUBTOTAL = "('code_10202','code_10203','code_10204','code_10205','code_10206','code_10207')";
	/**BASE_COD 退休ID标识*/
	public static final String BASE_CODE_ID_RETIRE = "('code_10202','code_10203','code_10204','code_10205')";   //退休人员ID串，“5.12干部、建退工人、一般退休干部、一般退休工人 ”为退休
	/**BASE_COD 退职ID标识*/
	public static final String BASE_CODE_ID_RESIGN = "('code_10206')";
	/**BASE_COD 内退ID标识*/
	public static final String BASE_CODE_ID_EARLY_RETIRE = "('code_10207')";
	/**行政区划类型编码*/
	public static final String BASE_CODE_ADMINISTRATIVE_DIVISION = "code_122";
}