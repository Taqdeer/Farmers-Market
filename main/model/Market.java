package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/* Represents a Market which has
 * a private id generator which starts from zero and increments every time a farmer sign up,
 * farmers list which has details of all the farmers who want sell their crops,
 * and selected farmers list who get selected from farmers list every time a MNC pays a visit to buy a specific crop
 * Note that selected farmers is a new and updated list of farmers every time one searches for a specific crop
 */

public class Market implements Writable {
    private int privateIdGenerator;         //this field is responsible for generating a unique id to every new farmer
    private List<Farmer> farmersList;       //list of all the farmers available to sell their crops
    private List<Farmer> selectedFarmers;   //list of farmers who get selected for a specific crop

    /*
     * MODIFIES: this
     * EFFECTS:  Sets up an empty list of farmers, initializes private id generator to 0 and
     *           makes selected farmers null as the farmers are selected only
     *           when a search is performed on farmers list for a specific crop
     */
    public Market() {
        farmersList = new LinkedList<>();
        privateIdGenerator = 0;
        selectedFarmers = null;
    }

    /*
     * REQUIRES: incoming farmer must not be present in the farmers list already
     * MODIFIES: this
     * EFFECTS:  Makes a new farmer object with the provided location and contact,
     *           assigns a private id to that farmer and adds the newly created farmer
     *           to the farmers list if not present already and
     *           returns back the unique private id generated otherwise returns -1
     */
    public int addFarmer(int location, int contact) {
        Farmer farmer = new Farmer(location, ++privateIdGenerator, contact);
        farmersList.add(farmer);
        return privateIdGenerator;
    }

    /*
     * REQUIRES: private id provided must be a valid id i.e. farmer must exist in the farmers list to be removed
     * MODIFIES: this
     * EFFECTS:  Finds the farmer with the given private id and removes it from the farmers list
     *           if successful in removing farmer, returns true otherwise returns false.
     */
    public boolean removeFarmer(int privateId) {
        Farmer toBeRemoved = getFarmer(privateId);
        if (toBeRemoved != null) {
            farmersList.remove(toBeRemoved);
            return true;
        }
        return false;
    }

    /*
     * REQUIRES: private id provided must be a valid id,
     *           crop quality of crop should be within range of 1-10 and is based on type of seed
     *           crop type must be of non zero length
     * MODIFIES: this
     * EFFECTS:  creates a crop with the provided specifications,
     *           finds the farmer with the given private id to add the newly created crop to his/her crop list
     *           returns true if successful in adding a crop, false otherwise
     */
    public boolean addCropExistingFarmer(int privateId, String cropType, int price, int cropQuality, int cropQuantity) {
        Farmer toAdd = getFarmer(privateId);
        if (toAdd != null && toAdd.addCrop(new Crop(cropQuantity, cropQuality, price, cropType))) {
            return true;
        }
        return false;
    }

    /*
     * REQUIRES: private id provided must be a valid id i.e. farmer must exist in the farmers list
     * EFFECTS:  returns the farmer with the specified id from the farmers list
     */
    public Farmer getFarmer(int privateId) {
        Farmer toReturn = null;
        for (Farmer farmer : farmersList) {
            if (farmer.getPrivateId() == privateId) {
                toReturn = farmer;
            }
        }
        return toReturn;
    }
    /*
     * REQUIRES: private id provided must be a valid id,
     *           crop type must exist in that specific farmer's crop list
     *           crop type must be of non zero length
     * MODIFIES: this
     * EFFECTS:  finds the farmer with the given private id in farmers list and removes the specified type of crop
     *           returns true if successful in finding the farmer, false otherwise
     */

    public boolean removeCropExistingFarmer(int privateId, String cropType) {
        Farmer farmerCropToBeRemoved = getFarmer(privateId);
        if (farmerCropToBeRemoved != null && farmerCropToBeRemoved.removeCrop(cropType)) {
            return true;
        }
        return false;
    }

    /*
     * REQUIRES: private id provided must be a valid id,
     *           crop type must exist in that specific farmer's crop list
     *           trait to change can and must be only - "quality", "quantity" or "price"
     *           if trait to change is quality, then it must be within range of 1-10
     * MODIFIES: this
     * EFFECTS:  finds the farmer with the given private id in farmers list, then finds the specified type of crop,
     *           then updates value of the trait which farmer wants to chang to the new value provided
     *           returns true if successful in finding the farmer, false otherwise
     */
    public boolean modifyCropExistingFarmer(int privateId, String cropType, String traitToChange, int newValueTrait) {
        Farmer toModify = getFarmer(privateId);
        try {
            if (toModify.modifyCrops(cropType, traitToChange, newValueTrait)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /*
     * REQUIRES: crop type must be a valid type - "wheat", "rice" or "barley"
     * MODIFIES: this
     * EFFECTS:  finds all the farmers who have specified crop type in their crop list and
     *           add them to the selected farmers list,
     *           if the selected farmers list has any farmers,
     *           returns the report(string representation of list) of selected farmers
     *           otherwise, returns empty report(empty string)
     */
    public String searchFarmers(String cropType) {
        selectedFarmers = new LinkedList<>();
        for (Farmer farmer : farmersList) {
            for (Crop crop : farmer.getCrops()) {
                if (crop.getType().equals(cropType)) {
                    selectedFarmers.add(farmer);
                }
            }
        }
        // sortOnLocation(selectedFarmers);  //in case if I want to provide the sorted results
        if (selectedFarmers.size() > 0) {
            return displayFarmersSelectedCrop(selectedFarmers, cropType);
        }
        return "";
    }

    /*
     * Helper method
     * REQUIRES: farmers list must be non empty
     *           selected crop type must be a valid type - "wheat", "rice" or "barley"
     * EFFECTS:  returns back the string representation of all farmers who have the specified crop in their crop list
     *           along with the crop details
     */

    private String displayFarmersSelectedCrop(List<Farmer> farmers, String selectedCrop) {
        String farmerInfo = "";
        int index = 1;
        for (Farmer farmer : farmers) {
            farmerInfo += "\n Index: " + index;
            farmerInfo += "\n Location Index: " + farmer.getLocation();
            farmerInfo += "\n Crop specification: \n " + farmer.getSpecificCrop(selectedCrop).displayCrop();
            index++;
        }
        return farmerInfo;
    }

    /*
     * REQUIRES: selected farmer must exist in the list of selected farmers
     * EFFECTS:  returns back the contact information of the selected farmer
     *           if selectedFarmer is invalid, catches exception and returns -1
     */
    public int makePurchase(int selectedFarmer) {
        try {
            Farmer chosenFarmer = selectedFarmers.get(selectedFarmer - 1);
            return chosenFarmer.getContact();
        } catch (Exception e) {
            return -1;
        }

    }

    public List<Farmer> getFarmersList() {
        return this.farmersList;
    }

    public List<Farmer> getSelectedFarmersList() {
        return this.selectedFarmers;
    }

    public int privateIdGenerator() {
        return this.privateIdGenerator;
    }

    /*
     * EFFECTS:  returns back the json array made of json objects where each object represents a farmer
     */
    @Override
    public JSONArray toJson() {
        JSONArray jsonArrayFarmers = new JSONArray();
        JSONObject jsonObjectTemp;
        for (Farmer farmer : farmersList) {
            jsonObjectTemp = new JSONObject();
            jsonObjectTemp.put("privateId", farmer.getPrivateId());
            jsonObjectTemp.put("locationIndex", farmer.getLocation());
            jsonObjectTemp.put("contact", farmer.getContact());
            jsonObjectTemp.put("crops", farmer.getCropsJson());
            jsonArrayFarmers.put(jsonObjectTemp);
        }
        return jsonArrayFarmers;
    }

    /*
     * EFFECTS: returns a string representation of farmer list
     */
    public String printMarket() {
        String farmers = "";
        for (Farmer farmer : getFarmersList()) {
            farmers += "\n Contact: " + farmer.getContact();
            farmers += "\n Location Index: " + farmer.getLocation();
            farmers += "\n Crop specification: " + farmer.displayCropList();
        }
        return farmers;
    }
}