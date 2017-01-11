package cme.pointmila.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 09-09-2016.
 */
public class RegisterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private Spinner spItems;
    private long mLastClickTime = 0;

    ArrayAdapter arrayAdapter;
    private Spinner spItems1;
    ArrayAdapter arrayAdapter1;
    ImageView Photo;
    Button SelectPhoto;
    private Bitmap thumbnail;
    String picturePath;
    RadioGroup RadioG;
    RadioGroup RadioGender;
    List<HashMap<String, String>> MedicalCouncilList;

    HashMap<String, String> MedicalCouncilDistrict = null;
    List<HashMap<String, String>> DistrictList;
    List<MedicalCouncil> mList;

    LinearLayout CouncileNo;
    LinearLayout CheckBoxLayout;
    LinearLayout MedicalCouncilLayout;
    LinearLayout DistrictLayout;
    Button Register;
    MedicalCouncil objMC;
    MedicalCouncilDistrict objMC1;
    String MedicalCouncilId, MedicalcouncilID;

    // List<String>listData = new ArrayList<String>();
    // List<String> listData1 = new ArrayList<String>();
    LinearLayout Image;
    TextView Text, Text1;
    TextView DistrictTxt;
    RadioButton rb1, rb2, Male, Female;
    String SelectedMedicalCouncil;


    String DistrictName, DistrictID;
    CouncilAdapter adapter;

    EditText FirstName, MiddleName, LastName, MobileNumber, Email, Password, ConfirmPassword, ReferalCode, CouncileNumber;
    AutoCompleteTextView CouncilMedical, District;
    CheckBox check;
    Bitmap photo1;
    String ba1;
    String StudentStatus, GenderStatus;
    int MedicalCouncilDistrictID;
    String CheckBoxStatus;
    CheckBox ShowPassword;
    ProgressDialog progressDialog;

    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private File outPutFile = null;
    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RadioG = (RadioGroup) findViewById(R.id.rdG);
        RadioGender = (RadioGroup) findViewById(R.id.rdG1);
        rb1 = (RadioButton) findViewById(R.id.studentbtn);
        rb2 = (RadioButton) findViewById(R.id.doctorbtn);
        Male = (RadioButton) findViewById(R.id.male);
        Female = (RadioButton) findViewById(R.id.female);

        Photo = (ImageView) findViewById(R.id.photo);
        // SelectPhoto = (Button) findViewById(R.id.selectphoto);
        CheckBoxLayout = (LinearLayout) findViewById(R.id.checklayout);
        MedicalCouncilLayout = (LinearLayout) findViewById(R.id.layoutmobile);
        DistrictLayout = (LinearLayout) findViewById(R.id.layoutmobile1);
        FirstName = (EditText) findViewById(R.id.firstnametxt);
        MiddleName = (EditText) findViewById(R.id.middlenametxt);
        LastName = (EditText) findViewById(R.id.lastnametxt);
        MobileNumber = (EditText) findViewById(R.id.mobiletxt);
        Email = (EditText) findViewById(R.id.emailtxt);
        Password = (EditText) findViewById(R.id.passw);
        //  ConfirmPassword = (EditText)findViewById(R.id.confirmpassw);
        ReferalCode = (EditText) findViewById(R.id.referalcode);
        check = (CheckBox) findViewById(R.id.check);
        CouncileNumber = (EditText) findViewById(R.id.councileno);
        ShowPassword = (CheckBox) findViewById(R.id.check1);
        DistrictTxt = (TextView) findViewById(R.id.districttxt);
        CheckBoxStatus = "0";

        General.deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        ShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    Password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });


        Picasso.with(this)
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo);


        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check.isChecked()) {
                    CheckBoxStatus = "1";
                }

            }
        });

        RadioG.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) this);
        RadioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {

                    case R.id.male:

                        Male.setTypeface(null, Typeface.BOLD_ITALIC);
                        Female.setTypeface(null, Typeface.NORMAL);
                        Female.setChecked(false);
                        GenderStatus = "true";
                        break;

                    case R.id.female:

                        Female.setTypeface(null, Typeface.BOLD_ITALIC);
                        Male.setTypeface(null, Typeface.NORMAL);
                        Male.setChecked(false);
                        GenderStatus = "false";
                        break;

                    default:
                        break;
                }


            }
        });

        spItems = (Spinner) findViewById(R.id.spitem);
        spItems1 = (Spinner) findViewById(R.id.spitem1);
        CouncilMedical = (AutoCompleteTextView) findViewById(R.id.edit_ip);
        District = (AutoCompleteTextView) findViewById(R.id.edit_ip1);

        CouncileNo = (LinearLayout) findViewById(R.id.councileET);
        Register = (Button) findViewById(R.id.registerbtn);
        Image = (LinearLayout) findViewById(R.id.Img);

        Text = (TextView) findViewById(R.id.txt);
        Text1 = (TextView) findViewById(R.id.txt1);
        RegisterViews();



       /* Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveStudentPhoto();
               // Intent i = new Intent(getApplicationContext(), OTPVerifyActivity.class);
               // startActivity(i);

                if (DetectConnection.isInternetAvailable(RegisterActivity.this))
                {
                    RegisterUser();
                }
            }
        });*/


        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SelectImage();
                SelectImageOption();
            }
        });


    }

    @Override

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.studentbtn:

                rb1.setTypeface(null, Typeface.BOLD_ITALIC);
                rb2.setTypeface(null, Typeface.NORMAL);
                rb2.setChecked(false);
                MedicalCouncilLayout.setVisibility(View.INVISIBLE);
                DistrictLayout.setVisibility(View.INVISIBLE);
                // spItems.setVisibility(View.INVISIBLE);
                //Arrow.setVisibility(View.INVISIBLE);
                CouncileNo.setVisibility(View.INVISIBLE);
                // spItems1.setVisibility(View.INVISIBLE);
                // Arrow1.setVisibility(View.INVISIBLE);
                Image.setVisibility(View.VISIBLE);
                Text.setVisibility(View.INVISIBLE);
                Text1.setVisibility(View.INVISIBLE);
                DistrictTxt.setVisibility(View.INVISIBLE);
                StudentStatus = "true";


                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                layoutParams.setMargins(20, -650, 20, 0);
                Image.setLayoutParams(layoutParams);
                CheckBoxLayout.setVisibility(View.INVISIBLE);
                break;

            case R.id.doctorbtn:

                rb2.setTypeface(null, Typeface.BOLD_ITALIC);
                rb1.setTypeface(null, Typeface.NORMAL);
                rb1.setChecked(false);
                MedicalCouncilLayout.setVisibility(View.VISIBLE);
                DistrictLayout.setVisibility(View.VISIBLE);
                CouncileNo.setVisibility(View.VISIBLE);
                Text.setVisibility(View.VISIBLE);
                Text1.setVisibility(View.VISIBLE);
                CheckBoxLayout.setVisibility(View.VISIBLE);
                Image.setVisibility(View.INVISIBLE);
                DistrictTxt.setVisibility(View.VISIBLE);
                StudentStatus = "false";

                try {


                    SelectMedicalCouncil();
                    //SelectDistrict();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }


    private void SaveStudentPhoto() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            photo1.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba1 = Base64.encodeBytes(ba);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void SelectMedicalCouncil() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl + "pointmilamaster/getlistofMc/1");


                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");

                        }
                        final JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();
                        final String strResult = objObject.getString("listOfCouncil").toString();
                        strResult.toString();
                        if (strResult.equals("null")) {
                            //  Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            // startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("listOfCouncil").toString());

                                            final List<MedicalCouncil> list = new ArrayList<MedicalCouncil>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC = new MedicalCouncil(jarray.getJSONObject(i).getString("MedicalCouncilName"), jarray.getJSONObject(i).getString("MedicalCouncilId"), jarray.getJSONObject(i).getString("MedicalCouncilShortCode"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter = new CouncilAdapter(RegisterActivity.this, R.layout.activity_register, R.id.txt, list);
                                                    // spItems.setAdapter(adapter);

                                                    CouncilMedical.setAdapter(adapter);
                                                    CouncilMedical.setThreshold(1);
                                                }
                                            });

                                            try {

                                                CouncilMedical.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        MedicalCouncil temp = list.get(position);
                                                        MedicalCouncilId = temp.getId();

                                                        SelectedDistrict();
                                                    }
                                                });

                                            } catch (Exception e) {
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


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
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

//return null;
    }


    protected void SelectedDistrict() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl + "PointMilaMaster/GetDistrictList/" + MedicalCouncilId);


                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");

                        }
                        final JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();
                        final String strResult = objObject.getString("districtList").toString();
                        strResult.toString();
                        if (strResult.equals("null")) {
                            //  Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            // startActivity(intent);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("districtList").toString());

                                            final List<MedicalCouncilDistrict> list = new ArrayList<MedicalCouncilDistrict>();

                                            //  DistrictList = new ArrayList<HashMap<String,String>>();//

                                            // lstStores = new ArrayList<HashMap<String,String>>();
                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC1 = new MedicalCouncilDistrict(jarray.getJSONObject(i).getString("DistrictName"), jarray.getJSONObject(i).getInt("DistrictId"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC1);


                                                // MedicalCouncilDistrict = new HashMap<String, String>();//


                                            }

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        MedicalCouncilDistrictAdapter1 leadData = new MedicalCouncilDistrictAdapter1(RegisterActivity.this, R.layout.activity_register, R.id.txt, list);
                                                        // MedicalCouncilDistrictAdapter LeadData = new MedicalCouncilDistrictAdapter(RegisterActivity.this, DistrictList);
                                                        // spItems1.setAdapter(LeadData);
                                                        District.setAdapter(leadData);
                                                        District.setThreshold(1);


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });


                                            try {

                                                District.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        MedicalCouncilDistrict temp = list.get(position);
                                                        MedicalCouncilDistrictID = temp.getId();
                                                        SelectedDistrict();
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
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


    private void RegisterViews() {
        // etEmailAddress = (EditText) findViewById(R.id.emailET);


        FirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(FirstName, true);

            }
        });


        LastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(LastName, true);

            }
        });


        Password.addTextChangedListener(new TextWatcher() {
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


        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(Email, true);

            }
        });
        // etMobileNumber = (EditText) findViewById(R.id.mobileET);
        MobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isPhoneNumber(MobileNumber, true);

            }
        });


      /*  ConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Validation.isPasswordMatching(Password,ConfirmPassword);
            }
        });*/




       /* etDevice = (EditText) findViewById(R.id.deviceET);
        etDevice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isDevice(etDevice, true);

            }
        });*/
       /* etconfirmPassword=(EditText) findViewById(R.id.confirmET);
        etconfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                Validation.isPasswordMatching(etconfirmPassword, etPassword);
            }
        });*/

        Register = (Button) findViewById(R.id.registerbtn);
        Register.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View view) {
                                            if (checkValidation()) {
                                                if (RadioGender.getCheckedRadioButtonId() != -1) {
                                                    if (RadioG.getCheckedRadioButtonId() != -1) {
                                                        progressDialog = ProgressDialog.show(RegisterActivity.this, "", "Please wait");
                                                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                       /*  Thread timerthread = new Thread() {
                                                             public void run() {
                                                                 try {
                                                                     sleep(8000);
                                                                 } catch (Exception e) {
                                                                     Log.e("tag", e.getMessage());
                                                                 }
                                                                 progressDialog.dismiss();

                                                             }
                                                         };
                                                         timerthread.start();*/


                                                        if (DetectConnection.isInternetAvailable(RegisterActivity.this)) {

                                                            try {
                                                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                                                    return;
                                                                }
                                                                mLastClickTime = SystemClock.elapsedRealtime();
                                                            } catch (Exception e) {

                                                            }

                                                            SaveStudentPhoto();
                                                            RegisterUser();
                                                        } else {
                                                            InternetMessage();
                                                        }
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, " Please Select Student or MBBS ", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, " Please Select Gender ", Toast.LENGTH_LONG).show();
                                                }

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Plz check the fields", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    }
        );

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


    protected void RegisterUser() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "Registration/RegisterUser");
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserFirstName", FirstName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("UserMiddleName", MiddleName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("UserLastName", LastName.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("UserMobileNumber", MobileNumber.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("EmailId", Email.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("Password", Password.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("ReferralCode", ReferalCode.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("StudentIdCard", ba1));
                    nameValuePairs.add(new BasicNameValuePair("IsStudent", StudentStatus));
                    nameValuePairs.add(new BasicNameValuePair("TypeofPractise", "1"));
                    nameValuePairs.add(new BasicNameValuePair("MedicalCouncilId", MedicalCouncilId));
                    nameValuePairs.add(new BasicNameValuePair("DistrictId", Integer.toString(MedicalCouncilDistrictID)));
                    nameValuePairs.add(new BasicNameValuePair("MedicalCouncilNumber", CouncileNumber.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("IntrestedToBeASpeaker", CheckBoxStatus));
                    nameValuePairs.add(new BasicNameValuePair("FromAndroid", "true"));
                    nameValuePairs.add(new BasicNameValuePair("DeviceId", General.deviceid));
                    nameValuePairs.add(new BasicNameValuePair("IsMale", GenderStatus));


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
                        final String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true")) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            General.GUID = Objobject.getString("result").toString();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Intent i = new Intent(getApplicationContext(), OTPVerifyActivity.class);
                                        RegisterActivity.this.startActivity(i);
                                        RegisterActivity.this.finish();

                                        //MsgBox(strResult);

                                        //  MsgBox();

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
                                    MsgBox(strResult);
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


    public void MsgBox(String strResult) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(strResult);
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


    private void SelectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera Image URI:" + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CROPING_CODE) {
            try {
                if (outPutFile.exists()) {
                    photo1 = decodeFile(outPutFile);
                    Photo.setImageBitmap(photo1);

                } else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void CropingIMG() {
        try {
            final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
            int size = list.size();
            if (size == 0) {
                Toast.makeText(this, "can't find image cropping  app", Toast.LENGTH_SHORT).show();
                return;
            } else {
                intent.setData(mImageCaptureUri);
                // intent.putExtra("outputX",256);
                // intent.putExtra("outputY",256);
                // intent.putExtra("aspectX",1);
                // intent.putExtra("aspectY",1);
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
                        co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                        co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);
                        co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        cropOptions.add(co);
                    }

                    CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                getContentResolver().delete(mImageCaptureUri, null, null);
                                mImageCaptureUri = null;
                            }
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {

        }

        return null;
    }

    private void SelectImage() {
        try {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (options[which].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    } else if (options[which].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[which].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isName(FirstName, true)) ret = false;
        // if (!Validation.isName(MiddleName,true))ret=false;
        if (!Validation.isName(LastName, true)) ret = false;
        // if(!Validation.isName(Address,true))ret = false;
        if (!Validation.isPhoneNumber(MobileNumber, true)) ret = false;
        // if(!Validation.isPincode(Pincode,true))ret=false;
        if (!Validation.isEmailAddress(Email, true)) ret = false;
        if (!Validation.isName(Password, true)) ret = false;
        // if (!Validation.isPasswordMatching(Password,ConfirmPassword))ret=false;
        // if (!Validation.isEmailAddress(Re_EmailId,true))ret=false;
        // if (!Validation.isPhoneNumber(Re_MobileNo,true))ret=false;

        // if (!Validation.isEmailAddress(username, true))ret = false;
        //  if (!Validation.isPassword(passw, true))ret = false;
        // if(Validation.isDevice(etDevice,true))ret=false;

        return ret;
    }


    //  @Override

   /* protected void onActivityResult(int requestcode,int resultCode,Intent data)
    {
        super.onActivityResult(requestcode,resultCode,data);
        if (resultCode == RESULT_OK) {
            if (requestcode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                try {
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try {
                    //Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    thumbnail = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    Photo.setImageBitmap(thumbnail);


                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera";
                    //String path= "DCIM"+File.separator+"Camera";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");//
                    try {
                        outFile = new FileOutputStream(file);
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestcode == 2) {

                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    Photo.setImageBitmap(thumbnail);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                //upload=ImgView.setImageBitmap(thumbnail);

            }
        }


    }*/


}
