package UserServices;

import Models.Movie;
import Models.User;
import dao.JdbcMovieDAO;
import dao.JdbcUserDAO;
import dao.JdbcUserFavoriteDAO;
import dao.UserDAO;
import javax.sound.midi.Soundbank;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRegister {
    DataSource ds;
    public UserRegister(DataSource ds){
        this.ds = ds;
    }

    public void makeAUser(){

        JdbcUserDAO jdbcUserDAO = new JdbcUserDAO(ds);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter a valid user name: ");
        String name = scanner.nextLine();
        User newUser = new User(name);
        System.out.println("Please Enter a password: ");
        newUser.setPassword(scanner.nextLine());
        jdbcUserDAO.addAUser(newUser.getName(), newUser.getPassword());
        JdbcUserFavoriteDAO favoriteDao = new JdbcUserFavoriteDAO(ds);
        newUser.setId(jdbcUserDAO.retrieveUserId(newUser.getName()));
        startingFavoriteMovies(newUser);
    }

    public void startingFavoriteMovies(User newUser){
        System.out.println("Please enter 5 movies you like.");
        for (int i = 0; i < 5; i ++){
            UserAccount newUserAccount = new UserAccount(ds, newUser);
            newUserAccount.rateANewFilm();
        }
    }
}
