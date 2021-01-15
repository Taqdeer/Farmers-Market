package model;

//Represents a crop with specifications of quantity (kilos), quality(Standard Index by Govt), price(dollars), type.
public class Crop {
    private int quantity;      //quantity of crop in kilograms
    private int qualityIndex;  //quality of crop should be within range of 1-10 and is based on type of seed
    private int price;         //price of crop per kilogram in dollars- determined by farmer
    private String type;       //name of the crop

    /* REQUIRES: type must be of non zero length, quality Index must be in range of 1 to 10.
     * MODIFIES: this
     * EFFECTS:  Sets the name of the crop to given type, quantity in kilos to quantity,
     *           MSP of the crop (in dollars) per kilo to price, quality of the crop to qualityIndex
     */
    protected Crop(int quantity, int qualityIndex, int price, String type) {
        this.quantity = quantity;
        this.qualityIndex = qualityIndex;
        this.price = price;
        this.type = type;
    }

    protected void setPrice(int newPrice) {
        this.price = newPrice;
    }

    protected void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    protected void setQualityIndex(int newQualityIndex) {
        this.qualityIndex = newQualityIndex;
    }

    protected String getType() {
        return this.type;
    }

    protected int getQuality() {
        return this.qualityIndex;
    }

    protected int getQuantity() {
        return this.quantity;
    }

    protected int getPrice() {
        return this.price;
    }

    /*
     * EFFECTS: returns a string representation of crop
     */
    public String displayCrop() {
        String crop = "";
        crop += " Type= " + getType();
        crop += " Price= " + getPrice();
        crop += " Quality Index= " + getQuality();
        crop += " Quantity offered= " + getQuantity() + "\n";
        return crop;
    }
}
