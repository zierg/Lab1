package helpSystem.controllers;

import helpSystem.controllers.console.*;

/**
 * Factory, makes all necessary object for HelpSystemController
 */
public class ConsoleControllerFactory extends ControllerElementsFactory {
    private static ConsoleControllerFactory instance;   // Instance-singleton
   
    private ConsoleControllerFactory()  // to prevent creating several instances
    {}
    
    /**
     * Get the instance of ConsoleControllerFactory
     * @return ControllerElementsFactory instance
     */
    public static ControllerElementsFactory getInstance() {
        if (instance==null) {
            instance = new ConsoleControllerFactory();
        }
        return instance;
    }
    
    /**
     * Create a new ConsoleMenu
     * @return new menu
     */
    @Override
    public ControllerMenu getMenu() {
        return new ConsoleMenu();
    }
    
    /**
     * Create a new ConsoleViewer
     * @return new viewer
     */
    @Override
    public  ControllerViewer getViewer() {
        return new ConsoleViewer();
    }
}
