package cme.pointmila.com.election;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cme.pointmila.com.R;
import cme.pointmila.com.election.Util.ShowPdf;

import static cme.pointmila.com.ProfileDashboardActivity.mmcPanels;

public class MMCPanelLitsFragment extends Fragment {
    ListView mmcpanelListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mmcpanel_lits_fragment, null);
        mmcpanelListView = (ListView) view.findViewById(R.id.listmmcpannel);

        mmcpanelListView.setAdapter(new MMCPanelListAdpater(getActivity(), mmcPanels));

        mmcpanelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowPdf.class);
                intent.putExtra("url", mmcPanels.get(position).getMMCPanelPDFlink());
                startActivity(intent);

            }
        });


        return view;
    }


}
