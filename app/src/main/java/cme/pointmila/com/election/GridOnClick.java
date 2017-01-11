package cme.pointmila.com.election;

import android.view.View;

import cme.pointmila.com.election.models.MyCandidate;

/**
 * Created by gauravbhoyar on 03/12/16.
 */

public interface GridOnClick {

    void onGridClickForNav(View v, int position, MyCandidate myCandidate);
}
