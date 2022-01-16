package Models;

public class Movie {

    private String title;
    private int movie_id;
    private int rating;

    public Movie(String title, int movie_id) {
        this.title = title;
        this.movie_id = movie_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
