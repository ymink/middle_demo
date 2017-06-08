package com.example.pethoalpar.zxingexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button button;
    ArrayList arrayList;
    String insert_barcode_url = "http://35.187.213.1/insert_barcode_info.php";
    String x;
    RequestQueue requestQueue;

    private TextView resultText;
    Button shop;
    Button plus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lotte_mart);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        button = (Button) this.findViewById(R.id.button);
        shop = (Button)this.findViewById(R.id.shop);
        arrayList = new ArrayList();

        final Activity activity = this;

        // 메인화면에서 바코드 스캔버튼을 누르면 바코드 스캔
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        // 장바구니 버튼을 누르면 장바구니 화면으로 넘어가도록
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LotteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {  // 바코드 스캐너 화면에 들어옴
            if(result.getContents() == null) {  // 취소버튼을 누른 경우
                Log.d("MainActivity", "Cancelled scan");  // 시스템에 로그 메세지 보내고
                Toast.makeText(this, "취소", Toast.LENGTH_LONG).show();  // 안드로이드 화면에 '취소'라는 토스트메세지 출력
            } else {  // 바코드 스캔에 성공한 경우
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                 x = result.getContents().toString();  // x에 바코드 스캔 값을 담는다

                // 바코드 스캔값을 서버에 전송
                StringRequest request = new StringRequest(Request.Method.POST, insert_barcode_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().equals("success")) {  // 바코드 값이 서버에 있다면 장바구니 화면으로 넘어감
                            Intent intent = new Intent(MainActivity.this, LotteActivity.class);
                            startActivity(intent);
                        } else if(s.trim().equals("failed")){  // 바코드 값이 서버에 없다면 토스트 메세지 출력
                            Toast.makeText(MainActivity.this, "등록되지 않은 물품입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameter = new HashMap<String, String>();
                        parameter.put("barcode",x);
                        return parameter;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());  // 발리 네트워크 사용
                requestQueue.add(request);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
