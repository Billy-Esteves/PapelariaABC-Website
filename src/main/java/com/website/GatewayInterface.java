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

    /**
     * Search for items based on a query string.
     * This method is intended to be called remotely to perform a search operation.
     * @param query
     * @throws RemoteException
     */
    void searchItems(String name, int price_min, int price_max, String type, int ID) throws RemoteException;

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
