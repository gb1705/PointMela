package cme.pointmila.com;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amoln on 07-11-2016.
 */
public class ClinicalAddressupdate extends ActionBarActivity
{
    EditText Street1,Street2,Pincode;
    AutoCompleteTextView StateAutoET,CityAutoET;
    StateAdapter adapter;
    Integer SelectedStateID;
    String SelectedStateName;
    Integer SelectedCityID;
    String SelectedCityName;
    State objMC;
    City objMC1;
    JSONObject Objobject;
    CityAdapter adapter1;
    Button OK,Delete;
    String AddressID,AddStreet1,AddressStreet2,Addressstate,AddressCity,AddressCityID,AddressPincode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicaladdress_update);
        Street1 = (EditText)findViewById(R.id.street1);
        Street2 = (EditText)findViewById(R.id.street2);
        StateAutoET = (AutoCompleteTextView)findViewById(R.id.state);
        CityAutoET = (AutoCompleteTextView)findViewById(R.id.city);
        Pincode = (EditText)findViewById(R.id.pincode);
        Delete = (Button)findViewById(R.id.deletebtn);

        OK = (Button)findViewById(R.id.okbtn);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteClinicalAddress();
            }
        });


        try {
            Intent i = getIntent();
            AddressID = i.getStringExtra("AddressId");
            AddStreet1 = i.getStringExtra("addressstreet1");
            AddressStreet2 = i.getStringExtra("addressstreet2");
            Addressstate = i.getStringExtra("addressstate");
            AddressCity = i.getStringExtra("addresscity");
            AddressCityID = i.getStringExtra("addresscityId");
            AddressPincode = i.getStringExtra("pincode");
            Street1.setText(AddStreet1);
            Street2.setText(AddressStreet2);
            StateAutoET.setText(Addressstate);
            CityAutoET.setText(AddressCity);
            Pincode.setText(AddressPincode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateClinicalAddress();
            }
        });


        SelectState();
    }

    protected void UpdateClinicalAddress()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl +"DoctorProfile/UpdateClinicAddress");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("Street1",Street1.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Street2",Street2.getText().toString()));
                    if (SelectedCityID==null)
                    {
                        nameValuePairs.add(new BasicNameValuePair("CityId",AddressCityID));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("CityId", Integer.toString(SelectedCityID)));
                    }
                    nameValuePairs.add(new BasicNameValuePair("PinCode",Pincode.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("userId",General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("addressId",AddressID));

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
                        String strSuccess = Objobject.getString("Success").toString();

                        if (strSuccess.equals("true"))
                        {
                            try
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        UpdateSuccessfulMsg();

                                      //  Fragment enquiresFragment = new AddressTabFragment();
                                      //  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                      //  fragmentTransaction.replace(R.id.containerView, enquiresFragment, null);
                                        // fragmentTransaction.add(R.id.containerView,enquiresFragment).addToBackStack("fragBack").commit();
                                    //    fragmentTransaction.commit();
                                       // Intent i = new Intent(getApplicationContext(),AddressTabFragment.class);
                                        //startActivity(i);
                                    }
                                });
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

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
    protected  void SelectState()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"pointmilamaster/GetListofStates");


                    try
                    {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; )
                        {
                            builder.append(line).append("\n");

                        }
                        final JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();
                        final String strResult = objObject.getString("stateList").toString();
                        strResult.toString();
                        if(strResult.equals("null"))
                        {
                            //  Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            // startActivity(intent);
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("stateList").toString());

                                            final List<State> list = new ArrayList<State>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC = new State(jarray.getJSONObject(i).getString("StateName"), jarray.getJSONObject(i).getInt("Stateid"));
                                                list.add(objMC);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter = new StateAdapter(ClinicalAddressupdate.this, R.layout.activity_residentialaddress, R.id.txt, list);
                                                    // adapter.appContext = getApplicationContext();
                                                    // adapter.tmpActivity = ResidentialAddress.this;
                                                    // spItems.setAdapter(adapter);

                                                    StateAutoET.setAdapter(adapter);
                                                    StateAutoET.setThreshold(1);
                                                }
                                            });

                                            try {

                                                StateAutoET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        State temp = list.get(position);
                                                        SelectedStateID= temp.getId();
                                                        SelectedStateName = temp.getName();

                                                        // StateId = list.get(position)
                                                        SelectCity();
                                                    }
                                                });

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

    protected void SelectCity()
    {
        Thread thread = new  Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetListofCity/"+SelectedStateID);

                    try
                    {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; )
                        {
                            builder.append(line).append("\n");

                        }
                        final JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();
                        final String strResult = objObject.getString("cityList").toString();
                        strResult.toString();
                        if(strResult.equals("null"))
                        {
                            //  Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            // startActivity(intent);
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("cityList").toString());

                                            final List<City> list = new ArrayList<City>();

                                            //  DistrictList = new ArrayList<HashMap<String,String>>();//

                                            // lstStores = new ArrayList<HashMap<String,String>>();
                                            for (int i = 0; i < jarray.length(); i++)
                                            {

                                                objMC1 = new City(jarray.getJSONObject(i).getString("CityName"), jarray.getJSONObject(i).getInt("CityId"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC1);


                                                // MedicalCouncilDistrict = new HashMap<String, String>();//


                                            }

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try
                                                    {
                                                        adapter1 = new CityAdapter(ClinicalAddressupdate.this, R.layout.activity_residentialaddress, R.id.txt, list);
                                                        CityAutoET.setAdapter(adapter1);
                                                        CityAutoET.setThreshold(1);

                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });


                                            try {


                                                CityAutoET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        City temp = list.get(position);
                                                        SelectedCityID= temp.getId();
                                                        SelectedCityName = temp.getName();
                                                    }
                                                });

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

    public void UpdateSuccessfulMsg()
    {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Successfully Updated");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                    startActivity(i);
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

    protected void DeleteClinicalAddress()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"DoctorProfile/DeleteClinicAddress/" + AddressID);
                    // httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    try
                    {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
                        StringBuffer builder = new StringBuffer();
                        for (String line = null;(line = reader.readLine())!=null;)
                        {
                            builder.append(line).append("\n");
                        }
                        Objobject = (JSONObject)new JSONTokener(builder.toString()).nextValue();

                        //String strResult= Objobject.getString("Token").toString();
                        // String strUserid = Objobject.getString("UserId");
                        final  String strSuccess = Objobject.getString("Success").toString();
                        //  final     String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true"))
                        {
                            //  JSONArray jarray = new JSONArray(Objobject.getString("Data").toString());
                            //  lstBooking = new ArrayList<HashMap<String, String>>();


                            // for (int i = 0; i < jarray.length(); i++) {
                            //  hmBooking = new HashMap<String, String>();

                            try {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DeleteMsg();
                                      //  Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                                        //startActivity(i);

                                    }
                                });


                                //  String PointID = jarray.getJSONObject(i).getString("PointsId");

                                // String CourseType = jarray.getJSONObject(i).getString("CMECourseId");

                                // String DeliveryDate = jarray.getJSONObject(i).getString("CMEDate");
                                // String timestamp = DeliveryDate.replace("/Date(", "").replace(")/", "");
                                //  Date createdon = new Date(Long.parseLong(timestamp));
                                //  SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                                // String formattedDate = sdf.format(createdon);
                                //  String CmePoints = jarray.getJSONObject(i).getString("CMEPoints");

                                //  CMEDate.setText(formattedDate);
                                //  CMEPoints.setText(CmePoints);





                                //   hmBooking.put("CMEPoints",Cmepoints);
                                //   hmBooking.put("CMENumber", CmeNumber);
                                // hmBooking.put("PointID",PointID);
                                //   hmBooking.put("CMECourse", CourseType);
                                //   hmBooking.put("CMEDate", formattedDate);
                                //   lstBooking.add(hmBooking);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //  }




                        }

                        else
                        {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                   // Intent i = new Intent(getApplicationContext(),ListOfPoints.class);
                                  //  PointsEditing.this.startActivity(i);
                                   // PointsEditing.this.finish();


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

    public void DeleteMsg()
    {
        {
            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Successfully Deleted");
                dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                        startActivity(i);
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


}
