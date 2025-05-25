package com.shop.inventory.util;

import com.shop.inventory.model.Inventory;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


 //Handles file operations for saving and loading inventory data.
 
public class FileUtil {
    private static final String INVENTORY_FILE = "inventory.dat";
    
    public static void saveInventory(Inventory inventory) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
        }
    }
    
    public static Inventory loadInventory() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
            return (Inventory) ois.readObject();
        }
    }
}