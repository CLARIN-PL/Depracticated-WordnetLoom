package db.migration.commentParser;

public class ParserResult{

    private ResultType type;
    private String value;
    private String secondValue;

    public ParserResult(ResultType type, String value) {
        this.type = type;
        this.value = value;
    }

    public ParserResult(ResultType type, String value, String secondValue){
        this.type = type;
        this.value = value;
        this.secondValue = secondValue;
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSecondValue(){
        return secondValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }
}