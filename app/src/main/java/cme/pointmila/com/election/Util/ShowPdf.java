package cme.pointmila.com.election.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cme.pointmila.com.R;

public class ShowPdf extends Activity {
    WebView mWebView;
    boolean loadingFinished = true;
    boolean redirect = false;
    private boolean isRedirected;
    String mDocViewr = "https://docs.google.com/gview?embedded=true&url=";
    String mUrl1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        try {

            try {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    mUrl1 = bundle.getString("url", "");
                }

            } catch (Exception e) {

            }
            mWebView = (WebView) findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);

            startWebView(mWebView, mDocViewr + mUrl1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startWebView(WebView webView, String url) {

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                isRedirected = true;
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isRedirected = false;
            }

            public void onLoadResource(WebView view, String url) {
                if (!isRedirected) {
                    try {
                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(ShowPdf.this);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

            }

            public void onPageFinished(WebView view, String url) {
                try {
                    isRedirected = true;

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
