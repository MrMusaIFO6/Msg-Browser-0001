package com.example.myprobrowser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    EditText searchBar;
    ImageButton btnHome, btnCopy, btnMenu;
    PopupWindow popupWindow;
    LinearLayout noInternetLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        searchBar = findViewById(R.id.search_bar);
        btnHome = findViewById(R.id.btn_home);
        btnCopy = findViewById(R.id.btn_copy);
        btnMenu = findViewById(R.id.btn_menu);
        noInternetLayout = findViewById(R.id.no_internet_layout);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        loadPage("https://www.google.com");

        btnHome.setOnClickListener(v -> loadPage("https://www.google.com"));

        btnCopy.setOnClickListener(v -> {
            String url = webView.getUrl();
            android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            cm.setPrimaryClip(android.content.ClipData.newPlainText("URL", url));
        });

        btnMenu.setOnClickListener(this::showPopup);
    }

    private void loadPage(String url) {
        if (NetworkUtil.isConnected(this)) {
            noInternetLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        } else {
            webView.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showPopup(View anchor) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_menu, null);
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TextView version = popupView.findViewById(R.id.txt_version);
        version.setText("Version 1.0");

        popupView.findViewById(R.id.btn_back).setOnClickListener(v -> webView.goBack());
        popupView.findViewById(R.id.btn_forward).setOnClickListener(v -> webView.goForward());
        popupView.findViewById(R.id.btn_refresh).setOnClickListener(v -> webView.reload());

        popupWindow.showAsDropDown(anchor);
    }
}
