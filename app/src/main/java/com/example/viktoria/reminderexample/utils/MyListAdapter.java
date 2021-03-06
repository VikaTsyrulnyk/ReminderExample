package com.example.viktoria.reminderexample.utils;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.viktoria.reminderexample.view.MainActivity;
import com.example.viktoria.reminderexample.R;

import java.util.Calendar;
import java.util.List;

/**
 * Adapter for ListView of Reminders in ReminderFragment
 */
public class MyListAdapter extends ArrayAdapter<Reminder> {
    private Context context;
    private Calendar calendar;
    private List<Reminder> items;
    private SparseBooleanArray selectedItemsIds;
    private int layoutResourceId; //layout of row

    public MyListAdapter(Context context, int layoutResourceId,
                         List<Reminder> items) {
        super(context, layoutResourceId, items);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.items = items;
        calendar = Calendar.getInstance();
        selectedItemsIds = new SparseBooleanArray();
    }

    //holder pattern used to avoids frequent call of findViewById()
    static class TaskHolder {
        TextView itemTitle;
        TextView itemTime;
        TextView itemIsElapsed;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Reminder getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new TaskHolder();
            holder.itemTitle = (TextView) row.findViewById(R.id.titleItem);
            holder.itemTime = (TextView) row.findViewById(R.id.timeItem);
            holder.itemIsElapsed = (TextView) row.findViewById(R.id.isElapsed);
            row.setTag(holder);
        } else {
            holder = (TaskHolder) row.getTag();
        }
        Reminder item = getItem(position);
        if (item != null) {
            holder.itemTitle.setText(item.getTitle());
            //Set date and time of event in itemTime
            calendar.setTimeInMillis(item.getEventTime());
            calendar.set(Calendar.SECOND, 0);
            switch (item.getMinutesBeforeEventTime()) {
                case ON_TIME:
                    break;
                case ONE_MINUTE:
                    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
                    break;
                case FIVE_MINUTES:
                    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 5);
                    break;
                case ONE_DAY:
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
                    break;
            }
            Long reminderTime = calendar.getTimeInMillis();
            holder.itemTime.setText(MainActivity.dateFormat.format(reminderTime) + ", " + MainActivity.timeFormat.format(reminderTime));
            calendar = Calendar.getInstance();

            if (calendar.getTimeInMillis() > reminderTime) {
                holder.itemIsElapsed.setText(context.getString(R.string.alarmElapsed));
            } else {
                holder.itemIsElapsed.setText(context.getString(R.string.alarmSetted));
            }

        }

        return row;
    }

    /**
     * Toggle selection of item by its position
     *
     * @param position position of item in selectedItemsIds
     */
    public void toggleSelection(int position) {
        selectView(position, !selectedItemsIds.get(position));
    }

    /**
     * Used in MultiChoiceModeListener to select or delete selection of item by its position
     *
     * @param position position of item in selectedItemsIds
     * @param value    true - select, false - delete selection
     */
    public void selectView(int position, boolean value) {
        if (value)
            selectedItemsIds.put(position, value);
        else
            selectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    /**
     * Used in MultiChoiceModeListener to select all items
     */
    public void selectAll() {
        removeSelection();
        for (int i = 0; i < items.size(); i++) {
           selectView(i, !selectedItemsIds.get(i));
        }

    }

    /**
     * Used in MultiChoiceModeListener to remove selection from all items
     */
    public void removeSelection() {
        selectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Used in MultiChoiceModeListener to get all selected items
     *
     * @return Array of selected items
     */
    public SparseBooleanArray getSelectedIds() {
        return selectedItemsIds;
    }
}
