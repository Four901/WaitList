package com.example.android.waitlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.waitlist.Data.WaitlistContract;
import com.example.android.waitlist.Data.WaitlistDbHelper;


public class MainActivity extends AppCompatActivity {

    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;

    // TODO (1) Create local EditText members for mNewGuestNameEditText and mNewPartySizeEditText
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    // TODO (13) Create a constant string LOG_TAG that is equal to the class.getSimpleName()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView waitlistRecyclerView;

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // TODO (2) Set the Edit texts to the corresponding views using findViewById
        mNewGuestNameEditText=(EditText)findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText=(EditText)findViewById(R.id.party_count_edit_text);
        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Create a DB helper (this will create the DB if run for the first time)
        WaitlistDbHelper dbHelper = new WaitlistDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // TODO (3) Remove this fake data call since we will be inserting our own data now
       // TestUtil.insertFakeData(mDb);

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllGuests();

        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeGuest(id);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                mAdapter.swapCursor(getAllGuests());
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(waitlistRecyclerView);
        //TODO (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions

        // TODO (4) Override onMove and simply return false inside

        // TODO (5) Override onSwiped

        // TODO (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
        // TODO (9) call removeGuest and pass through that id
        // TODO (10) call swapCursor on mAdapter passing in getAllGuests() as the argument

        //TODO (11) attach the ItemTouchHelper to the waitlistRecyclerView
    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {

        // TODO (9) First thing, check if any of the EditTexts are empty, return if so
        EditText nameWaala=view.findViewById(R.id.person_name_edit_text);
        EditText partyCountWaala=view.findViewById(R.id.party_count_edit_text);
        if(nameWaala.getText()==null||partyCountWaala.getText()==null)return;
        String name=nameWaala.getText().toString();
        int partyCount=1;
        try {
            partyCount=Integer.parseInt(partyCountWaala.getText().toString());
        }catch (Exception e)
        {
            e.fillInStackTrace();
        }
        addNewGuest(name,partyCount);
        mAdapter.swapCursor(getAllGuests());

        // COMPLETED (20) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
        //clear UI text fields
        mNewPartySizeEditText.clearFocus();
        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();
        // TODO (10) Create an integer to store the party size and initialize to 1

        // TODO (11) Useto parse Integer.parseInt  mNewPartySizeEditText.getText to an integer

        // TODO (12) Make sure you surround the Integer.parseInt with a try catch and log any exception

        // TODO (14) call addNewGuest with the guest name and party size

        // TODO (19) call mAdapter.swapCursor to update the cursor by passing in getAllGuests()

        // TODO (20) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText

    }




    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }
    private void addNewGuest(String name, int partyCount) {
        ContentValues values=new ContentValues();
        values.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME,name);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE,partyCount);
        mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME,null,values);
    }
    // TODO (4) Create a new addNewGuest method

    // TODO (5) Inside, create a ContentValues instance to pass the values onto the insert query

    // TODO (6) call put to insert the name value with the key COLUMN_GUEST_NAME

    // TODO (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE

    // TODO (8) call insert to run an insert query on TABLE_NAME with the ContentValues created

    // TODO (1) Create a new function called removeGuest that takes long id as input and returns a boolean

    // TODO (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
   public boolean removeGuest(long id)
   {
//       String toCheck="SELECT COUNT(*) FROM "+ WaitlistContract.WaitlistEntry.TABLE_NAME+" WHERE "+
//               WaitlistContract.WaitlistEntry._ID+" = "+String.valueOf(id);

return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID +" = "+id,null)>0;
   }

}
