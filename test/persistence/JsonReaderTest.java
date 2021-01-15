package persistence;

import model.Market;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Please note that for implementation of this class,
// major amount of help in terms of code has been taken from the JsonSerialization Demo
// Link : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonReaderTest {
    @Test
    public void testReaderNonExistentFile(){
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Market market = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyMarket() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMarket.json");
        try {
            Market market = reader.read();
            assertEquals(0,market.getFarmersList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralMarket() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralMarket.json");
        try {
            Market market = reader.read();
            assertEquals(3, market.getFarmersList().size());

            //farmer1
            assertEquals(1, market.getFarmersList().get(0).getPrivateId());
            assertEquals(123, market.getFarmersList().get(0).getContact());
            assertEquals(1, market.getFarmersList().get(0).getLocation());
            assertEquals(2, market.getFarmersList().get(0).getCrops().size());

            //farmer2
            assertEquals(2, market.getFarmersList().get(1).getPrivateId());
            assertEquals(345, market.getFarmersList().get(1).getContact());
            assertEquals(2, market.getFarmersList().get(1).getLocation());
            assertEquals(1, market.getFarmersList().get(1).getCrops().size());

            //farmer3
            assertEquals(3, market.getFarmersList().get(2).getPrivateId());
            assertEquals(356, market.getFarmersList().get(2).getContact());
            assertEquals(2, market.getFarmersList().get(2).getLocation());
            assertEquals(2, market.getFarmersList().get(2).getCrops().size());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
