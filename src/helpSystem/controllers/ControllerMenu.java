package helpSystem.controllers;

/**
 * Abstract class for different menus
 */
public abstract class ControllerMenu {    
    /**
     * Get int value that will be returned when "cancel" will be selected
     * @return cancel item value
     */
    public abstract int getCancelItem();
    
    /**
     * Shows menu, reads selected item
     * @param items array of menu items
     * @param text the text that will be shown when user is selecting item
     * @return selected item
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    public int showMenu(String[] items, String text) throws CanNotPrintException, CanNotReadException{
        showMenuItems(items);
        return selectItem(items, text);
    }
    
    /**
     * Shows menu, reads selected item
     * @param items array of menu items
     * @return selected item
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    public int showMenu(String[] items) throws CanNotPrintException, CanNotReadException{
        return showMenu(items, "Select item: ");
    }
    
    /**
     * Shows message
     * @param message the text of the message
     * @throws CanNotPrintException
     */
    public abstract void showMessage(String message) throws CanNotPrintException;
    
    /**
     * Shows error message
     * @param errorMessage the text of the message
     * @throws CanNotPrintException
     */
    public abstract void showError(String errorMessage) throws CanNotPrintException;
    
    /**
     * Reads String value
     * @param text text to show
     * @return read value
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    public abstract String read(String text)
            throws CanNotPrintException, CanNotReadException;
    
    /**
     * Reads String value
     * @return
     * @throws CanNotReadException
     */
    public abstract String read() throws CanNotReadException;
    
    /**
     * Reads int value
     * @param text text to show
     * @return read value
     * @throws CanNotPrintException
     * @throws CanNotReadException
     * @throws NumberFormatException
     */
    public int readInt(String text) 
            throws CanNotPrintException, CanNotReadException, NumberFormatException {
        return Integer.parseInt(read(text));
    }
    
    /**
     * Reads int value
     * @return read value
     * @throws CanNotReadException
     * @throws NumberFormatException
     */
    public int readInt() 
            throws CanNotReadException, NumberFormatException {
        return Integer.parseInt(read());
    }
    
    /**
     * Reads float value
     * @param text text to show
     * @return read value
     * @throws CanNotPrintException
     * @throws CanNotReadException
     * @throws NumberFormatException
     */
    public float readFloat(String text) 
            throws CanNotPrintException, CanNotReadException, NumberFormatException {
        return Float.parseFloat(read(text));
    }
    
    /**
     * Reads float value
     * @return read value
     * @throws CanNotReadException
     * @throws NumberFormatException
     */
    public float readFloat() 
            throws CanNotReadException, NumberFormatException {
        return Float.parseFloat(read());
    }
    
    /**
     * Shows menu items, required for showMenu()
     * @param items array of menu items
     * @throws CanNotPrintException
     */
    protected abstract void showMenuItems(String[] items) throws CanNotPrintException;
    
    /**
     * Reads selected item, required for showMenu()
     * @param items array of menu items
     * @param text text to show
     * @return selected item
     * @throws CanNotPrintException
     * @throws CanNotReadException
     */
    protected abstract int selectItem(String[] items, String text)
            throws CanNotPrintException, CanNotReadException;
}