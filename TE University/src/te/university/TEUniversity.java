/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package te.university;

import static java.lang.System.exit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Year;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;




/**
 *
 * @author steam
 */
public class TEUniversity extends Application {
    
    // JDBC driver Name And Database URL
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/school";
    static final String USER = "root";
    static final String PASS = "mysql";
    
    Label response;
     Label space = new Label("\t");
    public static String mssg = ("");
    public static Text errorMessage = new Text();
     static PreparedStatement ps = null;
     String studentN=" ";
     String studentSNN=" ";
     boolean isMatri= false;
     String courseId[] = new String [10];
     String courseName[] = new String [10];
     String courseday[] = new String [10];
     String courseTime[] = new String [10];
     String courseRoom[] = new String [10];
     int courseCredit[] = new int [10];
     
     // added courses
     int countOfCoursesAdd = 0;
     String addCourses[] = new String[10];
     int addCourseC[] = new int[10];
     boolean coursePay[] =new boolean[10];
     int non_credit=0,credit=0;
     
     String inf_studentName;
     String inf_classyear;
     String inf_courseNumber;
     String inf_courseName;
     String inf_courseMeeting;
     String inf_coursePlace;
   //
     String reg_courses[] = new String[10];
     

    public static void main(String[] args) {
        Connection connection = null; // Manages connection
        Statement statement = null;// query statement
        ResultSet resultSet = null; // manages results

         String query = "SELECT snn , first , mid , last , street , city , state , zipcode FROM demographic";
        
        // connect to database books and query database
        try {
            // load the driver class
          
            Class.forName(DRIVER);
          
    
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, USER , PASS);
            
            // create Statement for querying database
            statement = connection.createStatement();
            
            //Prepare stament for inserting data
             ps = connection.prepareStatement(query);
            
            // query database
            resultSet = ps.executeQuery();
            
            // Process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numbersOfColumns = metaData.getColumnCount();
            System.out.println( "Student Demographic table DataBase: \n");
            
            for(int i =1; i <= numbersOfColumns; i++)
                System.out.printf("%-8s\t", metaData.getColumnName(i));
                System.out.println();
                
                while (resultSet.next())
                {
                    for (int i =1 ; i <=  numbersOfColumns; i++){
                        
                        System.out.printf("%-8s\t",resultSet.getObject(i));   }
                    
                    System.out.println();
                }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            
        }
        catch (ClassNotFoundException classNotFound){
            System.out.println("It wasnt found");
            classNotFound.printStackTrace();
        }
        finally // ensure result set, statement and connection are closed
        {
            try {
                resultSet.close();
                statement.close();
                connection.close();
                
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
        
        launch(args);
    }
    
    public void start (Stage myStage){
        myStage.setTitle("TE University");
        BorderPane rootNode = new BorderPane();
        Image loginLogo = new Image("logo.png");
        ImageView loginView = new ImageView(loginLogo);
        ImageView view2 = new ImageView(loginLogo);
        VBox mainScreen = new VBox();
        
         errorMessage.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        errorMessage.setId("fancytext3");
        
        HBox hssnBox = new HBox(20);
        Text info = new Text ("              TE University             \n" +
                              "  \"Were your dream Start to grow     \n" +
                              "And your Knowledge start to develop\".\n");
        info.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        info.setId("fancytext");
        hssnBox.getChildren().addAll(info);
        hssnBox.setAlignment(Pos.CENTER);
        hssnBox.getStylesheets().add("Style.css");
        

        mainScreen.getChildren().addAll(loginView,hssnBox);
        
        rootNode.setStyle("-fx-background-color: #888888;");
        Scene myScene = new Scene(rootNode, 800, 500);
        myStage.setScene(myScene);
        response = new Label("Menu Demo");
        
        MenuBar mb = new MenuBar();
        Menu admissions = new Menu("Admissions");
         MenuItem matr = new MenuItem("Matriculated");
        //open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
         MenuItem nonMatr = new MenuItem("Non-Matriculated");
        // MenuItem save = new MenuItem("Save");
        // save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
         MenuItem quit = new MenuItem("Quit");
         quit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        admissions.getItems().addAll(matr,nonMatr, new SeparatorMenuItem(),quit);
        mb.getMenus().add(admissions);
        
        Menu reg = new Menu("Registration");
        MenuItem fTime = new MenuItem("Full-Time");
        MenuItem pTime = new MenuItem("Part-Time");
        MenuItem nCredit = new MenuItem("Non-Credit");
        reg.getItems().addAll(fTime,pTime,nCredit);
        mb.getMenus().add(reg);
        
        Menu report = new Menu("Reports");
        MenuItem receivables = new MenuItem("Receivables");
        MenuItem cSchedule = new MenuItem("Class Schedule");
        report.getItems().addAll(receivables,cSchedule);
        mb.getMenus().add(report);
        
        // Set Action event Handlers for the menu Items.
        matr.setOnAction(e ->{ matriculated();
        /*response.setText( matr.getText() + " Selected");*/ });
        nonMatr.setOnAction(e ->{ nonmatriculated();
            //response.setText( nonMatr.getText() + " Selected"); 
        });
        quit.setOnAction(e ->{ 
            response.setText( quit.getId() + " Selected"); 
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("University");
            stage.setMinWidth(400);
            VBox vBox = new VBox(10);
            HBox hBox = new HBox(20);
            Button yes = new Button("Yes");
            Button no = new Button("No");
            Label mess = new Label("Are you Sure you wanna quit?");
            
            yes.setOnAction(ae -> { Platform.exit();});
            no.setOnAction(ae -> { stage.close();});
            hBox.setAlignment(Pos.CENTER);
            vBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(space,no,yes);
            vBox.getChildren().addAll(view2,mess,hBox);
             vBox.setStyle("-fx-background-color: #888888;");
            
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();
        });
        
        fTime.setOnAction(e ->{ fTime();
           // response.setText( fTime.getText() + " Selected"); 
        });
        pTime.setOnAction(e ->{pTime(); });
        nCredit.setOnAction(e ->{ nonTime(); });
        receivables.setOnAction(e ->{ receivable(); });
        cSchedule.setOnAction(e ->{ classSchedule(); });
        rootNode.setTop(mb);
        //
        Label support = new Label("\t\t\t");
        rootNode.setLeft(support);
        rootNode.setCenter(mainScreen);
        rootNode.setBottom(response);
        
        myStage.show();
        
    }
    
    // New Stage, registration matriculated
    
    void matriculated(){
         Stage stage = new Stage();
         BorderPane matriculated = new BorderPane();
         VBox mText = new VBox();
         VBox mField = new VBox();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         
         
         matriculated.getStylesheets().add("Style.css");
         matriculated.setStyle("-fx-background-color: #888888;");
         
         
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Matriculated");
            Text mSNN = new Text(" SNN");
            mSNN.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mSNN.setId("fancytext");
            Text mFirst = new Text(" First Name");
            mFirst.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mFirst.setId("fancytext");
            Text mMiddle= new Text(" Middle Initial");
            mMiddle.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mMiddle.setId("fancytext");
            Text mLast= new Text(" Last Name");
            mLast.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mLast.setId("fancytext");
            Text mStreet= new Text(" Street Addrest");
            mStreet.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mStreet.setId("fancytext");
            Text mCity= new Text(" City");
            mCity.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mCity.setId("fancytext");
            Text mState= new Text(" State");
            mState.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mState.setId("fancytext");
            Text mZipCode= new Text(" Zipcode");
            mZipCode.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mZipCode.setId("fancytext");
            Text mTodayDate= new Text(" Today Dates");
            mTodayDate.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mTodayDate.setId("fancytext");
            Text mYearM= new Text(" Year of Matriculation");
            mYearM.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mYearM.setId("fancytext");
            Text mDegree= new Text(" Degree");
            mYearM.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mYearM.setId("fancytext");
            
            TextField mfSNN = new TextField();
            TextField mfFirst = new TextField();
            TextField mfMiddle = new TextField();
            TextField mfLast = new TextField();
            TextField mfStreet = new TextField();
            mfStreet.setPrefWidth(280);
            TextField mfCity = new TextField();
            mfCity.setPrefWidth(200);
            TextField mfState = new TextField();
            mfState.setPrefWidth(160);
            TextField mfZipCode = new TextField();
            mfZipCode.setPrefWidth(200);
            TextField mfTodayDate = new TextField(); // Take a look
            
            
            ObservableList<String> options = FXCollections.observableArrayList(
        "FreshMan",
        "Sophomore",
        "Junior",
        "Senior"
    ); final ComboBox mCYear = new ComboBox(options);

ObservableList<String> options2 = FXCollections.observableArrayList(
        "Associate of Science in Computer Programming ",
        "Associate of Arts in Humanities"
    ); final ComboBox degreeBox = new ComboBox(options2);
    
    HBox snn = new HBox(mSNN,mfSNN);
     snn.setSpacing(103);
    HBox first = new HBox(mFirst,mfFirst);
     first.setSpacing(40);
    HBox middle = new HBox(mMiddle,mfMiddle);
     middle.setSpacing(18);
    HBox last = new HBox(mLast,mfLast);
     last.setSpacing(44);
    HBox address = new HBox(mStreet,mfStreet,mCity , mfCity,mState,mfState,mZipCode,mfZipCode);
     address.setSpacing(10);
    HBox year = new HBox(mYearM,mCYear,mDegree,degreeBox);
     year.setSpacing(10);
     
     CheckBox hc = new CheckBox("High School Diploma");
     CheckBox imm = new CheckBox("Immunization");
     Button submit = new Button("Submit");
     Button reset = new Button("Reset");
     HBox buttons = new HBox(submit,reset);
     buttons.setAlignment(Pos.CENTER);
     buttons.setSpacing(10);

     submit.setOnAction(e -> {
         
         boolean hgd = hc.isSelected(), iMM = imm.isSelected();
         printErrors(hgd, iMM);   
         if (hgd == true && iMM == true){
             String nSNN = mfSNN.getText();
             String nFirst = mfFirst.getText();
             String nMid = mfMiddle.getText();
             String nLast = mfLast.getText();
             String nStreet = mfStreet.getText();
             String nCity = mfCity.getText();
             String nState = mfState.getText();
             String nZipCode = mfZipCode.getText();
             String nDate = mfTodayDate.getText();
             String nDegree = (degreeBox.getValue().toString()== "Associate of Arts in Humanities" ? "AA" : "AS");
             String yearOf = mCYear.getValue().toString();
             try{
             addInfo(nSNN,nFirst,nMid,nLast,nStreet,nCity,nState,nZipCode,nDate);
             studentN = nFirst + " " +nLast;
             studentSNN = nSNN;
             isMatri = true;
             addNewStudent(nSNN,nDegree,hgd,iMM,yearOf,true);
             }catch (SQLException esq){ esq.printStackTrace();}
             //KPTregi
             coursesAdded();
             stage.close();
             
         }
     
     });
     
     reset.setOnAction(e ->{stage.close();
                            matriculated();});
            
            VBox allignBox = new VBox();
            allignBox.getChildren().addAll(snn,first,middle,last,address,year,hc,imm,errorMessage,buttons);
            allignBox.setAlignment(Pos.CENTER);
            allignBox.setSpacing(15);
            matriculated.setTop(logoView);
            matriculated.setCenter(allignBox);
            
                        Scene scene = new Scene(matriculated,850, 550);
            stage.setScene(scene);
            stage.show();
        
    }
    
      void nonmatriculated(){
         Stage stage = new Stage();
         BorderPane nonmatriculated = new BorderPane();
         VBox mText = new VBox();
         VBox mField = new VBox();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         
         
         nonmatriculated.getStylesheets().add("Style.css");
         nonmatriculated.setStyle("-fx-background-color: #888888;");
         
         
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Non-Matriculated");
            Text mSNN = new Text(" SNN");
            mSNN.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mSNN.setId("fancytext");
            Text mFirst = new Text(" First Name");
            mFirst.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mFirst.setId("fancytext");
            Text mMiddle= new Text(" Middle Initial");
            mMiddle.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mMiddle.setId("fancytext");
            Text mLast= new Text(" Last Name");
            mLast.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mLast.setId("fancytext");
            Text mStreet= new Text(" Street Addrest");
            mStreet.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mStreet.setId("fancytext");
            Text mCity= new Text(" City");
            mCity.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mCity.setId("fancytext");
            Text mState= new Text(" State");
            mState.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mState.setId("fancytext");
            Text mZipCode= new Text(" Zipcode");
            mZipCode.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mZipCode.setId("fancytext");
            Text mTodayDate= new Text(" Today Dates");
            mTodayDate.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mTodayDate.setId("fancytext");
            
            TextField mfSNN = new TextField();
            TextField mfFirst = new TextField();
            TextField mfMiddle = new TextField();
            TextField mfLast = new TextField();
            TextField mfStreet = new TextField();
            mfStreet.setPrefWidth(280);
            TextField mfCity = new TextField();
            mfCity.setPrefWidth(200);
            TextField mfState = new TextField();
            mfState.setPrefWidth(160);
            TextField mfZipCode = new TextField();
            mfZipCode.setPrefWidth(200);
            TextField mfTodayDate = new TextField(); // Take a look
            
    
    HBox snn = new HBox(mSNN,mfSNN);
     snn.setSpacing(103);
    HBox first = new HBox(mFirst,mfFirst);
     first.setSpacing(40);
    HBox middle = new HBox(mMiddle,mfMiddle);
     middle.setSpacing(18);
    HBox last = new HBox(mLast,mfLast);
     last.setSpacing(44);
    HBox address = new HBox(mStreet,mfStreet,mCity , mfCity,mState,mfState,mZipCode,mfZipCode);
     address.setSpacing(10);
     CheckBox imm = new CheckBox("Immunization");
     Button submit = new Button("Submit");
     Button reset = new Button("Reset");
     HBox buttons = new HBox(submit,reset);
     buttons.setAlignment(Pos.CENTER);
     buttons.setSpacing(10);

     submit.setOnAction(e -> {
         boolean iMM = imm.isSelected();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.setTitle("Confirmation Alert");
            stage2.setMinWidth(400);
            VBox vBox = new VBox(10);
            HBox hBox = new HBox(20);
            Button yes = new Button("Yes");
            Button no = new Button("No");
            Text mess = new Text(" If you dont provide the Immunization You wont be Allowed \n"
                                  +"To register more than 9 credit. \n"
                                  +"Are you Sure?");
            mess.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            mess.setId("fancytext");
            
            yes.setOnAction(ae -> {
             String nSNN = mfSNN.getText();
             String nFirst = mfFirst.getText();
             String nMid = mfMiddle.getText();
             String nLast = mfLast.getText();
             String nStreet = mfStreet.getText();
             String nCity = mfCity.getText();
             String nState = mfState.getText();
             String nZipCode = mfZipCode.getText();
             String nDate = mfTodayDate.getText();
             try{
             addInfo(nSNN,nFirst,nMid,nLast,nStreet,nCity,nState,nZipCode,nDate);
             studentN = nFirst + " " +nLast;
             studentSNN = nSNN;
             isMatri = false;
             addNewStudent(nSNN," ",false,iMM," ",false);
             }catch (SQLException esq){ esq.printStackTrace();}
             stage2.close();
             stage.close(); 
             });
            Image loginCon = new Image("hand.png");
            ImageView logoCon = new ImageView(loginCon);
            no.setOnAction(ae -> { stage2.close();});
            hBox.setAlignment(Pos.CENTER);
            vBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(space,no,yes);
            vBox.getChildren().addAll(logoCon,mess,hBox);
             vBox.setStyle("-fx-background-color: #888888;");
            
            Scene scene = new Scene(vBox);
            stage2.setScene(scene);
            stage2.show();

             
         
     
     });
 
            VBox allignBox = new VBox();
            allignBox.getChildren().addAll(snn,first,middle,last,address,imm,errorMessage,buttons);
            allignBox.setAlignment(Pos.CENTER);
            allignBox.setSpacing(15);
            nonmatriculated.setTop(logoView);
            nonmatriculated.setCenter(allignBox);
            
                        Scene scene = new Scene(nonmatriculated,850, 550);
            stage.setScene(scene);
            stage.show();
        
    }
    
    public static void printErrors(boolean highs , boolean immunization){
                mssg = "";
        if (highs == false) {
            mssg += ("You Must have a High School Diploma to enroll as a Matriculate Student.\n");
        }

        if (immunization == false) {
            mssg += ("You Must have a Immunization.\n");
        }
         errorMessage.setText(mssg);
    }
    
    public void addInfo(String nSNN,String nFirst,String nMid,String nLast,String nStreet,String nCity,String nState,String nZipCode,String nDate)throws SQLException{
       ps = null;
        try
      {
        Connection connection = DriverManager.getConnection(DATABASE_URL, USER , PASS);
      
        String query = "INSERT INTO demographic (snn, first, mid, last , street, city, state, zipcode) VALUES (?,?,?,?,?,?,?,?)";
        try {
            
        
         ps = connection.prepareStatement(query);
        
        ps.setString(1, nSNN);
        ps.setString(2, nFirst);
        ps.setString(3, nMid);
        ps.setString(4, nLast);
        ps.setString(5, nStreet);
        ps.setString(6, nCity);
        ps.setString(7, nState);
        ps.setString(8, nZipCode);

       }
        catch(SQLException sqle)
        {sqle.printStackTrace();
        }
      }catch (SQLException eql){
           eql.printStackTrace();
       }
      finally {
          ps.execute();
          ps.close();
      }
    }
    
    void addNewStudent(String snn,String nDegree,boolean hsd, boolean imm, String yearOf, boolean isMatriculated)throws SQLException{
        String highS = (hsd == true ? "Y" : "N");
        String immunization = (imm == true ? "Y" : "N");      
        Connection connection = DriverManager.getConnection(DATABASE_URL, USER , PASS);
        String query = "INSERT INTO student (snn,hsd , imm, degree, yearsof,matriculated) VALUES (?,?,?,?,?,?)";
        ps= null;
        try
      {
        ps = connection.prepareStatement(query);
        
        try {
        ps.setString(1, snn);
        ps.setString(2, highS);
        ps.setString(3, immunization);
        ps.setString(4, nDegree);
        ps.setString(5, yearOf);
        ps.setBoolean(6, isMatriculated);

       }
        catch(SQLException sqle)
        {sqle.printStackTrace();
        }
      }catch (SQLException eql){
           eql.printStackTrace();
       }
      finally {
            try{
          ps.execute();
          ps.close();
            }catch (Exception exception){System.out.println("Couldnt execute or close");}
      }
        
    }
    
    void fTime() {
        
        BorderPane background = new BorderPane();
         Stage stage = new Stage();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         background.getStylesheets().add("Style.css");
         background.setStyle("-fx-background-color: #888888;");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Full Time");
        
        VBox personal = new VBox();
        Text student = new Text ("Student Name: "+studentN);
        student.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        student.setId("fancytext");
        
        Text matriculated = new Text("You "+(isMatri == true ? "are" : "are not") + " a matriculated student");
        matriculated.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        matriculated.setId("fancytext");
       
        Text finf = new Text ("\nAll 3 credit courses cost $285.00 \n"+
                                    "There is a 5 registration fee.\n"+
                                    "However Students who register \nfor more " +
                                    "than nine credits \npay $265.00 per three-credit\n" + 
                                    "courses for all beyond nine credit.\n");
        finf.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        finf.setId("fancytext");
        
        VBox infBox = new VBox(finf);
         infBox.setSpacing(10);
         infBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: black;" );
         Button registerB = new Button("Register Now!");
         registerB.getStyleClass().add("button1");
         registerB.setOnAction(e ->{
         
             register(stage);
             
         });
         
        personal.getChildren().addAll( logoView,student , matriculated,infBox,registerB);
        background.setLeft(personal);
        personal.setSpacing(20);
        personal.setBorder(Border.EMPTY);
        ScrollPane scrollPane = new ScrollPane();
         VBox coursesList = new VBox();
         
         
        // Set content for ScrollPane
        // COurses List 
      
        int i = 0;
        try{ 
            ResultSet catalog = getTable("courses");
        while (catalog.next()){
           
            System.out.println(catalog.getString("courseid"));
              courseId[i]=catalog.getString("courseid");
             courseName[i]=catalog.getString("name");
             courseday[i]=catalog.getString("day");
             courseTime[i]=catalog.getString("time");
             courseRoom[i]=catalog.getString("room");
             courseCredit[i]=catalog.getInt("credit");
                        i++;
        }
        }catch (SQLException ex) {System.out.println("");}
for (int j=0 ; j < 10 ; j++){
    System.out.println(courseId[j]);
        coursesList.getChildren().addAll(makeBox(j,"fullTime"));
}
                scrollPane.setContent(coursesList);
        // Always show vertical scroll bar
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        background.setCenter(scrollPane);
        
             Scene scene = new Scene(background,900,600);
            stage.setScene(scene);
            stage.show();
            
                    search(stage,"full");
            
           
        }
    
void nonTime(){
            
        BorderPane background = new BorderPane();
         Stage stage = new Stage();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         background.getStylesheets().add("Style.css");
         background.setStyle("-fx-background-color: #888888;");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Full Time");
        
        VBox personal = new VBox();
        Text student = new Text ("Student Name: "+studentN);
        student.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        student.setId("fancytext");
        
        Text matriculated = new Text("You "+(isMatri == true ? "are" : "are not") + " a matriculated student");
        matriculated.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        matriculated.setId("fancytext");
       
        Text finf = new Text ("\nAll 3 credit courses cost $285.00 \n"+
                                    "There is a 5 registration fee.\n"+
                                    "However Students who register \nfor more " +
                                    "than nine credits \npay $265.00 per three-credit\n" + 
                                    "courses for all beyond nine credit.\n");
        finf.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        finf.setId("fancytext");
        
        VBox infBox = new VBox(finf);
         infBox.setSpacing(10);
         infBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: black;" );
         Button registerB = new Button("Register Now!");
         registerB.getStyleClass().add("button1");
         registerB.setOnAction(e ->{
         
             register(stage);
             
         });
         
        personal.getChildren().addAll( logoView,student , matriculated,infBox,registerB);
        background.setLeft(personal);
        personal.setSpacing(20);
        personal.setBorder(Border.EMPTY);
        ScrollPane scrollPane = new ScrollPane();
         VBox coursesList = new VBox();
         
         
        // Set content for ScrollPane
        // COurses List 
      
        int i = 0;
        try{ 
            ResultSet catalog = getTable("courses");
        while (catalog.next()){
           
            System.out.println(catalog.getString("courseid"));
              courseId[i]=catalog.getString("courseid");
             courseName[i]=catalog.getString("name");
             courseday[i]=catalog.getString("day");
             courseTime[i]=catalog.getString("time");
             courseRoom[i]=catalog.getString("room");
             courseCredit[i]=catalog.getInt("credit");
                        i++;
        }
        }catch (SQLException ex) {System.out.println("");}
for (int j=0 ; j < 10 ; j++){
    System.out.println(courseId[j]);
        coursesList.getChildren().addAll(makeBox(j,"nonTime"));
}
                scrollPane.setContent(coursesList);
        // Always show vertical scroll bar
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        background.setCenter(scrollPane);
        
             Scene scene = new Scene(background,900,600);
            stage.setScene(scene);
            stage.show();
            
                    search(stage,"non");
}
    
void search(Stage stage, String partOfFull){
if (studentN.equals(" ")){
         BorderPane searchb = new BorderPane();
         Stage search = new Stage();
         Image loginLogo2 = new Image("hand.png");
         ImageView logoView2 = new ImageView(loginLogo2);
         searchb.getStylesheets().add("Style.css");
         searchb.setStyle("-fx-background-color: #888888;");
            search.initModality(Modality.APPLICATION_MODAL);
            search.setTitle("Full Time"); 
            Text searchSNN = new Text ("SNN");
            TextField snnField = new TextField();
            HBox allign = new HBox(searchSNN, snnField);
            allign.setSpacing(20);
            Button searchB = new Button("Search");
            VBox position = new VBox(logoView2, allign,searchB);
            position.setAlignment(Pos.CENTER);
            position.setSpacing(20);
            
            searchB.setOnAction(e -> {
                            try{
         ResultSet set = getTable("demographic");

       while(set.next()){
                        
                        //System.out.println("Field: "+snnField.getText());
                      //  System.out.println("FieldV: "+studentSNN);
                        
                        if(snnField.getText().equals(set.getString("snn"))){
                        studentN = set.getString("first");
                        studentSNN = snnField.getText();
                        isMatri = isMatriculated();
                        coursesAdded();
                        search.close();
                        stage.close();
                        if (partOfFull.equals("full"))
                            fTime();
                        else if (partOfFull.equals("part"))
                            pTime();
                        }
                        else 
                            nonTime();
                                  }
            }catch (SQLException exp) {System.out.println();}
            }); 
            searchb.setCenter(position);
            Scene scene2 = new Scene(searchb,400,300);
            search.setScene(scene2);
            search.show();
 }
}
    
    void pTime() {
        
        BorderPane background = new BorderPane();
         Stage stage = new Stage();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         background.getStylesheets().add("Style.css");
         background.setStyle("-fx-background-color: #888888;");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Part-Time");
        
        VBox personal = new VBox();
        Text student = new Text ("Student Name: "+studentN);
        student.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        student.setId("fancytext");
        
        Text matriculated = new Text("You "+(isMatri == true ? "are" : "are not") + " a matriculated student");
        matriculated.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        matriculated.setId("fancytext");
       
        Text finf = new Text ("\nAll 3 credit courses cost $300.00 \n"+
                                    "There is a 5 registration fee.\n"+
                                    "Part-Time Student can only take" +
                                    "six or fewer credit.\n");
        finf.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        finf.setId("fancytext");
        
        VBox infBox = new VBox(finf);
         infBox.setSpacing(10);
         infBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: black;" );
         Button registerB = new Button("Register Now!");
         registerB.getStyleClass().add("button1");
         registerB.setOnAction(e ->{
         
             register(stage);
             
         });
         
        personal.getChildren().addAll( logoView,student , matriculated,infBox,registerB);
        background.setLeft(personal);
        personal.setSpacing(20);
        personal.setBorder(Border.EMPTY);
        ScrollPane scrollPane = new ScrollPane();
         VBox coursesList = new VBox();
         
         
        // Set content for ScrollPane
        // COurses List 
      
        int i = 0;
        try{ 
            ResultSet catalog = getTable("courses");
        while (catalog.next()){
           
            System.out.println(catalog.getString("courseid"));
              courseId[i]=catalog.getString("courseid");
             courseName[i]=catalog.getString("name");
             courseday[i]=catalog.getString("day");
             courseTime[i]=catalog.getString("time");
             courseRoom[i]=catalog.getString("room");
             courseCredit[i]=catalog.getInt("credit");
                        i++;
        }
        }catch (SQLException ex) {System.out.println("");}
for (int j=0 ; j < 10 ; j++){
    System.out.println(courseId[j]);
        coursesList.getChildren().addAll(makeBox(j,"partTime"));
}
                scrollPane.setContent(coursesList);
        // Always show vertical scroll bar
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        
        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        background.setCenter(scrollPane);
        
             Scene scene = new Scene(background,900,600);
            stage.setScene(scene);
            stage.show();
            
                   search(stage,"part");
    }
    
    boolean isMatriculated() throws SQLException{
        boolean test=false;
                ResultSet set2 = getTable("student");
                while (set2.next()){
                       // System.out.println("hejejd"+studentSNN);
                      //  System.out.println("KkField: "+set2.getString("snn"));
                if(studentSNN.equals(set2.getString("snn"))){
                 test = set2.getBoolean("matriculated");
                 
                           }
                        }
        
        
        return  test;
    }
    
    ResultSet getTable(String field){
        
        // Get UserInformation
   try{
   Connection connection = getConnection();
   Statement getTable = connection.createStatement(); //get statement reference
   ResultSet dataUser = getTable.executeQuery("Select * from "+field+";");
   return dataUser;
   }catch(Exception e){System.out.println("Fail accessing DB!");
   return null;}
    }

Connection getConnection() throws Exception {
    try {

    Class.forName(DRIVER);
    Connection connection = DriverManager.getConnection(DATABASE_URL,USER,PASS);
    return connection;
    }catch(Exception e) {System.out.println("Error connecting to Databse! "+e);}
    return null;

    }

VBox makeBox(int i, String partOrFull){
    if(!(partOrFull.equals("nonTime"))){
Text courseID  = new Text(courseId[i]);
            courseID.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
            courseID.setId("fancytext");
Text courseN  = new Text(courseName[i]);
            courseN.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
            courseN.setId("fancytext");
Text coursed = new Text("Day : "+courseday[i]);
            coursed.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            coursed.setId("fancytext");
Text courseT = new Text("Time : "+courseTime[i]);
            courseT.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
           courseT.setId("fancytext");
Text courseR = new Text("Room : "+courseRoom[i]);
            courseR.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            courseR.setId("fancytext");
Text courseC = new Text(("Credit : "+courseCredit[i]));
           courseC.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            courseC.setId("fancytext");
            Text added = new Text("Course added");
           added.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
            added.setId("fancytext4");

            HBox title = new HBox(courseID, courseN);
            title.setSpacing(10);
            VBox schedule = new VBox(coursed,courseT,courseC);
            schedule.setSpacing(10);
            
            Button bt = new Button("add");
            bt.setOnAction(e -> {
                if(partOrFull.equals("fullTime")){
                System.out.println("Name" + studentN);
                    addCourses[countOfCoursesAdd] = courseId[i];
                                    System.out.println("course credit" + courseCredit[i]);
                    addCourseC[countOfCoursesAdd] = courseCredit[i];
                    countOfCoursesAdd++;
                System.out.println("Social Security" + studentSNN);
                
               // regCourses(, mssg);
                
                
            System.out.println("You Cliick mee Thanks" + courseId[i]);
                }
                else 
                {
                    if(checkPart(i)){
                    addCourses[countOfCoursesAdd] = courseId[i];
                    addCourseC[countOfCoursesAdd] = courseCredit[i];
                    countOfCoursesAdd++;
                    }
                    else
                        showPartAlert();
                }
                
            });
            HBox body;
         if(courseAva(i)){
            body = new HBox(schedule, bt);
         }
         else
         {
             if(courseP(i))
             {added.setText("Registered");
             body = new HBox(schedule,added);
             }
             
             else
              body = new HBox(schedule,added);
         }
            
            body.setSpacing(100);
            VBox whole = new VBox(title,body);
                    whole.setSpacing(10);
                    whole.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
                    return whole;
    }
    else {
        if(courseCredit[i]==0){
        Text courseID  = new Text(courseId[i]);
            courseID.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
            courseID.setId("fancytext");
Text courseN  = new Text(courseName[i]);
            courseN.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
            courseN.setId("fancytext");
Text coursed = new Text("Day : "+courseday[i]);
            coursed.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            coursed.setId("fancytext");
Text courseT = new Text("Time : "+courseTime[i]);
            courseT.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
           courseT.setId("fancytext");
Text courseR = new Text("Room : "+courseRoom[i]);
            courseR.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            courseR.setId("fancytext");
Text courseC = new Text(("Credit : "+courseCredit[i]));
           courseC.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
            courseC.setId("fancytext");
            Text added = new Text("Course added");
           added.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
            added.setId("fancytext4");

            HBox title = new HBox(courseID, courseN);
            title.setSpacing(10);
            VBox schedule = new VBox(coursed,courseT,courseC);
            schedule.setSpacing(10);
            
            Button bt = new Button("add");
            bt.setOnAction(e -> {
                if(partOrFull.equals("fullTime")){
                System.out.println("Name" + studentN);
                    addCourses[countOfCoursesAdd] = courseId[i];
                                    System.out.println("course credit" + courseCredit[i]);
                    addCourseC[countOfCoursesAdd] = courseCredit[i];
                    countOfCoursesAdd++;
                System.out.println("Social Security" + studentSNN);
                
               // regCourses(, mssg);
                
                
            System.out.println("You Cliick mee Thanks" + courseId[i]);
                }
                else 
                {
                    if(checkPart(i)){
                    addCourses[countOfCoursesAdd] = courseId[i];
                    addCourseC[countOfCoursesAdd] = courseCredit[i];
                    countOfCoursesAdd++;
                    }
                    else
                        showPartAlert();
                }
                
            });
            HBox body;
         if(courseAva(i)){
            body = new HBox(schedule, bt);
         }
         else
         {
             if(courseP(i))
             {added.setText("Registered");
             body = new HBox(schedule,added);
             }
             
             else
              body = new HBox(schedule,added);
         }
            
            body.setSpacing(100);
            VBox whole = new VBox(title,body);
                    whole.setSpacing(10);
                    whole.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
        
        
            return whole;
        }
        VBox whole = new VBox();
        return whole;
            
    }
 
}

void showPartAlert(){
             Image loginLogo = new Image("hand.png");
             
         ImageView logoView = new ImageView(loginLogo);
Stage stage = new Stage();
           
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Univerisy");
            stage.setMinWidth(400);
            VBox vBox = new VBox(10);
            HBox hBox = new HBox(20);
            Button oki = new Button("oki");

            Text warning= new Text("You are Trying to register more than\n"
                                  +"6 Credit, please used Full Time Student. ");
            System.out.println("\007");
            System.out.flush();
            warning.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            warning.setId("fancytext2");
            
            
            oki.setOnAction(ae -> { stage.close();});
            hBox.setAlignment(Pos.CENTER);
            vBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(space,oki);
            vBox.getChildren().addAll(logoView,warning,hBox);
             vBox.setStyle("-fx-background-color: #888888;");
            
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();

}

boolean checkPart(int i){
    int count =1;
for (int j = 0 ; j < countOfCoursesAdd ; j++){
    if (courseCredit[i] == 0)
        return true;
    else 
    {
        if (addCourseC[j] == 3)
            count++;
    }
    if(count >2)
        return false;
}
 return true;
}

boolean courseAva (int i){
    
       for (int k =0; k < countOfCoursesAdd; k++){
           System.out.println("CoursesID "+addCourses[k]);
                if(courseId[i].equals(addCourses[k])){
                    return false;
                }
                
            }
       return true;
}

boolean courseP(int i){
          // for (int k =0; k < countOfCoursesAdd; k++){
           System.out.println("Courses pay? "+coursePay[i]);
                if(coursePay[i]){
                    return true;
                }
                
            
       return false;
}


void coursesAdded(){
    
    ResultSet adset = getTable("register");
    countOfCoursesAdd=0;
    try {
        while(adset.next()){
            if(studentSNN.equals(adset.getString("ssn"))){
            addCourses[countOfCoursesAdd]=adset.getString("classreg");
            addCourseC[countOfCoursesAdd]=adset.getInt("credit");
            coursePay[countOfCoursesAdd]=adset.getBoolean("pay");
            countOfCoursesAdd++;
            }
        }
    } catch (SQLException e) {
    }
}

    void addCourses(String regSSN,String regcourseID, int regCredit)throws SQLException{   
        Connection connection = DriverManager.getConnection(DATABASE_URL, USER , PASS);
        String query = "INSERT INTO register (ssn,classreg,credit,pay) VALUES (?,?,?,?)";
        ps= null;
        boolean temp[] = new boolean [countOfCoursesAdd+1];
        try
      {
        ps = connection.prepareStatement(query);
        
        try {
//            for(int k =0 ; k < countOfCoursesAdd; k++){
//                if (studentSNN.equals(regSSN) ){
//                    if(addCourses[k].equals(regcourseID)){
//                        System.out.println("Same");
//                        return;
//                    }
//                }
//            }
           
        ps.setString (1, regSSN);
        ps.setString (2, regcourseID);
        ps.setInt    (3, regCredit);
        ps.setBoolean(4, true);


       }
        catch(SQLException sqle)
        {sqle.printStackTrace();
        }
      }catch (SQLException eql){
           eql.printStackTrace();
       }
      finally {
            try{
          ps.execute();
          ps.close();
            }catch (Exception exception){System.out.println("Couldnt execute or close");}
      }
        
    }
    
    void register(Stage previous){
        String message= " ";
        String calculation1="",calculation2="";
        
         non_credit=0;
         credit=0;
        double per_course=285.00,per_nonCredit=150.00,discount=265.00;
         BorderPane registerPane = new BorderPane();
         Stage stage = new Stage();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         Text messages = new Text();
         messages.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         messages.setId("fancytext");
         
         Text calc = new Text ();
         calc.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         calc.setId("fancytext");
         
         Text student = new Text ("Student Name: "+studentN);
         student.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         student.setId("fancytext");
        
         Button pay = new Button("Pay");
         registerPane.getStylesheets().add("Style.css");
         registerPane.setStyle("-fx-background-color: #888888;");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Registrate NOW!");
        
        VBox personal = new VBox();
        
        
        int i = 0;
        for(int j =0 ; j < countOfCoursesAdd; j++){
            System.out.println("Course Count#"+j+ " = "+addCourseC[j]);
            if (addCourseC[j] == 0)
                non_credit++;
            else
                credit++;
        }
        if (credit < 3) {message += "You are NOT elegible to be a\n"
                                 +  "Full time Student.";
                         per_course=300.00;
        }
        else{
        if (credit == 3)
            message += "You are a Full Time Student.\n";
        if(credit > 3)
            message += "You are elegible for an discount.\n"
                     + "Every Course after 9 credit now cost \n"
                     + " $265.00";
        
        
        }
        messages.setText(message);
        
        
        calculation1= "You have \"" + non_credit + "\" Non Credit course" +(non_credit ==1 ? "" : "s") +
                      "You have \"" + credit     + "\" Credit course"     +(non_credit ==1 ? "" : "s") +
                      "Your Amount to pay is $"+ (non_credit * per_nonCredit+((credit >3 ? (credit-3)*discount+3*per_course : credit*per_course))+5.00);
        
        calc.setText(calculation1);
        
        pay.setOnAction (e -> {
                for (int j= 0 ; j < countOfCoursesAdd; j++){
                try {
                addCourses(studentSNN, addCourses[j],addCourseC[j]);}
                catch(SQLException err){err.printStackTrace();}
                }
                     
                        stage.close();
                        previous.close();
                        fTime();
        });
        
        VBox center = new VBox();
        center.getChildren().addAll(messages,calc,pay);
        center.setSpacing(30);
        center.setAlignment(Pos.CENTER);
   registerPane.setTop(logoView);
   registerPane.setLeft(student);
   registerPane.setCenter(center);
           
           Scene scene2 = new Scene(registerPane,900,500);
            stage.setScene(scene2);
            stage.show();
        }
       
    void getStudentInfo(){
    
    ResultSet set = getTable("demographic");
    ResultSet set2 = getTable("student");
        try {
            while (set.next()){
                if (studentSNN.equals(set.getString("snn")))
                {
                    inf_studentName = set.getString("first") +" "+ set.getString("mid") +" "+ set.getString("last");
                    
                }
            }
           
                 while (set2.next()){
                if (studentSNN.equals(set2.getString("snn")))
                {
                    inf_classyear = set2.getString("yearsof") ;
                }
                
            }
        } catch (SQLException e) {
        }
        finally // ensure result set, statement and connection are closed
        {
            try {  
                set2.close();
                set.close();
              
                
                
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
    
    
        }
    }
    
    VBox getCourses(int i){
        
            ResultSet set = getTable("courses");
    
        try {
            while (set.next()){
                
                if (addCourses[i].equals(set.getString("courseid")))
                {
                    inf_courseName = set.getString("courseid")+ " " + set.getString("name") + "\t Room: "+ set.getString("room")
                                     + "\nMeeting Time: " + set.getString("time");
                    
                }
                
            }
           
        } catch (SQLException e) {
        }
        finally // ensure result set, statement and connection are closed
        {
            try {
                set.close();

                
            }
            catch (Exception exception){
                exception.printStackTrace();
            }}
        
        VBox scheduleBox = new VBox();     
         Text  course= new Text("Course "+inf_courseName);
         course.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         course.setId("fancytext"); 
     scheduleBox.getChildren().add(course);
        scheduleBox.setSpacing(30);
        scheduleBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
        + "-fx-border-radius: 5;" + "-fx-border-color: black;" );
        scheduleBox.setAlignment(Pos.CENTER);
        return scheduleBox;
    
    }
    
    void classSchedule(){
              BorderPane registerPane = new BorderPane();
         Stage stage = new Stage();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         VBox courses = new VBox();
         
          registerPane.getStylesheets().add("Style.css");
         registerPane.setStyle("-fx-background-color: #888888;");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Class Schedule!");
            getStudentInfo();
         Text name = new Text(studentSNN+"\n" +  inf_studentName + "\n" + inf_classyear);
         name.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         name.setId("fancytext");
         
         
         for(int i =0; i < countOfCoursesAdd; i++){
             System.out.println("How many courses: " +countOfCoursesAdd);
         courses.getChildren().addAll(getCourses(i));
         }

       
        HBox top = new HBox(logoView, name);
   registerPane.setTop(top);
   registerPane.setCenter(courses);
           
           Scene scene2 = new Scene(registerPane);
            stage.setScene(scene2);
            stage.show();
        
        
    }
    
    void receivable() {
        String temp = "Courses ID\n";
        int count=0;
        float grand = 0.00f;
        float fee = 5.00f;
          BorderPane registerPane = new BorderPane();
         Stage stage = new Stage();
         Image loginLogo = new Image("hand.png");
         ImageView logoView = new ImageView(loginLogo);
         
         Text infSSN = new Text ("Social Security #: "+studentSNN);
         infSSN.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         infSSN.setId("fancytext");
         Text infName = new Text ("Student Name: "+studentN);
         infName.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         infName.setId("fancytext");
         
         // Courses 
         credit =0;
         non_credit=0;
         for (int i = 0; i < countOfCoursesAdd; i++){
             if(addCourseC[i] > 0){
                 if (count <= 3){
                 temp +=addCourses[i];
                     if (addCourseC[i] >=3  )
                    {
                     temp +=" $285.00\n";
                     grand += 285.00f;
                   
                     
                    }
                     else{
                     temp +=" $300.00\n";
                    
                  grand += 300.00f;
                    }
                 
                 count++;
                 }
                 else {
                     temp +=addCourses[i]  +  " $265.00\n" ;
                 grand += 265.00f;
                 }
             credit++;
             }
               
         }
         Text infCredit = new Text ("You have : " + credit + " Courses Added.\n" + temp);
         infCredit.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
         infCredit.setId("fancytext");
         
         
         String nonTemp = "Courses ID\n";   
                 for (int i = 0; i < countOfCoursesAdd; i++){
             if(addCourseC[i] == 0){
                     nonTemp +=addCourses[i]  +  " $150.00\n" ;
                     grand+= 150.00f;
                 
             non_credit++;
             }
             
         }
   Text infNon = new Text ("You Have " + non_credit + " NonCredit courses Added.\n" + nonTemp );
         infNon.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        infNon.setId("fancytext");
          Text infgran = new Text ("Your Gran Total Plus the $5.00 fee. is" + ((float)(grand+fee)) );
         infgran.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        infgran.setId("fancytext");
   
         registerPane.getStylesheets().add("Style.css");
         registerPane.setStyle("-fx-background-color: #888888;");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Receivables!");

                VBox courseBox = new VBox(infSSN,infName,infCredit,infNon,infgran);
        courseBox.setSpacing(30);
        courseBox.setAlignment(Pos.CENTER);
   registerPane.setTop(logoView);
   registerPane.setCenter(courseBox);
           
           Scene scene2 = new Scene(registerPane);
            stage.setScene(scene2);
            stage.show();
        
    }
        
    }

