/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
import java.io.*;
import java.sql.*;
import java.util.Scanner;

import javax.lang.model.element.Name;

import java.util.Formatter;
import java.lang.String;
/**
 *
 * @author harve
 */
public class Shop {

    /**
     * @param args the command line arguments
     */
    public static final String oracleServer = "dbs3.cs.umb.edu";
    public static final String oracleServerSid = "dbs3";
    public static Connection conn;
    public static int currentID;
   
    
    
    public static void main(String args[])throws SQLException {

         conn = getConnection();

            if(conn != null)    {
                // Validate user, if not loop again until a valid user is inserted.
                System.out.println("Customer ID");
                 currentID = ID();
                 ResultSet data = selectFrom("name", "Customers",("cid =" + currentID));
                 if(data.next())
                System.out.println("Welcome back," + data.getString("name")+ "!");
                mainMenu();
                
                   
            }
            else{System.out.println("DB where not found!");
        }


        // if -1 is inserted, please create a new user.
        
        /*
        P - Products: Lists all products in products table
        O - Order: Order a product given a product id and quantity. Each customer can only order each product 
            once so make sure that there is no duplication. Subtract the total cost of this order from the budget of 
            the active customer.
        R - Return: Given a product id, return that product to the shop with a quantity of 1. Ensure that the 
            customer has ordered this product before and that the quantity ordered in the sales table will not drop 
            below 0 due to this return. Must add the cost of this product back to the customer-s budget.
        S - Product Search: Searches the products table given a substring of the product name
        E - Expenditure: Lists all orders the current customer has made. It should display the product ID, 
            product name, and the total cost of the order (quantity * price).
        C - Current Budget: List the budget for the current customer.
        X - Exit: Exit applicatio
        */
    }

    public static void mainMenu()throws SQLException{
        while(true){
        System.out.print(" ********** Main Menu **********\n");
        System.out.print(" P - Products Lists\n"+
                         " O - Place a order\n"+
                         " R - Return\n"+
                         " S - Search Product\n"+
                         " E - Expenditure\n"+
                         " C - Current Budget:\n"+
                         " X - Exit\n");
        System.out.print("choice: ");
            Scanner input = new Scanner(System.in);
                char cmd = input.next().toUpperCase().charAt(0);

            switch(cmd){

                case 'P':{
                        ResultSet data = selectFrom("*","Products");
                        System.out.print(" ********** Displaying all products **********");
                        while(data.next()){
                            System.out.print("\nCode " + data.getInt("pid"));
                            System.out.print("\tName "+ data.getString("name"));
                            System.out.print("\tPrice $"+ data.getInt("price"));
                        }
                        System.out.print("\n ********** End of products list **********");
                    }
                    break;

                case 'O':{
                        System.out.print("\nWhat product would you like to buy? ");
                        int pid = new Scanner(System.in).nextInt();
                        while(!(checkPrAv(pid))){
                            System.out.print("We dont have the item at the moment, please Try again: ");
                            pid = new Scanner(System.in).nextInt();
                        }
                        System.out.print("\nHow many would you like to buy? ");
                        int quantity = new Scanner(System.in).nextInt();

                      //  ResultSet data = selectFrom("cid","Sales","cid="+currentID);
                       // if (data.next()){
                           
                            if (pay(pid,currentID,quantity)){
                                updateSales(pid, currentID, quantity);
                            }

                        //}
    
                        
                    }
                    break;

                case 'R':{
                        int q;
                        int wallet=0;
                        System.out.print("\nWhat product would you like to return? ");
                        int pid = new Scanner(System.in).nextInt();

                       
                        ResultSet data = selectFrom("cid","Sales","cid = " + currentID+ " AND pid =" +pid);
                        if (data.next())
                            {
            
                                ResultSet dataC = selectFrom("cid,budget","Customers","cid = " +currentID);
                                ResultSet dataP = selectFrom("pid,price","Products","pid = " +pid);
                                if(dataC.next() && dataP.next()){
                                   wallet = dataC.getInt("budget");
                                   wallet += dataP.getInt("price");
                                   updateSales(pid,currentID,-1);
                                   PreparedStatement ps = null;
                                   String queryUp = "UPDATE Customers SET budget="+wallet+" WHERE cid = " + currentID;
                                   ps = conn.prepareStatement(queryUp);
                                   ps.executeUpdate();
                                   ps.close();
                                }
                            }


                    }break;

                case 'S':{
                        System.out.print("\n ********** Search Engine **********\n");
                        System.out.print("What product would you like you search?");
                        String n = new Scanner(System.in).nextLine();
                        ResultSet data = selectFrom("pid,name,price", "Products", "name LIKE \'" + n+"%\'");
                        if (data.next()){
                            System.out.println(" | ProductID: " + data.getInt("pid") +
                                               " | Product N: " + data.getString("name")+
                                               " | Product $: " + data.getInt("price"));
                        }
                    }break;

                case 'E':{
                    System.out.print("\n ********** Displaying list of orders **********\n");
                   
                    ResultSet dataS = selectFrom("pid,quantity", "Sales", "cid =" + currentID); 
   
                    while(dataS.next()){
                    ResultSet dataP = selectFrom("pid,name,price", "Products", "pid =" + dataS.getInt("pid"));
                    while(dataP.next()){
                        System.out.println(" |ProductID: " + dataP.getInt("pid") +
                                           " |Product N: " + dataP.getString("name")+
                                           " |Total Cost $: " + dataP.getInt("price")*dataS.getInt("quantity"));
                    }
                }
                    }break;
                    
                case 'C':{ System.out.print("\n ********** Current budget **********\n");
                            ResultSet dataC = selectFrom("name,budget", "Customers", "cid ="+currentID);
                            if(dataC.next())
                                System.out.println("Your wallet \""+ dataC.getString("name")+ "\"  is $"+dataC.getInt("budget"));
                    }break;

                case 'X':{
                    System.exit(1);
                    }break;

                default:
                    break;


            }
        }
                         
    }

    public static void updateSales(int item, int currentID,int quantity)throws SQLException{
        ResultSet data = selectFrom("cid,quantity","Sales","cid = "+currentID+" AND pid="+item);
        PreparedStatement ps = null;

        if(data.next()){
                          
            String queryUp = "UPDATE Sales SET quantity = "+(data.getInt("quantity")+quantity)+" WHERE cid = " + currentID+ " AND pid="+item;

            ps = conn.prepareStatement(queryUp);
            ps.execute();
            ps.close();

        }
        else{
            String query = "INSERT INTO Sales(pid, cid, quantity) VALUES (?,?,?)";

                ps = conn.prepareStatement(query);

                ps.setInt(1, item);
                ps.setInt(2, currentID);
                ps.setInt(3, quantity);

                ps.execute();
                ps.close();

            }
        }

    public static boolean pay(int item, int customer, int quantity)throws SQLException{

        ResultSet dataItem = selectFrom("price", "Products", "pid = " + item);
        ResultSet dataCustomer = selectFrom("budget","Customers","cid = "+ customer);
        int cost=0;
        int wallet=0;
        PreparedStatement ps = null;
     
        try{
            
            if(dataItem.next() && dataCustomer.next()){
                cost = dataItem.getInt("price")*quantity;
                wallet = dataCustomer.getInt("budget");
                if (cost > wallet){
                    System.out.print("The price of your cart exceeds that of your wallet\n"+
                                      "Continuing with this transaction make a negative \n"+
                                      "balance in your account, would you like to proceed?"+
                                      "(y/n)");
                    char choice = new Scanner(System.in).next().toLowerCase().charAt(0);
                    while((choice != 'y' || choice != 'n')){
                        System.out.print("Provide a y|n answer");
                        choice= new Scanner(System.in).next().charAt(0);
                    }
                    if(choice == 'n')
                        return false;
                    
                }
               
                wallet -= cost;

                String queryUp = "UPDATE Customers SET budget = "+ wallet+" WHERE cid = " + currentID;

                ps = conn.prepareStatement(queryUp);
                ps.executeUpdate();
                ps.close();
                
            }
            System.out.println("Your Purcharse was execute, Your "+(wallet >= 0 ? "remaining budget" : "debt" )+"  is: $" +wallet);
            return true;
        }
        catch(SQLException e){}
        return true;
    }

    public static void help(String cmd){

        System.out.println("Help");
    }

     public static int ID ()throws SQLException {
         boolean invalidID = true;
         int id= 0;
         Scanner input = new Scanner(System.in);
         
 
        while (invalidID){
            
            id = input.nextInt();
        
            if (checkIdAv(id) ||createIf(id == -1))
                invalidID=false; 
            else 
                System.out.println("You CID is wrong, if you dont have one please insert -1 to create one or try again: ");
        }
        

        return id;
     }

     public static boolean checkIdAv(int id) throws SQLException{
            ResultSet data = selectFrom("cid","Customers","cid = "+id);
            if (data.next())
                return true;
            return false;
     }
    
     public static boolean checkPrAv(int id) throws SQLException{
        ResultSet data = selectFrom("pid","Products","pid = " + id);
        if (data.next())
            return true;
        return false;
 }
    
     public static ResultSet selectFrom(String select, String from) throws SQLException {

     
        Statement getTable = conn.createStatement(); //get statement reference
        ResultSet dataUser = getTable.executeQuery("SELECT "+ select +" FROM "+from);
        
        return dataUser;

    }
    
     public static ResultSet selectFrom(String select, String from, String where) throws SQLException {

     
        Statement getTable = conn.createStatement(); //get statement reference
        ResultSet dataUser = getTable.executeQuery("SELECT "+ select +" FROM "+from+" WHERE "+where);
        
        return dataUser;

    }

    public static boolean createIf(boolean ans)throws SQLException{
        Scanner input = new Scanner(System.in);
        if(ans){
            System.out.print("Please provide your cid: ");
            int newId= input.nextInt();
            while (checkIdAv(newId)){
                System.out.print("CID "+newId+" is already being used, Try Again:");
                newId= input.nextInt();
                //if()
            }
            System.out.print("\nProvide name: ");
            String newName = new Scanner(System.in).nextLine();
            System.out.print("\bProvide budget: ");
            int newBudget = input.nextInt();
            createUser(newId,newName,newBudget);
            System.out.println("Welcome "+ newName+ "!");

            return true;

        }




        return false;
    }

    public static void createUser(int id, String name, int budget)throws SQLException{
        String query = "INSERT INTO Customers(cid,name, budget) VALUES (?,?,?)";
        PreparedStatement ps = null;
        try {

             ps = conn.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, budget);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            ps.execute();
            ps.close();

    }

}
    
     public static final Connection getConnection () {
        String jdbcDriver = "oracle.jdbc.OracleDriver";
        try {
             Class.forName(jdbcDriver);
        }
        catch (Exception e){
            e.printStackTrace();
        }
       
        String connString = "jdbc:oracle:thin:@" + oracleServer + ":1521:"
                             + oracleServerSid;
        
        System.out.println("Connecting to the database...");
        
        Connection conne;
        
        //Login To the database
		Scanner input = new Scanner(System.in);
		System.out.print("Username:");
		String user = input.nextLine();
		System.out.print("Password:");
		//the following is used to mask the password
		Console console = System.console();
		String pass = new String(console.readPassword()); 
        try {
            conne = DriverManager.getConnection(connString, user, pass);
            System.out.println("Connection Successful!");
        }
        catch(SQLException e){
            System.out.println("Connection Fail");
            e.printStackTrace();
            return null;
        }
        return conne;
    
    
    }
}
