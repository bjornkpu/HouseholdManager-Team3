package data;

public class StatisticsHelp {
    private String key;
    private double value;

    public StatisticsHelp(String key, double value){
        this.key=key;
        this.value=value;
    }

    public String getKey(){
        return key;
    }

    public double getValue(){
        return value;
    }

    public void setKey(String key){
        this.key=key;
    }

    public void setValue(int value){
        this.value=value;
    }
}
