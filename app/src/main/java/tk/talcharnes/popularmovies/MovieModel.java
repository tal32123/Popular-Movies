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
        return ((String)("http://image.tmdb.org/t/p/w185/" + poster_path));
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
