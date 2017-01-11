package cme.pointmila.com;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by amoln on 02-11-2016.
 */
public class Utility {
    static String timeZone = "GMT+5:30";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean hasPermissionInManifest(Context context, String permissionName) {
        boolean flag = false;
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        flag = true;
                        return flag;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return flag;
    }

    public static void showTempAlert(Context context, String message) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setMessage(message).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertbox.create();
        alertbox.show();
    }

    public static int checkDateNew(Date dateToBeChecked, Date referenceDate) {
        long refDate = referenceDate.getTime();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        cal.setTime(referenceDate);
        String str = referenceDate.toString();
        String temp = str.substring(11, 13);
        if (temp.indexOf("0") == 1) {
            temp = temp.substring(1, 2);
        }
        int hour = Integer.parseInt(temp);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        int milliseconds = cal.get(Calendar.MILLISECOND);
        long a = (hour * 3600 * 1000) + (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
        refDate = refDate - a;

        long toBeCheckedDate = dateToBeChecked.getTime();
        cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        cal.setTime(dateToBeChecked);
        str = dateToBeChecked.toString();
        temp = str.substring(11, 13);
        if (temp.indexOf("0") == 1) {
            temp = temp.substring(1, 2);
        }
        hour = Integer.parseInt(temp);
        minutes = cal.get(Calendar.MINUTE);
        seconds = cal.get(Calendar.SECOND);
        milliseconds = cal.get(Calendar.MILLISECOND);
        a = (hour * 3600 * 1000) + (minutes * 60 * 1000) + (seconds * 1000) + milliseconds;
        toBeCheckedDate = toBeCheckedDate - a;
        if (toBeCheckedDate < refDate) {
            return -1;
        } else if (toBeCheckedDate > refDate) {
            return 1;
        } else {
            return 0;
        }
    }


    public static void takeScreenshot(Context context, View textViews, String txtxtoshare) {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "pointmila44" + ".jpg";

            textViews.setVisibility(View.GONE);

            // create bitmap screen capture
            View v1 = ((Activity) context).getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            shareClick(txtxtoshare, context, mPath);

            textViews.setVisibility(View.VISIBLE);


//            share("gmail", mPath, context);

//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    public static void shareClick(String urlToShare, Context context, String filename) {
        Uri uri = Uri.parse("file:///" + filename);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PointMila");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent,
                "Share text Via"));

    }


}
