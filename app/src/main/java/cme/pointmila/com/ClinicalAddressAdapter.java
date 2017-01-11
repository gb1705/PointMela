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
 * Created by amoln on 07-11-2016.
 */
public class ClinicalAddressAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    List<HashMap<String,String>> DataCollection;

    String OrDId;

    public Context appContext;
    public Fragment tmpActivity;
    public ClinicalAddressAdapter()
    {
        // this.appContext = appContext;
    }

    public ClinicalAddressAdapter(Activity act, List<HashMap<String,String>> map)
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
                vi = inflater.inflate(R.layout.clinic_address_row, null);
                holder = new ViewHolder();
                holder.Street1 = (TextView)vi.findViewById(R.id.name);
                holder.Street2 = (TextView)vi.findViewById(R.id.name1);
                holder.StateName = (TextView)vi.findViewById(R.id.state);
                holder.CityName = (TextView)vi.findViewById(R.id.city);
                holder.Pincode = (TextView)vi.findViewById(R.id.pincode);
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

        holder.Street1.setText(DataCollection.get(position).get("AddressStreet1"));
        holder.Street2.setText(DataCollection.get(position).get("AddressStreet2"));
        holder.StateName.setText(DataCollection.get(position).get("AddressState"));
        holder.CityName.setText(DataCollection.get(position).get("AddressCity"));
        holder.Pincode.setText(DataCollection.get(position).get("AddressPincode"));
        holder.Addressid = DataCollection.get(position).get("AddressID");
        holder.CityID = DataCollection.get(position).get("CityiD");



        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HashMap<String, String> data = DataCollection.get(position);
                    Intent i = new Intent();
                    i.setClass(appContext, ClinicalAddressupdate.class);
                    i.putExtra("AddressId", data.get("AddressID"));
                    i.putExtra("addressstreet1",data.get("AddressStreet1"));
                    i.putExtra("addressstreet2",data.get("AddressStreet2"));
                    i.putExtra("addressstate",data.get("AddressState"));
                    i.putExtra("addresscity",data.get("AddressCity"));
                    i.putExtra("addresscityId",data.get("CityiD"));
                    i.putExtra("pincode",data.get("AddressPincode"));

                    tmpActivity.startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
      /*  holder.CMEno.setText(DataCollection.get(position).get("CMENumber"));
        holder.CMEpoints.setText(DataCollection.get(position).get("CMEPoints"));
        holder.CMEcourse.setText(DataCollection.get(position).get("CMECourse"));
        holder.Date.setText(DataCollection.get(position).get("CMEDate"));
        holder.PointId = DataCollection.get(position).get("PointID");*/




        return  vi;
    }

    private static  class  ViewHolder
    {
        // public String orderId;
        public String Addressid;
        public TextView Street1;
        public TextView Street2;
        public TextView StateName;
        public TextView CityName;
        public TextView Pincode;
        public TextView Edit;
        public String CityID;


    }



}
