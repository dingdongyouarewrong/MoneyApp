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

public class earnList extends Activity {

    DBHelper dbHelper;



    class EarningOne {
        String name;
        double sum;

        EarningOne(String name, double sum) {
            this.name = name;
            this.sum = sum;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstantService) {
        super.onCreate(savedInstantService);
        setContentView(R.layout.earnlist);

        ItemsAdapter adapter = new ItemsAdapter();
        ListView items = findViewById(R.id.earnListView);
        items.setAdapter(adapter);


        dbHelper = new DBHelper(this);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM earning;";
        Cursor itemCursor = database.rawQuery(query, null);
        itemCursor.moveToFirst();
        for (int i=0;i<itemCursor.getCount();i+=1) {
            int index = itemCursor.getColumnIndex(KEY_SUM);
            double itemSum = itemCursor.getDouble(index);
            index = itemCursor.getColumnIndex(KEY_NAME);
            String itemName = (String.valueOf(itemCursor.getString(index)));
            EarningOne d = new EarningOne(itemName,itemSum);
            adapter.add(d);
        }

    }

    private class ItemsAdapter extends ArrayAdapter<EarningOne> {

        public ItemsAdapter() {
            super(earnList.this, R.layout.earning_element);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.earning_element, null);
            EarningOne earningOne = getItem(position);
            ((TextView)view.findViewById(R.id.ea_name)).setText(earningOne.name);
            ((TextView)view.findViewById(R.id.ea_sum)).setText(String.valueOf(earningOne.sum));
            return view;
        }
    }

}
