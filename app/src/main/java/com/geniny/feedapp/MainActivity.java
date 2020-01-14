package com.geniny.feedapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView articles;
    TextView title;
    AutoCompleteTextView search;
    SwipeRefreshLayout refresher;
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articles = findViewById(R.id.list_view);
        title = findViewById(R.id.title);
        search = findViewById(R.id.search);
        root = findViewById(R.id.root);
        refresher = findViewById(R.id.swipe_refresh);

        articles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = (Article) articles.getAdapter().getItem(position);
                intent.putExtra("Link", article.getLink());
                startActivity(intent);
                return true;
            }
        });

        refresher.setOnRefreshListener(this);
        refresher.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    showTitle();
                    return true;
                }
                return false;
            }
        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(hasFocus)
                {
                    search.selectAll();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                else
                {
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                }
            }
        });

        articles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                title.setVisibility(View.VISIBLE);
                search.clearFocus();
                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(articles.getWindowToken(), 0);
            }
        });

        title.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                search.requestFocus();
            }
        });

        if(currentAdress.get() != "")
        {
            search.setText(currentAdress.get());
            load();
        }
    }

    void setArticlesContext(Articles articles)
    {
        App.setArticles(articles);
        title.setText(articles.getTitle());
        currentAdress.set(search.getText().toString());
        this.articles.setAdapter(new Adapter(
                this, R.layout.article_layout, articles.getArticles()));
        refresher.setRefreshing(false);
    }


    void load()
    {
        if(isConnected()) {

            new CollectAsync(this, search.getText().toString()).execute();
        }
        else if(App.getArticles() == null)
        {
            new SetAsync(this).execute();
            cashToast.show();
        }
        else
            setArticlesContext(App.getArticles());
        refresher.setRefreshing(false);
    }


    void showTitle()
    {
        title.setVisibility(View.VISIBLE);
        search.clearFocus();
        if(!currentAdress.get().equals(search.getText().toString()))
        {
            new CollectAsync(this, search.getText().toString()).execute();
        }
    }

    @Override
    public void onRefresh() {
        refresher.setRefreshing(true);
        load();
    }
}
