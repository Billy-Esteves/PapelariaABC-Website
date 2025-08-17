package com.website;

// for excel file reading
///////////////////////////////////////////////////////
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
///////////////////////////////////////////////////////

import java.util.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import javax.xml.crypto.Data;


public class DatabaseManager extends UnicastRemoteObject implements DatabaseInterface {

    private static final String excelPath = "src/main/resources/static/xls/";
    private static final String imagePath = "src/main/resources/static/images/";

    File exelFolder = new File(excelPath);
    File[] files = exelFolder.listFiles((dir, name) -> name.endsWith(".xlsx"));

    private List<product> products = new ArrayList<product>();

    /** 
     * Default constructor for DatabaseManager.
     * This constructor is required for RMI to create a remote object.
     * @throws RemoteException
     */
    protected DatabaseManager() throws RemoteException {
        super();

        try {
            loadDataBase();
            String temp1 = "salsaparrila";
            String temp2 = "biosaparooling";
            int distance = editDistance(temp1, temp2);
            System.out.println("Edit distance between '" + temp1 + "' and '" + temp2 + "' is: " + distance);
        } catch (IOException e) {
            System.err.println("Error loading database: " + e.getMessage());
        }
    }

    /**
     * Fetch items from the database based on search criteria.
     * This method is intended to be called remotely to fetch items based on search criteria.
     */
    @Override
    public List<product> dbFetch(String name, int price_min, int price_max, String type, int ID) throws RemoteException {
        System.out.println("Fetching items with query: " + name + 
                           ", price range: " + price_min + "-" + price_max + 
                           ", type: " + type + ", ID: " + ID);
        // Use Levenshtein distance to find similar names
        // The Levenshtein algorithm counts how many insertions, deletions, or substitutions are needed to transform one string into another.
        // If that number is less than or equal to your threshold X, you can consider them “similar.”
        // Also the search will present the top 15 results based on a scoring system that will be implemented later.
        List<product> results = new ArrayList<>();

        // If ID is provided, filter by ID only
        if (ID != -1) {
            for (product p : products) {
                if (p.getID() == ID) {
                    results.add(p);
                    break; 
                }
            }
        } else {

            // If name is provided, filter by name using Levenshtein distance, else add all products
            if (name != null && !name.isEmpty()) {
                for (product p : products) {
                    for (String n : p.getName()) {
                        if (editDistance(n, name) <= 3) { // Threshold of 3 for similarity in any term of the product's name TODO: (change later)
                            results.add(p);
                            break;
                        }
                    }
                }
            } else {
                results.addAll(products);
            }

            // If type is provided, filter by type
            if (type != null && !type.isEmpty()) {
                results.removeIf(p -> !p.getType().equalsIgnoreCase(type));
            }

            // If price range is provided, min or max, filter by price range
            if (price_min != -1){
                results.removeIf(p -> p.getPrice() < price_min);
            } 
            if (price_max != -1) {
                results.removeIf(p -> p.getPrice() > price_max);
            }

        } 

        return results;
    }

    @Override
    public void dbUpdate(String name, int price, int quantity, String type, int ID) throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'dbUpdate'");
        // TODO: Implement logic to update the database with the provided parameters
    }

    private void loadDataBase() throws IOException {

        /*
         * Checks if there is an Excel file in the folder.
         * If there is no file, it will print a message.
         * If there are multiple files, it will print a message and exit.
         */
        if (files == null || files.length == 0) {
            System.out.println("No Excel file found in the folder.");
            return;
        } else if (files.length > 1) {
            System.out.println("More than one Excel file found in the folder. Only one file must exist.");
            return;
        }

        // Load the single Excel file
        File excelFile = files[0];

        // Load images from the image folder
        File imageFolder = new File(imagePath);
        File[] imageFiles = imageFolder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        /*
         * Reads the Excel file and extracts product information.
         * Then it matches the product ID with the image files in the image folder.
         */
        try(FileInputStream fis = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // Temporary cell and product objects
            List<Cell> cells = new ArrayList<>();
            product temProduct = new product("", 0, "", 0, 0, new ArrayList<String>());

            int rowIndex = 0;

            for(Row row : sheet) {

                // Skip the header rows
                if(rowIndex > 2){
                    
                    // Load the product information from the row
                    for(int i = 0; i < 5; ++i) {
                        cells.add(row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

                        switch (i) {
                            case 0:
                                temProduct.setType(getCellValue(cells.get(i)));
                                break;
                            case 1:
                                temProduct.setID((int) cells.get(i).getNumericCellValue());
                                break;
                            case 2:
                                temProduct.setName(getCellValue(cells.get(i)));
                                break;
                            case 3:
                                temProduct.setQuantity((int) cells.get(i).getNumericCellValue());
                                break;
                            case 4:
                                temProduct.setPrice((int) cells.get(i).getNumericCellValue());
                                break;
                        }
                    }

                    // Match the product ID with the image files
                    for (File image : imageFiles) {
                        if(image.getName().startsWith(temProduct.getID() + "")) {
                            temProduct.getImagePath().add(imagePath + image.getName());
                        }
                    }

                    // Add the product to the list
                    products.add(temProduct);
                    
                    cells.clear();
                }
                ++rowIndex;
            }
        }
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private int editDistance(String str1, String str2) {
        String string1, string2;

        // Ensure that string1 is the shorter string
        if(str1.length() < str2.length()) {
            string1 = " " + str1;
            string2 = " " + str2;
        } else {
            string1 = " " + str2;
            string2 = " " + str1;
        }

        //Distance storage rows of the size of the smaller string, in order to save memory
        int prevRow[] = new int[string1.length()];
        int currentRow[] = new int[string1.length()];

        for (int i = 0; i < string2.length(); ++i) {

            // Initialize the previous row with the current row's values
            prevRow = currentRow.clone();

            System.out.printf("]\n[");

            for (int j = 0; j < string1.length(); ++j) {

                currentRow[j] = 0;

                if (i == 0) {
                    currentRow[j] = j;
                } else if (j == 0) {
                    currentRow[j] = i;
                } else {
                    if (string1.charAt(j) != string2.charAt(i)) {
                        currentRow[j] = Math.min(Math.min(prevRow[j - 1], prevRow[j]), currentRow[j - 1]) + 1;
                    } else {
                        currentRow[j] = prevRow[j - 1];
                    }
                }
                System.out.printf(currentRow[j] + ", ");
            }
        }

        return currentRow[currentRow.length - 1];
    }

    public static void main(String[] args) {
        try {
            new DatabaseManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
