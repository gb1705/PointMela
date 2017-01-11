package cme.pointmila.com;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 11-11-2016.
 */
public class AdditionalQualificationAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    List<HashMap<String,String>> DataCollection;

    String OrDId;

    public Context appContext;
    public Fragment tmpActivity;
    public AdditionalQualificationAdapter()
    {
        // this.appContext = appContext;
    }

    public AdditionalQualificationAdapter(Activity act, List<HashMap<String,String>> map)
    {
        this.appContext = act.getApplicationContext();
        try {
            this.DataCollection = map;
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public int getCount()
    {
        return DataCollection.size();
    }
    public Object getItem(int arg0)
    {
        return null;
    }
    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {

        View vi = convertView;
        ViewHolder holder = null;
        if (vi==null)
        {
            try {
                vi = inflater.inflate(R.layout.activity_additional_qualify_row, null);
                holder = new ViewHolder();
                holder.QualificationType = (TextView)vi.findViewById(R.id.type);
                holder.QualificationDate = (TextView)vi.findViewById(R.id.date);
                holder.Edit = (TextView)vi.findViewById(R.id.edit);

                vi.setTag(holder);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            holder = (ViewHolder)vi.getTag();
        }

        holder.QualificationType.setText(DataCollection.get(position).get("QualifyType"));
        holder.QualificationDate.setText(DataCollection.get(position).get("QualifyDate"));
        holder.QualificationId = DataCollection.get(position).get("QualifyID");





        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    HashMap<String,String> data = DataCollection.get(position);
                    Intent i = new Intent();
                    i.setClass(appContext,AdditionalQualification_Update.class);
                    i.putExtra("QualifyId",data.get("QualifyID"));
                    tmpActivity.startActivity(i);
                  /*  HashMap<String, String> data = DataCollection.get(position);
                    Intent i = new Intent();
                    i.setClass(appContext, ClinicalAddressupdate.class);
                    i.putExtra("AddressId", data.get("AddressID"));
                    i.putExtra("addressstreet1",data.get("AddressStreet1"));
                    i.putExtra("addressstreet2",data.get("AddressStreet2"));
                    i.putExtra("addressstate",data.get("AddressState"));
                    i.putExtra("addresscity",data.get("AddressCity"));
                    i.putExtra("addresscityId",data.get("CityiD"));
                    i.putExtra("pincode",data.get("AddressPincode"));

                    tmpActivity.startActivity(i);*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        return  vi;
    }

    private static  class  ViewHolder
    {
        public String QualificationId;
        public TextView QualificationDate;
        public TextView QualificationType;
        public TextView Edit;

    }


}
