package tk.talcharnes.popularmovies;

/**
 * Created by Tal on 6/17/2016.
 */
public class MovieTrailer{
        private String movieName;
        private String trailerUrl;

        public void setMovieName ( String movieName )
        {
            this.movieName = movieName;
        }

        public void setTrailerUrl ( String trailerUrl)
        {
            this.trailerUrl= trailerUrl;
        }

        public String getMovieName ()
        {
            return movieName;
        }

        public String getTrailerUrl ()
        {
            return trailerUrl;
        }

    public String getMovieTrailerList(){
        String trailerList = "";
        MovieJSON movieJSON = new MovieJSON();
        for (int i = 0; i< movieJSON.getMovieTrailerList().size(); i++){
            String trailerListItem = movieJSON.getMovieTrailerList().get(i).getMovieName();
            trailerList = trailerList + "\n" + trailerListItem;
        }
        return trailerList;
    }

    }

