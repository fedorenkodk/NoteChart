package com.nes.db;


import java.sql.Connection;
import java.sql.DriverManager;

public class DbAccessor {

    private static final String dbPath = "NotesDb.db";
    public static void createDb() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:{}", dbPath);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return;
        }
        System.out.println("Opened database successfully");
    }

    public static void initialiseDb() {
        // if db is not found
        // createDb()
    }
}
