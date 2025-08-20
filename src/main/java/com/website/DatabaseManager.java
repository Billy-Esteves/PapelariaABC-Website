package com.website;

// for excel file reading
///////////////////////////////////////////////////////
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
///////////////////////////////////////////////////////

import java.util.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.awt.image.BufferedImage;

import javax.xml.crypto.Data;



public class DatabaseManager extends UnicastRemoteObject implements DatabaseInterface {

    /**
     * Class to hold product data along with a score for search results.
     */
    private static class resultData {
        private product product;
        private float score;

        public resultData(product product, float score) {
            this.product = product;
            this.score = score;
        }

        public product getProduct() {
            return product;
        }

        public float getScore() {
            return score;
        }
    }

    private static final String excelPath = "src/main/resources/static/xls/";
    private static final String imagePath = "src/main/resources/static/images/";

    float similarityThreshold = 0.7f; // Threshold for similarity in search results

    File exelFolder = new File(excelPath);
    File[] files = exelFolder.listFiles((dir, name) -> name.endsWith(".xlsx"));

    private ArrayList<product> products = new ArrayList<product>();


    /** 
     * Default constructor for DatabaseManager.
     * This constructor is required for RMI to create a remote object.
     * @throws RemoteException
     */
    protected DatabaseManager() throws RemoteException {
        super();

        try {
            loadDataBase();
        } catch (IOException e) {
            System.err.println("Error loading database: " + e.getMessage());
        }
    }

    /**
     * Fetches products from the database based on search criteria.
     * This method allows clients to search for products by name, price range, type, and ID.
     * @param name List of product names to search for
     * @param price_min Minimum price for filtering products
     * @param price_max Maximum price for filtering products
     * @param type Type of product to filter by
     * @param ID Product ID to filter by
     * @throws RemoteException
     */
    @Override
    public ArrayList<product> dbFetch(String name, float price_min, float price_max, String type, int ID) throws RemoteException {
        /*
        System.out.println("Fetching items with query: " + name + 
                           ", price range: " + price_min + "-" + price_max + 
                           ", type: " + type + ", ID: " + ID);
        */

        // Also the search will present the top 15 results based on a scoring system that will be implemented later.
        
        ArrayList<product> results = new ArrayList<>();
        int searchScore;

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

                resultData tempResult;
                ArrayList<resultData> resultDataList = new ArrayList<>();

                for (product p : products) {

                    searchScore = searchMatch((ArrayList<String>) Arrays.asList(name.split(" ")), p.getName());

                    if (searchScore > 0) {
                        tempResult = new resultData(p, searchScore);
                        resultDataList.add(tempResult);
                    }
                    
                }

                // Sort results based on score
                resultDataList.sort((r1, r2) -> Float.compare(r2.getScore(), r1.getScore()));

                for (resultData rd : resultDataList) {
                    System.out.println("Product: " + rd.getProduct().getName() + ", Score: " + rd.getScore());
                }

                // Add top 15 results to the final results list
                for (int i = 0; i < Math.min(15, resultDataList.size()); i++) {
                    results.add(resultDataList.get(i).getProduct());
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

    /**
     * Updates the database with the provided product information.
     * This method allows Admin Clients to perform database management tasks.
     * @param name List of product names to update
     * @param price Price of the product to update
     * @param quantity Quantity of the product to update
     * @param type Type of the product to update
     * @param ID Product ID to update
     * @param command Command to specify the operation: 0 for create, 1 for update, 2 for delete
     * @throws RemoteException
     */
    @Override
    public void dbUpdate(String name, int price, int quantity, String type, int ID, ArrayList<String> images, int command) throws RemoteException {
        //throw new UnsupportedOperationException("Unimplemented method 'dbUpdate'");
        // TODO: Implement logic to update the database with the provided parameters

        boolean productFound = false;

        switch (command) {
            case 0: // Create
                // Create a new product and add it to the list
                product newProduct = new product(type, ID, name, quantity, price, images);
                products.add(newProduct);
                
                break;

            case 1: // Update
                for (product p : products) {
                    if (p.getID() == ID) {
                        if (name != null && !name.isEmpty()) {
                            p.setName(name);
                        }
                        if (price != -1) {
                            p.setPrice(price);
                        }
                        if (quantity != -1) {
                            p.setQuantity(quantity);
                        }
                        if (type != null && !type.isEmpty()) {
                            p.setQuantity(quantity);
                        }
                        if (images != null && !images.isEmpty()) {
                            p.setImagePath(images);
                        }
                        productFound = true;
                        break;
                    }
                }
                if (!productFound) {
                    System.out.println("Product with ID " + ID + " not found for update.");
                    return;
                }
                break;

            case 2: // Delete
                products.removeIf(p -> p.getID() == ID);
                System.out.println("Product with ID " + ID + " deleted.");
                break;

            default:
                System.out.println("Invalid command.");
        }

        if (files == null || files.length == 0) {
            System.out.println("No Excel file found in the folder.");
            return;
        } else if (files.length > 1) {
            System.out.println("More than one Excel file found in the folder. Only one file must exist.");
            return;
        }


        // Load the single Excel file
        File excelFile = files[0];

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("INVENTARIO 2025 - test modified");

             // Create bold font and style
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            // Create centered and bold style
            CellStyle boldCenterStyle = workbook.createCellStyle();
            boldCenterStyle.setFont(boldFont);
            boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);

            // Create a cell style for Euro currency format
            CellStyle euroStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            euroStyle.setDataFormat(format.getFormat("#,##0.00\" €\""));
            
            // First row: "INVENTÁRIO 2025 - test"
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(2);
            titleCell.setCellValue("INVENTÁRIO 2025 - test");
            titleCell.setCellStyle(boldCenterStyle);

            // Second row: "PAPELARIA ABC - TAROUCA 200225359"
            Row subtitleRow = sheet.createRow(1);
            Cell subtitleCell = subtitleRow.createCell(2);
            subtitleCell.setCellValue("PAPELARIA ABC - TAROUCA 200225359");
            subtitleCell.setCellStyle(boldCenterStyle);

            // Third row: header
            Row header = sheet.createRow(2);
            String[] headers = {"Família", "Codigo", "Descrição", "Existência", "P. Custo", "Total"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(boldCenterStyle);
            }

            int rowIdx = 3;
            for (product p : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getType());
                row.createCell(1).setCellValue(p.getID());
                row.createCell(2).setCellValue(String.join(" ", p.getName()));
                row.createCell(3).setCellValue(p.getQuantity());
                row.createCell(4).setCellValue(p.getPrice());
                row.getCell(4).setCellStyle(euroStyle);
                row.createCell(5).setCellValue(p.getPrice() * p.getQuantity());
                row.getCell(5).setCellStyle(euroStyle);
            }

            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            System.err.println("Error writing updated products to Excel: " + e.getMessage());
        }
    }

    /**
     * Loads the database from the Excel file and image folder.
     * This method reads the Excel file and extracts product information.
     * It also matches product IDs with image files in the image folder.
     * @throws IOException
     */
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
            ArrayList<Cell> cells = new ArrayList<>();

            int rowIndex = 0;

            for(Row row : sheet) {

                product temProduct = new product("", 0, "", 0, 0, new ArrayList<String>());

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
                                temProduct.setPrice((float) cells.get(i).getNumericCellValue());
                                break;
                        }
                    }


                    // Match the product ID with the image files
                    for (File image : imageFiles) {
                        if(image.getName().startsWith(temProduct.getID() + "")) {
                            temProduct.addImagePath(imagePath + image.getName());
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

    /**
     * Gets the value of a cell as a string.
     * This method is used to read the cell values from the Excel file.
     * @param cell The cell to read
     * @return The value of the cell as a string
     */
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

    /**
     * Calculates the Levenshtein distance between two strings.
     * This method is used to calculate the similarity between search terms and product names.
     * @param str1 First string
     * @param str2 Second string
     * @return The Levenshtein distance between the two strings
     */
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

            //System.out.printf("]\n[");

            for (int j = 0; j < string1.length(); ++j) {

                currentRow[j] = 0;

                if (i == 0) {
                    currentRow[j] = j;
                } else if (j == 0) {
                    currentRow[j] = i;
                } else {
                    if (string1.toLowerCase().charAt(j) != string2.toLowerCase().charAt(i)) {
                        currentRow[j] = Math.min(Math.min(prevRow[j - 1], prevRow[j]), currentRow[j - 1]) + 1;
                    } else {
                        currentRow[j] = prevRow[j - 1];
                    }
                }
                //System.out.printf(currentRow[j] + ", ");
            }
        }

        return currentRow[currentRow.length - 1];
    }

    /**
     * Searches for products that match the search terms.
     * This method calculates a score based on the similarity of the search terms to the product names.
     * @param searchTerms List of search terms
     * @param productName List of product names
     * @return The score based on the similarity of the search terms to the product names
     */
    private int searchMatch(ArrayList<String> searchTerms, ArrayList<String> productName) {
        int score = 0;
        float similarity;

        for (String term : searchTerms) {
            for (String name : productName) {

                similarity = 1 - ((float) editDistance(term, name) / Math.max(term.length(), name.length()));

                if (similarity > similarityThreshold) {
                    ++score;
                    break; // Stop checking once a match is found
                }
            }
        }

        return score;
    }

    /*
    public static void main(String[] args) {
        boolean registryLocated = false;
        Registry registry = null;
        
        try {
            // Locate the RMI registry
            while (registryLocated) {
                try {
                    registry = LocateRegistry.getRegistry(1099);
                    registryLocated = true;
                } catch (Exception e) {
                    System.out.println("RMI registry not found. Trying again in 3 seconds...");
                    registryLocated = false;
                    Thread.sleep(3000);
                }
            }

            System.out.println("RMI registry found. Starting DatabaseManager...");

            // Create an instance of DatabaseManager and bind it to the RMI registry
            DatabaseManager dbManager = new DatabaseManager();
            registry.rebind("DatabaseManager", dbManager);

            System.out.println("DatabaseManager: ready.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        */
}
