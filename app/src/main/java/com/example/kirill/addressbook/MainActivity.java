package com.example.kirill.addressbook;

import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
        implements ContactsFragment.ContactsFragmentListener,
        DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    public static final String CONTACT_URI = "contact_uri";

    private ContactsFragment contactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //phone
        if (findViewById(R.id.fragmentContainer) != null) {
            contactsFragment = new ContactsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, contactsFragment);
            transaction.commit();
        } else {//tablet
            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactsFragment);

        }

    }

    // show DetailFragment for selected contact
    @Override
    public void onContactSelected(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null) {//phone
            displayContact(contactUri, R.id.fragmentContainer);
        } else//tablet
        {
            getSupportFragmentManager().popBackStack();
            displayContact(contactUri, R.id.rightPaneContainer);
        }
    }

    private void displayContact(Uri contactUri, int containerId) {
        DetailFragment detailFragment = new DetailFragment();
        if (contactUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            detailFragment.setArguments(arguments);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    //show AddEditFragment for add new contact
    @Override
    public void onAddContact() {
        if (findViewById(R.id.fragmentContainer) != null) {//phone
            displayAddEditFragment(R.id.fragmentContainer, null);
        } else {//tablet
            displayAddEditFragment(R.id.rightPaneContainer, null);
        }
    }

    private void displayAddEditFragment(int viewId, Uri contactUri) {
        AddEditFragment addEditFragment = new AddEditFragment();

        if (contactUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            addEditFragment.setArguments(arguments);

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewId, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //back to contact list
    @Override
    public void onContactDeleted() {
        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList();
    }
    //show AddEditFragment for edit contact
    @Override
    public void onEditContact(Uri contactUri) {
        if (findViewById(R.id.fragmentContainer) != null) {//phone
            displayAddEditFragment(R.id.fragmentContainer, contactUri);

        } else {//tablet
            displayAddEditFragment(R.id.rightPaneContainer, contactUri);
        }
    }

    @Override
    public void onAddEditCompleted(Uri contactUri) {

        getSupportFragmentManager().popBackStack();
        contactsFragment.updateContactList();

        if(findViewById(R.id.fragmentContainer) == null) {//tablet

            getSupportFragmentManager().popBackStack();
            displayContact(contactUri, R.id.rightPaneContainer);
        }
    }
}
