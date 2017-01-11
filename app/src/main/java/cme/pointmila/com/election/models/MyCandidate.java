package cme.pointmila.com.election.models;

/**
 * Created by gauravbhoyar on 02/12/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCandidate {

    @SerializedName("CandidateId")
    @Expose
    private String candidateId;
    @SerializedName("CandidateProfilePicstring")
    @Expose
    private String candidateProfilePicstring;
    @SerializedName("CandidateName")
    @Expose
    private String candidateName;

    //    9833
    @SerializedName("AllowDetailView")
    @Expose
    private boolean AalowDetailView;

    public boolean isAalowDetailView() {
        return AalowDetailView;
    }

    public void setAalowDetailView(boolean aalowDetailView) {
        AalowDetailView = aalowDetailView;
    }


    /**
     * @return The candidateId
     */
    public String getCandidateId() {
        return candidateId;
    }

    /**
     * @param candidateId The CandidateId
     */
    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    /**
     * @return The candidateProfilePicstring
     */
    public String getCandidateProfilePicstring() {
        return candidateProfilePicstring;
    }

    /**
     * @param candidateProfilePicstring The CandidateProfilePicstring
     */
    public void setCandidateProfilePicstring(String candidateProfilePicstring) {
        this.candidateProfilePicstring = candidateProfilePicstring;
    }

    /**
     * @return The candidateName
     */
    public String getCandidateName() {
        return candidateName;
    }

    /**
     * @param candidateName The CandidateName
     */
    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

}