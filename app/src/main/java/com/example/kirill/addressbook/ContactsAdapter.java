package com.example.kirill.addressbook;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kirill.addressbook.data.DatabaseDescription;

/**
 * Created by Kirill on 21.02.2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView textView;
        private long rowId;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)(itemView.findViewById(android.R.id.text1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(DatabaseDescription.Contact.buildContactUri(rowId));
                }
            });
        }

        public  void setRowId(long rowId){
            this.rowId = rowId;
        }
    }

    private Cursor cursor = null;
    private final ContactClickListener clickListener;


    public ContactsAdapter(ContactClickListener contactClickListener) {
        this.clickListener = contactClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.setRowId(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Contact._ID)));
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME)));
    }

    @Override
    public int getItemCount() {
        return (cursor != null ) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor data) {
        this.cursor = data;
        notifyDataSetChanged();
    }

    public interface ContactClickListener {
        void onClick(Uri contactUri);
    }



}
