package helpSystem.controllers;

/**
 * Abstract class for different factories
 */
public abstract class ControllerElementsFactory {
    /**
     * Returns ControllerMenu
     * @return menu
     */
    public abstract ControllerMenu getMenu();
    
    /**
     * Returns ControllerViewer
     * @return viewer
     */
    public abstract ControllerViewer getViewer();
}
