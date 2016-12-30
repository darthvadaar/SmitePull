package com.example.aly.smitepull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView text;
    Document doc;
    String listString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<God> gods = extractGods();
        final ArrayList<String> godNames = new ArrayList<String>();
        ArrayList<Drawable> godImages = new ArrayList<Drawable>();

        for (God s : gods) {
            //Drawable pic = LoadImageFromWebOperations("http://www.smitefire.com/images/god/card/"+s.getName()+".png");
            //layers.add(pic);
            if (s.getName()!=null&&s.getDrawable()!=null) {
                godNames.add(s.getName());
                godImages.add(s.getDrawable());
            }
        }
        CustomList adapter = new CustomList(this, godNames, godImages);
        //ListAdapter godAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,godNames);
        //ArrayList<Drawable> layers = new ArrayList<>();
        //LayerDrawable layerDrawable = new LayerDrawable((Drawable[]) layers.toArray());
        //((ImageView) findViewById(R.id.imageView)).setImageDrawable(layerDrawable);
        ((ListView)findViewById(R.id.ListView)).setAdapter(adapter);
        text = (TextView)findViewById(R.id.Text);
        text.setText(listString);
        ((ListView)findViewById(R.id.ListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +godNames.get(position), Toast.LENGTH_SHORT).show();
                Intent godInfo = new Intent(MainActivity.this,Main2Activity.class);
                godInfo.putExtra("godNum",godNames.get(position));
                startActivity(godInfo);
            }
        });
    }




















    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
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




    public static ArrayList<God> extractGods(){
        //looks for all the url additions that lead to god pages
        //and returns them as an ArrayList<God>
        ArrayList<String> names= new ArrayList<String>();
        ArrayList<God> gods = new ArrayList<God>();
        Document doc = webpage("http://www.smitefire.com/smite/gods");
        Log.d("myTag", doc.toString());
        Elements links = doc.select("a[href]");
        for (Element link: links){
            if (link.attr("href").contains("/smite/god/")){
                String godLink = link.attr("href");
                String name = godLink.substring(11,godLink.lastIndexOf("-"));
                if (!names.contains(name)) {
                    gods.add(
                            new God(name, "http://www.smitefire.com" + godLink, LoadImageFromWebOperations("http://www.smitefire.com/images/god/icon/" + name + ".png"))
                    );
                    names.add(name);
                }
            }
        }
        return gods;
    }

}












////////////////////////////////Gods Classss////////////////////////////////////////////////

class God{
    public String url, name;
    public ArrayList<String> guides;
    public Drawable picture;

    public God(String name, String url,Drawable picture){
        this.guides = new ArrayList<String>();
        this.url = url;
        this.name = name;
        this.picture = picture;
    }

    //getters and setters
    public Drawable getDrawable(){return picture;}
    public String getName(){return name;}
    public String getURL(){return url;}
    public ArrayList<String> getGuides(){return guides;}



}
//////////////////////// Guide Class ////////////////////////////////////////////////////////
class Guide{
    public String name, url;


    public Guide(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName(){return name;}
    public String getURL(){return url;}
    //public ArrayList<String> getGuides(){return guides;}

}


////////////////////CustomList View Adapter
class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final ArrayList<String> web;
    private final ArrayList<Drawable> image;
    public CustomList(Activity context,ArrayList<String> web, ArrayList<Drawable> image) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.image = image;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web.get(position));

        imageView.setImageDrawable(image.get(position));
        return rowView;
    }
}