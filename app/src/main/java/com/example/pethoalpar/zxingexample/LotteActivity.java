package com.example.pethoalpar.zxingexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class LotteActivity extends Activity {

    private Button home;
    private Button search;
    private ListView listView;
    private String get_barcode_url = "http://35.187.213.1/get_barcode_info.php";
    private LotteActivity thisActivity = LotteActivity.this;
    RequestQueue requestQueue;

    ListAdapter listAdapter;


    ArrayList<ItemList> itemList = new ArrayList<ItemList>();
    TextView textView;
    MainActivity ma;
    Button cart;

    // 스캔한 값을 리스트 형태로 보여줌
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);
        home = (Button)findViewById(R.id.home);
        ma = new MainActivity();
        listAdapter = new ListAdapter(getLayoutInflater(),itemList);
        listView = (ListView)findViewById(R.id.list_item);
        listView.setAdapter(listAdapter);

        itemList = new ArrayList<ItemList>();
    }

    @Override
    protected void onResume() {  // 안드로이드 생성주기 중 onResume에서 겟 바코드 인포라는 함수를 부른다
        super.onResume();
        get_barcode_info(getLayoutInflater());
    }

    private void get_barcode_info(final LayoutInflater inflater){  // 매개변수로 레이아웃 인플레이터를 받고

        StringRequest request = new StringRequest(Request.Method.POST, get_barcode_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {  // 결과값들을 받아서 JSON형태로 바꿔주고 arrayList에 추가하여 리스트뷰를 갱신
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String name = jo.getString("name");
                        String price = jo.getString("price");
                        int price_number =
                                Integer.parseInt(price);
                        itemList.add(new ItemList(name,price_number));
                        listAdapter = new ListAdapter(inflater,itemList);
                        listView.setAdapter(listAdapter);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameter = new HashMap<String, String>();
                parameter.put("checked","1");  // 바코드에 체크됬다면 서버에 1이라는 값 전송
                return parameter;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}
