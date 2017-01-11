package cme.pointmila.com;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by amoln on 09-09-2016.
 */
public class OTPSuccessfulActivity extends AppCompatActivity
{
    Button Ok;
    @Override
    protected void onCreate(Bundle SaveInstanceState)
    {
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.activity_otpsuccessful);
        Ok=(Button)findViewById(R.id.okbtn);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgBox();

            }
        });
    }

    public void MsgBox()
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Successfully Register,Please Login!!!");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                OTPSuccessfulActivity.this.startActivity(i);
                OTPSuccessfulActivity.this.finish();

                dialog.cancel();
                //progressDialog.cancel();
            }
        });
        AlertDialog alert = dlgAlert.create();
        alert.show();

    }
}
