package com.website;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

@Component
public class GatewayServerRmiStarter {

    GatewayServer gatewayServer = null;

    @PostConstruct
    public void startGatewayServerRmi() {
        try {
            int gatewayRegistryPort = 1098;
            int databaseManagerRegistryPort = 1099;

            // Start or get the RMI registry for GatewayServer
            Registry gatewayRegistry;
            try {
                gatewayRegistry = LocateRegistry.createRegistry(gatewayRegistryPort);
                System.out.println("RMI registry created on port " + gatewayRegistryPort);
            } catch (Exception e) {
                gatewayRegistry = LocateRegistry.getRegistry(gatewayRegistryPort);
                System.out.println("RMI registry already running on port " + gatewayRegistryPort);
            }

            // Create and bind GatewayServer
            gatewayServer = new GatewayServer();
            gatewayRegistry.rebind("GatewayServer", gatewayServer);

            // Lookup DatabaseManager's stub from its registry (use "localhost" or IP if remote)
            Registry databaseManagerRegistry = LocateRegistry.getRegistry("localhost", databaseManagerRegistryPort);
            DatabaseInterface dbStub = (DatabaseInterface) databaseManagerRegistry.lookup("DatabaseManager");
            gatewayServer.setDatabaseManager(dbStub);

            System.out.println("GatewayServer is ready for RMI requests.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String strInput = null;
        int intInput = 0;
        while(true) {
            System.out.println("Select an option:");
            System.out.println("1. Manage Database");
            System.out.println("2. Search Items");
            
            // Read user input
            try {
                strInput = System.console().readLine();
                intInput = Integer.parseInt(strInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (intInput) {
                case 1:
                    System.out.println("Not implemented");
                    break;

                case 2:
                    System.out.println("Insert name, price_min, price_max, type, ID:");
                    String name = System.console().readLine();
                    int price_min = Integer.parseInt(System.console().readLine());
                    int price_max = Integer.parseInt(System.console().readLine());
                    String type = System.console().readLine();
                    int ID = Integer.parseInt(System.console().readLine());

                    try {
                        ArrayList<product> results = gatewayServer.searchItems(name, price_min, price_max, type, ID);
                        if (results.isEmpty()) {
                            System.out.println("No products found.");
                        } else {
                            System.out.println("Products found: " + results);
                        }
                    } catch (RemoteException e) {
                        System.err.println("Error during search: " + e.getMessage());
                    }
                    break;
            
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}