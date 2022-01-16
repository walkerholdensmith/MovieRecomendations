package Models;

import java.util.List;
import java.util.Map;

public class User {

    private int id;
    private String name;
    private String password;
    private List<Movie> favoriteMovies;

    public User(String name) {
        this.name = name;
    }

    public void addAMovie(Movie movie){
        favoriteMovies.add(movie);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(List<Movie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", favoriteMovies=" + favoriteMovies +
                '}';
    }
}
