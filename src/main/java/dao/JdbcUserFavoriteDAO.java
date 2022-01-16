package dao;

import Models.Movie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserFavoriteDAO implements UserFavoriteDAO{
    private final JdbcTemplate jdbcTemplate;
    public JdbcUserFavoriteDAO(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void addAFavoriteMovie(int userId, int movieId, int rating) {

        String sql = "INSERT INTO user_favorite_movie(movie_id, user_id, rating) values(?,?,?);";
        jdbcTemplate.update(sql, movieId, userId, rating);
    }

    public List<Integer> getUsersThatLikeSameMovie(int movie_id){
        String sql = "SELECT user_id FROM user_favorite_movie WHERE movie_id = ? AND rating > 3;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, movie_id);

        List<Integer> usersThatLikeSameMovie = new ArrayList<Integer>();
        Integer intToAdd = 0;
        while (results.next()){
            intToAdd = results.getInt("user_id");
            usersThatLikeSameMovie.add(intToAdd);
        }
        return usersThatLikeSameMovie;
    }



}
