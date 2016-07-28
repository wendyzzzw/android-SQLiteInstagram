package project3.wenproject3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import project3.wenproject3.database.MyDAO;
import project3.wenproject3.image.PhotoUtils;
import project3.wenproject3.image.RoundImageView;
import project3.wenproject3.model.User;

public class SignUpActivity extends AppCompatActivity {
    private Context context;
    private EditText mName;
    private EditText mUsername;
    private EditText mPassword;
    private RoundImageView mProfilePic;

    private String mFilename = "user";
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sign Up");
        setContentView(R.layout.activity_sign_up);
        context = this;

        mName = (EditText) findViewById(R.id.signup_name);
        mUsername = (EditText) findViewById(R.id.signup_username);
        mPassword = (EditText) findViewById(R.id.signup_password);
        mProfilePic = (RoundImageView) findViewById(R.id.signup_profile);

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                mFilename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                File picturesDir =
                        SignUpActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mPhotoFile = new File(picturesDir, mFilename);

                Uri photoUri = Uri.fromFile(mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(intent, 0);
            }
        });

        Button mSignUp = (Button) findViewById(R.id.signup_button);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (username.trim().length() == 0){
                    Toast.makeText(SignUpActivity.this, "Username cannot be empty.",Toast.LENGTH_SHORT ).show();
                }else if (password.trim().length() == 0){
                    Toast.makeText(SignUpActivity.this, "Password cannot be empty.",Toast.LENGTH_SHORT ).show();
                }else if(MyDAO.get(context).getUser(username)!=null){
                    Toast.makeText(SignUpActivity.this, "Username already exists, choose another one.",Toast.LENGTH_SHORT ).show();
                }else{
                    User user = new User(username,password);
                    user.setName(name);
                    user.setProfilePic(mFilename);

                    MyDAO.get(context).addUser(user);
                    Intent intent1 = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent1);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Bitmap photo = PhotoUtils.getScaledBitmap(mPhotoFile.getPath(),
                    mProfilePic.getWidth(), mProfilePic.getHeight());
            mProfilePic.setImageBitmap(photo);

        }
    }
}
