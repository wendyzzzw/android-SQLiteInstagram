package project3.wenproject3.homepage;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import project3.wenproject3.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    private ProgressBar mProgressBar;
    private WebView mWebView;


    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar_bowser);
        mProgressBar.setMax(100);

        mWebView = (WebView)view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                String scheme = uri.getScheme();
                return (scheme.equalsIgnoreCase("http") == false &&
                        scheme.equalsIgnoreCase("https") == false);
            }
        });

        String url = "http://www.instagram.com";
        mWebView.loadUrl(url);

        return view;
    }

    public boolean goBack() {
        boolean didGoBack = false;
        if(mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            didGoBack = true;
        }
        return didGoBack;
    }

}
