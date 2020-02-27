package com.example.smedicine;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smedicine.bean.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MedicineHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    //list中获取药物列表的任务
    class GetMedicineHistory extends AsyncTask<String, Integer, List<History>> {


        private List<History> getJsonData(String url) {
            List<History> histories = new ArrayList<>();
            JSONObject jsonObject;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                jsonObject = new JSONObject(responseData);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("items");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);

                            History history1 = new History();
                            history1.setName(jsonObject2.getString("name"));
                            history1.setTime(jsonObject2.getString("group"));
                            history1.setTimes(jsonObject2.getString("times"));
                            history1.setNum(jsonObject2.getString("num"));
                            histories.add(history1);

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return histories;
        }

        @SuppressLint("WrongThread")
        @Override
        protected List<History> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<History> histories) {
            super.onPostExecute(histories);
            HistoryAdapter HistoryAdapter = new HistoryAdapter(histories);
            recyclerView.setAdapter(HistoryAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_history);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_medicine_history);
        //加载GridLayoutManager布局管理器  第一个参数不知道是getActivity还是getContext，不知道有何区别
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        new GetMedicineHistory().execute("http://39.105.11.138:8881/saier/plan/m/list/history");
    }
}
