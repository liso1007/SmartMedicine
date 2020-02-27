package com.example.smedicine;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HealthAnalyseWeekActivity extends AppCompatActivity {
    private LineChart lineChart;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    private List<String> mList = new ArrayList<>();
    List<Entry> entries = new ArrayList<>();

//    //list中获取药物列表的任务
//    class GetMedicineHistoryDay extends AsyncTask<String, Integer, List<Rate>> {
//
//
//        private List<Rate> getJsonData(String url) {
//            List<Rate> rates = new ArrayList<>();
//            JSONObject jsonObject;
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url(url).build();
//            try {
//                Response response = client.newCall(request).execute();
//                String responseData = response.body().string();
//                jsonObject = new JSONObject(responseData);
//                JSONArray jsonArray = jsonObject.getJSONArray("items");
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                    JSONArray jsonArray1 = jsonObject1.getJSONArray("items");
//                    for (int j = 0; j < jsonArray1.length(); j++) {
//                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//
//                        History history1 = new History();
//                        history1.setName(jsonObject2.getString("name"));
//                        history1.setTime(jsonObject2.getString("group"));
//                        history1.setTimes(jsonObject2.getString("times"));
//                        history1.setNum(jsonObject2.getString("num"));
//                        histories.add(history1);
//
//                    }
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return histories;
//        }
//
//        @SuppressLint("WrongThread")
//        @Override
//        protected List<Rate> doInBackground(String... strings) {
//            return getJsonData(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(List<Rate> histories) {
//            super.onPostExecute(histories);
//            HistoryAdapter HistoryAdapter = new HistoryAdapter(histories);
//            recyclerView.setAdapter(HistoryAdapter);
//        }
//    }

    private void initData(){
        List<String> lists = new ArrayList<>();
        /**
         * "data": {
         * "2019-04-01": 2,
         * "2019-04-19": 1,
         * "2019-03-18": 3,
         * "2019-5-12": 1
         * }
         * */
        lists.add("2019-03-18");
        lists.add("2019-04-01");
        lists.add("2019-04-19");
        lists.add("2019-05-12");
        this.mList = lists;
        List<Entry> entriess = new ArrayList<>();
        entriess.add(new Entry(0,(float) 2/7));
        entriess.add(new Entry(1,(float) 1/7));
        entriess.add(new Entry(2,(float) 3/7));
        entriess.add(new Entry(3,(float) 1/7));
        this.entries = entriess;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_analyse_week);

        //设置导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_index);
        setSupportActionBar(toolbar);
        LineChart mLineChart = (LineChart) findViewById(R.id.lineChart);
        //显示边界
        mLineChart.setDrawBorders(true);
        //设置数据
        initData();

        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "用药状况");
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setLabelCount(4, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mList.get((int) value); //mList为存有月份的集合
            }
        });



    }
}
