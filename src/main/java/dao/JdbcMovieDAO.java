package dao;

import Models.Movie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class JdbcMovieDAO implements MovieDAO {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMovieDAO(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public int getMovieIdFromTitle(String movieTitle){

        String sql = "SELECT movie_id FROM movie WHERE title = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, movieTitle);
        int movieId = 0;
        if (results.next()){
            movieId = results.getInt("movie_id");
        }
        return movieId;

    }


    public Movie getMovieObjFromTitle(String title){
        String sql = "SELECT title, movie_id FROM movie WHERE title = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, title);
        Movie movie = new Movie("",0);
        if(results.next()){
            movie.setMovie_id(results.getInt("movie_id"));
            movie.setTitle(results.getString("title"));
        }
        return movie;
    }
    public String getMovieTitleFromId(int movieId){

        String sql = "SELECT title FROM movie WHERE movie_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, movieId);
        String movieTitle ="";
        if (results.next()){
            movieTitle = results.getString("title");
        }
        return movieTitle;

    }
    public Boolean isValidTitle(String title){
        String sql = "SELECT movie_id FROM movie WHERE title = ?";
        int returnID = 0;
        return jdbcTemplate.queryForObject(sql, Integer.class, title) != 0;

    }

    public List<String> getGenreOfMovie(int movie_id){
        List<String> genres = new ArrayList<>();
        String sql = "SELECT genre_name\n" +
                "FROM genre\n" +
                "JOIN movie_genre ON movie_genre.genre_id = genre.genre_id\n" +
                "WHERE movie_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, movie_id);
        while (results.next()){
            genres.add(results.getString("genre_name"));

        }
        return genres;
    }


    public List<String> getMovieBasedOnGenre(String genre){
        List<String> movies = new ArrayList<>();
        String sql = "SELECT title\n" +
                "FROM movie\n" +
                "JOIN movie_genre ON movie.movie_id = movie_genre.movie_id\n" +
                "JOIN genre ON movie_genre.genre_id = genre.genre_id\n" +
                "WHERE genre_name = ? ORDER BY random();";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, genre);
        while (results.next()){
            if (movies.size() <= 10){
                movies.add(results.getString("title"));
            }
        }
        return movies;
    }

}
