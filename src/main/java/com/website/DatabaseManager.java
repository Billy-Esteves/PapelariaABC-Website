package com.website;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.xml.crypto.Data;

public class DatabaseManager extends UnicastRemoteObject implements DatabaseInterface {

    /**
     * Default constructor for DatabaseManager.
     * This constructor is required for RMI to create a remote object.
     * @throws RemoteException
     */
    protected DatabaseManager() throws RemoteException {
        super();
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
        // TODO: Implement logic to fetch items from the database based on the search criteria
    }

    @Override
    public void dbUpdate(String name, int price, int quantity, String type, int ID) throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'dbUpdate'");
        // TODO: Implement logic to update the database with the provided parameters
    }

}
