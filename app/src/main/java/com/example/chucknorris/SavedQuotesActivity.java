package com.example.chucknorris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SavedQuotesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference quoteRef = db.collection("Quotes").document("Favourites");
    private CollectionReference quoteDatabase = db.collection("Quotes");

    private SavedQuotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_quotes);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        //Query query = quizListRef.orderBy("result", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SavedQuotes> options = new FirestoreRecyclerOptions.Builder<SavedQuotes>()
                .setQuery(quoteDatabase, SavedQuotes.class)
                .build();

        adapter = new SavedQuotesAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.quotes_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Intent intent = new Intent (this, FullQuoteActivity.class);

        adapter.setOnItemClickListener(new SavedQuotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SavedQuotes quotes = documentSnapshot.toObject(SavedQuotes.class);
                String path = documentSnapshot.getReference().getPath();
                System.out.println(path);
                Toast.makeText(SavedQuotesActivity.this, "", Toast.LENGTH_SHORT).show();
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
