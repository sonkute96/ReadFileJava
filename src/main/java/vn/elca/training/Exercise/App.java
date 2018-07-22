package vn.elca.training.Exercise;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class App {
    
    private static final String PATH = "/Users/phamminhson/Desktop/test.xlsx";
	private InputStream inputStream = null;
	private FieldNameAndType[] nameAndTypes = null;
	
	public static void main( String[] args ) {
		App app = new App();
		long beforeTime = System.currentTimeMillis();
		long beforeMemory = Runtime.getRuntime().totalMemory();
		app.setup();
		app.readFile();
		long afterMemory = Runtime.getRuntime().totalMemory();
		long afterTime = System.currentTimeMillis();
		System.out.println("Total memory: " + (afterMemory - beforeMemory));
		System.out.println("Total time: " + (afterTime - beforeTime));
	}
	public void readFile() {
		try {
			ParseExcelSheetToList<Student> list = new ParseExcelSheetToList<Student>(inputStream, Student.class, nameAndTypes);
			List<Student> listStudents = list.getList();
			if (listStudents != null && !listStudents.isEmpty()) {
				listStudents.forEach(System.out::println);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void setup() {
		if (inputStream == null) {
			try {
				inputStream = new FileInputStream(PATH);
			} catch (FileNotFoundException e) {
				System.out.println("File not found exception: " + e);
			}
		}
		if (nameAndTypes == null) {
			nameAndTypes = new FieldNameAndType[12];
			nameAndTypes[0] = new FieldNameAndType("first_name", String.class);
			nameAndTypes[1] = new FieldNameAndType("last_name", String.class);
			nameAndTypes[2] = new FieldNameAndType("company_name", String.class);
			nameAndTypes[3] = new FieldNameAndType("address", String.class);
			nameAndTypes[4] = new FieldNameAndType("city", String.class);
			nameAndTypes[5] = new FieldNameAndType("county", String.class);
			nameAndTypes[6] = new FieldNameAndType("state", String.class);
			nameAndTypes[7] = new FieldNameAndType("zip", int.class);
			nameAndTypes[8] = new FieldNameAndType("phone1", String.class);
			nameAndTypes[9] = new FieldNameAndType("phone2", String.class);
			nameAndTypes[10] = new FieldNameAndType("email", String.class);
			nameAndTypes[11] = new FieldNameAndType("web", String.class);
		}
	}
}
