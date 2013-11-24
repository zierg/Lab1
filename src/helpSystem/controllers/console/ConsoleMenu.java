package helpSystem.controllers.console;

import helpSystem.controllers.*;
import java.io.*;

/**
 * Menu that uses console to read and write
 */
public class ConsoleMenu extends ControllerMenu {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // stream to read
    private final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out)); // stream to write
    private final static String endl = "\n";    // end of line
    private static final int CANCEL_ITEM = 0;    
    
    @Override
    public int getCancelItem() {
        return CANCEL_ITEM;
    }
    
    @Override
    public void showMessage(String message) throws CanNotPrintException {
        write(message + endl);
    }
    
    @Override
    public void showError(String errorMessage) throws CanNotPrintException {
        showMessage(errorMessage);
    }
    
    @Override
    public String read(String text) 
            throws CanNotPrintException, CanNotReadException {
        try {
            write(text);
            return reader.readLine();
        } catch (IOException ex) {
             throw new CanNotReadException();
        }
    }
      
    @Override
    public String read() throws CanNotReadException {
        try {
            return reader.readLine();
        } catch (IOException ex) {
             throw new CanNotReadException();
        }
    }

    /**
     * Shows menu items and calcel item
     * @param items menu items
     * @throws CanNotPrintException 
     */
    @Override
    protected void showMenuItems(String[] items) throws CanNotPrintException {
        for (int i = 0; i < items.length; i++) {
            String menuItem = (i+1) + ". " + items[i] + endl;
            write(menuItem);
        }
        write(0 + ". Cancel." + endl);
    }

    /**
     * Tries to read selected item until item isn't entered correctly
     * @param items menu items
     * @param text text to show
     * @return selected item ( (1 < selected Item < amount of items) or cancel item)
     * @throws CanNotPrintException
     * @throws CanNotReadException 
     */
    @Override
    protected int selectItem(String[] items, String text) throws CanNotPrintException, CanNotReadException {
        int maxItem = items.length;
        int selectedItem = maxItem + 1;
        
        boolean firstTry = true;
        while ((selectedItem > maxItem || selectedItem <= 0) && selectedItem!=CANCEL_ITEM) {
            if (!firstTry) {
                showError("Please enter the correct menu item number.");
            }
            try {
                selectedItem = readInt(text);
                firstTry = false;
            }
            catch (NumberFormatException ex) {
                selectedItem = maxItem + 1;
                firstTry = false;
            }
        }
        return selectedItem; 
    }
    
    /**
     * Writes string to the output stream
     * @param str string to write
     * @throws CanNotPrintException 
     */
    private void write(String str)  throws CanNotPrintException {
        try {
            writer.write(str);
            writer.flush();
        } catch (IOException ex) {
            throw new CanNotPrintException();
        }
    }
}