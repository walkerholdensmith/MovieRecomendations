import Models.User;
import UserServices.UserAccount;
import UserServices.UserLogin;
import UserServices.UserRegister;
import dao.JdbcMovieDAO;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Scanner;

//
public class UserInterface {

    JdbcMovieDAO movieDAO;


    public static void main(String[] args) {

        UserInterface gui = new UserInterface();

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/MovieDB");
        ds.setUsername("walkersmith");
        ds.setPassword("");
        JdbcMovieDAO movieDAO = new JdbcMovieDAO(ds);

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Register\n2. Sign In");
        String userChoice = scanner.nextLine();

        if(userChoice.equals("1")){
            UserRegister newRegister = new UserRegister(ds);
            newRegister.makeAUser();

        } else if (userChoice.equals("2")) {
            UserLogin newLogin = new UserLogin(ds);
            User currUser = newLogin.login();
            while(currUser.getId() == 0){
                currUser = newLogin.login();
            }
            UserAccount userAccount = new UserAccount(ds, currUser);
            userAccount.userMenu();

        }
    }





}

