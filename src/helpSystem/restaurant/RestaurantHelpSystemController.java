package helpSystem.restaurant;

import helpSystem.handlers.database.*;
import helpSystem.handlers.model.*;
import java.util.List;
import java.util.ListIterator;
import java.io.File;
import helpSystem.controllers.*;
import helpSystem.models.*;

/**
 * Help system controller that works with dishes and categories
 */
public class RestaurantHelpSystemController extends HelpSystemController {
    private final static String DISHES_FILENAME;
    private final static String CATEGORIES_FILENAME;
    
    static {
        String sep = File.separator;
        DISHES_FILENAME = "data" + sep + "dish_help_system" + sep + "dishes.xml";
        CATEGORIES_FILENAME = "data" + sep + "dish_help_system" + sep + "categories.xml";
    }
    
    private final ModelHandler dishHandler;
    private final ModelHandler categoryHandler;
    
    private final static String[] MAIN_MENU = {
         "Work with dishes",
         "Work with categories"
     };
    private final static String[] WORK_WITH_MODEL_MENU = {
        "Show all",
        "Find",
        "Add",
        "Import"
    };
    private static final String[] FIND_MODEL_MENU = {
        "Find concrete",
        "Find by attribute" + 
            "\n  (works with templates:" + 
            "\n  * - an arbitrary number of characters," +
            "\n  ? - one character"
    };
    private static final String[] OPERATE_ON_MODEL_MENU = {
        "Modify",
        "Remove"
    };
    private static final String[] IMPORT_MENU = {
        "Import from XML file"
    };
    /**
     * Constructor.
     * @param factory ControllerElementsFactory
     * @throws ControllerCreatingErrorException
     */
    public RestaurantHelpSystemController(ControllerElementsFactory factory) 
            throws ControllerCreatingErrorException {
        
        super(factory);

        try {
            DishModelHandler tempDishHandler = new DishModelHandler(menu, new XMLHandler("dish", DISHES_FILENAME));
            CategoryModelHandler tempCategoryHandler = new CategoryModelHandler(menu, new XMLHandler("category", CATEGORIES_FILENAME));
            tempDishHandler.setCategoryHandler(tempCategoryHandler);
            tempCategoryHandler.setDishHandler(tempDishHandler);
            dishHandler = tempDishHandler;
            categoryHandler = tempCategoryHandler;
        }
        catch (HandlerCreatingErrorException ex) {
            throw new ControllerCreatingErrorException();
        }
    }
    
    @Override
    public void start() throws ControllerWorkingErrorException {
        int selectedItem = menu.getCancelItem();
        String mainMenuTitle = "Main menu";
        do {
            try {
                menu.showMessage(mainMenuTitle);
                selectedItem = menu.showMenu(MAIN_MENU);
                leaveMainMenu(selectedItem);
            } catch (CanNotWriteException ex) {
                showError("Modifying database failed.");
            } catch (CanNotPrintException ex) {
                throw new ControllerWorkingErrorException();
            } catch (CanNotReadException ex) {
                showError("Reading failed.");
            }
        }while(selectedItem!=menu.getCancelItem());
    }
    
    /**
     * Decide where to go from main menu
     * @param selectedItem item that has been selected in the main menu
     */
    private void leaveMainMenu(int selectedItem) 
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        switch(selectedItem) {
            case 1: {
                workWithModel(dishHandler);
                break;
            }
            case 2: {
                workWithModel(categoryHandler);
                break;
            }
        }
    }
    
    /**
     * Select one of different operations with model and go next
     * @param handler handler for selected model
     */
    private void workWithModel(ModelHandler handler)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        int selectedItem;
        String title = "Working with " + handler.getModelName();
        do {
            menu.showMessage(title);
            selectedItem = menu.showMenu(WORK_WITH_MODEL_MENU);
            switch (selectedItem) {
                case 1: {
                    findByAttribute(handler, new Attribute(handler.getDefaultAttributeString(), "*"));
                    break;
                }
                case 2: {
                    findModelSelected(handler);
                    break;
                }
                case 3: {
                    addModelSelected(handler);
                    break;
                }
                case 4: {
                    importSelected(handler);
                    break;
                }
            }
        } while(selectedItem!=menu.getCancelItem());
    }
    

    /**
     * Tries to add model
     * @param handler handler for selected model
     */
    private void addModelSelected(ModelHandler handler) 
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        Model newModel = handler.readModel();
        if (handler.addModel(newModel)) {
            menu.showMessage(handler.getModelName() + " has been added");
        }
        else {
            menu.showMessage(handler.getModelName() + " already exists");
        }
    }

    /**
     * Select the way of search 
     * @param handler handler for selected model
     */
    private void findModelSelected(ModelHandler handler)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        switch (menu.showMenu(FIND_MODEL_MENU)) {
            case 1: {
                findConcreteSelected(handler);
                break;
            }
            case 2: {
                findByAttributeSelected(handler);
                break;
            }
        }
    }
    
    // Methods for finding -----------------------------------------
    /**
     * Find concrete model with reading all attributes
     * @param handler handler for selected model
     */
    private void findConcreteSelected(ModelHandler handler)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        Model foundModel = handler.findConcreteModel(handler.readModel());
        if (foundModel!=null) {
            viewer.showMessage("Found " + handler.getModelName() + ":");
            viewer.show(foundModel);
            operateOnModel(foundModel, handler);
        }
        else {
            menu.showMessage(handler.getModelName() + " doesn't exist.");
        }
    }
    
    /**
     * Find model with attribute which will be selected
     * @param handler handler for selected model
     */
    private void findByAttributeSelected(ModelHandler handler)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        String[] attrNames = handler.getAttributeNamesArray();
        int selectedItem = menu.showMenu(attrNames);
        if (selectedItem!=menu.getCancelItem()) {
            String attrName = attrNames[selectedItem-1];
            findByAttribute(handler, handler.readAttribute(attrName));
        }
        
    }
    
    /**
     * Find all models which match to the mask and ask for
     * operations on one of them (or remove them all)
     * @param handler handler for selected model
     * @param attribute attribute to find
     */
    private void findByAttribute(ModelHandler handler, Attribute attribute)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        List<Model> foundModels = handler.findByAttribute(attribute);
        int size = foundModels.size();
        if (size==0) {
            menu.showMessage(handler.getModelName() + " doesn't exist.");
        }
        else {
            viewer.show(foundModels);
            String[] foundMenu = new String[size+1];
            {   // fill the menu
                ListIterator<Model> iterator = foundModels.listIterator();
                for (int i = 0; i < size; i++) {
                    foundMenu[i] = iterator.next().getAttributeValue(handler.getDefaultAttributeString());//Integer.toString(i+1);
                }
            }
            foundMenu[size] = "remove all"; // add the last item of menu
            int selectedItem = menu.showMenu(foundMenu, "Select " + handler.getModelName() + " to operate: ");
            if (selectedItem==foundMenu.length) {           // remove all selected
                ListIterator<Model> iterator = foundModels.listIterator();
                while(iterator.hasNext()) {
                    removeModel(iterator.next(), handler);
                }
            }
            else if ( selectedItem!=menu.getCancelItem() ) {    // operate on selected model
                operateOnModel(foundModels.get(selectedItem-1), handler);
            }
        }
    }
    //---------------------------------- /Methods for finding
    
    // Methods to operate on model ----------------------------------
    /**
     * Offers modifying and removing for selected model
     * @param model selected model
     * @param handler handler for selected model
     */
    private void operateOnModel(Model model, ModelHandler handler) 
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        menu.showMessage("Selected " + handler.getModelName() + ":");
        viewer.show(model);
        switch(menu.showMenu(OPERATE_ON_MODEL_MENU)) {
            case 1: {
                modifyModelSelected(model, handler);
                break;
            }
            case 2: {
                removeModel(model, handler);
                break;
            }
        }
    }
    
    /**
     * Shows all attributes of the model and offers to change them
     * @param model selected model
     * @param handler handler for selected model
     */
    private void modifyModelSelected(Model model, ModelHandler handler)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        String[] attrNames = handler.getAttributeNamesArray();
        int cancelItem = menu.getCancelItem();
        while (true) {  // не сделал while (selectedItem!=cancelItem), так как selectedItem всё равно нужно проверять внутри цикла
            menu.showMessage("You can change the following attributes:");
            int selectedItem = menu.showMenu(attrNames);
            if (selectedItem==cancelItem) {
                break;
            }
            Attribute attrToChange = handler.readAttribute(attrNames[selectedItem-1]);
            if (handler.modifyModel(model, attrToChange)) {
                menu.showMessage(handler.getModelName() + " has been modified.");
                model.setAttributValue(attrToChange.getName(), attrToChange.getValue());
            }
            else {
                menu.showMessage("Some error occured.");
            }
        }
    }
    
    /**
     * Removes selected model
     * @param model selected model
     * @param handler handler for selected model
     */
    private void removeModel(Model model, ModelHandler handler) 
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        if (handler.removeConcreteModel(model)) {
            menu.showMessage(handler.getModelName() + " has been removed.");
        }
        else {
            menu.showMessage(handler.getModelName() + " doesn't exist.");
        }
    }
    // ----------------------------------/Methods to operate on model
    
    /**
     * Asks for way of import and go next
     * @param handler handler for selected model
     */
    private void importSelected(ModelHandler handler)
            throws CanNotWriteException, CanNotPrintException, CanNotReadException {
        switch (menu.showMenu(IMPORT_MENU)) {
            case 1: {
                importXML(handler);
                break;
            }
        }
    }
    
    /**
     * Imports model from XML file
     * @param handler handler for selected model
     */
    private void importXML(ModelHandler handler)
            throws CanNotPrintException, CanNotReadException {
        try {
            String filename = menu.read("Enter filename: ");
            File file = new File(filename);
            if (!file.exists()) {
                menu.showError("File is not found!");
                return;
            }
            handler.importModels(new XMLHandler(handler.getModelName(), filename));
            menu.showMessage("Import complete.");
        }
        catch (HandlerCreatingErrorException | CanNotWriteException ex) {
            menu.showError("Import error!");
        }
    }
}