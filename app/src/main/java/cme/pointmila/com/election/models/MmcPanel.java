package cme.pointmila.com.election.models;

/**
 * Created by gauravbhoyar on 11/12/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MmcPanel {

    @SerializedName("MMCPanelName")
    @Expose
    private String mMCPanelName;
    @SerializedName("MMCPanelPDFlink")
    @Expose
    private String mMCPanelPDFlink;

    /**
     * @return The mMCPanelName
     */
    public String getMMCPanelName() {
        return mMCPanelName;
    }

    /**
     * @param mMCPanelName The MMCPanelName
     */
    public void setMMCPanelName(String mMCPanelName) {
        this.mMCPanelName = mMCPanelName;
    }

    /**
     * @return The mMCPanelPDFlink
     */
    public String getMMCPanelPDFlink() {
        return mMCPanelPDFlink;
    }

    /**
     * @param mMCPanelPDFlink The MMCPanelPDFlink
     */
    public void setMMCPanelPDFlink(String mMCPanelPDFlink) {
        this.mMCPanelPDFlink = mMCPanelPDFlink;
    }

}