package cme.pointmila.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 11-11-2016.
 */
public class AdditionalQualification_Update extends ActionBarActivity
{

    AutoCompleteTextView AcademicRecord,StateAutoET,CityAutoET,CollageNameAutoET;
    State objMC;
    City objMC1;
    CityAdapter adapter1;
    StateAdapter adapter;
    Integer SelectedStateID;
    String SelectedStateName;
    Integer SelectedCityID;
    String SelectedCityName;
    Button Update,Delete;
    //Spinner AcademicProgram;
    AcademicProgAdapter adapter2;
    Collage objMC2;
    CollageAdapter adapter3;

    String CollageId;

    List<HashMap<String,String>> lstAcademicProgm;
    HashMap<String, String> hmAcademicProgram=null;
    String ProgramDescription,ProgramID;
    Integer selectedAcademicID;

    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private File outPutFile = null;

    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";
    ImageView Photo;

    Button UploadPhoto;
    Bitmap photo1;
    EditText DateOfPassing;
    String ba1;

    Calendar cDate;
    final int Date_Dialog_ID=0;
    int cDay,cMonth,cYear;
    int sDay,sMonth,sYear;

    String QualificationID;
    JSONObject Objobject;
    //Integer i;
    int i ;


    String  CollageID;
    String StateID;
     String StateName;
    String CityID;
     String CityName;
    String QTypeid;
    String QTypeName;
    String CollegeName;
    AcademicPrograms objMC3;
    AcademicProgramAdapter adapter5;
    AutoCompleteTextView AcademicProgramAutoET;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additionalqualification_update);

        Intent i = getIntent();
        QualificationID = i.getStringExtra("QualifyId");

        AcademicProgramAutoET = (AutoCompleteTextView)findViewById(R.id.academicprog);

       // AcademicProgram = (Spinner)findViewById(R.id.academicprogdropdown);
        StateAutoET = (AutoCompleteTextView)findViewById(R.id.edit_ip1);
        CityAutoET = (AutoCompleteTextView)findViewById(R.id.edit_ip2);
        CollageNameAutoET = (AutoCompleteTextView)findViewById(R.id.edit_ip);
        Photo = (ImageView)findViewById(R.id.photo);
        Update = (Button)findViewById(R.id.addbtn);
        Delete = (Button)findViewById(R.id.deletebtn);
       // UploadPhoto = (Button)findViewById(R.id.selectphoto);
        DateOfPassing = (EditText)findViewById(R.id.ETYear);

        DateOfPassing.setFocusable(false);
        DateOfPassing.setClickable(true);




        Picasso.with(this)
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAdditional_Qualification();
            }
        });


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveImage();
                //SaveAdditionalQualification();

                UpdateAdditionalQualification();

            }
        });

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // SaveImage();
                SelectImageOption();
            }
        });

        DateOfPassing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Date_Dialog_ID);
                updateWorkDateDisplay(sYear, sMonth, sDay);

            }
        });

        cDate=Calendar.getInstance();
        cDay= cDate.get(Calendar.DAY_OF_MONTH);
        cMonth= cDate.get(Calendar.MONTH);
        cYear= cDate.get(Calendar.YEAR);

        sDay=cDay;
        sMonth=cMonth;
        sYear=cYear;

        SelectAcademicProgram();
        SelectState();

        GetAdditionalQualification();



    }



    @Override

    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case Date_Dialog_ID:
                DatePickerDialog dialog = new DatePickerDialog(this,onDateset,cYear,cMonth,cDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;

            // return new DatePickerDialog(this,onDateset,cYear,cMonth,cDay);

        }
        return null;
    }


    private void updateWorkDateDisplay(int year,int month,int date)
    {
      //  DateOfPassing.setText(date + "-" + (month + 1 ) + "-" + year);

        try
        {
            String DateOfPassing1 = String.valueOf(date) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateformat = sdf.parse(DateOfPassing1);
            // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
            //  String finalDate = timeformat.format(date);
            DateOfPassing.setText(General.ConvertDateFormat(dateformat));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private DatePickerDialog.OnDateSetListener onDateset = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;
            updateWorkDateDisplay(sYear,sMonth,sDay);
        }
    };


    protected void DeleteAdditional_Qualification()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"DoctorProfile/DeleteAdditionalQualification/" + QualificationID);

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
                                      //  Intent i = new Intent(getApplicationContext(),ListOfPoints.class);
                                       // PointsEditing.this.startActivity(i);
                                        //PointsEditing.this.finish();

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

                                   // DeleteMsg();

                                   // Intent i = new Intent(getApplicationContext(),ListOfPoints.class);
                                   // PointsEditing.this.startActivity(i);
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


    protected  void UpdateAdditionalQualification()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"DoctorProfile/UpdateAdditionalQualification");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("AdditionalQualificationId",QualificationID));
                    if (CollageId==null)
                    {
                        nameValuePairs.add(new BasicNameValuePair("CollegeId",CollageID));
                    }
                    else {
                        nameValuePairs.add(new BasicNameValuePair("CollegeId",CollageId));
                    }
                    if (selectedAcademicID == null)
                    {
                        nameValuePairs.add(new BasicNameValuePair("DoctorAcademicProgramId",QTypeid));

                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("DoctorAcademicProgramId", Integer.toString(selectedAcademicID)));

                    }
                    nameValuePairs.add(new BasicNameValuePair("DateOfPassing",DateOfPassing.getText().toString()));

                    nameValuePairs.add(new BasicNameValuePair("QualificationCertificateString",ba1));

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
                        // final     String strResult = Objobject.getString("Message").toString();

                        if (strSuccess.equals("true"))
                        {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            UpdateMsg();

                                           //  Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                                            // startActivity(i);


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

                        else
                        {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ErrorMsg();

                                    // MSG(strResult);
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

    private void SaveImage()
    {
        try
        {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            photo1.compress(Bitmap.CompressFormat.JPEG,50,bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba1 = Base64.encodeBytes(ba);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void SelectImageOption()
    {
        final CharSequence[] items = {"Capture Photo","Choose from Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AdditionalQualification_Update.this);
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
                    startActivityForResult(intent,CAMERA_CODE);
                }

                else if (items[item].equals("Choose from Gallery"))
                {
                    Intent  i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,GALLERY_CODE);
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == GALLERY_CODE && resultCode ==RESULT_OK && null!=data)
        {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" +mImageCaptureUri);
            CropingIMG();
        }

        else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK)
        {
            System.out.println("Camera Image URI:" +mImageCaptureUri);
            CropingIMG();
        }

        else if (requestCode == CROPING_CODE)
        {
            try
            {
                if (outPutFile.exists())
                {
                    photo1 = decodeFile(outPutFile);
                    Photo.setImageBitmap(photo1);

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Error while save image",Toast.LENGTH_SHORT).show();
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
                intent.putExtra("scale",true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outPutFile));
                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = (ResolveInfo) list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    startActivityForResult(i,CROPING_CODE);
                }
                else {
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

    public void ErrorMsg()
    {
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("academicProgram").toString());

                                            final List<AcademicPrograms> list = new ArrayList<AcademicPrograms>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC3 = new AcademicPrograms(jarray.getJSONObject(i).getString("DoctorAcademicProgramDescription"), jarray.getJSONObject(i).getInt("DoctorAcademicProgramId"));
                                                list.add(objMC3);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter5 = new AcademicProgramAdapter(AdditionalQualification_Update.this, R.layout.activity_residentialaddress, R.id.txt, list);
                                                    // adapter.appContext = getApplicationContext();
                                                    // adapter.tmpActivity = ResidentialAddress.this;
                                                    // spItems.setAdapter(adapter);

                                                    AcademicProgramAutoET.setAdapter(adapter5);
                                                    AcademicProgramAutoET.setThreshold(1);
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
                                                AcademicProgramAutoET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        AcademicPrograms temp = list.get(position);
                                                        selectedAcademicID= temp.getId();
                                                        //SelectedStateName = temp.getName();

                                                        // StateId = list.get(position)
                                                       // SelectCity();
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


                                                    adapter = new StateAdapter(AdditionalQualification_Update.this, R.layout.activity_additionalqualification_update, R.id.txt, list);
                                                    // adapter.appContext = getApplicationContext();
                                                    // adapter.tmpActivity = ResidentialAddress.this;
                                                    // spItems.setAdapter(adapter);

                                                    StateAutoET.setAdapter(adapter);
                                                    StateAutoET.setThreshold(1);
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
                                                        adapter1 = new CityAdapter(AdditionalQualification_Update.this, R.layout.activity_additionalqualification_update, R.id.txt, list);
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
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetCollegeList/"+SelectedCityID);


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
                            runOnUiThread(new Runnable() {
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

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    try
                                                    {
                                                        adapter3 = new CollageAdapter(AdditionalQualification_Update.this, R.layout.activity_additionalqualification_update, R.id.txt, list);
                                                        // adapter2.appContext = getActivity().getApplicationContext();
                                                        //adapter2.tmpActivity = AcademicRecordTabFragment.this;
                                                        // spItems.setAdapter(adapter);

                                                        CollageNameAutoET.setAdapter(adapter3);
                                                        CollageNameAutoET.setThreshold(1);

                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });




                                            try {



                                                CollageNameAutoET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    protected void GetAdditionalQualification()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl +"DoctorProfile/GetAdditionalQualification/" + QualificationID);
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

                        final  String strSuccess = Objobject.getString("Success").toString();
                        final String add = Objobject.getString("Image").toString();
                        final String pureBase64Encoded = add.substring(add.indexOf(",")+1);
                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded);
                        final Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // Image.setImageBitmap(decodedBitmap);
                                Photo.setImageBitmap(decodedBitmap);
                            }
                        });


                        if (strSuccess.equals("true"))
                        {
                            JSONArray jarray = new JSONArray(Objobject.getString("Data").toString());

                            try {
                                 QTypeid = jarray.getJSONObject(i).getString("QualificationTypeid");

                                 QTypeName = jarray.getJSONObject(i).getString("QualificationType");
                                 CollegeName = jarray.getJSONObject(i).getString("CollegeName");
                                //CMECourseId = jarray.getJSONObject(i).getString("CMECourseId");

                                String DeliveryDate = jarray.getJSONObject(i).getString("DateOfPassing");
                                String timestamp = DeliveryDate.replace("/Date(", "").replace(")/", "");
                                Date createdon = new Date(Long.parseLong(timestamp));
                                SimpleDateFormat sdf = new SimpleDateFormat("dd -MMM- yyyy");
                                final    String formattedDate = sdf.format(createdon);
                                CollageID = jarray.getJSONObject(i).getString("CollegeId");
                                StateID = jarray.getJSONObject(i).getString("StateId");
                                StateName = jarray.getJSONObject(i).getString("StateName");
                                CityID = jarray.getJSONObject(i).getString("Cityid");
                                CityName = jarray.getJSONObject(i).getString("CityMaster");


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        StateAutoET.setText(StateName);
                                        CityAutoET.setText(CityName);
                                        CollageNameAutoET.setText(CollegeName);
                                        DateOfPassing.setText(formattedDate);
                                        AcademicProgramAutoET.setText(QTypeName);




                                       // CMEDate.setText(formattedDate);
                                     //   CMEPoints.setText(CmePoints);
                                     //   CMENo.setText(CMENumber);
                                        try {

                                           // CourseType.setText(courseType);

                                            // CourseType.
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }
                                });


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



    public void UpdateMsg()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Successfully Save");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                startActivity(i);
                dialog.cancel();
                // progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }

    public void DeleteMsg()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Successfully Deleted");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                startActivity(i);
                dialog.cancel();
                // progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }


}
