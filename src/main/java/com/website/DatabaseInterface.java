package com.website;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Database interface for RMI communication.
 * Defines methods for remote database operations.
 */
public interface DatabaseInterface extends Remote {
    
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
    List<product> dbFetch(String name, float price_min, float price_max, String type, int ID) throws RemoteException;

    /**
     * Method to manage database operations such as creating, updating, or deleting entries.
     * This method allows Admin Clients to perform database management tasks.
     * @param name
     * @param price
     * @param quantity
     * @param type
     * @param ID
     * @throws RemoteException
     */
    void dbUpdate(String name, int price, int quantity, String type, int ID) throws RemoteException;

}
