package dhima.gerald.wump_final_project;

import dhima.gerald.wump_final_project.Database.MySQLQueries;
import dhima.gerald.wump_final_project.model.Account;
import dhima.gerald.wump_final_project.model.AccountCodes;
import dhima.gerald.wump_final_project.model.Data;
import dhima.gerald.wump_final_project.model.ModifyExperiment;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
/**
 * This class creates a server which processes the clients' requests  transferred by the servlets
 * saves the requests into a log and sends back a response to the servlet.
 * Multi-threaded to serve multiple clients simultaneously.
 * @author Gerald Dhima
 * @version 1.0
 */

public class Server extends Application implements AccountCodes, MySQLQueries {
    /**
     * Data Field: Text area where all the logs will be provided. */
    private TextArea textArea = new TextArea(); //a textarea where the log will be provided.
    /**
     * Data Field: A connection instance that is used to connect with the MySQL database.
     */
    private Connection connection;
    /**
     * Data Field: A statement instance that is used to send SQL queries.
     */
    private Statement statement;
    /**
     * Data Field: Textfield where the user provides their database username.
     */
    private TextField tfUsernameField;
    /**
     * Data Field:Passwordfield where the user will provide their database password.
     */
    private PasswordField pfPasswordField;
    /**
     * Data Field: Label which is dynamically updated based on the errors found.
     */
    private Label errorLabel = new Label("");
    /**
     * Data Field: The scene that will be displayed in the stage.
     */
    private Scene scene;
    /**
     * Data Field: The Encryption Number for the simple encryption.
     */
    private static final Integer encryptionNumber = 5;


    /**
     * Starts the server application and displays it in a friendly-UI.
     * Asks the admin/server handler to provide the database credentials to create the needed connection.
     * @param primaryStage The stage which will show all the logs.
     * @throws Exception Exceptions caused by non-appropriate usage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        tfUsernameField = new TextField();
        pfPasswordField = new PasswordField();

        //JavaFX formatting.
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Button logIn = new Button("Log In");
        Label welcomeLabel = new Label("Welcome! Provide the credentials of the database.");

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
        primaryStage.setTitle("Connect to Database."); //sets the title.
        primaryStage.show();

    }

    /**
     * Runnable task which handles each client's requests.
     */
    class HandleAClient implements Runnable { //Thread task for a client.
        /**
         * Data Field: The socket where the client has connected with the server.
         */
        private Socket socket;

        /**
         * Constructor.
         * @param socket Provides the socket where the connection server-client was made.
         */
        public HandleAClient(Socket socket) { //Constructor for this class
            this.socket = socket;
        }

        /**
         * Getter for the socket.
         * @return The socket of the connection server-client.
         */
        public Socket getSocket() {
            return socket;
        }

        /**
         * The engine of the project.
         * Creates input and output streams with the client in order to
         * constantly keep exchanging data with the client.
         * Gets the client's request after they have been processed in the servlets,
         * takes action based on the status/information and gives a response back to the servlet.
         * Purposes:
         *    Validates account creation through isUsernameUsed function.
         *    Creates account through createAccount function.
         *    Creates a request stage when the client applies to be a Professional Scientist. The Server Handler can decline or accept the request in this stage. Through createStageForProfessionalScientistRequest() function.
         *    Validates account login through validateLogin function.
         *    Gets the experiments and stores them in the database through functions insertExperimentDataIntoDatabase and insertImageToDatabase
         *    Sends all the experiments to the servlets to display them to the user.
         *    Deletes/Updates information on the database based on client's request.
         *    ETC.
         */
        @Override
        public void run() {
            try {
                ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream()); //Opens an input stream to receive an object (Account or Data) from the Client as a packet.
                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream()); //Opens an output stream to send data to the client/.
                DataInputStream codeFromClient = new DataInputStream(socket.getInputStream()); //Opens an input stream to receive data from the client (System codes).


                Integer actionFromClientCode = codeFromClient.readInt(); //Reads the code being sent by the client

                if (actionFromClientCode.equals(SENDING_ACCOUNT_OBJECT)) { //Checks whether the servlet is sending an account object
                    // In this case, the client is either trying to log in or create an account.
                    Account account = (Account) fromClient.readObject(); //Gets the account information given by the user.

                    Integer accountStatus = account.getAccountStatus(); //Checks the account status of the account (Logging In or Signing Up)

                    if (Objects.equals(accountStatus, SIGNING_UP)) { //If the client is trying to sign up :
                        if (isUsernameUsed(account.getUsername())) { //Gets the username stored in the model and checks whether it has already been used.
                            //If it has already been used :
                            textArea.appendText("Client with IP: " + socket.getInetAddress() + " sent a request for signup with an already existing username at " + new Date() + "\n"); //Print it as a log.
                            toClient.writeInt(ALREADY_USED_USERNAME); //Notify the servlet that the username is already used.
                        } else { //If the username has not been used:
                            textArea.appendText("Client with IP: " + socket.getInetAddress() + " sent a request for signup successfully at " + new Date() + "\n"); //Print the log.
                            if (Objects.equals(account.getVerificationStatus(), "Pending")) { //Checks whether the client has applied to be a professional scientist.
                                // If yes, his verification status is pending and the following steps are taken:
                                textArea.appendText("Client with IP: " + socket.getInetAddress() + " sent a request to open a professional scientist account at " + new Date() + "\n");

                                createAccount(account); //Stores the account in the database.

                                Platform.runLater(new Runnable() { //Updates the GUI from this NON-GUI thread making it possible to open a new stage.
                                    public void run() {
                                        createStageForProfessionalScientistRequest(account, String.valueOf(socket.getInetAddress()));
                                        //Opens a new stage which allows the server handler to handle the request.
                                    }
                                });


                                toClient.writeInt(SUCCESSFULLY_APPLIED_PROFESSIONAL_SCIENTIST_ACCOUNT); //Notifies the servlet that the user has successfully created and applied for the account
                            } else { //In this case the client has applied to be just a citizen scientist.
                                textArea.appendText("Client with IP: " + socket.getInetAddress() + " created a account successfully at " + new Date() + "\n");
                                createAccount(account); //Stores the account in the database.
                                toClient.writeInt(SUCCESSFULLY_CREATED_CITIZEN_SCIENTIST_ACCOUNT); //Notifies the servlet that the client has been able to successfully create citizen scientist account
                            }
                        }
                    } else if (Objects.equals(accountStatus, LOGGING_IN)) { //Checks whether the client is trying to log in.
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " tried to log in at " + new Date() + "\n");

                        if (Objects.equals(validateLogin(account), INVALID_ACCOUNT_CREDENTIALS)) {
                            textArea.appendText("Client with IP: " + socket.getInetAddress() + " provided the wrong login credentials.\n");
                        } else {
                            textArea.appendText("Client with IP: " + socket.getInetAddress() + " successfully logged in.\n");
                        }
                        toClient.writeInt(validateLogin(account));
                        //Notifies the servlet about the client's login process.
                    }
                } else if (actionFromClientCode.equals(SENDING_EXPERIMENT_DATA_OBJECT)) { //Checks whether the client is sending an experiment
                    //In this case the client is trying to insert data into the database.
                    Data experimentData = (Data) fromClient.readObject(); //Reads the experiment data transferred by the servlet.
                    experimentData.setUserId(getUserIdByUsername(experimentData.getUsername())); //Sets the User ID in this model based on the username of the client
                    experimentData.setAccessLevel(getAccessLevelByUsername(experimentData.getUsername())); //Sets the Access Level in this model based on the username of the client.


                   insertExperimentDataIntoDatabase(experimentData); //Inserts the data into the mysql database.
                   insertImageToDatabase(experimentData.getByteImage(),experimentData); //Stores the image into the harddrive
                   textArea.appendText("Client with IP: " + socket.getInetAddress() + " successfully inserted data.\n");
                   toClient.writeInt(SUCCESSFULLY_ENTERED_DATA); //Notify the servlet that the data was successfully stored.
                } else if (actionFromClientCode.equals(GETTING_EXPERIMENTS_DATA)){ //Checks whether the client is getting the experiments back
                   //In this case, they are trying to view the database.
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); //Opens an Object outputstream to transfer the experiments to the servlet.
                    textArea.appendText("Client with IP: " + socket.getInetAddress() + " is viewing the database.\n");

                    ArrayList<Data> experiments = getExperimentsFromDatabase(); //Stores all the experiments in an arraylist.
                    objectOutputStream.writeObject(experiments); //Transfers the arraylist to the servlet.

                } else if(actionFromClientCode.equals(VIEWING_DATABASE_AFTER_DELETING_EXPERIMENT)){ //Checks whether the client is trying to delete an experiment
                    textArea.appendText("Client with IP: " + socket.getInetAddress() + " is trying to delete an experiment from the database.\n");
                    ModifyExperiment experiment = (ModifyExperiment) fromClient.readObject(); //Reads the model of the experiment the client is trying to delete

                    Integer requestStatus = deleteExperimentId(experiment.getExperimentId()); //Deletes the experiment based on its id.

                    if(requestStatus.equals(SUCCESSFULLY_MODIFIED_DATABASE)){
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " successfully deleted an experiment the database\n");
                    }else if(requestStatus.equals(UNABLE_TO_MODIFY_DATABASE)){
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " was unable to delete an experiment from the database.\n");
                    }else if(requestStatus.equals(RECORD_DOES_NOT_EXIST)){
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " tried to delete an experiment which does not exist. \n");
                    }
                    toClient.writeInt(requestStatus); //Notifies the servlet about the request status.

                } else if(actionFromClientCode.equals(VIEWING_DATABASE_AFTER_UPDATING_EXPERIMENT)){ //checks whether the client is trying to update an experiment's data.
                    textArea.appendText("Client with IP: " + socket.getInetAddress() + " is updating an experiment from the database.\n");
                    ModifyExperiment experiment = (ModifyExperiment) fromClient.readObject();

                    Integer requestStatus = updateExperimentId(experiment.getExperimentId(),experiment.getUpdateField(),experiment.getUpdatedValue());
                    //Tries to update the experiment in the database and saves the Request Status returned from the function.
                    if(requestStatus.equals(SUCCESSFULLY_MODIFIED_DATABASE)){
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " successfully updated an experiment the database\n");
                    }else if(requestStatus.equals(UNABLE_TO_MODIFY_DATABASE)){
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " was unable to update an experiment from the database.\n");
                    }else if(requestStatus.equals(RECORD_DOES_NOT_EXIST)){
                        textArea.appendText("Client with IP: " + socket.getInetAddress() + " tried to update an experiment which does not exist. \n");
                    }
                    toClient.writeInt(requestStatus); //Notifies the servlet about the request status.
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Tries to connect to the database.
     * If it fails (ClassNotFoundException or SQLException exceptions caught), provides the information to the server handler.
     * If it connects successfully, it calls the function openServerView().
     */
    private void connectToDB() {
        errorLabel.setText(""); //resets the errorLabel to empty.

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //tries to load the JDBC driver.
        } catch (ClassNotFoundException e) { //if exception is caught,
            errorLabel.setText("Driver couldn't load."); //shows the user that the driver couldn't load through the label.
            //currentPhaseCode = DRIVER_NOT_LOADED; //indicates that the driver was not loaded.
            System.out.println("Driver not loaded");
        }

        try {
            String username = tfUsernameField.getText().trim(); //gets the username written by the user
            String password = pfPasswordField.getText().trim(); //gets the password written by the user
            connection = DriverManager.getConnection("jdbc:mysql://localhost/", username, password); //tries to establish a connection with the provided credentials.

            Statement statement = connection.createStatement();
            statement.executeUpdate(USE_WUMP_DATABASE); //Specifies the database we are using.

            openServerView(); //Opens the servers view after connecting to the database.

        } catch (SQLException e) { //if a SQLException is caught, it means that the connection couldn't be established with the credentials.
            tfUsernameField.clear(); //clears the username TF.
            pfPasswordField.clear(); //clears the password PF.
            errorLabel.setText("Wrong Credentials."); //shows the user that the credentials provided are wrong.
            //currentPhaseCode = WRONG_CREDENTIALS; //indicates that the credentials provided are wrong.
            System.out.println("Wrong Credentials.");
        }

    }

    /**
     * Changes the root of the scene of the primaryStage in order to display the server logs.
     * Creates a server socket which keeps listening for new connections/clients and creates a new thread for each-one of them.
     * Starts each thread once a connection is established with a client by running the HandleAClientThreadTask
     */
    private void openServerView() {
        //The GUI for the server.
        scene.setRoot(new ScrollPane(textArea));

        //Creates a new thread for every client.
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(7777); //creates a serversocket with 7777 as a port.
                textArea.appendText("Server was started at: " + new Date() + "\n"); //Log information.

                while (true) { //Infinite loop which keeps listening for new connections/clients.
                    Socket socket = serverSocket.accept(); //connects to a client by creating a mutual sock with them.

                    Platform.runLater(() -> {
                        textArea.appendText(""); //log information.
                    });
                    new Thread(new HandleAClient(socket)).start(); //starts a thread which deals with this client.
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }).start();
    }

    /**
     * Checks whether the username is already used by an account stored in the database.
     * @param username The username provided by the client in the account creation form.
     * @return Returns a boolean whether the username is occupied by another account or not.
     */
    private boolean isUsernameUsed(String username) {
        Boolean usernameFound = false; //Initializes the flag to false initially.
        PreparedStatement findUserPreparedStatement = null;
        try {

            findUserPreparedStatement = connection.prepareStatement(CHECK_FOR_USERNAME_PREPAREDSTATEMENT_QUERY); //creates a preparedstatement to check whether the username exists
            findUserPreparedStatement.setString(1, username); //sets the parameter equal to the username
            ResultSet resultSet = findUserPreparedStatement.executeQuery();

            if (resultSet.next()) //if the resultset is not null
                usernameFound = true; //The username already exists and the set the flag to true.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernameFound; //returns the flag.
    }

    /**
     * Stores the account the user was trying to create into the database.
     * @param account An Account Object which contains all the information provided by the user in the Create Account form.
     */
    private void createAccount(Account account) {

        PreparedStatement insertUserPreparedStatement = null;
        try {
            insertUserPreparedStatement = connection.prepareStatement(CREATE_ACCOUNT_PREPAREDSTATEMENT_QUERY);
            //Creates the prepared statement with all the account parameters needed to insert the user into the database.
            // Sets the parameters accordingly to the account information given by the client
            insertUserPreparedStatement.setString(1, account.getFirstName());
            insertUserPreparedStatement.setString(2, account.getLastName());
            insertUserPreparedStatement.setString(3, account.getInstitution());
            insertUserPreparedStatement.setString(4, account.getUsername());
            insertUserPreparedStatement.setString(5, simpleEncryptPassword(account.getPassword()));
            insertUserPreparedStatement.setString(6, account.getAccessLevel());
            insertUserPreparedStatement.setString(7, account.getVerificationStatus());
            insertUserPreparedStatement.executeUpdate(); //executes the update.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a stage with the information provided by the client
     * when they apply to create a Professional Scientist Account.
     * Allows the server handler to decline or accept the application through the use of buttons.
     * @param account Account object which contains all the information provided by the user.
     * @param ip The IP of the user trying to create the Professional Scientist account.
     */
    private void createStageForProfessionalScientistRequest(Account account, String ip) {
        //Java FX components
        Stage profScientistStage = new Stage();

        GridPane profScientistRequestGridPane = new GridPane();
        profScientistRequestGridPane.setAlignment(Pos.CENTER);
        profScientistRequestGridPane.setVgap(10);
        profScientistRequestGridPane.setHgap(10);
        profScientistRequestGridPane.setPadding(new Insets(25, 25, 25, 25));

        Label titleLabel = new Label("New professional scientist request!");
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label institutionLabel = new Label("School/Company:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label ipLabel = new Label("IP:");

        //Labels which show the account's information.
        Label firstNameValue = new Label(account.getFirstName());
        Label lastNameValue = new Label(account.getLastName());
        Label usernameValue = new Label(account.getUsername());
        Label passwordValue = new Label(account.getPassword());
        Label institutionValue = new Label(account.getInstitution());
        Label ipValue = new Label(ip);


        firstNameValue.setTextFill(Color.BLUE);
        lastNameValue.setTextFill(Color.BLUE);
        usernameValue.setTextFill(Color.BLUE);
        passwordValue.setTextFill(Color.BLUE);
        institutionValue.setTextFill(Color.BLUE);
        ipValue.setTextFill(Color.BLUE);

        titleLabel.setTextFill(Color.RED);

        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        //Buttons to decline and approve the request.
        Button declineRequestButton = new Button("Decline");
        Button acceptRequestButton = new Button("Approve");

        profScientistRequestGridPane.add(titleLabel, 0, 0, 3, 1);
        profScientistRequestGridPane.add(firstNameLabel, 0, 2, 1, 1);
        profScientistRequestGridPane.add(firstNameValue, 1, 2, 1, 1);
        profScientistRequestGridPane.add(lastNameLabel, 0, 3, 1, 1);
        profScientistRequestGridPane.add(lastNameValue, 1, 3, 1, 1);
        profScientistRequestGridPane.add(institutionLabel, 0, 4, 1, 1);
        profScientistRequestGridPane.add(institutionValue, 1, 4, 1, 1);
        profScientistRequestGridPane.add(usernameLabel, 0, 5, 1, 1);
        profScientistRequestGridPane.add(usernameValue, 1, 5, 1, 1);
        profScientistRequestGridPane.add(passwordLabel, 0, 6, 1, 1);
        profScientistRequestGridPane.add(passwordValue, 1, 6, 1, 1);
        profScientistRequestGridPane.add(ipLabel, 0, 7, 1, 1);
        profScientistRequestGridPane.add(ipValue, 1, 7, 1, 1);
        profScientistRequestGridPane.add(declineRequestButton, 0, 8, 1, 1);
        profScientistRequestGridPane.add(acceptRequestButton, 3, 8, 1, 1);

        //Functions and statements executed when the request is accepted
        acceptRequestButton.setOnAction(event -> {
            approveButtonAction(profScientistStage, account.getUsername());
            textArea.appendText("Client with IP: " + ip + " and username: " + account.getUsername() + " was approved as a professional scientist at:" + new Date() + "\n");
        });
        //Functions and statements executed when the request is declined.
        declineRequestButton.setOnAction(event -> {
            declineButtonAction(profScientistStage, account.getUsername());
            textArea.appendText("Client with IP: " + ip + " and username: " + account.getUsername() + " was declined for a professional scientist at:" + new Date() + "\n");
        });

        Scene scene = new Scene(profScientistRequestGridPane, 450, 400);
        profScientistStage.setScene(scene);

        profScientistStage.setTitle("Professional Scientist Request");
        profScientistStage.show();
    }

    /**
     * Button event handler which is set on action when the server handler approves the
     * Professional Scientist request by the client.(presses the Approve button)
     * Changes the verificationStatus of the account in the database from Pending to Approved
     * @param stage Provides the stage where the Approve button is located in order to close it the button is pressed.
     * @param username Provides the username of the account which is approved as a Professional Scientist.
     */
    private void approveButtonAction(Stage stage, String username) {
        try {
            PreparedStatement approveProfessionalScientistPreparedStatement = connection.prepareStatement(APPROVE_PROFESSIONAL_SCIENTIST_PREPAREDSTATEMENT_QUERY);
            approveProfessionalScientistPreparedStatement.setString(1, username);
            approveProfessionalScientistPreparedStatement.executeUpdate();

            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Button event handler which is set on action when the server handler declines the
     * Professional Scientist request by the client.(presses the Decline button)
     * Changes the verificationStatus of the account in the database from Pending to Declined
     * @param stage Provides the stage where the Decline button is located in order to close it the button is pressed.
     * @param username Provides the username of the account which is declined as a Professional Scientist.
     */
    private void declineButtonAction(Stage stage, String username) {
        try {
            PreparedStatement declineProfessionalScientistPreparedStatement = connection.prepareStatement(DECLINE_PROFESSIONAL_SCIENTIST_PREPAREDSTATEMENT_QUERY);
            declineProfessionalScientistPreparedStatement.setString(1, username);
            declineProfessionalScientistPreparedStatement.executeUpdate();

            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the account credentials entered by the user in the database and checks for the verification status of the account.
     * @param account Account object with the information provided by the user to log in.
     * @return Returns a system code which indicates whether the information provided was invalid or their verification status if it was valid.
     */
    private Integer validateLogin(Account account) {
        Integer systemCode = INVALID_ACCOUNT_CREDENTIALS;
        try {
            PreparedStatement validateLoginPreparedStatement = connection.prepareStatement(VALIDATE_LOGIN_QUERY);

            validateLoginPreparedStatement.setString(1, account.getUsername());
            validateLoginPreparedStatement.setString(2, simpleEncryptPassword(account.getPassword()));
            ResultSet rs = validateLoginPreparedStatement.executeQuery(); //Executes the query which checks for the account information.

            if (!rs.next()) { //If there are no results, the account doesn't exist or the user provided the wrong credentials.
                systemCode = INVALID_ACCOUNT_CREDENTIALS;
            } else { //If the account exists and the credentials are correct, the program checks for the verification status of the client.
                if (Objects.equals(rs.getString("verificationStatus"), "Pending")) { //Checks whether the verification status is still pending
                    systemCode = PENDING_PROFESSIONAL_SCIENTIST_REVIEW;
                } else if (Objects.equals(rs.getString("verificationStatus"), "Declined")) { //Checks whether the application has been denied.
                    systemCode = DECLINED_PROFESSIONAL_SCIENTIST;
                } else if (Objects.equals(rs.getString("verificationStatus"), "Approved")) { //Checks whether the account has been approved.
                    if (Objects.equals(rs.getString("accessLevel"), "citizenScientist")) { //For a citizen scientist
                        systemCode = CITIZEN_SCIENTIST;
                    } else { //Or for a professional scientist.
                        systemCode = APPROVED_PROFESSIONAL_SCIENTIST;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(systemCode);
        return systemCode; //Returns the code that indicates all the account's conditions.
    }

    /**
     * Gets the userId of the account from the database by using their unique username
     * @param username The username of the account we are looking to get the userId.
     * @return Returns the userId of the account if found and -1 if not found.
     */
    private Integer getUserIdByUsername(String username) {
        PreparedStatement getUserIdByUsernamePreparedStatement = null;
        int userId = -1;
        try {
            getUserIdByUsernamePreparedStatement = connection.prepareStatement(GET_USERID_BY_USERNAME_PREPAREDSTATEMENT_QUERY);
            getUserIdByUsernamePreparedStatement.setString(1, username);

            ResultSet rs = getUserIdByUsernamePreparedStatement.executeQuery();

            if(rs.next()){
            userId = rs.getInt("userId");}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
    /**
     * Gets the accessLevel of the account from the database by using their unique username
     * @param username The username of the account we are looking to get the userId.
     * @return Returns the accessLevel of the account if found and empty "" if not found.
     */
    private String getAccessLevelByUsername(String username){
        PreparedStatement getAccessLevelByUsernamePreparedStatement;
        String accessLevel = "";
        try{
            getAccessLevelByUsernamePreparedStatement = connection.prepareStatement(GET_ACCESSLEVEL_BY_USERNAME_PREPAREDSTATEMENT_QUERY);
            getAccessLevelByUsernamePreparedStatement.setString(1,username);

            ResultSet rs = getAccessLevelByUsernamePreparedStatement.executeQuery();

            if(rs.next()){
            accessLevel = rs.getString("accessLevel");}

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return accessLevel;
    }

    /**
     * Inserts the experiment data provided by the client (after processed in DataInsertionServlet) into the database
     * @param experimentData Data object containing all the experiment details provided by the user.
     */
    private void insertExperimentDataIntoDatabase(Data experimentData){
        try {
            PreparedStatement insertDataPreparedStatement = connection.prepareStatement(INSERT_DATA_PREPAREDSTATEMENT_QUERY);

            insertDataPreparedStatement.setInt(1,experimentData.getUserId());
            insertDataPreparedStatement.setString(2,experimentData.getUsername());
            insertDataPreparedStatement.setString(3,experimentData.getAccessLevel());
            insertDataPreparedStatement.setDouble(4,experimentData.getAreaOfPlate());
            insertDataPreparedStatement.setString(5,experimentData.getObjectsFound());
            insertDataPreparedStatement.setDouble(6,experimentData.getLongitude());
            insertDataPreparedStatement.setDouble(7,experimentData.getLatitude());
            insertDataPreparedStatement.setDouble(8,experimentData.getTemperature());
            insertDataPreparedStatement.setString(9,experimentData.getWeather());

            insertDataPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the unique experimentId of the experiment after it is inserted in the database.
     * Is used to save the images provided by the user accordingly based on the experimentId.
     * @param experimentData Data object containing the experiment details of the experiment we want to get the id.
     * @return Returns the experiments unique ID.
     */
    private String getExperimentId(Data experimentData){
        String experimentId = "";
        try {
            PreparedStatement getExperimentIDPreparedStatement = connection.prepareStatement(GET_EXPERIMENT_ID_PREPAREDSTATEMENT_QUERY);
            getExperimentIDPreparedStatement.setString(1,experimentData.getUsername());
            getExperimentIDPreparedStatement.setDouble(2,experimentData.getAreaOfPlate());
            getExperimentIDPreparedStatement.setString(3,experimentData.getObjectsFound());
            getExperimentIDPreparedStatement.setDouble(4,experimentData.getLongitude());
            getExperimentIDPreparedStatement.setDouble(5,experimentData.getLatitude());
            getExperimentIDPreparedStatement.setDouble(6,experimentData.getTemperature());
            getExperimentIDPreparedStatement.setString(7,experimentData.getWeather());

            ResultSet rs = getExperimentIDPreparedStatement.executeQuery();
            if(rs.next()){
                experimentId = String.valueOf(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return experimentId;
    }

    /**
     * Saves the image provided by the client for the experiment into the hard-drive.
     * The name of the image is the same as the ID of the experiment.
     * @param imageBytes Byte array which contains the image properties written in bytes so it can be used to store it in the database.
     * @param experimentData Data object which contains the experiments information.
     */
    private void insertImageToDatabase(byte[] imageBytes, Data experimentData){
        String experimentId = getExperimentId(experimentData); //Gets the ID of the experiment.
        String filePath = "src/main/webapp/images/" + File.separator + experimentId + ".jpg"; //Creates a specific filepath for that experiment.
        System.out.println(filePath);

        try {
            OutputStream outputStream = new FileOutputStream(filePath); //Creates an outputstream to that filepath
            outputStream.write(imageBytes); //Saves the image originally in bytes into that filepath.
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the experiments from the database.
     * @return ArrayList of Data objects in which the experiments are saved.
     */
    private ArrayList<Data> getExperimentsFromDatabase(){
        ArrayList<Data> experiments = new ArrayList<>(); //Creates an arraylist for the experiments.
        try {
            PreparedStatement getExperimentsPreparedStatement = connection.prepareStatement(GET_EXPERIMENT_DATA_PREPAREDSTATEMENT_QUERY);
            ResultSet resultSet = getExperimentsPreparedStatement.executeQuery(); //Execute the query which gives back all the experiments from the database.

            while(resultSet.next()){ //Check whether there are still experiments in the resultset.
                Integer experimentId = resultSet.getInt(1);
                Integer userId = resultSet.getInt(2);
                String username = resultSet.getString(3);
                String accessLevel = resultSet.getString(4);
                Double areaOfPlate = resultSet.getDouble(5);
                String objectsFound = resultSet.getString(6);
                Double longitude = resultSet.getDouble(7);
                Double latitude = resultSet.getDouble(8);
                Double temperature = resultSet.getDouble(9);
                String weather = resultSet.getString(10);

                String filePath = "src/main/webapp/images/" + File.separator + experimentId + ".jpg"; //Get the corresponding image from the hard-drive.
                File image = new File(filePath);
                byte[] imageByte = Files.readAllBytes(image.toPath()); //Turn the image into an array of bytes.

                experiments.add(new Data(experimentId,userId,username,accessLevel,areaOfPlate,objectsFound,longitude,latitude,temperature,weather,imageByte)); //Add the experiment to the arraylist
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return experiments; //Returns the arraylist which contains all the experiments
    }

    /**
     * Deletes an experiment from the database and their respective image from the hard-drive.
     * @param experimentId The ID of the experiment to be deleted.
     * @return Returns an integer code which indicated whether it was successful, the experiment is already deleted.
     */
    private int deleteExperimentId(Integer experimentId){
        Integer code = SUCCESSFULLY_MODIFIED_DATABASE;

        String filePath = "src/main/webapp/images/" + experimentId + ".jpg"; //Locates the path where the photo of that experiment should be.
        File file = new File(filePath); //opens the file in that path
        if(file.delete()){ //tries to delete that file.
            System.out.println("File deleted successfully");
        }else{
            System.out.println("Failed to delete the file");
        }

        try { //Executes the update that tries to delete the experiment
            PreparedStatement deleteExperimentIdPreparedStatement = connection.prepareStatement(DELETE_EXPERIMENT_PREPARED_STATEMENT_QUERY);
            deleteExperimentIdPreparedStatement.setInt(1,experimentId);

            int numberOfChanges = deleteExperimentIdPreparedStatement.executeUpdate(); //Saves the number of changes made by the update into a field.
            if(numberOfChanges == 0){ //If there were no changes made, notify the servlet
                code = RECORD_DOES_NOT_EXIST;
            }
        } catch (SQLException e) { //If there was a SQLexception caught, it means the input was invalid and the servlet is notified.
            code = UNABLE_TO_MODIFY_DATABASE;
            e.printStackTrace();
        }
        return code; //Returns the appropriate code.
    }

    /**
     * Updates a specific value of the experiment into a new one based on the experiments ID.
     * @param experimentId The ID of the experiment which is going to be updated.
     * @param field The field that will be updated.
     * @param updatedValue The new value of the field.
     * @return Returns an integer code that indicated whether the update was successful, the record does not exist or any other problems.
     */
    private int updateExperimentId(Integer experimentId,String field,String updatedValue){
        Integer code = SUCCESSFULLY_MODIFIED_DATABASE;
        PreparedStatement preparedStatement = null;
        try{
        switch (field){ //checks what field we are trying to update.
            case "areaOfPlate":
                preparedStatement = connection.prepareStatement(UPDATE_AREA_OF_PLATE_PREPAREDSTATEMENT_QUERY);
                preparedStatement.setDouble(1, Double.parseDouble(updatedValue));
                preparedStatement.setInt(2,experimentId);
                break;
            case "objectsFound":
                preparedStatement = connection.prepareStatement(UPDATE_OBJECTS_FOUND_PREPAREDSTATEMENT_QUERY);
                preparedStatement.setString(1,updatedValue);
                preparedStatement.setInt(2,experimentId);
                break;
            case "longitude":
                preparedStatement = connection.prepareStatement(UPDATE_LONGITUDE_PREPAREDSTATEMENT_QUERY);
                preparedStatement.setDouble(1, Double.parseDouble(updatedValue));
                preparedStatement.setInt(2,experimentId);
                break;
            case "latitude":
                preparedStatement = connection.prepareStatement(UPDATE_LATITUDE_PREPAREDSTATEMENT_QUERY);
                preparedStatement.setDouble(1, Double.parseDouble(updatedValue));
                preparedStatement.setInt(2,experimentId);
                break;
            case "temperature":
                preparedStatement = connection.prepareStatement(UPDATE_TEMPERATURE_PREPAREDSTATEMENT_QUERY);
                preparedStatement.setDouble(1, Double.parseDouble(updatedValue));
                preparedStatement.setInt(2,experimentId);
                break;
            case "weather":
                preparedStatement = connection.prepareStatement(UPDATE_WEATHER_PREPAREDSTATEMENT_QUERY);
                preparedStatement.setString(1, updatedValue);
                preparedStatement.setInt(2,experimentId);
                break;
            }
            int numberOfChanges = preparedStatement.executeUpdate(); //Executes the update and saves the number of changes.
            if(numberOfChanges == 0){ //if no changes were made, then the record doesn't exist
                code = RECORD_DOES_NOT_EXIST;
            }
        }catch (Exception e){ //if an exception is caught, then the input is invalid
            code = UNABLE_TO_MODIFY_DATABASE;
            e.printStackTrace();
        }
        return code;
    }

    /**
     * Encrypts the password entered by the user in the login form or when creating the account.
     * @param password The password that needs to be encrypted.
     * @return Returns the encrypted password.
     */
    private String simpleEncryptPassword(String password){ //returns the encrypted password according to a simple algorithm

        String encryptedPassword = "";
        for (int i = 0; i < password.length(); i++){
            encryptedPassword += (char)(password.charAt(i) + encryptionNumber);
        }
        return encryptedPassword;
    }

    /**
     * Decrypts the password.
     * @param password The encrypted password.
     * @return Returns the de-crypted password.
     */
    public static String decryptPassword(String password){ //public only for JUnit test purposes.
        //returns the encrypted password according to a simple algorithm

        String decryptedPassword = "";
        for (int i = 0; i < password.length(); i++){
            decryptedPassword += (char)(password.charAt(i) - encryptionNumber);
        }
        return decryptedPassword;
    }
}
