package com.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AdminClient {

    private final String websiteUrl = "http://localhost:8080";

    public void checkWebsiteStatus() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(websiteUrl + "/admin/status").openConnection();
        conn.setRequestMethod("GET");
        System.out.println("Response: " + conn.getResponseCode());
    }

    public void shutdownWebsite() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(websiteUrl + "/admin/shutdown").openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(0);  // dummy body
        conn.getInputStream().close();
    }

    public static void main(String[] args) throws Exception {
        AdminClient client = new AdminClient();

        // Check if website is running
        try {
            client.checkWebsiteStatus();
            System.out.println("✅ Website is running.");
        } catch (Exception e) {
            System.out.println("❌ Website not running.");
        }

        // Wait for user input to shut down website
        System.out.println("Press Enter to shut down website...");
        new Scanner(System.in).nextLine();

        client.shutdownWebsite();
        System.out.println("🛑 Website shutdown triggered.");
    }
}
