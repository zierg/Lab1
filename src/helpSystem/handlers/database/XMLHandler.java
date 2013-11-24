package helpSystem.handlers.database;

import helpSystem.models.*;
import java.util.*;
import java.io.*;
import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Works with XML database
 * @author Komissarov Ivan
 */
public class XMLHandler extends DatabaseHandler {
    private static final String ROOT_NAME = "database";     // uses to create XML-file
    private final String filename;      // name of XML file
    private final String modelName;     // name of model stored in this database
    
    private final Document document;    // XML document
    private final Element root;         // root of XML document
    
    private final XMLOutputter outputter;   // to save document
    
    /**
     * Constructor
     * @param modelName name of data
     * @param filename database file (will be created if it doesn't exist)
     * @throws HandlerCreatingErrorException 
     */
    public XMLHandler(String modelName, String filename) throws HandlerCreatingErrorException {

        this.filename = filename;
        this.modelName = modelName;
        
        try {
            SAXBuilder builder = new SAXBuilder();
            File file = new File(filename);

            outputter = new XMLOutputter( Format.getPrettyFormat().setIndent("\t") );

            if (file.exists()) {
                document = builder.build(file);
                root = document.getRootElement();
            }
            else {
                root = new Element(ROOT_NAME);
                document = new Document(root);
                saveDocument();
            } 
        } 
        catch (JDOMException | IOException | CanNotWriteException ex) {
            throw new HandlerCreatingErrorException();
        }     
    }
    
    @Override
    public boolean add(Model model) throws CanNotWriteException {
        if ( !(model.getName().equals(modelName)) ) {
            throw new CanNotWriteException();   // to prevent writing different models into the one file
        }
        if (findElement(model)==null) {
            root.addContent(createModelElement(model));
            saveDocument();
            return true;
        }
        else {
            return false;
        }
    }

    @Override   // в данной программе пока не используется
    public boolean modify(Model oldModel, Model newModel) throws CanNotWriteException {
        Element modifyableElement = findElement(oldModel);
        if (modifyableElement==null) {
            return false;
        }
        else {
            setElementAttrs(modifyableElement, newModel);
            saveDocument();
            return true;
        }
    }

    @Override
    public boolean modify(Model model, Attribute attribute) throws CanNotWriteException {
        Element modifyableElement = findElement(model);
        if (modifyableElement==null) {
            return false;
        }
        else {
            modifyableElement.setAttribute(attribute.getName(), attribute.getValue());
            saveDocument();
            return true;
        }
    }

    @Override
    public boolean removeConcrete(Model model) throws CanNotWriteException {
        Element removableElement = findElement(model);
        if (removableElement==null) {
            return false;
        }
        else {
            removeElement(removableElement);
            saveDocument();
            return true;
        }
    }

    @Override
    public Model findConcrete(Model model) {
        Element findedElement = findElement(model);
        if (findedElement!=null) {
            return createModel(findedElement);
        }
        else {
            return null;
        }
    }

    /**
     * Find all models which attribute matches to argument 
     * (works with mask: ? - one some symbol, * - any symbols)
     * @param attribute attribute to check
     * @return list with found models (could be empty)
     */
    @Override
    public List<Model> findByAttribute(Attribute attribute) {
        String attrName = attribute.getName();
        String attrValue = attribute.getValue();
        
        List<Model> foundModels = new LinkedList<>();

        ListIterator<Element> iterator = root.getChildren().listIterator();
        boolean someRemoved = false;
        while (iterator.hasNext()) {
            Element next = iterator.next();
            if (isArgumentEqual(next, attrName, attrValue)) {
                foundModels.add(createModel(next));
            }
        }
        return foundModels;
    }

    /**
     * Saves document into XML file
     * @throws CanNotWriteException 
     */
    private void saveDocument() throws CanNotWriteException {
        try ( Writer writer = new FileWriter(filename) ) {
            outputter.output(document, writer);
        }
        catch (IOException ex) {
            throw new CanNotWriteException();
        }
    }
    
    /**
     * Find an element in the document with
     * arguments which are equal the model arguments
     * @param model model to find
     * @return element if found, else null
     */
    private Element findElement(Model model) {
        ListIterator<Element> iterator = root.getChildren().listIterator();
        while (iterator.hasNext()) {
            Element next = iterator.next();
            if (isEqual(model, next)) {
                return next;
            }
        }
        return null;
    }
    
    /**
     * Checks if model's end element's arguments are equal
     * @param model model
     * @param modelInBase element
     * @return true if equal, else false
     */
    private boolean isEqual(Model model, Element modelInBase) {
        List<Attribute> modelAttrs = model.getAttributeList();
        /*List<org.jdom2.Attribute> elementAttrs = modelInBase.getAttributes();
        if ( modelAttrs.size() != elementAttrs.size() ) {   // На всякий случай
            return false;
        }*/
        ListIterator<Attribute> iterator = modelAttrs.listIterator();
        while (iterator.hasNext()) {
            Attribute next = iterator.next();
            if (!next.getValue().equals(modelInBase.getAttributeValue(next.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if argument value is equal to same element's argument in document
     * (works with mask: ? - one some symbol, * - any symbols)
     * @param element element to check
     * @param argName argument name
     * @param value argument value
     * @return true if equal, else false
     */
    private boolean isArgumentEqual(Element element, String argName, String value) {
        return matches(element.getAttributeValue(argName), createPattern(value));
    }
    
    /**
     * Creates Element based on model
     * @param model model
     * @return created element
     */
    private Element createModelElement(Model model) {
        Element newModel = new Element(model.getName());
        
        setElementAttrs(newModel, model);
        
        return newModel;
    }
    
    /**
     * Copy model's attributes to the element
     * @param element Element that will take attributes
     * @param model Model that will give attributes
     */
    private void setElementAttrs(Element element, Model model) {
        ListIterator<Attribute> iterator = model.getAttributeList().listIterator();
        
        while (iterator.hasNext()) {
            Attribute next = iterator.next();
            element.setAttribute(next.getName(), next.getValue());
        }
    }

    /**
     * removes element from document
     * @param element element to remove
     */
    private void removeElement(Element element) {
        root.removeContent(element);
    }

    /**
     * Creates suitable Pattern for matches() method based on string
     * @param patternBase base to pattern
     * @return 
     */
    private Pattern createPattern(String patternBase) {
        return Pattern.compile(patternBase.replaceAll("\\*", ".*").replaceAll("\\?", "."));
    }

    /**
     * Checks if string matches to pattern
     * (works with mask: ? - one some symbol, * - any symbols)
     * @param str string to check
     * @param pattern pattern to check
     * @return true if matches, else false
     */
    private boolean matches(String str, Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * Creates model based on Element
     * @param sourceElement element
     * @return created model
     */
    private Model createModel(Element sourceElement) {
        //System.out.println("sourceElement != null: " + (sourceElement!=null));
        List<Attribute> modelAttrList = new LinkedList<>();
        ListIterator<org.jdom2.Attribute> iterator = sourceElement.getAttributes().listIterator();

        while (iterator.hasNext()) {
            org.jdom2.Attribute next = iterator.next();
            modelAttrList.add(new Attribute(next.getName(),next.getValue()));
        }

        return new Model(sourceElement.getName(), modelAttrList);
    }
}
