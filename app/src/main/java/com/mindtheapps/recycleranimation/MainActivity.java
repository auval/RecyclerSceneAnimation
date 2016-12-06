package com.mindtheapps.recycleranimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author amir
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView list;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (RecyclerView) findViewById(R.id.list);
        myAdapter = new MyAdapter();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(myAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mi_add) {
            myAdapter.addRow();
            return true;
        }
        return false;
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        static ArrayList<MyRowData> data = new ArrayList<>();

        MyAdapter() {
            setHasStableIds(true);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final MyRowView myRowView = new MyRowView(parent);

            return new MyViewHolder(myRowView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.myRow.setColor(data.get(position).getColor());
            holder.myRow.setBig(data.get(position).isBig());

            holder.myRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (data.get(position).isBig()) {
                        // delete row
                        data.remove(position);

                    } else {
                        // enlarge row
                        data.get(position).setBig(true);
                    }
                    notifyDataSetChanged();

                }
            });
        }


        @Override
        public long getItemId(int position) {
            return data.get(position).hashCode();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public void addRow() {
            Random random = new Random();
            int c = random.nextInt();
            data.add(0, new MyRowData(c | 0xff000000));// the bitwise "or" clears the transparency

            // set first row to large and the rest to small
            data.get(0).setBig(true);
            for (int i = 1; i < data.size(); i++) {
                data.get(i).setBig(false);
            }

            notifyDataSetChanged();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private MyRowView myRow;

        public MyViewHolder(MyRowView itemView) {
            super(itemView);
            myRow = itemView;
        }
    }
}
