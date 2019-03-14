package com.example.nix6;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ProgressBar nixProgressBar;
    WebView nixWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nixProgressBar = findViewById(R.id.progressBar);
        nixWebView = findViewById(R.id.nixWebView);

        nixProgressBar.setMax(100);

        nixWebView.loadUrl("https://www.google.com");
        nixWebView.getSettings().setJavaScriptEnabled(true);
        nixWebView.setWebViewClient(new WebViewClient());

        nixWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    injectCSS();
                    injectJS();
                }
                nixProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                //nixImageView.setImageBitmap(icon);
            }
        });

    }

    private void injectCSS() {
        try {
            InputStream inputStream = getAssets().open("style.css");
            //InputStream inputStream = getAssets().open("style.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            nixWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectJS() {
        try {
            InputStream inputStream = getAssets().open("nix.js");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            nixWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nix_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nix_menu_back:
                onBackPressed();
                break;
            case R.id.nix_menu_forward:
                onForwardPressed();
                break;
            case R.id.nix_menu_refresh:
                nixWebView.reload();
                break;
            case R.id.nix_string_list:
                nixWebView.reload();
                //Intent intent = new Intent(this, nixInputActivity.class);
                Intent intent = new Intent(this, WordList.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed () {
        if (nixWebView.canGoForward()) {
            nixWebView.goForward();
        } else {
            Toast.makeText(this, "Can't go forward.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (nixWebView.canGoBack()) {
            nixWebView.goBack();
        } else {
            finish();
        }
    }
}
