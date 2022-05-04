package dhima.gerald.wump_final_project.Database;

/**
 * Interface which contains all the queries used in the application.
 */
public interface MySQLQueries {
    public String USE_WUMP_DATABASE = "use WUMP;";
    public String CHECK_FOR_USERNAME_PREPAREDSTATEMENT_QUERY = "SELECT * FROM WUMPUSERS WHERE username = ?;";
    public String CREATE_ACCOUNT_PREPAREDSTATEMENT_QUERY = "INSERT INTO WUMPUsers(firstName, lastName, institution, username,password,accessLevel,verificationStatus) VALUES (?,?,?,?,?,?,?);";
    public String APPROVE_PROFESSIONAL_SCIENTIST_PREPAREDSTATEMENT_QUERY = "UPDATE WUMPUsers SET verificationStatus = 'Approved' WHERE username = ?;";
    public String DECLINE_PROFESSIONAL_SCIENTIST_PREPAREDSTATEMENT_QUERY = "Update WUMPUsers SET verificationStatus = 'Declined', accessLevel = 'citizenScientist' WHERE username = ?;";
    public String INSERT_DATA_PREPAREDSTATEMENT_QUERY = "INSERT INTO WUMPDATA(userId,username,accessLevel,areaOfPlate,objectsFound,longitude,latitude,temperature,weather) VALUES (?,?,?,?,?,?,?,?,?);";
    public String GET_USERID_BY_USERNAME_PREPAREDSTATEMENT_QUERY = "SELECT userId FROM WUMPUSERS WHERE username = ?;";
    public String GET_ACCESSLEVEL_BY_USERNAME_PREPAREDSTATEMENT_QUERY = "SELECT accessLevel FROM WUMPUSERS WHERE username = ?;";
    public String GET_EXPERIMENT_ID_PREPAREDSTATEMENT_QUERY = "Select experimentId FROM WUMPData WHERE username = ? AND areaOfPlate = ? AND objectsFound = ? AND longitude = ? AND latitude = ? AND temperature = ? AND weather = ?;";
    public String GET_EXPERIMENT_DATA_PREPAREDSTATEMENT_QUERY = "SELECT * FROM WUMPData;";
    public String VALIDATE_LOGIN_QUERY = "Select * from WUMPUsers WHERE username = ? AND password = ?;";
    public String DELETE_EXPERIMENT_PREPARED_STATEMENT_QUERY = "DELETE FROM wumpData WHERE experimentId = ?";
    public String UPDATE_AREA_OF_PLATE_PREPAREDSTATEMENT_QUERY = "UPDATE wumpData SET areaOfPlate = ? WHERE experimentId = ?;";
    public String UPDATE_OBJECTS_FOUND_PREPAREDSTATEMENT_QUERY = "UPDATE wumpdata SET objectsFound = ? WHERE experimentId = ?;";
    public String UPDATE_LONGITUDE_PREPAREDSTATEMENT_QUERY = "UPDATE wumpdata SET longitude = ? WHERE experimentId = ?;";
    public String UPDATE_LATITUDE_PREPAREDSTATEMENT_QUERY = "UPDATE wumpData SET latitude = ? WHERE experimentId = ?;";
    public String UPDATE_TEMPERATURE_PREPAREDSTATEMENT_QUERY = "UPDATE wumpData SET temperature = ? WHERE experimentId = ?;";
    public String UPDATE_WEATHER_PREPAREDSTATEMENT_QUERY = "UPDATE wumpData SET weather = ? WHERE experimentId = ?;";

    public String CREATE_SQL_DATABASE = "CREATE DATABASE IF NOT EXISTS WUMP;";

    public String CREATE_DATA_TABLE = "create table WUMPDATA(" +
            "experimentId INT NOT NULL AUTO_INCREMENT," +
            "userId INT NOT NULL," +
            "username VARCHAR(50) NOT NULL," +
            "accessLevel VARCHAR(50) NOT NULL," +
            "areaOfPlate DOUBLE NOT NULL," +
            "objectsFound VARCHAR(50) NOT NULL," +
            "longitude DOUBLE NOT NULL," +
            "latitude DOUBLE NOT NULL," +
            "temperature DOUBLE NOT NULL," +
            "weather VARCHAR(20) NOT NULL," +
            "PRIMARY KEY (experimentId)"+
            //"photo LONGBLOB NOT NULL" +
            ");";

    public String CREATE_USERS_TABLE = "create table WUMPUSERS(" +
            "userId INT NOT NULL AUTO_INCREMENT," +
            "firstName VARCHAR(50) NOT NULL," +
            "lastName VARCHAR(50) NOT NULL," +
            "institution VARCHAR(50) NOT NULL," +
            "username VARCHAR(50) NOT NULL," +
            "password VARCHAR(50) NOT NULL," +
            "accessLevel VARCHAR(25) NOT NULL," +
            "verificationStatus VARCHAR(20) NOT NULL," +
            "PRIMARY KEY ( userId )" +
            ");";

}

