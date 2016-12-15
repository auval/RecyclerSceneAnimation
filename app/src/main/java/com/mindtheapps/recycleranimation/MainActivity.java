package com.mindtheapps.recycleranimation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
 * https://github.com/auval/RecyclerSceneAnimation/blob/master/app/src/main/java/com/mindtheapps/recycleranimation/MainActivity.java
 *
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

        // line separator
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (getBaseContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(getBaseContext(), R.drawable.line_sep));
        list.addItemDecoration(dividerItemDecoration);

        // swipe to delete row
        myAdapter.itemTouchHelper.attachToRecyclerView(list);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds the "+" button to the action bar
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
        static Paint drawUnder = new Paint();

        static {
            drawUnder.setAntiAlias(true);
            drawUnder.setColor(0xff_ff_00_ff); // white
            drawUnder.setTextSize(24);
        }

        Drawable rubbish;
        /**
         * swipe to dismiss
         */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            final int RUBBISH_SIZE = 48;

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

            @Override
            public void onChildDraw(Canvas c,
                                    RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState,
                                    boolean isCurrentlyActive) {


                MyRowView myRow = ((MyViewHolder) viewHolder).myRow;

                // 0,0 is the top left corner of the RecyclerView
                // the "0,0" of the row is:
                // myRow.getLeft(), myRow.getTop()

                final int h = myRow.getHeight();

                // trick to change the trash color according to the location
                float percent = Math.abs(dX / myRow.getWidth());
                DrawableCompat.setTint(rubbish, evaluateRgb(percent, 0xff_00_00_00, 0xff_ff_00_00));

                if (dX > 0) {
                    // moving right
                    rubbish.setBounds((int) (
                                    dX - RUBBISH_SIZE - 4),
                            myRow.getTop() + (h - RUBBISH_SIZE) / 2,
                            (int) (dX - 4),
                            myRow.getTop() + (h + RUBBISH_SIZE) / 2);
                } else {
                    // moving left
                    rubbish.setBounds((int) (
                                    myRow.getRight() + dX + RUBBISH_SIZE + 4),
                            myRow.getTop() + (h - RUBBISH_SIZE) / 2,
                            myRow.getRight() + (int) (dX + 4),
                            myRow.getTop() + (h + RUBBISH_SIZE) / 2);
                }
                rubbish.draw(c);


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        MyAdapter() {
            setHasStableIds(true);
        }

        /**
         * adapted from ArgbEvaluator, which does too much castings from Object to Integer.
         *
         * @param fraction
         * @param startValue
         * @param endValue
         * @return
         */
        public static int evaluateRgb(float fraction, int startValue, int endValue) {
            int startInt = startValue;
            int startA = (startInt >> 24) & 0xff;
            int startR = (startInt >> 16) & 0xff;
            int startG = (startInt >> 8) & 0xff;
            int startB = startInt & 0xff;

            int endInt = endValue;
            int endA = (endInt >> 24) & 0xff;
            int endR = (endInt >> 16) & 0xff;
            int endG = (endInt >> 8) & 0xff;
            int endB = endInt & 0xff;

            return ((startA + (int) (fraction * (endA - startA))) << 24) |
                    ((startR + (int) (fraction * (endR - startR))) << 16) |
                    ((startG + (int) (fraction * (endG - startG))) << 8) |
                    ((startB + (int) (fraction * (endB - startB))));
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final MyRowView myRowView = new MyRowView(parent);

            rubbish = ContextCompat.getDrawable(parent.getContext(), R.drawable.ic_delete_black_24dp);

            // mutate():
            // A mutable drawable is guaranteed to not share its state with any other drawable
            rubbish = DrawableCompat.wrap(rubbish).mutate();

//            DrawableCompat.setTint(rubbish, Color.RED);

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
