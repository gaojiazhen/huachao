package com.fjhcit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.fjhcit.common.kit.StringUtils;
import com.fjhcit.model.ExcelVO;

/**
 * @Description：POI导出、解析Excel工具类
 * @author：陈 麟
 * @date：2019年06月18日 下午16:17:35
 */
public class POIExcelUtils {
	
	/**
	 * @Description：POI导出Excel
	 * @param response：服务器响应对象 
	 * @param excelVO：excel自定义对象
	 * @param dataList：内容数据集合
	 */
	public static void exportExcel(HttpServletResponse response,ExcelVO excelVO,List<Map<String,String>> dataList) {
		ServletOutputStream sos = null;
    	try {
    		//一、创建表格
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();
			String sheetName = excelVO.getSheetName();
			if(StringUtils.isEmpty(sheetName)) {
				sheetName = "Sheet1";
			}
			HSSFSheet sheet = workbook.createSheet(sheetName);
			//二、单元格合并
			String[] mergedRegionArr = excelVO.getMergedRegionArr();
			if(null != mergedRegionArr) {
				for(int i=0;i<mergedRegionArr.length;i++) {
					String mergedRegio = mergedRegionArr[i];
					if(mergedRegio!=null) {
						String[] rowArr = mergedRegio.split(",");
						sheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(rowArr[0]), Integer.parseInt(rowArr[1]), Integer.parseInt(rowArr[2]), Integer.parseInt(rowArr[3])));
					}
				}
			}
			int rowIndex = 0;
			int colIndex = 0;
			float[] rowHeightArr = excelVO.getRowHeight();
			//三、创建第一行大标题
			String titleName = excelVO.getTitleName();
			if(StringUtils.isNotEmpty(titleName)) {
				HSSFRow row1 = sheet.createRow(rowIndex);
				row1.setHeightInPoints(rowHeightArr[0]);  //行高
				//20号粗体
				HSSFFont font20 = workbook.createFont();
				font20.setFontName("宋体");
				font20.setFontHeightInPoints((short)20);
				font20.setBold(true);
				//20号居中
				HSSFCellStyle center20 = workbook.createCellStyle();
				center20.setFont(font20);
				center20.setWrapText(true);
				center20.setAlignment(HorizontalAlignment.CENTER);
				center20.setVerticalAlignment(VerticalAlignment.CENTER);
				HSSFCell titleCell = row1.createCell(colIndex);
				titleCell.setCellValue(new HSSFRichTextString(excelVO.getTitleName()));
				titleCell.setCellStyle(center20);
				rowIndex++;
			}
			//四、创建表头
			List<String[]> headList = excelVO.getTableHeadList();
			if(StringUtils.isNotEmpty(headList)) {
				//11号粗体
				HSSFFont font11 = workbook.createFont();
				font11.setFontName("宋体");
				font11.setFontHeightInPoints((short)11);
				font11.setBold(true);
				//11号居中
				HSSFCellStyle center11 = workbook.createCellStyle();
				center11.setFont(font11);
				center11.setWrapText(true);
				center11.setAlignment(HorizontalAlignment.CENTER);
				center11.setVerticalAlignment(VerticalAlignment.CENTER);
				center11.setBorderLeft(BorderStyle.THIN);
				center11.setBorderRight(BorderStyle.THIN);
				center11.setBorderTop(BorderStyle.THIN);
				center11.setBorderBottom(BorderStyle.THIN);
				for(int i=0;i<headList.size();i++) {
					HSSFRow rowHead = sheet.createRow(rowIndex);
					rowHead.setHeightInPoints(rowHeightArr[1]);  //行高
					String[] headTextArr = headList.get(i);
					if(null!=headTextArr) {
						for(int j=0;j<headTextArr.length;j++) {
							if(i==0) {
								// (int)((50 + 0.72) * 256) 在EXCEL文档中实际列宽为50
								sheet.setColumnWidth(colIndex, (int)((excelVO.getColumnWidthArr()[j] + 0.72)  * 256)); 
							}
						    HSSFCell cell = rowHead.createCell(colIndex);
						    cell.setCellStyle(center11);
						    cell.setCellValue(new HSSFRichTextString(headTextArr[j]));
						    colIndex++;
						}
					}
					rowIndex++;
					colIndex = 0;
				}
			}
			//五、输出内容单元格
			if(StringUtils.isNotEmpty(dataList)) {
				//10号粗体
				HSSFFont font10 = workbook.createFont();
				font10.setFontName("宋体");
				font10.setFontHeightInPoints((short)10);
				//10号居中
				HSSFCellStyle center10 = workbook.createCellStyle();
				center10.setFont(font10);
				center10.setWrapText(true);
				center10.setAlignment(HorizontalAlignment.CENTER);
				center10.setVerticalAlignment(VerticalAlignment.CENTER);
				center10.setBorderLeft(BorderStyle.THIN);
				center10.setBorderRight(BorderStyle.THIN);
				center10.setBorderTop(BorderStyle.THIN);
				center10.setBorderBottom(BorderStyle.THIN);
				String[] dataFieldArr = excelVO.getDataFieldArr();
				for(int i=0;i<dataList.size();i++) {
					colIndex = 0;
					Map<String,String> map = dataList.get(i);
					HSSFRow rowData = sheet.createRow(rowIndex);
					rowData.setHeightInPoints(15f);  //行高
					for(int j=0;j<dataFieldArr.length;j++) {
						HSSFCell cell = rowData.createCell(colIndex++);
						if("SEQUENCE".contentEquals(dataFieldArr[j])) {  //编号列，自动计算
							cell.setCellValue(i+1);
						}else {
							String value = map.get(dataFieldArr[j]);
							cell.setCellValue(new HSSFRichTextString(value));
						}
						cell.setCellStyle(center10);
					}
				    rowIndex++;
				}
			}
			//二进制输出流 
			//response.setContentType("application/octet-stream");  //警告：Resource interpreted as Document but transferred with MIME type application/octet-stream: 
			//response.setContentType("application/x-download");
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("text/html; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			String filename = new String(excelVO.getFileName().getBytes("GBK"), "ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
			//刷新缓冲区
			response.flushBuffer();
			sos = response.getOutputStream();
			//workbook将Excel写入到response的输出流中，供页面下载
			workbook.write(sos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null!=sos) {
					//sos.flush();   //慎用，可能会内存溢出
					sos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 公共的导入excel方法
	 * @param file      文件
	 * @param obj       实体类
	 * @param startRowNum       从第几行开始读取数据，从0开始数
	 * @param startColNum       从第几列开始读取数据，从0开始数（可以跳过自增编号列）
	 * @return
	 * @throws IOException
	 */
	public List<Object> importExcel(MultipartFile file, Object obj, int startRowNum, int startColNum) throws Exception {
		List<Object> list = new ArrayList<Object>();
		Workbook workbook = null;
		// 读取文件内容
		workbook = this.chooseWorkbook(file);
		// 获取工作表
		Sheet sheet = workbook.getSheetAt(0);
		// 获取sheet中第一行行号（空行不读取）
		int firstRowNum = sheet.getFirstRowNum();
		// 获取sheet中最后一行行号
		int lastRowNum = sheet.getLastRowNum();
		// 获取该实体所有定义的属性 返回Field数组
		Class<? extends Object> cls = obj.getClass();
		java.lang.reflect.Field[] entityName = cls.getDeclaredFields();
		String classname = cls.getName();
		Class<?> clazz = Class.forName(classname);
		//1、判断模板是否正确
		Row oneRow = sheet.getRow(0);
		if (entityName.length == oneRow.getLastCellNum()-startColNum) {   //减掉序号列
			for (int columnIndex = 0; columnIndex < oneRow.getLastCellNum()-1; columnIndex++) {
				Cell cell = oneRow.getCell(columnIndex+startColNum);
				if (!entityName[columnIndex].getName().equalsIgnoreCase(getVal(cell))) {
					throw new Exception("当前Excel表和模板不匹配！");
		        }
			}
		} else {
			throw new Exception("当前Excel表和模板不匹配！");
		}

		// 循环读取数据（第三行开始）
		for (int i = firstRowNum + startRowNum; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			// 可以根据该类名生成Java对象
			Object pojo = clazz.newInstance();
			// 除自增编号外，实体字段匹配sheet列
			for (int j = 0; j < entityName.length; j++) {
				// 获取属性的名字,将属性的首字符大写，方便构造set方法
				String name = "set" + entityName[j].getName().substring(0, 1).toUpperCase().concat(entityName[j].getName().substring(1).toLowerCase());
				// 获取属性的类型
				String type = entityName[j].getGenericType().toString();
				// getMethod只能调用public声明的方法，而getDeclaredMethod基本可以调用任何类型声明的方法
				Method m = obj.getClass().getDeclaredMethod(name, entityName[j].getType());
				Cell pname = row.getCell(j+startColNum);
				// 根据属性类型装入值
				switch (type) {
				case "char":
				case "java.lang.Character":
				case "class java.lang.String":
					m.invoke(pojo, getVal(pname));
					break;
				case "int":
				case "class java.lang.Integer":
					m.invoke(pojo, Integer.valueOf(getVal(pname)));
					break;
				case "class java.util.Date":
					m.invoke(pojo, getVal(pname));
					break;
				case "float":
				case "double":
				case "java.lang.Double":
				case "java.lang.Float":
				case "java.lang.Long":
				case "java.lang.Short":
				case "java.math.BigDecimal":
					m.invoke(pojo, Double.valueOf(getVal(pname)));
					break;
				default:
					break;
				}
			}
			list.add(pojo);
		}
		if(null!=workbook) {
			workbook.close();
		}
		return list;
	}
	
	/**
	 * 根据上传文件判断Excel版本
	 * @return
	 * @throws Exception
	 */
	public Workbook chooseWorkbook(MultipartFile file) throws Exception {
		Workbook workbook = null;
		String filename = file.getOriginalFilename();
		String fileType = (filename.substring(filename.lastIndexOf("."), filename.length())).toLowerCase();
		InputStream is = file.getInputStream();
		if (fileType.endsWith(".xls")) {	// 2003- 版本的excel
			workbook = new HSSFWorkbook(is); // 2003-
		} else if (fileType.endsWith(".xlsx")) {	// 2007+ 版本的excel
			workbook = new XSSFWorkbook(is); // 2007+
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return workbook;
	}

	/**
	 * 处理类型
	 * @param cell
	 * @return
	 */
	public static String getVal(Cell cell) {
		if (null != cell) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC: // 数字
//				String val = cell.getNumericCellValue() + "";
//				int index = val.indexOf(".");
//				if (Integer.valueOf(val.substring(index + 1)) == 0) {
//					DecimalFormat df = new DecimalFormat("0");// 处理科学计数法
//					return df.format(cell.getNumericCellValue());
//				}
//				return cell.getNumericCellValue() + "";// double
				if (DateUtil.isCellDateFormatted(cell)) {
		            Date date = cell.getDateCellValue();
		            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		            return formater.format(date);
		        } else if (String.valueOf(cell.getNumericCellValue()).contains(".")) {
		            DecimalFormat df = new DecimalFormat("#");
		            return df.format(cell.getNumericCellValue());
		        } else {
		        	return (cell + "").trim();
		        }
			case STRING: // 字符串
				return cell.getStringCellValue() + "";
			case BOOLEAN: // Boolean
				return cell.getBooleanCellValue() + "";
			case FORMULA: // 公式
				try {
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						return sdf.format(date);
						//return (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
					} else {
						return String.valueOf((int) cell.getNumericCellValue());
					}
				} catch (IllegalStateException e) {
					return String.valueOf(cell.getRichStringCellValue());
				}
			case BLANK: // 空值
				return "";
			case ERROR: // 故障
				return "";
			default:
				return "未知类型   ";
			}
		} else {
			return "";
		}
	}
}