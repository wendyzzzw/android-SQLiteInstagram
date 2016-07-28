package project3.wenproject3.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import project3.wenproject3.MainActivity;
import project3.wenproject3.R;
import project3.wenproject3.database.MyDAO;
import project3.wenproject3.image.PhotoUtils;
import project3.wenproject3.image.RoundImageView;
import project3.wenproject3.model.User;

public class EditProfileActivity extends AppCompatActivity {
    public static  final  String KEY_USERNAME = "username";
    private String username;
    private User mUser;
    private Context context;

    private TextView mUsername;
    private RoundImageView mProfilePic;
    private EditText mName;
    private EditText mCurrentPw;
    private EditText mNewPw;
    private EditText mNewPw2;

    private String mFilename;
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Profile");
        setContentView(R.layout.activity_edit_profile);
        context = this;

        Intent intent = getIntent();
        if (intent!= null){
            username = intent.getStringExtra(UserProfileFragment.KEY_USERNAME);
        }
        mUser = MyDAO.get(context).getUser(username);
        mFilename = mUser.getProfilePic();

        mUsername = (TextView) findViewById(R.id.edit_profile_username);
        mProfilePic = (RoundImageView) findViewById(R.id.edit_profile_profilepic);
        mName = (EditText)findViewById(R.id.edit_profile_name);
        mCurrentPw = (EditText) findViewById(R.id.edit_profile_current_pw);
        mNewPw = (EditText) findViewById(R.id.edit_profile_new_pw);
        mNewPw2 = (EditText)findViewById(R.id.edit_profile_new_pw2);

        mUsername.setText(mUser.getUsername());
        mName.setText(mUser.getName());

        if (mUser.getProfilePic().equals("user")){

        }else{
            File picturesDir =
                    EditProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = new File(picturesDir, mUser.getProfilePic());
            Bitmap photo = PhotoUtils.getScaledBitmap(file.getPath(),
                    mProfilePic.getWidth(), mProfilePic.getHeight());
            mProfilePic.setImageBitmap(photo);
        }

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                mFilename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                File picturesDir =
                        EditProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mPhotoFile = new File(picturesDir, mFilename);

                Uri photoUri = Uri.fromFile(mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(intent, 0);
            }
        });

        Button mDone = (Button) findViewById(R.id.edit_profile_done);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setName(mName.getText().toString());
                mUser.setProfilePic(mFilename);

                String currentPw = mCurrentPw.getText().toString();
                if (currentPw.trim().length() ==0){
                    Toast.makeText(context,"Change has been submitted.",Toast.LENGTH_SHORT).show();
                }else{
                    if (currentPw.equals(mUser.getPassword())){
                        String newPw1 = mNewPw.getText().toString();
                        String newPw2 = mNewPw2.getText().toString();

                        if (newPw1.trim().length()==0){
                            Toast.makeText(context,"Please enter your new password.",Toast.LENGTH_SHORT).show();
                        }else if (! newPw1.equals(newPw2)){
                            Toast.makeText(context,"New passwords do not match.",Toast.LENGTH_SHORT).show();
                        }else{
                            mUser.setPassword(newPw1);
                            Toast.makeText(context,"Change has been submitted.",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(context,"Your old password was entered incorrectly, please enter again.",Toast.LENGTH_SHORT).show();
                    }
                }

                MyDAO.get(context).updateUser(mUser);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //reference: android developer
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(KEY_USERNAME,username);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
