package cme.pointmila.com.election;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cme.pointmila.com.R;
import cme.pointmila.com.election.Util.ShowPdf;

public class ElectionDetailedActivity extends AppCompatActivity implements View.OnClickListener {
    String base64, drName;

    private RelativeLayout detailArea;
    private ImageView drImage;
    private TextView drnametextview;
    private TextView qualificationtextview;
    private TextView mmcnotextview;
    private TextView districttxt;
    private View seperator;
    private TextView phonenumnbertxtview;
    private TextView textmain;
    private LinearLayout footer;
    private Button pdfbuttonS;
    private Button callbutton;
    private Button videolink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_detailed);
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                base64 = bundle.getString("img", "");
                drName = bundle.getString("name", "");
            }

        } catch (Exception e) {

        }
        findViews();
        setviews();

    }

    private void setviews() {
        drnametextview.setText(drName);
        qualificationtextview.setText(ElectionFragment.detailInfo.getQualification());
        mmcnotextview.setText(ElectionFragment.detailInfo.getMMCNo());
        districttxt.setText(ElectionFragment.detailInfo.getMMCDistric());
        phonenumnbertxtview.setText(ElectionFragment.detailInfo.getPhNo());
        WebView webview = (WebView) this.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
//        webview.loadDataWithBaseURL("", ElectionFragment.detailInfo.getDetailTest(), "text/html", "UTF-8", "");
        webview.loadUrl(ElectionFragment.detailInfo.getDetailTest());


    }


    public void setImage(ImageView image, String base64) {
        if (base64 != null && !base64.equals("")) {
            try {
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 100, 100, false));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-12-03 03:08:45 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        detailArea = (RelativeLayout) findViewById(R.id.detail_area);

        drImage = (ImageView) findViewById(R.id.dr_image);

        drnametextview = (TextView) findViewById(R.id.drnametextview);
        qualificationtextview = (TextView) findViewById(R.id.qualificationtextview);
        mmcnotextview = (TextView) findViewById(R.id.mmcnotextview);
        districttxt = (TextView) findViewById(R.id.districttxt);

        seperator = (View) findViewById(R.id.seperator);

        phonenumnbertxtview = (TextView) findViewById(R.id.phonenumnbertxtview);
        footer = (LinearLayout) findViewById(R.id.footer);
        pdfbuttonS = (Button) findViewById(R.id.pdfbuttonS);
        callbutton = (Button) findViewById(R.id.callbutton);
        videolink = (Button) findViewById(R.id.videolink);
        setImage(drImage, base64);

        pdfbuttonS.setOnClickListener(this);
        callbutton.setOnClickListener(this);
        videolink.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-12-03 03:08:45 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == pdfbuttonS) {
            Intent intent = new Intent(ElectionDetailedActivity.this, ShowPdf.class);
            intent.putExtra("url", ElectionFragment.detailInfo.getPDFLink());
            startActivity(intent);
        } else if (v == callbutton) {
            Intent intent = new Intent(ElectionDetailedActivity.this, ContactUs.class);
            startActivity(intent);
            // Handle clicks for callbutton
        } else if (v == videolink) {
            Intent videoClient = new Intent(Intent.ACTION_VIEW);
            videoClient.setData(Uri.parse(ElectionFragment.detailInfo.getYoutubeLink()));
            startActivityForResult(videoClient, 1234);
            // Handle clicks for videolink
        }
    }

}
