package dhima.gerald.wump_final_project.Database;

import dhima.gerald.wump_final_project.Database.MySQLQueries;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;

/**
 * JavaFX application which creates the WUMP database with the appropriate tables in your system.
 */
public class CreateSQLTables extends Application implements SQLCodeConstants, MySQLQueries {
    private Connection connection; //A connection instance created used to connect with the MySQL database.
    private Statement statement; //A statement instance that will be used to send SQL queries.
    private TextField tfUsernameField; //Textfield where the user will provide their database username.
    private PasswordField pfPasswordField; //Passwordfield where the user will provide their database password.
    private Label errorLabel = new Label(""); //Label which is dynamically updated based on the errors found.
    private Integer currentPhaseCode; //Integer that will keep track of the errors found or the stage the program is currently at.
    private Scene scene; //The scene that will be displayed.
    private Button createUsersTable; //Button that creates the users table.
    private Button createDataTable; //Button which creates the logins table.
    private Button checkTablesStatus; //Button which checks the statuses of the user and login table.
    private Label userTableStatusLabel; //Label which shows the status of the user table.
    private Label dataTableStatusLabel; //Label which shows the status of the login table.

    /**
     * Starts the program and designs a scene which allows the user to enter his MySQL username and password.
     * @param primaryStage The primary stage which will show tbe scene
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        currentPhaseCode = PROGRAM_STARTED; //sets the phaseCode to indicate that the program started.

        tfUsernameField = new TextField();
        pfPasswordField = new PasswordField();

        //JavaFX formatting.
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Button logIn = new Button("Log In");
        Label welcomeLabel = new Label("Welcome! Provide the credentials of your user.");

        welcomeLabel.setTextFill(Color.BLUE);
        welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        errorLabel.setTextFill(Color.RED);

        //Creating a gridPane for the login form when the application starts.
        GridPane dbCredentials = new GridPane();
        dbCredentials.setAlignment(Pos.CENTER);
        dbCredentials.setHgap(10);
        dbCredentials.setVgap(10);
        dbCredentials.setPadding(new Insets(25, 25, 25, 25));

        dbCredentials.add(welcomeLabel, 0, 1, 5, 1);
        dbCredentials.add(usernameLabel, 0, 2);
        dbCredentials.add(tfUsernameField, 2, 2);
        dbCredentials.add(passwordLabel, 0, 3);
        dbCredentials.add(pfPasswordField, 2, 3);
        dbCredentials.add(logIn, 4, 4);
        dbCredentials.add(errorLabel, 0, 4, 3, 1);

        scene = new Scene(dbCredentials, 500, 250); //sets the credentialsGridPane as a the root of the scene.

        logIn.setOnAction(e -> connectToDB()); //Eventhandler when the login button is pressed.

        primaryStage.setScene(scene); //sets the Scene
        primaryStage.setTitle("MySQL User Client"); //sets the title.
        primaryStage.show();
    }

    /**
     * Tries to connect to the database by using the input provided by the user.
     * If the credentials are invalid (not able to create a connection) displays the user the appropiate response in the scene.
     * If the credentials are valid (connected successfully) displays a new scene which allows the user to create the needed tables.
     */
    private void connectToDB() {
        errorLabel.setText(""); //resets the errorLabel to empty.
        currentPhaseCode = PROGRAM_STARTED; //indicates that the program is still waiting for the credentials.

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //tries to load the JDBC driver.
        } catch (ClassNotFoundException e) { //if exception is caught,
            errorLabel.setText("Driver couldn't load."); //shows the user that the driver couldn't load through the label.
            currentPhaseCode = DRIVER_NOT_LOADED; //indicates that the driver was not loaded.
            System.out.println("Driver not loaded");
        }

        try {
            String username = tfUsernameField.getText().trim(); //gets the username written by the user
            String password = pfPasswordField.getText().trim(); //gets the password written by the user
            connection = DriverManager.getConnection("jdbc:mysql://localhost/", username, password); //tries to establish a connection with the provided credentials.

        } catch (SQLException e) { //if a SQLException is caught, it means that the connection couldn't be established with the credentials.
            tfUsernameField.clear(); //clears the username TF.
            pfPasswordField.clear(); //clears the password PF.
            errorLabel.setText("Wrong Credentials."); //shows the user that the credentials provided are wrong.
            currentPhaseCode = WRONG_CREDENTIALS; //indicates that the credentials provided are wrong.
            System.out.println("Wrong Credentials.");
        }

        if (currentPhaseCode == PROGRAM_STARTED) //checks if the code hasn't changed from the beginning of the handler.
            currentPhaseCode = CONNECTED; //in that case, we have connected successfully and the code is reassigned to the new phase.

        if (currentPhaseCode == CONNECTED) { //checks if we are connected to the database.

            int result; //keeps track of the number of changes made through the queries.
            try {
                statement = connection.createStatement(); //creates an instance of a statement.
                result = statement.executeUpdate(CREATE_SQL_DATABASE); //executes the query responsible for creating the database
                //and assigns the row count for the changes made to the result variable.
                if(result != 0) //checks if there were made changes and the database was actually created.
                    System.out.println("Database created");

                //Creating a GridPane for the Connection Phase.
                GridPane successfulLogin = new GridPane();
                successfulLogin.setAlignment(Pos.CENTER);
                successfulLogin.setHgap(10);
                successfulLogin.setVgap(10);
                successfulLogin.setPadding(new Insets(25, 25, 25, 25));

                Label successfulLoginLabel = new Label("Welcome");
                successfulLoginLabel.setTextFill(Color.BLUE);
                successfulLoginLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

                Label tablesStatus = new Label("Status");
                Label userTableLabel = new Label("Users Table:");
                userTableStatusLabel = new Label("Unknown");
                Label dataTableLabel = new Label("Data Table:");
                dataTableStatusLabel = new Label("Unknown");
                checkTablesStatus = new Button("Check Tables' Statuses");
                createDataTable = new Button("Create Data Table");
                createUsersTable = new Button("Create Users Table");
                createUsersTable.setDisable(true); //Doesn't allow the user to create the User Table without checking the status first.
                createDataTable.setDisable(true); //Doesn't allow the user to create the Login Table without checking the status first.

                successfulLogin.add(successfulLoginLabel, 1, 0, 3, 1);
                successfulLogin.add(tablesStatus, 1, 1);
                successfulLogin.add(userTableLabel, 0, 2);
                successfulLogin.add(userTableStatusLabel, 1, 2);
                successfulLogin.add(dataTableLabel, 0, 3);
                successfulLogin.add(dataTableStatusLabel, 1, 3);
                successfulLogin.add(checkTablesStatus, 3, 3, 3, 1);
                successfulLogin.add(createUsersTable,0,4,2,1);
                successfulLogin.add(createDataTable,3,4,2,1);

                scene.setRoot(successfulLogin); //sets the root of the scene accordingly to the new phase.

                checkTablesStatus.setOnAction(e-> { //eventHandler for the checkTables button.
                    try {
                        checkTables();
                    } catch (SQLException ex) {
                        System.out.println("Closed connection or database error occurred.");
                        ex.printStackTrace();
                    }
                });
                System.out.println(currentPhaseCode);
            } catch (SQLException e) {
                System.out.println("SQL query is invalid.");
                e.printStackTrace();
            }
        }

    }

    /**
     * Checks whether any of the tables already exists in the system.
     * @throws SQLException
     */
    private void checkTables() throws SQLException {
        DatabaseMetaData dbm = connection.getMetaData(); //gets the metadata of the database.
        Boolean userTableFound; //flag whether the user table exists or not.
        Boolean dataTableFound; //flag whether the login table exists or not.
        checkTablesStatus.setDisable(true); //doesn't allow the user to check for the tables' status again.

        ResultSet tables = dbm.getTables(null,null,"WUMPUsers",null); //creates a result set
        //which checks whether they are any tables named Users.
        if(tables.next()){ //checks whether the result set is not empty.
            userTableFound = true; //if it's not empty, the table exists and we rearrange the flag accordingly
        }else{
            userTableFound = false; //if it's empty, the table does not exist and the flag is arrenged accordingly.
        }

        tables = dbm.getTables(null,null,"WUMPData",null); //gets the result set of
        //the tables which are named GDhimaLogins.
        if(tables.next()){ //if the result set is not empty, the table exists, make the flag for the login table true.
            dataTableFound = true;
        }else{ //if the set is empty, the table doesn't exist and the flag is equal to false.
            dataTableFound = false;
        }

        //Creating 4 possible outcomes regarding the tables' statuses.
        if(userTableFound && dataTableFound){ //both tables exist.
            currentPhaseCode = USER_FOUND_DATA_FOUND;
            userTableStatusLabel.setText("Found");
            userTableStatusLabel.setTextFill(Color.GREEN);

            dataTableStatusLabel.setText("Found");
            dataTableStatusLabel.setTextFill(Color.GREEN);

        }else if(!userTableFound && dataTableFound){ //the user table is missing, the login table exists.
            currentPhaseCode = USER_NOTFOUND_DATA_FOUND; //changes the code accordingly to the phase
            userTableStatusLabel.setText("Missing");
            userTableStatusLabel.setTextFill(Color.RED);

            dataTableStatusLabel.setText("Found");
            dataTableStatusLabel.setTextFill(Color.GREEN);
            createUsersTable.setDisable(false); //allows the user to create the user table.
            createUsersTable.setOnAction(e-> { //eventhandler for the button which allows the user to create the users table.
                try {
                    createUsersTableEvent();
                } catch (SQLException ex) {
                    System.out.println("Unable to create the users table.");
                    ex.printStackTrace();
                }
            });

        }else if(userTableFound && !dataTableFound){ //The login table is missing, user table exists.
            currentPhaseCode = USER_FOUND_DATA_NOTFOUND; //changes the code accordingly to the phase
            userTableStatusLabel.setText("Found");
            userTableStatusLabel.setTextFill(Color.GREEN);

            dataTableStatusLabel.setText("Missing");
            dataTableStatusLabel.setTextFill(Color.RED);
            createDataTable.setDisable(false); //allows the user to press the button to create the logins table.
            createDataTable.setOnAction(e -> { //eventhandler for that button
                try {
                    createDataTableEvent();
                } catch (SQLException ex) {
                    System.out.println("Unable to create the Data table");
                    ex.printStackTrace();
                }
            });

        }else if (!userTableFound && !dataTableFound){ //Neither of the tables is found.
            currentPhaseCode = USER_NOTFOUND_DATA_NOTFOUND;
            userTableStatusLabel.setText("Missing");
            userTableStatusLabel.setTextFill(Color.RED);

            dataTableStatusLabel.setText("Missing");
            dataTableStatusLabel.setTextFill(Color.RED);
            createDataTable.setDisable(false); //allows the user to press the button to create the logins table.
            createUsersTable.setDisable(false); //allows the user to press the button to create the users table.

            createDataTable.setOnAction(e -> { //event handler for the button which creates the logins table.
                try {
                    createDataTableEvent();
                } catch (SQLException ex) {
                    System.out.println("Unable to create the login table.");
                    ex.printStackTrace();
                }
            });

            createUsersTable.setOnAction(e-> { // event handler for the button which creates the users table.
                try {
                    createUsersTableEvent();
                } catch (SQLException ex) {
                    System.out.println("Unable to create the users table.");
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Button On Click Listener which creates the Data table in the database when the button is pressed by the user.
     * @throws SQLException For incorrect SQL queries.
     */
    private void createDataTableEvent() throws SQLException {
        statement.executeUpdate(USE_WUMP_DATABASE); //tells the program to use the database we created earlier.
        statement.executeUpdate(CREATE_DATA_TABLE); //executes the query to create the login table.
        System.out.println("Data Table Created.");
        createDataTable.setDisable(true); //prohibits user from creating the login table again.
        dataTableStatusLabel.setText("Created"); //changes the status to created.
        dataTableStatusLabel.setTextFill(Color.GREEN);
    }
    /**
     * Button On Click Listener which creates the Users table in the database when the button is pressed by the user.
     * @throws SQLException For incorrect SQL queries.
     */
    private void createUsersTableEvent() throws SQLException {
        statement.executeUpdate(USE_WUMP_DATABASE);  //tells the program to use the database we created earlier.
        statement.executeUpdate(CREATE_USERS_TABLE); //executes the query to create the users table.
        createUsersTable.setDisable(true); //prohibits user from creating the user table again.
        userTableStatusLabel.setText("Created"); //changes the status to created.
        userTableStatusLabel.setTextFill(Color.GREEN);

        System.out.println("User Table Created.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

