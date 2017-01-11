package cme.pointmila.com;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by amoln on 16-08-2016.
 */
public class Validation {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@\" + \"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_REGEX = "\\d{10}";
    private static final String PASSWORD_REGEX ="((?=.*)(?=.*[a-z]).{1,12})";
    private static final String NAME_REGEX="[A-Za-z0-9\\.\\@\\s\\,\\&\\-\\(\\)\\*]+";
    private static final String PinCode_REGEX="\\d{6}";
    private static final String CME = "\\d{1}";

    private static final String Month_REGEX="^(\\d|(1[0-2]))$";
    private static final String Date_REGEX="(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\\\d\\\\d)\n";

    private static final String REQUIRED_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid E-mail Address";
    private static final String MOBILE_MSG = "please enter 10 digit Mobile number";
    private static final String PASSWORD_MSG="Please enter atleast 4 character Password";
    private static final String CONFIRMPASSW_MSG="Password is not matching";
    private static final String NAME_MSG="Please enter valid name";
    private static final String Month_MSG ="Please enter valid Month";
    private static final String PinCode_MSG="Pincode Should be 6 digits";
    private static final String Date_MSG="Please enter valid Date";
    private static final String Date_MSG1 = "Please enter the CME number";


    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    public static boolean isPincode(EditText editText,boolean required)
    {
        return isValid(editText,PinCode_REGEX,PinCode_MSG,required);
    }

    public static boolean isDate(EditText editText,boolean required)
    {
        return isValid(editText,Date_REGEX,Date_MSG,required);
    }

    public static boolean isMonth(EditText editText,boolean required)
    {
        return isValid(editText,Month_REGEX,Month_MSG,required);
    }
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, MOBILE_REGEX, MOBILE_MSG, required);
    }
    public static boolean isPassword(EditText editText, boolean required)
    {

        return isValid(editText,PASSWORD_REGEX,PASSWORD_MSG,required);
    }

    public static boolean isName(EditText editText,boolean required)
    {
        return isValid(editText,NAME_REGEX,NAME_MSG,required);
    }

    public static boolean isCME(EditText editText,boolean required)
    {
        return isValid(editText,CME,Date_MSG1,required);
    }

   /*public static boolean isDevice(EditText editText,boolean required)
    {
        return isValid(editText,Device_REGEX,Device_MSG,required);
    }*/

    public static boolean isPasswordMatching(EditText password, EditText confirmpassw)
    {
        if (password.getText().toString().equals(confirmpassw.getText().toString())) {
            confirmpassw.setError(null);
            return true;
        }
        else{

            confirmpassw.setError("Confirm password does not match.");
            return false;
        }
    }

    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }


}
