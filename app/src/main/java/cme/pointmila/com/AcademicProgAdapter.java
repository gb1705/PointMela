package cme.pointmila.com;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cme.pointmila.com.R;

/**
 * Created by amoln on 09-11-2016.
 */
public class AcademicProgAdapter extends BaseAdapter
{
    LayoutInflater inflater;
    List<HashMap<String,String>> DataCollection;
    public Context appContext;
    public Fragment tmpActivity;
    public AcademicProgAdapter()
    {}

    public AcademicProgAdapter(Activity act, List<HashMap<String,String>> map)
    {
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

    public View getView(int position, View convertView, ViewGroup parent)
    {

        View vi = convertView;
        ViewHolder holder = null;
        if (vi==null)
        {
            try {
                vi = inflater.inflate(R.layout.state_row, null);
                holder = new ViewHolder();
                holder.Text = (TextView) vi.findViewById(R.id.txt);
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
        try {

            holder.Text.setText(DataCollection.get(position).get("ProgramDescription"));
            holder.DoctorspecialityId=DataCollection.get(position).get("ProgramID");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return  vi;
    }

    private static  class  ViewHolder
    {
        public TextView Text;
        public  String DoctorspecialityId;
    }



}
