package ders.yasin.jsonparser;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ListView listCompanies;
    ImageView ivLogos;
    ProgressDialog progressDialog;
    private final static String DATA_URL="http://web.karabuk.edu.tr/yasinortakci/dokumanlar/web_dokumanlari/recyle.json";
    //private final static String DATA_URL="http://furkanbalta.com/web_dokumanlari/recyleTwo.json";

    String [] imageURLs;
    String [] listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listCompanies=(ListView) findViewById(R.id.lv_Compainies);
        ivLogos=(ImageView) findViewById(R.id.iv_Logos);

        listCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Picasso.with(getApplicationContext()).load(imageURLs[position]).into(ivLogos);
            }
        });


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("Companies");
                            int length=jsonArray.length();
                            imageURLs=new String[length];
                            listData=new String[length];
                            for(int i=0;i<length;i++){
                                JSONObject o=jsonArray.getJSONObject(i);
                                String heading=o.getString("Heading");
                                String detail=o.getString("Detail");
                                imageURLs[i]=o.getString("ImageURL");
                                listData[i]=heading+" : "+detail;
                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.list_layout,listData);
                            listCompanies.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error",error.getMessage());
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
