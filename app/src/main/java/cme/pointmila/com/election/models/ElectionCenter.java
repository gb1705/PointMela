package cme.pointmila.com.election.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gauravbhoyar on 11/12/16.
 */

public class ElectionCenter implements Parcelable{

    @SerializedName("CenterLatitude")
    @Expose
    private String centerLatitude;
    @SerializedName("CenterLongitude")
    @Expose
    private String centerLongitude;
    @SerializedName("CenterDescription")
    @Expose
    private String centerDescription;

    /**
     *
     * @return
     * The centerLatitude
     */
    public String getCenterLatitude() {
        return centerLatitude;
    }

    /**
     *
     * @param centerLatitude
     * The CenterLatitude
     */
    public void setCenterLatitude(String centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    /**
     *
     * @return
     * The centerLongitude
     */
    public String getCenterLongitude() {
        return centerLongitude;
    }

    /**
     *
     * @param centerLongitude
     * The CenterLongitude
     */
    public void setCenterLongitude(String centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    /**
     *
     * @return
     * The centerDescription
     */
    public String getCenterDescription() {
        return centerDescription;
    }

    /**
     *
     * @param centerDescription
     * The CenterDescription
     */
    public void setCenterDescription(String centerDescription) {
        this.centerDescription = centerDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(centerLatitude);
        dest.writeString(centerLongitude);
        dest.writeString(centerDescription);
    }

    protected ElectionCenter(Parcel in) {
        this.centerLatitude = in.readString();
        this.centerLongitude = in.readString();
        this.centerDescription = in.readString();
    }

    public static final Parcelable.Creator<ElectionCenter> CREATOR = new Parcelable.Creator<ElectionCenter>() {
        public ElectionCenter createFromParcel(Parcel source) {
            return new ElectionCenter(source);
        }

        public ElectionCenter[] newArray(int size) {
            return new ElectionCenter[size];
        }
    };
}
