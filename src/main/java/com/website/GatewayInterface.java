package com.website;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Gateway interface for RMI communication.
 * Defines methods for remote operations.
 */
public interface GatewayInterface extends Remote {

    public void setDatabaseManager(DatabaseInterface db) throws RemoteException;

    /**
     * Search for items based on a query string.
     * This method is intended to be called remotely to perform a search operation.
     * @param query
     * @param price_min
     * @param price_max
     * @param type
     * @param ID
     * @return List of products matching the search criteria.
     *         Returns an empty list if no products match the criteria or if an error occurs.
     * @throws RemoteException
     */
    ArrayList<product> searchItems(String name, int price_min, int price_max, String type, int ID) throws RemoteException;

    /**
     * Manage database operations such as creating, updating, or deleting entries.
     * This method allows Admin Clients to perform database management tasks.
     * @param action
     * @param dbName
     * @param params
     * @throws RemoteException
     */
    void manageDB(String name, int price, int quantity, String type, int ID, ArrayList<String> images, int command) throws RemoteException;
 
}
