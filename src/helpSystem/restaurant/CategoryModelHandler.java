package helpSystem.restaurant;

import helpSystem.controllers.*;
import helpSystem.models.*;
import helpSystem.handlers.database.*;
import helpSystem.handlers.model.*;
import java.util.List;
import java.util.ListIterator;

class CategoryModelHandler extends ModelHandler {
    protected static final String MODEL_NAME = "category";
    private static final String ATTR_NAME_STRING = "name";
    private static final String[] attrNamesArray = {
        ATTR_NAME_STRING
    };
    
    private static final String[] YES_NO_MENU = {
        "Yes",
        "No"
    };
    
    private ModelHandler dishHandler;   // for relations
    
    public CategoryModelHandler(ControllerMenu menu, DatabaseHandler dbHandler) {
        super(menu, dbHandler);
    }
    
    /**
     * Set dish hanler. You must set it before work with this handler.
     * @param dishHandler dish handler
     * @return true if argument is not null
     */
    public boolean setDishHandler(ModelHandler dishHandler) {
        if (this.dishHandler==null) {
            this.dishHandler = dishHandler;
            return true;
        }
        return false;
    }
    
    @Override
    public Model readModel() throws CanNotPrintException, CanNotReadException {
        return readModel(readAttribute(ATTR_NAME_STRING).getValue());
    }
    
    @Override
    public Model readModel(String defaultAttrValue) {
        List<Attribute> attrList = createEmptyAttributeList();
        attrList.add(new Attribute(ATTR_NAME_STRING, defaultAttrValue));
        return new Model(MODEL_NAME, attrList);
    }
    
    @Override
    public String getModelName() {
        return MODEL_NAME;
    }
    
    @Override
    public Attribute readAttribute(String attrName) 
            throws CanNotPrintException, CanNotReadException, UncorrectAttributeNameException {
        switch (attrName) {
            case ATTR_NAME_STRING: {
                return readStringAttribute(attrName);
            }
            default: {
                throw new UncorrectAttributeNameException();
            }
        }
    }
    
    @Override
    public void importModels(DatabaseHandler dbHandler) throws CanNotWriteException {
        CategoryModelHandler importHandler = new CategoryModelHandler(menu, dbHandler);
        importHandler.setDishHandler(dishHandler);
        Attribute attribute = new Attribute(importHandler.getDefaultAttributeString(), "*");
        List<Model> addList = importHandler.findByAttribute(attribute);
        ListIterator<Model> iterator = addList.listIterator();
        while(iterator.hasNext()) {
            addModel(iterator.next());
        }
    }
    
    @Override
    public String getDefaultAttributeString() {
        return ATTR_NAME_STRING;
    }
    
    @Override
    public String[] getAttributeNamesArray() {
        return attrNamesArray;
    }
    
    /* override for relations.
       asks to change category in dishes with modified category
     */
    @Override
    public boolean modifyModel(Model model, Attribute attribute) throws CanNotWriteException {
        boolean modified = dbHandler.modify(model, attribute);
        //if (attribute.getName().equals(ATTR_NAME_STRING)) {
        List<Model> foundDishes = dishHandler.findByAttribute(  // find all dishes with this category and ask to change them
                new Attribute(MODEL_NAME, model.getAttributeValue(ATTR_NAME_STRING)));
        if (foundDishes.size()>0) {
            try {
                menu.showMessage("There are dishes with this category. Change their category?");
                if (menu.showMenu(YES_NO_MENU) == 1) {  // answer == YES
                    ListIterator<Model> iterator = foundDishes.listIterator();
                    Attribute attrToChange = new Attribute(MODEL_NAME, attribute.getValue());
                    while(iterator.hasNext()) {
                        dishHandler.modifyModel(iterator.next(), attrToChange);
                    }
                }
            }
            catch (CanNotPrintException | CanNotReadException ex) {   
            }
        }
        //}
        return modified;
    }
    
    /* override for relations.
       asks to remove dishes with removed category
     */
    @Override
    public boolean removeConcreteModel(Model model) throws CanNotWriteException {
        boolean removed = dbHandler.removeConcrete(model);
        List<Model> foundDishes = dishHandler.findByAttribute(  // find all dishes with this category and ask to remove them
                new Attribute(MODEL_NAME, model.getAttributeValue(ATTR_NAME_STRING)));
        if (foundDishes.size()>0) {
            try {
                menu.showMessage("There are dishes with this category. Remove them?");
                if (menu.showMenu(YES_NO_MENU) == 1) {  // answer == YES
                    ListIterator<Model> iterator = foundDishes.listIterator();
                    while(iterator.hasNext()) {
                        dishHandler.removeConcreteModel(iterator.next());
                    }
                }
            }
            catch (CanNotPrintException | CanNotReadException ex) {   
            }
        }
        return removed;
    }
}