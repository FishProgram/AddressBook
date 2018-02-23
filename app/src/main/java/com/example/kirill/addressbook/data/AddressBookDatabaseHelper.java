package com.example.kirill.addressbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kirill on 21.02.2018.
 */

public class AddressBookDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AddressBook.db";
    private static final int DATABASE_VERSION = 1;

    public AddressBookDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final  String CREATE_CONTACTS_TABLE =
                "CREATE TABLE " + DatabaseDescription.Contact.TABLE_NAME + "(" +
                        DatabaseDescription.Contact._ID + " integer primary key, " +
                        DatabaseDescription.Contact.COLUMN_NAME + " Text, " +
                        DatabaseDescription.Contact.COLUMN_PHONE + " Text, " +
                        DatabaseDescription.Contact.COLUMN_EMAIL + " Text, " +
                        DatabaseDescription.Contact.COLUMN_STREET + " Text, " +
                        DatabaseDescription.Contact.COLUMN_CITY + " Text, " +
                        DatabaseDescription.Contact.COLUMN_STATE + " Text, " +
                        DatabaseDescription.Contact.COLUMN_ZIP + " Text);";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
