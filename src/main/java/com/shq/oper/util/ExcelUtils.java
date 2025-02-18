package com.shq.oper.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtils {
	
	public static List<String> readTxtFile(String filePath) throws Exception {
		
		List<String> result = new ArrayList<String>(); 
		String encoding = "GBK";
		
		File file = new File(filePath);
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				result.add(lineTxt);
			}
			read.close();
		} else {
			return null;
		}
		
		return result;
	}


	public static <T> List<T> getData(File file, Class<T> cls) throws Exception {
		String[][] datas = getData(file, 0);
		List<T> beans = new ArrayList<T>();
		List<String> cols = new ArrayList<String>();
		for (int i = 0; i < datas.length; i++) {
			String[] rows = datas[i];
			if(i == 0){
				for (int j = 0; j < rows.length; j++) {
					// 第一行为列名，对应实体的字段名
					if(StringUtils.isNotBlank(rows[j])){
						cols.add(j, rows[j]);
					}
				}
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0, size = cols.size(); j < size; j++) {
					String cell = rows[j], colName = cols.get(j);
					Class<?> fieldType = cls.getDeclaredField(colName).getType();
					// 根据类型转换相应的值
					if(BigDecimal.class.isAssignableFrom(fieldType)){
						map.put(colName, NumberUtils.createBigDecimal(cell));
					}else if(Integer.class.isAssignableFrom(fieldType)){
						map.put(colName, NumberUtils.createInteger(cell));
					}else if(Long.class.isAssignableFrom(fieldType)){
						map.put(colName, NumberUtils.createLong(cell));
					}else if(Timestamp.class.isAssignableFrom(fieldType)){
						map.put(colName, parseTimestamp(cell));
					}else{
						map.put(colName, cell);
					}
				}
				T bean = cls.newInstance();
				BeanUtils.populate(bean, map);
				beans.add(bean);
			}
		}
		return beans;
	}
	/**
	 * 
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @return 读出的Excel中数据的内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String[][] getData(File file, int ignoreRows) throws FileNotFoundException, IOException {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		// 打开HSSFWorkbook
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum() + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
					String value = "";
					cell = row.getCell(columnIndex);
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码
//						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								if (date != null) {
									value = new SimpleDateFormat("yyyy-MM-dd").format(date);
								} else {
									value = "";
								}
							} else {
								value = new DecimalFormat("#.##").format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (!cell.getStringCellValue().equals("")) {
								value = cell.getStringCellValue();
							} else {
								value = cell.getNumericCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = (cell.getBooleanCellValue() == true ? "Y" : "N");
							break;
						default:
							value = "";
						}
					}
					if (columnIndex == 0 && value.trim().equals("")) {
						break;
					}
					values[columnIndex] = rightTrim(value);
					values[columnIndex] = value.trim();
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
		}
		in.close();
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = (String[]) result.get(i);
		}
		return returnArray;
	}

	/**
	 * 
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

	public static HSSFWorkbook exportExcel(String sheetName, List<Map<String, Object>> list, String[] titles, String[] fieldNames) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = null;
			// 对每个表生成一个新的sheet,并以表名命名
			if (sheetName == null) {
				sheetName = "sheet1";
			}
			sheet = wb.createSheet("sheet1");
			// 设置表头的说明
			HSSFRow topRow = sheet.createRow(0);

			// 设置列宽
			// sheet.setColumnWidth((short) 0, (short) 2000);
			// sheet.setColumnWidth((short) 1, (short) 5000);
			// sheet.setColumnWidth((short) 2, (short) 5000);
			// sheet.setColumnWidth((short) 3, (short) 2000);
			// sheet.setColumnWidth((short) 4, (short) 7000);
			// sheet.setColumnWidth((short) 5, (short) 7000);
			for (int i = 0; i < titles.length; i++) {
				setCellGBKValue(topRow.createCell((short) i), titles[i]);
			}
			if(list !=null && list.size()>0){
				int k = 1;
				for (Map<String, Object> map : list) {
					HSSFRow row = sheet.createRow(k);
					for (int i = 0; i < fieldNames.length; i++) {
						setCellGBKValue(row.createCell((short) i), map.get(fieldNames[i]) + "");
					}
					k++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	public static<T> HSSFWorkbook exportObjectExcel(String sheetName, List<T> list, String[] titles, String[] fieldNames) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = null;
			// 对每个表生成一个新的sheet,并以表名命名
			if (sheetName == null) {
				sheetName = "sheet1";
			}
			sheet = wb.createSheet("sheet1");
			// 设置表头的说明
			HSSFRow topRow = sheet.createRow(0);

			// 设置列宽
			// sheet.setColumnWidth((short) 0, (short) 2000);
			// sheet.setColumnWidth((short) 1, (short) 5000);
			// sheet.setColumnWidth((short) 2, (short) 5000);
			// sheet.setColumnWidth((short) 3, (short) 2000);
			// sheet.setColumnWidth((short) 4, (short) 7000);
			// sheet.setColumnWidth((short) 5, (short) 7000);
			for (int i = 0; i < titles.length; i++) {
				setCellGBKValue(topRow.createCell((short) i), titles[i]);
			}
			if(list !=null && list.size()>0){
				int k = 1;
				for (T t : list) {
					HSSFRow row = sheet.createRow(k);
					for (int i = 0; i < fieldNames.length; i++) {
						if(t instanceof Map){
							
							setCellGBKValue(row.createCell((short) i), ((Map) t).get(fieldNames[i]).toString());
						}else{
							
							setCellGBKValue(row.createCell((short) i), ReflectUtil.getFieldValue(t, fieldNames[i]) + "");
						}
					}
					k++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	private static void setCellGBKValue(HSSFCell cell, String value) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		// 设置CELL的编码信息
//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (value == null || "".equals(value) || "null".equals(value)) {
			cell.setCellValue("");
		} else {
			cell.setCellValue(value);
		}
	}
	
	
	public static Timestamp parseTimestamp(String s) {
		return parseTimestamp(s, null);
	}
	
	public static Timestamp parseTimestamp(String s, String format) {
		if (!StringUtils.isEmpty(s)) {
			s = s.trim();
			SimpleDateFormat sdf = null;
			if (StringUtils.isEmpty(format)) {
				format = "";
				String flag = "-";
				if (s.contains("/")) {
					flag = "/";
				}
				String[] dts = s.split("/|-");
				for (int i = 0; i < dts[0].length(); i++) {
					format += 'y';
				}
				format += flag;
				for (int i = 0; i < dts[1].length(); i++) {
					format += 'M';
				}
				format += flag;
				for (int i = 0; i < dts[2].length(); i++) {
					format += 'd';
				}

				StringBuilder fmt = new StringBuilder(format);
				if (s.length() > fmt.length()) {
					fmt.append(" HH");
				}
				if (s.length() > fmt.length()) {
					fmt.append(":mm");
				}
				if (s.length() > fmt.length()) {
					fmt.append(":dd");
				}
				sdf = new SimpleDateFormat(fmt.toString());
			} else {
				sdf = new SimpleDateFormat(format);
			}
			try {
				Timestamp t = new Timestamp(sdf.parse(s).getTime());
				return t;
			} catch (ParseException e) {
				log.error("prase time error", e);
			}
		}
		return null;
	}
}
