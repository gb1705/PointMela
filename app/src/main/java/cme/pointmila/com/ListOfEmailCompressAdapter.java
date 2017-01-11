package cme.pointmila.com;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by amoln on 15-11-2016.
 */
public class ListOfEmailCompressAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    List<HashMap<String,String>> DataCollection;

    String OrDId;

    public Context appContext;
    public Fragment tmpActivity;
    public ListOfEmailCompressAdapter()
    {
        // this.appContext = appContext;
    }

    public ListOfEmailCompressAdapter(Activity act, List<HashMap<String,String>> map)
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
                vi = inflater.inflate(R.layout.emailcompress_list_row, null);
                holder = new ViewHolder();
                holder.CMEno = (TextView)vi.findViewById(R.id.cmeNo);
                holder.CMEpoints = (TextView)vi.findViewById(R.id.cmepoint);
                holder.CMEcourse = (TextView)vi.findViewById(R.id.cmeCourse);
                holder.Date = (TextView)vi.findViewById(R.id.date);
                holder.check = (CheckBox)vi.findViewById(R.id.check);


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

        holder.CMEno.setText(DataCollection.get(position).get("CMENumber"));
        holder.CMEpoints.setText(DataCollection.get(position).get("CMEPoints"));
        holder.CMEcourse.setText(DataCollection.get(position).get("CMECourse"));
        holder.Date.setText(DataCollection.get(position).get("CMEDate"));
        holder.PointId = DataCollection.get(position).get("PointID");
        holder.Certificateid = DataCollection.get(position).get("CMECertificateid");


        final ViewHolder hold = holder;

        hold.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if (  hold.check.isChecked())
                {
                    HashMap<String,String>data = DataCollection.get(position);

                   // General.SelectedFile = data.get("CMECertificateid")+",";
                    General.SelectedFile = General.SelectedFile + data.get("CMECertificateid")+",";

                }

            }
        });
        if (position%2 ==0)
        {
            vi.setBackgroundColor(Color.parseColor("#E5E4E2"));
        }
        else
        {
            vi.setBackgroundColor(Color.parseColor("#D1D0CE"));
        }


       /* holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HashMap<String,String>data = DataCollection.get(position);

                try {
                    HashMap<String,String>data = DataCollection.get(position);
                    Intent i = new Intent(appContext,PointsEditing.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra("PointId", data.get("PointID"));
                    appContext.startActivity(i);

                    // ((ActionBarActivity)appContext).finish();

                    //((ListOfPoints)appContext).finish();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });*/


        return  vi;
    }

    private static  class  ViewHolder
    {
        // public String orderId;
        public String PointId;
        public TextView CMEno;
        public TextView CMEpoints;
        public TextView CMEcourse;
        public TextView Date;
        public CheckBox check;
        public String Certificateid;


    }



}
