package cme.pointmila.com;

import android.app.AlertDialog;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Spinner;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;

/**
 * Created by amoln on 09-09-2016.
 */
public class Profile_DoctorActivity extends AppCompatActivity {
    EditText DOB;
    EditText RenewvalET;

    private Spinner spItems;
    private Spinner spItems2;
    private Spinner spItems3;
    private Spinner spItems4;
    private Spinner spItems5;
    ImageView Photo;
    ImageView CertificatePhoto;
    ImageView ReniewCertificate;
    Button SelectPhoto;
    Button SelectPhoto1;
    Button SelectPhoto2;
    String picturePath,ba1;
    private Bitmap thumbnail,thumbnail1,thumbnail2;
    Button Submit;

    ArrayAdapter arrayAdapter;
    ArrayAdapter arrayAdapter1;
    ArrayAdapter arrayAdapter2;
    ArrayAdapter arrayAdapter3;
    ArrayAdapter arrayAdapter4;
    ArrayAdapter arrayAdapter5;
    ImageView Arrow;
    ImageView Arrow2;
    ImageView Arrow3;
    ImageView Arrow4;
    ImageView Arrow5;


    List<String> listData = new ArrayList<String>();
    List<String> listData2 = new ArrayList<String>();
    List<String> listData3 = new ArrayList<String>();
    List<String> listData4 = new ArrayList<String>();
    List<String> listData5 = new ArrayList<String>();

    Calendar cDate;
    final int Date_Dialog_ID=0;
    int cDay,cMonth,cYear;
    int sDay,sMonth,sYear;

    Calendar cDate1;
    final int Date_Dialog_ID1=1;
    int cDay1,cMonth1,cYear1;
    int sDay1,sMonth1,sYear1;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_doctorprofile);
        DOB = (EditText)findViewById(R.id.dateET);
        Photo = (ImageView)findViewById(R.id.photo);
        SelectPhoto = (Button)findViewById(R.id.selectphoto);
        CertificatePhoto = (ImageView)findViewById(R.id.photo1);
        SelectPhoto1 = (Button) findViewById(R.id.selectphoto1);

        ReniewCertificate = (ImageView)findViewById(R.id.photo2);
        SelectPhoto2 = (Button) findViewById(R.id.selectphoto2);
        RenewvalET = (EditText)findViewById(R.id.renewalcertificate);
        Submit = (Button)findViewById(R.id.submitbtn);

        listData.add("Select Doctor Speciality");
        listData.add("Cardiologist");
        listData.add("Anesthesiologist");
        listData.add("Neurologist");


        listData2.add("Select District");
        listData2.add("Mumbai");
        listData2.add("Pune");
        listData2.add("Satara");

        listData3.add("Select State");
        listData3.add("Maharashtra");
        listData3.add("Punjab");
        listData3.add("Gujarat");

        listData4.add("Select City");
        listData4.add("Mumbai");
        listData4.add("Pune");
        listData4.add("Akola");

        listData5.add("Select Academic Program");
        listData5.add("MBBS");
        listData5.add("MS");
        listData5.add("MD");


        spItems = (Spinner)findViewById(R.id.spitem);
        Arrow = (ImageView)findViewById(R.id.img);

        spItems2 = (Spinner)findViewById(R.id.spitem2);
        Arrow2 = (ImageView)findViewById(R.id.img2);

        spItems3 = (Spinner)findViewById(R.id.spitem3);
        Arrow3 = (ImageView)findViewById(R.id.img3);

        spItems4 = (Spinner)findViewById(R.id.spitem4);
        Arrow4 = (ImageView)findViewById(R.id.img4);

        spItems5 = (Spinner)findViewById(R.id.spitem5);
        Arrow5 = (ImageView)findViewById(R.id.img5);


        arrayAdapter = new ArrayAdapter(Profile_DoctorActivity.this,R.layout.row_spinner,listData);
        arrayAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spItems.setAdapter(arrayAdapter);

        arrayAdapter2 = new ArrayAdapter(Profile_DoctorActivity.this,R.layout.row_spinner,listData2);
        arrayAdapter2.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spItems2.setAdapter(arrayAdapter2);

        arrayAdapter3 = new ArrayAdapter(Profile_DoctorActivity.this,R.layout.row_spinner,listData3);
        arrayAdapter3.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spItems3.setAdapter(arrayAdapter3);

        arrayAdapter4 = new ArrayAdapter(Profile_DoctorActivity.this,R.layout.row_spinner,listData4);
        arrayAdapter4.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spItems4.setAdapter(arrayAdapter4);

        arrayAdapter5 = new ArrayAdapter(Profile_DoctorActivity.this,R.layout.row_spinner,listData5);
        arrayAdapter5.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spItems5.setAdapter(arrayAdapter5);


        DOB.setFocusable(false);
        DOB.setClickable(true);

        RenewvalET.setFocusable(false);
        RenewvalET.setClickable(true);
       // RenewvalET.setHint("select Date");


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Date_Dialog_ID);
            }
        });

        RenewvalET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Date_Dialog_ID1);
            }
        });

        cDate = Calendar.getInstance();
        cDay = cDate.get(Calendar.DAY_OF_MONTH);
        cMonth = cDate.get(Calendar.MONTH);
        cYear = cDate.get(Calendar.YEAR);

        sDay=cDay;
        sMonth=cMonth;
        sYear=cYear;
        //updateDateDisplay(sYear, sMonth, sDay);

        cDate1 = Calendar.getInstance();
        cDay1 = cDate.get(Calendar.DAY_OF_MONTH);
        cMonth1 = cDate.get(Calendar.MONTH);
        cYear1 = cDate.get(Calendar.YEAR);

        sDay1=cDay1;
        sMonth1=cMonth1;
        sYear1=cYear1;
       // updateDateDisplay1(sYear1, sMonth1, sDay1);


        SelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        SelectPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage1();
            }
        });

        SelectPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage2();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Profile_StudentActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case Date_Dialog_ID:
                return new DatePickerDialog(this,onDateset,cYear,cMonth,cDay);
            case Date_Dialog_ID1:
                return new DatePickerDialog(this,onDateset1,cYear1,cMonth1,cDay1);
        }
        return null;
    }

    private void updateDateDisplay(int year,int month,int date)
    {
        //DOB.setText(date + "-" + (month+1)+"-"+year);
    }

    private void updateDateDisplay1(int year,int month,int date)
    {
        //RenewvalET.setHint("Select Date");

       // RenewvalET.setText(date + "-" + (month+1)+"-"+year);
    }

    private DatePickerDialog.OnDateSetListener onDateset = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            sYear=year;
            sMonth=monthOfYear;
            sDay=dayOfMonth;

            DOB.setText(sDay + "-" + (sMonth+1)+"-"+sYear);

           // updateDateDisplay(sYear,sMonth,sDay);

            //UpdateDateDisplay(sYear,sMonth,sDay);
        }
    };

    private DatePickerDialog.OnDateSetListener onDateset1 = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            sYear1=year;
            sMonth1=monthOfYear;
            sDay1=dayOfMonth;
            RenewvalET.setText(sDay1+ "-" + (sMonth1+1)+"-"+sYear1);
            //updateDateDisplay1(sYear1,sMonth1,sDay1);
            //UpdateDateDisplay(sYear,sMonth,sDay);
        }
    };


    private void SelectImage()
    {
        try {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Profile_DoctorActivity.this);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SelectImage1()
    {
        try {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Profile_DoctorActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (options[which].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 3);
                    } else if (options[which].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 4);
                    } else if (options[which].equals("Cancel")) {
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

    private void SelectImage2()
    {
        try {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Profile_DoctorActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (options[which].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 5);
                    } else if (options[which].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 6);
                    } else if (options[which].equals("Cancel")) {
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

    protected void onActivityResult(int requestcode,int resultCode,Intent data)
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


        if (resultCode == RESULT_OK) {
            if (requestcode == 3) {
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
                    thumbnail1 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    CertificatePhoto.setImageBitmap(thumbnail1);


                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera";
                    //String path= "DCIM"+File.separator+"Camera";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");//
                    try {
                        outFile = new FileOutputStream(file);
                        thumbnail1.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
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
            } else if (requestcode == 4) {

                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    thumbnail1 = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    CertificatePhoto.setImageBitmap(thumbnail1);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                //upload=ImgView.setImageBitmap(thumbnail);

            }
        }


        if (resultCode == RESULT_OK) {
            if (requestcode == 5) {
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
                    thumbnail2 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    ReniewCertificate.setImageBitmap(thumbnail2);


                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera";
                    //String path= "DCIM"+File.separator+"Camera";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");//
                    try {
                        outFile = new FileOutputStream(file);
                        thumbnail2.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
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
            } else if (requestcode == 6) {

                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    thumbnail2 = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    ReniewCertificate.setImageBitmap(thumbnail2);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                //upload=ImgView.setImageBitmap(thumbnail);

            }
        }




    }//


}
