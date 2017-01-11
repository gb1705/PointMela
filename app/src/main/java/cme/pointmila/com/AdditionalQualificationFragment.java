package cme.pointmila.com;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 04-11-2016.
 */
public class AdditionalQualificationFragment extends Fragment
{
    ImageView AddImg;
    TextView QualificationTxt;
    ListView listView;

    JSONObject Objobject;
    List<HashMap<String,String>> lstBooking;
    HashMap<String,String> hmBooking=null;

   LinearLayout listviewLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_additionalqualification, null);
        lstBooking = new ArrayList<HashMap<String,String>>();
        QualificationTxt = (TextView)view.findViewById(R.id.qualification);
        AddImg = (ImageView)view.findViewById(R.id.addimg);
        listView = (ListView)view.findViewById(R.id.listview);
        listviewLayout = (LinearLayout)view.findViewById(R.id.listviewLayout);

        QualificationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AdditionalQualification_Popup.class);
                startActivity(i);
            }
        });

        AddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AdditionalQualification_Popup.class);
                startActivity(i);
            }
        });


        GetAdditionalQualification();

        return view;
    }

            protected  void GetAdditionalQualification()
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        try
                        {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpPost = new HttpGet(General.baseurl+"DoctorProfile/GetAdditionalQualificationInfoForUser/" + General.GUID);
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

                                //  final     String strResult = Objobject.getString("result").toString();

                                if (strSuccess.equals("true"))
                                {
                                    JSONArray jarray = new JSONArray(Objobject.getString("Info").toString());
                                    //  lstBooking = new ArrayList<HashMap<String, String>>();


                                    for (int i = 0; i < jarray.length(); i++) {
                                        hmBooking = new HashMap<String, String>();

                                        try {
                                            String QualificationType = jarray.getJSONObject(i).getString("QualificationType");
                                            String QualificationDate = jarray.getJSONObject(i).getString("QualificationDate");


                                            String timestamp = QualificationDate.replace("/Date(", "").replace(")/", "");
                                            Date passingdate = new Date(Long.parseLong(timestamp));
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd -MMM- yyyy");
                                            final    String formattedDate = sdf.format(passingdate);


                                            String ID = jarray.getJSONObject(i).getString("QualificationId");


                                            hmBooking.put("QualifyType",QualificationType);
                                            hmBooking.put("QualifyDate", formattedDate);
                                            hmBooking.put("QualifyID",ID);
                                            lstBooking.add(hmBooking);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            AdditionalQualificationAdapter clinicAddress = new AdditionalQualificationAdapter(getActivity(),lstBooking);
                                            clinicAddress.appContext = getActivity().getApplicationContext();
                                            clinicAddress.tmpActivity = AdditionalQualificationFragment.this;
                                            listviewLayout.setVisibility(View.VISIBLE);
                                            listView.setAdapter(clinicAddress);


                                            //  ListOfPointsAdapter pointslist = new ListOfPointsAdapter(AddressTabFragment.this,lstBooking);
                                            //   listView.setAdapter(pointslist);

                                        }
                                    });


                                }

                                else
                                {
                                    // General.Token = Objobject.getString("Token").toString();
                                    getActivity().runOnUiThread(new Runnable() {
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
    }
