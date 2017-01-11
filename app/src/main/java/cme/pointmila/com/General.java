package cme.pointmila.com;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by amoln on 05-10-2016.
 */
public class General
{
   //public static String baseurl="http://192.168.1.105/PointMilaAPi/";
   //public static String baseurl="http://corp.hirdhav.com/PointMilaAPI/";
    public static String baseurl="http://103.242.119.63:8090/";
//    public static String baseurl="http://52.172.185.34/";//cange to :
    public static String GUID ;
    public static String Student;
    public static String str;
    public static String ReferenceDate;
    public static String Street1;
    public static String Street2;
    public static String Pincode;
    public static String deviceid;
    public  static String SelectedState;
    public  static  String SelectedCity;
    public static  String UserID;
    public static String CheckBoxStatus;
    public static String CheckBoxStatus1;
    public static String SelectCheckBox;
    public static String SelectedFile;
    public static String ProfilePicture;
    public static String profileUsername;
    public static String UserTag;
  public static String ConvertDateFormat(Date currentDate)
  {
    SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
    return(  timeformat.format(currentDate));

  }


  }


