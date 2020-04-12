package com.example.chucknorris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FullQuoteActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference quoteRef = db.collection("Quotes").document("Favourites");
    private CollectionReference quoteDatabase = db.collection("Quotes");

    private TextView savedQ, fullQ;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_quote);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        DocumentReference getSpecificQuote = db.document(path);

        savedQ = findViewById(R.id.savedAs);
        fullQ = findViewById(R.id.fullQuote);
        ratingBar = findViewById(R.id.ratingBarFull);

        getSpecificQuote.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (documentSnapshot.exists()) {
                    SavedQuotes quotes = documentSnapshot.toObject(SavedQuotes.class);
                    String quotes_name = quotes.getQuote();
                    String quotes_description = quotes.getDescription();
                    String quotes_rating = quotes.getRating();


                    savedQ.setText(quotes_name);
                    fullQ.setText(quotes_description);
                    ratingBar.setRating(Float.parseFloat(quotes_rating));

                }
            }
        });
    }
}
