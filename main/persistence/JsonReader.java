package persistence;

import model.Market;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// Please note that for implementation of this class,
// major amount of help in terms of code has been taken from the JsonSerialization Demo
// Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;      //file from which the contents are read

    /*
     * EFFECTS: constructs reader to read from source file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /*
     * EFFECTS: reads workroom from file and returns it;
     * throws IOException if an error occurs reading data from file
     */
    public Market read() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseMarket(jsonArray);
    }

    /*
     * EFFECTS: reads source file as string and returns it
     */
    private String readFile(String source) throws IOException {
        StringBuilder content = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> content.append(s));
        }
        return content.toString();
    }

    /*
     * EFFECTS: parses market from JSON object and returns it
     */
    private Market parseMarket(JSONArray jsonArray) {
        Market market = new Market();
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            int privateId = jsonObject.getInt("privateId");
            market.addFarmer(jsonObject.getInt("locationIndex"), jsonObject.getInt("contact"));
            JSONArray cropArray = jsonObject.getJSONArray("crops");
            addCropsToFarmers(market, cropArray, privateId);
        }
        return market;
    }

    /*
     * MODIFIES: market
     * EFFECTS: parses crops from JSON object and adds them to each farmer object of the market
     */
    private Market addCropsToFarmers(Market market, JSONArray cropArray, int privateId) {
        for (Object crop : cropArray) {
            JSONObject cropJsonObj = (JSONObject) crop;
            String type = cropJsonObj.getString("cropType");
            int price = cropJsonObj.getInt("cropPrice");
            int quality = cropJsonObj.getInt("cropQuality");
            int quantity = cropJsonObj.getInt("cropQuantity");
            market.addCropExistingFarmer(privateId, type, price, quality, quantity);
        }
        return market;
    }

}
