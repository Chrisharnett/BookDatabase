import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author saxDev
 * studentnumber 20188141
 **/
public class BookApplication {
    public static void main(String[] args) {

        BookDatabaseManager bdm = new BookDatabaseManager();

        System.out.println("Welcome to the book Database Manager.");

        Scanner input = new Scanner(System.in);
        char c;
        do {
            printMenu();
            c = Character.toUpperCase(input.next().charAt(0));
            if (c == 'A') {
                System.out.println();
                for (Book book : bdm.getBookList()){
                    System.out.printf("Title: %s\nAuthor(s): ", book.getTitle());
                    for (Author author: book.getAuthorList()){
                        System.out.printf("%s, %s; ", author.getLastName(), author.getFirstName());
                    }
                    System.out.println();
                    System.out.println();
                }
            } else if (c == 'B') {
                System.out.println();
                for(Author author: bdm.getAuthorList()){
                    System.out.printf("Author: %s\nCurrent Title(s): \n", author.getLastName() + ", " + author.getFirstName());
                    for (Book book: author.getBookList()){
                        System.out.printf("%s\n", book.getTitle());
                    }
                    System.out.println();
                }
            } else if (c == 'C') {
                addBook(bdm);
            } else if (c == 'D') {
                addAuthor(bdm);
            }
        } while (c != 'Q');
        System.out.println("GoodBye!");
    }

    public static void printMenu() {
        System.out.println("\nPlease make a selection:");
        System.out.println("A - Print all books.");
        System.out.println("B - Print all authors.");
        System.out.println("C - Add a new book (for an existing author).");
        System.out.println("D - Add a new author.");
        System.out.println("Q - quit");
    }

    public static void addAuthor(BookDatabaseManager bdm){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Author first name: ");
        String firstName = "";
        while (firstName.equals("")){
            firstName = scanner.next();
            scanner.nextLine();
        }
        System.out.println("Author last name: ");
        String lastName = "";
        while (lastName.equals("")){
            lastName = scanner.next();
            scanner.nextLine();
        }
        bdm.addNewAuthor(firstName, lastName);
    }
    public static void addBook(BookDatabaseManager bdm){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select an author");
        for (int i=0; i < bdm.getAuthorList().size(); i++){
            System.out.printf("%d. %s, %s\n", i+1, bdm.getAuthorList().get(i).getLastName(), bdm.getAuthorList().get(i).getFirstName());
        }
        int authorIndex = scanner.nextInt() - 1;
        System.out.println("Enter book details");
        String isbn = "";
        while (isbn.equals("")){
            System.out.println("ISBN: ");
            isbn = scanner.next();
            scanner.nextLine();
        }
        String title = "";
        while (title.equals("")){
            System.out.println("Title: ");
            title = scanner.next();
            scanner.nextLine();
        }
        int editionNumber = 0;
        while(editionNumber <= 0){
            try{
                System.out.println("Edition Number: ");
                editionNumber = scanner.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Invalid integer, try again.");
                scanner.nextLine();
                editionNumber=0;
            }

        }
        String copyright = "";
        while (copyright.equals("")){
            System.out.println("Copyright: ");
            copyright = scanner.next();
            scanner.nextLine();
        }
        Book book = new Book(isbn, title, editionNumber, copyright);
        bdm.addNewBook(book, bdm.getAuthorList().get(authorIndex));
    }
}