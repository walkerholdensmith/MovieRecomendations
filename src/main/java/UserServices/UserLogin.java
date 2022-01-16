package UserServices;

import Models.User;
import dao.JdbcUserDAO;

import javax.sql.DataSource;
import java.util.Scanner;

public class UserLogin {

    DataSource ds;
    JdbcUserDAO dao;
    public UserLogin(DataSource ds){
        this.ds = ds;
        this.dao = new JdbcUserDAO(ds);
    }

    public User login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user name: ");
        String userName = scanner.nextLine();
        System.out.println("Enter Password: ");
        String passWord = scanner.nextLine();
        User currUser = dao.retrieveUser(userName, passWord);
        currUser.setFavoriteMovies(dao.getFavorites(currUser.getId()));
        return currUser;

    }

}
