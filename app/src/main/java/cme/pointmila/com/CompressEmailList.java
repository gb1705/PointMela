package cme.pointmila.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 15-11-2016.
 */
public class CompressEmailList extends ActionBarActivity {
    List<HashMap<String, String>> lstBooking;
    HashMap<String, String> hmBooking = null;
    ListView listView;
    JSONObject Objobject;
    CheckBox checkbox, checkbox1;
    Button Ok;
    String GetList;
    String CheckInfo;
    ProgressDialog progressDialog;

    //String CheckBoxStatus,CheckBoxStatus1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compressemail_list);
        lstBooking = new ArrayList<HashMap<String, String>>();
        listView = (ListView) findViewById(R.id.listview);
        checkbox = (CheckBox) findViewById(R.id.check);
        checkbox1 = (CheckBox) findViewById(R.id.check1);
        Ok = (Button) findViewById(R.id.okbtn);
        General.CheckBoxStatus1 = "0";
        General.CheckBoxStatus = "0";
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox.isChecked()) {
                    General.CheckBoxStatus = "1";
                }

            }
        });

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox1.isChecked()) {
                    General.CheckBoxStatus1 = "1";
                }

            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (General.SelectedFile == null) {
                        NoPointMsg();
                    }

                    General.SelectedFile = General.SelectedFile.replace("null", "");
                    GetList = General.CheckBoxStatus + "," + General.CheckBoxStatus1 + "," + General.SelectedFile;

                    progressDialog = ProgressDialog.show(CompressEmailList.this, "", "Please wait");
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

                    if (DetectConnection.isInternetAvailable(CompressEmailList.this)) {
                        CompressEmailFile();
                    } else {
                        InternetMessage();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        GetCompressEmailList();
        // GetListOfPointsForAllYear();
    }

    public void InternetMessage() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("No Internet Connection");
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


    protected void GetCompressEmailList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "CMEPoints/GetCMEPointsListForAllyears");
                    // httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId", General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("ReferenceDate", General.ReferenceDate));

                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuffer builder = new StringBuffer();
                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");
                        }
                        Objobject = (JSONObject) new JSONTokener(builder.toString()).nextValue();

                        //String strResult= Objobject.getString("Token").toString();
                        // String strUserid = Objobject.getString("UserId");
                        final String strSuccess = Objobject.getString("Success").toString();

                        //  final     String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true")) {
                            JSONArray jarray = new JSONArray(Objobject.getString("PointList").toString());
                            //  lstBooking = new ArrayList<HashMap<String, String>>();


                            for (int i = 0; i < jarray.length(); i++) {
                                hmBooking = new HashMap<String, String>();

                                try {
                                    String Cmepoints = jarray.getJSONObject(i).getString("CMEPoints");
                                    String CmeNumber = jarray.getJSONObject(i).getString("CMENumber");
                                    String PointID = jarray.getJSONObject(i).getString("PointsId");
                                    String CertificateID = jarray.getJSONObject(i).getString("CMECertificateID");

                                    String CourseType = jarray.getJSONObject(i).getString("CMECourseDescription");

                                    String DeliveryDate = jarray.getJSONObject(i).getString("CMEDate");
                                    String timestamp = DeliveryDate.replace("/Date(", "").replace(")/", "");
                                    Date createdon = new Date(Long.parseLong(timestamp));
                                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                                    String formattedDate = sdf.format(createdon);

                                    hmBooking.put("CMEPoints", Cmepoints);
                                    hmBooking.put("CMENumber", CmeNumber);
                                    hmBooking.put("PointID", PointID);
                                    hmBooking.put("CMECourse", CourseType);
                                    hmBooking.put("CMEDate", formattedDate);
                                    hmBooking.put("CMECertificateid", CertificateID);
                                    lstBooking.add(hmBooking);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListOfEmailCompressAdapter pointslist = new ListOfEmailCompressAdapter(CompressEmailList.this, lstBooking);
                                    listView.setAdapter(pointslist);


                                }
                            });

                        } else {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ErrorMsg();
                                    // MsgBox(strResult);
                                    // MsgBox();

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    protected void CompressEmailFile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "Email/MailCompressedFileRequest");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId", General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("PictureRequestList", GetList));


                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuffer builder = new StringBuffer();
                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");
                        }
                        final JSONObject Objobject = (JSONObject) new JSONTokener(builder.toString()).nextValue();

                        //String strResult= Objobject.getString("Token").toString();
                        // String strUserid = Objobject.getString("UserId");
                        final String strSuccess = Objobject.getString("Success").toString();

                        // final     String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true")) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        SaveMessage();
                                        //   Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                                        //  CompressEmailList.this.startActivity(i);
                                        // CompressEmailList.this.finish();

                                        //SaveProfileMsg();
                                        //  Msg();


                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ErrorMsg();

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    public void SaveMessage() {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Your request have been Send Successfully ");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(getApplicationContext(), ProfileDashboardActivity.class);
                    CompressEmailList.this.startActivity(i);
                    CompressEmailList.this.finish();
                    dialog.cancel();
                    progressDialog.cancel();
                }
            });
            AlertDialog alert = dlgAlert.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ErrorMsg() {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Error has been Occured,Please Try again later");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    progressDialog.cancel();
                }
            });
            AlertDialog alert = dlgAlert.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void NoPointMsg() {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Your Files Will not be Compressed because of No points Added");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    try {
                        progressDialog.cancel();
                    } catch (Exception e) {

                    }

                }
            });
            AlertDialog alert = dlgAlert.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
