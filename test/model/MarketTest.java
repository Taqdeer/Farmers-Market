package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    private Market testMarket;
    @BeforeEach
    void runBefore(){
        testMarket = new Market();
    }

    @Test
    void testConstructor(){
         assertEquals(0,testMarket.privateIdGenerator());
         assertEquals(0,testMarket.getFarmersList().size());
         assertNull(testMarket.getSelectedFarmersList());
    }

    @Test
    void testAddFarmer(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertEquals(1,testMarket.getFarmersList().size());
        assertEquals(1,generatedPrivateId);
        assertEquals(1,testMarket.getFarmer(1).getPrivateId());
        assertEquals(1,testMarket.getFarmer(1).getLocation());
        assertEquals(123,testMarket.getFarmer(1).getContact());
    }
    @Test
    void testRemoveFarmerExists(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertEquals(1,testMarket.getFarmersList().size());
        assertTrue(testMarket.removeFarmer(generatedPrivateId));
        assertEquals(0,testMarket.getFarmersList().size());
    }
    @Test
    void testRemoveFarmerNotExists(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertEquals(1,testMarket.getFarmersList().size());
        assertFalse(testMarket.removeFarmer(2));
        assertEquals(1,testMarket.getFarmersList().size());
    }
    @Test
    void testAddCropExistingFarmerTrue(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertTrue(testMarket.addCropExistingFarmer(generatedPrivateId, "rice", 1000,10,100));
        assertEquals(1,testMarket.getFarmersList().size());
        List<Crop> cropsOfFarmer = testMarket.getFarmer(1).getCrops();
        assertEquals(1,cropsOfFarmer.size());
        assertEquals("rice", cropsOfFarmer.get(0).getType());
        assertEquals(100, cropsOfFarmer.get(0).getQuantity());
        assertEquals(10, cropsOfFarmer.get(0).getQuality());
        assertEquals(1000, cropsOfFarmer.get(0).getPrice());
    }
    @Test
    void testAddCropExistingFarmerFalse(){
        assertFalse(testMarket.addCropExistingFarmer(1, "rice", 1000,10,100));
    }

    @Test
    void testAddCropExistingFarmerDuplicate(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertTrue(testMarket.addCropExistingFarmer(1, "rice", 1000,10,100));
        assertFalse(testMarket.addCropExistingFarmer(1, "rice", 1000,10,100));
    }

    @Test
    void testRemoveCropExistingFarmerTrue(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertTrue(testMarket.addCropExistingFarmer(generatedPrivateId, "rice", 1000,10,100));
        assertTrue(testMarket.removeCropExistingFarmer(generatedPrivateId, "rice"));
        assertEquals(1,testMarket.getFarmersList().size());
        List<Crop> cropsOfFarmer = testMarket.getFarmer(1).getCrops();
        assertEquals(0,cropsOfFarmer.size());
    }
    @Test
    void testRemoveCropExistingFarmerFalse(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertFalse(testMarket.removeCropExistingFarmer(2, "rice"));
        assertEquals(1,testMarket.getFarmersList().size());
    }

    @Test
    void testRemoveCropExistingFarmerCropInvalid(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertFalse(testMarket.removeCropExistingFarmer(1, "rice"));
    }

    @Test
    void testModifyCropExistingFarmerTrue(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertTrue(testMarket.addCropExistingFarmer(generatedPrivateId, "rice", 1000,10,100));
        assertTrue(testMarket.modifyCropExistingFarmer(generatedPrivateId, "rice", "quality", 7));
        assertTrue(testMarket.modifyCropExistingFarmer(generatedPrivateId, "rice", "quantity", 200));
        assertTrue(testMarket.modifyCropExistingFarmer(generatedPrivateId, "rice", "price", 2000));
        assertEquals(1,testMarket.getFarmersList().size());

        List<Crop> cropsOfFarmer = testMarket.getFarmer(1).getCrops();
        assertEquals(1,cropsOfFarmer.size());
        assertEquals("rice", cropsOfFarmer.get(0).getType());
        assertEquals(200, cropsOfFarmer.get(0).getQuantity());
        assertEquals(7, cropsOfFarmer.get(0).getQuality());
        assertEquals(2000, cropsOfFarmer.get(0).getPrice());

    }
    @Test
    void testModifyCropExistingFarmerFalse(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertFalse(testMarket.modifyCropExistingFarmer(2, "rice", "quality", 7));
        assertFalse(testMarket.modifyCropExistingFarmer(2, "rice", "quantity", 200));
        assertFalse(testMarket.modifyCropExistingFarmer(2, "rice", "price", 2000));
        assertEquals(1,testMarket.getFarmersList().size());
        List<Crop> cropsOfFarmer = testMarket.getFarmer(1).getCrops();
        assertEquals(0,cropsOfFarmer.size());
    }

    @Test
    void testModifyCropExistingFarmerCropInvalid(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        testMarket.addCropExistingFarmer(generatedPrivateId, "rice", 1000, 1,1);
        assertFalse(testMarket.modifyCropExistingFarmer(generatedPrivateId, "wheat", "price", 3));
    }

    @Test
    void testModifyCropExistingFarmerTraitInvalid(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        testMarket.addCropExistingFarmer(generatedPrivateId, "rice", 1000, 1,1);
        assertFalse(testMarket.modifyCropExistingFarmer(generatedPrivateId, "wheat", "p", 3));
    }

    @Test
    void testSearchFarmers(){
        int farmerOne = testMarket.addFarmer(1, 123);
        int farmerTwo = testMarket.addFarmer(2, 345);
        int farmerThree = testMarket.addFarmer(3, 678);
        assertTrue(testMarket.addCropExistingFarmer(farmerOne, "rice", 1000, 10, 100));
        assertTrue(testMarket.addCropExistingFarmer(farmerTwo, "rice", 700, 10, 100));
        assertTrue(testMarket.addCropExistingFarmer(farmerThree, "wheat", 1000, 10, 100));

        String selectFarmers = testMarket.searchFarmers("rice");

        assertTrue(selectFarmers.contains("Index: 1"));
        assertTrue(selectFarmers.contains("Location Index: 1"));
        assertTrue(selectFarmers.contains("Crop specification: \n"));
        assertTrue(selectFarmers.contains("Type= rice"));
        assertTrue(selectFarmers.contains("Price= 1000"));
        assertTrue(selectFarmers.contains("Index: 2"));
        assertTrue(selectFarmers.contains("Location Index: 2"));
        assertTrue(selectFarmers.contains("Crop specification: \n"));
        assertTrue(selectFarmers.contains("Type= rice"));
        assertTrue(selectFarmers.contains("Price= 700"));
    }

    @Test
    void testGetFarmerPresent(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        assertEquals(1,testMarket.getFarmersList().size());
        Farmer farmer = testMarket.getFarmer(1);
        assertEquals(1,farmer.getPrivateId());
        assertEquals(1,farmer.getLocation());
        assertEquals(123,farmer.getContact());
        assertEquals(0,farmer.getCrops().size());
    }
    @Test
    void testGetFarmerNotPresent(){
        int generatedPrivateId = testMarket.addFarmer(1, 123);
        Farmer farmer = testMarket.getFarmer(2);
        assertNull(farmer);
    }
    @Test
    void testSearchFarmersEmpty(){
        String selectFarmers = testMarket.searchFarmers("rice");
        assertTrue(selectFarmers.contains(""));
    }
    @Test
    void testMakePurchase(){
        int farmerOne = testMarket.addFarmer(1, 123);
        assertTrue(testMarket.addCropExistingFarmer(farmerOne, "rice", 1000, 10, 100));
        String selectFarmers = testMarket.searchFarmers("rice");
        assertEquals(123, testMarket.makePurchase(1));
    }

    @Test
    void testMakePurchaseInvalidFarmer(){
        int farmerOne = testMarket.addFarmer(1, 123);
        assertTrue(testMarket.addCropExistingFarmer(farmerOne, "rice", 1000, 10, 100));
        String selectFarmers = testMarket.searchFarmers("rice");
        assertEquals(-1, testMarket.makePurchase(2));
    }

    @Test
    void testPrintMarket(){
        int farmerOne = testMarket.addFarmer(1, 123);
        int farmerTwo = testMarket.addFarmer(2, 345);

        assertTrue(testMarket.addCropExistingFarmer(farmerOne, "rice", 1000, 10, 100));
        assertTrue(testMarket.addCropExistingFarmer(farmerTwo, "barley", 2000, 2, 20));

        String farmers = testMarket.printMarket();

        assertTrue(farmers.contains("Location Index: 1"));
        assertTrue(farmers.contains("Crop specification: \n"));
        assertTrue(farmers.contains("Type= rice"));
        assertTrue(farmers.contains("Price= 1000"));
    }

}
