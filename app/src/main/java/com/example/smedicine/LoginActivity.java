package com.example.smedicine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    List<String> genderList;
    List<String> bloodList;
    private static String babyDOB;
    private EditText username;
    private static EditText userBirthday;
    //体重
    private EditText userWeight;
    //身高
    private EditText userHeight;
    //腰臀比
    private EditText userYaoTun;
    //家庭医生姓名
    private EditText userDoctor;
    //家庭医生电话
    private EditText userDcotorPhone;
    //血型
    private Spinner spinnerBlood;
    private Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterBlood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        username = (EditText)findViewById(R.id.userNameEditText);
        userBirthday = (EditText)findViewById(R.id.dateOfBirthEditText);
        userWeight = (EditText)findViewById(R.id.birthWeightSpinner);
        userHeight = (EditText)findViewById(R.id.birthHeightSpinner);
        userYaoTun = (EditText)findViewById(R.id.birthHeadSpinner);
        userDoctor = (EditText)findViewById(R.id.doctorNameEditText);
        userDcotorPhone = (EditText)findViewById(R.id.doctorContactEditText);


        //用于选择性别的性别选择器
        spinner = (Spinner)findViewById(R.id.genderSpinner);
        genderList = new ArrayList<>();
        genderList.add("女");
        genderList.add("男");
        genderList.add("请选择性别");
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.login_spinner_item, genderList);
        adapter.setDropDownViewResource(R.layout.login_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount() - 1);

        //用于选择血型的选择器
        spinnerBlood = (Spinner)findViewById(R.id.bloodGroupSpinner);
        bloodList = new ArrayList<>();
        bloodList.add("A");
        bloodList.add("B");
        bloodList.add("AB");
        bloodList.add("O");
        bloodList.add("请选择血型");
        adapterBlood = new ArrayAdapter<String>(getApplicationContext(), R.layout.login_spinner_item, bloodList);
        adapterBlood.setDropDownViewResource(R.layout.login_spinner_dropdown_item);
        spinnerBlood.setAdapter(adapterBlood);
        spinnerBlood.setSelection(adapterBlood.getCount() - 1);



        //用于开始的button
        Button button = (Button)findViewById(R.id.enterBirthDataBtn);
        //时间选择的EditText的点击事件
        userBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment selectDateDialog = new SelectDateFragment();
                selectDateDialog.show(getFragmentManager(), "选择日期");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String gender = spinner.getSelectedItem().toString();
                String birthday = userBirthday.getText().toString();
                if (name.trim().length() > 0) {
                    if (username.getText().length() > 0) {
                        if (!spinner.equals("请选择性别")) {
//                            enterProgressDialog = ProgressDialog.show(Login_main.this, "please wait", "sddssds", true);
                            Toast.makeText(LoginActivity.this,"可以传输数据了",Toast.LENGTH_LONG).show();
                            /**
                             *
                             *
                             * 这边数据传输，之后进行活动跳转
                             *
                             *
                             *
                             * */
                            MyDatabaseHelper dbHelper = new MyDatabaseHelper(LoginActivity.this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            MyDatabaseHelper.updateUsername(db,username.getText().toString());
                            MyDatabaseHelper.loginLogin(db);
                            Intent intent1 = new Intent(LoginActivity.this,IndexActivity.class);
                            startActivity(intent1);



                        } else {
                            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Date Of Birth", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Provide Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //时间选择器的子类，用于选择时间
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private static String date;
        private int yy, mm, dd;
        public String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final java.util.Calendar calendar = java.util.Calendar.getInstance();
            yy = calendar.get(java.util.Calendar.YEAR);
            mm = calendar.get(java.util.Calendar.MONTH);
            dd = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, yy, mm, dd) {
                @Override
                public void onDateChanged(DatePicker view, int year, int month, int day) {
                    super.onDateChanged(view, year, month, day);
                    if (year > yy) {
                        view.updateDate(yy, mm, dd);

                    }
                    if (month > mm && year == yy) {
                        view.updateDate(yy, mm, dd);
                    }
                    if (day > dd && month == mm && year == yy) {
                        view.updateDate(yy, mm, dd);
                    }
                }

                @Override
                public void onClick(DialogInterface dialog, int doneBtn) {
                    if (doneBtn == BUTTON_POSITIVE) {

                        int year = getDatePicker().getYear();
                        int month = getDatePicker().getMonth() + 1;
                        int day = getDatePicker().getDayOfMonth();

                        SelectDateFragment.date = day + "/" + month + "/" + year;
                        babyDOB = day + "-" + month + "-" + year;
                        System.out.println("babyDOB----------" + babyDOB);
                        userBirthday.setText(year + "-" + month + "-" + day);


                    }
                    super.onClick(dialog, doneBtn);
                }
            };


        }


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }

    }
}
