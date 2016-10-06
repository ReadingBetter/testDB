package com.example.kmg878.testdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmg878.testdb.connect.GetConnect;
import com.example.kmg878.testdb.connect.PutConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private PutConnect task_insert;
    private int no=2345;
    String[] candyList;
    String[] scoreList;
    String[] bookNoList;
    public String php;

    // TextView 주소값 배열
    int[] candy = {R.id.candy1, R.id.candy2, R.id.candy3, R.id.candy4, R.id.candy5};
    int[] score = {R.id.score1, R.id.score2, R.id.score3, R.id.score4, R.id.score5};
    int[] bookNo = {R.id.book_no1, R.id.book_no2, R.id.book_no3, R.id.book_no4, R.id.book_no5};

    private int count = 5;
    // TextView 배열 선언
    TextView[] candyArray = new TextView[count];
    TextView[] scoreArray = new TextView[count];
    TextView[] bookNoArray = new TextView[count];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPrev = (Button) findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        task_insert = new PutConnect();
        String url = "http://220.67.115.227:8088/readingbetter/mypage/historyapp?no="+no;
        task_insert.execute(url);
        Toast.makeText(getApplicationContext(), "입력되었습니다 ", Toast.LENGTH_SHORT).show();

        // history.php를 Connect로 연결 mem_id만 선택
        php = "http://220.67.115.227:8088/readingbetter/mypage/historyapp";
        GetConnect task = new GetConnect(php);
        task.start();

        try {
            task.join();


            System.out.println("waiting... for result");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = task.getResult();
        System.out.println("here");
        result="{'result':["+result+"]}";
        System.out.println(result);

        // JSON 파싱
        try {
            JSONObject root = new JSONObject(result);

           JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();
            candyList = new String[ARRAY_LENGTH];
            scoreList = new String[ARRAY_LENGTH];
            bookNoList = new String[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < count; i++) {
                JSONObject jo = ja.getJSONObject(i);
                String Candy = jo.getString("point");
                String Score = jo.getString("score");
                String book = jo.getString("title");

                System.out.println(Candy + "," + Score + "," + book);

                candyList[i] = Candy;
                candyArray[i] = (TextView) findViewById(candy[i]);
                candyArray[i].setText(candyList[i]);

                scoreList[i] = Score;
                scoreArray[i] = (TextView) findViewById(score[i]);
                scoreArray[i].setText(scoreList[i]);

                bookNoList[i] = book;
                bookNoArray[i] = (TextView) findViewById(bookNo[i]);
                bookNoArray[i].setText(bookNoList[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
