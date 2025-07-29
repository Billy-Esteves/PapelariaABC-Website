package com.website;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface Admin extends Remote {

    /**
     * Method to launch the website.
     * This method is intended to be called remotely to start the website.
     * @throws RemoteException
     */
    void websiteLaunch() throws RemoteException;

    /**
     * Method to shut down the website.
     * This method is intended to be called remotely to stop the website.
     * @throws RemoteException
     */
    void shutDownWebsite() throws RemoteException;

    /**
     * Method to perform administrative tasks.
     * This method is intended to be called remotely by an admin client.
     * @param taskName Name of the task to be performed
     * @param params Parameters required for the task
     * @throws RemoteException
     */
    void manageDBInterface(String taskName, HashMap<String, String> params) throws RemoteException;

    /**
     * Method to search for items.
     * This method is intended to be called remotely to perform a search operation.
     * @return
     * @throws RemoteException
     */
    String search(String name, int price_min, int price_max, String type, int ID) throws RemoteException;
    
}
