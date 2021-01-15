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

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Market wr = new Market();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Market market = new Market();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMarket.json");
            writer.open();
            writer.write(market);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMarket.json");
            market = reader.read();
            assertEquals(0,market.getFarmersList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Market market = new Market();
            int farmer1 = market.addFarmer(1, 123);
            int farmer2 = market.addFarmer(2, 345);
            market.addCropExistingFarmer(farmer1, "rice", 1000, 1, 10);
            market.addCropExistingFarmer(farmer1, "barley", 2000, 2, 20);
            market.addCropExistingFarmer(farmer2, "barley", 2000, 2, 20);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMarket.json");
            writer.open();
            writer.write(market);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMarket.json");
            market = reader.read();

            assertEquals(2, market.getFarmersList().size());

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

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
