package cme.pointmila.com;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by amoln on 09-09-2016.
 */
public class OTPVerifyActivity extends AppCompatActivity
{
    Button Verify,ResendOTP;
    EditText Value,Value1,Value2,Value3;
    TextView Close;
    private ProgressDialog progressDialog;
    @Override
  protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_otpverification);
        Verify = (Button)findViewById(R.id.verifybtn);
        Value = (EditText)findViewById(R.id.editvalue1);
        Value1 = (EditText)findViewById(R.id.editvalue2);
        Value2 = (EditText)findViewById(R.id.editvalue3);
        Value3 = (EditText)findViewById(R.id.editvalue4);
        ResendOTP = (Button)findViewById(R.id.resendOtp);
        Close = (TextView)findViewById(R.id.close);

        Value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

               if (Value.getText().toString().length()==1)
               {
                   Value1.requestFocus();
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Value1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Value1.getText().toString().length()==1)
                {
                    Value2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Value2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (Value2.getText().toString().length()==1)
                {
                    Value3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(getApplicationContext(),OTPSuccessfulActivity.class);
               // startActivity(i);


                progressDialog = ProgressDialog.show(OTPVerifyActivity.this, "", "Please Wait....");
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                /*Thread timerthread = new Thread() {
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
                if (DetectConnection.isInternetAvailable(OTPVerifyActivity.this)) {
                    VerifyOTP();
                } else {
                    InternetMessage();
                }






            }
        });
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                OTPVerifyActivity.this.startActivity(i);
                OTPVerifyActivity.this.finish();
            }
        });

        ResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendOtp();
            }
        });
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


    protected void VerifyOTP()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"OTP/VerifyOTP");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("OTP",Value.getText().toString()+Value1.getText().toString()+Value2.getText().toString()+Value3.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("UserId",General.GUID));
                   // nameValuePairs.add(new BasicNameValuePair("Password",Password.getText().toString()));

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
                        JSONObject Objobject = (JSONObject)new JSONTokener(builder.toString()).nextValue();

                        String strResult= Objobject.getString("Verified").toString();
                        if (strResult.equals("true"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(),OTPSuccessfulActivity.class);
                                    OTPVerifyActivity.this.startActivity(i);
                                    OTPVerifyActivity.this.finish();
                                    //MsgBox();

                                }
                            });

                        }


                        else
                        {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


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

    protected void ResendOtp()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"OTP/ResendOTP");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("id",General.GUID));
                    // nameValuePairs.add(new BasicNameValuePair("Password",Password.getText().toString()));

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
                        JSONObject Objobject = (JSONObject)new JSONTokener(builder.toString()).nextValue();

                        String strResult= Objobject.getString("Success").toString();
                        if (strResult.equals("true"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    OtpSendMsg();
                                    //MsgBox();

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


    public void OtpSendMsg()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("You will get New Otp after a few second");
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

    public void Msgbox()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Invalid OTP number");
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
}
