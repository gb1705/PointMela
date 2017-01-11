package cme.pointmila.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
 * Created by amoln on 12-09-2016.
 */
public class ProfileTabFragment extends Fragment {
    ViewPager viewPager;
    EditText DOB;
    private Spinner spItems;
    // private Spinner spItems1;
    // private Spinner spItems2;
    ArrayAdapter arrayAdapter;
    //ArrayAdapter arrayAdapter1;
    // ArrayAdapter arrayAdapter2;
    List<String> listData = new ArrayList<String>();
    List<HashMap<String, String>> lstDoctorSpeciality;
    HashMap<String, String> hmDoctorspeciality = null;

    //List<String> listData1 = new ArrayList<String>();
    //List<String> listData2 = new ArrayList<String>();

    Calendar cDate;
    final int Date_Dialog_ID = 0;
    int cDay, cMonth, cYear;
    int sDay, sMonth, sYear;
    ImageView Arrow;
    ImageView Arrow1;
    ImageView Arrow2;
    ImageView Photo, Photo1, Photo2;
    Button SelectPhoto, SelectPhoto1, SelectPhoto2;
    String picturePath;
    private Bitmap thumbnail, thumbnail1, thumbnail2;
    Spinner DoctorSpeciality;

    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private static final int CAMERA_CODE1 = 111, GALLERY_CODE1 = 211, CROPING_CODE1 = 311;
    private static final int CAMERA_CODE2 = 221, GALLERY_CODE2 = 231, CROPING_CODE2 = 341;

    private File outPutFile = null;
    DoctorSpecialityAdapter adapter;
    Button Save;

    String DoctorSpecilaityName, DoctorSpecilaityID;
    boolean isImageFitToScreen;
    String DoctorspecialityId;
    Bitmap BitPhoto1, BitPhoto2, BitPhoto3;
    String ba1, ba2, ba3;

    JSONObject Objobject;
    int i;
    public SharedPreferences settings1;
    boolean checkprofile = false;
    ProgressDialog progressDialog;
    Bitmap decodedBitmap;
    Bitmap decodedBitmap1;
    Bitmap decodedBitmap2;

    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profiletab, null);
        DOB = (EditText) view.findViewById(R.id.dateET);

        Photo = (ImageView) view.findViewById(R.id.photo);
        //SelectPhoto = (Button)view.findViewById(R.id.selectphoto);
        Save = (Button) view.findViewById(R.id.registerbtn);
        DoctorSpeciality = (Spinner) view.findViewById(R.id.Doctorspeciality);


        Photo1 = (ImageView) view.findViewById(R.id.photo1);

        Photo2 = (ImageView) view.findViewById(R.id.photo2);
        // SelectPhoto2 = (Button)view.findViewById(R.id.selectphoto2);


        Picasso.with(getActivity())
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo);

        Picasso.with(getActivity())
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo1);
        Picasso.with(getActivity())
                .load(Url)
                .placeholder(R.drawable.cameraimg)
                .into(Photo2);

        SelectDoctorSpeciality();


        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        DOB.setFocusable(false);
        DOB.setClickable(true);

        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });


        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageOption();
            }
        });

        Photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageOption1();
            }
        });

        Photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageOption2();
            }
        });
        ProfileView();
        GetProfileInfo();

        return view;
    }

    private void ProfileView() {

        DOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isName(DOB, true);

            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity().getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();

                if (checkValidation()) {

                    SaveProfilePhoto();
                    SaveSignaturePhoto();
                    SavePassportPhoto();

                    progressDialog = ProgressDialog.show(getActivity(), "", "Updating..Please Wait");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                      /*  Thread timerthread = new Thread() {
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


                        UpdateProfile();
                    } else {
                        InternetMessage();
                    }


                }
            }
        });


        // etMobileNumber = (EditText) findViewById(R.id.mobileET);


    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.isName(DOB, true)) ret = false;
        // if(!Validation.isPincode(Pincode,true))ret=false;
        return ret;
    }


    public void InternetMessage() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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


    public void GetProfileInfo()

    {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl + "DoctorProfile/GetProfileInformation/" + General.GUID);
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
                        final String add = Objobject.getString("ProfilePhoto").toString();
                        if (!add.equals("")) {
                            final String pureBase64Encoded = add.substring(add.indexOf(",") + 1);
                            final byte[] decodedBytes = Base64.decode(pureBase64Encoded);
                            decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        }


                        final String add1 = Objobject.getString("SignaturePhoto").toString();
                        if (!add1.equals("")) {
                            final String pureBase64Encoded1 = add1.substring(add1.indexOf(",") + 1);
                            final byte[] decodedBytes1 = Base64.decode(pureBase64Encoded1);
                            decodedBitmap1 = BitmapFactory.decodeByteArray(decodedBytes1, 0, decodedBytes1.length);
                        }

                        final String add2 = Objobject.getString("PassportPhoto").toString();
                        if (!add2.equals("")) {
                            final String pureBase64Encoded2 = add2.substring(add2.indexOf(",") + 1);
                            final byte[] decodedBytes2 = Base64.decode(pureBase64Encoded2);
                            decodedBitmap2 = BitmapFactory.decodeByteArray(decodedBytes2, 0, decodedBytes2.length);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!add.equals("")) {
                                    Photo.setImageBitmap(decodedBitmap);
                                }
                                if (!add1.equals("")) {
                                    Photo1.setImageBitmap(decodedBitmap1);
                                }
                                if (!add2.equals("")) {
                                    Photo2.setImageBitmap(decodedBitmap2);
                                }
                            }
                        });


                        String DateOfPassing = Objobject.getString("DOB").toString();
                        String timestamp = DateOfPassing.replace("/Date(", "").replace(")/", "");
                        Date passingdate = new Date(Long.parseLong(timestamp));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd -MMM- yyyy");
                        final String formattedDate = sdf.format(passingdate);

                        final String DoctorSpecialityDropdown = Objobject.getString("DoctorSpecialityId").toString();


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DOB.setText(formattedDate);
                                    //DoctorSpeciality.setSelection(Integer.parseInt(DoctorSpecialityDropdown));

                                    //  Photo.setImageBitmap(decodedBitmap);
                                    // Photo1.setImageBitmap(decodedBitmap1);
                                    //  Photo2.setImageBitmap(decodedBitmap2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Image.setImageBitmap(decodedBitmap);
                            }
                        });


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

    public void SaveProfilePhoto() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            BitPhoto1.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba1 = Base64.encodeBytes(ba);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SaveSignaturePhoto() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            BitPhoto2.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba2 = Base64.encodeBytes(ba);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SavePassportPhoto() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            BitPhoto3.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba3 = Base64.encodeBytes(ba);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void UpdateProfile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "DoctorProfile/UpdateProfileInformation");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId", General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("DoctorDOB", DOB.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("DoctorSpecialityId", DoctorspecialityId));
                    nameValuePairs.add(new BasicNameValuePair("ProfilePicString", ba1));
                    nameValuePairs.add(new BasicNameValuePair("PassportPicString", ba3));
                    nameValuePairs.add(new BasicNameValuePair("SignaturePicString", ba2));


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

                        // SharedPreferences.Editor editor = settings1.edit();
                        // editor.putBoolean("checkprofile",true);
                        // editor.commit();
                        // final     String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true")) {
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        SaveProfileMsg();


                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // General.Token = Objobject.getString("Token").toString();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


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

    public void SaveProfileMsg() {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setMessage(" ProfileInfo Updated Successfully");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    progressDialog.cancel();
                }
            });
            AlertDialog alert = dlgAlert.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void SelectDoctorSpeciality() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpPost = new HttpGet(General.baseurl + "PointMilaMaster/GetDoctorSpeciality/1");


                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        StringBuilder builder = new StringBuilder();

                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");

                        }
                        final JSONObject objObject = (JSONObject) new JSONTokener(builder.toString()).nextValue();
                        final String strResult = objObject.getString("doctorSpeciality").toString();
                        strResult.toString();
                        if (strResult.equals("null")) {
                            //  Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            // startActivity(intent);
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    {
                                        try {

                                            JSONArray jarray = new JSONArray(objObject.getString("doctorSpeciality").toString());

                                            // final List<DoctorSpeciality> list = new ArrayList<DoctorSpeciality>();
                                            lstDoctorSpeciality = new ArrayList<HashMap<String, String>>();

                                            for (int i = 0; i < jarray.length(); i++) {

                                                hmDoctorspeciality = new HashMap<String, String>();

                                                try {

                                                    DoctorSpecilaityName = jarray.getJSONObject(i).getString("DoctorSpecialityDescription");
                                                    DoctorSpecilaityID = jarray.getJSONObject(i).getString("DoctorSepcialityId");

                                                    hmDoctorspeciality.put("Doctorspecialityname", DoctorSpecilaityName);
                                                    hmDoctorspeciality.put("DoctorspecilaityID", DoctorSpecilaityID);


                                                    lstDoctorSpeciality.add(hmDoctorspeciality);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            }

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    adapter = new DoctorSpecialityAdapter(getActivity(), lstDoctorSpeciality);
                                                    adapter.appContext = getActivity().getApplicationContext();
                                                    adapter.tmpActivity = ProfileTabFragment.this;
                                                    // spItems.setAdapter(adapter);

                                                    DoctorSpeciality.setAdapter(adapter);
                                                    //  DoctorSpeciality.setThreshold(1);
                                                }
                                            });

                                            try {

                                                DoctorSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                        HashMap<String, String> tmpData = lstDoctorSpeciality.get(position);

                                                        DoctorspecialityId = tmpData.get("DoctorspecilaityID");

                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

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

    private void SelectImageOption() {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Capture Photo")) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
//                        mImageCaptureUri = Uri.fromFile(f);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                        startActivityForResult(intent, CAMERA_CODE);

                        getCamera(CAMERA_CODE);
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

    private void SelectImageOption1() {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Capture Photo")) {
                        getCamera(CAMERA_CODE1);
                    } else if (items[item].equals("Choose from Gallery")) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_CODE1);
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

    private void SelectImageOption2() {
        try {
            final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Capture Photo")) {
                        getCamera(CAMERA_CODE2);
                    } else if (items[item].equals("Choose from Gallery")) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_CODE2);
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
                    Photo.setImageBitmap(BitPhoto1);
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


        if (requestCode == GALLERY_CODE2 && resultCode == getActivity().RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI :" + mImageCaptureUri);
            CropingIMG2();
        } else if (requestCode == CAMERA_CODE2 && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera Image URI:" + mImageCaptureUri);
            CropingIMG2();
        } else if (requestCode == CROPING_CODE2) {
            try {
                if (outPutFile.exists()) {
                    BitPhoto3 = decodeFile(outPutFile);
                    Photo2.setImageBitmap(BitPhoto3);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
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

            List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
            int size = list.size();
            if (size == 0) {
                Toast.makeText(getActivity(), "can't find image cropping  app", Toast.LENGTH_SHORT).show();
                return;
            } else {
                intent.setData(mImageCaptureUri);
                //intent.putExtra("outputX", 512);
                //intent.putExtra("outputY", 812);
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 2);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CropingIMG1() {
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
//                intent.putExtra("outputX", 512);
//                intent.putExtra("outputY", 812);
                intent.putExtra("aspectX", 5);
                intent.putExtra("aspectY", 3);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CropingIMG2() {
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
                intent.putExtra("aspectX", 4);
                intent.putExtra("aspectY", 5);
                intent.putExtra("scale", true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = (ResolveInfo) list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    startActivityForResult(i, CROPING_CODE2);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE = 212;
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

    private void selectDate() {
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
          /*  DOB.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/

            // String mdate = ConvertDate(convertToMillis)
          /*  try {
                String mDate = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                String GetDate = format1.format(mDate);
                DOB.setText(GetDate);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }*/

            try {

                String DateOfPassing = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(DateOfPassing);
                // SimpleDateFormat timeformat = new SimpleDateFormat("dd-MMM-yyyy");
                //  String finalDate = timeformat.format(date);
                DOB.setText(General.ConvertDateFormat(date));
                //  SimpleDateFormat sdf = new SimpleDateFormat("dd -MM- yyyy");
                //Date date = sdf.parse(DateOfPassing);
                //String date1 = date;
                // Date passingdate = new Date(DateOfPassing);
                //SimpleDateFormat sdf = new SimpleDateFormat("dd -MM- yyyy");
                //String formattedDate = sdf.format(passingdate);
                //DOB.setText(formattedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    public void getCamera(final int requestCode) {


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                showMessageOKCancel("You need to allow camera usage",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        1234);

                            }
                        });
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                mImageCaptureUri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(intent, requestCode);

            }

        } else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                mImageCaptureUri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(intent, requestCode);

            } catch (Exception e) {

            }

        }


//        boolean trmp=Utility.hasPermissionInManifest(getActivity(), Manifest.permission.CAMERA);
//        if (trmp) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
//            mImageCaptureUri = Uri.fromFile(f);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//            startActivityForResult(intent, requestCode);
//        } else {
//            showMessageOKCancel("You need to allow camera usage",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            requestPermissions(new String[]{Manifest.permission.CAMERA},
//                                    requestCode);
//
//                        }
//                    });
//        }

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    criarFoto();
                } else {
                    Toast.makeText(getActivity(), "You did not allow camera usage :(", Toast.LENGTH_SHORT).show();
//                    noFotoTaken();
                }
                return;
            }
        }
    }
}
