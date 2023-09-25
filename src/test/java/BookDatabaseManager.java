
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author saxDev
 * studentnumber 20188141
 **/
public class BookDatabaseManager {
    final String DATABASE_URL = "jdbc:mariadb://localhost:3306/books";
    final String USER = "root";
    final String PASS = "root";

    private List<Book> bookList;
    private List<Author> authorList;

// Constructor for Book DatabaseManager that takes no parameters.
    public BookDatabaseManager() {
        loadDatabase();
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void addNewBook(Book book, Author author){
        try(Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASS)){
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO titles VALUES(?, ?, ?, ?)");
            pstmt.setString(1, book.getISBN());
            pstmt.setString(2, book.getTitle());
            pstmt.setInt(3, book.getEdition());
            pstmt.setString(4, book.getCopyright());
            pstmt.execute();

            pstmt = conn.prepareStatement("INSERT INTO authorISBN VALUES(?, ?)");
            pstmt.setInt(1, author.getId());
            pstmt.setString(2, book.getISBN());
            pstmt.execute();
            loadDatabase();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void  addNewAuthor(String firstName, String lastName){
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASS)){
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO authors VALUES(DEFAULT, ?, ?)");
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.execute();
            loadDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    private void loadDatabase()  {
        bookList = new ArrayList<>();
        authorList = new ArrayList<>();
        loadBooks();
        loadAuthors();
        String queryAuthor ="SELECT a.authorID, a.firstName, a.lastName " +
                            "FROM authors a JOIN authorISBN i ON(a.authorID = i.authorID)" +
                            "JOIN titles t using(isbn)" +
                            "WHERE i.isbn = ?";
        for (Book book : bookList) {
            try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASS)){
                PreparedStatement pstmt = conn.prepareStatement(queryAuthor);
                pstmt.setString(1, book.getISBN());
                ResultSet results = pstmt.executeQuery();
                List<Author> bookAuthors = new ArrayList<>();
                while (results.next()) {
                    for (Author author: authorList){
                        if (author.getId() == results.getInt("authorId")){
                            bookAuthors.add(author);
                        }
                    }
                }
                book.setAuthorList(bookAuthors);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String queryTitle = "SELECT t.title, t.isbn, t.editionNumber, t.copyright " +
                            "FROM authors a JOIN authorISBN i ON(a.authorID = i.authorID)" +
                            "JOIN titles t using(isbn)" +
                            "WHERE i.authorId = ?";
        for (Author author: authorList){
            try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASS)){
                PreparedStatement pstmt = conn.prepareStatement(queryTitle);
                pstmt.setInt(1, author.getId());
                ResultSet results = pstmt.executeQuery();
                List<Book> authorsBooks = new ArrayList<>();
                while (results.next()) {
                    for (Book book: bookList){
                        if (results.getString("title").equalsIgnoreCase(book.getTitle())){
                            authorsBooks.add(book);
                        }
                    }
                }
                author.setBookList(authorsBooks);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    private void loadBooks(){
        try(Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM titles")
        ) {
            while (rs.next()) {
                Book book = new Book(rs.getString("isbn"), rs.getString("title"),
                        rs.getInt("editionNumber"), rs.getString("copyright"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadAuthors(){
        try(Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM authors")
        ) {
            while (rs.next()) {
                Author author = new Author(rs.getInt("authorid"), rs.getString("firstName"),
                        rs.getString("lastName"));
                authorList.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
