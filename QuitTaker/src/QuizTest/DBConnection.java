/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizTest;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author steam
 */
public class DBConnection {
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/TeDB";
    static final String USER = "root";
    static final String PASS = "mysql";
    
   // throws Exception
        Connection getConnection()  {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASS);
            return connection;
        } catch (Exception e) {
            System.out.println("Error connecting to Databse! " + e);
        }
        return null;

    }
        
         ResultSet getTable() {

        // Get UserInformation
        try {
            Connection connection = getConnection();
            Statement getTable = connection.createStatement(); //get statement reference
            ResultSet dataUser = getTable.executeQuery("Select * from student;");
            return dataUser;
        } catch (Exception e) {
            System.out.println("Fail accessing DB!");
            return null;
        }
    }
    
}
