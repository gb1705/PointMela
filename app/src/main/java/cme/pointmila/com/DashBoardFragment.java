package cme.pointmila.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by amoln on 12-09-2016.
 */
public class DashBoardFragment extends Fragment {
    EditText txt;
    // ImageView Plus;
    String StartDate, EndDate;
    TextView FirstYear;
    //TextView AddpointTxt;

    ProgressDialog progressDialog;
    Button CompressFile, CompressFile1;
    JSONObject Objobject;
    TextView FirstYearCount, SecondYearCount, ThirdYearCount, FourthYearCount, FifthYearCount;
    FloatingActionButton shareTextView;
    String FinalInputString;
    Button Addpoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_dashboard, null);
        init(view);
        setListeners();

        return view;


    }

    public void init(View view) {
        txt = (EditText) view.findViewById(R.id.txt);
        FirstYear = (TextView) view.findViewById(R.id.first);
        // AddpointTxt = (TextView)view.findViewById(R.id.addpoint);
        CompressFile = (Button) view.findViewById(R.id.compressbtn);
        CompressFile1 = (Button) view.findViewById(R.id.compressbtn1);
        FirstYearCount = (TextView) view.findViewById(R.id.countFirst);
        SecondYearCount = (TextView) view.findViewById(R.id.countsecond);
        ThirdYearCount = (TextView) view.findViewById(R.id.countthird);
        FourthYearCount = (TextView) view.findViewById(R.id.fourthcount);
        FifthYearCount = (TextView) view.findViewById(R.id.fifthcount);
        Addpoint = (Button) view.findViewById(R.id.Addpoint);
        shareTextView = (FloatingActionButton) view.findViewById(R.id.share);

    }

    private void setListeners() {


        FirstYearCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity().getApplicationContext(), FirstYearCountActivity.class);
                startActivity(i);

            }
        });

        SecondYearCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), SecondYearCountActivity.class);
                startActivity(i);
            }
        });

        ThirdYearCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ThirdYearCountActvity.class);
                startActivity(i);
            }
        });
        FourthYearCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), FourthYearCountActivity.class);
                startActivity(i);
            }
        });

        FifthYearCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity().getApplicationContext(), FifthYearCountActivity.class);
                startActivity(i);
            }
        });


        CompressFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CompressEmailFile();

                CompressFile.setEnabled(true);

                progressDialog = ProgressDialog.show(getActivity(), "", "Please wait");
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


                Intent i = new Intent(getActivity().getApplicationContext(), CompressEmailList.class);
                startActivity(i);
                //progressDialog.dismiss();

            }
        });

        txt.setClickable(false);
        txt.setFocusable(false);

        // Plus = (ImageView)view.findViewById(R.id.plus);
        Addpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity().getApplicationContext(), AddPoints.class);
                startActivity(in);

            }
        });


        GetListOfPoints();


        // Intent i = new Intent(getActivity().getApplicationContext(), ListOfPoints.class);
        //  startActivity(i);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent i = new Intent(getActivity().getApplicationContext(), ListOfPoints.class);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent= new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
                String txttoshare = "Total CME Points: " + txt.getText().toString();
                Utility.takeScreenshot(getActivity(), v, txttoshare);
                v.setVisibility(View.VISIBLE);
//                Utility.shareClick("Text", getActivity());

            }
        });
    }


    protected void GetListOfPoints() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "CMEPoints/GetCMEPointListforUser/" + General.GUID);
                    // httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

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
                        final String strFirstYear = Objobject.getString("FirstYearPoints").toString();
                        final String strSecondYear = Objobject.getString("SecondYearPoints").toString();
                        final String strThiredYear = Objobject.getString("ThirdYearPoints").toString();
                        final String strFourthYear = Objobject.getString("FourthYearPoints").toString();
                        final String strFifthYear = Objobject.getString("FifthYearPoints").toString();
                        final String strTotalpoints = Objobject.getString("Totalpoints").toString();

                        final String strMessage = Objobject.getString("Message").toString();
                        final String strDate = Objobject.getString("ReferenceDate").toString();
                        String timestamp1 = strDate.replace("/Date(", "").replace(")/", "");
                        Date createdon1 = new Date(Long.parseLong(timestamp1));
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM- yyyy");
                        General.ReferenceDate = sdf1.format(createdon1);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!strMessage.equals("")) {
                                    MsgBox(strMessage);
                                }

                                FirstYearCount.setText(strFirstYear);
                                SecondYearCount.setText(strSecondYear);
                                ThirdYearCount.setText(strThiredYear);
                                FourthYearCount.setText(strFourthYear);
                                FifthYearCount.setText(strFifthYear);
                                FinalInputString = "000".substring(0, (3 - strTotalpoints.length())) + strTotalpoints;

                                txt.setText(FinalInputString);
                                int number = Integer.parseInt(txt.getText().toString());

                              /*  if (number>30)
                                {
                                    CompressFile1.setVisibility(View.INVISIBLE);
                                    CompressFile.setVisibility(View.VISIBLE);
                                }*/

                            }
                        });


                        //  final     String strResult = Objobject.getString("result").toString();


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

    public void MsgBox(String strMessage) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(strMessage);
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


    protected void CompressEmailFile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(General.baseurl + "Email/MailCompressedFileRequest");
                    //httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("UserId", General.GUID));
                    // nameValuePairs.add(new BasicNameValuePair("PictureRequestList",));


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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //SaveProfileMsg();
                                        Msg();


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

                                    ErrorMsg();

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

    public void Msg() {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setMessage("Your Request has been succeeded");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void ErrorMsg() {
        try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
