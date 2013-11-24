package helpSystem.handlers.model;

import helpSystem.models.*;
import helpSystem.handlers.database.DatabaseHandler;
import helpSystem.handlers.database.CanNotWriteException;
import helpSystem.controllers.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Abstract class for different model handlers
 */
public abstract class ModelHandler {
    /**
     * ControllerMenu, you can use it to show menus and read values
     */
    protected final ControllerMenu menu;
    
    /**
     * You can use it to access the database
     */
    protected final DatabaseHandler dbHandler;
    
    /**
     * Constructor
     * @param menu menu
     * @param dbHandler database handler
     */
    public ModelHandler(ControllerMenu menu, DatabaseHandler dbHandler) {
        this.menu = menu;
        this.dbHandler = dbHandler;
    }
    
    /**
     * Read a model by using menu
     * @return Model
     * @throws CanNotPrintException
     * @throws CanNotReadException 
     */
    public abstract Model readModel() throws CanNotPrintException, CanNotReadException;
    
    /**
     * Read a model by using menu (and set default attribute automatically)
     * @param defaultAttrValue default attribute value
     * @return Model
     * @throws CanNotPrintException
     * @throws CanNotReadException 
     */
    public abstract Model readModel(String defaultAttrValue) throws CanNotPrintException, CanNotReadException;
    
    /**
     * Get the model name
     * @return model name
     */
    public abstract String getModelName();
    
    /**
     * Reads an attribute by using menu
     * @param attrName name of the attribute
     * @return attribute
     * @throws CanNotPrintException 
     * @throws CanNotReadException 
     * @throws UncorrectAttributeNameException if attribute is not supported by handler
     */
    public abstract Attribute readAttribute(String attrName) 
            throws CanNotPrintException, CanNotReadException, UncorrectAttributeNameException;
            
    /**
     * Read an attribute by using menu
     * @return Attribute
     * @throws CanNotPrintException
     * @throws CanNotReadException 
     */
    
    /**
     * Returns name of the default attribute
     * @return name of the default attribute
     */
    public abstract String getDefaultAttributeString();
    
    /**
     * Returns array of the attribute names which are supported by handler
     * @return array of the attribute names
     */
    public abstract String[] getAttributeNamesArray();
    
    /**
     * Imports models from another database
     * @param dbHandler database handler from which the data will be imported
     * @throws CanNotWriteException  
     */
    public abstract void importModels(DatabaseHandler dbHandler) throws CanNotWriteException;
    
    /**
     * Returns attribute with String value read by menu
     * @param name attribute name
     * @return Attribute
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected Attribute readStringAttribute(String name)
            throws CanNotPrintException, CanNotReadException {
        return new Attribute( name, readString("Enter " + name + ": ") );
    }
    
    /**
     * Returns attribute with int value read by menu
     * @param name attribute name
     * @return Attribute
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected Attribute readIntAttribute(String name) 
            throws CanNotPrintException, CanNotReadException {
        return new Attribute( name, Integer.toString(readInt("Enter " + name + ": ")) );
    }
    
    /**
     * Returns attribute with float value read by menu
     * @param name attribute name
     * @return Attribute
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected Attribute readFloatAttribute(String name) 
            throws CanNotPrintException, CanNotReadException {
        return new Attribute( name, Float.toString(readFloat("Enter " + name + ": ")) );
    }
    
    /**
     * Add the model to the database
     * @param model model to add
     * @return true if adding was successful, else false
     * @throws CanNotWriteException 
     */
    public boolean addModel(Model model) throws CanNotWriteException {
        return dbHandler.add(model);
    }
    
    /**
     * Modify a model (if it exists)
     * @param model model to modify
     * @param attribute attribute to modify
     * @return true if modifying was successful, else false
     * @throws CanNotWriteException 
     */
    public boolean modifyModel(Model model, Attribute attribute) throws CanNotWriteException {
        return dbHandler.modify(model, attribute);
    }
    
    /**
     * Remove a model (if it exists)
     * @param model model to remove
     * @return true if removing was successful, else false
     * @throws CanNotWriteException 
     */
    public boolean removeConcreteModel(Model model) throws CanNotWriteException {
        return dbHandler.removeConcrete(model);
    }
        
    /**
     * Find a model that matches all of the arguments
     * @param model model to find
     * @return found model if it was found, else null
     */
    public Model findConcreteModel(Model model) {
        return dbHandler.findConcrete(model);
    }
    
    /**
     * Find all models which attribute matches to argument 
     * @param attribute attribute to check
     * @return list with found models (could be empty)
     */
    public List<Model> findByAttribute(Attribute attribute) {
        return dbHandler.findByAttribute(attribute);
    }
    
    /**
     * Reads String value
     * @param text text to show
     * @return String value
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected String readString(String text) 
            throws CanNotPrintException, CanNotReadException {
        return menu.read(text);
    }
    
    /**
     * Reads float value
     * @param text text to show
     * @return float value
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected float readFloat(String text) 
            throws CanNotPrintException, CanNotReadException {
        while(true) {
            try {
                return menu.readFloat(text);
            }
            catch (NumberFormatException ex) {
                    menu.showError("Please enter the correct value.");
            }
        }
    }
    
    /**
     * Reads int value
     * @param text text to show
     * @return int value
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected int readInt(String text) 
            throws CanNotPrintException, CanNotReadException {
        while(true) {
            try {
                return menu.readInt(text);
            }
            catch (NumberFormatException ex) {
                    menu.showError("Please enter the correct value.");
            }
        }
    }
    
    /**
     * Creates empty List<Attribute>
     * @return empty list
     */
    protected List<Attribute> createEmptyAttributeList() {
        return new LinkedList<>();
    }
}