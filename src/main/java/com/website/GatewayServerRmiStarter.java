package com.website;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Component
public class GatewayServerRmiStarter {

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
            GatewayServer gatewayServer = new GatewayServer();
            gatewayRegistry.rebind("GatewayServer", gatewayServer);

            // Lookup DatabaseManager's stub from its registry (use "localhost" or IP if remote)
            Registry databaseManagerRegistry = LocateRegistry.getRegistry("localhost", databaseManagerRegistryPort);
            DatabaseInterface dbStub = (DatabaseInterface) databaseManagerRegistry.lookup("DatabaseManager");
            gatewayServer.setDatabaseManager(dbStub);

            System.out.println("GatewayServer is ready for RMI requests.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}