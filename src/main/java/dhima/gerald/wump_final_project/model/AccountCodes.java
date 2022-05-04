package dhima.gerald.wump_final_project.model;

/**
 * Interface which contains the codes that organize the communication server/client.
 */
public interface AccountCodes {
    public final Integer LOGGING_IN = 1;
    public final Integer SIGNING_UP = 2;

    public final Integer SENDING_ACCOUNT_OBJECT = 1000;
    public final Integer SENDING_EXPERIMENT_DATA_OBJECT = 1001;
    public final Integer GETTING_EXPERIMENTS_DATA = 1002;

    public final Integer ALREADY_USED_USERNAME = 4000;
    public final Integer INVALID_ACCOUNT_CREDENTIALS = 4001;
    public final Integer PENDING_PROFESSIONAL_SCIENTIST_REVIEW = 4002;
    public final Integer DECLINED_PROFESSIONAL_SCIENTIST = 4003;
    public final Integer APPROVED_PROFESSIONAL_SCIENTIST = 4004;
    public final Integer CITIZEN_SCIENTIST = 4005;

    public final Integer SUCCESSFULLY_CREATED_CITIZEN_SCIENTIST_ACCOUNT = 5000;
    public final Integer SUCCESSFULLY_APPLIED_PROFESSIONAL_SCIENTIST_ACCOUNT = 5001;
    public final Integer SUCCESSFULLY_ENTERED_DATA = 5005;

    public final Integer VIEWING_DATABASE_AFTER_DELETING_EXPERIMENT = 6000;
    public final Integer VIEWING_DATABASE_AFTER_UPDATING_EXPERIMENT = 6001;

    public final Integer UNABLE_TO_MODIFY_DATABASE = 7000;
    public final Integer SUCCESSFULLY_MODIFIED_DATABASE = 7001;
    public final Integer RECORD_DOES_NOT_EXIST = 7002;
}
