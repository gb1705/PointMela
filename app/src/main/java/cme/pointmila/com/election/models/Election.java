package cme.pointmila.com.election.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Election {

@SerializedName("Success")
@Expose
private Boolean success;
@SerializedName("CutOfDate")
@Expose
private String cutOfDate;
@SerializedName("myCandidates")
@Expose
private List<MyCandidate> myCandidates = new ArrayList<MyCandidate>();

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
* The cutOfDate
*/
public String getCutOfDate() {
return cutOfDate;
}

/**
* 
* @param cutOfDate
* The CutOfDate
*/
public void setCutOfDate(String cutOfDate) {
this.cutOfDate = cutOfDate;
}

/**
* 
* @return
* The myCandidates
*/
public List<MyCandidate> getMyCandidates() {
return myCandidates;
}

/**
* 
* @param myCandidates
* The myCandidates
*/
public void setMyCandidates(List<MyCandidate> myCandidates) {
this.myCandidates = myCandidates;
}

}