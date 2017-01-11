package cme.pointmila.com;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 12-09-2016.
 */
public class AddressTabFragment extends Fragment
{
    ListView listView;
    PopupWindow popupWindow;
    EditText Street1;
    EditText Street2;
    EditText Pincode;
    EditText ResidentialAddress;
    ImageView ClinicAddress;
    ImageView Arrow;
    ImageView Arrow1;


    String Edit;
    String Edit1;
    String Edit2;
    String StateSpinner;
    String CitySpinner;
    LinearLayout listviewLayout;

    boolean checkAddress=false;
    boolean checkClinicAddress = false;


    Spinner State;
    Spinner city;

    HashMap<String, String> hmState=null;
    List<HashMap<String,String>> lstState,lstCity;
    String Statename,StateID,SelectedStateID,selectedStatename,Cityname,CityID,SelectedCityID,SelectedCityname;
    public SharedPreferences AddressResident;
    public SharedPreferences AddressClinical;
    ViewPager viewPager;
    JSONObject Objobject;
    List<HashMap<String,String>> lstBooking;
    HashMap<String,String> hmBooking=null;
    public SharedPreferences settings1;
    boolean checkprofile=false;

     String AddStreet1;
     String AddStreet2;
     String AddCityName;
     String AddCityId;
     String AddStateName;
     String AddPincode;

    TextView ClinicAddTxt;


    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            GetResidentialAddress();
            GetClinicalAddress();

        }
        else
        {
        }
    }*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_addresstab, null);
        ResidentialAddress = (EditText)view.findViewById(R.id.address);
        ClinicAddress = (ImageView) view.findViewById(R.id.Clinicaddress);
        lstBooking = new ArrayList<HashMap<String,String>>();
        listView = (ListView)view.findViewById(R.id.listview);
        listviewLayout = (LinearLayout)view.findViewById(R.id.listviewLayout);
        ClinicAddTxt = (TextView)view.findViewById(R.id.clinicAdd);

        ResidentialAddress.setFocusable(false);
        ResidentialAddress.setClickable(true);

        ClinicAddress.setFocusable(false);
        ClinicAddress.setClickable(true);




        settings1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
         checkprofile = settings1.getBoolean("checkprofile",false);


       GetResidentialAddress();
        GetClinicalAddress();

        ResidentialAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(getActivity().getApplicationContext(), ResidentialAddress.class);

                i.putExtra("addressstreet1",AddStreet1);
                i.putExtra("addressstreet2",AddStreet2);
                i.putExtra("addressstate",AddStateName);
                i.putExtra("addresscity",AddCityName);
                i.putExtra("addresscityId",AddCityId);
                i.putExtra("pincode",AddPincode);

                startActivity(i);

            }
        });


        ClinicAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),ClinicalAddress.class);
                startActivity(i);

            }
        });



        ClinicAddTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),ClinicalAddress.class);
                startActivity(i);
            }
        });



        return view;
    }

    protected void GetResidentialAddress()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"DoctorProfile/GetResidenceAddress/" + General.GUID);
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
                        AddStreet1 = Objobject.getString("ResidenceStreet1").toString();
                         AddStreet2 = Objobject.getString("ResidenceStreet2").toString();
                        if (AddStreet2.equals("null"))
                        {
                          AddStreet2=  AddStreet2.replace("null","");
                        }
                        AddCityId = Objobject.getString("ResidenceCityId").toString();
                        AddCityName   = Objobject.getString("ResidenceCityName").toString();
                         AddStateName = Objobject.getString("ResidenceStateName").toString();
                         AddPincode = Objobject.getString("ResidencePinCode").toString();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ResidentialAddress.setText(AddStreet1 + "" + "," +"" + AddStreet2 + "" +"," + "" + AddCityName + "-" +""+ AddPincode + ""+","+"" + AddStateName);
                            }
                        });

                        //  final     String strResult = Objobject.getString("result").toString();


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

   protected void GetClinicalAddress()
    {
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run()
           {
               try
               {
                   HttpClient httpClient = new DefaultHttpClient();
                   HttpGet httpPost = new HttpGet(General.baseurl+"DoctorProfile/GetClinicAddress/" + General.GUID);
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
                           JSONArray jarray = new JSONArray(Objobject.getString("AddressInfo").toString());
                           //  lstBooking = new ArrayList<HashMap<String, String>>();


                           for (int i = 0; i < jarray.length(); i++) {
                               hmBooking = new HashMap<String, String>();

                               try {
                                   String Addressstreet = jarray.getJSONObject(i).getString("Street1");
                                   String Addressstreet1 = jarray.getJSONObject(i).getString("Street2");
                                   if (Addressstreet1.equals("null"))
                                   {
                                       Addressstreet1 = Addressstreet1.replace("null","");
                                   }
                                   String AddressCity = jarray.getJSONObject(i).getString("CityName");
                                   String AddressID = jarray.getJSONObject(i).getString("AddressId");

                                   String CityID = jarray.getJSONObject(i).getString("CityId");

                                   String AddressState = jarray.getJSONObject(i).getString("StateName");
                                   String AddressPincode = jarray.getJSONObject(i).getString("Pincode");

                                   hmBooking.put("AddressStreet1",Addressstreet);
                                   hmBooking.put("AddressStreet2", Addressstreet1);
                                   hmBooking.put("AddressState",AddressState);
                                   hmBooking.put("AddressCity",AddressCity);
                                   hmBooking.put("AddressPincode",AddressPincode);
                                   hmBooking.put("AddressID",AddressID);
                                   hmBooking.put("CityiD",CityID);
                                   lstBooking.add(hmBooking);

                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           }

                           getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run()
                               {
                                   ClinicalAddressAdapter clinicAddress = new ClinicalAddressAdapter(getActivity(),lstBooking);
                                   clinicAddress.appContext = getActivity().getApplicationContext();
                                   clinicAddress.tmpActivity = AddressTabFragment.this;

                                   listviewLayout.setVisibility(View.VISIBLE);
                                   listView.setAdapter(clinicAddress);


                                   //  ListOfPointsAdapter pointslist = new ListOfPointsAdapter(AddressTabFragment.this,lstBooking);
                                //   listView.setAdapter(pointslist);

                               }
                           });


                       }

                       else
                       {
                           // General.Token = Objobject.getString("Token").toString();
                          getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {

                                   ErrorMsg();
                                   // MsgBox(strResult);
                                   // MsgBox();

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
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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

    private void callPop()
    {
        try {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup, null);

            popupWindow = new PopupWindow(popupView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
          //  Street1 = (EditText) popupView.findViewById(R.id.street1);
           // Street2 = (EditText) popupView.findViewById(R.id.street2);
            //Pincode = (EditText) popupView.findViewById(R.id.pincode);
            //StatePopup = (AutoCompleteTextView)popupView.findViewById(R.id.edit_ip1);
            // CityPopup = (AutoCompleteTextView)popupView.findViewById(R.id.edit_ip2);

            //SelectPopupState();


            //  final Spinner State;
            // ArrayAdapter StatearrayAdapter;
            // List<String> listData = new ArrayList<String>();
            //ImageView Arrow;

            // final Spinner city;
            // ArrayAdapter CityarrayAdapter;
            // List<String> listData1 = new ArrayList<String>();
            // ImageView Arrow1;



            //listData.add("Select State");
            // listData.add("Maharashtra");
            //  listData.add("Punjab");
            //   listData.add("Gujarat");

            //  listData1.add("Select City");
            // listData1.add("Mumbai");
            //  listData1.add("Amrutsar");
            // listData1.add("Ahemdabad");


            State = (Spinner) popupView.findViewById(R.id.state);
            city = (Spinner) popupView.findViewById(R.id.city);
            Arrow = (ImageView) popupView.findViewById(R.id.img1);
            Arrow1 = (ImageView) popupView.findViewById(R.id.img);
            SelectPopupState();






            // StatearrayAdapter = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData);
            //StatearrayAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
            //State.setAdapter(StatearrayAdapter);

            //CityarrayAdapter = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData1);
            //CityarrayAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
            //city.setAdapter(CityarrayAdapter);


            ((Button) popupView.findViewById(R.id.saveBtn)).setOnClickListener(new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                public void onClick(View arg0) {
                    // Toast.makeText(getApplicationContext(),
                    //Name.getText().toString(), Toast.LENGTH_LONG).show();
                    String Edit = Street1.getText().toString();
                    String Edit1 = Street2.getText().toString();
                    String Edit2 = Pincode.getText().toString();
                    String StateSpinner = selectedStatename;
                    // String StateSpinner = StatePopup.getText().toString();
                    String CitySpinner = SelectedCityname;

                    ResidentialAddress.setText(Edit + "" + "," +"" + Edit1 + "" +"," + "" + CitySpinner + "-" +""+ Edit2 + ""+","+"" + StateSpinner);
                    popupWindow.dismiss();
                }
            });

            ((Button) popupView.findViewById(R.id.cancelbtutton))
                    .setOnClickListener(new View.OnClickListener() {

                        public void onClick(View arg0) {

                            popupWindow.dismiss();
                        }
                    });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    protected void SelectPopupState()
    {
        Thread thread = new  Thread(new Runnable() {
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
                        {builder.append(line).append("\n");

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
                           getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("stateList").toString());

                                            lstState = new ArrayList<HashMap<String,String>>();

                                            // lstStores = new ArrayList<HashMap<String,String>>();
                                            for (int i = 0; i < jarray.length(); i++)
                                            {

                                                hmState = new HashMap<String, String>();
                                                try {

                                                    Statename = jarray.getJSONObject(i).getString("StateName");
                                                    StateID = jarray.getJSONObject(i).getString("Stateid");

                                                    hmState.put("Statename", Statename);
                                                    hmState.put("StateId",StateID);


                                                    lstState.add(hmState);
                                                    //  lstStores.add(hmStore);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try
                                                    {
                                                        StateAdapterPopUp StateData = new StateAdapterPopUp(getActivity(), lstState);
                                                        StateData.appContext = getActivity().getApplicationContext();
                                                        StateData.tmpActivity = AddressTabFragment.this;

                                                        State.setAdapter(StateData);


                                                       // adapter = new StateAdapter(getActivity(), R.layout.activity_academicrecordtab, R.id.txt, list);
                                                        //adapter.appContext = getActivity().getApplicationContext();
                                                        //adapter.tmpActivity = AcademicRecordTabFragment.this;







                                                        State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                HashMap<String, String> tmpData = lstState.get(position);
                                                                SelectedStateID = tmpData.get("StateId");
                                                                selectedStatename = tmpData.get("Statename");

                                                                SelectedCity();
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

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
    protected void SelectedCity()
    {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetListofCity/"+SelectedStateID);

                    //registerInBackground();

                    try
                    {

                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; )
                        {
                            builder.append(line).append("\n");
                        }
                        final    JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();

                        String strResult = objObject.getString("cityList").toString();
                        if(strResult.equals("null"))
                        {

                        }

                        else
                        {
                           getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    try {

                                        JSONArray jarray = new JSONArray(objObject.getString("cityList").toString());

                                        lstCity = new ArrayList<HashMap<String,String>>();

                                        // lstStores = new ArrayList<HashMap<String,String>>();
                                        for (int i = 0; i < jarray.length(); i++)
                                        {

                                            hmState = new HashMap<String, String>();
                                            try {

                                                Cityname = jarray.getJSONObject(i).getString("CityName");
                                                CityID = jarray.getJSONObject(i).getString("CityId");

                                                hmState.put("Cityname", Cityname);
                                                hmState.put("CityId",CityID);


                                                lstCity.add(hmState);




                                                //  lstStores.add(hmStore);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                try
                                                {
                                                    CityAdapterPopUp CityData = new CityAdapterPopUp(getActivity(), lstCity);

                                                    CityData.appContext = getActivity().getApplicationContext();
                                                    CityData.tmpActivity = AddressTabFragment.this;

                                                    city.setAdapter(CityData);

                                                    city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            HashMap<String, String> tmpData = lstCity.get(position);
                                                            SelectedCityID = tmpData.get("CityId");
                                                            SelectedCityname = tmpData.get("Cityname");
                                                            //SelectionTruckWeight();
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    });
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }



                                }
                            });

                        }
                    }
                    catch (Exception e)
                    {

                        e.printStackTrace();
                    }

                }//
                catch (Exception e)
                {

                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }
    private void GetAddress()
    {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup1, null);

            popupWindow = new PopupWindow(popupView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Street1 = (EditText) popupView.findViewById(R.id.street1);
            Street2 = (EditText) popupView.findViewById(R.id.street2);
            Pincode = (EditText) popupView.findViewById(R.id.pincode);


           // final   Spinner State;
           // ArrayAdapter StatearrayAdapter;
           // List<String> listData = new ArrayList<String>();
           // ImageView Arrow;

          //  final Spinner city;
          //  ArrayAdapter CityarrayAdapter;
          //  List<String> listData1 = new ArrayList<String>();
           // ImageView Arrow1;



            //listData.add("Select State");
           // listData.add("Maharashtra");
           // listData.add("Punjab");
           // listData.add("Gujarat");

           // listData1.add("Select City");
          //  listData1.add("Mumbai");
          //  listData1.add("Amrutsar");
          //  listData1.add("Ahemdabad");


            State = (Spinner) popupView.findViewById(R.id.state);
            city = (Spinner) popupView.findViewById(R.id.city);
            Arrow = (ImageView) popupView.findViewById(R.id.img1);
            Arrow1 = (ImageView) popupView.findViewById(R.id.img);

            SelectedStateSpinner();

          /*  StatearrayAdapter = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData);
            StatearrayAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
            State.setAdapter(StatearrayAdapter);*/

           /* CityarrayAdapter = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData1);
            CityarrayAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
            city.setAdapter(CityarrayAdapter);*/







            ((Button) popupView.findViewById(R.id.saveBtn)).setOnClickListener(new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                public void onClick(View arg0) {
                    // Toast.makeText(getApplicationContext(),
                    //Name.getText().toString(), Toast.LENGTH_LONG).show();
                    listviewLayout.setVisibility(View.VISIBLE);

                     Edit = Street1.getText().toString();
                     Edit1 = Street2.getText().toString();
                     Edit2 = Pincode.getText().toString();
                     StateSpinner = selectedStatename;
                     CitySpinner = SelectedCityname;
                     String[] Name=
                            {
                                    Edit
                            };
                    String[] Street=
                            {
                                    Edit1
                            };
                    String[] City=
                            {
                                    CitySpinner
                            };
                    String[]Pincode=
                            {
                                    Edit2

                            };
                    String[] State =
                            {
                                    StateSpinner
                            };

                    ClinicAddressAdapter adapter = new ClinicAddressAdapter(getActivity(),Name,Street,City,Pincode,State);

                    listView.setAdapter(adapter);
                   // adapter.notifyDataSetChanged();




                    //ResidentialAddress.setText(Edit + "" + Edit1 + "" + "" + CitySpinner + "-" + Edit2 + "" + StateSpinner);
                    popupWindow.dismiss();
                }
            });

            ((Button) popupView.findViewById(R.id.cancelbtutton))
                    .setOnClickListener(new View.OnClickListener() {

                        public void onClick(View arg0) {

                            popupWindow.dismiss();
                        }
                    });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    protected void SelectedStateSpinner()
    {
        Thread thread = new  Thread(new Runnable() {
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
                        {builder.append(line).append("\n");

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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("stateList").toString());

                                            lstState = new ArrayList<HashMap<String,String>>();

                                            // lstStores = new ArrayList<HashMap<String,String>>();
                                            for (int i = 0; i < jarray.length(); i++)
                                            {

                                                hmState = new HashMap<String, String>();
                                                try {

                                                    Statename = jarray.getJSONObject(i).getString("StateName");
                                                    StateID = jarray.getJSONObject(i).getString("Stateid");

                                                    hmState.put("Statename", Statename);
                                                    hmState.put("StateId",StateID);


                                                    lstState.add(hmState);
                                                    //  lstStores.add(hmStore);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try
                                                    {
                                                        StateAdapterPopUp StateData = new StateAdapterPopUp(getActivity(), lstState);
                                                        StateData.appContext = getActivity().getApplicationContext();
                                                        StateData.tmpActivity = AddressTabFragment.this;
                                                        State.setAdapter(StateData);


                                                        // adapter = new StateAdapter(getActivity(), R.layout.activity_academicrecordtab, R.id.txt, list);
                                                        //adapter.appContext = getActivity().getApplicationContext();
                                                        //adapter.tmpActivity = AcademicRecordTabFragment.this;
                                                        State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                HashMap<String, String> tmpData = lstState.get(position);
                                                                SelectedStateID = tmpData.get("StateId");
                                                                selectedStatename = tmpData.get("Statename");

                                                                SelectedCitySpinner();
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

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

    protected void SelectedCitySpinner()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetListofCity/"+SelectedStateID);

                    //registerInBackground();

                    try
                    {

                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; )
                        {
                            builder.append(line).append("\n");
                        }
                        final    JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();

                        String strResult = objObject.getString("cityList").toString();
                        if(strResult.equals("null"))
                        {

                        }

                        else
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    try {

                                        JSONArray jarray = new JSONArray(objObject.getString("cityList").toString());

                                        lstCity = new ArrayList<HashMap<String,String>>();

                                        // lstStores = new ArrayList<HashMap<String,String>>();
                                        for (int i = 0; i < jarray.length(); i++)
                                        {

                                            hmState = new HashMap<String, String>();
                                            try {

                                                Cityname = jarray.getJSONObject(i).getString("CityName");
                                                CityID = jarray.getJSONObject(i).getString("CityId");

                                                hmState.put("Cityname", Cityname);
                                                hmState.put("CityId",CityID);
                                                lstCity.add(hmState);

                                                //  lstStores.add(hmStore);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                try
                                                {
                                                    CityAdapterPopUp CityData = new CityAdapterPopUp(getActivity(), lstCity);

                                                    CityData.appContext = getActivity().getApplicationContext();
                                                    CityData.tmpActivity = AddressTabFragment.this;

                                                    city.setAdapter(CityData);

                                                    city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            HashMap<String, String> tmpData = lstCity.get(position);
                                                            SelectedCityID = tmpData.get("CityId");
                                                            SelectedCityname = tmpData.get("Cityname");
                                                            //SelectionTruckWeight();
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    });
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }



                                }
                            });

                        }
                    }
                    catch (Exception e)
                    {

                        e.printStackTrace();
                    }

                }//
                catch (Exception e)
                {

                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }



}
