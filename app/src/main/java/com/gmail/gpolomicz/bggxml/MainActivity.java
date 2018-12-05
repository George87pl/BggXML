package com.gmail.gpolomicz.bggxml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadXML.OnDataAvailable {
    private static final String TAG = "GPDEB";

    private ListView bg_search_list;
    private List<BGSearchEntry> searchList;
    ParseString parseString;
    BGSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg_search_list = findViewById(R.id.bg_search_list);

        parseString = new ParseString();

        DownloadXML xml = new DownloadXML(this, "https://www.boardgamegeek.com/xmlapi2/search?type=boardgame&query=");
        xml.execute("Catan");

    }


    @Override
    public void onDataAvailable(String result) {

        parseString.parseXML(result);
        searchList = parseString.getInformationArrayList();

        Log.d(TAG, "onDataAvailable: " + searchList.size());

        for (int i = 0; i < searchList.size(); i++) {
            searchList.get(i).setLink("https://boardgamegeek.com/boardgame/"+String.valueOf(searchList.get(i).getId()));
            DownloadXML xml2 = new DownloadXML(this, "https://boardgamegeek.com//xmlapi2/thing?id=", i);
            xml2.execute(String.valueOf(searchList.get(i).getId()));

            adapter = new BGSearchAdapter(MainActivity.this, R.layout.list_record, searchList);
            bg_search_list.setAdapter(adapter);
        }
    }

    @Override
    public void onDataAvailable(String result, Integer id) {

        parseString.parseXML(result, id);
        searchList = parseString.getInformationArrayList();

        Log.d(TAG, searchList.get(id).toString());

        adapter.notifyDataSetChanged();

    }
}
