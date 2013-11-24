package helpSystem.tests;

import helpSystem.restaurant.RestaurantHelpSystemController;
import helpSystem.controllers.HelpSystemController;
import helpSystem.controllers.ConsoleControllerFactory;

/**
 * Class for testing DishHelpSystemController
 */
public class RestaurantHelpSystemTest {
    /**
     * The entry point of the program
     * @param args The command line arguments, are not used in program
     * @throws Exception
     */
    
    // One more test
    public static void main(String[] args) throws Exception {
        HelpSystemController pc = new RestaurantHelpSystemController(ConsoleControllerFactory.getInstance());
        pc.start();
    }
}