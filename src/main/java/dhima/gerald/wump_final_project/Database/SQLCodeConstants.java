package dhima.gerald.wump_final_project.Database;

/**
 * Interface that provides the CreateSQLTables app with the codes regarding different database/connection statuses.
 */
public interface SQLCodeConstants {
    public static int PROGRAM_STARTED = 0;
    public static int DRIVER_NOT_LOADED = 1;
    public static int WRONG_CREDENTIALS = 2;
    public static int CONNECTED = 3;
    public static int USER_FOUND_DATA_FOUND = 4;
    public static int USER_NOTFOUND_DATA_FOUND = 5;
    public static int USER_FOUND_DATA_NOTFOUND = 6;
    public static int USER_NOTFOUND_DATA_NOTFOUND = 7;
}
