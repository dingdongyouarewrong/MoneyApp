package com.example.donny.moneyapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.donny.moneyapp.DBHelper.KEY_CATEGORY;
import static com.example.donny.moneyapp.DBHelper.KEY_DATE;
import static com.example.donny.moneyapp.DBHelper.KEY_DAY;
import static com.example.donny.moneyapp.DBHelper.KEY_MONTH;
import static com.example.donny.moneyapp.DBHelper.KEY_NAME;
import static com.example.donny.moneyapp.DBHelper.KEY_SUM;
import static com.example.donny.moneyapp.DBHelper.KEY_WEEK;
import static com.example.donny.moneyapp.Main_Activity.card;
import static java.lang.Long.valueOf;

public class inShop extends AppCompatActivity {

    public float sums = 0;
    Context context;
    SharedPreferences prefs = null;
    DBHelper dbHelper;
    long currentWeek, currentMonth, currentDay, currentDate;
    ItemsAdapter adapter;
    SQLiteDatabase database;
    TextView sum;

    class Item {
        String name;
        double price;



        Item (String name, Double price) {
            this.name = name;
            this.price = price;
        }
    }



    @Override

    public void onCreate(@Nullable final Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.shop);
        //getSupportActionBar().hide();

        dbHelper = new DBHelper(this);
        prefs = getSharedPreferences("com.donny.MoneyApp", MODE_PRIVATE);
        prefs.contains("com.donny.MoneyApp");
        checktime();

        context = inShop.this;
        final EditText name = findViewById(R.id.name);
        final EditText price = findViewById(R.id.price);
        final Button add = findViewById(R.id.add);
        final ListView items = findViewById(R.id.items);
        sum = findViewById(R.id.sum);
        adapter = new ItemsAdapter();
        final Button check = findViewById(R.id.checkButton);
//        final Button clear = findViewById(R.id.clearButton);
        items.setAdapter(adapter);


        database = dbHelper.getWritableDatabase();


////////////////////////////okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk//
        items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //////
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDeleteDialog(position);
                return false;
            }
        });



        ////////кнопка чек//////////////сохранение массива в базе данных
        check.setOnClickListener(new View.OnClickListener() { //кнопка чек
            @Override
            public void onClick(View v) {

                if (adapter.getCount() == 0) {
                    Toast.makeText(inShop.this, "добавьте товар", Toast.LENGTH_SHORT).show();
                } else {
                    showCardCashDialog();
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {////////кнопка добавить
            @Override
            public void onClick(View v) {
                if (!((price.getText().toString().length() == 0) || (name.getText().toString().length() == 0))) {
                    cont();
                }
                else {
                    Toast.makeText(inShop.this,"введите корректное значения",Toast.LENGTH_SHORT ).show();
                }
            }
            void cont () {


                Item d = new Item(name.getText().toString(), Double.valueOf(price.getText().toString()));



                adapter.add(d);

                sums += Double.parseDouble(price.getText().toString());
                int cypher = (int)sums % 100; //всё ниже - вывод верного окончания слова

                String currency = daysstat.currencyWord(settings.value, cypher, sums);
                if ((cypher > 10)&& (cypher<20)){
                    sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" "+currency));
                }
                else {
                    cypher = (int)sums % 10;
                    if (cypher == 1) {
//                        switch (settings.value)
                        sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" "+currency));
                    } else if ((cypher > 1) && (cypher < 5)) {
                        sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" "+currency));
                    } else if ((cypher == 0) || (cypher >= 5)) {
                        sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" "+currency));
                    }

                }

            }
        });

    }


    public void showCardCashDialog() {  ///выводит диалог с вопросом о средстве оплаты

        AlertDialog.Builder builder = new AlertDialog.Builder(inShop.this);
        builder.setMessage("чем вы расплатились?")
                .setCancelable(true)
                .setPositiveButton("картой", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        card-=sums;
                        prefs.edit().putFloat("savedCard", Main_Activity.card).apply();
                        dbPut();
                        Intent intent = new Intent(inShop.this,Main_Activity.class);
                        startActivity(intent);
                        finish();


                    }
                })
                .setNegativeButton("наличными", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Main_Activity.cash-=sums;
                        prefs.edit().putFloat("savedCash", Main_Activity.cash).apply();
                        dbPut();
                        Intent intent = new Intent(inShop.this,Main_Activity.class);
                        startActivity(intent);
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void checktime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("w");
        currentWeek = valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("M");
        currentMonth = valueOf(timeFormat.format(calendar.getTime()));
        timeFormat = new SimpleDateFormat("D");
        currentDate = valueOf(timeFormat.format(calendar.getTime()));
        currentDay = valueOf(calendar.get(Calendar.DAY_OF_WEEK))-1;

    }


    private class ItemsAdapter extends ArrayAdapter<Item> {

        public ItemsAdapter() {
            super(inShop.this, R.layout.item);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.item,null);
            final Item item = getItem(position);
            ((TextView) view.findViewById(R.id.name)).setText(String.valueOf(item.name));
            ((TextView) view.findViewById(R.id.price)).setText(String.valueOf(item.price));
            return view;
        }
    }

    private void dbPut() {
        int i = 0;
        ContentValues contentValues = new ContentValues();

        database.beginTransaction();
        try {
            while (i < adapter.getCount()) {
                Item itemtobase = adapter.getItem(i);
                contentValues.put(KEY_NAME, String.valueOf(itemtobase.name));
                contentValues.put(KEY_SUM, String.valueOf(itemtobase.price));
                contentValues.put(KEY_CATEGORY, "покупки в магазине");
                contentValues.put(KEY_WEEK, currentWeek);
                contentValues.put(KEY_MONTH, currentMonth);
                contentValues.put(KEY_DAY, currentDay);
                contentValues.put(KEY_DATE, currentDate);
                database.insert(DBHelper.TABLE_NAME, null, contentValues);
                itemtobase = null;
                i++;
            }
            database.setTransactionSuccessful();
        }
        catch (Exception e) {
            Toast.makeText(inShop.this,"ошибка",Toast.LENGTH_SHORT ).show();
        }
        finally {
            database.endTransaction();
        }
    }

    public void showDeleteDialog(final int position) {  ///выводит диалог с вопросом о средстве оплаты
        final AlertDialog.Builder builder = new AlertDialog.Builder(inShop.this);
        builder.setMessage("Удалить товар?")
                .setCancelable(true)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Item itemtobase = adapter.getItem(position);
                        adapter.remove(adapter.getItem(position));
                        if (itemtobase != null) {
                            sums -= itemtobase.price;
                        }
                        int cypher = (int)sums % 10;

                        if (cypher == 1) {
                            sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" рубль"));
                        } else if ((cypher > 1) && (cypher < 5)) {
                            sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" рубля"));
                        } else if ((cypher == 0) || (cypher >= 5)) {
                            sum.setText("Вы заплатите: ".concat(String.valueOf(sums)).concat(" рублей"));
                        }


                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}