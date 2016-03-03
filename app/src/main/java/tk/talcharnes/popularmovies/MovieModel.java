package tk.talcharnes.popularmovies;

/**
 * Created by Tal on 2/26/2016.
 */
public class MovieModel {
    private String poster_path;
    private String overview;
    private String title;
    private String vote_average;
    private String release_date;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        String poster_url;
        if(poster_path != null){
         poster_url = ((String)("http://image.tmdb.org/t/p/w185/" + poster_path));
        }
      else{  poster_url ="http://1vyf1h2a37bmf88hy3i8ce9e.wpengine.netdna-cdn.com/wp-content/themes/public/img/noimgavailable.jpg";}
        return poster_url;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}