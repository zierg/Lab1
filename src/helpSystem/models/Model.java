package helpSystem.models;

import java.util.*;

/**
 * Model for use in ModelHandler and DatabaseHandler
 */
public class Model {
    private static final String SEPARATOR = ": ";
    private static final String END_LINE = "\n";
    private String name;
    private List<Attribute> attrs;
    
    /**
     * Constructor.
     * @param name model name
     * @param attrs attribute list
     */
    public Model(String name, List<Attribute> attrs) {
        this.name = name;
        this.attrs = attrs;
    }
    
    /**
     * Set model name
     * @param name model name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get model name
     * @return model name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set attribute value (if attribute exists)
     * @param attrName attribute name
     * @param value attribute value
     * @return true if attribute exists, else false
     */
    public boolean setAttributValue(String attrName, String value) {
        Attribute modifiableAttr = getAttrByName(attrName);
        if (modifiableAttr!=null) {
            modifiableAttr.setValue(value);
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Get attribute value (if attribute exists)
     * @param attrName attribute name
     * @return attribute valueif attribute exists, else null
     */
    public String getAttributeValue(String attrName) {
        Attribute temp = getAttrByName(attrName);
        if (temp != null) {
            return temp.getValue();
        }
        else {
            return null;
        }
    }
    
    /**
     * Get attribute list
     * @return attribute list
     */
    public List<Attribute> getAttributeList() {
        return attrs;
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        ListIterator<Attribute> iterator = attrs.listIterator();
        while(iterator.hasNext()) {
            Attribute next = iterator.next();
            str.append(next.getName());
            str.append(SEPARATOR);
            str.append(next.getValue());
            str.append(END_LINE);
        }
        return str.toString();
    }
    
    /**
     * Returns attribute with needed name
     * @param name attribute name
     * @return Attribute if exists, else false
     */
    private Attribute getAttrByName(String name) {
        ListIterator<Attribute> iterator = attrs.listIterator();
        while(iterator.hasNext()) {
            Attribute next = iterator.next();
            if ( next.getName().equals(name) ) {
                return next;
            }
        }
        return null;
    }
}
