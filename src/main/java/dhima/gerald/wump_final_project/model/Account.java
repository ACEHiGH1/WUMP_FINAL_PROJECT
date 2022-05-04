package dhima.gerald.wump_final_project.model;

import java.io.Serializable;

/**
 * This class serves as a model to send/receive data about the clients' account login or registration.
 */
public class Account implements Serializable {
    /**
     * Data Field: Stores the User ID of the account.
     */
    private String userId;
    /**
     * Data Field: Stores the First Name of the client's account.
     */
    private String firstName;
    /**
     * Data Field: Stores the Last Name of the client's account
     */
    private String lastName;
    /**
     * Data Field: Stores the name of the institution of the account.
     */
    private String institution;
    /**
     * Data Field: Stores the username of the client's account.
     */
    private String username;
    /**
     * Data Field: Stores the encrypted password of the client's account.
     */
    private String password;
    /**
     * Data Field: Stores the accessLevel of the client's account.
     */
    private String accessLevel;
    /**
     * Data Field: Stores the verification status of the client's account.
     */
    private String verificationStatus;
    /**
     * Data Field: Stores the account status (Logging in or Signing Up) of the client's account.
     */
    private Integer accountStatus;

    /**
     * Default Constructor.
     */
    public Account() {
    }

    /**
     * Multi-arg constructor used to create a model account with the information provided by the user when signing up.
     * @param firstName First Name provided by the client in the Sign-Up page.
     * @param lastName Last Name provided by the client in the Sign-Up page.
     * @param institution Name of institution provided by the client in the Sign-Up page.
     * @param username Username provided by the client in the Sign-Up page.
     * @param password The encrypted password of the password provided by the client in the Sign-Up page.
     * @param accessLevel The accessLevel the client has applied for.
     * @param verificationStatus The verification status of the application.
     * @param accountStatus The status of the account (Signing Up or Logging in)
     */
    public Account(String firstName, String lastName, String institution,String username, String password, String accessLevel, String verificationStatus,Integer accountStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.institution = institution;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.verificationStatus = verificationStatus;
        this.accountStatus = accountStatus;
    }

    /**
     * Multi-arg constructor used to create a model account with the information provided by the user when logging in.
     * @param username The username provided by the client when trying to log in.
     * @param password The encrypted password of the one provided by the client in the log in page.
     * @param accountStatus The account status (in this case Logging In) which indicates the server what actions to take.
     */
    public Account(String username, String password, Integer accountStatus) {
        this.username = username;
        this.password = password;
        this.accountStatus = accountStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", verificationStatus='" + verificationStatus + '\'' +
                '}';
    }
}
