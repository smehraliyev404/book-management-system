package GeneralDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCsv {
    public static List<Book> readCSV(final String filename) throws IOException {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String[] titles = data[0].trim().split("\"");
                List<String> bookTitles = new ArrayList<>();
                if (titles.length > 1) {
                    String[] splitTitles = titles[1].split(",");
                    for (String splitTitle : splitTitles) {
                        bookTitles.add(splitTitle.trim());
                    }
                } else {
                    bookTitles.add(titles[0].trim());
                }
                String author = data.length == 2 ? data[1].trim() : "";
                books.add(new Book(bookTitles, author));
            }
        }
        return books;
    }
}
