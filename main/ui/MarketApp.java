package ui;


import model.Market;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;

/*
 * Represents a market application
 */
public class MarketApp {

    private static final String JSON_STORE = "./data/market.json";     //file where data is saved

    private Market market;
    private Scanner input;
    private final int quit;                                                  //used to quit/return from a menu

    private JsonWriter jsonWriter;                                     //writes and saves to a file
    private JsonReader jsonReader;                                     //reads from the file

    /*
     * EFFECTS: constructs a market object and runs application
     */
    public MarketApp() {
        market = new Market();
        input = new Scanner(System.in);
        quit = 0;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runMarket();
    }

    /*
     * MODIFIES: this
     * EFFECTS: processes user input and execute the corresponding function requested by the user
     */
    public void runMarket() {
        System.out.println("Welcome to Online Mandi (Farmers' Market)");
        int incomingBody;
        do {
            System.out.println("Please select \n 1: If you are farmer \n 2: If you are a MNC");
            System.out.print(" 3: If you are a manager \n 0: To quit\n");
            incomingBody = validIntegerInput();
            if (incomingBody == 1) {
                serveFarmer();
            } else if (incomingBody == 2) {
                serveMNC();
            } else if (incomingBody == 3) {
                serveManager();
            } else if (incomingBody == quit) {
                System.out.println("Have a good day");
            } else {
                System.out.println("Please enter a valid number");
            }
        } while (incomingBody != quit);
    }

    /*
     * EFFECTS: processes manager's input and execute the corresponding function requested by the manager
     */
    private void serveManager() {
        int managerInput;
        do {
            System.out.println("\nPlease select \n 1: Save Market \n 2: Load Market");
            System.out.print(" 3: Print Market \n 0: To quit\n");
            managerInput = validIntegerInput();
            if (managerInput == 1) {
                saveMarket();
            } else if (managerInput == 2) {
                loadMarket();
            } else if (managerInput == 3) {
                System.out.println(market.printMarket());
            } else if (managerInput == quit) {
                System.out.println("Returning back to main menu");
            } else {
                System.out.println("Please enter a valid number");
            }
        } while (managerInput != quit);

    }

    /*
     * EFFECTS: keeps asking user for input until it is an integer
     *          and returns back its value
     */
    public int validIntegerInput() {
        int userInput;
        input = new Scanner(System.in);
        if (input.hasNextInt()) {
            userInput = input.nextInt();
        } else {
            userInput = quit;
            System.out.println("\nYou should have entered a valid input");
        }
        return userInput;
    }

    /*
     * MODIFIES: this
     * EFFECTS: loads market from file
     * Please note that for implementation of this method,
     * amount of help in terms of code has been taken from the JsonSerialization Demo
     * Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
     */
    private void loadMarket() {
        try {
            market = jsonReader.read();
            System.out.println("Loaded  market from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    /*
     * EFFECTS: saves market to a file
     * Please note that for implementation of this method,
     * amount of help in terms of code has been taken from the JsonSerialization Demo
     * Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
     */
    private void saveMarket() {
        try {
            jsonWriter.open();
            jsonWriter.write(market);
            jsonWriter.close();
            System.out.println("Saved market to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    /*
     * EFFECTS: processes a farmer's command and execute the corresponding function given valid inputs
     */
    private void serveFarmer() {
        int option;
        do {
            displayFarmerOptions();
            option = validIntegerInput();
            switch (option) {
                case 0:
                    System.out.println("Returning back to main menu");
                    break;
                case 1:
                    signUpNewFarmer();
                    break;
                case 2:
                    removeExistingFarmer();
                    break;
                case 3:
                    modifyCropExistingFarmer();
                    break;
                default:
                    System.out.println("Please enter a valid option");
            }
        } while (option != quit);
    }

    /*
     * MODIFIES: this
     * EFFECTS: asks the farmer's id who wants to get removed,
     *          if present, removes the farmer from market, for given valid inputs
     */
    private void removeExistingFarmer() {
        int privateIdToBeRemoved;
        System.out.println("Please enter your private ID to get removed from the market");
        privateIdToBeRemoved = validIntegerInput();
        if (market.removeFarmer(privateIdToBeRemoved)) {
            System.out.println("You have been successfully removed");
        } else {
            System.out.println("We are sorry but a problem occurred.");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: ask farmer's private id, processes command of addition/removal/modification
     *          of crops and then executes the corresponding function for given valid inputs
     */
    private void modifyCropExistingFarmer() {
        System.out.println("Your Private ID");
        int privateId = validIntegerInput();
        System.out.println(" 1: Add a new crop \n 2: Remove a crop \n 3: Modify a crop");
        int taskSelectionCrop = validIntegerInput();
        if (privateId == 0 || taskSelectionCrop == 0) {
            return;
        }
        switch (taskSelectionCrop) {
            case 1:
                addCrop(privateId);
                break;
            case 2:
                removeCrop(privateId);
                break;
            case 3:
                modifyCrop(privateId);
                break;
            default:
                System.out.println("You did not select a valid option. Returning back to main menu.");
        }

    }

    /*
     * REQUIRES: private id must be a valid id
     * MODIFIES: this
     * EFFECTS: modifies quality/quantity/price of specified crop of the farmer for given valid inputs
     */
    private void modifyCrop(int privateId) {
        System.out.println("Please enter that which crop you want to modify and it must exist in your list");
        String cropType = input.next();
        System.out.println("Please enter that which trait you want to change 'quality', 'quantity' or 'price'");
        String traitToChange = input.next();
        System.out.println("Please enter new value of trait (should be a number only)");
        System.out.println("-Quality Index depends on the seed used to grow crop. Find it at this.com");
        System.out.println("-Quantity should be entered in Kilograms");
        System.out.println("-Price should be entered in USD per KG");
        int newValueOfTrait = validIntegerInput();
        if (newValueOfTrait == 0) {
            return;
        }
        if (market.modifyCropExistingFarmer(privateId, cropType, traitToChange, newValueOfTrait)) {
            System.out.println("If crop existed in your list, it has been modified");
        } else {
            System.out.println("An error occurred : couldn't find your private id");
        }

    }

    /*
     * REQUIRES: private id must be a valid id
     * MODIFIES: this
     * EFFECTS: removes the specified crop of the farmer for given valid inputs
     */
    private void removeCrop(int privateId) {
        System.out.println("Please enter that which crop you want to remove");
        String cropType = input.next();
        if (market.removeCropExistingFarmer(privateId, cropType)) {
            System.out.println("If Crop existed in your list, then it has been removed now");
        } else {
            System.out.println("An error occurred - couldn't find your privateId");
        }
    }

    /*
     * REQUIRES: private id must be a valid id
     * MODIFIES: this
     * EFFECTS: adds a crop to the farmer's list for given valid inputs
     */
    private void addCrop(int privateId) {
        System.out.println("Please enter the following");
        System.out.println("Crop Type (should be a name of the crop): ");
        String cropType = input.next();
        System.out.println("Crop Quality Index, Quantity, Price");
        System.out.println("-Quality Index depends on the seed used to grow crop. Find it at this.com");
        System.out.println("-Quantity should be entered in Kilograms");
        System.out.println("-Price should be entered in USD per KG");
        int cropQuality = validIntegerInput();
        int cropQuantity = validIntegerInput();
        int cropPrice = validIntegerInput();
        if (cropPrice == 0 || cropQuality == 0 || cropQuantity == 0) {
            return;
        }
        if (market.addCropExistingFarmer(privateId, cropType, cropPrice, cropQuality, cropQuantity)) {
            System.out.println("Crop has been added");
        } else {
            System.out.println("An error occurred");
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: signs up a new farmer with location index and contact number and
     *          generates a private Id to be given back to the farmer for given valid inputs
     */
    private void signUpNewFarmer() {
        System.out.println("We will require two things to sign you up in our market");
        System.out.println("Location Index and Contact Number");
        System.out.println("-Location Index is your location number with respect to the capital of your state");
        System.out.println("-It can be found at this.com");
        System.out.println("-Contact number should be your mobile number only");
        int locationOfFarmer = validIntegerInput();
        int contactFarmer = validIntegerInput();
        if (locationOfFarmer == 0 || contactFarmer == 0) {
            return;
        }
        int privateId = market.addFarmer(locationOfFarmer, contactFarmer);
        System.out.println("You have been successfully added to the market");
        System.out.println("Your Private ID is: " + privateId);
        System.out.println("You will always require this id to make changes in your profile");
    }

    /*
     * EFFECTS: displays a list of options which a farmer can execute
     */
    private void displayFarmerOptions() {
        System.out.println("We are very pleased to serve you");
        System.out.println("What would you like to do? Please select from the following");
        System.out.println("Enter \n 1: Sign up as a new farmer \n 2: Remove yourself from the market");
        System.out.println(" 3: Modify/Add/Remove crop in your profile \n 0: To quit");
    }

    /*
     * EFFECTS: displays a list of options which a MNC can execute
     *          and then execute the corresponding function for given valid inputs
     */
    private void serveMNC() {
        System.out.println("Welcome.\n To use this app, you will have to first search market for a specific crop.");
        System.out.println("Then you can go ahead and make a purchase");
        System.out.println("Please enter for what crop are you looking for?");
        System.out.println("'Wheat', 'Rice', 'Barley'");
        String lookForCrop = input.next();
        String selectedFarmers = market.searchFarmers(lookForCrop);
        if (selectedFarmers.length() > 0) {
            System.out.println(selectedFarmers);
            System.out.println("Now please enter the index number with whom you want to do business: ");
            int farmerSelected = validIntegerInput();
            if (farmerSelected == 0) {
                return;
            }
            int contactOfFarmerSelected = market.makePurchase(farmerSelected);
            System.out.println("Go ahead and make a purchase with your selected farmer: " + contactOfFarmerSelected);
        } else {
            System.out.println("Sorry we don't have " + lookForCrop + " at the moment");
        }
    }

}
