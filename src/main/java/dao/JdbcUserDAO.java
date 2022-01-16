package dao;

import Models.Movie;
import Models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDAO implements UserDAO{


    private final JdbcTemplate jdbcTemplate;
    private DataSource ds;
    public JdbcUserDAO(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.ds = ds;
    }

    @Override
    public void addAUser(String name, String password) {
        String sql = "INSERT INTO users(user_name, password) values(?, ?);";

        jdbcTemplate.update(sql, name, password);
    }

    public User retrieveUser(String userName, String password){
        String sql = "SELECT user_id, user_name, password FROM users WHERE user_name = ? AND password = ? ;";
        SqlRowSet results =  jdbcTemplate.queryForRowSet(sql, userName, password);
        User newUser = new User(null);
        if(results.next()){
            newUser.setId(results.getInt("user_id"));
            newUser.setName(results.getString("user_name"));
            newUser.setPassword(results.getString("password"));
        }
        return newUser;

    }


    public int retrieveUserId(String userName){
        String sql = "SELECT user_id FROM users WHERE user_name = ?;";
        SqlRowSet result =  jdbcTemplate.queryForRowSet(sql, userName);
        int user_id = 0;
        if (result.next()){
            user_id = result.getInt("user_id");
        }
        return user_id;
    }

    public List<Movie> getFavorites(int userId){
        JdbcMovieDAO movieDAO = new JdbcMovieDAO(ds);

        String sql = "SELECT movie_id, rating FROM user_favorite_movie WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        List<Movie> favorites = new ArrayList<Movie>();
        while(results.next()){
            int movieId = results.getInt("movie_id");
            String movieTitle = movieDAO.getMovieTitleFromId(movieId);
            int rating = results.getInt("rating");
            Movie movie = new Movie(movieTitle, movieId);
            movie.setRating(rating);


            favorites.add(movie);
        }
        return favorites;

    }
}
