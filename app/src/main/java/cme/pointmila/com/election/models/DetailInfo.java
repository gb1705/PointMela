package cme.pointmila.com.election.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailInfo {

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("MMCNo")
    @Expose
    private String mMCNo;
    @SerializedName("MMCDistric")
    @Expose
    private String mMCDistric;
    @SerializedName("PhNo")
    @Expose
    private String phNo;
    @SerializedName("DetailTest")
    @Expose
    private String detailTest;
    @SerializedName("PDFLink")
    @Expose
    private String pDFLink;
    @SerializedName("YoutubeLink")
    @Expose
    private String youtubeLink;

    /**
     * @return The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The Success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The qualification
     */
    public String getQualification() {
        return qualification;
    }

    /**
     * @param qualification The Qualification
     */
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    /**
     * @return The mMCNo
     */
    public String getMMCNo() {
        return mMCNo;
    }

    /**
     * @param mMCNo The MMCNo
     */
    public void setMMCNo(String mMCNo) {
        this.mMCNo = mMCNo;
    }

    /**
     * @return The mMCDistric
     */
    public String getMMCDistric() {
        return mMCDistric;
    }

    /**
     * @param mMCDistric The MMCDistric
     */
    public void setMMCDistric(String mMCDistric) {
        this.mMCDistric = mMCDistric;
    }

    /**
     * @return The phNo
     */
    public String getPhNo() {
        return phNo;
    }

    /**
     * @param phNo The PhNo
     */
    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    /**
     * @return The detailTest
     */
    public String getDetailTest() {
        return detailTest;
    }

    /**
     * @param detailTest The DetailTest
     */
    public void setDetailTest(String detailTest) {
        this.detailTest = detailTest;
    }

    /**
     * @return The pDFLink
     */
    public String getPDFLink() {
        return pDFLink;
    }

    /**
     * @param pDFLink The PDFLink
     */
    public void setPDFLink(String pDFLink) {
        this.pDFLink = pDFLink;
    }

    /**
     * @return The youtubeLink
     */
    public String getYoutubeLink() {
        return youtubeLink;
    }

    /**
     * @param youtubeLink The YoutubeLink
     */
    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

}