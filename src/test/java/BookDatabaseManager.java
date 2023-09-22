import java.util.List;

/**
 * @author saxDev
 * studentnumber 20188141
 **/
public class BookDatabaseManager {
    private String DATABASE_URL;
    private String SELECT_QUERY_AUTHOR;
    private List<Book> bookList;
    private List<Author> authorList;

// Constructor for Book DatabaseManager that takes no parameters.
    public BookDatabaseManager() {
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }
    public void addNewBook(Book book){

    }
    public void  addNewAuthor(Author author){

    }
    private void loadDatabase(){

    }
    private void loadBooks(){

    }
    private void loadAuthor(){

    }
}
