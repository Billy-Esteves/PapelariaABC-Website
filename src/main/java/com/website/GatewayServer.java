package com.website;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;



public class GatewayServer extends UnicastRemoteObject implements GatewayInterface {

    private static int gatewayRegistryPort = 1098; // Port for GatewayServer's RMI registry
    private static int databaseManagerRegistryPort = 1099; // Port for DatabaseManager's RMI registry
    // declare the database manager interface
    private DatabaseInterface databaseManager;

    /**
     * Default constructor for GatewayServer.
     * This constructor is required for RMI to create a remote object.
     * @throws RemoteException
     */
    protected GatewayServer() throws RemoteException {

        super();
    }

    @Override
    public void setDatabaseManager(DatabaseInterface db) {
        this.databaseManager = db;
    }

    /**
     * Search for items based on a query string.
     * This method is intended to be called remotely to perform a search operation.
     */
    @Override
    public ArrayList<product> searchItems(String name, int price_min, int price_max, String type, int ID) throws RemoteException {
        // Implementation of searchItems method
        System.out.println("Searching for items with query: " + name + 
                           ", price range: " + price_min + "-" + price_max + 
                           ", type: " + type + ", ID: " + ID);
        // TODO: logic to perform search operation, e.g., querying a database or an external API
        
        if (databaseManager != null) {
            try {
                return databaseManager.dbFetch(name, price_min, price_max, type, ID);
            } catch (RemoteException e) {
                System.err.println("RemoteException occurred while fetching items: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return new ArrayList<product>();
    }

    /**
     * Manage database operations such as creating, updating, or deleting entries.
     * This method allows Admin Clients to perform database management tasks.
     */
    @Override
    public void manageDB(String name, int price, int quantity, String type, int ID, ArrayList<String> images, int command) throws RemoteException {
        // Implementation of manageDB method
        System.out.println("Managing database: with action: " + command);
        // TODO: logic to handle database operations based on the action and parameters
    
    }

    /* 
    // TODO: add IP adress to RMI registry creation/connection
    public static void main (String[] args) throws NotBoundException {
        Registry gatewayRegistry = null;
        Registry databaseManagerRegistry = null;

        try {

            // Locate/Create GatewayServers's RMI registry
            try {
                gatewayRegistry = LocateRegistry.createRegistry(gatewayRegistryPort);
                System.out.println("RMI registry created on port " + gatewayRegistryPort);
            } catch (Exception e) {
                gatewayRegistry = LocateRegistry.getRegistry(gatewayRegistryPort);
                System.out.println("RMI registry already running on port " + gatewayRegistryPort);
            } 

            System.out.println("RMI registry available. Starting GatewayServer...");

            // Create an instance of GatewayServer and bind it to the registry
            GatewayServer gatewayServer = new GatewayServer();
            gatewayRegistry.rebind("GatewayServer", gatewayServer);

            // Locate DatabaseManager's stub from its registry
            databaseManagerRegistry = LocateRegistry.getRegistry(databaseManagerRegistryPort);
            DatabaseInterface dbStub = (DatabaseInterface) databaseManagerRegistry.lookup("DatabaseManager");
            
            // Set the database manager in the GatewayServer instance previously created
            gatewayServer.setDatabaseManager(dbStub);

        } catch (RemoteException e) {
            System.err.println("RemoteException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("NotBoundException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("GatewayServer is ready and waiting for requests...");
        }
        
    }
    */
}