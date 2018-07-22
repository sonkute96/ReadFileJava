package vn.elca.training.Exercise;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@SuppressWarnings("rawtypes")
public class ParseExcelSheetToList<T> {
	private final Class<T> type;
	private final FieldNameAndType[] fieldNameAndType;
	@SuppressWarnings({ "unused"})
	private final InputStream inputStream;
	private final Sheet sheet;
	private List<T> list;
	
	public ParseExcelSheetToList(InputStream inputStream, Class<T> type, FieldNameAndType[] fieldNameAndTypes) throws Exception {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream can not been null");
		}
		this.inputStream = inputStream;
		if (type == null) {
			throw new IllegalArgumentException("Type can not been null");
		}
		this.type = type;
		if (fieldNameAndTypes == null) {
			throw new IllegalArgumentException("Field name and types can not bene null");
		}
		this.fieldNameAndType = fieldNameAndTypes;
		
		try {
			Workbook workbook = WorkbookFactory.create(inputStream);
			sheet = workbook.getSheetAt(0);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new Exception("Close inputStream failed");
				}
			}
		}
	}
	public List<T> getList() {
		if (list == null) {
			list = traverseSheet();
		}
		return list;
	}
	private List<T> traverseSheet() {
		List<T> list = new ArrayList<T>();
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			list.add(parseRecord(row));
		}
		return list;
	}
	private T parseRecord(Row row) {
		T targetObject= null;
		try {
			targetObject = type.newInstance();
		} catch (InstantiationException e) {
			System.out.println("Instantiation exception: " + e);
		} catch (IllegalAccessException e) {
			System.out.println("Illegal access exception: " + e);
		}
		for (int i = 0; i < fieldNameAndType.length; i++) {
			Cell cell = row.getCell(i);
			Object value = null;
			try {
				Method method = targetObject.getClass().getMethod("set" + ParseExcelSheetToList.capitalize(fieldNameAndType[i].getFieldName()),
						fieldNameAndType[i].getFieldType());
				
				Class parameterType = fieldNameAndType[i].getFieldType();
				if (int.class == parameterType || Integer.class == parameterType) {
					value = (int) cell.getNumericCellValue();
				}
				if (double.class == parameterType || Double.class == parameterType) {
					value = cell.getNumericCellValue();
				}
				if (float.class == parameterType || Float.class == parameterType) {
					value = cell.getNumericCellValue();
				}
				if (String.class == parameterType) {
					value = cell.getStringCellValue();
				}
				method.invoke(targetObject, value);
			} catch (InvocationTargetException e) {
                System.out.println("Invocation target exception: " + e);
            } catch (IllegalAccessException e) {
            	System.out.println("Illegal access exception: " + e);
            } catch (NoSuchMethodException e) {
            	System.out.println("No such method exception: " + e);
            }
		}
		return targetObject;
	}
	private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return Character.toTitleCase(s.charAt(0)) + s.substring(1, s.length());
    }
}
