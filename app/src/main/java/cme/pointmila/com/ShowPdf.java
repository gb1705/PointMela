package cme.pointmila.com;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowPdf extends Fragment {
    WebView mWebView;
    boolean loadingFinished = true;
    boolean redirect = false;
    private boolean isRedirected;
    String mDocViewr = "https://docs.google.com/gview?embedded=true&url=";
//    String mUrl1 = "http://52.172.185.34/MMCDocuments/MahMedicalCouncilRules1967.pdf";
//    String mUrl2 = "http://52.172.185.34/MMCDocuments/Code-of-Medical-Ethics-Regulations.pdf";
//    String mUrl3 = "http://52.172.185.34/MMCDocuments/MahMedicalCouncilAct1965.46.pdf";

    String mUrl1 = General.baseurl + "MMCDocuments/MahMedicalCouncilRules1967.pdf";
    String mUrl2 = General.baseurl + "MMCDocuments/Code-of-Medical-Ethics-Regulations.pdf";
    String mUrl3 = General.baseurl + "MMCDocuments/MahMedicalCouncilAct1965.46.pdf";



//    MMC Rules
//    http://52.172.185.34/MMCDocuments/MahMedicalCouncilRules1967.pdf
//    MMC Ethics
//    http://52.172.185.34/MMCDocuments/Code-of-Medical-Ethics-Regulations.pdf
//    MMC Act
//    http://52.172.185.34/MMCDocuments/MahMedicalCouncilAct1965.46.pdf


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String strtext = getArguments().getString("CID");

        View view = inflater.inflate(R.layout.activity_show_pdf, null);
        try {


            mWebView = (WebView) view.findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            if (strtext.equals("1"))
                startWebView(mWebView, mDocViewr + mUrl1);
            else if (strtext.equals("2"))
                startWebView(mWebView, mDocViewr + mUrl2);
            else if (strtext.equals("3"))
                startWebView(mWebView, mDocViewr + mUrl3);


        } catch (Exception e) {
            e.printStackTrace();
        }
        // finish();

        return view;
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
                            progressDialog = new ProgressDialog(getActivity());
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
