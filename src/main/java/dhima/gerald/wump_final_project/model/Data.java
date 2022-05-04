package dhima.gerald.wump_final_project.model;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * This class serves as a model to send/receive data about the clients' experiment insertion/display.
 */
public class Data implements Serializable {
    /**
     * Data Field: Stores the ID of the experiment.
     */
    private Integer experimentId;
    /**
     * Data Field: Stores the User ID of client who inserted the experiment's data.
     */
    private Integer userId;
    /**
     * Data Field: Stores the username of the client who inserted the experiment's data.
     */
    private String username;
    /**
     * Data Field: Stores the Access Level of the client who inserted the experiment's data.
     */
    private String accessLevel;
    /**
     * Data Field: Stores the area of plate of an experiment.
     */
    private Double areaOfPlate;
    /**
     * Data Field: Stores the objects found in an experiment.
     */
    private String objectsFound;
    /**
     * Data Field: Stores the longitude of the place where an experiment was taken.
     */
    private Double longitude;
    /**
     * Data Field: Stores the latitude of the place where the experiment was taken.
     */
    private Double latitude;
    /**
     * Data Field: Stores the temperature of the place where the experiment was taken.
     */
    private Double temperature;
    /**
     * Data Field: Stores the weather information of the place where the experiment was taken during that moment.
     */
    private String weather;
    /**
     * Data Field: Stores the image entered by the user in bytes.
     */
    private byte[] byteImage;

    /**
     * Default Constructor.
     */
    public Data() {
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getAreaOfPlate() {
        return areaOfPlate;
    }

    public void setAreaOfPlate(Double areaOfPlate) {
        this.areaOfPlate = areaOfPlate;
    }

    public String getObjectsFound() {
        return objectsFound;
    }

    public void setObjectsFound(String objectsFound) {
        this.objectsFound = objectsFound;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    /**
     * Multi-arg constructor used in the DataInsertionServlet to create an experiment model and send it to the server.
     * This model contains the info provided by the user when trying to insert the experiment in the database.
     * @param username The username of the client who is trying to insert an experiment.
     * @param areaOfPlate The area of the plate used in the experiment.
     * @param objectsFound The objects found in the experiment.
     * @param longitude The longitude of the place where the experiment was held.
     * @param latitude The latitude of the place where the experiment was held.
     * @param temperature The temperature of the place when the experiment was held.
     * @param weather The weather information when the experiment was held.
     * @param byteImage The image entered by the client, stored in bytes.
     */
    public Data(String username, Double areaOfPlate, String objectsFound, Double longitude, Double latitude, Double temperature, String weather,byte[] byteImage) {
        this.username = username;
        this.areaOfPlate = areaOfPlate;
        this.objectsFound = objectsFound;
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.weather = weather;
        this.byteImage = byteImage;
    }

    /*public Data(Integer userId, String accessLevel, String username, Double areaOfPlate, String objectsFound, Double longitude, Double latitude, Double temperature, String weather) {
        this.userId = userId;
        this.username = username;
        this.areaOfPlate = areaOfPlate;
        this.objectsFound = objectsFound;
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.weather = weather;
    }*/

    /**
     * Multi-arg constructor that creates an experiment model that stores all the information registered in the database.
     * @param experimentId The experiment ID of the experiment.
     * @param userId The User ID of the client who inserted it.
     * @param username The username of the client who inserted it.
     * @param accessLevel The access level of the client who inserted it.
     * @param areaOfPlate The area of the plate that was used in the experiment.
     * @param objectsFound The objects found in the experiment.
     * @param longitude The longitude of the place where the experiment was taken.
     * @param latitude The latitude of the place where the experiment was taken.
     * @param temperature The temperature of the place where the experiment was taken.
     * @param weather The weather at the moment the experiment was taken.
     * @param byteImage The image inserted by the user in bytes.
     */
    public Data(Integer experimentId, Integer userId, String username, String accessLevel, Double areaOfPlate, String objectsFound, Double longitude, Double latitude, Double temperature, String weather, byte[] byteImage) {
        this.experimentId = experimentId;
        this.userId = userId;
        this.username = username;
        this.accessLevel = accessLevel;
        this.areaOfPlate = areaOfPlate;
        this.objectsFound = objectsFound;
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.weather = weather;
        this.byteImage = byteImage;
    }

    public Data(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    @Override
    public String toString() {
        return "Data{" +
                "experimentId=" + experimentId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", areaOfPlate=" + areaOfPlate +
                ", objectsFound='" + objectsFound + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", temperature=" + temperature +
                ", weather='" + weather + '\'' +
                ", byteImage=" + Arrays.toString(byteImage) +
                '}';
    }
}
