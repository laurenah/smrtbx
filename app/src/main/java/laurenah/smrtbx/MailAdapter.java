package laurenah.smrtbx;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> {

    private ArrayList<Mail> mMail;
    private static final String MAIL_PATH = "mail";
    private FirebaseDatabase mDb;
    private DatabaseReference mMailRef;
    private Context mContext;

    private NotificationManager manager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MailAdapter(Context context) {
        mContext = context;
        mMail = new ArrayList<>();
        mDb = FirebaseDatabase.getInstance();
        mMailRef = mDb.getReference(MAIL_PATH);

        mMailRef.addChildEventListener(new MailChildEventListener());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView text = holder.view.findViewById(R.id.mail_time);
        String mailText = "Mailbox accessed at " + mMail.get(position).getTime();
        text.setText(mailText);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMail.size();
    }

    public class MailChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Mail mail = dataSnapshot.getValue(Mail.class);
            mail.setKey(dataSnapshot.getKey());
            mMail.add(0,mail);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
