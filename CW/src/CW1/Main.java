package CW1;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.*;

public class Main extends Application{

    static final int SEATING_CAPACITY = 42; // initializing seating capacity as a static final value to be used throughout the program
    // SEATING_CAPACITY + 1  :  will be used as we need seats to start from 1 and not 0
    Stage window; // making it stage window
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage; // naming the primary stage as window to be used within the program
        // Initializing all arrays to be use in the program
        String[] seatArray = new String[SEATING_CAPACITY+1];
        String[] customerArray = new String[SEATING_CAPACITY+1];
        // setting initial values to all the arrays
        for (int k = 1; k < (SEATING_CAPACITY + 1); k++) {
            seatArray[k] = "not_booked"; // this sets the initial value of all the seats to not booked
        }
        for (int k = 1; k < (SEATING_CAPACITY + 1); k++) {
            customerArray[k] = null; // sets initial value of all names to a null value
        }
        boolean returnJourney = false;
        Scanner input = new Scanner(System.in);
        System.out.println("*******************************************************************************************************");
        System.out.println("**********************************   Denuwara Menike Train  *******************************************");
        System.out.println("**********************************   SEAT BOOKING PROGRAM   *******************************************");
        System.out.println("*******************************************************************************************************");
        System.out.println("Enter '1' for Colombo to Badulla Train Menu system");
        System.out.println("Enter '2' for Badulla to Colombo Train Menu system");
        System.out.print("Enter Option :");
        String journey = input.nextLine();  //creating journey variable to get the users input
        if(journey.equals("2")){ //checks if the user has entered "y" or "Y"
            System.out.println("You have selected Badulla to Colombo menu system");
            returnJourney = true;  //this assigns the returnJourney variable to true
            journey = "from Badulla to Colombo";  //assigning the journey to a variable
        }
        else if(journey.equals("1")){
            System.out.println("You have selected Colombo to Badulla menu system");
            journey = "from Colombo to Badulla";  //assigning the journey to a variable
        }
        else{
            System.out.println("Invalid input! Colombo to Badulla menu system has been set by default");
        }
        menu(seatArray,customerArray,returnJourney); // calling menu to start program
    }

    //Menu on console :
    private void menu(String[] seatArray, String[] customerArray, boolean returnJourney) {
        Scanner sc = new Scanner(System.in);
        System.out.println("|============================================================|");
        System.out.println("|-- --- -- Welcome to the Train Seat Booking System -- ------|");
        System.out.println("|--- ------- ------  Denuwara Menike Train  ------ ------ ---|");
        if (returnJourney==false) {
            System.out.println("|------ ----- Colombo to Badulla | AC Compartment --- -------|");
        } else{
            System.out.println("|------ ----- Badulla to Colombo | AC Compartment --- -------|");
        }
        System.out.println("|============================================================|");
        System.out.println("--> Enter 'A' to Add a Customer");
        System.out.println("--> Enter 'V' to View the seats");
        System.out.println("--> Enter 'E' to View Empty seats");
        System.out.println("--> Enter 'D' to Delete booked seat");
        System.out.println("--> Enter 'F' to Find seat by customer name");
        System.out.println("--> Enter 'S' to Save current data to file");
        System.out.println("--> Enter 'L' to Load data from file ");
        System.out.println("--> Enter 'O' to View Seats sorted according to Alphabetical order of Customer Name");
        System.out.println("--> Enter 'Q' to Quit");
        System.out.print("Enter your Option : ");
        String option = sc.next();
        option = option.toUpperCase();
        switch (option) {
            case ("A"):
                System.out.println("Add Customer to seat executing...\n");
                addCustomer(seatArray,customerArray,returnJourney);
                break;
            case ("V"):
                System.out.println("View all seats executing...\n");
                viewAllSeats(seatArray,customerArray,returnJourney);
                break;
            case ("E"):
                System.out.println("View Empty seats  executing...\n");
                viewEmptySeats(seatArray,customerArray,returnJourney);
                break;
            case ("D"):
                System.out.println("Delete a booked seat executing...\n");
                deleteBookedSeat(seatArray,customerArray,returnJourney);
                break;
            case ("F"):
                System.out.println("Find seat by customer name executing...\n");
                findSeatByCustomer(seatArray,customerArray,returnJourney);
                break;
            case ("S"):
                System.out.println("Saving Data to file executing...\n");
                saveToFile(seatArray,customerArray,returnJourney);
                break;
            case ("L"):
                System.out.println("Loading data from file executing...\n");
                loadFromFile(seatArray,customerArray,returnJourney);
                break;
            case ("O"):
                System.out.println("Sorting seats executing...\n");
                sortBooking(seatArray,customerArray,returnJourney);
                break;
            case ("Q"):
                System.exit(0);
            default:
                System.out.println("\n Invalid input! please re-enter selection \n ");
                menu(seatArray, customerArray,returnJourney);
        } // End of Switch case
    } // End of menu

    // when A is pressed:
    private String[] addCustomer( String[] seatArray, String[] customerArray, boolean returnJourney) {
        Label header1;
        if (!returnJourney) {
            header1 = new Label("Denuwara Menike Train - Colombo to Badulla - AC Compartment ");
        } else{
            header1 = new Label("Denuwara Menike Train - Badulla to Colombo - AC Compartment "); //header label
        }
        header1.setLayoutX(40); // position of label
        header1.setStyle("-fx-font-size:19px;"); // size of label
        Label header2 = new Label("Book a Seat"); // header label
        header2.setLayoutX(40);// position of label
        header2.setLayoutY(30);
        header2.setStyle("-fx-font-size:18px;");// size of label
        Button details = new Button("Help"); // Help button if user does not understand how to add a customer
        details.setOnAction(event->{ //lambda event handler
            Alert a1 = new Alert(Alert.AlertType.INFORMATION); // alert box
            a1.setTitle("Help"); //alert title
            a1.setHeaderText("How to book a seat"); // alert header
            a1.setContentText("-> Select an booked seat \n-> Enter customer name for seats chosen\n-> Click finish "); //alert context
            a1.show(); //to show alert box
        });
        details.setLayoutY(130); // position of help button
        details.setLayoutX(320);
        details.setStyle("-fx-font-size:12px;"); // size of text in help button
        Label greenLabel = new Label("Green seat : Seat is available "); //info label
        Label redLabel = new Label("Red seat : Seat is not available ");//info label
        greenLabel.setStyle("-fx-font-size:18px;");//styling labels
        redLabel.setStyle("-fx-font-size:18px;");
        greenLabel.setLayoutX(320);//label positions
        greenLabel.setLayoutY(170);
        redLabel.setLayoutX(320);
        redLabel.setLayoutY(200);
        Label INFO = new Label("Note: Seats cannot be unselected, if you have selected a seat \nby accident, Please click cancel and try again."); //info label
        INFO.setStyle("-fx-font-size:10px; -fx-color:#db2f2c"); // styling label
        INFO.setLayoutX(320); // label position
        INFO.setLayoutY(420);
        Label nameLabel = new Label("Enter Customer Name : "); // label
        nameLabel.setStyle("-fx-font-size:20px;"); // styling label
        nameLabel.setLayoutX(320);//label positions
        nameLabel.setLayoutY(250);
        // Array to store Buttons
        Button[] btn = new Button[SEATING_CAPACITY+1];
        Button finish = new Button("Book");
        finish.setLayoutX(320);
        finish.setLayoutY(390);
        Button cancel = new Button("Cancel");
        cancel.setLayoutX(370);
        cancel.setLayoutY(390);
        TextField user_Name = new TextField(); // Text Field to input customer name
        user_Name.setLayoutX(320);
        user_Name.setLayoutY(290);
        GridPane gridPane = new GridPane(); // creating gridPane to align buttons
        // initializing variables
        int columnIndex = 0;
        int rowIndex = 0;
        int rowLoop = 0;
        String seatNumber;
        int[] position = new int[SEATING_CAPACITY + 1]; //storing the button values booked temporally
        for(int indexValue = 1; indexValue<(SEATING_CAPACITY+1); indexValue++){
//          Generating Seat Numbers
            if (indexValue<=9){
                seatNumber = "0"+(indexValue);
            }
            else{
                seatNumber = ""+(indexValue);
            }
            btn[indexValue] = new Button(seatNumber); // assigning seatNumber to button
            btn[indexValue].setPadding(new Insets(12)); // Padding inside the button around the seat number
            gridPane.add(btn[indexValue],columnIndex,rowIndex);
            columnIndex++; // incrementing value
            if (columnIndex == 2) { // creates a space between the first two columns and 2nd two columns
                columnIndex += 1;
            }
            rowLoop++; // incrementing value
            if(rowLoop==4){ // if row = 4 , it stops creating more rows and columns
                columnIndex=0;
                rowLoop=0;
                rowIndex++; // incrementing value
            }
            int seatValue = indexValue;
            // when a seat is clicked
            btn[indexValue].setOnAction(event -> { // event handler
                //Checking whether seats are booked or not and assigning color when button click
                for(int seatColor=1; seatColor<(SEATING_CAPACITY+1);seatColor++) {
                    if (seatColor == seatValue) {
                        seatArray[seatColor] = "booked";
                        btn[seatColor].setStyle("-fx-background-color:#db2f2c; -fx-border-width:1 1 1 1; -fx-border-color:black;");
                        position[seatColor] = seatValue;
                    }
                    if (seatArray[seatColor].equals("booked")){ // checks whether the seat is booked and assigns the red color to the seat
                        btn[seatColor].setStyle("-fx-background-color:#db2f2c; -fx-border-width:1 1 1 1; -fx-border-color:black;");
                    }
                    if (seatColor!=seatValue && seatArray[seatColor].equals("not_booked")){ // checks whether the seat is booked and assigns the green color to the seat
                        btn[seatColor].setStyle("-fx-background-color:#84f51b; -fx-border-width:1 1 1 1; -fx-border-color:black;");
                    }
                }
            });
        }
        //Sets colour of button based on whether its booked or not booked
        for(int n = 1; n<(SEATING_CAPACITY+1); n++){
            if (seatArray[n].equals("not_booked")){
                btn[n].setStyle("-fx-background-color:#84f51b; -fx-border-width:1 1 1 1; -fx-border-color:black;"); //makes button green
            }
            if (seatArray[n].equals("booked")){ //
                btn[n].setStyle("-fx-background-color:#db2f2c; -fx-border-width:1 1 1 1; -fx-border-color:black;"); // makes button red
                btn[n].setDisable(true); // disabling already booked buttons so it cannot be clicked again
            }
        }
        // when finish button is clicked
        finish.setOnAction(event -> {
            Alert informationBox = new Alert(Alert.AlertType.INFORMATION); //creating a information box
            String customerName = user_Name.getText();
            customerName = customerName.toLowerCase();
            if (!customerName.equals("")) {
                if (!customerName.matches("[a-z A-Z]+")) { //loops when the customer name is not in alphabetical order
                    informationBox.setHeaderText("You have entered an Invalid character! \nCustomer name cannot have numerical or special characters \nPlease re-enter name in console"); //texts inside the information box
                    informationBox.setTitle("Invalid Character"); //setting a title to the information box
                    informationBox.showAndWait(); //displays the alert box
                    window.close();
                    Scanner sc = new Scanner(System.in);
                    System.out.println("As you have entered invalid character,do not enter any numerical or special characters ");
                    System.out.println("Re-enter customer name :"); //getting user name from console as invalid character entered
                    customerName=sc.nextLine();
                }
            }
            String finalNameValue = "";  // initializing finalname as a string
            boolean spaces = false;  // declaring spaces as a boolean value set to false
            // validation loop to remove blank spaces(if any) from the start of the name before adding it into the array
            for (int indexValue = 0; indexValue < customerName.length(); indexValue++) {
                char character = customerName.charAt(indexValue); //getting index characters from customerName
                if ((int) character != 32) { //32 is the integer ASCII value for space input  - so its checking whether the starting characters is a space value
                    spaces = true; //declaring a variable to true
                }
                if (spaces) {
                    finalNameValue+=character; //adds the characters of the customer name one by one to the variable FinalCustomerName
                }
            }
            for(int loopValue = 1; loopValue<(SEATING_CAPACITY+1); loopValue++){
                if (loopValue == position[loopValue]){
                    seatArray[loopValue] = "booked"; // turning selected seats into booked seats
                    customerArray[loopValue] = finalNameValue; //adding finalNameValue to customerArray
                }

            }
            window.close();//closing window
            System.out.println("Seat(s) has been booked successfully! \n");
            menu(seatArray, customerArray,returnJourney);//calling menu
        });
        // when Cancel button is clicked
        cancel.setOnAction(event -> { // event handler
            Alert confirmBox = new Alert(Alert.AlertType.CONFIRMATION); //creating the confirmation box
            confirmBox.setTitle("Train Seat Booking System"); //setting title for the confirmation box
            confirmBox.setHeaderText("Are you sure you want to Cancel ?"); //setting the header of the confirm box
            ButtonType yes = new ButtonType("Yes");  //creating the button YES
            ButtonType no = new ButtonType("No"); //creating the button No
            confirmBox.getButtonTypes().setAll(yes, no);
            Optional<ButtonType> result = confirmBox.showAndWait(); //displays the confirmation box
            if (result.get() == yes) { //checks if the user has clicked YES
                window.close();//closing window
                menu(seatArray,customerArray,returnJourney); //calling menu
            }
        });
        // positioning button grid pane
        gridPane.setLayoutY(70);
        gridPane.setLayoutX(40);
        gridPane.setHgap(25);
        gridPane.setVgap(5);
        AnchorPane anchorPane = new AnchorPane(); //creating anchor pane
        anchorPane.setStyle("-fx-background-color:#a9d9d9"); //background color
        anchorPane.getChildren().addAll(gridPane,nameLabel,cancel,user_Name,details,header1,header2,finish,greenLabel,redLabel,INFO); // adding items to be displayed in window
        Scene scene = new Scene(anchorPane,600,600);
        window.setTitle("Train Seat Booking System");
        window.setScene(scene);
        window.setResizable(false); // making sure window cannot be resized
        window.show(); // displaying window
        return seatArray;
    } // End of Add customer method

    // when V is pressed :
    private void viewAllSeats(String[] seatArray, String[] customerArray, boolean returnJourney) {

        Button[] btn = new Button[SEATING_CAPACITY+1]; //Array for buttons
        Button cancel = new Button("Done");
        cancel.setLayoutX(320);
        cancel.setLayoutY(250);
        Label greenLabel = new Label("Green seat : Seat is available ");//label
        Label redLabel = new Label("Red seat : Seat is not available ");//label
        greenLabel.setLayoutX(320);//label position
        greenLabel.setLayoutY(170);
        redLabel.setLayoutX(320);//label position
        redLabel.setLayoutY(200);
        greenLabel.setStyle("-fx-font-size:18px;");//label style
        redLabel.setStyle("-fx-font-size:18px;");//label style
        Label header3 = new Label("Denuwara Menike Train - Colombo to Badulla - AC Compartment "); //header
        header3.setLayoutX(40);//header position
        header3.setStyle("-fx-font-size:19px;");// header style
        Label header4 = new Label("View all Seats");//header2
        header4.setLayoutX(40);//header2 position
        header4.setLayoutY(30);
        header4.setStyle("-fx-font-size:18px;");//header2 style
        GridPane gridPane = new GridPane();
        int columnIndex = 0;
        int rowIndex = 0;
        int rowLoop = 0;
        String seatNumber; //initializing seat number
        //Generating Seat Numbers
        for(int indexValue = 1; indexValue<(SEATING_CAPACITY+1); indexValue++){
            // indexValue is the seat number
            if (indexValue<=9){
                seatNumber = "0"+(indexValue);
            }
            else{
                seatNumber = ""+(indexValue);
            }
            btn[indexValue] = new Button(seatNumber);
            btn[indexValue].setPadding(new Insets(10)); // Padding inside the button around the seat number
            gridPane.add(btn[indexValue],columnIndex,rowIndex); //adding button with values to gridPane
            columnIndex++; // incrementing value
            if (columnIndex == 2) { // creates a space between the first two columns and 2nd two columns
                columnIndex += 1;
            }
            rowLoop++; // incrementing value
            if(rowLoop==4){ // if row = 4 , it stops creating more rows and columns
                columnIndex=0;
                rowLoop=0;
                rowIndex++;
            }
        }
        //Sets default colour of button based on whether its booked or not
        for(int n = 1; n<(SEATING_CAPACITY+1); n++){
            if (seatArray[n].equals("not_booked")){ // if seat is not booked sets color to red
                btn[n].setStyle("-fx-background-color:#84f51b; -fx-border-width:1 1 1 1; -fx-border-color:black;");
            }
            if (seatArray[n].equals("booked")){ // if seat is booked sets color to green
                btn[n].setStyle("-fx-background-color:#db2f2c; -fx-border-width:1 1 1 1; -fx-border-color:black;");
            }
        }
        //when close button is clicked
        cancel.setOnAction(event -> {
            window.close(); // closing window
            menu(seatArray, customerArray,returnJourney); //calling menu
        });
        //button grid positioning
        gridPane.setLayoutY(70);
        gridPane.setLayoutX(40);
        gridPane.setHgap(25);
        gridPane.setVgap(5);
        AnchorPane anchorPane = new AnchorPane(); // creating anchorpane
        anchorPane.setStyle("-fx-background-color:#a9d9d9;"); // background color
        anchorPane.getChildren().addAll(gridPane,cancel,header3,header4,greenLabel,redLabel); // adding values to be displayed in window
        Scene scene2 = new Scene(anchorPane,600,600);
        window.setTitle("Train Seat Booking System"); // window title
        window.setScene(scene2);
        window.setResizable(false); // makes sure the window cannot be resized
        window.show();// display window
    } // End of View All Seats

    // when E is pressed :
    private void viewEmptySeats(String[] seatArray, String[] customerArray, boolean returnJourney) {
        Button[] btn = new Button[SEATING_CAPACITY+1]; // array for buttons
        Button cancel = new Button("Close"); // cancel button
        cancel.setLayoutX(320);// cancel position
        cancel.setLayoutY(250);
        Label greenLabel = new Label("Green seat : Seat is available "); // info label
        greenLabel.setLayoutX(320);//label position
        greenLabel.setLayoutY(170);
        greenLabel.setStyle("-fx-font-size:18px;");//label style
        Label header3 = new Label("Denuwara Menike train to Badulla - AC Compartment "); // headerlabel
        header3.setLayoutX(40); // label position
        header3.setStyle("-fx-font-size:20px;");//label style
        Label header4 = new Label("View all Empty Seats"); //header label
        header4.setLayoutX(40);//label position
        header4.setLayoutY(30);
        header4.setStyle("-fx-font-size:18px;");//label style
        //positioning gridPane
        GridPane gridPane = new GridPane();
        int columnIndex = 0;
        int rowIndex = 0;
        int rowLoop = 0;
        String seatNumber; //initializing seatnumber
        // generating the seat numbers
        for(int indexValue = 1; indexValue<(SEATING_CAPACITY+1); indexValue++){
            // assigning seat numbers
            if (indexValue<=9){
                seatNumber = "0"+(indexValue); // since single value, 0 is added in the beginning
            }
            else{
                seatNumber = ""+(indexValue);
            }
            btn[indexValue] = new Button(seatNumber);
            btn[indexValue].setPadding(new Insets(10)); // Padding inside the button
            gridPane.add(btn[indexValue],columnIndex,rowIndex); // adding buttons with values to gridPane
            columnIndex++;
            if (columnIndex == 2) { // creates a space between the first two columns and 2nd two columns
                columnIndex += 1;
            }
            rowLoop++; // incrementing value
            if(rowLoop==4){ // if row = 4 , it stops creating more rows and columns
                columnIndex=0;
                rowLoop=0;
                rowIndex++;
            }
        }
        for(int value = 1; value<(SEATING_CAPACITY+1); value++){
            if (seatArray[value].equals("booked")){ // if seats are booked do following code
                btn[value].setVisible(false); // if seats are booked it makes it invisible
            }
            else{
                btn[value].setStyle("-fx-background-color:#5ceb1a; -fx-border-width:1 1 1 1; -fx-border-color:black;"); //sets style to button
            }
        }
        // cancel button
        cancel.setOnAction(event -> { //lambda event handler
            window.close(); //closes window
            menu(seatArray, customerArray,returnJourney); // calls menu
        });
        //button grid positioning
        gridPane.setLayoutY(70);
        gridPane.setLayoutX(40);
        gridPane.setHgap(25);
        gridPane.setVgap(5);
        AnchorPane anchorPane = new AnchorPane(); // creating anchorPane
        anchorPane.setStyle("-fx-background-color:#a9d9d9;"); // adding background color
        anchorPane.getChildren().addAll(gridPane,cancel,header3,header4,greenLabel); // adding items to the window anchor pane
        Scene scene3 = new Scene(anchorPane,600,600); // creating scene and putting size
        window.setTitle("Train Seat Booking System"); // window title
        window.setScene(scene3); // calling scene
        window.setResizable(false); // makes sure window is not resizable
        window.show(); // calling window to show
    } // End of View all Empty Seats

    // when D is pressed:
    private void deleteBookedSeat(String[] seatArray, String[] customerArray, boolean returnJourney) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter customer name to delete seat: "); //asking user question
        String name = input.nextLine(); // getting user input and storing value
        String customer_not_found = ("there isn't any seat assigned to " + name + ", therefore no seats have been deleted. "); // if seat with name not found
        boolean found = false;//setting initial value to false
        for(int n=1; n<(SEATING_CAPACITY+1); n++){
            if (name.toLowerCase().equals(customerArray[n])){
                customerArray[n] = ""; // removes name from array
                seatArray[n] = "not_booked"; // makes seat not booked
                System.out.println(name + "'s seat has been successfully deleted (unbooked) \n"); // output statement
                found=true; //as seat was found therefore it is true
            }
        }
        if (!found){
            System.out.println(customer_not_found); // if there is no seat with name given
        }
        menu(seatArray, customerArray,returnJourney); // calling menu
    } // End of Delete Booked Seat Seats

    //when F is pressed :
    private void findSeatByCustomer(String[] seatArray, String[] customerArray, boolean returnJourney) {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter Customer Name: "); // asking question
        String name = input.nextLine();//taking userinput and storing answer
        String customer_not_booked = " Sorry, there isn't any seat booked with the name given \n "; // if seat not found this statement is printed
        boolean found = false; // initializing value to false
        for(int n=1; n<(SEATING_CAPACITY+1); n++){
            if (name.toLowerCase().equals(customerArray[n])){ //checks whether name == name in customerArray
                System.out.println("Seat number of " + name +" is "+ n); // statement to be printed if name found
                found=true; // found is set to true
            }
        }
        if (!found){ // if name was not found
            System.out.println(customer_not_booked);// not found statement is printed
        }
        menu(seatArray, customerArray,returnJourney);// calling menu
    } // End of Find seat by customer

    //when O is pressed :
    private void sortBooking(String[] seatArray, String[] customerArray, boolean returnJourney) {

        //Bubble sorting the customers based on the first letters of their name
        String[] sortedValuesList = new String[SEATING_CAPACITY+1]; // initializing array to store values temporarily till it gets sorted
        int number_of_loops = SEATING_CAPACITY-1; // initializing number_of_loops
        List<String> sortedList = new ArrayList<>(); // initializing list where all final Sorted values will be stored
        for(int seatNumber=1; seatNumber<(SEATING_CAPACITY+1); seatNumber++){
            sortedValuesList[seatNumber]=customerArray[seatNumber] + ", Seat number : " + seatNumber; // output statement
        }
        for(int indexValue=1; indexValue<=(SEATING_CAPACITY-1);indexValue++){
            for(int value=1; value<(number_of_loops); value++){
                if((int)sortedValuesList[value].charAt(0)>(int)sortedValuesList[value+1].charAt(0)) { //checks the first letter of the customers name
                    String temp = sortedValuesList[value]; // stores value in temporary
                    sortedValuesList[value] = sortedValuesList[value+1]; // exchanges value with next value
                    sortedValuesList[value+1] = temp; //takes temporary
                }
            }
            number_of_loops-=1; // decrementing value
        }
        for(int x=1;x<(SEATING_CAPACITY+1); x++){
            if((int)sortedValuesList[x].charAt(0)!=32){ //32 is the integer ASCII value for space
                sortedList.add(sortedValuesList[x]); // adding the values to the final sorted list
            }
        }
        for(int s=0; s<sortedList.size(); s++){

            System.out.println(sortedList.get(s)); // Printing out the sorted Values

        }
        System.out.println("\n All Booked Seats have been sorted successfully \n");
        menu(seatArray, customerArray,returnJourney);
    } // End of Sort seats by Customer Name

    //when S is pressed :
    private void saveToFile(String[] seatArray, String[] customerArray, boolean returnJourney){
        MongoClient client = new MongoClient("localhost", 27017); // calling mongo client
        if (!returnJourney) {
            DB dbs = client.getDB("PP2CW1DB"); // creating database
            DBCollection collection1 = dbs.getCollection("seatArrayCollection"); // creating collection to store values from seatArray
            DBCollection collection2 = dbs.getCollection("customerArrayCollection"); // creating collection to store values from customerArray

            collection1.drop();  //clears all the records stored in this collection
            collection2.drop();  //clears all the records stored in this collection

            BasicDBObject seatArrayDocument = new BasicDBObject();  // Saving seatArray to the database
            for (int value = 1; value < seatArray.length; value++) {
                seatArrayDocument.append(String.valueOf(value), seatArray[value]);
            }
            collection1.insert(seatArrayDocument); // inserting the values to the collection
            BasicDBObject customerArrayDocument = new BasicDBObject();  //Saving customerArray into the database
            for (int value = 1; value < customerArray.length; value++) {
                customerArrayDocument.append(String.valueOf(value), customerArray[value]); //appending the data into the document
            }
            collection2.insert(customerArrayDocument); // inserting the values into the collection
        }
        else{
            DB dbs = client.getDB("PP2CW1ReturnDB"); // creating database
            DBCollection collection1 = dbs.getCollection("seatArrayCollection"); // creating collection to store values from seatArray
            DBCollection collection2 = dbs.getCollection("customerArrayCollection"); // creating collection to store values from customerArray
            collection1.drop();  //clears all the records stored in this collection
            collection2.drop();  //clears all the records stored in this collection
            BasicDBObject seatArrayDocument = new BasicDBObject();  // Saving seatArray to the database
            for (int value = 1; value < seatArray.length; value++) {
                seatArrayDocument.append(String.valueOf(value), seatArray[value]);
            }
            collection1.insert(seatArrayDocument); // inserting the values to the collection
            BasicDBObject customerArrayDocument = new BasicDBObject();  //Saving customerArray into the database
            for (int value = 1; value < customerArray.length; value++) {
                customerArrayDocument.append(String.valueOf(value), customerArray[value]); //appending the data into the document
            }
            collection2.insert(customerArrayDocument); // inserting the values into the collection
        }
        System.out.println("\n Data has been saved successfully \n "); // statement that lets user know that data has been saved
        menu(seatArray, customerArray,returnJourney);//calls menu
    }

    //when L is pressed :
    private void loadFromFile(String[] seatArray,String[] customerArray, boolean returnJourney) {
        MongoClient client = new MongoClient("localhost", 27017);
        if(!returnJourney) {
            MongoDatabase database = client.getDatabase("PP2CW1DB"); // getting datablase where data was saved into
            // LOADING SEAT ARRAY
            MongoCollection<Document> collection1 = database.getCollection("seatArrayCollection"); // loading collection where seatArray is stored
            List<String> list1 = new ArrayList<>(); // creating list to store data temporarily
            for (Document dataSet : collection1.find()) { // searches the data in collection1
                list1.add(String.valueOf(dataSet)); // adding the data into the list
            }
            String firstSet = list1.get(0);
            firstSet = firstSet.replaceAll(" ", "");
            firstSet = firstSet.replaceAll("=", "");
            String[] seatvalues = firstSet.split(","); //separating each value with a comma
            int length1 = seatvalues.length; // getting length of seatValues and storing it in length1
            seatvalues[length1 - 1] = seatvalues[length1 - 1].replace("}", "");
            for (int index = 1; index < seatvalues.length; index++) {
                if (index < 10) {
                    seatvalues[index] = seatvalues[index].substring(1); // getting data for seats 1 to 9
                } else {
                    seatvalues[index] = seatvalues[index].substring(2); // getting data for seats 10 to 42
                }
                seatArray[index] = seatvalues[index]; // exchanging values from seatvalues into seatArray to be used in program
            }
            //LOADING CUSTOMER ARRAY
            MongoCollection<org.bson.Document> collection2 = database.getCollection("customerArrayCollection"); // calling collection2 where customerArray data is stored
            List<String> list2 = new ArrayList<>(); // creating temporary list to store data
            for (org.bson.Document dataSet : collection2.find()) {
                list2.add(String.valueOf(dataSet)); //adding data from collection2 into list2
            }
            String firstSet1 = list2.get(0); // getting first value
            firstSet1 = firstSet1.replaceAll(" ", "");// replacing the space with empty value
            firstSet1 = firstSet1.replaceAll("=", "");// replacing the = sign with empty value
            String[] customerValues = firstSet1.split(","); // separating each value with a comma
            int length2 = customerValues.length; // getting length of customerValues and storing it in length2
            customerValues[length2 - 1] = customerValues[length2 - 1].replace("}", "");
            for (int index = 1; index < customerValues.length; index++) {
                if (index < 10) {
                    customerValues[index] = customerValues[index].substring(1); // getting data for seats 1 to 9
                } else {
                    customerValues[index] = customerValues[index].substring(2); // getting data for seats 10 to 42
                }
                customerArray[index] = customerValues[index]; // exchanging values from customerValues into customerArray to be used in program
            }
        }else{
            //IF RETURN JOURNEY

            MongoDatabase database = client.getDatabase("PP2CW1ReturnDB"); // getting database where data was saved into
            // LOADING SEAT ARRAY
            MongoCollection<org.bson.Document> collection1 = database.getCollection("seatArrayCollection"); // loading collection where seatArray is stored
            List<String> list1 = new ArrayList<>(); // creating list to store data temporarily
            for (org.bson.Document dataSet : collection1.find()) { // searches the data in collection1
                list1.add(String.valueOf(dataSet)); // adding the data into the list
            }
            String firstSet = list1.get(0);
            firstSet = firstSet.replaceAll(" ", "");
            firstSet = firstSet.replaceAll("=", "");
            String[] seatvalues = firstSet.split(","); //separating each value with a comma
            int length1 = seatvalues.length; // getting length of seatValues and storing it in length1
            seatvalues[length1 - 1] = seatvalues[length1 - 1].replace("}", "");
            for (int index = 1; index < seatvalues.length; index++) {
                if (index < 10) {
                    seatvalues[index] = seatvalues[index].substring(1); // getting data for seats 1 to 9
                } else {
                    seatvalues[index] = seatvalues[index].substring(2); // getting data for seats 10 to 42
                }
                seatArray[index] = seatvalues[index]; // exchanging values from seatvalues into seatArray to be used in program
            }
            //LOADING CUSTOMER ARRAY
            MongoCollection<org.bson.Document> collection2 = database.getCollection("customerArrayCollection"); // calling collection2 where customerArray data is stored
            List<String> list2 = new ArrayList<>(); // creating temporary list to store data
            for (org.bson.Document dataSet : collection2.find()) {
                list2.add(String.valueOf(dataSet)); //adding data from collection2 into list2
            }
            String firstSet1 = list2.get(0); // getting first value
            firstSet1 = firstSet1.replaceAll(" ", "");// replacing the space with empty value
            firstSet1 = firstSet1.replaceAll("=", "");// replacing the = sign with empty value
            String[] customerValues = firstSet1.split(","); // separating each value with a comma
            int length2 = customerValues.length; // getting length of customerValues and storing it in length2
            customerValues[length2 - 1] = customerValues[length2 - 1].replace("}", "");
            for (int index = 1; index < customerValues.length; index++) {
                if (index < 10) {
                    customerValues[index] = customerValues[index].substring(1); // getting data for seats 1 to 9
                } else {
                    customerValues[index] = customerValues[index].substring(2); // getting data for seats 10 to 42
                }
                customerArray[index] = customerValues[index]; // exchanging values from customerValues into customerArray to be used in program
            }
        }
        System.out.println("\n Data has been Loaded successfully \n ");
        menu(seatArray,customerArray,returnJourney); // calling menu after loading
    }

    public static void main(String[] args) {
        launch(args);
    }
}