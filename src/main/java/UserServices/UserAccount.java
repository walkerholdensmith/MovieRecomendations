package UserServices;

import Models.Movie;
import Models.User;
import dao.JdbcMovieDAO;
import dao.JdbcUserDAO;
import dao.JdbcUserFavoriteDAO;

import javax.sql.DataSource;
import java.util.*;

public class UserAccount {

    DataSource ds;
    User currUser;
    JdbcUserFavoriteDAO userFavoriteDAO;
    JdbcUserDAO jdbcUserDAO;
    JdbcMovieDAO jdbcMovieDAO;

    public UserAccount(DataSource ds, User currUser) {
        this.ds = ds;
        this.currUser = currUser;
        this.userFavoriteDAO = new JdbcUserFavoriteDAO(ds);
        this.jdbcUserDAO = new JdbcUserDAO(ds);
        this.jdbcMovieDAO = new JdbcMovieDAO(ds);
    }


    public void userMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. See suggestions:\n2. Rate a new Movie:  ");
        String answer = scanner.nextLine();
        if (answer.equals("1")) {
            System.out.println("=======================================================");
            becauseYouLiked();
            System.out.println("=======================================================");
            collaborativeFiltering();
            favoriteGenre();

        } else if(answer.equals("2")){
            rateANewFilm();
        }

    }

    public void rateANewFilm(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Movie Title: ");
        String movieToAdd = scanner.nextLine();
        while(jdbcMovieDAO.getMovieIdFromTitle(movieToAdd) == 0){
            System.out.println("Not a valid film, enter a new one: ");
            movieToAdd = scanner.nextLine();
        }

        System.out.println("Rating (1-5): ");

        int rating = 0;//
        String strRating = scanner.nextLine();
        boolean isNum = false;
        while (!isNum){

            if(!canBeParsed(strRating)){
                System.out.println("Rating (1-5): ");
                strRating = scanner.nextLine();

            }
            rating = Integer.parseInt(strRating);
            isNum = true;


        }
        userFavoriteDAO.addAFavoriteMovie(currUser.getId(),jdbcMovieDAO.getMovieIdFromTitle(movieToAdd),rating);
    }

    public boolean canBeParsed(String num){
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void becauseYouLiked() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(currUser.getFavoriteMovies().size());
        String randomTitle = currUser.getFavoriteMovies().get(randomIndex).getTitle();
        int randomId = currUser.getFavoriteMovies().get(randomIndex).getMovie_id();

        List<Integer> usersThatLikedSameMovie = userFavoriteDAO.getUsersThatLikeSameMovie(randomId);
        List<String> listOfTitles = new ArrayList<String>();

        System.out.println("People Who Liked " + randomTitle + " Also Liked These Titles... ");

        for (Integer userId : usersThatLikedSameMovie) {
            if (userId != currUser.getId()) {
                for (Movie movie : jdbcUserDAO.getFavorites(userId)) {
                    if (!movie.getTitle().equals(randomTitle)) {
                        if(!listOfTitles.contains(movie.getTitle())){
                            listOfTitles.add(movie.getTitle());
                        }

                    }

                }
            }

        }
        for (String title : listOfTitles){
            System.out.println(title);
        }
    }

    public void favoriteGenre(){
        Map<String, Integer> genreCount = new HashMap<>();
        int movieId = currUser.getFavoriteMovies().get(0).getMovie_id();

        List<String> favoriteTitles = new ArrayList<>();
        for (Movie movie : currUser.getFavoriteMovies()){
            favoriteTitles.add(movie.getTitle());
            List<String> genres = jdbcMovieDAO.getGenreOfMovie(movie.getMovie_id());
            for(String genre: genres){
                if(genreCount.containsKey(genre)){
                    genreCount.put(genre, genreCount.get(genre) + 1);
                } else {
                    genreCount.put(genre, 1);
                }
            }
        }
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        genreCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        int count = 1;
        for (Map.Entry<String,Integer> entry : reverseSortedMap.entrySet()) {
            List<String> movies = jdbcMovieDAO.getMovieBasedOnGenre(entry.getKey());
            System.out.println("=======================================================");
            System.out.println("Suggestions in " + entry.getKey());
            System.out.println("=======================================================");
            for (String title : movies) {
                if (!favoriteTitles.contains(title)) {
                    System.out.println(title);
                    count++;
                }
            }
        }
    }

    public void collaborativeFiltering() {

        Map<String, List<Integer>> similarRankings = new HashMap<>();

        List<String> watchedTitles = new ArrayList<>();
        for(Movie watched : currUser.getFavoriteMovies()){
            watchedTitles.add(watched.getTitle());
        }

        List<Integer> heroList = new ArrayList<>();
        for (Movie favoriteMovie : currUser.getFavoriteMovies()) {
            String mostLiked = favoriteMovie.getTitle();

            List<Integer> usersThatLikedSameMovie = userFavoriteDAO.getUsersThatLikeSameMovie(favoriteMovie.getMovie_id());


            for (Integer userId : usersThatLikedSameMovie) {

                if (userId != currUser.getId()){

                    for (Movie similarMovie : jdbcUserDAO.getFavorites(userId)) {
                        if (similarRankings.containsKey(similarMovie.getTitle()) && !watchedTitles.contains(similarMovie.getTitle())) {
                            if(!similarRankings.get(similarMovie.getTitle()).contains(userId)){

                                similarRankings.get(similarMovie.getTitle()).add(userId);
                            }

                        } else if(!similarRankings.containsKey(similarMovie.getTitle())&& !watchedTitles.contains(similarMovie.getTitle())){
                            List<Integer> users = new ArrayList<>();
                            users.add(userId);
                            similarRankings.put(similarMovie.getTitle(), users);
                        }
                    }
                }

            }

        }
        Map<String, Integer> finalMap = new HashMap<>();
        for (Map.Entry<String,List<Integer>> entry : similarRankings.entrySet())
        {
            if (!finalMap.containsKey(entry.getKey())){
                finalMap.put(entry.getKey(), similarRankings.get(entry.getKey()).size());
            }
        }
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        finalMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        System.out.println("Your top movie suggestions: ");
        for (Map.Entry<String,Integer> entry : reverseSortedMap.entrySet()){
            System.out.println(entry.getKey());
        }
    }


}
