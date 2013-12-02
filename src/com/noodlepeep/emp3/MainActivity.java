package com.noodlepeep.emp3;


import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	EditText query = (EditText)findViewById(R.id.query);
	ImageButton search ;
	ImageButton settings;
	TextView title;
	ArrayList<String> songLink = new ArrayList<String>();
	ArrayList<String> songDetails = new ArrayList<String>();
	ArrayList<String> songName = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		search = (ImageButton)findViewById(R.id.search);
		settings = (ImageButton)findViewById(R.id.settings);
        title = (TextView)findViewById(R.id.title);
			search.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
				String searchQuery = query.getText().toString();
				searchQuery = searchQuery.replace(" ","_");
				String url = "http://mp3skull.com/mp3/"+searchQuery+".html";
				if(isNetworkAvailable())
					queryParser(url);
				else
					noNetwork();
				}				
			});
		}

	private void queryParser(String url) {
	//	title.setText("Showing results for : "+query.getText());		
		int i = 0,j = 0;
		try{
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		Elements divs1 = doc.select("div.left");
		Elements divs2 = doc.select("div[id=right_song]");
		for (Element link : links) {
			if(link.text().equals("Download")){
			String parseUrl = link.attr("href"); 
			parseUrl = parseUrl.replace(" ", "%20");
			songLink.add(i++,parseUrl);
		}
		}
		for(Element div2 : divs2){
			 String name = div2.text();
			 name = name.substring(0,name.indexOf(" mp3"))+".mp3";
			 songName.add(j++,name);
	    }
		for(Element div1 : divs1){	
			songDetails.add(div1.text());			
		}
		}
		catch (IOException e) {
			e.printStackTrace();
		
	}
	    catch (NullPointerException npe) {
            npe.printStackTrace();
    }


}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	private void noNetwork(){
		Toast.makeText(getApplicationContext(), "No Network Connection Detected. Enable network connection and try again.", Toast.LENGTH_LONG).show();
	}
}


