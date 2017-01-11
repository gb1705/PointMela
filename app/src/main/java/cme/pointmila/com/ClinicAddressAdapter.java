package cme.pointmila.com;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cme.pointmila.com.R;

/**
 * Created by amoln on 23-09-2016.
 */
public class ClinicAddressAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] Name;
    private final String[] Street;
    private final String[] City;
    private final String[] Pincode;
    private final String[] State;


    public ClinicAddressAdapter(Activity context,
                            String[] Name,String[] Street, String[] City,String[] Pincode,String[] State)
    {
        super(context, R.layout.clinic_address_row, Name);
        this.context = context;
        this.Name = Name;
        this.Street = Street;
        this.City = City;
        this.Pincode = Pincode;
        this.State = State;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.clinic_address_row, null, true);
        TextView Nametxt = (TextView)rowView.findViewById(R.id.name);
        TextView StreetTxt = (TextView)rowView.findViewById(R.id.name1);
        TextView CityTxt = (TextView)rowView.findViewById(R.id.city);
        TextView PincodeTxt = (TextView)rowView.findViewById(R.id.pincode);
        TextView StateTxt = (TextView)rowView.findViewById(R.id.state);
        Nametxt.setText(Name[position]);
        StreetTxt.setText(Street[position]);
        CityTxt.setText(City[position]);
        PincodeTxt.setText(Pincode[position]);
        StateTxt.setText(State[position]);
        return rowView;
    }

}
