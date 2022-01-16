package dao;

public interface UserFavoriteDAO {
    public void addAFavoriteMovie(int userId, int movieId, int rating);
}
