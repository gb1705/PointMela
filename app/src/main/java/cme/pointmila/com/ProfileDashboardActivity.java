package cme.pointmila.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cme.pointmila.com.election.ElectionFragment;
import cme.pointmila.com.election.MMCPanelLitsFragment;
import cme.pointmila.com.election.Util.Connectivity;
import cme.pointmila.com.election.Util.RestClient;
import cme.pointmila.com.election.models.Election;
import cme.pointmila.com.election.models.ElectionCenter;
import cme.pointmila.com.election.models.ElectionCenterArrayModel;
import cme.pointmila.com.election.models.MMCList;
import cme.pointmila.com.election.models.MmcPanel;
import cme.pointmila.com.election.models.MyCandidate;
import cme.pointmila.com.maps.MapsActivity;
import cme.pointmila.com.navigationDrawer.ExpandableListAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by amoln on 12-09-2016.
 */
public class ProfileDashboardActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer, nvDrawer1;
    EditText Txt;
    ImageView Profile;
    private Uri mImageCaptureUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private File outPutFile = null;
    Bitmap photo1;
    String ba3;
    TextView ProfileUsername;
    TextView usertag;

    String pdfIndex = "1";

    Class fragmentClassele = null;
    public static List<MyCandidate> myCandidates;
    public static List<MmcPanel> mmcPanels;

    String Url = "https://www.google.co.in/search?q=how+to+add+the+image+placeholder+in+android&biw=1440&bih=799&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjroPyHtbbPAhXDoJQKHT0KCBQQ_AUIBygC#tbm=isch&q=placeholder+image+android&imgrc=IpIIbFrNqKDZkM%3A";
    private ProgressDialog progressDialog;

    ///expandble list view


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);


        try {
            setContentView(R.layout.profile_dashboard);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);

            actionBar.setDisplayHomeAsUpEnabled(true);

            mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            nvDrawer = (NavigationView) findViewById(R.id.nvView);

            //expanlistview
            expListView = (ExpandableListView) findViewById(R.id.navList);

            prepareListData();

            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);

            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                    selectDrawerItem(-1, groupPosition);
                    return false
                            ;
                }
            });
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    selectDrawerItem(childPosition, groupPosition);

//                    if (Connectivity.isConnected(this)) {
//                    pdfIndex = "3";
//                    fragmentClass = ShowPdf.class;
//                    getFragmentSteup(fragment, fragmentClass);
//                } else {
//                    Utility.showTempAlert(this, "Please Check Internet Connection");
//                }
                    return true;
                }
            });


            //  nvDrawer1 = (NavigationView)findViewById(R.id.nvView1);

            outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");


            if (General.Student.equals("false")) {
                //nvDrawer1.setVisibility(View.INVISIBLE);
                try {


                    nvDrawer.getMenu().clear();
                    nvDrawer.inflateMenu(R.menu.drawer_view);


                    // nvDrawer.setVisibility(View.VISIBLE);
                    setupDrawerContent(nvDrawer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    nvDrawer.getMenu().clear();
//                    nvDrawer.inflateMenu(R.menu.drawer_view1);
                    //  nvDrawer.setVisibility(View.INVISIBLE);
                    // nvDrawer1.setVisibility(View.VISIBLE);
                    setupDrawerContent1(nvDrawer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // actionBar.setLogo(R.drawable.pointlogo);


            try {

                Fragment enquiresFragment = new DashBoardFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerView, enquiresFragment, null);
                // fragmentTransaction.add(R.id.containerView,enquiresFragment).addToBackStack("fragBack").commit();
                fragmentTransaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                try {

                    Profile = (ImageView) findViewById(R.id.profileimg);
                    ProfileUsername = (TextView) findViewById(R.id.text);
                    usertag = (TextView) findViewById(R.id.text1);
                    ProfileUsername.setText(General.profileUsername);
                    General.UserTag = General.UserTag.replace("null", "");
                    usertag.setText(General.UserTag);
                    usertag.setTextColor(Color.YELLOW);

                    Picasso.with(this)
                            .load(Url)
                            .placeholder(R.drawable.cameraimg)
                            .into(Profile);


                    final String add2 = General.ProfilePicture;
                    final String pureBase64Encoded2 = add2.substring(add2.indexOf(",") + 1);
                    final byte[] decodedBytes2 = Base64.decode(pureBase64Encoded2);
                    final Bitmap decodedBitmap2 = BitmapFactory.decodeByteArray(decodedBytes2, 0, decodedBytes2.length);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Profile.setImageBitmap(decodedBitmap2);
                        }
                    });


                    Profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
                            SelectImageOption();
                            // SaveImage();
                            //  UpdateProfileImage();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


                // UpdateProfileImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        try {
//                            selectDrawerItem(menuItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }


    private void setupDrawerContent1(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem1) {


                        try {
                            selectDrawerItem1(menuItem1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }

    private void SelectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDashboardActivity.this);
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
    protected void onResume() {
        super.onResume();
        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "pointmila44" + ".jpg";

            File fdelete = new File(mPath);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :");
                } else {
                    System.out.println("file not Deleted :");
                }
            }
        } catch (Exception e) {

        }

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
                    Profile.setImageBitmap(photo1);
                    // UpdateProfileImage();
                    SaveImage();

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

    public void SaveImage() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            photo1.compress(Bitmap.CompressFormat.JPEG, 50, bao);
            byte[] ba = bao.toByteArray();
            //Image = new String(ba);
            ba3 = Base64.encodeBytes(ba);
            UpdateProfileImage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void UpdateProfileImage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "Registration/UpdateProfilePic");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId", General.GUID));
                    nameValuePairs.add(new BasicNameValuePair("ProfilePicString", ba3));


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

                        // final     String strResult = Objobject.getString("result").toString();

                        if (strSuccess.equals("true")) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //SaveProfileMsg();


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


    public void selectDrawerItem(int Child, int Parent) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch (Parent) {
            case 0:
                fragmentClass = DashBoardFragment.class;
                getFragmentSteup(fragment, fragmentClass);
                mDrawer.closeDrawers();
                break;
            case 1:
                fragmentClass = DoctorProfileFragment.class;
                getFragmentSteup(fragment, fragmentClass);
                mDrawer.closeDrawers();
                break;
            case 2:
                if (Child == 0) {
                    pdfIndex = "1";
                    fragmentClass = ShowPdf.class;
                    getFragmentSteup(fragment, fragmentClass);
                    mDrawer.closeDrawers();
                } else if (Child == 1) {
                    pdfIndex = "2";
                    fragmentClass = ShowPdf.class;
                    getFragmentSteup(fragment, fragmentClass);
                    mDrawer.closeDrawers();
                } else if (Child == 2) {
                    pdfIndex = "3";
                    fragmentClass = ShowPdf.class;
                    getFragmentSteup(fragment, fragmentClass);
                    mDrawer.closeDrawers();

                }

                break;

            case 4:
                fragmentClass = LogOut.class;
                getFragmentSteup(fragment, fragmentClass);
                mDrawer.closeDrawers();
                break;
            case 3:
                if (Child == 0) {
                    if (Connectivity.isConnected(this)) {
                        getProgressBar();
                        getElectionDataFromServer();
                    } else {
                        Utility.showTempAlert(this, "Please Check Internet Connection");
                    }
                    mDrawer.closeDrawers();
                }
                if (Child == 1) {


                    getdialog();
                    if (Connectivity.isConnected(this)) {
                        getProgressBar();
                        getElectionCentersFromServer();

//                        getElectionDataFromServer();
                    } else {
                        Utility.showTempAlert(this, "Please Check Internet Connection");
                    }
                    mDrawer.closeDrawers();
                }
                if (Child == 2) {
                    if (Connectivity.isConnected(this)) {
                        getProgressBar();
                        getElectionMMCPanelListFromServer();

//                        getElectionDataFromServer();
                    } else {
                        Utility.showTempAlert(this, "Please Check Internet Connection");
                    }
                    mDrawer.closeDrawers();
                }
                break;

            //  fragmentClass = StudentProfileFragment.class;
            // break;

            default:
                fragmentClass = DashBoardFragment.class;
                getFragmentSteup(fragment, fragmentClass);
                mDrawer.closeDrawers();
                break;
        }


        // Highlight the selected item, update the title, and close the drawer
//        setTitle(Parent);


    }

//    public void selectDrawerItem(MenuItem menuItem) {
//        // Create a new fragment and specify the planet to show based on
//        // position
//        Fragment fragment = null;
//
//        Class fragmentClass;
//        switch (menuItem.getItemId()) {
//            case R.id.nav_first_fragment:
//                fragmentClass = DashBoardFragment.class;
//                getFragmentSteup(fragment, fragmentClass);
//                break;
//            case R.id.nav_second_fragment:
//                fragmentClass = DoctorProfileFragment.class;
//                getFragmentSteup(fragment, fragmentClass);
//                break;
//            case R.id.nav_thired_fragment:
//                fragmentClass = LogOut.class;
//                getFragmentSteup(fragment, fragmentClass);
//                break;
//
//            case R.id.nav_pdf_view1:
//                if (Connectivity.isConnected(this)) {
//                    pdfIndex = "1";
//                    fragmentClass = ShowPdf.class;
//                    getFragmentSteup(fragment, fragmentClass);
//                } else {
//                    Utility.showTempAlert(this, "Please Check Internet Connection");
//                }
//
//                break;
//
//            case R.id.nav_pdf_view2:
//                if (Connectivity.isConnected(this)) {
//                    pdfIndex = "2";
//                    fragmentClass = ShowPdf.class;
//                    getFragmentSteup(fragment, fragmentClass);
//                } else {
//                    Utility.showTempAlert(this, "Please Check Internet Connection");
//                }
//
//                break;
//            case R.id.nav_pdf_view3:
//                if (Connectivity.isConnected(this)) {
//                    pdfIndex = "3";
//                    fragmentClass = ShowPdf.class;
//                    getFragmentSteup(fragment, fragmentClass);
//                } else {
//                    Utility.showTempAlert(this, "Please Check Internet Connection");
//                }
//
//                break;
//
//            case R.id.nav_election:
//                if (Connectivity.isConnected(this)) {
//                    getProgressBar();
//                    getElectionDataFromServer();
//                } else {
//                    Utility.showTempAlert(this, "Please Check Internet Connection");
//                }
//                break;
//
//            //  fragmentClass = StudentProfileFragment.class;
//            // break;
//
//            default:
//                fragmentClass = DashBoardFragment.class;
//                getFragmentSteup(fragment, fragmentClass);
//                break;
//        }
//
//
//        // Highlight the selected item, update the title, and close the drawer
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());
//        mDrawer.closeDrawers();
//
//    }

    public void callMapFragment(ArrayList<ElectionCenter> electionCenters) {
        Fragment fragment = null;
        try {
            fragment = new MapsActivity();
            fragment.setRetainInstance(true);

            Bundle args = new Bundle();
            args.putParcelableArrayList("electionCenters", electionCenters);
            fragment.setArguments(args);
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getFragmentSteup(Fragment fragment, Class fragmentClass) {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setRetainInstance(true);

            Bundle args = new Bundle();
            args.putString("CID", pdfIndex);
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

    }

    public void selectDrawerItem1(MenuItem menuItem1) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem1.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = DashBoardFragment.class;
                break;

            case R.id.nav_second_fragment:
                fragmentClass = StudentProfileFragment.class;
                break;
            case R.id.nav_thired_fragment:
                fragmentClass = LogOut.class;
                break;


            default:
                fragmentClass = DashBoardFragment.class;
        }

        try {
            mDrawer.closeDrawers();
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Insert the fragment by replacing any existing fragment
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            menuItem1.setChecked(true);
            setTitle(menuItem1.getTitle());
            mDrawer.closeDrawers();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getElectionDataFromServer() {

        RestClient.GitApiInterface service = RestClient.getClient();
        final Call<Election> call = service.callElection();
        call.enqueue(new Callback<Election>() {
            @Override
            public void onResponse(Response<Election> response, Retrofit retrofit) {
                if (response.code() == 200 || !response.message().equals("Not Found")) {
                    dismissProgress();
                    Election election = response.body();

                    if (election.getSuccess()) {
                        String cutOfDate = election.getCutOfDate();
                        String timestamp = cutOfDate.replace("/Date(", "").replace(")/", "");
                        Date createdon = new Date(Long.parseLong(timestamp));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd -MMM- yyyy");
                        String formattedDate = sdf.format(createdon);
                        if (Utility.checkDateNew(createdon, new Date()) == -1) {
                            Utility.showTempAlert(ProfileDashboardActivity.this, "Cutof Date is passed ");
                            fragmentClassele = null;
                            mDrawer.closeDrawers();

                        } else {
                            if (election.getMyCandidates() != null && election.getMyCandidates().size() > 0) {
                                fragmentClassele = ElectionFragment.class;
                                myCandidates = election.getMyCandidates();
                                getFragmentSteup(new Fragment(), fragmentClassele);
                            }
                        }
                    } else {

                        Utility.showTempAlert(ProfileDashboardActivity.this, "No data");

                    }

                } else {

                    Utility.showTempAlert(ProfileDashboardActivity.this, "Please check internet connection");
                }

            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgress();
                Log.d("ELEC", "" + t.toString());
            }
        });
    }

    public void getElectionCentersFromServer() {

        RestClient.GitApiInterface service = RestClient.getClient();
        final Call<ElectionCenterArrayModel> call = service.getElectionCenter();
        call.enqueue(new Callback<ElectionCenterArrayModel>() {
            @Override
            public void onResponse(Response<ElectionCenterArrayModel> response, Retrofit retrofit) {
                if (response.code() == 200 || !response.message().equals("Not Found")) {
                    dismissProgress();
                    ElectionCenterArrayModel electionCentersArrayModel = response.body();

                    if (electionCentersArrayModel.getSuccess()) {
                        ArrayList<ElectionCenter> electionCenters = electionCentersArrayModel.getElectionCenter();
                        callMapFragment(electionCenters);
                    } else {
                        Utility.showTempAlert(ProfileDashboardActivity.this, "No data");
                    }

                } else {
                    Utility.showTempAlert(ProfileDashboardActivity.this, "No data");
                    dismissProgress();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgress();
                Log.d("ELEC", "" + t.toString());
            }
        });
    }

    public void getElectionMMCPanelListFromServer() {

        RestClient.GitApiInterface service = RestClient.getClient();
        final Call<MMCList> call = service.getElectionMMCPannel();
        call.enqueue(new Callback<MMCList>() {
            @Override
            public void onResponse(Response<MMCList> response, Retrofit retrofit) {
                if (response.code() == 200 || !response.message().equals("Not Found")) {
                    dismissProgress();
                    MMCList mmcList = response.body();

                    if (mmcList.getSuccess()) {

                        fragmentClassele = MMCPanelLitsFragment.class;
                        mmcPanels = mmcList.getMmcPanels();
                        getFragmentSteup(new Fragment(), fragmentClassele);

//                        callMapFragment(electionCenters);
                    } else {
                        Utility.showTempAlert(ProfileDashboardActivity.this, "No data");
                    }

                } else {
                    dismissProgress();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                dismissProgress();
                Log.d("ELEC", "" + t.toString());
            }
        });
    }


    public void getProgressBar() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }

    public void dismissProgress() {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }


    /*
    * Preparing the list data
    */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Dashboard");
        listDataHeader.add("Doctor Profile");
        listDataHeader.add("MMC Regulations");
        listDataHeader.add("Election");
        listDataHeader.add("Log out");

        // Adding child data
        List<String> top250 = new ArrayList<String>();

        List<String> nowShowing = new ArrayList<String>();

        List<String> MMCReg = new ArrayList<String>();
        MMCReg.add("MMC Rules");
        MMCReg.add("MMC Ethics");
        MMCReg.add("MMC Act");
        List<String> election = new ArrayList<String>();
        election.add("MMC Candidates");
        election.add("Polling Center");
        election.add("MMC Panel");
        List<String> logout = new ArrayList<String>();


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), MMCReg);
        listDataChild.put(listDataHeader.get(3), election);
        listDataChild.put(listDataHeader.get(4), logout);

    }


    public void getdialog() {

        final Dialog openDialog = new Dialog(ProfileDashboardActivity.this);
        openDialog.setContentView(R.layout.dialogformap);
        openDialog.setTitle("Note");
        openDialog.show();

    }
}
