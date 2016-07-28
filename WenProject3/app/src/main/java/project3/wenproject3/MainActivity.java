package project3.wenproject3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import project3.wenproject3.database.MyDAO;
import project3.wenproject3.homepage.HomepageActivity;
import project3.wenproject3.model.Music;
import project3.wenproject3.model.User;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_USERNAME = "username";
    private Context context;
    private String username;
    private String password;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        context = this;

        final EditText mUsername = (EditText) findViewById(R.id.main_username);
        final EditText mPassword = (EditText) findViewById(R.id.main_password);

        Button mSignUp = (Button) findViewById(R.id.main_signup);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });


        //log in
        Button mLogIn = (Button) findViewById(R.id.main_log_in);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                mUser = MyDAO.get(context).getUser(username);
                if(mUser!=null){
                    if (mUser.getPassword().equals(password)){
                        Intent intent = new Intent(MainActivity.this,HomepageActivity.class);
                        intent.putExtra(KEY_USERNAME,username);
                        startActivityForResult(intent,0);
                    }else {
                        Toast.makeText(context, "Password incorrect, try again." ,Toast.LENGTH_SHORT ).show();
                    }

                }else{
                    Toast.makeText(context, "Username does not exist, pleas register." ,Toast.LENGTH_SHORT ).show();
                }
            }
        });

        ImageView mInsta = (ImageView)findViewById(R.id.main_insta);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_view);
        mInsta.startAnimation(hyperspaceJumpAnimation);
    }


}
