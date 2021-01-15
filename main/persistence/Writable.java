package persistence;

import org.json.JSONArray;

// Please note that for implementation of this class,
// major amount of help in terms of code has been taken from the JsonSerialization Demo
// Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public interface Writable {
    /*
     * EFFECTS: returns this as JSON Array
     */
    JSONArray toJson();
}