package com.stratio.sdh.meta.structures;

public class Option {
    
    public static final int OPTION_PROPERTY = 1;
    public static final int OPTION_COMPACT = 2;
    public static final int OPTION_CLUSTERING = 3;
                    
    private int fixedOption;
    private String nameProperty;
    private ValueProperty valueProperty;

    public Option(int fixedOption, String nameProperty, ValueProperty properties) {
        this.fixedOption = fixedOption;
        this.nameProperty = nameProperty;
        this.valueProperty = properties;
    }    
    
    public Option(int fixedOption) {
        this(fixedOption, null, null);
    }   
    
    public Option(String nameProperty, ValueProperty properties) {
        this.fixedOption = OPTION_PROPERTY;
        this.nameProperty = nameProperty;
        this.valueProperty = properties;
    }   
    
    public int getFixedOption() {
        return fixedOption;
    }

    public void setFixedOption(int fixedOption) {
        this.fixedOption = fixedOption;
    }

    public String getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty = nameProperty;
    }

    public ValueProperty getProperties() {
        return valueProperty;
    }

    public void setProperties(ValueProperty properties) {
        this.valueProperty = properties;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch(fixedOption){
            case OPTION_PROPERTY:
                sb.append(nameProperty).append(" = ").append(valueProperty.toString());
                break;
            case OPTION_COMPACT:
                sb.append("COMPACT STORAGE");
                break;
            case OPTION_CLUSTERING:
                sb.append("CLUSTERING ORDER");
                break;
        }
        return sb.toString();
    }
                
}
