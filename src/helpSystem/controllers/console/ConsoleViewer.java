package helpSystem.controllers.console;

import helpSystem.models.Model;
import helpSystem.controllers.*;
import java.io.*;
import java.util.List;
import java.util.ListIterator;

/**
 * Viewer that uses console to write
 */
public class ConsoleViewer extends ControllerViewer {
    private final BufferedWriter writer=  new BufferedWriter(new OutputStreamWriter(System.out)); // stream to write
    private final static String ENDL = "\n";    // end of line
    private static final String SPACE = " ";
    
    @Override
    public void show(Model model) throws CanNotPrintException {
        try {
            writer.write (model.toString() + ENDL);
            writer.flush();
        } 
        catch (IOException ex) {
            throw new CanNotPrintException();
        }
    }
    
    @Override
    public void show(List<Model> models) throws CanNotPrintException {
        try {
            int i = 1;
            ListIterator<Model> iterator = models.listIterator();
            while (iterator.hasNext()) {
                Model next = iterator.next();
                writer.write(next.getName() + SPACE + i + ENDL + next.toString() + ENDL);
                i++;
            }
            writer.flush();
        } 
        catch (IOException ex) {
            throw new CanNotPrintException();
        }
    }
    
    @Override
    public void showMessage(String message) throws CanNotPrintException {
        try {
            writer.write(message + ENDL);
            writer.flush();
        } 
        catch (IOException ex) {
            throw new CanNotPrintException();
        }
    }
}