package dhima.gerald.wump_final_project.model;

import java.io.Serializable;

/**
 * This class serves as a model to modify experiment's data.
 */
public class ModifyExperiment implements Serializable {
    /**
     * Data Field: Stores the experiment ID of the experiment the client wants to modify.
     */
    private Integer experimentId;
    /**
     * Data Field: Stores the field that the client wants to update in the experiment's data.
     */
    private String updateField;
    /**
     * Data Field: Stores the new value of the field the client wants to update.
     */
    private String updatedValue;

    /**
     * Constructor that is used to create an experiment model that deletes a specific experiment from the database.
     * @param experimentId The ID of the experiment the client wants to delete.
     */
    public ModifyExperiment(Integer experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * Multi-arg constructor that creates a model for the experiment the client wants to update.
     * @param experimentId The experiment ID of the experiment the client wants to delete.
     * @param updateField The field the client wants to update.
     * @param updatedValue The new value the client wants to insert.
     */
    public ModifyExperiment(Integer experimentId, String updateField, String updatedValue) {
        this.experimentId = experimentId;
        this.updateField = updateField;
        this.updatedValue = updatedValue;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public String getUpdateField() {
        return updateField;
    }

    public void setUpdateField(String updateField) {
        this.updateField = updateField;
    }

    public String getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(String updatedValue) {
        this.updatedValue = updatedValue;
    }
}
