package helpSystem.restaurant;

import helpSystem.handlers.model.*;
import helpSystem.controllers.*;
import helpSystem.handlers.database.CanNotWriteException;
import helpSystem.models.*;
import helpSystem.handlers.database.DatabaseHandler;
import java.util.List;
import java.util.ListIterator;

class DishModelHandler extends ModelHandler {
    protected static final String MODEL_NAME = "dish";
    private static final String ATTR_NAME_STRING = "name";
    private static final String ATTR_CATEGORY_STRING = CategoryModelHandler.MODEL_NAME;
    private static final String ATTR_PRICE_STRING = "price";
    private static final String[] attrNamesArray = {
        ATTR_NAME_STRING,
        ATTR_CATEGORY_STRING,
        ATTR_PRICE_STRING
    };
    
    private ModelHandler categoryHandler;   // for relations

    public DishModelHandler(ControllerMenu menu, DatabaseHandler dbHandler) {
        super(menu, dbHandler);
    }

    /**
     * Set category hanler. You must set it before work with this handler.
     * @param categoryHandler category handler
     * @return true if argument is not null
     */
    public boolean setCategoryHandler(ModelHandler categoryHandler) {
        if (this.categoryHandler==null) {
            this.categoryHandler = categoryHandler;
            return true;
        }
        return false;
    }

    @Override
    public Model readModel() throws CanNotPrintException, CanNotReadException {
        return readModel(readAttribute(ATTR_NAME_STRING).getValue());
    }
    
    @Override
    public Model readModel(String defaultAttrValue) 
            throws CanNotPrintException, CanNotReadException {
        List<Attribute> attrList = createEmptyAttributeList();
        attrList.add(new Attribute(ATTR_NAME_STRING, defaultAttrValue));
        attrList.add(readAttribute(ATTR_CATEGORY_STRING));
        attrList.add(readAttribute(ATTR_PRICE_STRING));
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
            case ATTR_NAME_STRING:
            case ATTR_CATEGORY_STRING: {
                return readStringAttribute(attrName);
            }
            case ATTR_PRICE_STRING: {
                return readFloatAttribute(attrName);
            }
            default: {
                throw new UncorrectAttributeNameException();
            }
        }
    }
    
    @Override
    public void importModels(DatabaseHandler dbHandler) throws CanNotWriteException {
        DishModelHandler importHandler = new DishModelHandler(menu, dbHandler);
        importHandler.setCategoryHandler(categoryHandler);
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
    
    /*
     * Override for relations
     * Adds category if added model if it doesn't exist
     */
    @Override
    public boolean addModel(Model model) throws CanNotWriteException {
        boolean added = dbHandler.add(model);
        try {
            categoryHandler.addModel(categoryHandler.readModel(model.getAttributeValue(ATTR_CATEGORY_STRING)));
        }
        catch (CanNotPrintException | CanNotReadException ex) {   
        }
        return added;
    }
    
    /*
     * Override for relations
     * Adds category if modified model if modified attribute is "category" and the category doesn't exist
     */
    @Override
    public boolean modifyModel(Model model, Attribute attribute) throws CanNotWriteException {
        boolean modified = dbHandler.modify(model, attribute);
        if (attribute.getName().equals(ATTR_CATEGORY_STRING)) {
            try {
                categoryHandler.addModel(categoryHandler.readModel(attribute.getValue()));
            }
            catch (CanNotPrintException | CanNotReadException ex) {   
            }
        }
        return modified;
    }
}