package cme.pointmila.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.List;

/**
 * Created by amoln on 23-09-2016.
 */
public class AddPoints extends ActionBarActivity
{

    EditText DateET;
    ImageView Photo;
    Button SelectPhoto;
    String picturePath;
    private Bitmap thumbnail;
    private Spinner spItems;
    ArrayAdapter arrayAdapter;
    List<String> listData = new ArrayList<String>();
    ImageView Arrow;
    String CourseTypeId;

    Calendar cDate;
    final int Date_Dialog_ID=0;
    int cDay,cMonth,cYear;
    int sDay,sMonth,sYear;

    ProgressDialog progressDialog;


    Button Save;

    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101,GALLERY_CODE = 201,CROPING_CODE = 301;
    private   File outPutFile = null;
    AutoCompleteTextView CourceType;
    CourseType objMC;
    CourseTypeAdapter adapter;
    EditText CMENumber,CmePoint;
    Bitmap Photo1;
    String ba1;
    ImageView Image;
    boolean Uploadcertificate = false;
    AutoCompleteTextView MedicalCouncilAutoET;
    EditText MMC;
    MedicalCouncil objMC5;
    CouncilAdapter adapter4;
    String MedicalCouncilId;
    String CourseTypeShortCode;
    String MCShortCode;
    String MMCText;
    String FinalInputString;

    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        DateET = (EditText)findViewById(R.id.dateET);
        DateET.setFocusable(false);
        DateET.setClickable(true);
        Photo = (ImageView)findViewById(R.id.photo);
       // SelectPhoto = (Button)findViewById(R.id.selectphoto);
        CourceType = (AutoCompleteTextView)findViewById(R.id.edit_ip);
        CMENumber = (EditText)findViewById(R.id.cmeno);
        CmePoint = (EditText)findViewById(R.id.Cmepoint);
        MedicalCouncilAutoET = (AutoCompleteTextView) findViewById(R.id.edit_ip1);
        MMC = (EditText)findViewById(R.id.address);
        MMC.setFocusable(false);
        MMC.setClickable(false);

        Picasso.with(this)
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
        Save = (Button)findViewById(R.id.savebtn);


        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SelectImage();

                SelectImageOption();
            }
        });


        SelectCourceType();

        DateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(Date_Dialog_ID);
                updateWorkDateDisplay(sYear, sMonth, sDay);
               // selectDate();
            }
        });

        cDate=Calendar.getInstance();
        cDay= cDate.get(Calendar.DAY_OF_MONTH);
        cMonth= cDate.get(Calendar.MONTH);
        cYear= cDate.get(Calendar.YEAR);

        sDay=cDay;
        sMonth=cMonth;
        sYear=cYear;
       // updateWorkDateDisplay(sYear, sMonth, sDay);

        spItems = (Spinner)findViewById(R.id.spitem);


        SelectMedicalCouncil();

        AddPointsView();
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

    protected void SelectMedicalCouncil()
    {
        Thread thread = new  Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"pointmilamaster/getlistofMc/1");


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
                        final String strResult = objObject.getString("listOfCouncil").toString();
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

                                            JSONArray jarray = new JSONArray(objObject.getString("listOfCouncil").toString());

                                            final List<MedicalCouncil> list = new ArrayList<MedicalCouncil>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC5 = new MedicalCouncil(jarray.getJSONObject(i).getString("MedicalCouncilName"), jarray.getJSONObject(i).getString("MedicalCouncilId"),jarray.getJSONObject(i).getString("MedicalCouncilShortCode"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC5);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter4 = new CouncilAdapter(AddPoints.this, R.layout.activity_points, R.id.txt, list);
                                                    // spItems.setAdapter(adapter);

                                                    MedicalCouncilAutoET.setAdapter(adapter4);
                                                    MedicalCouncilAutoET.setThreshold(1);
                                                }
                                            });

                                            try {

                                                MedicalCouncilAutoET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        MedicalCouncil temp = list.get(position);
                                                        MedicalCouncilId= temp.getId();
                                                        MCShortCode = temp.getShortcode();
                                                        MMCText = MCShortCode + "/" + sYear + "/" + CourseTypeShortCode + "-" ;
                                                        MMCText = MMCText.replace("null","");
                                                        MMC.setText(MMCText);

                                                       // SelectedDistrict();
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

//return null;
    }


    public void SuccessfullyAddPointMsg()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Successfully Add Points");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                AddPoints.this.startActivity(i);
                AddPoints.this.finish();

                dialog.cancel();
                progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }


    private void updateWorkDateDisplay(int year,int month,int date)
    {



      //  DateET.setText(date + "-" + (month + 1 ) + "-" + year);

        try
        {
            String DateOfPassing1 = String.valueOf(date) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateformat = sdf.parse(DateOfPassing1);
            // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
            //  String finalDate = timeformat.format(date);
            DateET.setText(General.ConvertDateFormat(dateformat));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        //CertificStartDate.setText(date + "-" + (month + 1) + "-" + year);
        //endDate.setText(date + "-" +(month + 2) + "-" + year);
    }

    private DatePickerDialog.OnDateSetListener onDateset = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;
            updateWorkDateDisplay(sYear,sMonth,sDay);
            MMCText = MCShortCode + "/" + sYear + "/" + CourseTypeShortCode + "-" ;
            MMCText = MMCText.replace("null","");
            MMC.setText(MMCText);
        }
    };


    private void SelectImageOption()
    {
        try
        {
            final CharSequence[] items = {"Capture Photo","Choose from Gallery","Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                  //  boolean result = Utility.checkPermission(AddPoints.this);
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
                        Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode ==RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera Image URI:" + mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CROPING_CODE) {
            try {
                if (outPutFile.exists()) {
                     Photo1 = decodeFile(outPutFile);
                    Photo.setImageBitmap(Photo1);
                    Uploadcertificate = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
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
                Toast.makeText(AddPoints.this, "can't find image cropping  app", Toast.LENGTH_SHORT).show();
                return;
            } else {
                intent.setData(mImageCaptureUri);
               // intent.putExtra("outputX", 512);
               // intent.putExtra("outputY", 512);
               // intent.putExtra("aspectX", 1);
               // intent.putExtra("aspectY", 1);
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



    private void SaveCMECertificateImage()
    {
        try
        {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            Photo1.compress(Bitmap.CompressFormat.JPEG,50,bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba1 = Base64.encodeBytes(ba);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    protected void Addpoints()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+ "CMEPoints/AddCMEPoints");
                    httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId",General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("CMENumber",FinalInputString));
                    nameValuePairs.add(new BasicNameValuePair("CMENumberPrefix",MMC.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CMEDate",DateET.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CMEPoints",CmePoint.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CMECourseId",CourseTypeId));
                    nameValuePairs.add(new BasicNameValuePair("CMECertificate",ba1));
                    nameValuePairs.add(new BasicNameValuePair("MedicalCouncilId",MedicalCouncilId));


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
                        final     String strResult = Objobject.getString("Message").toString();

                        if (strSuccess.equals("true"))
                        {
                            try {
                               runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            MsgBox(strResult);

                                           // General.GUID = Objobject.getString("result").toString();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                       // Intent i = new Intent(getActivity().getApplicationContext(), OTPVerifyActivity.class);
                                       // startActivity(i);

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
                           runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    MSG(strResult);
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


    public void MsgBox(String strResult)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(strResult);
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(getApplicationContext(),ProfileDashboardActivity.class);
                AddPoints.this.startActivity(i);
                AddPoints.this.finish();
                dialog.cancel();

                //progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }

    public void MSG(String strResult)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(strResult);
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

    protected void SelectCourceType()
    {
        Thread thread = new  Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl+"PointMilaMaster/GetCMECourse/1");


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
                        final String strResult = objObject.getString("cmeCourses").toString();
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

                                            JSONArray jarray = new JSONArray(objObject.getString("cmeCourses").toString());

                                            final List<CourseType> list = new ArrayList<CourseType>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC = new CourseType(jarray.getJSONObject(i).getString("CMECourseDescription"), jarray.getJSONObject(i).getString("CMECourseId"),jarray.getJSONObject(i).getString("ShortCode"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter = new CourseTypeAdapter(AddPoints.this, R.layout.activity_points, R.id.txt, list);

                                                    CourceType.setAdapter(adapter);
                                                    CourceType.setThreshold(1);

                                                }
                                            });

                                            try {

                                                CourceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        CourseType temp = list.get(position);
                                                        CourseTypeId= temp.getId();
                                                        CourseTypeShortCode = temp.getShortcode();


                                                        MMCText = MCShortCode + "/" + sYear + "/" + CourseTypeShortCode + "-" ;
                                                        MMCText = MMCText.replace("null","");
                                                        MMC.setText(MMCText);
                                                       // SelectedDistrict();
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

//return null;
    }

    public void AddPointsView()
    {



        DateET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(DateET, true);

            }
        });
        // etMobileNumber = (EditText) findViewById(R.id.mobileET);
        CourceType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(CourceType, true);

            }
        });

        CMENumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(CMENumber, true);

            }
        });

        CmePoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(CmePoint, true);

            }
        });


        MedicalCouncilAutoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(MedicalCouncilAutoET, true);

            }
        });



        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    if (Uploadcertificate ==false)
                    {
                        Toast.makeText(getApplicationContext(), "Please Upload the  Certificate", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        FinalInputString  = "000000".substring(0,(6- CMENumber.getText().toString().length()))+CMENumber.getText().toString();


                        progressDialog = ProgressDialog.show(AddPoints.this, "", "Please wait");
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
                        if (DetectConnection.isInternetAvailable(AddPoints.this)) {
                            SaveCMECertificateImage();
                            Addpoints();

                        } else {
                            InternetMessage();
                        }
                    }


                }

            }
        });


    }


    private  boolean checkValidation()
    {
        boolean ret = true;
        if (!Validation.isName(DateET,true))ret=false;
        if (!Validation.isName(CourceType,true))ret=false;
        if (!Validation.isName(CMENumber,true))ret = false;
        if (!Validation.isName(CmePoint,true))ret = false;
        if (!Validation.isName(MedicalCouncilAutoET,true))ret= false;

        return  ret;
    }



   /* private void selectDate()
    {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");

    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            DateET.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));


        }
    };*/





}
