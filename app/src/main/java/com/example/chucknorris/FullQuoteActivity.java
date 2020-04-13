package com.example.chucknorris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView savedQ, fullQ, ratingScore;
    private RatingBar ratingBar;
    private ImageView editImage;
    private Button tempButton;

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
        ratingScore = findViewById(R.id.ratingScore);
        editImage = findViewById(R.id.editIcon);
        tempButton = findViewById(R.id.deleteQuote);

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
                    ratingScore.setText(quotes_rating);

                }
            }
        });


        //Saved Quote button
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FullQuoteActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.edit_quotes, null);
                final EditText mNewQuote = (EditText) mView.findViewById(R.id.newQuote);
                final EditText mNewRating = (EditText) mView.findViewById(R.id.newRating);
                Button mSave = (Button) mView.findViewById(R.id.btnSave);
                mSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mNewQuote.getText().toString().isEmpty() && !mNewRating.getText().toString().isEmpty()) {

                            String saveQAs = mNewQuote.getText().toString();
                            String saveRAs = mNewRating.getText().toString();
                            String sameQ = fullQ.getText().toString();

                            SavedQuotes quotes = new SavedQuotes(saveQAs, sameQ, saveRAs);

                            getSpecificQuote.set(quotes);
                            Toast.makeText(FullQuoteActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(FullQuoteActivity.this, "Please fill in empty fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //Delete quote button
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(FullQuoteActivity.this);
                View mView2 = getLayoutInflater().inflate(R.layout.delete_alert, null);
                Button mDelete = (Button) mView2.findViewById(R.id.btnSave);
                mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSpecificQuote.delete();
                    }
                });
                mBuilder2.setView(mView2);
                AlertDialog dialog2 = mBuilder2.create();
                dialog2.show();
            }
        });
    }
}
