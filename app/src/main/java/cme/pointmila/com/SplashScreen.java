package cme.pointmila.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amoln on 12-11-2016.
 */
public class SplashScreen extends Activity {
    Context mcontext = SplashScreen.this;
    public SharedPreferences appPreferences;
    boolean isAppInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        if (!checkAndRequestPermissions()) {


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
//            Toast.makeText(this, "You need to allow all the permission from setting", Toast.LENGTH_SHORT);
       } else {
            appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
            if (isAppInstalled == false) {
                Intent shortcutintent = new Intent(getApplicationContext(), SplashScreen.class);
                shortcutintent.setAction(Intent.ACTION_MAIN);
                Intent addIntent = new Intent();
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutintent);
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "PointMila");
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.logonew));
                addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                getApplicationContext().sendBroadcast(addIntent);

                SharedPreferences.Editor editor = appPreferences.edit();
                editor.putBoolean("isAppInstalled", true);
                editor.commit();

            }

            Thread timerThread = new Thread() {

                public void run() {
                    try {
                        //Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
                        sleep(3000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        //msgbox();
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    }

                }
            };
            timerThread.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
//                    (new String[listPermissionsNeeded.size()]), 23456);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 23456: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    criarFoto();
                } else {
                    Toast.makeText(SplashScreen.this, "You did not allow all Permission usage :(", Toast.LENGTH_SHORT).show();
//                    noFotoTaken();
                }
                return;
            }
        }
    }
}
