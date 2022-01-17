package com.example.android.waitlist;

import android.content.Context;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.waitlist.Data.WaitlistContract;


public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {

    private Context mContext;
    private  Cursor mCursor;

    /**
     * Constructor using the context and the db cursor
     *  @param context the calling context/activity
     * @param// count
     */
    public GuestListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor=cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // COMPLETED (5) Move the cursor to the passed in position, return if moveToPosition returns false
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null\
        int index=0;
        index=mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME);
        if(index<0)index=0;
        // COMPLETED (6) Call getString on the cursor to get the guest's name
        String name = mCursor.getString(index);
        // COMPLETED (7) Call getInt on the cursor to get the party size
        index=mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE);
        if(index<0)index=0;
        int partySize = mCursor.getInt(index);
        // COMPLETED (8) Set the holder's nameTextView text to the guest's name
        // Display the guest name
        index=mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID);
        if(index<0)index=0;
        long id = mCursor.getLong(index);

        // Display the guest name
        holder.nameTextView.setText(name);
        // Display the party count
        holder.partySizeTextView.setText(String.valueOf(partySize));
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor allGuests) {
        if(allGuests==null)return;
        mCursor=allGuests;
        if(allGuests==null)
        {
            this.notifyDataSetChanged();
        }
    }

    // TODO (15) Create a new function called swapCursor that takes the new cursor and returns void

    // TODO (16) Inside, check if the current cursor is not null, and close it if so

    // TODO (17) Update the local mCursor to be equal to  newCursor

    // TODO (18) Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView nameTextView;
        // Will display the party size number
        TextView partySizeTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link GuestListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}
