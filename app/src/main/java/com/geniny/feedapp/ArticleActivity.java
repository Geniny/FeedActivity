package com.geniny.feedapp;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Objects;

public class ArticleActivity extends BaseActivity {

    private String link;
    private SwipeRefreshLayout refreshLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        link = Objects.requireNonNull(getIntent().getExtras()).getString("Link");

        webView = findViewById(R.id.main_web_view);
        refreshLayout = findViewById(R.id.web_refresh_view);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(link);
            }
        });

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                webView.loadUrl(link);
            }
        });
    }
}
