package com.example.chucknorris;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private TextView quote_generate;
    private EditText quote_saved_as;
    private RatingBar rating;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quote_generate = findViewById(R.id.quote);
        quote_saved_as = findViewById(R.id.editTv);
        rating = findViewById(R.id.ratingBar);



    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, SavedQuotesActivity.class);

        String quote = quote_generate.getText().toString();
        String saved_quote = quote_saved_as.getText().toString();
        String saved_rating = String.valueOf(rating.getRating());

        SavedQuotes quotes = new SavedQuotes(quote, saved_quote, saved_rating);

        Button quote_button = findViewById(R.id.quote_button);
        quote_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.chucknorris.io").addConverterFactory(GsonConverterFactory.create()).build();
                ChuckNorrisInterface service = retrofit.create(ChuckNorrisInterface.class);
                Call<ChuckNorris> norrisCall = service.getQuotes();
                norrisCall.enqueue(new Callback<ChuckNorris>() {
                    @Override
                    public void onResponse(Call<ChuckNorris> call, Response<ChuckNorris> response) {
                        if (response.isSuccessful()) {
                            quote_generate.setText(String.valueOf(response.body().getQuotes()));
                        }
                    }
                    @Override
                    public void onFailure(Call<ChuckNorris> call, Throwable t) {

                    }
                });

            }
        });

        Button go_to_favourites = findViewById(R.id.favourite_quotes_button);
        go_to_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(intent);
            }
        });

        Button save_quote_button = findViewById(R.id.save_quote);
        save_quote_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quote = quote_generate.getText().toString();
                String saved_quote = quote_saved_as.getText().toString();
                String saved_rating = String.valueOf(rating.getRating());

                SavedQuotes quotes = new SavedQuotes(saved_quote, quote, saved_rating);
                db.collection("Quotes").add(quotes)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {


                                Toast.makeText(MainActivity.this, "Quote Saved", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

    }

}
