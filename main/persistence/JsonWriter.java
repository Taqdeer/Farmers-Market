package persistence;

import model.Market;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
// Please note that for implementation of this class,
// major amount of help in terms of code has been taken from the JsonSerialization Demo
// Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;         //used to write into the destination file
    private String destination;         //file where the contents are written and saved

    /*
     * EFFECTS: constructs write to write to the destination file
     */
    public JsonWriter(String file) {
        this.destination = file;
    }

    /*
     * MODIFIES: this
     * EFFECTS: opens writer, throws throws FileNotFoundException if destination file cannot
     * be opened for writing
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    /*
     * MODIFIES: this
     * EFFECTS: writes JSON representation of Market to destination file
     */
    public void write(Market market) {
        JSONArray jsonArray = market.toJson();
        writer.print(jsonArray.toString(TAB));
    }

    /*
     * MODIFIES: this
     * EFFECTS: closes writer
     */
    public void close() {
        writer.close();
    }
}

