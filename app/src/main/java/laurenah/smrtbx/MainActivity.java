package laurenah.smrtbx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    public static final String CHANNEL_ID = "id_smrtbox";
    private static final String CHANNEL_NAME = "SmrtBox";
    private static final String CHANNEL_DESC = "SmrtBox Notifications";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));

        mAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().subscribeToTopic("mailbox");

        FirebaseMessaging.getInstance().subscribeToTopic("mailbox")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Subscribed to Mailbox", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Creating Notification Channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        etEmail = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When Sign up is clicked, create the user
                startSignupActivity();
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When Sign up is clicked, create the user
                if (etEmail.getText().toString().trim().equals("")) {
                    etEmail.setError("Email required");
                    etPassword.requestFocus();
                } else if (etPassword.getText().toString().trim().equals("")) {
                    etPassword.setError("Password required");
                    etPassword.requestFocus();
                } else {
                    userLogin(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                }
            }
        });
    }

    //User login method
    //Logs the user in
    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startProfileActivity();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if user is logged in, redirect
        if (mAuth.getCurrentUser() != null) {
            startProfileActivity();
        }
    }

    //Start ProfileActivity Activity method
    //Starts the profile activity
    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Start Signup Activity method
    //Starts the Signup activity
    private void startSignupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
