package com.fjhcit.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @Description：导出Excel数据对象
 * @author：陈 麟
 * @date：2019年6月18日 下午20:59:37
 */
public class ExcelVO implements Serializable { 
	private static final long serialVersionUID = -4863649897025868378L;
	private String sheetName;			// 表格名称
	private String[] mergedRegionArr;	// 单元格跨行跨列数据
	private String titleName;			// 大标题名称
	private float[] rowHeight;			// 行高 ['大标题高度','表头高度','内容行高度']
	private List<String[]> tableHeadList;	// 表头行标题
	private int[] columnWidthArr;			// 每列的宽度
	private String[] dataFieldArr;			// 数据字段
	private String fileName;				// 文件名称

	@Override
	public String toString() {
		return "ExcelVO [sheetName=" + sheetName + ", titleName=" + titleName + ", fileName=" + fileName
				+ ", columnWidthArr=" + Arrays.toString(columnWidthArr) + ", mergedRegionArr="
				+ Arrays.toString(mergedRegionArr) + ", tableHeadList=" + tableHeadList + ", dataFieldArr="
				+ Arrays.toString(dataFieldArr) + "]";
	}
	
	//以下是get和set方法
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int[] getColumnWidthArr() {
		return columnWidthArr;
	}
	public void setColumnWidthArr(int[] columnWidthArr) {
		this.columnWidthArr = columnWidthArr;
	}
	public String[] getMergedRegionArr() {
		return mergedRegionArr;
	}
	public void setMergedRegionArr(String[] mergedRegionArr) {
		this.mergedRegionArr = mergedRegionArr;
	}
	public List<String[]> getTableHeadList() {
		return tableHeadList;
	}
	public void setTableHeadList(List<String[]> tableHeadList) {
		this.tableHeadList = tableHeadList;
	}
	public String[] getDataFieldArr() {
		return dataFieldArr;
	}
	public void setDataFieldArr(String[] dataFieldArr) {
		this.dataFieldArr = dataFieldArr;
	}
	public float[] getRowHeight() {
		return rowHeight;
	}
	public void setRowHeight(float[] rowHeight) {
		this.rowHeight = rowHeight;
	}
}