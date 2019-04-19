
public class BooksReader {
    public String bookTitle;
    private static String language = "en";

    public BooksReader(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void readBook() {
        System.out.print("Read book: " + bookTitle + " in " + language);
    }
}
