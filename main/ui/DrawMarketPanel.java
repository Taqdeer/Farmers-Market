package ui;

import model.Market;
import persistence.JsonReader;
import persistence.JsonWriter;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

//Represents a graphical user interface of the market


public class DrawMarketPanel extends JPanel implements ActionListener {
    private static final Color FARMER_COLOR = new Color(217, 250, 224, 112);
    private static final Color MNC_COLOR = new Color(250, 236, 236);
    private static final Color MANAGER_COLOR = new Color(252, 246, 215, 203);

    private static final int INVALID = -1;
    private static final String JSON_STORE = "./data/market.json";

    private Market market;
    //private int interfaceSelector;
    JButton goBack;
    private JsonWriter jsonWriter;                                     //writes and saves to a file
    private JsonReader jsonReader;


    /*
     * MODIFIES: this
     * EFFECTS:  initializes a new market, Json Reader and writes to save data, sets up a goBack/return button
     *           which takes the user to default Interface, finally displays the default interface.
     */
    public DrawMarketPanel() {
        //interfaceSelector = 0;
        market = new Market();
        setUpGoBack();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        defaultInterface();
    }

    /*
     * MODIFIES: this
     * EFFECTS:  creates an interface with three buttons - Farmer, MNC, Manager,
     *           connects them to default action listener
     *           everytime a button is clicked, respective interface gets displayed.
     *           all elements are directly added to the default panel (this)
     */
    private void defaultInterface() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 225, 40));
        add(new JLabel("Farmers' Market")).setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        JButton farmerButton = new JButton("Farmer");
        JButton mncButton = new JButton("MNC");
        JButton managerButton = new JButton("Manager");

        farmerButton.setPreferredSize(new Dimension(300, 100));
        mncButton.setPreferredSize(new Dimension(300, 100));
        managerButton.setPreferredSize(new Dimension(300, 100));

        add(farmerButton);
        add(mncButton);
        add(managerButton);

        farmerButton.addActionListener(this);
        mncButton.addActionListener(this);
        managerButton.addActionListener(this);
    }

    /*
    ------------------------------------Manager------------------------------------
     */

    /*
     * MODIFIES: this
     * EFFECTS:  creates three respective buttons to Save, Load, Print the current market.
     *           each button is connected to a newly created action listener,
     *           which calls the method to perform the specified function
     */
    private void managerInterface() {

        setLayout(new FlowLayout(FlowLayout.CENTER, 225, 40));
        JButton saveMarket = new JButton("Save Market");
        JButton loadMarket = new JButton("Load Market");
        JButton printMarket = new JButton("Print Market");

        saveMarket.setPreferredSize(new Dimension(300, 100));
        loadMarket.setPreferredSize(new Dimension(300, 100));
        printMarket.setPreferredSize(new Dimension(300, 100));

        add(new JLabel("Manager")).setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        add(saveMarket);
        add(loadMarket);
        add(printMarket);

        saveMarket.addActionListener(e -> saveMarket());
        loadMarket.addActionListener(e -> loadMarket());
        printMarket.addActionListener(e -> printMarket());

        add(goBack);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  prints the current market in a text area
     *           goBack button takes user to default interface
     */
    private void printMarket() {
        clearPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JTextArea printMarket = new JTextArea(30, 40);
        printMarket.setBackground(MANAGER_COLOR);
        printMarket.setText(market.printMarket());
        JScrollPane printMarketPane = new JScrollPane(printMarket);

        add(new JLabel("Whole Market"));
        add(printMarketPane);
        add(goBack);
    }

    /*
     * MODIFIES: this
     * EFFECTS: loads market from file
     *          Please note that for implementation of this method,
     *          amount of help in terms of code has been taken from the JsonSerialization Demo
     *          Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
     *          goBack button takes user to default interface
     */
    private void loadMarket() {
        clearPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        try {
            market = jsonReader.read();
            add(new JLabel("Loaded  market from " + JSON_STORE));
        } catch (IOException e) {
            add(new JLabel("Unable to read from file: " + JSON_STORE));
        }
        add(goBack);
    }

    /*
     * MODIFIES: this
     * EFFECTS: saves market to a file
     *          Please note that for implementation of this method,
     *          amount of help in terms of code has been taken from the JsonSerialization Demo
     *          Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
     */
    private void saveMarket() {
        clearPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        try {
            jsonWriter.open();
            jsonWriter.write(market);
            jsonWriter.close();
            System.out.println("Saved market to " + JSON_STORE);

            add(new JLabel("Market has been saved"));
        } catch (FileNotFoundException e) {
            add(new JLabel("Unable to write to file: " + JSON_STORE));
        }
        add(goBack);
    }

    /*
   ------------------------------------MNC------------------------------------
    */

    /*
     * MODIFIES: this
     * EFFECTS:  takes input of crop type, upon button click of "Search Farmers", "Get Contact",
     *           performs the action methods attached to them
     */
    private void mncInterface() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        //buttons
        JButton searchFarmers = new JButton("Search Farmers");
        JButton getContact = new JButton("Get Contact");
        //textFields : input
        JTextField searchCrop = new JTextField(20);
        JTextField selectedFarmer = new JTextField(20);

        //Text fields : output
        JTextArea output = new JTextArea(20, 40);
        output.setBackground(MNC_COLOR);
        JScrollPane outputScrollPane = new JScrollPane(output);
        JTextField outputContact = new JTextField(20);
        outputContact.setBackground(MNC_COLOR);

        add(new JLabel("Crop Type:"));
        add(searchCrop);
        add(searchFarmers);
        add(outputScrollPane);
        add(searchFarmers);
        add(new JLabel("-----------------------------------------"));
        add(getContact);
        add(selectedFarmer);
        add(outputContact);
        add(new JLabel("-----------------------------------------"));

        add(goBack);

        searchFarmers.addActionListener(e -> displayFarmersSpecificCrop(output, searchCrop.getText()));
        getContact.addActionListener(e -> getContactSelectedFarmer(selectedFarmer.getText(), outputContact));
    }

    /*
     * MODIFIES: this
     * EFFECTS:  prints the selected farmer's contact in output area
     */

    private void getContactSelectedFarmer(String selectedFarmer, JTextField output) {
        int selectedFarmerToInteger = toInteger(selectedFarmer);
        if (selectedFarmerToInteger != INVALID) {
            playSound();
            String contactInfo = market.makePurchase(selectedFarmerToInteger) + "";
            output.setText(contactInfo);
        } else {
            clearPanel();
            mncInterface();
            add(new JLabel("      Invalid input. Please try  again       "));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS:  prints all the farmers in a text area which have the specified crop in their list
     */
    private void displayFarmersSpecificCrop(JTextArea outputArea, String cropType) {
        playSound();
        outputArea.setText(market.searchFarmers(cropType));
    }

    /*
   ------------------------------------Farmer------------------------------------
    */

    /*
     * MODIFIES: this
     * EFFECTS:  creates interfaces to sign in, remove or sign up a farmer.
     */
    private void farmerInterface() {
        clearPanel();
        add(new JLabel("Farmer")).setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        farmerSignInRemoveInterface();
        farmerSignUpInterface();
        add(goBack);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  creates interfaces to sign in, remove a farmer.
     *           To Sign in or remove, interface asks for input for Private Id
     *           Connects the method action listener to the sign in and remove button
     */
    private void farmerSignInRemoveInterface() {
        JPanel signInPanel = new JPanel();
        signInPanel.setBackground(FARMER_COLOR);
        signInPanel.setLayout(new GridLayout(5, 0));
        JButton signInCurrentFarmer = new JButton("Sign In");
        JTextField privateIdField = new JTextField(20);
        JButton removeCurrentFarmer = new JButton("Remove Farmer");

        signInPanel.add(new JLabel("Private Id: ", SwingConstants.CENTER));
        signInPanel.add(privateIdField);
        signInPanel.add(signInCurrentFarmer);
        signInPanel.add(new JLabel("      Remove Farmer to remove yourself from market       "));
        signInPanel.add(removeCurrentFarmer);

        add(signInPanel);

        signInCurrentFarmer.addActionListener(e -> signInFarmer(privateIdField.getText()));
        removeCurrentFarmer.addActionListener(e -> removeFarmer(privateIdField.getText()));
    }

    /*
     * MODIFIES: this
     * EFFECTS:  creates interfaces to sign up a farmer.
     *           To Sign up, interface asks for location index and contact
     *           Connects the method action listener to the sign up button
     */
    private void farmerSignUpInterface() {
        JPanel signUpPanel = new JPanel();
        signUpPanel.setBackground(FARMER_COLOR);
        signUpPanel.setLayout(new GridLayout(7, 0));
        JButton signUpNewFarmer = new JButton("Sign up");
        JTextField locationField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        signUpPanel.add(new JLabel("Sign up yourself to become a member of Online Market"));
        signUpPanel.add(new JLabel("Location Index (1-10)", SwingConstants.CENTER));
        signUpPanel.add(locationField);
        signUpPanel.add(new JLabel("Contact", SwingConstants.CENTER));
        signUpPanel.add(contactField);
        signUpPanel.add(signUpNewFarmer);
        add(signUpPanel);
        signUpNewFarmer.addActionListener(e -> signUpFarmer(locationField.getText(), contactField.getText()));
    }

    /*
     * MODIFIES: this
     * EFFECTS:  if the private id provided is valid,
     *           displays the crop interface to further add/remove/modify crops for the farmer
     */
    public void signInFarmer(String privateIdStr) {
        int privateId = toInteger(privateIdStr);
        if (privateId != INVALID && market.getFarmer(privateId) != null) {
            cropInterface(privateId);
        } else {
            clearPanel();
            farmerInterface();
            add(new JLabel("Invalid input. Please try again"));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects input location index and contact otherwise displays error.
     *           if the location index is between [1,10],
     *           add the farmer to the market and gives back his newly generated private id, otherwise displays error.
     *
     */
    private void signUpFarmer(String locationIndex, String contact) {
        clearPanel();
        int locationInfo = toInteger(locationIndex);
        int contactInfo = toInteger(contact);
        if (locationInfo != INVALID && contactInfo != INVALID && validateInput(locationInfo, 1, 10)) {
            int privateId = market.addFarmer(Integer.parseInt(locationIndex), Integer.parseInt(contact));
            add(new JLabel("Your Private ID is: " + privateId));
            add(new JLabel("You will require this private ID to add/modify/remove your crops. "));
            add(new JLabel("You will also use this private ID to sign in or remove yourself from the market."));
            add(goBack);
        } else {
            farmerInterface();
            add(new JLabel("Invalid input. Please try again"));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects input valid private id which already exists in market otherwise displays error.
     *           if above condition is met, removes farmer from market, otherwise displays error.
     *
     */
    private void removeFarmer(String privateIdStr) {
        clearPanel();
        int privateId = toInteger(privateIdStr);
        if (privateId != INVALID) {
            if (market.removeFarmer(privateId)) {
                add(new JLabel("You have been successfully removed!"));
            } else {
                add(new JLabel("We are sorry, an error occurred!"));
                add(goBack);
            }
        } else {
            farmerInterface();
            add(goBack);
            add(new JLabel("Invalid Input. Please try again"));
        }
    }

    /*
   ------------------------------------Crop------------------------------------
    */

    /*
     * MODIFIES: this
     * EFFECTS:  creates interfaces to add, remove or modify a crop for the given farmer.
     */
    private void cropInterface(int privateId) {
        clearPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        add(new JLabel("Add Crop:      ", SwingConstants.CENTER)).setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        addCropInterface(privateId);
        add(new JLabel("Modify Crop: ", SwingConstants.CENTER)).setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        modifyCropInterface(privateId);
        add(new JLabel("Remove Crop:", SwingConstants.CENTER)).setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        removeCropInterface(privateId);
        add(goBack);
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects valid input of crop type to be removed otherwise displays error
     *           if above condition is met, removes the crop from the given farmer's crop list, otherwise displays error
     */
    private void removeCropInterface(int privateId) {
        JPanel removeCropPanel = new JPanel();
        removeCropPanel.setLayout(new GridLayout(3, 1));
        removeCropPanel.setBackground(FARMER_COLOR);
        JButton removeCrop = new JButton("Remove Crop");
        JTextField cropToBeRemoved = new JTextField(20);

        removeCropPanel.add(new JLabel("Crop to Remove: ", SwingConstants.CENTER));
        removeCropPanel.add(cropToBeRemoved);
        removeCropPanel.add(removeCrop);

        add(removeCropPanel);

        removeCrop.addActionListener(e -> removeCrop(privateId, cropToBeRemoved.getText()));
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects valid input of crop type, quality index in range [1,10] to be added otherwise displays error
     *           if above condition is met, adds the crop to the given farmer's crop list, otherwise displays error
     */
    private void addCropInterface(int id) {
        JPanel addCropPanel = new JPanel();
        addCropPanel.setLayout(new GridLayout(9, 0));
        addCropPanel.setBackground(FARMER_COLOR);
        JButton addCrop = new JButton("Add Crop");

        JTextField name = new JTextField(20);
        JTextField quality = new JTextField(20);
        JTextField quantity = new JTextField(20);
        JTextField price = new JTextField(20);


        addCropPanel.add(new JLabel("Crop Name: ", SwingConstants.CENTER));
        addCropPanel.add(name);
        addCropPanel.add(new JLabel("Crop Quality (1-10): ", SwingConstants.CENTER));
        addCropPanel.add(quality);
        addCropPanel.add(new JLabel("Crop Quantity: ", SwingConstants.CENTER));
        addCropPanel.add(quantity);
        addCropPanel.add(new JLabel("Crop Price: ", SwingConstants.CENTER));
        addCropPanel.add(price);
        addCropPanel.add(addCrop);

        add(addCropPanel);

        addCrop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCrop(id, name.getText(), quality.getText(), quantity.getText(), price.getText());
            }
        });
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects valid input of crop type, quality index in range [1,10] to be modifies otherwise displays error
     *           if above condition is met, modifies the crop to the given farmer's crop list,
     *           otherwise displays error
     */
    private void modifyCropInterface(int id) {
        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new GridLayout(6, 2));
        modifyPanel.setBackground(FARMER_COLOR);

        JTextField traitToChange = new JTextField(20);
        JTextField newValue = new JTextField(20);

        JButton modifyCrop = new JButton("Modify Crop");
        ButtonGroup buttonGroup = makeRadioButtons(modifyPanel);

        modifyPanel.add(new JLabel("Crop to Modify: ", SwingConstants.CENTER));
        modifyPanel.add(traitToChange);
        modifyPanel.add(new JLabel("New Value: ", SwingConstants.CENTER));
        modifyPanel.add(newValue);
        modifyPanel.add(modifyCrop);

        add(modifyPanel);

        modifyCrop.addActionListener(e -> modifyCrop(buttonGroup, id, traitToChange.getText(), newValue.getText()));
    }

    /*
     * MODIFIES: this
     * EFFECTS:  makes a group of radio buttons, adds them to given panel and returns it back
     *
     */
    private ButtonGroup makeRadioButtons(JPanel modifyCropPanel) {
        JRadioButton price = new JRadioButton("Price");
        price.setActionCommand("price");
        JRadioButton quality = new JRadioButton("Quality");
        quality.setActionCommand("quality");
        JRadioButton quantity = new JRadioButton("Quantity");
        quantity.setActionCommand("quantity");
        ButtonGroup buttonGroup = new ButtonGroup();

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 1));
        radioButtonPanel.setBackground(FARMER_COLOR);
        buttonGroup.add(price);
        buttonGroup.add(quality);
        buttonGroup.add(quantity);

        radioButtonPanel.add(price);
        radioButtonPanel.add(quality);
        radioButtonPanel.add(quantity);
        modifyCropPanel.add(radioButtonPanel);

        return buttonGroup;
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects input valid private id, crop type which already exists in market otherwise displays error.
     *           if above condition is met, removes crop from farmer's crop list, otherwise displays error.
     *
     */
    private void removeCrop(int privateId, String cropType) {
        clearPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        if (market.removeCropExistingFarmer(privateId, cropType)) {
            add(new JLabel("Your crop has been successfully removed"));
            add(goBack);
        } else {
            cropInterface(privateId);
            add(new JLabel("Error occurred - Please enter a valid cropType"));
        }

    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects input valid private id, crop type, crop quality, crop price
     *           which already exists in market otherwise displays error.
     *           if above condition is met, adds crop to farmer's crop list, otherwise displays error.
     *
     */
    private void addCrop(int privateId, String cropName, String quality, String quantity, String price) {
        clearPanel();
        int cropQuality = toInteger(quality);
        int cropQuantity = toInteger(quantity);
        int cropPrice = toInteger(price);
        if (cropQuality != INVALID && cropQuantity != INVALID && cropPrice != INVALID) {
            if (validateInput(cropQuality, 1, 10)) {
                if (market.addCropExistingFarmer(privateId, cropName, cropPrice, cropQuality, cropQuantity)) {
                    add(new JLabel(cropName + " crop has been added to your list"));
                } else {
                    add(new JLabel("This crop is already in the list. Go Back and try again"));
                }
                add(goBack);
            } else {
                cropInterface(privateId);
                add(new JLabel("Please enter a valid crop quality index"));
            }
        } else {
            cropInterface(privateId);
            add(new JLabel("Error occurred - Please enter valid inputs"));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS:  expects input valid private id, crop type which already exists in market, newValue of trait,
     *           otherwise displays error. Also, expects radio button group - if none selected, displays error
     *           if above condition is met, modifies crop in farmer's crop list, otherwise displays error.
     *
     */
    private void modifyCrop(ButtonGroup btnGroup, int privateId, String cropName, String newValueOfTrait) {
        clearPanel();
        int traitValue = toInteger(newValueOfTrait);
        ButtonModel traitToChange = btnGroup.getSelection();
        if (traitValue != INVALID && traitToChange != null) {
            if (traitToChange.getActionCommand().equals("quality")  && !validateInput(traitValue, 1, 10)) {
                cropInterface(privateId);
                add(new JLabel("Error occurred - Quality Index should be between 1 and 10"));
                return;
            }
            if (market.modifyCropExistingFarmer(privateId, cropName, traitToChange.getActionCommand(), traitValue)) {
                add(new JLabel("Your crop has been successfully modified"));
            } else {
                add(new JLabel("We are sorry but an error occurred."));
                add(new JLabel("You must specify the crop which is already in your list"));
            }
            add(goBack);
        } else {
            cropInterface(privateId);
            add(new JLabel("Error occurred - Please select all valid inputs"));
        }

    }


    /*
   ------------------------------------Helper Methods------------------------------------
    */

    /*
     * MODIFIES: this
     * EFFECTS:  clears the whole panel and plays sound
     *
     */
    private void clearPanel() {
        removeAll();
        revalidate();
        repaint();
        playSound();
        // add(new JLabel(market.printMarket()));
    }

    /*
     * EFFECTS: returns the integer equivalence of provided string otherwise catches NumberFormatException
     *          and returns INVALID constant
     */
    private int toInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return INVALID;
        }
    }

    /*
     * EFFECTS: return true if the inputValue is >= min and inputValue <= max, otherwise returns false
     */
    private boolean validateInput(int inputValue, int min, int max) {
        return inputValue <= max && inputValue >= min;
    }

    /*
     * EFFECTS:  initializes goBack button, connects it to default action listener,
     *           sets its size, foreground color, font.
     */
    private void setUpGoBack() {
        goBack = new JButton("Go Back");
        goBack.addActionListener(this);
        goBack.setPreferredSize(new Dimension(200, 50));
        goBack.setForeground(new Color(245, 115, 110));
        goBack.setFont(new Font("Arial", Font.PLAIN, 20));
    }


    // The following code has been taken from :
    // https://alvinalexander.com/java/java-audio-example-java-au-play-sound/
    /*
     * EFFECTS: initializes audio stream and plays the given sound file,
     *          catches Exception if fails
     */
    private void playSound() {
        String soundName = "sounds/sound.wav";
        try {
            InputStream inputStream = getClass().getResourceAsStream(soundName);
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
            System.out.println("sound error");
        }

    }

    /*
   ------------------------------------Overwritten Methods------------------------------------
    */
    /*
     * MODIFIES: this
     * EFFECTS: clears the panel, and displays the respective interface of farmer, MNC or Manager
     *          based on the detected button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        clearPanel();
        switch (e.getActionCommand()) {
            case "Farmer":
                farmerInterface();
                break;
            case "MNC":
                mncInterface();
                break;
            case "Manager":
                managerInterface();
                break;
            default:
                defaultInterface();
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: draws the farmer, MNC, Manager on g (Graphics component) of default panel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, 500, 700);
        drawPersonIcon("Farmer", g, 115, 550);
        drawPersonIcon("MNC", g, 220, 550);
        drawPersonIcon("Manager", g, 330, 550);

    }

    /*
     * REQUIRES: type, graphics, horizontal and vertical positions must be valid
     * MODIFIES: this
     * EFFECTS:  draws the person icon thrice - for farmer, MNC, Manager - in a specific color on given g (graphics)
     *
     */
    private void drawPersonIcon(String type, Graphics g, int horizontalPos, int verticalPos) {
        Color color;
        if (type.equals("Farmer")) {
            color = FARMER_COLOR;
        } else if (type.equals("MNC")) {
            color = MNC_COLOR;
        } else {
            color = MANAGER_COLOR;
        }
        g.setColor(color);
        drawFace(horizontalPos, verticalPos, g);
        drawBody(horizontalPos, verticalPos + 50, g);
    }

    /*
     * REQUIRES:  graphics, horizontal and vertical positions must be valid
     * MODIFIES: this
     * EFFECTS:  draws the face of the person on g (graphics)
     *
     */
    private void drawFace(int horizontalPos, int verticalPos, Graphics g) {
        g.fillOval(horizontalPos, verticalPos, 50, 50);
    }

    /*
     * REQUIRES:  graphics, horizontal and vertical positions must be valid
     * MODIFIES: this
     * EFFECTS:  draws the body of the person on g (graphics)
     *
     */
    private void drawBody(int horizontalPos, int verticalPos, Graphics g) {
        g.fillRect(horizontalPos, verticalPos, 50, 50);
    }

}
