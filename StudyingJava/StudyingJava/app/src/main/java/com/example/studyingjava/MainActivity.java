package com.example.studyingjava;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    final static String USER_DATA = "mPrefs";
    final static String USER_NAME = "name";
    final static String USER_LAST_NAME = "lastName";
    final static String USER_LOGIN = "login";
    SharedPreferences mPrefs;
    ConstraintLayout mainLayout;
    CheckBox checkBox;
    TextView txt;
    Button btn1, btn2, dbBtnAdd, dbBtnRead, dbBtnClear;
    EditText etName, etEmail;
    DBHelper dbhelper;
    SeekBar seekbar;
    ImageView myImage;
    LinearLayout.LayoutParams leftBtnParams, rightBtnParams;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        activity = this;
        setMainLinearLayout();
        setUserGreeting();

    }

    private void setUserGreeting() {
        String name = mPrefs.getString(USER_NAME, "");
        String lastName = mPrefs.getString(USER_LAST_NAME, "");
        String login = mPrefs.getString(USER_LOGIN, "");
        txt.setText("Hello, " + name + " " + lastName + "\n" + "Your login: " + login);
    }

    private void setNewLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button button = new Button(this);
        button.setText("new layout");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMainLinearLayout();
            }
        });
        linearLayout.addView(button);
        setContentView(linearLayout);
    }

    private void setMainLinearLayout() {
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.main_layout);
        checkBox = findViewById(R.id.checkbox);
        txt = findViewById(R.id.txt);
        btn1 = findViewById(R.id.left_btn);
        btn2 = findViewById(R.id.right_btn);
        seekbar = findViewById(R.id.seekbar);
        myImage = findViewById(R.id.my_image);
        leftBtnParams = (LinearLayout.LayoutParams) btn1.getLayoutParams();
        rightBtnParams = (LinearLayout.LayoutParams) btn2.getLayoutParams();
        checkBoxClickHandle(checkBox);
        buttonsClickHandle(btn1);
        buttonsClickHandle(btn2);
        seekbarHandle(seekbar);
        registerForContextMenu(checkBox);
        registerForContextMenu(txt);
        registerForContextMenu(myImage);
        registerForContextMenu(btn1);
        registerForContextMenu(btn2);
    }

    private void seekbarHandle(final SeekBar seekbar) {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftBtnParams.weight = 100 - seekBar.getProgress();
                rightBtnParams.weight = seekBar.getProgress();
                btn1.setLayoutParams(leftBtnParams);
                btn2.setLayoutParams(rightBtnParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void buttonsClickHandle(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getId() == R.id.left_btn) {
                    btn1.setBackgroundResource(R.drawable.btn_first_design);
                    btn2.setBackgroundResource(R.drawable.btn_first_design);
                }
                if (button.getId() == R.id.right_btn) {
                    btn1.setBackgroundResource(R.drawable.btn_second_design);
                    btn2.setBackgroundResource(R.drawable.btn_second_design);
                }
            }
        });
    }

    private void checkBoxClickHandle(final CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "changed to active", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "changed to inactive", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.txt:
                menu.add(0, 1, 0, "Hello");
                menu.add(0, 2, 0, "Konichiwa");
                menu.add(0, 3, 0, "ыыыыыыы!");
                menu.add(0, 4, 0, "Шалом");
                menu.add(0, 5, 0, "Здарова");
                break;
            case R.id.checkbox:
                menu.add(1, 6, 0, "Set first text");
                menu.add(1, 7, 0, "Set second text");
                menu.add(1, 8, 0, "Set third text");
                break;
            case R.id.left_btn:
                menu.add(0, 9, 0, "Set first style");
                break;
            case R.id.right_btn:
                menu.add(0, 10, 0, "Set second style");
                break;
            case R.id.my_image:
                menu.add(2, 1, 0, "Прозрачность");
                menu.add(2, 2, 0, "Асинхронный двигатель");
                menu.add(2, 6, 0, "Увеличение");
                menu.add(2, 4, 0, "Перемещение");
                menu.add(2, 5, 0, "Комбо");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 3) {
            setNewLinearLayout();
        }
        switch (item.getGroupId()) {
            case 0:
                txt.setText(item.getTitle());
                break;
            case 1:
                checkBox.setText(item.getTitle());
                mainLayout.setBackgroundResource(R.color.white);
                break;
            case 2:
                Animation animation = null;
                switch (item.getItemId()) {
                    case 1:
                        animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
                        break;
                    case 2:
                        animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
                        break;
                    case 6:
                        txt.setText("fuuuuuuuck!!!!");
                        animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
                        break;
                    case 4:
                        animation = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
                        break;
                    case 5:
                        animation = AnimationUtils.loadAnimation(this, R.anim.anim_combo);
                        break;
                }
                myImage.startAnimation(animation);
                break;
        }
        switch (item.getItemId()) {
            case 9:
                mainLayout.setBackgroundResource(R.drawable.btn_first_design);
                break;
            case 10:
                mainLayout.setBackgroundResource(R.drawable.btn_second_design);
                break;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 6, "Go to Browser");
        menu.add(0, 6, 5, "Change data");
        menu.add(0, 3, 4, "Go to DB");
        menu.add(1, 4, 3, "menu4");
        menu.add(1, 5, 2, "menu1");
        menu.add(1, 2, 1, "menu2");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(1, checkBox.isChecked());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com")));
                break;
            case 6:
                startActivityForResult(new Intent(activity, ChildActivity.class), 1);
                break;
            case 3:
                Log.d(LOG_TAG, "going to DB window");
                makeDataBaseTestLayout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeDataBaseTestLayout() {
        setContentView(R.layout.database_fill);
        dbBtnAdd = findViewById(R.id.btnAdd);
        dbBtnAdd.setOnClickListener(this);
        dbBtnRead = findViewById(R.id.btnRead);
        dbBtnRead.setOnClickListener(this);
        dbBtnClear = findViewById(R.id.btnClear);
        dbBtnClear.setOnClickListener(this);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        dbhelper = new DBHelper(this);
        Log.d(LOG_TAG, "db window is opened");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        String name = data.getStringExtra("name");
        String lastName = data.getStringExtra("lastName");
        String login = data.getStringExtra("login");
        mPrefs
                .edit()
                .putString(USER_NAME, name)
                .putString(USER_LAST_NAME, lastName)
                .putString(USER_LOGIN, login)
                .apply();
        txt.setText("Hello, " + name + " " + lastName + "\n" + "Your login: " + login);
    }

    @Override
    public void onClick(View v) {
        final ContentValues cv = new ContentValues();
        final String name = etName.getText().toString();
        final String email = etEmail.getText().toString();

        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        switch (v.getId()){
            case R.id.btnAdd:
                cv.put("name", name);
                cv.put("email", email);
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.btnRead:
                Log.d(LOG_TAG, "Rows in table:");
                Cursor c = db.query("mytable", null, null, null, null, null, null);

                if (c.moveToNext()) {
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");

                    do {
                        Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) +
                                "  " + c.getString(nameColIndex) +
                                "  " + c.getString(emailColIndex));
                    } while (c.moveToNext());

                } else {
                    Log.d("log", "0 rows");
                    c.close();
                }
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "Clear database");
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "rows deleted: " + clearCount);
                break;
        }
        dbhelper.close();
    }

}
