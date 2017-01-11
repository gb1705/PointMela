package cme.pointmila.com;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by amoln on 03-11-2016.
 */
public class FirstYearActivity extends ActionBarActivity
{
    String EndDate,Date,StartDate,Month,Year,firstDate;
    String[] arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstyear);
        try {

            Date = General.ReferenceDate;
            // arr = Date.split("/");

           // StartDate = arr[0];
           // Month = arr[1];
            //Year = arr[2];
            //firstDate = Year;




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    }
