package cme.pointmila.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by amoln on 25-10-2016.
 */
public class ListOfPoints extends ActionBarActivity {
    List<HashMap<String, String>> lstBooking;
    HashMap<String, String> hmBooking = null;
    ListView listView;
    JSONObject Objobject;

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cmepointlist_activity, null);
        lstBooking = new ArrayList<HashMap<String,String>>();
        listView = (ListView) view.findViewById(R.id.listview);
        GetListOfPoints();

        return view;


    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmepointlist_activity);
        lstBooking = new ArrayList<HashMap<String, String>>();
        listView = (ListView) findViewById(R.id.listview);


        GetListOfPointsForAllYear();
        // GetListOfPointsForAllYear();
    }


    protected void GetListOfPointsForAllYear() {
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

                                    String CourseType = jarray.getJSONObject(i).getString("CMECourseDescription");

                                    String DeliveryDate = jarray.getJSONObject(i).getString("CMEDate");
                                    String timestamp = DeliveryDate.replace("/Date(", "").replace(")/", "");
                                    Date createdon = new Date(Long.parseLong(timestamp));
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM- yyyy");
                                    String formattedDate = sdf.format(createdon);

                                    hmBooking.put("CMEPoints", Cmepoints);
                                    hmBooking.put("CMENumber", CmeNumber);
                                    hmBooking.put("PointID", PointID);
                                    hmBooking.put("CMECourse", CourseType);
                                    hmBooking.put("CMEDate", formattedDate);
                                    lstBooking.add(hmBooking);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListOfPointsAdapter pointslist = new ListOfPointsAdapter(ListOfPoints.this, lstBooking);
                                    listView.setAdapter(pointslist);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (DetectConnection.isInternetAvailable(ListOfPoints.this)) {
                                                HashMap<String, String> data = lstBooking.get(position);
                                                Intent i = new Intent();
                                                i.setClass(ListOfPoints.this, PointsEditing.class);
                                                i.putExtra("PointId", data.get("PointID"));
                                                ListOfPoints.this.startActivity(i);
                                                ListOfPoints.this.finish();
                                            } else {
                                                InternetMessage();
                                            }
                                        }
                                    });

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


        try {
            findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.takeScreenshot(ListOfPoints.this, v, "Edited the points for following");
                }
            });

        } catch (Exception e) {

        }
    }


    public void ErrorMsg() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void InternetMessage() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("No Internet Connection");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ProfileDashboardActivity.class);
        ListOfPoints.this.startActivity(i);
        ListOfPoints.this.finish();
    }


}
