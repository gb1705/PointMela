package cme.pointmila.com.election.models;

/**
 * Created by gauravbhoyar on 11/12/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MMCList {

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("mmcPanels")
    @Expose
    private List<MmcPanel> mmcPanels = null;

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
     * @return The mmcPanels
     */
    public List<MmcPanel> getMmcPanels() {
        return mmcPanels;
    }

    /**
     * @param mmcPanels The mmcPanels
     */
    public void setMmcPanels(List<MmcPanel> mmcPanels) {
        this.mmcPanels = mmcPanels;
    }

}
