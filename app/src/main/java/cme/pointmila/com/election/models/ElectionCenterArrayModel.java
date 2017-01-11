package cme.pointmila.com.election.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gauravbhoyar on 11/12/16.
 */

public class ElectionCenterArrayModel {
    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("electionCenter")
    @Expose
    private ArrayList<ElectionCenter> electionCenter = null;

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The Success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The electionCenter
     */
    public ArrayList<ElectionCenter> getElectionCenter() {
        return electionCenter;
    }

    /**
     *
     * @param electionCenter
     * The electionCenter
     */
    public void setElectionCenter(ArrayList<ElectionCenter> electionCenter) {
        this.electionCenter = electionCenter;
    }
}
