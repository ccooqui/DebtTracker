package er.debttracker;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import objects.Debts;

public class DatabaseHelper {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef;
    private DatabaseReference couponRef;
    private List<Debts> Gallery = new LinkedList<>();

    protected void onCreate() {
        //Prefetch all existing items for the node using a SingleValueEventListener
        FirebaseDatabase.getInstance().getReference("debts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                for(DataSnapshot aSnapshotIterable : snapshotIterable) {
                    aSnapshotIterable.getValue(Debts.class);
                    Gallery.add(aSnapshotIterable.getValue(Debts.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        //Once you fetch all existing items, add a listener on the last item for realtime updates
        FirebaseDatabase.getInstance().getReference("coupon").orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> snapshotIterator = snapshotIterable.iterator();
                //fetch last added item
                if(snapshotIterator.hasNext())
                {
                    Debts value = snapshotIterator.next().getValue(Debts.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
