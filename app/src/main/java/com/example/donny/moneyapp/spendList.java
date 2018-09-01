package com.example.donny.moneyapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.donny.moneyapp.DBHelper.KEY_CATEGORY;
import static com.example.donny.moneyapp.DBHelper.KEY_NAME;
import static com.example.donny.moneyapp.DBHelper.KEY_SUM;

public class spendList extends Activity {

    DBHelper dbHelper;



    class SpendingOne {
        String name;
        String category;
        double sum;

        SpendingOne(String name, String category, double sum) {
            this.name = name;
            this.category = category;
            this.sum = sum;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstantService) {
        super.onCreate(savedInstantService);
        setContentView(R.layout.spendlist);

        ListView items = findViewById(R.id.spendListView);
        ItemsAdapter adapter = new ItemsAdapter();
        items.setAdapter(adapter);


        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM spending;";
        Cursor itemCursor = database.rawQuery(query, null);
        itemCursor.moveToFirst();
        for (int i=0;i<itemCursor.getCount();i+=1) {
            int index = itemCursor.getColumnIndex(KEY_SUM);
            double itemSum = itemCursor.getDouble(index);
            index = itemCursor.getColumnIndex(KEY_NAME);
            String itemName = (String.valueOf(itemCursor.getString(index)));
            index = itemCursor.getColumnIndex(KEY_CATEGORY);
            String itemCategory = itemCursor.getString(index);
            SpendingOne d = new SpendingOne(itemName, itemCategory,itemSum);
            adapter.add(d);
        }

    }

    private class ItemsAdapter extends ArrayAdapter<SpendingOne> {

        public ItemsAdapter() {
            super(spendList.this, R.layout.spending_element);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.spending_element, null);

            SpendingOne spendingOne = getItem(position);
            ((TextView)view.findViewById(R.id.sp_name)).setText(spendingOne.name);
            ((TextView)view.findViewById(R.id.sp_sum)).setText(String.valueOf(spendingOne.sum));
            ((TextView)view.findViewById(R.id.sp_category)).setText(spendingOne.category);
            return view;
        }


    }

}
