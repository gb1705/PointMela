package cme.pointmila.com;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amoln on 12-10-2016.
 */
public class CityAdapter extends ArrayAdapter<City>
{
    Context context;
    int resource,textViewResourceId;
    List<City> items, tempItems, suggestions;
    public Context appContext;
    public Fragment tmpActivity;


    public CityAdapter(Context context,int resource,int textViewResourceId, List<City> items)
    {
        super(context, resource, textViewResourceId,items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<City>(items); // this makes the difference.
        suggestions = new ArrayList<City>();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.city_row, parent, false);
        }
        //Contacts.Med people = items.get(position);
        City people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.txt);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter()
    {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((City) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (City people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<City> filterList = (ArrayList<City>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (City people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }


    };


}
