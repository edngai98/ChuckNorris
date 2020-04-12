package com.example.chucknorris;

public class SavedQuotes {

    private String quote;
    private String description;
    private String rating;

    public SavedQuotes() {

    }

    public SavedQuotes(String quote, String description, String rating) {
        this.quote = quote;
        this.description = description;
        this.rating = rating;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
