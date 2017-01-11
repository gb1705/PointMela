package cme.pointmila.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by amoln on 12-09-2016.
 */
public class AcademicRecordTabFragment extends Fragment
{
    ViewPager viewPager;

    private Spinner spItems3;
    private Spinner spItems4;
    private Spinner spItems5;
    ArrayAdapter arrayAdapter3;
    ArrayAdapter arrayAdapter4;
    ArrayAdapter arrayAdapter5;
    List<String> listData3 = new ArrayList<String>();
    List<String> listData4 = new ArrayList<String>();
    List<String> listData5 = new ArrayList<String>();
    ImageView Arrow3;
    ImageView Arrow4;
    State objMC;
    City objMC1;
    Collage objMC2;
    ImageView Arrow5;
    ImageView CertificatePhoto;
    Button SelectPhoto1;

    int StateId;
    int CityId;

    ImageView DegreeCertificate;
    Button SelectPhoto;

    ImageView ReniewCertificate;
    Button SelectPhoto2;

    EditText DatePassing;
    private Bitmap thumbnail1, thumbnail2,thumnail3;
    String picturePath;
    StateAdapter adapter;
    CityAdapter adapter1;
    CollageAdapter adapter2;

    EditText RenewvalET;
    EditText YearOfAdmission;//

    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE=34;
    private static final int CAMERA_CODE = 101,GALLERY_CODE = 201,CROPING_CODE = 301;
    private static final int CAMERA_CODE1 = 111,GALLERY_CODE1 = 211,CROPING_CODE1 = 311;
    private static final int CAMERA_CODE2 = 121,GALLERY_CODE2 = 221,CROPING_CODE2 = 321;

    private   File outPutFile = null;
    AutoCompleteTextView State,City,Collage;
    Spinner spitem;
    EditText DegreeCertificateDate;


    AutoCompleteTextView AcademicProgram;
    AcademicPrograms objMCAcademic;
    AcademicProgramAdapter adapterAcademicProgram;
    Button Save;
    EditText AcademicET;
    String CollageId;
    JSONObject Objobject;

    public SharedPreferences settings2;
    boolean checkrecord=false;

    String CollageName;
    String CollageID;
    String StateName;
    String  CityName;

    ProgressDialog progressDialog;
    int year1;
    String yearAdmission;




    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_academicrecordtab, null);

        AcademicET = (EditText)view.findViewById(R.id.academicET);
        AcademicET.setFocusable(false);
        AcademicET.setClickable(false);
        //spitem = (Spinner)view.findViewById(R.id.spitem);
        DegreeCertificateDate = (EditText)view.findViewById(R.id.ETDegreeCertificateDate);

        State = (AutoCompleteTextView)view.findViewById(R.id.edit_ip1);
        City = (AutoCompleteTextView)view.findViewById(R.id.edit_ip2);
        Collage = (AutoCompleteTextView)view.findViewById(R.id.edit_ip);

        Save = (Button)view.findViewById(R.id.registerbtn);
        YearOfAdmission = (EditText)view.findViewById(R.id.Yearadmission);
        //Save.setText("Save");

        //GetAcademicRecords();



        //SelectAcademicProgram();
        SelectState();


        RenewvalET = (EditText)view.findViewById(R.id.renewalcertificate);
        DatePassing = (EditText)view.findViewById(R.id.ETYear);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");

       // listData3.add("Select State");
        //listData3.add("Maharashtra");
       // listData3.add("Gujarat");
       // listData3.add("Tamil Nadu");

       // listData4.add("Select City");
       // listData4.add("Thane");
       // listData4.add("Andheri");
       // listData4.add("Pune");

       // listData5.add("Select Academic Program");
       // listData5.add("MBBS");
        //listData5.add("MS");
        //listData5.add("MD");
        try {

           // spItems3 = (Spinner) view.findViewById(R.id.spitem3);
          //  Arrow3 = (ImageView) view.findViewById(R.id.img3);

          //  spItems4 = (Spinner) view.findViewById(R.id.spitem4);
          //  Arrow4 = (ImageView) view.findViewById(R.id.img4);

            spItems5 = (Spinner) view.findViewById(R.id.spitem);
            Arrow5 = (ImageView) view.findViewById(R.id.img5);

           // arrayAdapter3 = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData3);
           // arrayAdapter3.setDropDownViewResource(R.layout.row_spinner_dropdown);
           // spItems3.setAdapter(arrayAdapter3);

           // arrayAdapter4 = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData4);
           // arrayAdapter4.setDropDownViewResource(R.layout.row_spinner_dropdown);
          //  spItems4.setAdapter(arrayAdapter4);

            arrayAdapter5 = new ArrayAdapter(getActivity(), R.layout.row_spinner, listData5);
            arrayAdapter5.setDropDownViewResource(R.layout.row_spinner_dropdown);
            spItems5.setAdapter(arrayAdapter5);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        RenewvalET.setFocusable(false);
        RenewvalET.setClickable(true);

        DatePassing.setFocusable(false);
        DatePassing.setClickable(true);

        YearOfAdmission.setFocusable(false);//
        YearOfAdmission.setClickable(true);//

        DegreeCertificateDate.setFocusable(false);
        DegreeCertificateDate.setClickable(false);

        YearOfAdmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 year1 = Calendar.getInstance().get(Calendar.YEAR);

                ShowYearDialog();
            }
        });


        RenewvalET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });
        DegreeCertificateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDegreeDate();
            }
        });

        DatePassing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPassingDate();
            }
        });



            //GetProfileInfo();

        AcademicRecordView();
        GetAcademicRecords();

        return view;
    }

    public void ShowYearDialog()
    {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Select Year");
        d.setContentView(R.layout.yeardialog_layout);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year1+100);
        nopicker.setMinValue(year1-100);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year1);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YearOfAdmission.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    private  void AcademicRecordView() {

        State.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(State, true);

            }
        });

        City.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(City, true);

            }
        });

        Collage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(Collage, true);

            }
        });

        DatePassing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(DatePassing, true);

            }
        });

        DegreeCertificateDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(DegreeCertificateDate, true);

            }
        });
        YearOfAdmission.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(YearOfAdmission, true);

            }
        });






        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity().getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();

                if (checkValidation())
                {
                    progressDialog = ProgressDialog.show(getActivity(), "", "Updating..Please Wait");
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

                    if (DetectConnection.isInternetAvailable(getActivity())) {

                        SaveAcademicRecordsAPI();
                        //UpdateProfile();
                    } else {
                        InternetMessage();
                    }


                }
            }
        });

    }

    public void InternetMessage()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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


    public void GetAcademicRecords()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"DoctorProfile/GetAcademicRecords/"+ General.GUID);
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
                        String strSuccess = Objobject.getString("Success").toString();
                      /*  String StudentDOB = Objobject.getString("StudentDOb").toString();
                        String timestamp = StudentDOB.replace("/Date(", "").replace(")/", "");
                        Date createdon = new Date(Long.parseLong(timestamp));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd -MM- yyyy");
                        final    String formattedDate = sdf.format(createdon);*/
                         CollageName = Objobject.getString("CollegeName").toString();
                         CollageID = Objobject.getString("CollegeId").toString();
                         StateName = Objobject.getString("StateName").toString();
                         CityName = Objobject.getString("CityName").toString();
                        yearAdmission = Objobject.getString("YearOfPassing").toString();


                         String DateOfPassing = Objobject.getString("DateOfPassing").toString();
                        String timestamp = DateOfPassing.replace("/Date(", "").replace(")/", "");
                        Date passingdate = new Date(Long.parseLong(timestamp));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd -MMM- yyyy");
                        final    String formattedDate = sdf.format(passingdate);


                        String DateOfDegreeCertificate = Objobject.getString("DateOfDegree").toString();
                        String timestamp1 = DateOfDegreeCertificate.replace("/Date(", "").replace(")/", "");
                        Date passingdate1 = new Date(Long.parseLong(timestamp1));
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd -MMM- yyyy");
                        final    String formattedDate1 = sdf1.format(passingdate1);


                        String DateOfRenewalCertificate = Objobject.getString("DateOfRenewal").toString();
                        String timestamp2 = DateOfRenewalCertificate.replace("/Date(", "").replace(")/", "");
                        Date passingdate2 = new Date(Long.parseLong(timestamp2));
                        SimpleDateFormat sdf2 = new SimpleDateFormat("dd -MMM- yyyy");
                        final    String formattedDate2 = sdf2.format(passingdate2);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                try {

                                    State.setText(StateName);
                                    City.setText(CityName);
                                    Collage.setText(CollageName);
                                    DegreeCertificateDate.setText(formattedDate1);
                                    RenewvalET.setText(formattedDate2);
                                    DatePassing.setText(formattedDate);
                                    YearOfAdmission.setText(yearAdmission);


                                  //  DOB.setText(formattedDate);
                                 //   State.setText(CollageState);
                                  //  City.setText(CollageCity);
                                //    Collage.setText(CollageID);

                                    // Street1.setText(UserAddress1);
                                    //  Street2.setText(UserAddress2);
                                    // Pincode.setText(UserPincode);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                // General.str="1";




                            }
                        });









                       /* final String add = Objobject.getString("Image").toString();
                        final String pureBase64Encoded = add.substring(add.indexOf(",")+1);
                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded);
                        final Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Image.setImageBitmap(decodedBitmap);
                            }
                        });*/



                        //  url = new URL(add);
                        //   image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        // Image.setImageBitmap(image);



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
    protected void SaveAcademicRecordsAPI()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"DoctorProfile/SaveAcademicRecords");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    if (CollageId==null)
                    {
                        nameValuePairs.add(new BasicNameValuePair("CollegeId",CollageID));
                    }
                    else {
                        nameValuePairs.add(new BasicNameValuePair("CollegeId", CollageId));
                    }
                    nameValuePairs.add(new BasicNameValuePair("DateOfPassing",DatePassing.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("DateOfDegree",DegreeCertificateDate.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("NextRenewalDate",RenewvalET.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("YearOfPassing",YearOfAdmission.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("UserId",General.GUID));


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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        SaveAcademicRecordMsg();
                                      /*  try {
                                            General.GUID = Objobject.getString("result").toString();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }*/

                                        // Intent i = new Intent(getActivity().getApplicationContext(), OTPVerifyActivity.class);
                                        //  startActivity(i);

                                        //MsgBox(strResult);

                                        //  MsgBox();

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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ErrorMsg();
                                    //  MsgBox(strResult);
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

    public void SaveAcademicRecordMsg()
    {

            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                dlgAlert.setMessage("Successfully Updated");
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
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    protected void SelectAcademicProgram()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl +"PointMilaMaster/GetDoctorAcademicProgram/1");


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
                        final String strResult = objObject.getString("academicProgram").toString();
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

                                            JSONArray jarray = new JSONArray(objObject.getString("academicProgram").toString());

                                            final List<AcademicPrograms> list = new ArrayList<AcademicPrograms>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMCAcademic = new AcademicPrograms(jarray.getJSONObject(i).getString("DoctorAcademicProgramDescription"), jarray.getJSONObject(i).getInt("DoctorAcademicProgramId"));
                                                list.add(objMCAcademic);

                                            }

                                            getActivity(). runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapterAcademicProgram = new AcademicProgramAdapter(getActivity(), R.layout.activity_academicrecordtab, R.id.txt, list);
                                                    adapterAcademicProgram.appContext = getActivity().getApplicationContext();
                                                    adapterAcademicProgram.tmpActivity = AcademicRecordTabFragment.this;
                                                    // spItems.setAdapter(adapter);

                                                    AcademicProgram.setAdapter(adapterAcademicProgram);
                                                    AcademicProgram.setThreshold(1);
                                                }
                                            });

                                            try {

                                                /*CouncilMedical.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        MedicalCouncilId = objMC.getId();
                                                        SelectedDistrict();
                                                    }
                                                });*/

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

    protected void SelectState()
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
                            getActivity().runOnUiThread(new Runnable() {
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

                                           getActivity(). runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter = new StateAdapter(getActivity(), R.layout.activity_academicrecordtab, R.id.txt, list);
                                                    adapter.appContext = getActivity().getApplicationContext();
                                                    adapter.tmpActivity = AcademicRecordTabFragment.this;
                                                    // spItems.setAdapter(adapter);

                                                    State.setAdapter(adapter);
                                                    State.setThreshold(1);
                                                }
                                            });

                                            try {

                                                /*CouncilMedical.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        MedicalCouncilId = objMC.getId();
                                                        SelectedDistrict();
                                                    }
                                                });*/
                                                State.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        State temp = list.get(position);
                                                        StateId= temp.getId();

                                                       // StateId = list.get(position)
                                                        SelectCity();
                                                    }
                                                });

                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }



                                            //  MedicalCouncilList = new ArrayList<HashMap<String,String>>();//

                                            // lstStores = new ArrayList<HashMap<String,String>>();
                                           /* for (int i = 0; i < jarray.length(); i++)
                                            {

                                                MedicalCouncil = new HashMap<String, String>();//
                                                try {

                                                    MedicalcouncilName = jarray.getJSONObject(i).getString("MedicalCouncilName");
                                                    MedicalcouncilID = jarray.getJSONObject(i).getString("MedicalCouncilId");

                                                    MedicalCouncil.put("MedicalCouncilname", MedicalcouncilName);
                                                    MedicalCouncil.put("MedicalCouncilID",MedicalcouncilID);


                                                    MedicalCouncilList.add(MedicalCouncil);
                                                    //  lstStores.add(hmStore);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }*/


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
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetListofCity/"+StateId);


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
                          getActivity().runOnUiThread(new Runnable() {
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

                                           getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try
                                                    {
                                                        adapter1 = new CityAdapter(getActivity(), R.layout.activity_academicrecordtab, R.id.txt, list);
                                                        adapter1.appContext = getActivity().getApplicationContext();
                                                        adapter1.tmpActivity = AcademicRecordTabFragment.this;
                                                        // spItems.setAdapter(adapter);

                                                        City.setAdapter(adapter1);
                                                        City.setThreshold(1);

                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });




                                            try {

                                                /*CouncilMedical.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        MedicalCouncilId = objMC.getId();
                                                        SelectedDistrict();
                                                    }
                                                });*/
                                                City.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        City temp = list.get(position);
                                                        CityId= temp.getId();

                                                        // StateId = list.get(position)
                                                        SelectCollageName();
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

    protected void SelectCollageName()
    {
        Thread thread = new  Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetCollegeList/"+CityId);


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
                        final String strResult = objObject.getString("collegeList").toString();
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

                                            JSONArray jarray = new JSONArray(objObject.getString("collegeList").toString());

                                            final List<Collage> list = new ArrayList<Collage>();

                                            //  DistrictList = new ArrayList<HashMap<String,String>>();//

                                            // lstStores = new ArrayList<HashMap<String,String>>();
                                            for (int i = 0; i < jarray.length(); i++)
                                            {

                                                objMC2 = new Collage(jarray.getJSONObject(i).getString("CollegeName"), jarray.getJSONObject(i).getString("CollegeId"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC2);


                                                // MedicalCouncilDistrict = new HashMap<String, String>();//


                                            }

                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try
                                                    {
                                                        adapter2 = new CollageAdapter(getActivity(), R.layout.activity_academicrecordtab, R.id.txt, list);
                                                        adapter2.appContext = getActivity().getApplicationContext();
                                                        adapter2.tmpActivity = AcademicRecordTabFragment.this;
                                                        // spItems.setAdapter(adapter);

                                                        Collage.setAdapter(adapter2);
                                                        Collage.setThreshold(1);

                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });




                                            try {



                                                Collage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Collage temp = list.get(position);
                                                        CollageId= temp.getId();

                                                        // StateId = list.get(position)
                                                        // SelectCollageName();
                                                    }
                                                });

                                                /*CouncilMedical.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        MedicalCouncilId = objMC.getId();
                                                        SelectedDistrict();
                                                    }
                                                });*/
                                               /* City.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        City temp = list.get(position);
                                                        CityId= temp.getId();

                                                        // StateId = list.get(position)
                                                        SelectCollageName();
                                                    }
                                                });*/

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

    private void SelectDate()
    {
        DatePickerFragment1 date = new DatePickerFragment1();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");

    }

    private void SelectDegreeDate()
    {

        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondateDegreeCertificate);
        date.show(getFragmentManager(), "Date Picker");

    }

    private void SelectPassingDate()
    {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondatepass);
        date.show(getFragmentManager(), "Date Picker");

    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {

          /*  RenewvalET.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/


           try
           {
               String DateOfPassing = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
               SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
               Date date = sdf.parse(DateOfPassing);
               // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
               //  String finalDate = timeformat.format(date);
               RenewvalET.setText(General.ConvertDateFormat(date));
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }



        }
    };



    DatePickerDialog.OnDateSetListener ondatepass = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
           /* DatePassing.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
            try
            {
                String DateOfPassing = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(DateOfPassing);
                // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
                //  String finalDate = timeformat.format(date);
                DatePassing.setText(General.ConvertDateFormat(date));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


    DatePickerDialog.OnDateSetListener ondateDegreeCertificate = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            try
            {
                String DateOfPassing = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(DateOfPassing);
                // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
                //  String finalDate = timeformat.format(date);
                DegreeCertificateDate.setText(General.ConvertDateFormat(date));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
          /*  DegreeCertificateDate.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
        }
    };








    private void SelectImageOption()
    {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int item)
                {
                    if (items[item].equals("Capture Photo"))
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                        mImageCaptureUri = Uri.fromFile(f);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        startActivityForResult(intent, CAMERA_CODE);
                    }
                    else if (items[item].equals("Choose from Gallery"))
                    {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_CODE);
                    }
                    else if (items[item].equals("Cancel"))
                    {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SelectImageOption1()
    {
        try
        {
            final CharSequence[] items = {"Capture Photo","Choose from Gallery","Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item)
                {
                   if (items[item].equals("Capture Photo"))
                   {
                       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp1.jpg");
                       mImageCaptureUri = Uri.fromFile(f);
                       intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                       startActivityForResult(intent,CAMERA_CODE1);
                   }

                    else if (items[item].equals("Choose from Gallery"))
                   {
                       Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                       startActivityForResult(i,GALLERY_CODE1);
                   }

                    else if (items[item].equals("Cancel"))
                   {
                       dialog.dismiss();
                   }

                }
            });

            builder.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SelectImageOption2()
    {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item)
                {
                    if (items[item].equals("Capture Photo"))
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(),"temp1.jpg");
                        mImageCaptureUri = Uri.fromFile(f);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                        startActivityForResult(intent,CAMERA_CODE2);
                    }

                    else if (items[item].equals("Choose from Gallery"))
                    {
                        Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i,GALLERY_CODE2);
                    }

                    else if (items[item].equals("Cancel"))
                    {
                        dialog.dismiss();
                    }
                }
            });

            builder.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == getActivity().RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera Image URI:" + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CROPING_CODE) {
            try {
                if (outPutFile.exists()) {
                    Bitmap Photo1 = decodeFile(outPutFile);
                    CertificatePhoto.setImageBitmap(Photo1);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (requestCode == GALLERY_CODE1 && resultCode == getActivity().RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" + mImageCaptureUri);
            CropingIMG1();
        }
        else if (requestCode == CAMERA_CODE1 && resultCode == Activity.RESULT_OK)
        {
            System.out.println("Camera Image URI:" + mImageCaptureUri);
            CropingIMG1();
        }
        else if (requestCode == CROPING_CODE1)
        {
            try
            {
                if (outPutFile.exists())
                {
                    Bitmap BitmapPhoto = decodeFile(outPutFile);
                    DegreeCertificate.setImageBitmap(BitmapPhoto);
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        if (requestCode == GALLERY_CODE2 && resultCode == getActivity().RESULT_OK && null !=data)
        {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" + mImageCaptureUri);
            CropingIMG2();
        }
        else if (requestCode == CAMERA_CODE2 && requestCode == Activity.RESULT_OK)
        {
            System.out.println("Camera Image URI:"+ mImageCaptureUri);
            CropingIMG2();
        }
        else if (requestCode == CROPING_CODE2)
        {
            try
            {
                if (outPutFile.exists())
                {
                    Bitmap BitmapPhoto1 = decodeFile(outPutFile);
                    ReniewCertificate.setImageBitmap(BitmapPhoto1);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    private void CropingIMG()
    {
        try {
            final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");

            List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
            int size = list.size();
            if (size == 0) {
                Toast.makeText(getActivity(), "can't find image cropping  app", Toast.LENGTH_SHORT).show();
                return;
            } else {
                intent.setData(mImageCaptureUri);
               // intent.putExtra("outputX", 512);
               // intent.putExtra("outputY", 512);
                //intent.putExtra("aspectX", 1);
                //intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = (ResolveInfo) list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    startActivityForResult(i, CROPING_CODE);
                } else {
                    for (ResolveInfo res : list) {
                        final CropingOption co = new CropingOption();
                        co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                        co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);
                        co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        cropOptions.add(co);
                    }

                    CropingOptionAdapter adapter = new CropingOptionAdapter(getActivity().getApplicationContext(), cropOptions);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose Cropping App");
                    builder.setCancelable(false);
                    builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE);

                        }
                    });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUri != null) {
                                getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void CropingIMG2()
    {
        try
        {
            final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");
            List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent,0);
            int size = list.size();

            if (size ==0)
            {
                Toast.makeText(getActivity(),"can't find image cropping app",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                intent.setData(mImageCaptureUri);
                //intent.putExtra("outputX",512);
                //intent.putExtra("outputY",512);
                //intent.putExtra("aspectX",1);
                //intent.putExtra("aspectY",1);
                intent.putExtra("scale",true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outPutFile));
                if (size == 1)
                {
                    Intent i = new Intent(intent);
                    ResolveInfo res = (ResolveInfo)list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
                    startActivityForResult(i,CROPING_CODE2);
                }

                else
                {
                    for (ResolveInfo res : list)
                    {
                        final CropingOption co = new CropingOption();
                        co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                        co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);
                        co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        cropOptions.add(co);
                    }

                    CropingOptionAdapter adapter = new CropingOptionAdapter(getActivity().getApplicationContext(), cropOptions);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose Cropping App");
                    builder.setCancelable(false);

                    builder.setAdapter(adapter, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE2);
                        }
                    });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUri != null) {
                                getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void CropingIMG1()
    {
        try
        {
            final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");
            List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent,0);
            int size = list.size();

            if (size == 0) {
                Toast.makeText(getActivity(), "can't find image cropping  app", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                intent.setData(mImageCaptureUri);
                //intent.putExtra("outputX", 512);
                //intent.putExtra("outputY", 512);
                //intent.putExtra("aspectX", 1);
                //intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
                if (size == 1)
                {
                    Intent i = new Intent(intent);
                    ResolveInfo res = (ResolveInfo) list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    startActivityForResult(i, CROPING_CODE1);
                }
                else
                {
                    for (ResolveInfo res : list)
                    {
                        final CropingOption co = new CropingOption();
                        co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                        co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);
                        co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        cropOptions.add(co);
                    }

                    CropingOptionAdapter adapter = new CropingOptionAdapter(getActivity().getApplicationContext(), cropOptions);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose Cropping App");
                    builder.setCancelable(false);

                    builder.setAdapter(adapter, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            startActivityForResult(cropOptions.get(item).appIntent, CROPING_CODE1);
                        }
                    });

                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            if (mImageCaptureUri != null) {
                                getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Bitmap decodeFile(File f)
    {
        try
        {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth,height_tmp = o.outHeight;
            int scale = 1;
            while (true)
            {
                if (width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale *=2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return  BitmapFactory.decodeStream(new FileInputStream(f),null,o2);
        }
        catch (FileNotFoundException e)
        {

        }

        return null;
    }

    private  boolean checkValidation()
    {
        boolean ret = true;
        if (!Validation.isName(State,true))ret=false;
        // if(!Validation.isPincode(Pincode,true))ret=false;
        if (!Validation.isName(City,true))ret=false;
        if (!Validation.isName(Collage,true))ret = false;
        if (!Validation.isName(DatePassing,true))ret = false;
        if (!Validation.isName(DegreeCertificateDate,true))ret = false;
        if (!Validation.isName(YearOfAdmission,true))ret = false;
        return  ret;
    }


}
