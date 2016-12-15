package com.mindtheapps.recycleranimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        myAdapter = new MyAdapter();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        list.setLayoutManager(mLayoutManager);
        list.setAdapter(myAdapter);

        myAdapter.itemTouchHelper.attachToRecyclerView(list);

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

        /**
         * swipe to dismiss
         */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                data.remove(viewHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);


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
            holder.myRow.bindThis(data.get(position));

            holder.myRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (data.get(position).isBig()) {
                        // shrink row
                        data.get(position).setBig(false);

                    } else {
                        // enlarge row
                        data.get(position).setBig(true);
                    }
                    notifyDataSetChanged();

                }
            });
            holder.myRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // delete row
                    data.remove(position);
                    notifyDataSetChanged();
                    return false;
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
            int c = random.nextInt() | 0xff000000;
            MyRowData d = new MyRowData()
                    .setBig(true)
                    .setColor(c)
                    .setColorName("#" + Integer.toHexString(c));

            // set first row to large and the rest to small
            data.add(0, d);

            for (int i = 1; i < data.size(); i++) {
                data.get(i).setBig(false).notifyChange();
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
