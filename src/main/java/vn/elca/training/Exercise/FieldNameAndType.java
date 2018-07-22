package vn.elca.training.Exercise;

@SuppressWarnings("rawtypes") 
public class FieldNameAndType {
	private final String fieldName;
    private final Class fieldType;

    public FieldNameAndType(String fieldName, Class fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getFieldType() {
        return fieldType;
    }
}
