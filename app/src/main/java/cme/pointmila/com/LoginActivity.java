package cme.pointmila.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    TextView Registerbtn,ForgotPassw;
    Button Login;
    EditText Mobile,Password;
    private ProgressDialog progressDialog;
    boolean isAppInstalled=false;
    boolean checkLogin=false;
    public SharedPreferences appPreferences;
    public SharedPreferences settings;



    String strResult;
    String strstudent;
    String struserId ;
    String strOtp;
    String Username,password;
    CheckBox checkbox;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        settings = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        checkLogin = settings.getBoolean("checkLogin",false);
        if (checkLogin==false)
        {
        setContentView(R.layout.activity_login);

        General.deviceid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        }
        else
        {
            try {
             final String strUsername = settings.getString("username", "");
             final String strPassword = settings.getString("password", "");
                LoginUser(strUsername, strPassword);
                General.deviceid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        try {

            Registerbtn = (TextView) findViewById(R.id.registertxt);
            Login = (Button) findViewById(R.id.loginbtn);
            Mobile = (EditText) findViewById(R.id.EmailET);
            Password = (EditText) findViewById(R.id.passw);
            ForgotPassw = (TextView)findViewById(R.id.forgotpasswtxt);
            checkbox = (CheckBox)findViewById(R.id.check1);


            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    else
                    {
                        Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    }
                }
            });



            Registerbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);

                }
            });

            ForgotPassw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),EmailCheckingActivity.class);
                    i.putExtra("mobilno",Username);
                    LoginActivity.this.startActivity(i);
                    LoginActivity.this.finish();
                }
            });

            LoginView();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    public void InternetMessage()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("No Internet Connection");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }

    private  void LoginView() {

        Mobile.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(Mobile, true);

            }
        });
        // etMobileNumber = (EditText) findViewById(R.id.mobileET);
        Password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(Password, true);

            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkValidation()) {
                    progressDialog = ProgressDialog.show(LoginActivity.this, "", "Loading");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                  /*  Thread timerthread = new Thread() {
                        public void run() {
                            try {
                                sleep(5000);
                            } catch (Exception e) {
                                Log.e("tag", e.getMessage());
                            }
                            progressDialog.dismiss();

                        }
                    };
                    timerthread.start();*/
                    if (DetectConnection.isInternetAvailable(LoginActivity.this)) {

                        LoginUser(Mobile.getText().toString(), Password.getText().toString());


                    } else {
                        InternetMessage();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please Check the fields", Toast.LENGTH_LONG).show();
                }

            }//
        });


    }




    private  boolean checkValidation()
    {
        boolean ret = true;
        if (!Validation.isName(Mobile,true))ret=false;
        // if(!Validation.isPincode(Pincode,true))ret=false;
        if (!Validation.isName(Password,true))ret=false;

        return  ret;
    }



    private void LoginUser(String strUsername, String strPassword)
    {
        final String tmpUsername = strUsername;
        final String tmpPassword = strPassword;
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl +"Access/VerifyCredentials");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserName",tmpUsername));
                    nameValuePairs.add(new BasicNameValuePair("Password",tmpPassword));
                    nameValuePairs.add(new BasicNameValuePair("Deviceid",General.deviceid));
                    nameValuePairs.add(new BasicNameValuePair("FromAndroid","true"));

                    try
                    {
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
                        StringBuffer builder = new StringBuffer();
                        for (String line = null;(line = reader.readLine())!=null;)
                        {
                            builder.append(line).append("\n");
                        }
                      final  JSONObject Objobject = (JSONObject)new JSONTokener(builder.toString()).nextValue();

                         strResult= Objobject.getString("IsAuthorized").toString();
                         strstudent = Objobject.getString("IsStudent");
                         struserId = Objobject.getString("UserID");
                         strOtp = Objobject.getString("OTPverified");

                        try {


                           // settings = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                             Username=tmpUsername;
                             password = tmpPassword;
                           /* SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username",Username);
                            editor.putString("password",password);
                           editor.putBoolean("checkLogin",true);
                           editor.commit();*/
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if (strResult.equals("true")&& strstudent.equals("true")&& !struserId.equals("")&& strOtp.equals("true"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("username",Username);
                                    editor.putString("password",password);
                                    editor.putBoolean("checkLogin",true);
                                    editor.commit();
                                    try {
                                        General.ProfilePicture = Objobject.getString("ProfilePicture");
                                        General.profileUsername = Objobject.getString("ProfileUserName");

                                        General.GUID= Objobject.getString("UserID");
                                        General.Student = Objobject.getString("IsStudent");
                                        General.UserTag = Objobject.getString("UserTag");
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    if(checkLogin == false) {
                                        MsgBox();
                                    }
                                    else
                                    {
                                        Intent i = new Intent(getApplicationContext(), ProfileDashboardActivity.class);
                                        LoginActivity.this.startActivity(i);
                                        LoginActivity.this.finish();

                                    }

                                }
                            });

                        }

                        else if (strResult.equals("true")&& strstudent.equals("false")&& !struserId.equals("")&& strOtp.equals("true"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("username",Username);
                                    editor.putString("password",password);
                                    editor.putBoolean("checkLogin",true);
                                    editor.commit();
                                    try {
                                        General.ProfilePicture = Objobject.getString("ProfilePicture");
                                        General.profileUsername = Objobject.getString("ProfileUserName");
                                        General.GUID= Objobject.getString("UserID");
                                        General.UserTag = Objobject.getString("UserTag");
                                        General.Student = Objobject.getString("IsStudent");
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    if(checkLogin == false) {
                                        MsgBox();
                                    }
                                    else {

                                          Intent i = new Intent(getApplicationContext(), ProfileDashboardActivity.class);
                                          LoginActivity.this.startActivity(i);
                                          LoginActivity.this.finish();
                                    }


                                }
                            });
                        }
                        else if(strResult.equals("true")&& strstudent.equals("true")&& !struserId.equals("")&& strOtp.equals("false"))
                        {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username",Username);
                            editor.putString("password",password);
                            editor.putBoolean("checkLogin",true);
                            editor.commit();
                            General.ProfilePicture = Objobject.getString("ProfilePicture");
                            General.profileUsername = Objobject.getString("ProfileUserName");
                            General.UserTag = Objobject.getString("UserTag");
                            General.Student = Objobject.getString("IsStudent");
                            General.GUID = Objobject.getString("UserID");
                            Intent i = new Intent(getApplicationContext(), OTPVerifyActivity.class);
                            LoginActivity.this.startActivity(i);
                            LoginActivity.this.finish();

                        }
                        else if (strResult.equals("true")&& strstudent.equals("false")&& !struserId.equals("")&& strOtp.equals("false"))
                        {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username",Username);
                            editor.putString("password",password);
                            editor.putBoolean("checkLogin",true);
                            editor.commit();
                            General.ProfilePicture = Objobject.getString("ProfilePicture");
                            General.profileUsername = Objobject.getString("ProfileUserName");
                            General.Student = Objobject.getString("IsStudent");
                            General.GUID = Objobject.getString("UserID");
                            General.UserTag = Objobject.getString("UserTag");
                            Intent i = new Intent(getApplicationContext(), OTPVerifyActivity.class);
                            LoginActivity.this.startActivity(i);
                            LoginActivity.this.finish();

                        }

                        else
                        {
                           // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                  /*  if (checkLogin== true)
                                    {
                                        //setContentView(R.layout.activity_login);
                                        General.deviceid = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                                        LoginUser(tmpUsername, tmpPassword);


                                    }
                                    else
                                    {
                                        Msgbox();
                                    }*/
                                    Msgbox();


                                }
                            });
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void MsgBox()
    {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Successfully Login!!!");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Mobile.setText("9594964665");
                    //Password.setText("jj");
                    Intent i = new Intent(getApplicationContext(), ProfileDashboardActivity.class);
                    LoginActivity.this.startActivity(i);
                    LoginActivity.this.finish();
                    dialog.cancel();
                    progressDialog.cancel();
                }
            });
            AlertDialog alert = dlgAlert.create();
            alert.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void Msgbox()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Invalid User Id or Password");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }

  /*  @Override
    public void onPause()
    {
        super.onPause();
        savePreferences();
    }*/

  /*  @Override
    public void onResume()
    {
        super.onResume();
        loadPreferences();
    }*/

    /*private void savePreferences()
    {
        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            // Edit and commit
            UnameValue = Mobile.getText().toString();
            PasswordValue = Password.getText().toString();
            editor.putString(PREF_UNAME, UnameValue);
            editor.putString(PREF_PASSWORD, PasswordValue);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

   /* private void loadPreferences() {

        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE);

            // Get value
            UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
            PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
            Mobile.setText(UnameValue);
            Password.setText(PasswordValue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }*/

}
