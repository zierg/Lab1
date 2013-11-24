package helpSystem.handlers.database;

import helpSystem.models.*;
import java.util.List;

/**
 * Abstract class for different database handlers
 */
public abstract class DatabaseHandler {
    /**
     * Add a model to database (if it doesn't already exist)
     * @param model model to add
     * @return true if adding was successful, else false
     * @throws CanNotWriteException 
     */
    public abstract boolean add(Model model) throws CanNotWriteException;
    
    /**
     * Modify a model (if it exists)
     * @param oldModel model to modify
     * @param newModel model with changes which will be apply
     * @return true if modifying was successful, else false
     * @throws CanNotWriteException 
     */
    public abstract boolean modify(Model oldModel, Model newModel) throws CanNotWriteException;
    
    /**
     * Modify a model (if it exists)
     * @param model model to modify
     * @param attribute attribute to modify
     * @return true if modifying was successful, else false
     * @throws CanNotWriteException 
     */
    public abstract boolean modify(Model model, Attribute attribute) throws CanNotWriteException;
    
    /**
     * Remove a model (if it exists)
     * @param model model to remove
     * @return true if removing was successful, else false
     * @throws CanNotWriteException 
     */
    public abstract boolean removeConcrete(Model model) throws CanNotWriteException;
        
    /**
     * Find a model that matches all of the arguments
     * @param model model to find
     * @return found model if it was found, else null
     */
    public abstract Model findConcrete(Model model);
    
    /**
     * Find all models which attribute matches to argument 
     * @param attribute attribute to check
     * @return list with found models (could be empty)
     */
    public abstract List<Model> findByAttribute(Attribute attribute);
}
