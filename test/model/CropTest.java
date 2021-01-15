package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CropTest {
    private Crop testCrop;

    @BeforeEach
    void runBefore(){
        testCrop = new Crop(100,10,1000,"rice");
    }

    //Tests the constructor
    @Test
    void testConstructor(){
        assertEquals(100, testCrop.getQuantity());
        assertEquals(10, testCrop.getQuality());
        assertEquals(1000, testCrop.getPrice());
        assertEquals("rice",testCrop.getType());
    }
    @Test
    void testDisplayCrop(){
        assertTrue(testCrop.displayCrop().contains("Type= rice"));
        assertTrue(testCrop.displayCrop().contains("Price= 1000"));
        assertTrue(testCrop.displayCrop().contains("Quality Index= 10"));
        assertTrue(testCrop.displayCrop().contains("Quantity offered= 100"));
    }
    @Test
    void testSetQualityIndex(){
        testCrop.setQualityIndex(7);
        assertEquals(7, testCrop.getQuality());
    }
    @Test
    void testSetQuantity(){
        testCrop.setQuantity(200);
        assertEquals(200, testCrop.getQuantity());
    }
    @Test
    void testSetPrice(){
        testCrop.setPrice(2000);
        assertEquals(2000, testCrop.getPrice());
    }
}
