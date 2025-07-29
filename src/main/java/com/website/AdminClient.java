package com.website;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class AdminClient extends UnicastRemoteObject implements Admin {

    protected AdminClient() throws RemoteException {
        super();
    }

    /**
     * Method to launch the website.
     * This method is intended to be called remotely to start the website.
     */
    @Override
    public void websiteLaunch() throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'websiteLaunch'");
        // TODO: Implement logic to launch the website
    }

    /**
     * Method to shut down the website.
     * This method is intended to be called remotely to stop the website.
     */
    @Override
    public void shutDownWebsite() throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'shutDownWebsite'");
        // TODO: Implement logic to shut down the website
    }

    /**
     * Method to perform administrative tasks.
     * This method is intended to be called remotely by an admin client.
     * @param taskName Name of the task to be performed
     * @param params Parameters required for the task
     */
    @Override
    public void manageDBInterface(String taskName, HashMap<String, String> params) throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'manageDBInterface'");
        // TODO: Implement logic to handle database management tasks
    }

    /**
     * Method to search for items.
     * This method is intended to be called remotely to perform a search operation.
     * @return
     */
    @Override
    public String search(String name, int price_min, int price_max, String type, int ID) throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'search'");
        // TODO: Implement logic to perform a search operation
    }
 
}
