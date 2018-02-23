package com.example.kirill.addressbook;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kirill.addressbook.data.DatabaseDescription;

/**
 * Created by Kirill on 21.02.2018.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public interface DetailFragmentListener {
        void onContactDeleted();

        void onEditContact(Uri contactUri);
    }

    private static final int CONTACT_LOADER = 0;

    private static DetailFragmentListener listener; // MainActivity
    private static Uri contactUri;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView streetTextView;
    private TextView cityTextView;
    private TextView stateTextView;
    private TextView zipTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);


        Bundle arguments = getArguments();

        if (arguments != null)
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        streetTextView = (TextView) view.findViewById(R.id.streetTextView);
        cityTextView = (TextView) view.findViewById(R.id.cityTextView);
        stateTextView = (TextView) view.findViewById(R.id.stateTextView);
        zipTextView = (TextView) view.findViewById(R.id.zipTextView);


        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        return view;

    }

    // display this fragment's menu items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    // handle menu item selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditContact(contactUri); // pass Uri to listener
                return true;
            case R.id.action_delete:
                deleteContact();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteContact() {
        // use FragmentManager to display the confirmDelete DialogFragment
        confirmDelete.show(getFragmentManager(), "confirm delete");
    }

    private final ConfirmDelete confirmDelete = new ConfirmDelete();

    public static class ConfirmDelete extends DialogFragment{


        // create an AlertDialog and return it
        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            // create a new AlertDialog Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message);

            builder.setPositiveButton(R.string.button_delete,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface dialog, int button) {

                            // delete on the AddressBookContentProvider
                            getActivity().getContentResolver().delete(
                                    contactUri, null, null);
                            listener.onContactDeleted();
                        }
                    }
            );

            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create(); // return the AlertDialog
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;

        switch (id) {
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        contactUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // if the contact exists in the database, display its data
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int nameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_PHONE);
            int emailIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_EMAIL);
            int streetIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STREET);
            int cityIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_CITY);
            int stateIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STATE);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);

            // fill TextViews with the retrieved data
            nameTextView.setText(data.getString(nameIndex));
            phoneTextView.setText(data.getString(phoneIndex));
            emailTextView.setText(data.getString(emailIndex));
            streetTextView.setText(data.getString(streetIndex));
            cityTextView.setText(data.getString(cityIndex));
            stateTextView.setText(data.getString(stateIndex));
            zipTextView.setText(data.getString(zipIndex));
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
