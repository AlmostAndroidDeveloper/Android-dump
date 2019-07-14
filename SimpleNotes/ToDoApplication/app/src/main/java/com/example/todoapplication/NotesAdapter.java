package com.example.todoapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {

    private ArrayList<Note> notes;
    private LayoutInflater layoutInflater;
    private DBHelper dbHelper;

    NotesAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        this.dbHelper = new DBHelper(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = layoutInflater.inflate(R.layout.one_task_layout, parent, false);

       final Note note = notes.get(position);

        ((TextView)view.findViewById(R.id.id_text)).setText(String.valueOf(position + 1));
        ((TextView)view.findViewById(R.id.task_text)).setText(note.text);
        view.findViewById(R.id.del_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("tasks","id = " + note.id, new String[]{});
                notes.remove(position);
                db.close();
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
