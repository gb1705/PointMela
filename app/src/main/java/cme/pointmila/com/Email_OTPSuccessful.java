package cme.pointmila.com;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by amoln on 14-11-2016.
 */
public class Email_OTPSuccessful extends ActionBarActivity
{
    Button Ok;
    @Override
    protected void onCreate(Bundle SaveInstanceState)
    {
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.changepassw_otpsuccessful);
        Ok=(Button)findViewById(R.id.okbtn);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  MsgBox();
                Intent i = new Intent(getApplicationContext(),Reset_PasswordActivity.class);
                Email_OTPSuccessful.this.startActivity(i);
                Email_OTPSuccessful.this.finish();


            }
        });
    }


}
