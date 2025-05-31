package GeneralDatabase;

import java.util.List;

public class Book {
    private List<String> titles;
    private String author;

    public Book(List<String> titles, String author) {
        this.titles = titles;
        this.author = author;
    }

    public List<String> getTitles() {
        return titles;
    }

    public String getAuthor() {
        return author;
    }

    public String getRating() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRating'");
    }

    public String getReview() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReview'");
    }
}
