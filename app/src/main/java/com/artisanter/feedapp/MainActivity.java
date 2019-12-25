package com.artisanter.feedapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ListView articlesView;
    TextView rssName;
    AutoCompleteTextView rssEdit;
    SwipeRefreshLayout refreshLayout;
    // AllRecords allRecords;
    History history;
    LinearLayout mainLayout;
    int lastHeight = 0;


//    @Override
//    protected void onSaveInstanceState(Bundle state)
//    {
//        super.onSaveInstanceState(state);
//        state.putSerializable("key", allRecords);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle state)
//    {
////        new Load(this).execute();
//        if (state != null)
//        {
//            allRecords = (AllRecords)state.getSerializable("key");
//        }
//        super.onRestoreInstanceState(state);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        articlesView = findViewById(R.id.list_view);

        articlesView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                Record record = (Record) articlesView.getAdapter().getItem(position);
                intent.putExtra("Link", record.getLink());
                startActivity(intent);
            }
        });
        refreshLayout = findViewById(R.id.swipe_refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadRecords();
            }
        });
        refreshLayout.setRefreshing(true);

        history = new History(this);
        rssName = findViewById(R.id.rss_name);
        rssEdit = findViewById(R.id.rss_edit);
        rssEdit.setText(RSSAddres.get());
        rssEdit.setAdapter(new ArrayAdapter<>(this
                , R.layout.support_simple_spinner_dropdown_item
                , new ArrayList<>(history.get())));
        rssName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                rssEdit.requestFocus();
            }
        });
        rssEdit.setOnEditorActionListener(new TextView.OnEditorActionListener()
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
        rssEdit.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm == null)
                    return;
                if(hasFocus)
                {
                    rssEdit.selectAll();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                else
                    imm.hideSoftInputFromWindow(articlesView.getWindowToken(), 0);
            }
        });

        mainLayout = findViewById(R.id.root);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        int newHeight = mainLayout.getHeight();
                        if(newHeight == lastHeight)
                            return;
                        if(newHeight > lastHeight)
                            showTitle();
                        lastHeight = newHeight;
                    }
                }
        );
        if (!App.getApplicationState())
        {
            loadRecords();
            App.setApplicationState(true);
        }
    }

    @Override
    void onGetRecords(AllRecords allRecords) // Получение записей
    {
        App.setApplicationAllRecorders(allRecords);
        //this.allRecords = allRecords;
        rssName.setText(allRecords.getTitle());
        RSSAddres.set(rssEdit.getText().toString());
        history.update(rssEdit.getText().toString());
        rssEdit.setAdapter(new ArrayAdapter<>(this
                , R.layout.support_simple_spinner_dropdown_item
                , new ArrayList<>(history.get())));
        //int width = articlesView.getWidth();
        int width = screenSize();
        articlesView.setAdapter(new Adapter(
                this, R.layout.article_layout, allRecords.getRecords(), width));
        refreshLayout.setRefreshing(false);
    }

    @Override
    void onError(Exception e) // Реакция на исключения
    {
        refreshLayout.setRefreshing(false);
        super.onError(e);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        rssEdit.clearFocus();
        if (App.getApplicationAllRecorders()!= null && App.getApplicationState()) {
            onGetRecords(App.getApplicationAllRecorders());
        }
    }

    // вспомогательный метод, в зависимости от того есть ли сеть либо скачивает записи,
    // либо востанавливает кэшированные
    void loadRecords()
    {
        if(isOnline())
            new Download(this, rssEdit.getText().toString()).execute();
        else if(App.getApplicationAllRecorders() == null)
        {
            new Load(this).execute();
            offlineToast.show();
        }
    }

    // Вызывает загрузку записей в случае смены канала RSS
    void showTitle()
    {
        rssName.setVisibility(View.VISIBLE);
        rssEdit.clearFocus();
        if(!RSSAddres.get().equals(rssEdit.getText().toString()))
        {
            new Download(this, rssEdit.getText().toString()).execute();
            refreshLayout.setRefreshing(true);
        }
    }

    int screenSize()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            return width;
        }
        else
        {
            return width;
        }
    }
}
