package cme.pointmila.com;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
import android.widget.PopupWindow;
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
 * Created by amoln on 13-09-2016.
 */
public class StudentProfileFragment extends Fragment
{
    EditText DOB;
   // private Spinner spItems;
   // ArrayAdapter arrayAdapter;
   // List<String> listData = new ArrayList<String>();
    ImageView Arrow;
    ImageView Arrow1;
    private Spinner spItems3;
    private Spinner spItems4;
    ArrayAdapter arrayAdapter3;
    ArrayAdapter arrayAdapter4;
    //List<String> listData3 = new ArrayList<String>();
   // List<String> listData4 = new ArrayList<String>();
   // ImageView Arrow3;
  //  ImageView Arrow4;
    EditText Address;
    State objMC;
    City objMC1;
    Collage objMC2;
    StateAdapter adapter;
    CityAdapter adapter1;
    int StateId;
    int CityId;
    String CollageId;
    AutoCompleteTextView StatePopup,CityPopup;
    CollageAdapter adapter2;
    JSONObject Objobject;




    HashMap<String, String> hmState=null;
    List<HashMap<String,String>> lstState,lstCity;
    String Statename,StateID,SelectedStateID,selectedStatename,Cityname,CityID,SelectedCityID,SelectedCityname;

    String AddCollageState;
     String AddCollageStateID;
      String CollageCity;
     String CollageCityID;
      String CollageID;
     String CollageName;



    PopupWindow popupWindow;
    EditText Street1;
    EditText Street2;
    EditText Pincode;

    String StateSpinner;
    String CitySpinner;

    String SelectedState;
    String SelectedCity;

    Spinner Statespinner,Cityspinner;

    AutoCompleteTextView State,City,Collage;

    Button Submit;
    String Edit ,Edit1,Edit2;
    int i = 0;


    String AddStreet1;
    String AddStreet2;
    String AddCityName;
    String AddCityId;
    String AddStateName;
    String AddPincode;
    ImageView Photo,Photo1;

    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";

    private Uri mImageCaptureUri;
    private static final int CAMERA_CODE = 101,GALLERY_CODE = 201,CROPING_CODE = 301;
    private static final int CAMERA_CODE1 = 111,GALLERY_CODE1 = 211,CROPING_CODE1 = 311;
    private   File outPutFile = null;
    Bitmap BitPhoto1,BitPhoto2;
    String ba1;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_student_profile, null);
        DOB = (EditText)view.findViewById(R.id.dateET);
        Address = (EditText)view.findViewById(R.id.address);
        Submit = (Button)view.findViewById(R.id.submitbtn);
        Photo = (ImageView)view.findViewById(R.id.photo);
        Photo1 = (ImageView)view.findViewById(R.id.photo1);



        Address.setFocusable(false);
        Address.setClickable(true);

        Picasso.with(getActivity())
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo);

        Picasso.with(getActivity())
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo1);



        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity().getApplicationContext(), Student_ResidentialAddress.class);

                i.putExtra("addressstreet1",AddStreet1);
                i.putExtra("addressstreet2",AddStreet2);
                i.putExtra("addressstate",AddStateName);
                i.putExtra("addresscity",AddCityName);
                i.putExtra("addresscityId",AddCityId);
                i.putExtra("pincode",AddPincode);

                startActivity(i);
              //  callPop();

            }
        });

        Photo.setClickable(false);
        Photo.setFocusable(false);

       /* Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageOption();
            }
        });*/
        Photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageOption1();
            }
        });

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(),"temp.jpg");
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveStudentProfile();
                CreateStudentProfile();
            }
        });

        spItems3 = (Spinner) view.findViewById(R.id.spitem3);
       // Arrow3 = (ImageView) view.findViewById(R.id.img3);

        spItems4 = (Spinner) view.findViewById(R.id.spitem4);
        State = (AutoCompleteTextView)view.findViewById(R.id.edit_ip1);
        City = (AutoCompleteTextView)view.findViewById(R.id.edit_ip2);
        Collage = (AutoCompleteTextView)view.findViewById(R.id.edit_ip);

        SelectState();


        DOB.setFocusable(false);
        DOB.setClickable(true);

        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDialog(Date_Dialog_ID);
                selectDate();
            }
        });

        /*if (General.str.equals("1"))
        {
            GetStudentProfile();
        }*/

        GetResidentialAddress();

        GetStudentProfile();


       // CreateStudentProfile();

        return view;
    }


    private void SelectImageOption()
    {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    }
                    else if (items[item].equals("Choose from Gallery")) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_CODE);
                    } else if (items[item].equals("Cancel")) {
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
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Capture Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                        mImageCaptureUri = Uri.fromFile(f);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        startActivityForResult(intent, CAMERA_CODE1);
                    }
                    else if (items[item].equals("Choose from Gallery")) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_CODE1);
                    } else if (items[item].equals("Cancel")) {
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
                    BitPhoto1 = decodeFile(outPutFile);
                   // Photo.setImageBitmap(BitPhoto1);
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
        } else if (requestCode == CAMERA_CODE1 && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera Image URI:" + mImageCaptureUri);
            CropingIMG1();
        } else if (requestCode == CROPING_CODE1) {
            try {
                if (outPutFile.exists()) {
                    BitPhoto2 = decodeFile(outPutFile);
                    Photo1.setImageBitmap(BitPhoto2);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
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

            List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
            int size = list.size();
            if (size == 0) {
                Toast.makeText(getActivity(), "can't find image cropping  app", Toast.LENGTH_SHORT).show();
                return;
            } else {
                intent.setData(mImageCaptureUri);
                //intent.putExtra("outputX", 512);
                //intent.putExtra("outputY", 812);
                //intent.putExtra("aspectX", 2);
                //intent.putExtra("aspectY", 2);
                intent.putExtra("scale", true);
                // intent.putExtra("crop","true");

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
    private void CropingIMG1()
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
                //intent.putExtra("outputX", 512);
                //intent.putExtra("outputY", 812);
                //intent.putExtra("aspectX", 2);
                //intent.putExtra("aspectY", 2);
                intent.putExtra("scale", true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = (ResolveInfo) list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    startActivityForResult(i, CROPING_CODE1);
                } else {
                    for (ResolveInfo res : list) {
                        final CropingOption co = new CropingOption();
                        co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                        co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                        co.appIntent = new Intent(intent);
                        co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        cropOptions.add(co);
                    }

                    CropingOptionAdapter1 adapter = new CropingOptionAdapter1(getActivity().getApplicationContext(), cropOptions);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Choose Cropping App");
                    builder.setCancelable(false);
                    builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
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

            final int REQUIRED_SIZE = 212;
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


   protected void SaveStudentProfile()
   {
       try
       {
           ByteArrayOutputStream bao = new ByteArrayOutputStream();
           BitPhoto2.compress(Bitmap.CompressFormat.JPEG,50,bao);
           byte[] ba = bao.toByteArray();
           //Image = new String(ba);
           ba1 = Base64.encodeBytes(ba);

       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
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
                        AddCityId = Objobject.getString("ResidenceCityId").toString();
                        AddCityName   = Objobject.getString("ResidenceCityName").toString();
                        AddStateName = Objobject.getString("ResidenceStateName").toString();
                        AddPincode = Objobject.getString("ResidencePinCode").toString();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Address.setText(AddStreet1 + "" + "," +"" + AddStreet2 + "" +"," + "" + AddCityName + "-" +""+ AddPincode + ""+","+"" + AddStateName);
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

    protected void CreateStudentProfile()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"StudentProfile/UpdateStudentProfile");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId",General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("StudentDateOfBirth",DOB.getText().toString()));
                    if (CollageId==null)
                    {
                        nameValuePairs.add(new BasicNameValuePair("CollegeId",CollageID));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("CollegeId", CollageId));
                    }
                    nameValuePairs.add(new BasicNameValuePair("CityId",AddCityId));
                    nameValuePairs.add(new BasicNameValuePair("Address1",AddStreet1));
                    nameValuePairs.add(new BasicNameValuePair("Address2",AddStreet2));
                    nameValuePairs.add(new BasicNameValuePair("PinCode",AddPincode));
                    nameValuePairs.add(new BasicNameValuePair("StudentProfilePic",ba1));


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

                                        SavingStudentProfileMsg();
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

    protected void GetStudentProfile()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl+"StudentProfile/GetStudentProfile/"+ General.GUID);
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
                          String StudentDOB = Objobject.getString("StudentDOb").toString();
                        String timestamp = StudentDOB.replace("/Date(", "").replace(")/", "");
                        Date createdon = new Date(Long.parseLong(timestamp));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd -MM- yyyy");
                        final    String formattedDate = sdf.format(createdon);


                         AddCollageState = Objobject.getString("CollegeState").toString();
                         AddCollageStateID = Objobject.getString("CollegeStateId").toString();
                         CollageCity = Objobject.getString("CollegeCity").toString();
                         CollageCityID = Objobject.getString("CollegeCItyId").toString();
                         CollageID = Objobject.getString("CollegeId").toString();
                         CollageName = Objobject.getString("CollegeName").toString();
                        General.Street1 = Objobject.getString("UserAddress1").toString();
                        General.Street2 = Objobject.getString("UserAddress2").toString();
                        final    String UserState = Objobject.getString("UserState").toString();
                        final String UserStateID = Objobject.getString("UserStateId").toString();
                        final   String UserCity = Objobject.getString("UserCity").toString();
                        final String UserCityID = Objobject.getString("UserCityId").toString();
                        General.Pincode = Objobject.getString("UserPincode").toString();

                        final String add = Objobject.getString("StudentICardText").toString();
                        final String pureBase64Encoded = add.substring(add.indexOf(",")+1);
                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded);
                        final Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);

                        final String add1 = Objobject.getString("ProfilePicText").toString();
                        final String pureBase64Encoded1 = add1.substring(add1.indexOf(",")+1);
                        final byte[] decodedBytes1 = Base64.decode(pureBase64Encoded1);
                        final Bitmap decodedBitmap1 = BitmapFactory.decodeByteArray(decodedBytes1,0,decodedBytes1.length);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Photo.setImageBitmap(decodedBitmap);
                                Photo1.setImageBitmap(decodedBitmap1);

                               // Image.setImageBitmap(decodedBitmap);
                            }
                        });




                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                try {
                                    DOB.setText(formattedDate);
                                    State.setText(AddCollageState);
                                    City.setText(CollageCity);
                                    Collage.setText(CollageName);
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
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    public void MsgBox()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage("Successfully Updated");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
               // progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }

    public void SavingStudentProfileMsg()
    {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage("Successfully Updated");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                // progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();
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


                                                    adapter = new StateAdapter(getActivity(), R.layout.activity_student_profile, R.id.txt, list);
                                                    adapter.appContext = getActivity().getApplicationContext();
                                                    adapter.tmpActivity = StudentProfileFragment.this;
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
                                                        adapter1 = new CityAdapter(getActivity(), R.layout.activity_student_profile, R.id.txt, list);
                                                        adapter1.appContext = getActivity().getApplicationContext();
                                                        adapter1.tmpActivity = StudentProfileFragment.this;
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
                                                        adapter2 = new CollageAdapter(getActivity(), R.layout.activity_student_profile, R.id.txt, list);
                                                        adapter2.appContext = getActivity().getApplicationContext();
                                                        adapter2.tmpActivity = StudentProfileFragment.this;
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

    private void selectDate()
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
            DOB.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };

    private void callPop()
    {
        try {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup, null);

            popupWindow = new PopupWindow(popupView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Street1 = (EditText) popupView.findViewById(R.id.street1);
            Street2 = (EditText) popupView.findViewById(R.id.street2);
            Pincode = (EditText) popupView.findViewById(R.id.pincode);

            Statespinner = (Spinner) popupView.findViewById(R.id.state);
            Cityspinner = (Spinner) popupView.findViewById(R.id.city);
            Arrow = (ImageView) popupView.findViewById(R.id.img1);
            Arrow1 = (ImageView) popupView.findViewById(R.id.img);
            SelectPopupState();

            Street1.setText(General.Street1);
            Street2.setText(General.Street2);
            Pincode.setText(General.Pincode);


            ((Button) popupView.findViewById(R.id.saveBtn)).setOnClickListener(new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                public void onClick(View arg0) {
                     Edit = Street1.getText().toString();
                     Edit1 = Street2.getText().toString();
                     Edit2 = Pincode.getText().toString();
                     String StateSpinner = selectedStatename;
                     String CitySpinner = SelectedCityname;

                    Address.setText(Edit + "" + "," +"" + Edit1 + "" +"," + "" + CitySpinner + "-" +""+ Edit2 + ""+","+"" + StateSpinner);
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
                                                        StateData.tmpActivity = StudentProfileFragment.this;
                                                        Statespinner.setAdapter(StateData);

                                                        Statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    protected  void SelectedCitySpinner()
    {
        Thread thread = new Thread(new Runnable() {
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
                                                    CityData.tmpActivity = StudentProfileFragment.this;
                                                    Cityspinner.setAdapter(CityData);
                                                    Cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
