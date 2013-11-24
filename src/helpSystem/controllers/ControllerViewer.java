package helpSystem.controllers;

import helpSystem.models.Model;
import java.util.List;

/**
 * Abstract class for different viewers
 */
public abstract class ControllerViewer {
    /**
     * Shows model's attributes
     * @param model model to show
     * @throws CanNotPrintException
     */
    public abstract void show(Model model) throws CanNotPrintException;
    
    /**
     * Show attributes of all models in the list
     * @param models list of models which will be shown
     * @throws CanNotPrintException
     */
    public abstract void show(List<Model> models) throws CanNotPrintException;
    
    /**
     * Shows message
     * @param message text of message
     * @throws CanNotPrintException
     */
    public abstract void showMessage(String message) throws CanNotPrintException;
}
