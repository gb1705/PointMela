package cme.pointmila.com.election;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.TextView;

import cme.pointmila.com.R;

public class ContactUs extends AppCompatActivity {

    private TextView con1;
    private TextView con5;
    private TextView emailtxt;
    private TextView con2;
    private TextView con3;
    private TextView phonenumnbertxtview;
    private TextView con4;
    private TextView phonenumber2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        findViews();
    }


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-12-05 20:53:49 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        emailtxt = (TextView) findViewById(R.id.emailtxt);
        Linkify.addLinks(emailtxt, Linkify.EMAIL_ADDRESSES);
        phonenumnbertxtview = (TextView) findViewById(R.id.phonenumnbertxtview);
        Linkify.addLinks(phonenumnbertxtview, Linkify.PHONE_NUMBERS);
        phonenumber2 = (TextView) findViewById(R.id.phonenumber2);
        Linkify.addLinks(phonenumber2, Linkify.PHONE_NUMBERS);
    }

}
