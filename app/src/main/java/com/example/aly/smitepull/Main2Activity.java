package com.example.aly.smitepull;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent info = getIntent();
        String godName = info.getExtras().getString("godNum");
        ArrayList<Guide> guides = extractGuides("http://www.smitefire.com/smite/gods/"+godName);

    }



    public static ArrayList<Guide> extractGuides( String url){
        ArrayList<Guide> guides = new ArrayList<Guide>();
        Document doc = webpage(url);
        Elements glinks = doc.select("a[href]");
        for (Element link : glinks){
            if (link.attr("href").contains("/smite/guide/")){
                String guideLink = link.attr("href");
                String name = guideLink.substring( 13,guideLink.lastIndexOf("-"));
                guides.add(new Guide(name, "http://www.smitefire.com" + guideLink));
            }
        }
        return guides;
    }

    public static Document webpage(String url){
        //returns a Document with the given url
        Document doc = null;
        try{
            doc = Jsoup.connect(url).userAgent("Mozilla").get();	//if userAgent isnt used to disguise, page won't let me access its contents
        }catch (IOException e){
            System.out.println(e);
        }
        return doc;
    }
}










