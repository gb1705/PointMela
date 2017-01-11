package cme.pointmila.com.election;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cme.pointmila.com.R;
import cme.pointmila.com.election.models.MmcPanel;

/**
 * Created by gauravbhoyar on 11/12/16.
 */

public class MMCPanelListAdpater extends BaseAdapter {
    private Context mContext;
    List<MmcPanel> mmcLists;

    public MMCPanelListAdpater(Context c, List<MmcPanel> mmcLists) {

        this.mmcLists = mmcLists;
        try {
            mContext = c;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getCount() {
        return mmcLists.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        MMCPanelListAdpater.ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mmclist_item, null);
            holder = new MMCPanelListAdpater.ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.panelnametxt);
            convertView.setTag(holder);

        } else {
            holder = (MMCPanelListAdpater.ViewHolder) convertView.getTag();
        }
        holder.nameTextView.setText(mmcLists.get(position).getMMCPanelName());

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;


    }

}


