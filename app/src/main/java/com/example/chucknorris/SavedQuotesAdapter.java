package com.example.chucknorris;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SavedQuotesAdapter extends FirestoreRecyclerAdapter<SavedQuotes, SavedQuotesAdapter.QuotesHolder> {

    private OnItemClickListener listener;


    public SavedQuotesAdapter(@NonNull FirestoreRecyclerOptions<SavedQuotes> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull SavedQuotesAdapter.QuotesHolder holder, int position, @NonNull SavedQuotes savedQuotes) {
        holder.name.setText(savedQuotes.getQuote());
        holder.description.setText(savedQuotes.getDescription());
        holder.rating.setText(savedQuotes.getRating());
        holder.ratingBar.setRating(Float.parseFloat(savedQuotes.getRating()));
    }

    @NonNull
    @Override
    public QuotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotes_card_rows, parent, false);
        return new QuotesHolder(v);
    }


    public class QuotesHolder extends RecyclerView.ViewHolder {
        TextView name, description, rating;
        RatingBar ratingBar;


        public QuotesHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.quote_name);
            description = itemView.findViewById(R.id.quote_description);
            rating = itemView.findViewById(R.id.quote_rating);
            ratingBar = itemView.findViewById(R.id.quote_score_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
