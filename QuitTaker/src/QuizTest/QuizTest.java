/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package quiztest;
package QuizTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;
import javafx.application.Application;
import javafx.application.Platform;
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

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.lang.Thread;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



/**
 *
 * @author steam
 */
public class QuizTest extends Application {

    // JDBC driver Name And Database URL
    Circle c1;
    Circle c2;

    static final String title1 = "Java Certified Programmer";
    static final String title2 = "Java Certified Developer";
    static final String title3 = "Java Certified Architect";
    DBConnection connect = new DBConnection();
    static PreparedStatement ps = null;
    boolean isCorrect = false;
    Text message;
    String cStudent = "";
    int cID = 0;
    double quiz1 = -1.00;
    double quiz2 = -1.00;
    String ctitle = "None";
    Text minute, second;
    int min = 10, sec = 1;
    String mText, sText;
    public Question[] quest;
    Stage quizStage;
    boolean isSubmit = false;
    ToggleGroup group;
    RadioButton[] ans;
    int qArray[] = new int[20];
    double temp=0;
    Stage primaryStage = new Stage();
    public static int count = 0;
    final int questionLimit = 20;
    int currentMax=25,currentMin=0;
    boolean goodAns=false;
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage none) {
        // Minutes

        minute = new Text(mText);
        minute.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        minute.setId("fancytext");

        // Seconds
        second = new Text(sText);
        second.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        second.setId("fancytext");
        mText = "10";
        sText = "00";

        message = new Text("Main Menu");
        ImageView Banner = new ImageView(new Image("images/Banner.png"));
        ImageView Logo = new ImageView(new Image("images/logo.png"));
    // Login Area
        // Text Area
        Text userT = new Text("UserName: ");
        userT.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        userT.setId("fancytext");
        Text passT = new Text("Password:  ");
        passT.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        passT.setId("fancytext");

        // Field Area
        TextField userF = new TextField();
        PasswordField passF = new PasswordField();

       // Buttons
        Button loginB = new Button("Login");

        // Boxes Area
        HBox userHB = new HBox(userT, userF);
        userHB.setSpacing(10);
        userHB.setAlignment(Pos.CENTER);

        HBox passHB = new HBox(passT, passF);
        passHB.setSpacing(10);
        passHB.setAlignment(Pos.CENTER);

        VBox loginVB = new VBox(userHB, passHB, loginB, message);
        loginVB.setSpacing(10);
        loginVB.setAlignment(Pos.CENTER);

        // Last Pane
        BorderPane root = new BorderPane();
        HBox bannerB = new HBox(Banner);
        bannerB.setAlignment(Pos.CENTER);
        root.getStylesheets().add("Style/Style.css");
        root.setTop(bannerB);
        root.setCenter(loginVB);

        Scene scene = new Scene(root);

        // Button Action Event or Enter Key press
        passF.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login(userF.getText(), passF.getText(), primaryStage);
            }
        });
        loginB.setOnAction(e -> {
            login(userF.getText(), passF.getText(), primaryStage);
        });

        // Stage Declaration
        primaryStage.getIcons().add(new Image("images/logo.png"));
        primaryStage.setTitle("TE Admission Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
        quizStage = new Stage();
    }

    void login(String userF, String pass, Stage primaryStage) {

        if (checkData(userF, pass)) {
            if (isCorrect) {
                try {
                    updateScore();
                } catch (SQLException es) {
                    System.out.println();
                }
                qArray[0]=-1;
                mainArea(primaryStage,true);
            }
            message.setText("Wrong Password, please try Again");
            message.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
            message.setId("fancytext3");

        } else {
            System.out.println("Creating a new user.");
            try {
                createStudent(userF, pass, quiz1, quiz2,ctitle);
            } catch (SQLException es) {
            }
            qArray[0]=-1;
            mainArea(primaryStage,true);
        }
    }

    public void mainArea(Stage currentStage, boolean reset) { 
      

        BorderPane background = new BorderPane();
        background.getStylesheets().add("Style/Style.css");

        ImageView Banner = new ImageView(new Image("images/Banner.png"));
        ImageView profile = new ImageView(new Image("images/profile.png"));

        Text userText = new Text(cStudent);
        userText.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        userText.setId("fancytext");
        Text title = new Text("Title: "+ctitle);
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        title.setId("fancytext");

        VBox infB = new VBox();
        infB.setSpacing(10);
        infB.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: black;");
        infB.getChildren().addAll(title, profile, userText);

        HBox top = new HBox();
        top.getChildren().addAll(infB, Banner);
        top.setSpacing(130);
        
        c1 = new Circle(75);
        c2 = new Circle(75);
        Text firstQ = new Text("Quiz #1");
        firstQ.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        firstQ.setId("fancytext");
        Text secondQ = new Text("Quiz #2");
        secondQ.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        secondQ.setId("fancytext");

        Text firstG;
        Text secondG;
        StackPane circle1 = new StackPane();
        StackPane circle2 = new StackPane();

        if (quiz1 >= 0) {
            firstG = new Text(quiz1 + "%");
            if (quiz1 >= 65) {
                c1.setStroke(Color.GREEN);
                c1.setStrokeWidth(10);
                c1.setFill(Color.AZURE);
                firstG.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
                firstG.setId("fancytext4");
            } else {
                c1.setStroke(Color.RED);
                c1.setStrokeWidth(10);
                c1.setFill(Color.AZURE);
                firstG.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
                firstG.setId("fancytext3");
            }

        } else {
            c1.setStroke(Color.TRANSPARENT);
            c1.setFill(Color.TRANSPARENT);
            firstG = new Text("None Score");
            firstG.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
            firstG.setId("fancytext");
        }
        if (quiz2 >= 0) {
            secondG = new Text(quiz2 + "%");
            if (quiz2 >= 65) {
                c2.setStroke(Color.GREEN);
                c2.setStrokeWidth(10);
                c2.setFill(Color.AZURE);
                secondG.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
                secondG.setId("fancytext4");
            } else {
                c2.setStroke(Color.RED);
                c2.setStrokeWidth(10);
                c2.setFill(Color.AZURE);
                secondG.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
                secondG.setId("fancytext3");
            }

        } else {
            c2.setStroke(Color.TRANSPARENT);
            c2.setFill(Color.TRANSPARENT);
            secondG = new Text("None Score");
            secondG.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
            secondG.setId("fancytext");
        }

        circle1.getChildren().addAll(c1, firstG);
        circle2.getChildren().addAll(c2, secondG);
        HBox first = new HBox(firstQ, circle1);
        first.setSpacing(25);
        first.setAlignment(Pos.CENTER);
        HBox second = new HBox(secondQ, circle2);
        second.setSpacing(25);
        second.setAlignment(Pos.CENTER);

        Button TakeQ = new Button("Take Quiz");
        TakeQ.getStyleClass().add("button1");

        TakeQ.setOnAction(e -> {
            
            if (quiz1 < 0||quiz2 < 0){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Shit");
            stage.setMinWidth(400);
            VBox vBox = new VBox(10);
            HBox hBox = new HBox(20);
            Button ok = new Button("ok");
            Text mess = new Text("Note: You Only Have 10:00 Minutes\n" + 
                                 "Complete 20 Questions. Good luck.!");
                        mess.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
            mess.setId("fancytext");
            
            ok.setOnAction(ae -> { quiz(primaryStage); stage.close();});
           
            hBox.setAlignment(Pos.CENTER);
            vBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(new Text("\t"),ok);
            vBox.getChildren().addAll(new ImageView(new Image("images/logo.png")),mess,hBox);
             vBox.setStyle("-fx-background-color: #888888;");
            vBox.setStyle("Style/Style.css");
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();
            }
        });
        HBox button = new HBox();
        button.setAlignment(Pos.CENTER);
        VBox center = new VBox();
        center.setSpacing(25);
       if(!(reset)){

           button.getChildren().add(new Text ("Come back the next day."));}
       else 
       {
                      background.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                         Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Shit");
            stage.setMinWidth(400);
            VBox vBox = new VBox(10);
            HBox hBox = new HBox(20);
            Button ok = new Button("ok");
            Text mess = new Text("Note: You Only Have 10:00 Minutes\n" + 
                                 "Complete 20 Questions. Good luck.!");
                        mess.setFont(Font.font("Tahoma", FontWeight.BOLD, 35));
            mess.setId("fancytext");
            
            ok.setOnAction(ae -> { quiz(primaryStage); stage.close();});
           
            hBox.setAlignment(Pos.CENTER);
            vBox.setAlignment(Pos.CENTER);
            hBox.getChildren().addAll(new Text("\t"),ok);
            vBox.getChildren().addAll(new ImageView(new Image("images/logo.png")),mess,hBox);
             vBox.setStyle("-fx-background-color: #888888;");
            vBox.setStyle("Style/Style.css");
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.show();
                
            }
        });
           button.getChildren().add(TakeQ);
       
       
       }
           
       
       center.getChildren().addAll(first, second,button);
           
      // center.setAlignment(Pos.CENTER);

        background.setTop(top);
        background.setCenter(center);

        Scene scene = new Scene(background, 900, 900);
        currentStage.setTitle("Student Area");
        currentStage.setScene(scene);
        currentStage.show();

    }

    void setTitle(){
        if (quiz1>=65 && quiz1 <=74.9 || quiz2>=65 && quiz2 <=74.9 )
        {
        ctitle = title1;
        }
        else if (quiz1>=75 && quiz1 <=84.9 || quiz2>=65 && quiz2 <=84.9 )
        {
        ctitle = title2;
        }
        else if (quiz1>=85|| quiz2>=85 )
        {
        ctitle = title3;
        }
    }
    
    public void updateScore() throws SQLException {
        ps = null;

       // try
        // {
        Connection connection = connect.getConnection();
        String query = "UPDATE student set quiz1 = " + quiz1 + " , quiz2 = " + quiz2 + " where studentID = " + cID;
       // try {

        ps = connection.prepareStatement(query);
        ps.execute();
        ps.close();

    }
    
    public void loadQ() {
        quest = new Question[100];
        int j = 0;
        try {
            Scanner input = new Scanner(new File("question.txt"));
            while (input.hasNext()) {
                quest[j] = new Question();
                quest[j].setQuestion(input.nextLine());
                for (int i = 0; i < 5; i++) {
                    quest[j].setAns(input.nextLine(), i);
                }
                j++;
            }

           // return false;
        } catch (FileNotFoundException ex) {
            System.out.println("didnt open");
            //Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void quiz(Stage mainStage) {
        thread.start();
        loadQ();
        
        Button submit = new Button("Submit");
        Button next = new Button("Next");
        HBox buttons = new HBox(submit, next);
        buttons.setSpacing(30);
        buttons.setAlignment(Pos.CENTER);
        quizStage.setTitle("Do you best");
        quizStage.initModality(Modality.APPLICATION_MODAL);
        quizStage.setMinWidth(400);


        HBox top = new HBox(new ImageView(new Image("images/logo.png")));
        top.setSpacing(1300);
          
        VBox center = centerB();
        center.setSpacing(75);

        VBox bottom = new VBox(buttons, new Text("\t"));

        VBox right = new VBox();
        right.getChildren().addAll(timer());
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);
        root.setRight(timer());

        Scene scene = new Scene(root, 1300, 900);
        quizStage.setFullScreen(true);
        quizStage.setScene(scene);
        quizStage.show();
        
        
      
        next.setOnAction(e-> {
        if (getAnswer(group.getSelectedToggle().getUserData().toString())){
        temp +=1;
        goodAns = true;
        }
        else {
            temp+=0;goodAns= false;}
        root.setCenter(centerB());
        count++;
        
        if (count == 19){
            
            HBox button = new HBox();
        button.getChildren().add(submit);
        button.setAlignment(Pos.CENTER);
        root.setBottom(button);}
        });

        submit.setOnAction(e -> {
            submitQuit();
            
        });

    }
    
    boolean getAnswer(String cAns) {
        for (int i= 0; i <= count; i++){
            if (cAns.equals(quest[qArray[i]].getAns(0)))
           return true;

        }
        return false;
    }
    
    int generatedRandom(boolean Correct){
        Random rand = new Random();
        int v;
        if (qArray[0] == -1)
        {
            v =rand.nextInt(26);
            return v;
            
        }
        v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
       if (Correct){   
           if (currentMax < 100){ 
           currentMin = currentMax;
           currentMax +=25;
            v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
               for (int i = 0 ; i <= count; i++){
                  if (qArray[i]==v){
                      v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
                      i=0;
                  }
               }
               return v;
            
           }
           else
           {
                for (int i = 0 ; i < count; i++){
                  if (qArray[i]==v){
                      v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
                      i=0;
                  }
               
                }
                
           }
       }
       else 
       { 
           if(currentMin > 0){ 
               currentMax = currentMin;
               currentMin -=25;
                v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
               for (int i = 0 ; i <= count; i++){
                  if (qArray[i]==v){
                      v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
                      i=0;
                  }
               }

       
              }
           else
           {
                 for (int i = 0 ; i < count; i++){
                  if (qArray[i]==v){
                      v =rand.nextInt((currentMax - currentMin) + 1) + currentMin;
                      i=0;
                  }
               
                }
           }
       }

            
        return v;
    }
    int getCount() {
        return count;
    }
    
    VBox centerB(){
 
        if (qArray[0] == 0)
            qArray[0]=generatedRandom(false);
        else{
            qArray[count]=generatedRandom(goodAns);
                    
                    
                    }

        int[] rP = new int[]{-1,-1,-1,-1,-1};
        ans = new RadioButton[5];
        HBox Dialog = new HBox();
        Text qt = new Text(quest[qArray[count]].getQuestion());
        qt.setFont(Font.font("Tahoma", FontWeight.BOLD, 19));
        qt.setId("fancytext");
        Dialog.getChildren().add(qt);
        Dialog.setAlignment(Pos.CENTER);
        int x=-1;

        for (int i = 0; i < ans.length; i++){
            while (rP[0]==x || rP[1]==x || rP[2]==x || rP[3]==x || rP[4]==x ){
            x =(int) (Math.random()*5);
            }
            rP[i]=x;
           
        }
        
          group = new ToggleGroup();
        for (int i = 0; i < ans.length; i++) {
            ans[i] = new RadioButton();
            ans[i].setText(quest[qArray[count]].getAns(rP[i]));
            ans[i].setToggleGroup(group);
            ans[i].setUserData(quest[qArray[count]].getAns(rP[i]));
        }
              
        HBox firstR = new HBox();
        for (int n=0; n < 2 ; n++){
            if (!(ans[n].getText().equals("."))){
        firstR.getChildren().addAll(ans[n]);}}
        firstR.setSpacing(10);
        firstR.setAlignment(Pos.CENTER);
        HBox secondR = new HBox();
          for (int k=2; k < 5 ; k++){
            if (!(ans[k].getText().equals("."))){
        secondR.getChildren().addAll(ans[k]);}}
        secondR.setSpacing(10);
        secondR.setAlignment(Pos.CENTER);
        VBox answesB = new VBox(Dialog, firstR, secondR);
        answesB.setAlignment(Pos.CENTER);
        
        return new VBox(Dialog,answesB);
    }

    VBox timer() {

        VBox frameM = new VBox();
        VBox frameS = new VBox();
        frameM.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 3;"
                + "-fx-border-radius: 3;" + "-fx-border-color: transparent;"
                + "-fx-background-width: 2;" + "-fx-background-insets: 3;" + "-fx-background-radius: 5;" + "-fx-background-color: gainsboro;");
        frameS.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 3;"
                + "-fx-border-radius: 3;" + "-fx-border-color: transparent;"
                + "-fx-background-width: 2;" + "-fx-background-insets: 3;" + "-fx-background-radius: 5;" + "-fx-background-color: gainsboro;");
        frameM.getChildren().add(minute);
        frameS.getChildren().add(second);
        HBox clock = new HBox(frameM, frameS);
        clock.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;"
                + "-fx-background-width: 2;" + "-fx-background-insets: 5;" + "-fx-background-radius: 5;" + "-fx-background-color: gray;");
        clock.setAlignment(Pos.CENTER);

        return new VBox(clock);

    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            while (min != -1) {

                if (min > 9) {
                    mText = Integer.toString(min);
                } else {
                    mText = "0" + Integer.toString(min);
                }
                if (sec > 9) {
                    sText = "" + Integer.toString(sec);
                } else {
                    sText = "0" + Integer.toString(sec);
                }

                Platform.runLater(() -> {
                    minute.setText(mText);
                    second.setText(sText);
                    if(min == 0 && sec == 0)submitQuit();
                } // Run from JavaFX GUI
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(QuizTest.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (sec == 0) {
                    min--;
                    sec = 59;
                } else {
                    sec--;
                }
            }
            try{
              thread.stop();
            }catch (ThreadDeath es) {thread.stop();}
        }

    });
    
  double getMath(){
        double wrong=0;
        double limit = 20.00;
        double wAns = limit - temp;
        if (temp > 0){
            wrong= ((1.00- (temp/limit)) * wAns + wAns ) ;
            return temp*5 - wrong;
        }
       
        
        
        return wrong;
    }
    
    void submitQuit() {
        min=10;
        sec=00;
        if (quiz1 < 0)
        quiz1=getMath();
        else 
            quiz2=getMath();
         
 
        try{
            updateScore();
            setTitle();
        }
        catch (SQLException e) {System.out.checkError();}
         mainArea(primaryStage,false);
                    try{
              thread.stop();
            }catch (ThreadDeath es) {thread.stop();}
                   try{
              thread.stop();
            }catch (ThreadDeath es) {thread.stop();}
        
        quizStage.close();
        
    }

    public boolean checkData(String userName, String password) {

        try {
            ResultSet set = connect.getTable();

            while (set.next()) {
                if (userName.equals(set.getString("userN"))) {
                    cStudent = set.getString("userN");
                    if (password.equals(set.getString("pass"))) {

                        cID = set.getInt("studentID");
                        quiz1 = set.getDouble("quiz1");
                        quiz2 = set.getDouble("quiz2");
                        setTitle();
                        isCorrect = true;
                        return true;
                    }
                    return true;
                }
            }

        } catch (SQLException exp) {
            System.out.println();
        }

        return false;

    }

    public void createStudent(String userName, String password, double quiz1, double quiz2, String title) throws SQLException {
        ps = null;

        
            Connection connection = connect.getConnection();

            String query = "INSERT INTO student (userN, pass,title, quiz1, quiz2) VALUES (?,?,?,?,?)";
            try {

                ps = connection.prepareStatement(query);

                ps.setString(1, userName);
                ps.setString(2, password);
                ps.setString(3, title);
                ps.setDouble(4, quiz1);
                ps.setDouble(5, quiz2);
                cStudent = userName;

            } catch (SQLException sqle) {
                sqle.printStackTrace();
        } finally {
            ps.execute();
            ps.close();
            
        }

    }

}
