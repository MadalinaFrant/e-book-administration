import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class Book implements IPublishingArtifact {

    private final int ID, pageCount, languageID;
    private final String name, subtitle, keywords;
    public String ISBN;
    private final Calendar createdOn;
    private Author[] authors;

    public Book(int ID, String name, String subtitle, String ISBN, int pageCount,
                String keywords, int languageID, Calendar createdOn) {
        this.ID = ID;
        this.name = name;
        this.subtitle = subtitle;
        this.ISBN = ISBN;
        this.pageCount = pageCount;
        this.keywords = keywords;
        this.languageID = languageID;
        this.createdOn = createdOn;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getKeywords() {
        return keywords;
    }

    public int getLanguageID() {
        return languageID;
    }

    public String getCreatedOn() {
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        return date.format(createdOn.getTime());
    }

    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    /* adauga author in lista de autori a book */
    public static void addAuthor(Book book, Author author) {
        Author[] authors;
        if (book.getAuthors() == null) {
            authors = new Author[1];
            authors[0] = author;
        } else {
            authors = book.getAuthors();
            authors = Arrays.copyOf(authors, authors.length + 1);
            authors[authors.length - 1] = author;
        }
        book.setAuthors(authors);
    }

    public String printBook(int tabs) {
        String t = "\t".repeat(tabs);
        return
                t + "<title>" + getName() + "</title>\n" +
                t + "<subtitle>" + getSubtitle() + "</subtitle>\n" +
                t + "<isbn>" + getISBN() + "</isbn>\n" +
                t + "<pageCount>" + getPageCount() + "</pageCount>\n" +
                t + "<keywords>" + getKeywords() + "</keywords>\n" +
                t + "<languageID>" + getLanguageID() + "</languageID>\n" +
                t + "<createdOn>" + getCreatedOn() + "</createdOn>\n" +
                t + "<authors>" + Arrays.toString(getAuthors()) + "</authors>\n";
    }

    public String Publish() {
        return "<xml>\n" + printBook(1) + "</xml>\n";
    }

    /* intoarce cartea cu ID-ul dat ca parametru */
    public static Book getBookForID(Book[] books, int ID) {
        for (Book b : books)
            if (b.getID() == ID)
                return b;
        return null;
    }

    /* initializarea cartilor (toate atributele mai putin vectorul de autori) */
    public static Book[] readBooks() throws FileNotFoundException, ParseException {
        File f = new File("./init/books.in");
        Scanner s = new Scanner(f);

        Book[] books = new Book[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String name = s.next();
            String subtitle = s.next();
            String ISBN = s.next();
            int pageCount = s.nextInt();
            String keywords = s.next();
            int languageID = s.nextInt();
            String createdOnStr = s.nextLine();
            createdOnStr = createdOnStr.substring(3);

            Calendar createdOn = Calendar.getInstance();
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            createdOn.setTime(date.parse(createdOnStr));

            Book book = new Book(ID, name, subtitle, ISBN, pageCount, keywords, languageID, createdOn);
            books[i] = book;
            i++;
        }
        s.close();

        return books;
    }

    /* adaugarea listei de autori pentru fiecare carte */
    public static Book[] readBooksAuthors() throws FileNotFoundException, ParseException {
        Book[] books = readBooks();
        Author[] authors = Author.readAuthors();

        File f = new File("./init/books-authors.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int bookID = s.nextInt();
            int authorID = Integer.parseInt(s.nextLine().substring(3));

            Book bookForID = getBookForID(books, bookID);
            if (bookForID != null) {
                Author authorForID = Author.getAuthorForID(authors, authorID);
                addAuthor(bookForID, authorForID);
            }
        }
        s.close();

        return books;
    }

    /* verifica existenta cartii cu ID-ul dat ca parametru in books */
    public static boolean bookExistsInArray(Book[] books, int ID) {
        for (Book b : books)
            if (b.getID() == ID)
                return true;
        return false;
    }

    /* adauga book in vectorul books daca nu exista deja */
    public static Book[] addBookToArray(Book[] books, Book book) {
        if (!bookExistsInArray(books, book.getID())) {
            books = Arrays.copyOf(books, books.length + 1);
            books[books.length - 1] = book;
        }
        return books;
    }

}