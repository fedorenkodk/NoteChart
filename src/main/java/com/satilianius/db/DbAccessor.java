package com.satilianius.db;


import java.sql.Connection;
import java.sql.DriverManager;

public class DbAccessor {

    private static final String dbName = "notes_db";
    public static void createDb() {
       Connection connection = null;
        try {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(String.format("jdbc:h2:./%s", dbName), "Satilianius", "H2OPassword");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return;
        }
        System.out.println("Opened database successfully");
    }

    public static void initialiseDb() {
        // if db is not found
        createDb();
    }
}
