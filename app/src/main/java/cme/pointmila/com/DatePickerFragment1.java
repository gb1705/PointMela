package cme.pointmila.com;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by amoln on 12-09-2016.
 */
public class DatePickerFragment1 extends DialogFragment
{
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;

    public DatePickerFragment1() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }
    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //DatePickerDialog dialog = new DatePickerDialog(getActivity(),ondateSet,year,month,day);
        //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
       // return dialog;
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }


}
