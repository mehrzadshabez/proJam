package com.example.mehrzadshabez.appjam001;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.Element;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehrzadshabez.appjam001.R;

public class MainActivity extends Activity {

    String url = "http://www.csun.edu/catalog/planning/plans/2010/computer-engineering-4/";
    ProgressDialog mProgressDialog;

    List<String> example = new ArrayList<>();//= new List<String>() ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        example.add("example");
        example.add("second");
        Button titlebutton = (Button) findViewById(R.id.titlebutton);
        Button descbutton = (Button) findViewById(R.id.descbutton);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);

        // Create an ArrayAdapter containing country names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, example);

        // Set the adapter for the AutoCompleteTextView
        textView.setAdapter(adapter);




        // Capture button click
        titlebutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Execute Title AsyncTask
                new Title().execute();
            }
        });

        // Capture button click
        descbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Execute Description AsyncTask
                new Description().execute();
            }
        });

    }

    // Title AsyncTask
    private class Title extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("getting the title");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Get the html document title
                title = document.title();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            TextView txttitle = (TextView) findViewById(R.id.titletxt);
            txttitle.setText(title);
            mProgressDialog.dismiss();
        }
    }

    // Description AsyncTask
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;
        ArrayList<String> downServers = new ArrayList<>();

            @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Getting courses");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();

                org.jsoup.nodes.Element table = document.select("table").get(1);
                Elements rows = table.select("p");
                desc = rows.get(2).text();
                for(int j=0; j<= 7 ; j++) {
                    table = document.select("table").get(j);
                    rows = table.select("p");
                    for (int i = 2; i < rows.size()-1; i++) {
                        if (!rows.get(i).text().matches(("-?\\d+")) && !rows.get(i).text().matches(("Total"))) {
                            Log.v("Courses", rows.get(i).text());

                             downServers.add(rows.get(i).text());

                        }

                    }
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            TextView txtdesc = (TextView) findViewById(R.id.desctxt);
            txtdesc.setText(desc);
            for(int i=0;i<downServers.size();i++)
            example.add(downServers.get(i));

            mProgressDialog.dismiss();
        }
    }


}