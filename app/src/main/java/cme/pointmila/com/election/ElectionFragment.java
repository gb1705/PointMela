package cme.pointmila.com.election;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import cme.pointmila.com.R;
import cme.pointmila.com.Utility;
import cme.pointmila.com.election.Util.Connectivity;
import cme.pointmila.com.election.Util.RestClient;
import cme.pointmila.com.election.models.DetailInfo;
import cme.pointmila.com.election.models.MyCandidate;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static cme.pointmila.com.ProfileDashboardActivity.myCandidates;


/**
 * Created by amoln on 04-11-2016.
 */
public class ElectionFragment extends Fragment implements GridOnClick {

    GridView gridView;
    private ProgressDialog progressDialog;
    public static DetailInfo detailInfo;
    Button contactus;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_election, null);
        gridView = (GridView) view.findViewById(R.id.gridview);
        contactus = (Button) view.findViewById(R.id.callbutton);

        gridView.setAdapter(new ImageAdapter(getActivity(), myCandidates, this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactUs.class);
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onGridClickForNav(View v, int position, MyCandidate myCandidate) {
        if (myCandidate.isAalowDetailView()) {
            if (Connectivity.isConnected(getActivity())) {
                getProgressBar();
                getElectionDataFromServer(myCandidate);
            } else {
                Utility.showTempAlert(getActivity(), "Please Check Internet Connection");
            }

        } else {
            Utility.showTempAlert(getActivity(), "Detailed View Not Available");
        }
    }

    public void getElectionDataFromServer(final MyCandidate myCandidate) {

        RestClient.GitApiInterface service = RestClient.getClient();
        final Call<DetailInfo> call = service.callElectionDetail(myCandidate.getCandidateId());
        call.enqueue(new Callback<DetailInfo>() {
            @Override
            public void onResponse(Response<DetailInfo> response, Retrofit retrofit) {
                if (response.code() == 200 || !response.message().equals("Not Found")) {
                    dismissProgress();

                    detailInfo = response.body();
                    if (detailInfo.getSuccess()) {
                        Intent intent = new Intent(getActivity(), ElectionDetailedActivity.class);
                        intent.putExtra("name", myCandidate.getCandidateName());
                        intent.putExtra("img", myCandidate.getCandidateProfilePicstring());
                        getActivity().startActivity(intent);
                    } else {
                        Utility.showTempAlert(getActivity(), "Info not available");
                    }


                } else {
                    Utility.showTempAlert(getActivity(), "Info not available");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.toString();

            }
        });

    }


    public void getProgressBar() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }

    public void dismissProgress() {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }
}
