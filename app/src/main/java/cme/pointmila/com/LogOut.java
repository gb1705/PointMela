package cme.pointmila.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by amoln on 04-11-2016.
 */
public class LogOut extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profiletab, null);
        try {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("finish", true); // if you are checking for this in your other Activities
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

           // ExistMsg();

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("username");
            editor.remove("password");
            editor.remove("checkLogin");
            editor.clear();
            editor.commit();
            getActivity().finish();
            getActivity().moveTaskToBack(true);
           // ((Activity) getActivity()).overridePendingTransition(0, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       // finish();

        return view;
    }



    protected void ExistMsg()
    {

     /*   try {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setMessage("Are you sure you want to exit");
            dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
            dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("username");
                    editor.remove("password");
                    editor.remove("checkLogin");
                    editor.commit();
                    getActivity().finish();
                    getActivity().moveTaskToBack(true);
                    // Mobile.setText("9594964665");
                    //Password.setText("jj");
                   // Intent i = new Intent(getApplicationContext(), ProfileDashboardActivity.class);
                   // LoginActivity.this.startActivity(i);
                    //LoginActivity.this.finish();
                    //dialog.cancel();
                    //progressDialog.cancel();
                }
            });
            AlertDialog alert = dlgAlert.create();
            alert.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/



    }



    }
