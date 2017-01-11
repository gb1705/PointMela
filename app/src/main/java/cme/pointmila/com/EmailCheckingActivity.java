package cme.pointmila.com;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amoln on 12-11-2016.
 */
public class EmailCheckingActivity extends ActionBarActivity
{

    Button Submitbtn;
    EditText EmailET;
    String MobileNumbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailchecking);
        EmailET = (EditText)findViewById(R.id.EmailET);
        Submitbtn = (Button)findViewById(R.id.loginbtn);

        Intent i = getIntent();
        General.UserID = i.getStringExtra("mobilno");

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmail();
            }
        });

    }
    protected void CheckEmail()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"Email/PasswordChangeRequest");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("Id",EmailET.getText().toString()));


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

                        //String strResult= Objobject.getString("Token").toString();
                        // String strUserid = Objobject.getString("UserId");
                        final  String strSuccess = Objobject.getString("Success").toString();

                        // final     String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true"))
                        {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        General.UserID = EmailET.getText().toString();

                                        Intent i = new Intent(getApplicationContext(),Email_OTP.class);

                                        EmailCheckingActivity.this.startActivity(i);
                                        EmailCheckingActivity.this.finish();
                                        //SaveProfileMsg();
                                      //  Msg();

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
        });
        thread.start();
    }

    public void ErrorMsg()
    {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Error has been Occured,Please Try again later");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                    //startActivity(i);
                    // Fragment enquiresFragment = new AddressTabFragment();
                    // FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    // fragmentTransaction.replace(R.id.containerView1, enquiresFragment, null);
                    // fragmentTransaction.commit();
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
