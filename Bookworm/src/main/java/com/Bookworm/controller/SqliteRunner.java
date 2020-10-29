package com.Bookworm.controller;

import java.sql.ResultSet;

public class SqliteRunner {

    public static void main(String[] args) {

        SQLiteTest test = new SQLiteTest();
        ResultSet rs;

        try {
            rs = test.displayBooks();

            while (rs.next()) {
                System.out.println(rs.getString("title") + " " + rs.getString("title"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}