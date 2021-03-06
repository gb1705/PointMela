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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by amoln on 16-11-2016.
 */
public class ThirdYearPointEditingActivity extends ActionBarActivity {
    String PointID;
    EditText CMENo, CMEDate, CMEPoints;
    AutoCompleteTextView CourseType;
    JSONObject Objobject;
    Button Deletebtn;
    Button Savebtn;
    CourseType objMC;
    CourseTypeAdapter adapter;
    String CourseTypeId;
    URL url = null;
    Bitmap image;
    ImageView Image;
    ProgressDialog progressDialog;
    Button SelectPhoto;

    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    Bitmap Photo1;
    private File outPutFile = null;
    String ba1;
    String CourseTypeName;

    Calendar cDate;
    final int Date_Dialog_ID = 0;
    int cDay, cMonth, cYear;
    int sDay, sMonth, sYear;
    String CMECourseId;

    AutoCompleteTextView MedicalCouncilAutoET;
    MedicalCouncil objMC5;
    CouncilAdapter adapter4;
    String MedicalCouncilId;
    EditText MMC;
    String MCShortCode;
    String CourseTypeShortCode;
    String MMCText;
    String FinalInputString;
    String MedicalCouncilid;
    String MedicalCouncileName;


    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointediting);

        Intent i = getIntent();
        PointID = i.getStringExtra("PointId");

        // PointID = getArguments().getString("PointId");
        CMENo = (EditText) findViewById(R.id.cmeno);
        CMEDate = (EditText) findViewById(R.id.dateET);
        CMEPoints = (EditText) findViewById(R.id.Cmepoint);
        CourseType = (AutoCompleteTextView) findViewById(R.id.edit_ip);
        Deletebtn = (Button) findViewById(R.id.deletebtn);
        Savebtn = (Button) findViewById(R.id.savebtn);
        Image = (ImageView) findViewById(R.id.photo);


        MedicalCouncilAutoET = (AutoCompleteTextView) findViewById(R.id.edit_ip1);
        MMC = (EditText) findViewById(R.id.address);
        MMC.setFocusable(false);
        MMC.setClickable(false);

        //  SelectPhoto = (Button)findViewById(R.id.selectphoto);

        CMEDate.setFocusable(false);
        CMEDate.setClickable(true);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        SelectCourceType();
        GetSelectedListInfo();

        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SelectImage();

                SelectImageOption();
            }
        });

        SelectMedicalCouncil();


        Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = ProgressDialog.show(ThirdYearPointEditingActivity.this, "", "Deleting..Please Wait");
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
                if (DetectConnection.isInternetAvailable(ThirdYearPointEditingActivity.this)) {
                    // LoginUser();
                    DeleteSelectedListItem();
                } else {
                    InternetMessage();
                }


            }
        });


        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveCMECertificateImage();


                progressDialog = ProgressDialog.show(ThirdYearPointEditingActivity.this, "", "Saving..Please Wait");
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
                if (DetectConnection.isInternetAvailable(ThirdYearPointEditingActivity.this)) {
                    // LoginUser();
                    //DeleteSelectedListItem();
                    FinalInputString = MMC.getText().toString() + "000000".substring(0, (6 - CMENo.getText().toString().length())) + CMENo.getText().toString();
                    SaveEditData();
                } else {
                    InternetMessage();
                }


            }
        });

        CMEDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectDate();
                showDialog(Date_Dialog_ID);
                updateWorkDateDisplay(sYear, sMonth, sDay);
            }
        });

        cDate = Calendar.getInstance();
        cDay = cDate.get(Calendar.DAY_OF_MONTH);
        cMonth = cDate.get(Calendar.MONTH);
        cYear = cDate.get(Calendar.YEAR);

        sDay = cDay;
        sMonth = cMonth;
        sYear = cYear;

        try {
            findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.takeScreenshot(ThirdYearPointEditingActivity.this, v, "Edited the points for following");
                }
            });

        } catch (Exception e) {

        }


    }

    @Override

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Date_Dialog_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, onDateset, cYear, cMonth, cDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;

            // return new DatePickerDialog(this,onDateset,cYear,cMonth,cDay);

        }
        return null;
    }

    private void updateWorkDateDisplay(int year, int month, int date) {


        //CMEDate.setText(date + "-" + (month + 1 ) + "-" + year);

        try {
            String DateOfPassing1 = String.valueOf(date) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateformat = sdf.parse(DateOfPassing1);
            // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
            //  String finalDate = timeformat.format(date);
            CMEDate.setText(General.ConvertDateFormat(dateformat));

        } catch (Exception e) {
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
            updateWorkDateDisplay(sYear, sMonth, sDay);

            MMCText = MCShortCode + "/" + sYear + "/" + CourseTypeShortCode + "-";
            MMC.setText(MMCText);

        }
    };


    private void SaveCMECertificateImage() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            Photo1.compress(Bitmap.CompressFormat.JPEG, 50, bao);
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

                                                objMC5 = new MedicalCouncil(jarray.getJSONObject(i).getString("MedicalCouncilName"), jarray.getJSONObject(i).getString("MedicalCouncilId"), jarray.getJSONObject(i).getString("MedicalCouncilShortCode"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC5);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter4 = new CouncilAdapter(ThirdYearPointEditingActivity.this, R.layout.activity_points, R.id.txt, list);
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
                                                        MedicalCouncilId = temp.getId();
                                                        MCShortCode = temp.getShortcode();
                                                        MMCText = MCShortCode + "/" + sYear + "/" + CourseTypeShortCode + "-";
                                                        MMC.setText(MMCText);

                                                        // SelectedDistrict();
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


    private void SelectImageOption() {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                    Photo1 = decodeFile(outPutFile);
                    Image.setImageBitmap(Photo1);
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

    public void SaveEditData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "CMEPoints/UpdateCMEPoints");
                    httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("CMEPointId", PointID));
                    nameValuePairs.add(new BasicNameValuePair("CMENumber", CMENo.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CMENumberPrefix", MMC.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CMEDate", CMEDate.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("CMEPoints", CMEPoints.getText().toString()));
                    if (MedicalCouncilId == null) {
                        nameValuePairs.add(new BasicNameValuePair("MedicalCouncilId", MedicalCouncilid));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("MedicalCouncilId", MedicalCouncilId));
                    }


                    if (CourseTypeId == null) {
                        nameValuePairs.add(new BasicNameValuePair("CMECourseId", CMECourseId));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("CMECourseId", CourseTypeId));
                    }
                    nameValuePairs.add(new BasicNameValuePair("CMECertificate", ba1));

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
                        // final     String strResult = Objobject.getString("Message").toString();

                        if (strSuccess.equals("true")) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            Intent i = new Intent(getApplicationContext(), ThirdYearCountActvity.class);
                                            ThirdYearPointEditingActivity.this.startActivity(i);
                                            ThirdYearPointEditingActivity.this.finish();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


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

                                    // MSG(strResult);
                                    //  MsgBox(strResult);
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

    protected void SelectCourceType() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl + "PointMilaMaster/GetCMECourse/1");


                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");

                        }
                        final JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();
                        final String strResult = objObject.getString("cmeCourses").toString();
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

                                            JSONArray jarray = new JSONArray(objObject.getString("cmeCourses").toString());

                                            final List<CourseType> list = new ArrayList<CourseType>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                objMC = new CourseType(jarray.getJSONObject(i).getString("CMECourseDescription"), jarray.getJSONObject(i).getString("CMECourseId"), jarray.getJSONObject(i).getString("ShortCode"));
                                                // objMC.setName("MedicalCouncilName");
                                                //objMC.getId("")
                                                list.add(objMC);

                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter = new CourseTypeAdapter(ThirdYearPointEditingActivity.this, R.layout.activity_points, R.id.txt, list);

                                                    CourseType.setAdapter(adapter);
                                                    CourseType.setThreshold(1);
                                                }
                                            });

                                            try {

                                                CourseType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        CourseType temp = list.get(position);
                                                        CourseTypeId = temp.getId();
                                                        CourseTypeName = temp.getName();

                                                        CourseTypeShortCode = temp.getShortcode();
                                                        MMCText = MCShortCode + "/" + sYear + "/" + CourseTypeShortCode + "-";
                                                        MMC.setText(MMCText);

                                                        // SelectedDistrict();
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

//return null;
    }


    protected void GetSelectedListInfo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "CMEPoints/GetCMEPointInfo/" + PointID);
                    // httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuffer builder = new StringBuffer();
                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");
                        }
                        Objobject = (JSONObject) new JSONTokener(builder.toString()).nextValue();

                        final String strSuccess = Objobject.getString("Success").toString();
                        final String add = Objobject.getString("Image").toString();
                        final String pureBase64Encoded = add.substring(add.indexOf(",") + 1);
                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded);
                        final Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Image.setImageBitmap(decodedBitmap);
                            }
                        });


                        if (strSuccess.equals("true")) {
                            JSONArray jarray = new JSONArray(Objobject.getString("Data").toString());

                            try {
                                String PointID = jarray.getJSONObject(i).getString("PointsId");

                                final String courseType = jarray.getJSONObject(i).getString("CMECourseDescription");
                                final String CMENumber = jarray.getJSONObject(i).getString("CMENumber");
                                CMECourseId = jarray.getJSONObject(i).getString("CMECourseId");
                                final String CMEPrefix = jarray.getJSONObject(i).getString("CMENumberPrefix");
                                MedicalCouncilid = jarray.getJSONObject(i).getString("MedicalCouncilId");
                                MedicalCouncileName = jarray.getJSONObject(i).getString("MedicalCouncilName");
                                String DeliveryDate = jarray.getJSONObject(i).getString("CMEDate");
                                String timestamp = DeliveryDate.replace("/Date(", "").replace(")/", "");
                                Date createdon = new Date(Long.parseLong(timestamp));
                                SimpleDateFormat sdf = new SimpleDateFormat("dd -MMM- yyyy");
                                final String formattedDate = sdf.format(createdon);
                                final String CmePoints = jarray.getJSONObject(i).getString("CMEPoints");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        CMEDate.setText(formattedDate);
                                        CMEPoints.setText(CmePoints);
                                        CMENo.setText(CMENumber);
                                        try {

                                            CourseType.setText(courseType);
                                            MedicalCouncilAutoET.setText(MedicalCouncileName);
                                            MMC.setText(CMEPrefix);


                                            // CourseType.
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //  }


                        } else {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

    protected void DeleteSelectedListItem() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "CMEPoints/DeleteCMEPoints/" + PointID);

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
                            //  JSONArray jarray = new JSONArray(Objobject.getString("Data").toString());
                            //  lstBooking = new ArrayList<HashMap<String, String>>();


                            // for (int i = 0; i < jarray.length(); i++) {
                            //  hmBooking = new HashMap<String, String>();

                            try {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getApplicationContext(), ThirdYearCountActvity.class);
                                        ThirdYearPointEditingActivity.this.startActivity(i);
                                        ThirdYearPointEditingActivity.this.finish();

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


                        } else {
                            // General.Token = Objobject.getString("Token").toString();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //    Intent i = new Intent(getApplicationContext(),ThirdYearCountActvity.class);
                                    //   ThirdYearPointEditingActivity.this.startActivity(i);
                                    //  ThirdYearPointEditingActivity.this.finish();


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


}
