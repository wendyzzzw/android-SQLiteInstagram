package project3.wenproject3.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import project3.wenproject3.MainActivity;
import project3.wenproject3.R;
import project3.wenproject3.database.MyDAO;
import project3.wenproject3.model.Music;
import project3.wenproject3.model.Photo;
import project3.wenproject3.model.User;

public class HomepageActivity extends AppCompatActivity {
    public static  final  String KEY_USERNAME = "username";
    private String username;
    private User mUser;

    private ImageButton mHome;
    private ImageButton mExplore;
    private ImageButton mCamera;
    private ImageButton mFavorite;
    private ImageButton mProfile;

    private LinearLayout mBotBar;

    private Context context;

    private UserProfileFragment mUPfrag;
    private UserListFragment mULfrag;
    private PhotoListFragment mPLfrag;
    private LikeListFragment mLLfrag;

    private UUID mPhotoId;
    private String mPhotoFilename;
    private File mPhotoFile;

    private static final String DIR_MUSIC = "music";
    private MediaPlayer mPlayer;
    private AssetManager mAssets;
    private Music mMusic;
    private int mPlay=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_homepage);

        mAssets = getAssets();
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
            }
        });



        Intent intent = getIntent();
        if (intent!= null){
            username = intent.getStringExtra(MainActivity.KEY_USERNAME);
        }


        mBotBar = (LinearLayout) findViewById(R.id.hp_bot_bar) ;

        mHome = (ImageButton) findViewById(R.id.hp_bot_bar_view);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBotBar.setBackground(getDrawable(R.drawable.hp_view));
                mPLfrag = PhotoListFragment.newInstance(username);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.hp_fl_container, mPLfrag, null)
                        .commit();
            }
        });

        mExplore = (ImageButton) findViewById(R.id.hp_bot_bar_explore);
        mExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBotBar.setBackground(getDrawable(R.drawable.hp_explore));
                mULfrag = UserListFragment.newInstance(username);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.hp_fl_container, mULfrag, null)
                        .commit();
            }
        });

        mFavorite = (ImageButton) findViewById(R.id.hp_bot_bar_favorite);
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBotBar.setBackground(getDrawable(R.drawable.hp_favorite));
                mLLfrag = LikeListFragment.newInstance(username);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.hp_fl_container, mLLfrag, null)
                        .commit();
            }
        });

        mProfile = (ImageButton) findViewById(R.id.hp_bot_bar_profile);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBotBar.setBackground(getDrawable(R.drawable.hp_profile));
                mUPfrag = UserProfileFragment.newInstance(username);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.hp_fl_container, mUPfrag, null)
                        .commit();
            }
        });

        mCamera = (ImageButton) findViewById(R.id.hp_bot_bar_photo);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mPhotoId = UUID.randomUUID();
                mPhotoFilename = "IMG_" + mPhotoId.toString() + ".jpg";
                File picturesDir =
                        HomepageActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mPhotoFile = new File(picturesDir, mPhotoFilename);
                Uri photoUri = Uri.fromFile(mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(intent, 0);
            }
        });

        mUPfrag = UserProfileFragment.newInstance(username);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.hp_fl_container, mUPfrag, null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {

            Photo photo = new Photo(mPhotoId);
            photo.setPath(mPhotoFile.getPath());
            photo.setOwnerUsername(username);
            MyDAO.get(context).addPhoto(photo);

            mUser = MyDAO.get(context).getUser(username);
            mUser.setPosts(mUser.getPosts()+1);
            MyDAO.get(context).updateUser(mUser);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBotBar.setBackground(getDrawable(R.drawable.hp_profile));
        mUPfrag = UserProfileFragment.newInstance(username);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.hp_fl_container, mUPfrag, null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled;
        switch(item.getItemId()) {

            case R.id.menu_item_music_play:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.invalidateOptionsMenu();
                }
                if (mPlay == 2){
                    mPlayer.start();
                }
                if (mPlay == 0){
                    try {
                        String[] musicNames = mAssets.list(DIR_MUSIC);
                        String path = DIR_MUSIC + "/" + musicNames[0];
                        mMusic = new Music(musicNames[0], path);
                        play(mMusic);
                    }
                    catch (IOException ioe) {
                    }
                }
                handled =true;
                break;

            case R.id.menu_item_music_pause:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    this.invalidateOptionsMenu();
                }
                mPlayer.pause();
                handled =true;
                mPlay=2;
                break;

            case R.id.menu_item_web_view:
                Intent intent = new Intent(HomepageActivity.this,WebViewActivity.class);
                startActivity(intent);
                handled =true;
                break;

            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }
        return handled;
    }

    private void play (Music music) {
        try {
            AssetManager assets = getAssets();
            AssetFileDescriptor afd = assets.openFd(music.getPath());
            if(mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mPlayer.prepare();
        }
        catch(IOException ioe) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        mPlayer = null;
    }


}
