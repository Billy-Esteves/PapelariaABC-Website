package com.website;

import java.util.ArrayList;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Database interface for RMI communication.
 * Defines methods for remote database operations.
 */
public interface DatabaseInterface extends Remote {
    
    /**
     * Method to perform search operations in the database.
     * This method is intended to be called remotely to fetch items based on search criteria.
     * @param name
     * @param price_min
     * @param price_max
     * @param type
     * @param ID
     * @throws RemoteException
     */
    ArrayList<product> dbFetch(String name, float price_min, float price_max, String type, int ID) throws RemoteException;

    /**
     * Method to manage database operations such as creating, updating, or deleting entries.
     * This method allows Admin Clients to perform database management tasks.
     * @param name
     * @param price
     * @param quantity
     * @param type
     * @param ID
     * @param command
     * @throws RemoteException
     */
    void dbUpdate(String name, int price, int quantity, String type, int ID, ArrayList<String> images, int command) throws RemoteException;

}
