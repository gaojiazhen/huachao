package com.fjhcit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fjhcit.common.kit.StringUtils;

/**
 * @description 根据oracle库表自动生成Java三层代码
 * @author 陈 麟
 * @date 2019-06-01
 */
public class GetBaseJavaCodeFromTable {
	public String filepaths = "/src/main/java/com/fjhcit/retire/";
	public String DO_paths = "/pojos/src/main/java/com/fjhcit/entity/";	// 实体类路径（另一个工程pojos）
	public String mappers_paths = "/src/main/resources/mappers/";		// 映射器路径（在本工程resources文件夹）
	public String table_name = "RETIRE_COMMUNIST8";	// 表名
	public String implementName = "impl"; 			// 实现层的包名
	public String author = "陈麟"; 	// 类作者
	public Connection con; 			// 数据库连接

	/**
	 * @description 生成java类及xml配置文件
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// 1、读yml文件，连接数据库
			GetBaseJavaCodeFromTable test = new GetBaseJavaCodeFromTable();
			FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + 
					"/target/classes/application.yml");
			test.init(fs);
			// 2、查表字段属性
			List<Map<String,String>> datalist = test.findTableInfo();
			Map<String,String> datamap = test.findTableName();
			// 3、生成DO.java文件 
			test.GenerationDOJavaFile(datalist,datamap);
			// 4、生成Mapper.XML文件
			List<Map<String,String>> typelist = test.GenerationSqlXmlFile(datalist,datamap);
			// 5、生成DAO.java文件
			test.GenerationDAOJavaFile(typelist,datamap); 
			// 6、生成Service.java文件
			test.GenerationServiceJavaFile(typelist,datamap); 
			// 7、生成ServiceImpl.java文件
			test.GenerationServiceImplJavaFile(typelist,datamap); 
			// 8、生成Controller.java文件
			test.GenerationActionJavaFile(datamap); 
			System.out.println("创建工作结束！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 读取boot application.yml文件数据库配置连接
	 * @param fs
	 * @throws Exception
	 */
	public void init(FileInputStream fs) throws Exception {
		Properties props = new Properties();
		props.load(fs);
		String url = props.getProperty("url");
		String userName = props.getProperty("username");
		String password = props.getProperty("password");
		Class.forName(props.getProperty("driver-class-name"));
		con = DriverManager.getConnection(url, userName, password);
	}

	/**
	 * @description 获取表字段相关属性
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public List<Map<String, String>> findTableInfo() throws SQLException, IOException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT USER_TAB_COLS.TABLE_NAME as 表名,");
		sql.append("USER_TAB_COLS.COLUMN_NAME  as 列名,");
		sql.append("USER_TAB_COLS.DATA_TYPE    as 数据类型,");
		sql.append("USER_TAB_COLS.DATA_LENGTH  as 长度,");
		sql.append("USER_TAB_COLS.NULLABLE     as 是否可为空,");
		sql.append("USER_TAB_COLS.COLUMN_ID    as 列序号,");
		sql.append("user_col_comments.comments as 备注 ");
		sql.append("FROM USER_TAB_COLS ");
		sql.append("inner join user_col_comments on user_col_comments.TABLE_NAME = USER_TAB_COLS.TABLE_NAME ");
		sql.append("and user_col_comments.COLUMN_NAME = USER_TAB_COLS.COLUMN_NAME ");
		sql.append("and USER_TAB_COLS.TABLE_NAME='" + table_name.toUpperCase() + "'");
		System.out.println(sql);
		PreparedStatement ps = con.prepareStatement(sql.toString());
		ResultSet rs = ps.executeQuery();
		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
		while (rs.next()) {
			Map<String, String> datamap = new HashMap<String, String>();
			datamap.put("table_name", rs.getString(1)); 	// 表名
			datamap.put("column_name", rs.getString(2).toLowerCase()); // 列名
			datamap.put("data_type", rs.getString(3)); 		// 数据类型
			datamap.put("data_length", rs.getString(4)); 	// 长度
			datamap.put("nullable", rs.getString(5)); 	// 是否可为空
			datamap.put("column_id", rs.getString(6)); 	// 列序号
			datamap.put("comments", rs.getString(7));	// 备注
			datalist.add(datamap);
		}
		rs.close();
		ps.close();
		return datalist;
	}

	/**
	 * @description 获取表名及表注释
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public Map<String, String> findTableName() throws SQLException, IOException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.TABLE_NAME,A.COMMENTS FROM USER_TAB_COMMENTS A WHERE  A.TABLE_NAME='"
				+ table_name.toUpperCase() + "'");
		System.out.println(sql);
		PreparedStatement ps = con.prepareStatement(sql.toString());
		ResultSet rs = ps.executeQuery();
		Map<String, String> datamap = new HashMap<String, String>();
		while (rs.next()) {
			String tablename = rs.getString(1);
			datamap.put("table_name", tablename); 		// 表名
			datamap.put("comments", rs.getString(2)); // 表注释
			String tablenamelower = tablename.toLowerCase();
			//首字母及下划线后面首字段大写
			String filename = "";
			String[] tabnameArr = tablenamelower.split("_");
			int frequency = this.getCharInStr(tablenamelower, "_") + 1;
			for (int i = 0; i < frequency; i++) {
				if (i != frequency - 1) {
					filename += (tabnameArr[i]).substring(0, 1).toUpperCase()
							+ (tabnameArr[i]).substring(1, (tabnameArr[i]).length()).toLowerCase();
				} else {
					filename += (tabnameArr[i]).substring(0, 1).toUpperCase()
							+ (tabnameArr[i]).substring(1, (tabnameArr[i]).length()).toLowerCase();
				}
			}
			datamap.put("filename", filename);
		}
		rs.close();
		ps.close();
		return datamap;
	}

	/**
	 * @description 生成DO代码
	 * @param datalist
	 * @param datamap
	 * @throws IOException
	 * D:\CodeWorkSpace\LTXWorkSpace\retire\pojos\src\main\java\com\fjhcit\entity
	 */
	public void GenerationDOJavaFile(List<Map<String, String>> datalist, Map<String, String> datamap)
			throws IOException {
		StringUtils util = new StringUtils();
		String comments = datamap.get("comments").toString();
		String filename = datamap.get("filename");
		// 生成文件
		File file = new File(this.findUserDir() + this.DO_paths);
		file.mkdirs(); // 生成目录
		OutputStreamWriter xmlfile = new OutputStreamWriter(new FileOutputStream(
				this.findUserDir() + this.DO_paths + filename + "DO.java", true), "UTF-8");
		PrintWriter pw = new PrintWriter(xmlfile);
		String beantype = (DO_paths.replace("/pojos/src/main/java/", "")).replace("/", ".");
		pw.println("package " + beantype.substring(0,beantype.length()-1) + ";");
		pw.println("");
		pw.println("/**");
		pw.println(" * @description " + comments + "_实体类 ");
		pw.println(" * @author " + author);
		pw.println(" * @date " + new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss").format(new Date()));
		pw.println(" */");
		pw.println("public class " + filename + "DO{");
		int len = 0;
		// 计算注释的最长位置
		for (Map<String, String> temp : datalist) {
			String info = "    private String " + util.ChkNull((String) temp.get("column_name"), "") + ";";
			if (info.length() > len) {
				len = info.length();
			}
		}
		for (Map<String, String> m : datalist) {
			String info = "    private String " + util.ChkNull((String) m.get("column_name"), "") + ";";
			pw.println(info + this.AddSpaces(len - info.length()) + "//"
					+ util.ChkNull((String) m.get("comments"), ""));
		}
		pw.println("");
		pw.println("    @Override");
		pw.println("    public String toString() {");
		String return_toString = "return \"" + filename + "DO [";
		for (int i=0;i<datalist.size();i++) {
			Map<String, String> m = datalist.get(i);
			return_toString += util.ChkNull((String) m.get("column_name"), "") + "=\" + " + util.ChkNull((String) m.get("column_name"), "");
			//3个字段输出一次，最后一个另外输出
			if ((i+1)%3==0 && i!=datalist.size()-1) {
				pw.println("        " + return_toString);
				return_toString = "        + \"";
			} else {
				return_toString += " + \"";
			}
			if (i!=datalist.size()-1) {
				return_toString += ",";
			} else {
				return_toString+="]\";";
				pw.println("        " + return_toString);
			}
		}
		pw.println("    }");
		pw.println("	//以下是Get和Set方法");
		for (Map<String, String> temp : datalist) {
			String element = temp.get("column_name").toString();
			String element1 = element.substring(0, 1).toUpperCase()
					+ element.substring(1, element.length()).toLowerCase();
			pw.println("    public String get" + element1 + "() {");
			pw.println("        return " + element + ";");
			pw.println("    }");
			pw.println("    public void set" + element1 + "(String " + element + ") {");
			pw.println("        this." + element + " = " + element + ";");
			pw.println("    }");
		}
		pw.print("}");
		pw.flush();
		xmlfile.close();
	}

	/**
	 * @description 生成Mapper配置文件
	 * @param datalist
	 * @param datamap
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public List<Map<String, String>> GenerationSqlXmlFile(List<Map<String, String>> datalist,
			Map<String, String> datamap) throws IOException {
		StringUtils util = new StringUtils();
		List<Map<String, String>> typelist = new ArrayList<Map<String, String>>();
		String tablename = datamap.get("table_name").toString().toLowerCase();
		String comments = datamap.get("comments").toString();
		String[] elementsSelectArr = new String[datalist.size()];
		String[] elementsForInsertArr = new String[datalist.size()];
		for (int i=0;i<datalist.size();i++) {
			Map<String, String> temp = datalist.get(i);
			String data_type = util.ChkNull((String) temp.get("data_type"), "");
			String column_name = util.ChkNull((String) temp.get("column_name"), "");
			if ("DATE".equals(data_type)) {
				if("gmt_create".contentEquals(column_name) || "gmt_modified".contentEquals(column_name)) {
					elementsSelectArr[i] = "to_char(" + column_name + ",'yyyy-mm-dd hh24:mi') as " + column_name;
					elementsForInsertArr[i] = "sysdate";
				}else {
					elementsSelectArr[i] = "to_char(" + column_name + ",'yyyy-mm-dd') as " + column_name;
					elementsForInsertArr[i] = "to_date(#{" + column_name + "},'yyyy-mm-dd')";
				}
			} else {
				elementsSelectArr[i] = column_name;
				elementsForInsertArr[i] = "#{" + column_name +"}";
			}
		}
		// 文件名称
		String filename = datamap.get("filename");
		String beantype = this.DO_paths.replace("/pojos/src/main/java/", "").replace("/", ".") + filename + "DO";
		String namespace = (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "dao." + filename + "DAO";
		// 生成文件
		File file = new File(System.getProperty("user.dir") + this.mappers_paths);
		file.mkdirs(); // 生成目录
		OutputStreamWriter xmlfile = new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + this.mappers_paths + filename + "Mapper.xml", true), "UTF-8");
		PrintWriter pw = new PrintWriter(xmlfile);
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
		pw.println("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
		pw.println("<!--");
		pw.println("@description " + comments + "_映射器");
		pw.println("@author " + author);
		pw.println("@date " + new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss").format(new Date()));
		pw.println("-->");
		pw.println("<mapper namespace=\"" + namespace + "\">");
		/**
		 * 1、查找列表信息
		 */
		tablename = table_name.toLowerCase();
		pw.println("");
		pw.println("	<!-- 查找" + comments + "列表信息 -->");
		pw.println("	<select id=\"list" + filename + "\" resultType=\"" + beantype + "\" parameterType=\"java.util.Map\">");
		String elements_select = "select ";
		for(int i=0;i<elementsSelectArr.length;i++) {
			elements_select += elementsSelectArr[i] + ",";
			if((i + 1) % 10 == 0 && i!=datalist.size()-1) {
				if(i>10) {
					pw.println("			" + elements_select);
				}else {
					pw.println("		" + elements_select);
				}
				elements_select = "";
			}
			if(i==datalist.size()-1) {
				elements_select = elements_select.substring(0,elements_select.length()-1);
				if(elementsSelectArr.length > 10) {
					pw.println("			" + elements_select);
				}else {
					pw.println("		" + elements_select);
				}
			}
		}
		pw.println("		from " + tablename);
		pw.println("		<where>");
		// id相关的关键字加入查询
		for(Map<String,String> temp : datalist){
			String element = temp.get("column_name");
			if(element.indexOf("_id") >= 0 || element.indexOf("type") >= 0) {
				pw.println("			<if test=\"" + element + " != null and " + element + " != ''\">"); 
				pw.println("				and " + element + " = #{" + element + "}"); 
				pw.println("			</if>"); 
			}
		}
		// id相关的关键字加入查询
		for(Map<String,String> temp : datalist){
			String element = temp.get("column_name");
			if(element.indexOf("_name") >= 0) {
				pw.println("			<if test=\"keyword == '" + element + "'\">"); 
				pw.println("				and " + element + " like '%' || #{" + element + "} || '%'"); 
				pw.println("			</if>"); 
			}
		}
		pw.println("		</where>");
		pw.println("		<choose>");
		pw.println("			<when test=\"sortField == 'gmt_create'\">");
		pw.println("				order by gmt_create");
		pw.println("			</when>");
		pw.println("			<when test=\"sortField == 'gmt_modified'\">");
		pw.println("				order by gmt_modified");
		pw.println("			</when>");
		pw.println("			<otherwise>");
		pw.println("				order by id");
		pw.println("			</otherwise>");
		pw.println("		</choose>");
		pw.println("		<choose>");
		pw.println("        	<when test=\"sortType == 'asc'\">");
		pw.println("            	asc");
		pw.println("        	</when>");
		pw.println("        	<when test=\"sortType == 'desc'\">");
		pw.println("            	desc");
		pw.println("        	</when>");
		pw.println("    	</choose>");
		pw.println("	</select>");
		pw.println("");
		// 保存配置文件信息以供DAO读取使用
		Map<String, String> temps = new HashMap<String, String>();
		temps.put("id", "list" + filename);
		temps.put("type", "queryForList");
		temps.put("resultClass", "List<" + filename + "DO>");
		temps.put("parameterType", "Map<String,String>");
		temps.put("comments", "查找" + comments + "列表信息");
		typelist.add(temps);
		/**
		 * 2、查找单条信息
		 */
		pw.println("	<!-- 查找" + comments + "单条信息 -->");
		pw.println("	<select id=\"get" + filename + "DOById\" resultType=\"" + beantype + "\" parameterType=\"java.lang.String\">");
		elements_select = "select ";
		for(int i=0;i<elementsSelectArr.length;i++) {
			elements_select += elementsSelectArr[i] + ",";
			if((i + 1) % 10 == 0 && i!=datalist.size()-1) {
				if(i>10) {
					pw.println("			" + elements_select);
				}else {
					pw.println("		" + elements_select);
				}
				elements_select = "";
			}
			if(i==datalist.size()-1) {
				elements_select = elements_select.substring(0,elements_select.length()-1);
				if(elementsSelectArr.length > 10) {
					pw.println("			" + elements_select);
				}else {
					pw.println("		" + elements_select);
				}
			}
		}
		pw.println("		from " + tablename + " where id=#{id}");
		pw.println("	</select>");
		pw.println("");
		// 保存配置文件信息以供DAO读取使用
		temps = new HashMap<String, String>();
		temps.put("id", "get" + filename + "DOById");
		temps.put("type", "queryForObject");
		temps.put("resultClass", filename + "DO");
		temps.put("parameterType", "String");
		temps.put("comments", "查找" + comments + "单条信息");
		typelist.add(temps);
		/**
		 * 3、添加信息
		 */
		String seq_ = "seq_" + tablename;
		pw.println("	<!-- 添加" + comments + "信息 -->");
		pw.println("	<insert id=\"insert" + filename + "\" parameterType=\"" + beantype + "\" >");
		if ("id".equals(datalist.get(0).get("column_name"))) {
			pw.println("		<selectKey keyProperty=\"id\" resultType=\"java.lang.String\" order=\"BEFORE\">");
			pw.println("			select " + seq_ + ".nextval as id from dual");
			pw.println("		</selectKey>");
		}
		pw.println("		insert into " + tablename);
		String elements_insert = "(";
		for (int i=0;i<datalist.size();i++) {
			Map<String, String> temp = datalist.get(i);
			elements_insert += temp.get("column_name") + ",";
			if((i + 1) % 10 == 0 && i!=datalist.size()-1) {
				pw.println("			" + elements_insert);
				elements_insert = "";
			}
			if(i==datalist.size()-1) {
				elements_insert = elements_insert.substring(0,elements_insert.length()-1);
				pw.println("			" + elements_insert + ")");
			}
		}
		pw.println("		values");
		elements_insert = "(";
		for (int i=0;i<elementsForInsertArr.length;i++) {
			elements_insert += elementsForInsertArr[i] + ",";
			if((i + 1) % 10 == 0 && i!=datalist.size()-1) {
				pw.println("			" + elements_insert);
				elements_insert = "";
			}
			if(i==datalist.size()-1) {
				elements_insert = elements_insert.substring(0,elements_insert.length()-1);
				pw.println("			" + elements_insert + ")");
			}
		}
		pw.println("	</insert> ");
		pw.println("");
		// 保存配置文件信息以供DAO读取使用
		temps = new HashMap<String, String>();
		temps.put("id", "insert" + filename);
		temps.put("type", "insert");
		temps.put("comments", "添加" + comments + "信息");
		temps.put("resultClass", "void");
		temps.put("parameterType", filename + "DO");
		typelist.add(temps);
		/**
		  *  4、更新信息
		 */
		pw.println("	<!-- 更新" + comments + "信息 -->");
		pw.println("	<update id=\"update" + filename + "\" parameterType=\"" + beantype + "\">");
		// where条件
		String updatekey = "";
		for (Map<String, String> temp : datalist) {
			if ("N".equals(temp.get("nullable"))) {
				String element = temp.get("column_name").toString();
				updatekey += ", " + element + " = #{" + element + "}";
			}
		}
		if (!"".equals(updatekey)) {
			updatekey = updatekey.substring(1);
		}
		// set字段
		pw.println("		update " + tablename);
		pw.println("		<set>");
		pw.println("			MODIFIED_USER_ID = #{modified_user_id},GMT_MODIFIED = sysdate,");
		for (Map<String, String> temp : datalist) {
			String element = temp.get("column_name").toString();
			if (!"modified_user_id".equals(element) && !"gmt_modified".equals(element) && 
					!"create_user_id".equals(element) && !"gmt_create".equals(element) && !"id".equals(element)) {
				String data_type = util.ChkNull((String) temp.get("data_type"), "");
				if ("DATE".equals(data_type)) {
					pw.println("			<if test=\"" + element + "!=null\">" + element + " = to_date(#{" + element + "},'yyyy-mm-dd'),</if>");
				}else {
					pw.println("			<if test=\"" + element + "!=null\">" + element + " = #{" + element + "},</if>");
				}
				
			}
		}
		pw.println("		</set>");
		pw.println("		where" + updatekey);
		pw.println("	</update>");
		pw.println("");
		// 保存配置文件信息以供DAO读取使用
		temps = new HashMap<String, String>();
		temps.put("id", "update" + filename);
		temps.put("type", "update");
		temps.put("comments", "更新" + comments + "信息");
		temps.put("parameterType", filename + "DO");
		typelist.add(temps);
		/**
		 * 5、删除信息
		 */
		pw.println("	<!-- 删除" + comments + "信息 -->");
		pw.println("	<delete id=\"remove" + filename + "ByIdsArr\" parameterType=\"java.util.Map\">");
		pw.println("		delete from " + tablename + " where id in ");
		pw.println("		<foreach collection=\"idsArr\" index=\"index\" item=\"item\"  open=\"(\" separator=\",\" close=\")\">");
		pw.println("			#{item}");
		pw.println("		</foreach>");
		pw.println("	</delete>");
		// 保存配置文件信息以供DAO读取使用
		temps = new HashMap<String, String>();
		temps.put("id", "remove" + filename + "ByIdsArr");
		temps.put("type", "delete");
		temps.put("comments", "删除" + comments + "信息");
		temps.put("parameterType", "Map<String,Object>");
		typelist.add(temps);
		pw.print("</mapper>");
		pw.flush();
		xmlfile.close();
		return typelist;
	}

	/**
	 * @description 生成DAO接口代码
	 * @param typelist
	 * @param datamap
	 * @throws IOException
	 */
	public void GenerationDAOJavaFile(List<Map<String, String>> typelist, Map<String, String> datamap)
			throws IOException {
		StringUtils util = new StringUtils();
		String comments = datamap.get("comments").toString();
		// 文件名称
		String filename = datamap.get("filename");
		String beantype = (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "dao";
		// 生成文件
		File file = new File(System.getProperty("user.dir") + filepaths + "/dao");
		file.mkdirs(); // 生成目录
		OutputStreamWriter xmlfile = new OutputStreamWriter(new FileOutputStream(
				System.getProperty("user.dir") + filepaths + "/dao/" + filename + "DAO.java", true), "UTF-8");
		PrintWriter pw = new PrintWriter(xmlfile);
		pw.println("package " + beantype + ";");
		pw.println("");
		pw.println("import java.util.List;");
		pw.println("import java.util.Map;");
		pw.println("");
		pw.println("import " + DO_paths.replace("/pojos/src/main/java/", "").replace("/", ".") + filename + "DO;");
		pw.println("");
		pw.println("/**");
		pw.println(" * @description " + comments + "_数据库操作接口");
		pw.println(" * @author " + author);
		pw.println(" * @date " + new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss").format(new Date()));
		pw.println(" */");
		pw.println("public interface " + filename + "DAO {");
		for (Map<String, String> temp : typelist) {
			String id = util.ChkNull(temp.get("id").toString(), "");
			String parameterType = util.ChkNull(temp.get("parameterType"), "");
			String resultClass = util.ChkNull((String) temp.get("resultClass"), "void");
			pw.println("");
			pw.println("	/**");
			pw.println("	 * @description " + util.ChkNull(temp.get("comments"), ""));
			pw.println("	 * @param " + this.Conversion(parameterType).replace("<String,String>", "").replace("<String,Object>", ""));
			pw.println("	 * @return");
			pw.println("	 */");
			pw.println("	" + resultClass + " " + id + "(" + parameterType + " " + 
					this.Conversion(parameterType).replace("<String,String>", "").replace("<String,Object>", "") + ");");
		}
		pw.print("}");
		pw.flush();
		xmlfile.close();
	}
 
	/**
	 * @description 生成Service接口代码
	 * @param typelist
	 * @param datamap
	 * @throws IOException
	 */
	public void GenerationServiceJavaFile(List<Map<String, String>> typelist, Map<String, String> datamap)
			throws IOException {
		StringUtils util = new StringUtils();
		String comments = datamap.get("comments").toString();
		// 文件名称
		String filename = datamap.get("filename");
		String beantype = (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "service";
		// 生成文件
		File file = new File(System.getProperty("user.dir") + filepaths + "/service");
		file.mkdirs(); // 生成目录
		OutputStreamWriter xmlfile = new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + filepaths + 
				"/service/" + filename + "Service.java", true),"UTF-8");
		PrintWriter pw = new PrintWriter(xmlfile);
		pw.println("package " + beantype + ";");
		pw.println("");
		pw.println("import java.util.List;");
		pw.println("import java.util.Map;");
		pw.println("");
		pw.println("import " + this.DO_paths.replace("/pojos/src/main/java/", "").replace("/", ".") + filename + "DO;");
		pw.println("");
		pw.println("/**");
		pw.println(" * @description " + comments + "_业务逻辑接口");
		pw.println(" * @author " + author);
		pw.println(" * @date " + new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss").format(new Date()));
		pw.println(" */");
		pw.println("public interface " + filename + "Service {");
		for (Map<String, String> temp : typelist) {
			String id = util.ChkNull(temp.get("id").toString(),"");
			//id = id.replace("update", "save");
			String parameterType = util.ChkNull(temp.get("parameterType").toString(), "");
			//if (id.indexOf("save") >= 0) {
				//parameterType = "Map<String,Object>";
			//}
			String resultClass = util.ChkNull((String) temp.get("resultClass"), "void");
			pw.println("");
			pw.println("	/**");
			pw.println("	 * @description " + util.ChkNull(temp.get("comments").toString(), ""));
			pw.println("	 * @param " + this.Conversion(parameterType).replace("<String,String>", "").replace("<String,Object>", ""));
			pw.println("	 * @return");
			pw.println("	 */");
			pw.println("	" + resultClass + " " + id + "(" + parameterType + " "
					+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", ""))
					+ ");");
		}
		//把新增和修改合并成保存
		//typelist.remove(2);
//		for (Map<String, String> temp : typelist) {
//			String id = util.ChkNull(temp.get("id").toString(),"");
//			id = id.replace("update", "save");
//			String parameterType = util.ChkNull(temp.get("parameterType").toString(), "");
//			if (id.indexOf("save") >= 0) {
//				parameterType = "Map<String,Object>";
//			}
//			String resultClass = util.ChkNull((String) temp.get("resultClass"), "void");
//			pw.println("");
//			pw.println("	/**");
//			pw.println("	 * " + util.ChkNull(temp.get("comments").toString().replace("更新", "保存"), ""));
//			pw.println("	 * @param "
//					+ this.Conversion(parameterType).replace("<String,String>", "").replace("<String,Object>", ""));
//			pw.println("	 * @return");
//			pw.println("	 */");
//			pw.println("	" + resultClass + " " + id + "(" + parameterType + " "
//					+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", ""))
//					+ ");");
//		}
		pw.print("}");
		pw.flush();
		xmlfile.close();
	}

	/**
	 * @description 生成ServiceImpl业务逻辑实现类代码
	 * @param typelist
	 * @param datamap
	 * @throws IOException
	 */
	public void GenerationServiceImplJavaFile(List<Map<String, String>> typelist, Map<String, String> datamap)
			throws IOException {
		StringUtils util = new StringUtils();
		// 文件名称
		String filename = datamap.get("filename");
		String beantype = (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "service." + implementName;
		// 生成文件
		File file = new File(System.getProperty("user.dir") + filepaths + "/service/" + implementName);
		file.mkdirs(); // 生成目录
		OutputStreamWriter xmlfile = new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")
				+ filepaths + "/service/" + implementName + "/" + filename + "ServiceImpl.java", true), "UTF-8");
		PrintWriter pw = new PrintWriter(xmlfile);
		pw.println("package " + beantype + ";");
		pw.println("");
		pw.println("import java.util.List;");
		pw.println("import java.util.Map;");
		pw.println("");
		pw.println("import org.springframework.beans.factory.annotation.Autowired;");
		pw.println("import org.springframework.stereotype.Service;");
		pw.println("");
		pw.println("import " + DO_paths.replace("/pojos/src/main/java/", "").replace("/", ".") + filename + "DO;");
		pw.println("import " + (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "dao." + filename + "DAO;");
		pw.println("import " + (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "service." + filename + "Service;");
		pw.println("");
		pw.println("@Service");
		pw.println("public class " + filename + "ServiceImpl implements " + filename + "Service {");
		pw.println("");
		pw.println("	@Autowired");
		pw.println("	private " + filename + "DAO " + this.Conversion(filename) + "DAO;");
		for (Map<String, String> temp : typelist) {
			String id = util.ChkNull(temp.get("id").toString(), "");
			//id = id.replace("update", "save");
			String parameterType = util.ChkNull(temp.get("parameterType").toString(), "");
			//if (id.indexOf("save") >= 0) { 
			//	parameterType = "Map<String,Object>";
			//}
			String resultClass = util.ChkNull((String) temp.get("resultClass"), "void");
			pw.println("");
			pw.println("	@Override");
			pw.println("	public " + resultClass + " " + id + "(" + parameterType + " "
					+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", "")) + "){");
			String returnstr = "";
			if ("void".equals(resultClass)) {
				//if (id.indexOf("save") == -1) {
					returnstr = "this." + this.Conversion(filename) + "DAO." + id + "("
							+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", ""))+ ");";
//				} else {
//					pw.println("		String modified_user_id = (String)map.get(\"modified_user_id\");");
//					pw.println("		" + filename + "DO " + this.Conversion(filename) + "DO = (" + filename
//							+ "DO)map.get(\"" + this.Conversion(filename) + "DO\");");
//					pw.println("		" + this.Conversion(filename + "DO") + ".setModified_user_id(modified_user_id);");
//					pw.println(
//							"		if(StringUtils.isEmpty(" + this.Conversion(filename + "DO") + ".getId())){");
//					pw.println("			" + this.Conversion(filename + "DO") + ".setCreate_user_id(modified_user_id);");
//					pw.println("			this." + this.Conversion(filename) + "DAO.insert" + id.substring(4) + "("
//							+ this.Conversion(filename + "DO") + ");");
//					pw.println("		}else{");
//					pw.println("			this." + this.Conversion(filename) + "DAO.update" + id.substring(4) + "("
//							+ this.Conversion(filename + "DO") + ");");
//					returnstr = "}";
//				}
			} else {
				returnstr = "return this." + this.Conversion(filename) + "DAO." + id + "("
						+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", ""))
						+ ");";
			}
			pw.println("		" + returnstr);
			pw.println("	}");
		}
		//把新增和修改合并成保存
//		for (Map<String, String> temp : typelist) {
//			String id = util.ChkNull(temp.get("id").toString(), "");
//			id = id.replace("update", "save");
//			String parameterType = util.ChkNull(temp.get("parameterType").toString(), "");
//			if (id.indexOf("save") >= 0) { 
//				parameterType = "Map<String,Object>";
//			}
//			String resultClass = util.ChkNull((String) temp.get("resultClass"), "void");
//			pw.println("");
//			pw.println("	@Override");
//			pw.println("	public " + resultClass + " " + id + "(" + parameterType + " "
//					+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", "")) + "){");
//			String returnstr = "";
//			if ("void".equals(resultClass)) {
//				if (id.indexOf("save") == -1) {
//					returnstr = "this." + this.Conversion(filename) + "DAO." + id + "("
//							+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", ""))+ ");";
//				} else {
//					pw.println("		String modified_user_id = (String)map.get(\"modified_user_id\");");
//					pw.println("		" + filename + "DO " + this.Conversion(filename) + "DO = (" + filename
//							+ "DO)map.get(\"" + this.Conversion(filename) + "DO\");");
//					pw.println("		" + this.Conversion(filename + "DO") + ".setModified_user_id(modified_user_id);");
//					pw.println(
//							"		if(StringUtils.isEmpty(" + this.Conversion(filename + "DO") + ".getId())){");
//					pw.println("			" + this.Conversion(filename + "DO") + ".setCreate_user_id(modified_user_id);");
//					pw.println("			this." + this.Conversion(filename) + "DAO.insert" + id.substring(4) + "("
//							+ this.Conversion(filename + "DO") + ");");
//					pw.println("		}else{");
//					pw.println("			this." + this.Conversion(filename) + "DAO.update" + id.substring(4) + "("
//							+ this.Conversion(filename + "DO") + ");");
//					returnstr = "}";
//				}
//			} else {
//				returnstr = "return this." + this.Conversion(filename) + "DAO." + id + "("
//						+ this.Conversion(parameterType.replace("<String,String>", "").replace("<String,Object>", ""))
//						+ ");";
//			}
//			pw.println("		" + returnstr);
//			pw.println("	}");
//		}
		pw.print("}");
		pw.flush();
		xmlfile.close();
	}

	/**
	 * @description 生成controller控制层代码
	 * @param typelist
	 * @param datamap
	 * @throws IOException
	 */
	public void GenerationActionJavaFile(Map<String, String> datamap) throws IOException {
		String comments = datamap.get("comments").toString();
		// 文件名称
		String filename = datamap.get("filename");
		String beantype = (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "controller";
		String conversion = this.Conversion(filename);
		// 生成文件
		File file = new File(System.getProperty("user.dir") + filepaths + "/controller");
		file.mkdirs(); // 生成目录 
		OutputStreamWriter xmlfile = new OutputStreamWriter(new FileOutputStream(
				System.getProperty("user.dir") + filepaths + "/controller/" + filename + "Controller.java", true), "UTF-8");
		PrintWriter pw = new PrintWriter(xmlfile);
		pw.println("package " + beantype + ";");
		pw.println("");
		pw.println("import java.util.HashMap;");
		pw.println("import java.util.List;");
		pw.println("import java.util.Map;");
		pw.println("");
		pw.println("import org.springframework.beans.factory.annotation.Autowired;");
		pw.println("import org.springframework.web.bind.annotation.RequestBody;");
		pw.println("import org.springframework.web.bind.annotation.RequestMapping;");
		pw.println("import org.springframework.web.bind.annotation.RequestMethod;");
		pw.println("import org.springframework.web.bind.annotation.RequestParam;");
		pw.println("import org.springframework.web.bind.annotation.RestController;");
		pw.println("");
		pw.println("import com.github.pagehelper.Page;");
		pw.println("import com.github.pagehelper.PageHelper;");
		pw.println("import com.fjhcit.common.kit.StringUtils;");
		pw.println("import com.fjhcit.model.ResultVO;");
		pw.println("import " + DO_paths.replace("/pojos/src/main/java/", "").replace("/", ".") + filename + "DO;");
		pw.println("import " + (filepaths.replace("/src/main/java/", "")).replace("/", ".") + "service." + filename + "Service;");
		pw.println("");
		pw.println("/**");
		pw.println(" * @description " + comments + "_控制器");
		pw.println(" * @author " + author);
		pw.println(" * @date " + new SimpleDateFormat("yyyy年MM月dd日 aHH:mm:ss").format(new Date()));
		pw.println(" */");
		pw.println("@RestController");
		pw.println("@RequestMapping(\"/" + conversion + "\")");
		pw.println("public class " + filename + "Controller{");
		pw.println("	@Autowired");
		pw.println("	private " + filename + "Service		" + conversion + "Service;  		//" + comments + "_业务接口");
		pw.println("");
		pw.println("	/**");
		pw.println("	 * @description 查" + comments + "列表数据");
		pw.println("	 */");
		pw.println("    @RequestMapping(value = \"/list" + filename + "\", method = RequestMethod.POST)");
		pw.println("	public ResultVO list" + filename + "(@RequestParam Map<String, String> param){");
		pw.println("    	return new ResultVO(this." + conversion + "Service.list" + filename + "(param),true,\"查询成功\");");
		pw.println("	}");
		pw.println("");
		pw.println("	/**");
		pw.println("	 * @description 查" + comments + "分页列表数据");
		pw.println("	 */");
		pw.println("    @RequestMapping(value = \"/list" + filename + "ByPaging\", method = RequestMethod.POST)");
		pw.println("	public ResultVO list" + filename + "ByPaging(@RequestParam Map<String, String> param){");
		pw.println("    	if(StringUtils.isEmpty(param.get(\"pageNum\"))){");
		pw.println("    		param.put(\"pageNum\", \"1\");");
		pw.println("    	}");
		pw.println("    	if(StringUtils.isEmpty(param.get(\"pageSize\"))){");
		pw.println("    		param.put(\"pageSize\", \"10\");");
		pw.println("    	}");
		pw.println("    	int pageNum = Integer.parseInt(param.get(\"pageNum\"));		//当前页");
		pw.println("    	int pageSize = Integer.parseInt(param.get(\"pageSize\"));//每页条数");
		pw.println("    	Page<Object> p = PageHelper.startPage(pageNum, pageSize);");
		pw.println("		List<" + filename + "DO> personList = this." + conversion + "Service.list" + filename + "(param);");
		pw.println("    	return new ResultVO(personList, true, \"查询成功\", p.getTotal(), pageNum, pageSize);");
		pw.println("	}");
		pw.println("");
		pw.println("	/**");
		pw.println("	 * @description 保存" + comments + "数据（新增、修改）");
		pw.println("	 */");
		pw.println("    @RequestMapping(value = \"/save" + filename + "\", method = RequestMethod.POST)");
		pw.println("	public ResultVO save" + filename + "(@RequestBody " + filename + "DO " + conversion + "DO) {");
		pw.println("    	ResultVO result;");
		pw.println("    	try {");
		pw.println("    		if(StringUtils.isEmpty(" + conversion + "DO.getId())) {");
		pw.println("    			this." + conversion + "Service.insert" + filename + "(" + conversion + "DO);");
		pw.println("    		}else {");
		pw.println("    			this." + conversion + "Service.update" + filename + "(" + conversion + "DO);");
		pw.println("    		}");
		pw.println("    		result = new ResultVO(true,true,\"保存成功\");");
		pw.println("		} catch (Exception e) {");
		pw.println("    		result = new ResultVO(false,false,\"保存失败\");");
		pw.println("        	e.printStackTrace();");
		pw.println("		}");
		pw.println("        return result;");
		pw.println("	}");
		pw.println("");
		pw.println("	/**");
		pw.println("	 * @description 删除" + comments + "数据");
		pw.println("	 */");
		pw.println("    @RequestMapping(value = \"/remove" + filename + "\", method = RequestMethod.POST)");
		pw.println("	public ResultVO remove" + filename + "(@RequestParam Map<String, String> param){");
		pw.println("    	ResultVO result;");
		pw.println("    	try {");
		pw.println("    		String ids = (String)param.get(\"ids\");");
		pw.println("    		String[] idsArr = ids.split(\",\");");
		pw.println("    		//删除");
		pw.println("    		Map<String,Object> delParam = new HashMap<String, Object>();");
		pw.println("    		delParam.put(\"idsArr\",idsArr);");
		pw.println("			this." + conversion + "Service.remove" + filename + "ByIdsArr(delParam);");
		pw.println("    		result = new ResultVO(true,true,\"删除成功\");");
		pw.println("    	} catch (Exception e) {");
		pw.println("    		result = new ResultVO(false,false,\"删除失败\");");
		pw.println("        	e.printStackTrace();");
		pw.println("        }");
		pw.println("        return result;");
		pw.println("	}");
		pw.print("}");
		pw.flush();
		xmlfile.close();
	}

	/**
	 * @description 把单词的头一个字母转换成小写
	 * @param str
	 * @return
	 */
	public String Conversion(String str) {
		String str1 = "";
		str1 = (str.substring(0, 1)).toLowerCase() + str.substring(1, str.length());
		if (str1.equals("string"))
			str1 = "id";// --
		return str1;
	}

	/**
	 * @description 把单词的头一个字母转换成大写
	 * @param str
	 * @return
	 */
	public String Uppercase(String str) {
		String str1 = "";
		str1 = (str.substring(0, 1)).toUpperCase() + str.substring(1, str.length());
		return str1;
	}

	/**
	 * @description 添加空格用于对齐注释
	 * @param l
	 * @return
	 */
	public String AddSpaces(int l) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < l; i++) {
			str.append(" ");
		}
		return str.toString();
	}

	/**
	 * @description 计算字符在字符串中出现的次数
	 * @param str 源字符串
	 * @param charStr 要查找的字符
	 */
	public int getCharInStr(String str, String charStr) {
		int lenStr = str.length();
		int lenChar = str.replaceAll(charStr, "").length();
		return (lenStr - lenChar) / charStr.length();
	}
	
	/**
	 * @description 获取LTX总工程目录地址
	 * @return
	 */
	public String findUserDir() {
		String user_dir = System.getProperty("user.dir");
		String[] user_dir_arr = user_dir.split("\\\\");
		int index = user_dir_arr.length-1; //删除最后一级的子模块目录名称
		if(index<0||index>=user_dir_arr.length) {
			System.out.println("没有对应的元素可删除");
		}
		String dir = "";
		for(int i=0;i<user_dir_arr.length;i++) {
			if(i==index) {
				continue;
			}
			dir += user_dir_arr[i] + "\\";
		}
		return dir;
	} 
}