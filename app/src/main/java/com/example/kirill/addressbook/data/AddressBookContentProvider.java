package com.example.kirill.addressbook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.kirill.addressbook.R;

public class AddressBookContentProvider extends ContentProvider {

    private AddressBookDatabaseHelper dbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ONE_CONTACT = 1;
    private static final int CONTACTS = 2;

    static {
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME + "/#", ONE_CONTACT);
        uriMatcher.addURI(DatabaseDescription.AUTHORITY, DatabaseDescription.Contact.TABLE_NAME, CONTACTS);
    }

    public AddressBookContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new AddressBookDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                queryBuilder.appendWhere(DatabaseDescription.Contact._ID + "=" + uri.getLastPathSegment());
                break;
            case CONTACTS:
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newContactUri = null;

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                long rowId = dbHelper.getWritableDatabase().insert(DatabaseDescription.Contact.TABLE_NAME, null, values);
                if (rowId > 0) {
                    newContactUri = DatabaseDescription.Contact.buildContactUri(rowId);
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);

                }
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        return newContactUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numberOfRowUpdated;

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                String id = uri.getLastPathSegment();
                numberOfRowUpdated = dbHelper.getWritableDatabase().update(
                        DatabaseDescription.Contact.TABLE_NAME, values, DatabaseDescription.Contact._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        if (numberOfRowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRowUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                String id = uri.getLastPathSegment();
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        DatabaseDescription.Contact.TABLE_NAME, DatabaseDescription.Contact._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }
        if (numberOfRowsDeleted != 0 ){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
}
