package com.sangalo.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.sangalo.quizzer.QuestionsActivity.FILE_NAME;
import static com.sangalo.quizzer.QuestionsActivity.KEY_NAME;

public class BookmarkActivity extends AppCompatActivity {

    private List<QuestionModel> bookmarksList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = findViewById(R.id.toolbar);

        loadAds();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_bookmarks);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        BookmarkAdapter adapter = new BookmarkAdapter(bookmarksList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookmarks(){
        String json =  preferences.getString(KEY_NAME,"");

        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json,type);
        if (bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }

    }

    private void storeBookmarks(){
        String json = gson.toJson(bookmarksList);

        editor.putString(KEY_NAME,json);
        editor.commit();
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}