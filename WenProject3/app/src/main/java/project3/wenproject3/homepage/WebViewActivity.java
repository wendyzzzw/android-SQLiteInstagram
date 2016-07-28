package project3.wenproject3.homepage;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import project3.wenproject3.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        FragmentManager manager = getSupportFragmentManager();
        WebViewFragment frag = WebViewFragment.newInstance();
            manager.beginTransaction()
                    .add(R.id.fl_web_container, frag)
                    .commit();

    }
}
