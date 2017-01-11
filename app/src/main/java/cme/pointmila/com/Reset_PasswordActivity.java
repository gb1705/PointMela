package cme.pointmila.com;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

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

/**
 * Created by amoln on 14-11-2016.
 */
public class Reset_PasswordActivity extends ActionBarActivity
{
    Button ChangePasswordbtn;
    EditText Password;
    CheckBox Select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ChangePasswordbtn = (Button)findViewById(R.id.loginbtn);
        Password = (EditText)findViewById(R.id.firstnametxt);
        Select = (CheckBox)findViewById(R.id.check1);

        ChangePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordAPI();
            }
        });

        Select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


    }

    protected void ChangePasswordAPI()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl +"Access/ResetPassword");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserName",General.UserID));
                    nameValuePairs.add(new BasicNameValuePair("Password",Password.getText().toString()));

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
                        final JSONObject Objobject = (JSONObject)new JSONTokener(builder.toString()).nextValue();
                        final  String strSuccess = Objobject.getString("Success").toString();

                        try {

                            if (strSuccess.equals("true"))
                            {
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            changePasswordMsg();


                                        }
                                    });
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            else
                            {
                                // General.Token = Objobject.getString("Token").toString();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                         ErrorMsg();
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
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    protected void changePasswordMsg()
    {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Successfully Change Password, Please Login");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Mobile.setText("9594964665");
                    //Password.setText("jj");
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    Reset_PasswordActivity.this.startActivity(i);
                    Reset_PasswordActivity.this.finish();
                    dialog.cancel();
                   // progressDialog.cancel();
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

    protected void ErrorMsg()
    {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Error has been Occured,Please Try again later");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    //progressDialog.cancel();
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


    }
