package cme.pointmila.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by amoln on 05-11-2016.
 */
public class ClinicalAddress extends ActionBarActivity
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
    CityAdapter adapter1;
    Button OK;
    ProgressDialog progressDialog;
    String Address1,Address2,ResidentialState,ResidentialCity,pincode;
    public SharedPreferences AddressClinical;
    String AddressID,AddStreet1,AddressStreet2,Addressstate,AddressCity,AddressCityID,AddressPincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicaladdress);
        Street1 = (EditText)findViewById(R.id.street1);
        Street2 = (EditText)findViewById(R.id.street2);
        StateAutoET = (AutoCompleteTextView)findViewById(R.id.state);
        CityAutoET = (AutoCompleteTextView)findViewById(R.id.city);
        OK = (Button)findViewById(R.id.okbtn);
        Pincode = (EditText)findViewById(R.id.pincode);
        try {


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




        //  GetClinicalAddress();

       /* OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateClinicAddress();
            }
        });*/

        SelectState();

        ClinicAddressView();
    }

    public void ClinicAddressView()
    {

        Street1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(Street1, true);

            }
        });
        // etMobileNumber = (EditText) findViewById(R.id.mobileET);
        StateAutoET.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(StateAutoET, true);

            }
        });

        CityAutoET.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(CityAutoET, true);

            }
        });

        Pincode.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isPincode(Pincode, true);

            }
        });


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checkValidation())
                {
                    progressDialog = ProgressDialog.show(ClinicalAddress.this, "", "Please wait");
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
                    if (DetectConnection.isInternetAvailable(ClinicalAddress.this)) {
                        //SaveResidentialAddress();
                        CreateClinicAddress();
                    }
                    else
                    {
                        InternetMessage();
                    }

                }

            }
        });





       /* Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkValidation()) {
                    progressDialog = ProgressDialog.show(LoginActivity.this, "", "Loading");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Thread timerthread = new Thread() {
                        public void run() {
                            try {
                                sleep(5000);
                            } catch (Exception e) {
                                Log.e("tag", e.getMessage());
                            }
                            progressDialog.dismiss();

                        }
                    };
                    timerthread.start();
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
        });*/


    }

    protected void CreateClinicAddress()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl +"DoctorProfile/AddClinicAddress");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("Street1",Street1.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Street2",Street2.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CityId", Integer.toString(SelectedCityID)));
                    nameValuePairs.add(new BasicNameValuePair("PinCode",Pincode.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("userId",General.GUID));

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

                        final  String strSuccess = Objobject.getString("Success").toString();

                       if (strSuccess.equals("true"))
                       {
                           try {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {

                                       SaveClinicalAddress();
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
                           ErrorMsg();
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


    public void SaveClinicalAddress()
    {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Successfully Added");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(ClinicalAddress.this,ProfileDashboardActivity.class);
                    startActivity(i);
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


                                                    adapter = new StateAdapter(ClinicalAddress.this, R.layout.activity_residentialaddress, R.id.txt, list);
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
                                                        adapter1 = new CityAdapter(ClinicalAddress.this, R.layout.activity_residentialaddress, R.id.txt, list);
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

    private  boolean checkValidation()
    {
        boolean ret = true;
        if (!Validation.isName(Street1,true))ret=false;
        // if(!Validation.isPincode(Pincode,true))ret=false;
        if (!Validation.isName(StateAutoET,true))ret=false;
        if (!Validation.isName(CityAutoET,true))ret = false;
        if (!Validation.isPincode(Pincode,true))ret = false;

        return  ret;
    }





}
