package PersonalDatabase;

import java.awt.print.Book;
import java.util.Date;

public class PersonalBook {
    private Book book;
    private String status;
    private String timeSpent;
    private Date startDate;
    private Date endDate;
    private int userRating;
    private String userReview;

    public PersonalBook(Book book, String title, String author, double rating, String review, String status, String timeSpent, Date startDate, Date endDate, int userRating, String userReview) {
        this.book = book;
        this.status = status;
        this.timeSpent = timeSpent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userRating = userRating;
        this.userReview = userReview;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getUserRating() {
        return userRating;
    }

    public String getUserReview() {
        return userReview;
    }
}
