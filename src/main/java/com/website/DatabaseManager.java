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
        } catch (IOException e) {
            System.err.println("Error loading database: " + e.getMessage());
        }
    }

    /**
     * Fetch items from the database based on search criteria.
     * This method is intended to be called remotely to fetch items based on search criteria.
     */
    @Override
    public void dbFetch(String name, int price_min, int price_max, String type, int ID) throws RemoteException {
        System.out.println("Fetching items with query: " + name + 
                           ", price range: " + price_min + "-" + price_max + 
                           ", type: " + type + ", ID: " + ID);
        // Use Levenshtein distance to find similar names
        // The Levenshtein algorithm counts how many insertions, deletions, or substitutions are needed to transform one string into another.
        // If that number is less than or equal to your threshold X, you can consider them “similar.”
        
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


    public static void main(String[] args) {
        try {
            new DatabaseManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
