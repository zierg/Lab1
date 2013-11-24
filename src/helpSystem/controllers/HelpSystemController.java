package helpSystem.controllers;


/**
 * Abstract class for different controllers
 */
public abstract class HelpSystemController {
    /**
     * ControllerMenu, you can use it to show menus and read values
     */
    protected final ControllerMenu menu;
    
    /**
     * ControllerViewer, you can use it to show models
     */
    protected final ControllerViewer viewer;
    
    /**
     * Constructor.
     * @param factory ControllerElementsFactory
     */
    public HelpSystemController(ControllerElementsFactory factory) {
        this.menu = factory.getMenu();
        this.viewer = factory.getViewer();
    }
    
    /**
     * Starts controller's work.
     * @throws ControllerWorkingErrorException if some error occured
     */
    public abstract void start() throws ControllerWorkingErrorException;
    
    /**
     * Shows error text by using menu
     * @param errorText error text
     * @throws ControllerWorkingErrorException 
     */
    protected void showError(String errorText) throws ControllerWorkingErrorException {
        try {
            menu.showError(errorText);
        } catch (CanNotPrintException ex1) {
            throw new ControllerWorkingErrorException();
        }
    }
}
