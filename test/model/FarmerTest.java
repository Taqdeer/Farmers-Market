package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FarmerTest {
    private Farmer testFarmer;
    private Crop crop;

    @BeforeEach
    void runBefore() {
        testFarmer = new Farmer(5, 1, 123456789);
        crop = new Crop(100, 10, 1000, "rice");
    }

    @Test
    void testConstructor() {
        assertEquals(1, testFarmer.getPrivateId());
        assertEquals(5, testFarmer.getLocation());
        assertEquals(123456789, testFarmer.getContact());
        assertEquals(0, testFarmer.getCrops().size());
    }

    @Test
    void testAddCrop() {
        testFarmer.addCrop(crop);
        assertEquals(1, testFarmer.getCrops().size());
        assertEquals(100, testFarmer.getSpecificCrop("rice").getQuantity());
        assertEquals(10, testFarmer.getSpecificCrop("rice").getQuality());
        assertEquals(1000, testFarmer.getSpecificCrop("rice").getPrice());
        assertEquals("rice", testFarmer.getSpecificCrop("rice").getType());
    }

    @Test
    void testAddCropDuplicate() {
        testFarmer.addCrop(crop);
        testFarmer.addCrop(crop);
        assertEquals(1, testFarmer.getCrops().size());
        assertEquals("rice", testFarmer.getSpecificCrop("rice").getType());
    }

    @Test
    void testRemoveCropPresent() {
        testFarmer.addCrop(crop);
        testFarmer.removeCrop("rice");
        assertEquals(0, testFarmer.getCrops().size());
    }

    @Test
    void testRemoveCropNotPresent() {
        testFarmer.addCrop(crop);
        assertFalse(testFarmer.removeCrop("wheat"));
        assertEquals(1, testFarmer.getCrops().size());
    }

    @Test
    void testModifyCropsChangeQuantity() {
        testFarmer.addCrop(crop);
        try {
            testFarmer.modifyCrops("rice", "quantity", 200);
            assertEquals(1, testFarmer.getCrops().size());
            assertEquals(10, testFarmer.getSpecificCrop("rice").getQuality());
            assertEquals(200, testFarmer.getSpecificCrop("rice").getQuantity());
            assertEquals(1000, testFarmer.getSpecificCrop("rice").getPrice());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

    }

    @Test
    void testModifyCropsChangeQuality() {
        testFarmer.addCrop(crop);
        try {
            testFarmer.modifyCrops("rice", "quality", 7);
            assertEquals(1, testFarmer.getCrops().size());
            assertEquals(7, testFarmer.getSpecificCrop("rice").getQuality());
            assertEquals(100, testFarmer.getSpecificCrop("rice").getQuantity());
            assertEquals(1000, testFarmer.getSpecificCrop("rice").getPrice());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

    }

    @Test
    void testModifyCropsChangePrice() {
        testFarmer.addCrop(crop);
        try {
            testFarmer.modifyCrops("rice", "price", 2000);
            assertEquals(1, testFarmer.getCrops().size());
            assertEquals(10, testFarmer.getSpecificCrop("rice").getQuality());
            assertEquals(100, testFarmer.getSpecificCrop("rice").getQuantity());
            assertEquals(2000, testFarmer.getSpecificCrop("rice").getPrice());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

    }

    @Test
    void testModifyCropsChangeInvalidTrait() {
        testFarmer.addCrop(crop);
        try {
            assertFalse(testFarmer.modifyCrops("rice", "invalidTrait", 200));
            assertEquals(1, testFarmer.getCrops().size());
            assertEquals(10, testFarmer.getSpecificCrop("rice").getQuality());
            assertEquals(100, testFarmer.getSpecificCrop("rice").getQuantity());
            assertEquals(1000, testFarmer.getSpecificCrop("rice").getPrice());
            fail("Exception should have been thrown");
        } catch (Exception e) {
           //passes
        }

    }

    @Test
    void testModifyCropNotPresent() {
        try {
            assertFalse(testFarmer.modifyCrops("wheat", "price", 10));
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }

    }

    @Test
    void testGetSpecificCropPresent() {
        testFarmer.addCrop(crop);
        assertEquals(crop, testFarmer.getSpecificCrop("rice"));
        assertEquals(1, testFarmer.getCrops().size());
    }

    @Test
    void testGetSpecificCropNotPresent() {
        testFarmer.addCrop(crop);
        assertNull(testFarmer.getSpecificCrop("wheat"));
        assertEquals(1, testFarmer.getCrops().size());
    }

    @Test
    void testDisplayCropList() {
        testFarmer.addCrop(crop);
        testFarmer.addCrop(new Crop(200, 7, 2000, "wheat"));
        assertTrue(testFarmer.displayCropList().contains("Type= rice"));
        assertTrue(testFarmer.displayCropList().contains("Price= 1000"));
        assertTrue(testFarmer.displayCropList().contains("Quality Index= 10"));
        assertTrue(testFarmer.displayCropList().contains("Quantity offered= 100"));
        assertTrue(testFarmer.displayCropList().contains("Type= wheat"));
        assertTrue(testFarmer.displayCropList().contains("Price= 2000"));
        assertTrue(testFarmer.displayCropList().contains("Quality Index= 7"));
        assertTrue(testFarmer.displayCropList().contains("Quantity offered= 200"));
    }
}