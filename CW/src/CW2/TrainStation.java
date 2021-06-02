// Hammadh Arquil - 1761780 - 2018128
package CW2;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static CW2.PassengerQueue.isFull;

public class TrainStation extends Application {
    Scanner input = new Scanner(System.in); // input
    Stage window; // making it stage window
    TableView<Passenger> waitingTables = new TableView<>(); // creating table to be used within the program
    TableView<Passenger> queueTables = new TableView<>();// creating table to be used within program
    PassengerQueue trainQueue = new PassengerQueue(); // creating trainqueue object from to be used within the trainstation class
    Passenger[] trainArray = trainQueue.getArray(); // creating array to store values from train queue

    static final int SEATING_CAPACITY = 42; // initializing seating capacity as a static final value to be used throughout the program

    @Override
    public void start(Stage primaryStage) {
            window = primaryStage; // naming the primary stage as window to be used within the program

            // Initializing all arrays to be use in the program
            String[] customerArray = new String[SEATING_CAPACITY+1]; // to be used to load details from seat booking system
            Passenger[] waitingRoom = new Passenger[SEATING_CAPACITY]; // creating waiting room passenger array
            Arrays.fill(waitingRoom,null); // filling array with null values

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
            if(journey.equals("2")){ //checks if the user has entered "1" or "2"
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
            System.out.println("Loading Passenger List...");
            loadPassengerDetails(customerArray,waitingRoom,returnJourney); // loading passenger details from cw1

            menu(waitingRoom,returnJourney, true); // calling menu to start program
        }

    private void menu(Passenger[] waitingRoom, boolean returnJourney, boolean count){
        Scanner sc = new Scanner(System.in);
        System.out.println("|============================================================|");
        System.out.println("|-- --- -- Welcome to the Train Station simulation  -- ------|");
        System.out.println("|--- ------- ------  Denuwara Menike Train  ------ ------ ---|");
        if (!returnJourney) {
            System.out.println("|------ ----- Colombo to Badulla | AC Compartment --- -------|");
        } else{
            System.out.println("|------ ----- Badulla to Colombo | AC Compartment --- -------|");
        }
        System.out.println("|============================================================|");
        System.out.println("--> Enter 'A' to Add passenger to Train Queue");
        System.out.println("--> Enter 'V' to View the Train Queue");
        System.out.println("--> Enter 'D' to Delete passenger(s) from Train Queue");
        System.out.println("--> Enter 'S' to Save current data to file");
        System.out.println("--> Enter 'L' to Load data from file ");
        System.out.println("--> Enter 'R' to Run a simulation and produce a report");
        System.out.println("--> Enter 'Q' to Quit");
        System.out.print("Enter your Option : ");
        String option = input.next();
        option = option.toUpperCase();

        switch (option) {
            case ("A"):
                System.out.println("Add Passenger to Queue executing...\n");
                addPassenger(waitingRoom,returnJourney,count);
                break;

            case ("V"):
                if(!count) {
                    System.out.println("View Queue executing...\n");
                    viewQueue(waitingRoom,returnJourney,count);
                }else{
                    System.out.println("There is no one in Queue, Please Add a Passenger to Queue to View");
                    menu(waitingRoom, returnJourney, true);
                }
                break;

            case ("D"):
                System.out.println("Delete a passenger from Queue executing...\n");
                deletePassenger(waitingRoom,returnJourney);
                break;

            case ("S"):
                System.out.println("Saving Queue Data to file executing...\n");
                saveQueueToFile(waitingRoom,returnJourney);
                break;

            case ("L"):
                System.out.println("Loading data from file executing...\n");
                loadQueueFromFile(waitingRoom,returnJourney,count);
                break;
            case ("R"):
                if(!count) {
                    System.out.println("Run Simulation executing...\n");
                    runSimulation(waitingRoom, returnJourney, count);

                }else{
                    System.out.println("There is no one in Queue, Please Add a Passenger to Queue to run simulation");
                    menu(waitingRoom, returnJourney, true);
                }
                break;
            case ("Q"):
                System.exit(0);
            default:
                System.out.println("\n Invalid input! please re-enter selection \n ");

        } // End of Switch case
    } // End of menu

    private void addPassenger(Passenger[] waitingRoom, boolean returnJourney, boolean count){

        //Waiting Name column
        TableColumn<Passenger, String> nameColumn1 = new TableColumn<>("Name"); // column heading
        nameColumn1.setMinWidth(200);
        nameColumn1.setCellValueFactory(new PropertyValueFactory<>("name")); // setting what value will be used in column

        //Waiting Seat column
        TableColumn<Passenger, String> seatColumn1 = new TableColumn<>("Seat");// column heading
        seatColumn1.setMinWidth(100);
        seatColumn1.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));// setting what value will be used in column

        //Queue Name column
        TableColumn<Passenger, String> nameColumn2 = new TableColumn<>("Name");// column heading
        nameColumn2.setMinWidth(200);
        nameColumn2.setCellValueFactory(new PropertyValueFactory<>("Name"));// setting what value will be used in column

        //Queue Seat column
        TableColumn<Passenger, String> seatColumn2 = new TableColumn<>("Seat");// column heading
        seatColumn2.setMinWidth(100);
        seatColumn2.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));// setting what value will be used in column


        if (count) { // to make sure that columns are not duplicated everytime A is entered
            waitingTables.getColumns().addAll(nameColumn1, seatColumn1);
            queueTables.getColumns().addAll(nameColumn2, seatColumn2);
            count=false;
        }

        Button addToQueue = new Button("Add Passenger to Queue"); // button

        waitingTables.setItems(getQueue(waitingRoom));// setting items to table
        queueTables.setItems(getQueue(trainArray)); //setting items to table

        Button closeWindow = new Button("Close");//button
        closeWindow.setOnAction(event -> {
            window.close(); // closing window
            menu(waitingRoom,returnJourney, false); // calls menu
        });
        closeWindow.setLayoutX(240);
        closeWindow.setLayoutY(20);

        addToQueue.setOnAction(event -> {
            int diceValue; // initiates value
            Random no = new Random();
            diceValue = no.nextInt(6); // gererates random value
            diceValue += 1;// makes sure value isnt 0
            if(!isFull()) { // checks if queue is full
                for (int i = 0; i <= diceValue; i++) {
                    for (int j = 0; j < SEATING_CAPACITY; j++) {
                        if (!(waitingRoom[j] == null)) {
                            trainQueue.add(waitingRoom[j]); // adds passenger to train queue
                            waitingRoom[j] = null; // removes passenger from waiting room
                            break;// breaks loop
                        }
                    }
                    waitingTables.setItems(getQueue(waitingRoom));// setting values to table
                    queueTables.setItems(getQueue(trainArray));// setting values to table
                }
            }else{
                System.out.println("Queue is full!"); // if queue is full print statement
            }
        });
        addToQueue.setLayoutX(300); // layout
        addToQueue.setLayoutY(20);

        Label header1 = new Label("Waiting Room"); // heading
        header1.setStyle("-fx-font-size:20px;");
        header1.setLayoutX(100);
        header1.setLayoutY(460);
        AnchorPane waitingPane = new AnchorPane(); // pane to keep waiting room table
        waitingPane.setPrefWidth(335); // setting width of pane
        waitingTables.setLayoutX(15);
        waitingTables.setLayoutY(20);
        waitingPane.setStyle("-fx-background-color:#a9d9d9"); // adding background color
        waitingPane.getChildren().addAll(waitingTables,header1); // adding items to pane

        Label header2 = new Label("Train Queue"); // heading
        header2.setStyle("-fx-font-size:20px;");
        header2.setLayoutX(100);
        header2.setLayoutY(460);
        AnchorPane queuePane = new AnchorPane(); // pane to keep queue table
        queuePane.setPrefWidth(350); // setting width of pane
        queueTables.setLayoutX(15);
        queueTables.setLayoutY(20);
        queuePane.getChildren().addAll(queueTables,header2); // adding items to pane
        queuePane.setStyle("-fx-background-color:#d98648");// adding background color

        AnchorPane bottomPane = new AnchorPane(); //creating anchor pane
        bottomPane.setStyle("-fx-background-color:white; -fx-text-align:center"); //background color
        bottomPane.getChildren().addAll(addToQueue,closeWindow); // adding items to be displayed in window
        bottomPane.setPrefHeight(60);

        BorderPane borderPane = new BorderPane(); // used to align panes
        borderPane.setStyle("-fx-background-color:#bfbfbf");// adding background color
        borderPane.setBottom(bottomPane);
        borderPane.setLeft(waitingPane);
        borderPane.setCenter(queuePane);
        Scene scene = new Scene(borderPane,662,600);
        window.setTitle("Add Passenger - Train Queue Program");
        window.setScene(scene);
        window.setResizable(false); // making sure window cannot be resized
        window.show(); // displaying window
    }

    public ObservableList<Passenger> getQueue(Passenger[] passenger){ // creating observable list to set items to table
        ObservableList<Passenger> passengerObservableList = FXCollections.observableArrayList(); // creating an observable list
        for (Passenger index : passenger){ // enhanced for loop to go through passengers
            if(index!=null){
                passengerObservableList.add(index); // adds all not null values to train queue table
            }
        }
        return passengerObservableList; // returns values to be added
    }

    private void viewQueue(Passenger[] waitingRoom, boolean returnJourney,boolean count) {
        Button addToQueue = new Button("Add Passenger to Queue"); // button
        Button[] btn = new Button[SEATING_CAPACITY]; // button array
        Label[] labels = new Label[SEATING_CAPACITY]; // label array
        GridPane gridPane = new GridPane(); // grid pane to align buttons and labels
        int columnIndex = 0; // column value
        int rowIndex = 0;// row value
        int rowLoop = 0;
        for(int indexValue = 0; indexValue<(SEATING_CAPACITY); indexValue++){
            if(!(trainArray[indexValue] == null)) {
                btn[indexValue] = new Button(trainArray[indexValue].getSeatNumber()); // creating button and adding seat number in it
                btn[indexValue].setStyle("-fx-background-color:#db2f2c; -fx-border-width:1 1 1 1; -fx-border-color:black;"); // styling button
                labels[indexValue] = new Label(trainArray[indexValue].getName()); // creating label and adding passenger name to it

            }else{
                btn[indexValue] = new Button("--"); // creates button if no passenger
                btn[indexValue].setStyle("-fx-background-color:#84f51b; -fx-border-width:1 1 1 1; -fx-border-color:black;");
                labels[indexValue] = new Label("Empty"); // label empty if no passenger in queue
            }
            btn[indexValue].setMinWidth(40);
            btn[indexValue].setPadding(new Insets(10)); // Padding inside the button around the seat number

            gridPane.add(btn[indexValue],columnIndex,rowIndex); //adding button with values to gridPane
            gridPane.add(labels[indexValue],columnIndex+1,rowIndex); //adding labels with values to gridPane

            columnIndex+=2; // increasing value

            rowLoop++; // incrementing value
            if(rowLoop==5){ // if row = 5 , it stops creating more columns
                columnIndex=0;
                rowLoop=0;
                rowIndex++;
            }
        }

        Button closeWindow = new Button("Close");
        closeWindow.setOnAction(event -> {
            window.close();
            menu(waitingRoom,returnJourney, false);
        });

        Label header2 = new Label("Train Queue");
        header2.setStyle("-fx-font-size:23px;"); // styling label
        header2.setLayoutX(290);//label positions
        header2.setLayoutY(20);
        closeWindow.setLayoutX(670);
        closeWindow.setLayoutY(600);

        gridPane.setLayoutY(70);
        gridPane.setLayoutX(40);
        gridPane.setHgap(25);
        gridPane.setVgap(25);

        AnchorPane anchorPane2 = new AnchorPane(); //creating anchor pane
        anchorPane2.setStyle("-fx-background-color:#a9d9d9; -fx-text-align:center"); //background color
        anchorPane2.getChildren().addAll(header2,gridPane,closeWindow); // adding items to be displayed in window
        anchorPane2.setPrefHeight(10);

        Scene scene = new Scene(anchorPane2,730,650);
        window.setTitle("Train Seat Booking System");
        window.setScene(scene);
        window.setResizable(false); // making sure window cannot be resized
        window.show(); // displaying window

    }

    private void deletePassenger(Passenger[] waitingRoom, boolean returnJourney) {
        System.out.print("Enter seat number of passenger to delete passenger from queue: "); //asking user question
        String seatNo = input.nextLine(); // getting user input and storing value
        boolean found = false;//setting initial value to false
        for(int n=0; n<SEATING_CAPACITY; n++){
            if (!(trainArray[n] == null)) {
                if (seatNo.equals(trainArray[n].getSeatNumber())) {
                    System.out.println(trainArray[n].getName() +" of seat number "+ trainArray[n].getSeatNumber() + " has been successfully removed from Train Queue \n"); // output deleted passenger
                    trainArray[n] = null; // removes passenger from array
                    found = true; //as passenger was found therefore it is true
                }
            }
        }
        String customer_not_found = ("there isn't any passenger at seat number " + seatNo + ", therefore no passengers have been deleted. "); // if seat with name not found
        if (!found){
            System.out.println(customer_not_found); // if there is no passenger with seat number given
        }

        menu(waitingRoom,returnJourney, false);// calling menu
    }

    private void saveQueueToFile(Passenger[] waitingRoom, boolean returnJourney){

        MongoClient client = new MongoClient("localhost", 27017); // calling mongo client
        if (!returnJourney) {
            DB dbs = client.getDB("PP2CW2DB"); // creating database
            DBCollection collection1 = dbs.getCollection("waitingRoomCollection"); // creating collection to store values
            DBCollection collection2 = dbs.getCollection("trainQueueCollection"); // creating collection to store values
            collection1.drop();  //clears all the records stored in this collection
            collection2.drop();  //clears all the records stored in this collection

            for (Passenger passenger : waitingRoom) { // going through waiting room and adding passengers
                if (passenger!= null) {
                    BasicDBObject waitingRoomDocument = new BasicDBObject(); // creating new document object to store new passenger
                    waitingRoomDocument.append("seatNumber", passenger.getSeatNumber()); // adding seat number
                    waitingRoomDocument.append("name", passenger.getName()); //  adding passenger name
                    collection1.insert(waitingRoomDocument); // inserting the document to the collection
                }
            }
            for (Passenger passenger : trainArray) {
                if (passenger!= null) {
                    BasicDBObject trainQueueDocument = new BasicDBObject();
                    trainQueueDocument.append("seatNumber",passenger.getSeatNumber()); // adding seat number
                    trainQueueDocument.append("Name", passenger.getName()); //  adding passenger name
                    collection2.insert(trainQueueDocument); // inserting the document into the collection
                }
            }
        }
        else{
            DB dbs = client.getDB("PP2CW2ReturnDB"); // creating database
            DBCollection collection1 = dbs.getCollection("waitingRoomCollection"); // creating collection to store values
            DBCollection collection2 = dbs.getCollection("trainQueueCollection"); // creating collection to store values
            collection1.drop();  //clears all the records stored in this collection
            collection2.drop();  //clears all the records stored in this collection

            for (Passenger passenger : waitingRoom) {
                if (passenger!= null) {
                    BasicDBObject waitingRoomDocument = new BasicDBObject();
                    waitingRoomDocument.append("seatNumber", passenger.getSeatNumber()); // adding seat number
                    waitingRoomDocument.append("name", passenger.getName());//  adding passenger name
                    collection1.insert(waitingRoomDocument); // inserting the document into the collection

                }
            }

            for (Passenger passenger : trainArray) {
                if (passenger!= null) {
                    BasicDBObject trainQueueDocument = new BasicDBObject();
                    trainQueueDocument.append("seatNumber",passenger.getSeatNumber()); // adding seat number
                    trainQueueDocument.append("Name", passenger.getName());//  adding passenger name
                    collection2.insert(trainQueueDocument); // inserting the document into the collection
                }
            }
        }
        System.out.println("\n Data has been saved successfully \n "); // statement that lets user know that data has been saved
        menu(waitingRoom,returnJourney, false); //calls menu
    }

    private void loadQueueFromFile(Passenger[] waitingRoom, boolean returnJourney, boolean count) {
        MongoClient client = new MongoClient("localhost", 27017);
        for(int i = 0;i<SEATING_CAPACITY;i++) {
            waitingRoom[i] =null; // looping through and making all values null
            trainArray[i] = null; // looping through and making all values null
        }
        if(!returnJourney) {
            MongoDatabase database = client.getDatabase("PP2CW2DB"); // getting database where data was saved into
            //LOADING WAITING ROOM
            MongoCollection<org.bson.Document> collection2 = database.getCollection("waitingRoomCollection"); // calling collection2 where waitingRoom data is stored
            FindIterable<org.bson.Document> findWaitingDocument = collection2.find();// finding document in loop
            for (org.bson.Document dataSet : findWaitingDocument) {
                for(int i=0; i<SEATING_CAPACITY;i++){
                    if(waitingRoom[i]==null){
                        Passenger passenger = new Passenger(); // making new passenger object
                        passenger.setName(dataSet.getString("name")); // setting passenger name
                        passenger.setSeatNumber(dataSet.getString("seatNumber")); // setting passenger seat number
                        waitingRoom[i] = passenger;// adding passenger to waiting room
                        break; // break loop
                    }
                }
            }
            //LOADING TRAIN QUEUE
            MongoCollection<org.bson.Document> collection1 = database.getCollection("trainQueueCollection"); // calling collection2 where train queue data is stored
            FindIterable<org.bson.Document> findQueueDocument = collection1.find();// finding document in loop
            for (org.bson.Document dataSet : findQueueDocument) {
                for(int i=0; i<SEATING_CAPACITY;i++){
                    if(trainArray[i]==null){
                        Passenger passenger = new Passenger();// making new passenger object
                        passenger.setName(dataSet.getString("Name"));// setting passenger name
                        passenger.setSeatNumber(dataSet.getString("seatNumber"));// setting passenger seat number
                        trainQueue.add(passenger); // adding passenger to train queue
                        break; // break loop
                    }
                }
            }
        }
        else{
            MongoDatabase database = client.getDatabase("PP2CW2ReturnDB"); // getting database where data was saved into
            //LOADING WAITING ROOM
            MongoCollection<org.bson.Document> collection2 = database.getCollection("waitingRoomCollection"); // calling collection2 where waitingRoom data is stored
            FindIterable<org.bson.Document> findWaitingDocument = collection2.find();// finding document in loop
            for (org.bson.Document dataSet : findWaitingDocument) {
                for(int i=0; i<SEATING_CAPACITY;i++){
                    if(waitingRoom[i]==null){
                        Passenger passenger = new Passenger(); // making new passenger object
                        passenger.setName(dataSet.getString("name")); // setting passenger name
                        passenger.setSeatNumber(dataSet.getString("seatNumber")); // setting passenger seat number
                        waitingRoom[i] = passenger;// adding passenger to waiting room
                        break; // break loop
                    }
                }
            }
            //LOADING TRAIN QUEUE
            MongoCollection<org.bson.Document> collection1 = database.getCollection("trainQueueCollection"); // calling collection2 where train queue data is stored
            FindIterable<org.bson.Document> findQueueDocument = collection1.find();// finding document in loop
            for (org.bson.Document dataSet : findQueueDocument) {
                for(int i=0; i<SEATING_CAPACITY;i++){
                    if(trainArray[i]==null){
                        Passenger passenger = new Passenger();// making new passenger object
                        passenger.setName(dataSet.getString("Name"));// setting passenger name
                        passenger.setSeatNumber(dataSet.getString("seatNumber"));// setting passenger seat number
                        trainQueue.add(passenger); // adding passenger to train queue
                        break; // break loop
                    }
                }
            }
        }

        //Waiting Name column
        TableColumn<Passenger, String> nameColumn1 = new TableColumn<>("Name"); // Column header
        nameColumn1.setMinWidth(200);
        nameColumn1.setCellValueFactory(new PropertyValueFactory<>("name")); // column value
        //Waiting Seat column
        TableColumn<Passenger, String> seatColumn1 = new TableColumn<>("Seat");// Column header
        seatColumn1.setMinWidth(100);
        seatColumn1.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));// column value

        //Queue Name column
        TableColumn<Passenger, String> nameColumn2 = new TableColumn<>("Name");// Column header
        nameColumn2.setMinWidth(200);
        nameColumn2.setCellValueFactory(new PropertyValueFactory<>("Name"));// column value
        //Queue Seat column
        TableColumn<Passenger, String> seatColumn2 = new TableColumn<>("Seat");// Column header
        seatColumn2.setMinWidth(100);
        seatColumn2.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));// column value

        if (count) { // makes sure no duplicate columns are created
            waitingTables.getColumns().addAll(nameColumn1, seatColumn1);
            queueTables.getColumns().addAll(nameColumn2, seatColumn2);
            count=false;
        }
        waitingTables.setItems(getQueue(waitingRoom)); // setting items to array
        queueTables.setItems(getQueue(trainArray)); // setting items to array

        System.out.println("\n Data has been Loaded successfully \n "); // statement so user knows
        menu(waitingRoom,returnJourney,false); // calling menu after loading
    }

    private void runSimulation(Passenger[] waitingRoom,boolean returnJourney,boolean count){
        //initializing values
        GridPane gridPane = new GridPane(); // creating grid pane to visualize output
        int rowIndex = 1;
        int columnIndex = 0;
        int rowLoop = 0;
        Label details;
        Label reportDetails;
        int[] timeTaken = new int[SEATING_CAPACITY];
        int timeValues = 0;
        int length = 0;
        int sum = 0;

        Button closeWindow = new Button("Close"); // close button
        closeWindow.setOnAction(event -> {
            window.close();
            menu(waitingRoom, returnJourney, false);
        });
        Passenger[] boardedPassengers = new Passenger[SEATING_CAPACITY];
        for (int i = 0; i < SEATING_CAPACITY; i++) {
            if (!(trainArray[i] == null)) {
                boardedPassengers[i] = trainArray[i]; // adds value to boarded passengers array
                trainQueue.remove(trainArray[i]);// removing passenger from train queue
                trainArray[i] = null;
            }
        }
        for (int i = 0; i < boardedPassengers.length; i++) {
            int diceValue, diceValue2, diceValue3;
            Random no = new Random();
            diceValue = no.nextInt(6) + 1;
            diceValue2 = no.nextInt(6) + 1;
            diceValue3 = no.nextInt(6) + 1;
            int timeInQueue = diceValue + diceValue2 + diceValue3; // generating time in queue values
            if (boardedPassengers[i] != null) {
                timeValues += timeInQueue; // to get passenger time in queue
                sum+=timeValues; // to be used for average
                timeTaken[i] = timeValues;
                details = new Label("Name : " + boardedPassengers[i].getName() + "\nSeatNumber: " + boardedPassengers[i].getSeatNumber() + "\nTime in Queue: " + timeValues + " Seconds");
                length++;//incrementing value
                gridPane.add(details, columnIndex, rowIndex);// adding details grid pane
                columnIndex++;
                rowLoop++; // incrementing value
                if (rowLoop == 6) { // if row = 6 , it stops creatinh more columns
                    columnIndex = 0;
                    rowLoop = 0;
                    rowIndex++;
                }
            }
        }
        int MaxStayInQueue = timeValues; // assigning values
        timeValues=0;
        int MinStayInQueue = timeTaken[0];
        int queueLength = length;
        int averageWaitTime = sum / queueLength;
        reportDetails = new Label("Max Queue length : " + queueLength + " Passengers, Maximum Wait time : " + MaxStayInQueue + " Seconds, Minimum Wait time : " + MinStayInQueue + " Seconds, Average Wait time : " + averageWaitTime + " Seconds \n\n");
        try {
            FileWriter report = new FileWriter("QueueReport.txt"); // to generate report text file and write to it
            report.write("Max Queue length : " + queueLength + " Passengers, Maximum Wait time : " + MaxStayInQueue + " Seconds, Minimum Wait time : " + MinStayInQueue + " Seconds, Average Wait time : " + averageWaitTime + " Seconds \n\n");
            for (int i = 0; i < SEATING_CAPACITY; i++) {
                if (boardedPassengers[i] != null) {
                    // writing data into text file
                    report.write("Name : " + boardedPassengers[i].getName() + "\nSeatNumber: " + boardedPassengers[i].getSeatNumber() + "\nTime in Queue: " + timeTaken[i] + " Seconds \n\n");
                }
            }
            // closing file
            report.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Label reportHeading = new Label("Simulation Report"); // header
        reportHeading.setLayoutY(15);
        reportHeading.setLayoutX(430);
        reportHeading.setStyle("-fx-font-size:20px;"); // styling label
        reportDetails.setLayoutX(50);
        reportDetails.setLayoutY(50);
        reportDetails.setStyle("-fx-font-size:16px;"); // styling label
        closeWindow.setLayoutX(470);
        closeWindow.setLayoutY(640);
        gridPane.setLayoutY(50);
        gridPane.setHgap(15);
        gridPane.setVgap(25);
        gridPane.setPadding(new Insets(30)); // setting padding to gridpane
        AnchorPane anchorPane = new AnchorPane(); //creating anchor pane
        anchorPane.setStyle("-fx-background-color:white; -fx-text-align:center"); //background color
        anchorPane.getChildren().addAll(reportDetails, reportHeading, gridPane, closeWindow); // adding items to be displayed in window
        Scene scene = new Scene(anchorPane, 1050, 700);
        window.setTitle("Simulation Report - Train Queue Program");
        window.setScene(scene);
        window.setResizable(false); // making sure window cannot be resized
        window.show(); // displaying window
    }

    private void loadPassengerDetails(String[] customerArray,Passenger[] waitingRoom, boolean returnJourney){
        MongoClient client = new MongoClient("localhost", 27017);
        if(!returnJourney) {
            MongoDatabase database = client.getDatabase("PP2CW1DB"); // getting database where data was saved into
            //LOADING CUSTOMER ARRAY
            MongoCollection<org.bson.Document> collection2 = database.getCollection("customerArrayCollection"); // calling collection2 where customerArray data is stored
            List<String> document = new ArrayList<>(); // creating temporary list to store data
            for (org.bson.Document dataSet : collection2.find()) {
                document.add(String.valueOf(dataSet)); //adding data from collection2 into document
            }
            String firstSet1 = document.get(0); // getting first value
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
            //LOADING CUSTOMER ARRAY
            MongoCollection<org.bson.Document> collection2 = database.getCollection("customerArrayCollection"); // calling collection2 where customerArray data is stored
            List<String> document = new ArrayList<>(); // creating temporary list to store data
            for (org.bson.Document dataSet : collection2.find()) {
                document.add(String.valueOf(dataSet)); //adding data from collection2 into document
            }
            String firstSet1 = document.get(0); // getting first value
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

        // After loading the array turning it into a 2d arraylist and assigning the seat number to each passenger

        ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>(); //arraylist in arraylist
        for(int i=1;i<SEATING_CAPACITY+1;i++){
            ArrayList<String> innerList = new ArrayList();
            if(!customerArray[i].equals("null")) {
                innerList.add(customerArray[i]); // adding customer name
                innerList.add(String.valueOf(i));// adding seat number
                listOfLists.add(innerList); // adding value to main arraylist
            }
        }
        for(List<String> index : listOfLists) {
            Passenger passenger = new Passenger(); // creating new passenger
            passenger.setName(index.get(0)); // getting first passenger name and setting it
            passenger.setSeatNumber(index.get(1)); // getting passenger seat number and setting it
            waitingRoom[listOfLists.indexOf(index)] = passenger; // adding passenger to waiting room
        }
        System.out.println("\n Data has been Loaded successfully \n ");
    }
}