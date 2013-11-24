package helpSystem.models;

/**
 * Model attribute class
 */
public class Attribute {
    private String name;
    private String value;
    
    /**
     * Constructor.
     * @param name attribute name
     * @param value attribute value
     */
    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    /**
     * Set attribute name
     * @param name attribute name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get attribute name
     * @return attribute name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set attribute value
     * @param value attribute value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Get attribute value
     * @return attribute value
     */
    public String getValue() {
        return value;
    }
}
