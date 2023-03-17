import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class PublishingBrand implements IPublishingArtifact {

    private final int ID;
    private final String name;
    private Book[] books;

    public PublishingBrand(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Book[] getBooks() {
        return books;
    }

    public void setBooks(Book[] books) {
        this.books = books;
    }

    /* adauga book in lista books a publishing brand */
    public static void addBook(PublishingBrand brand, Book book) {
        Book[] books;
        if (brand.getBooks() == null) {
            books = new Book[1];
            books[0] = book;
        } else {
            books = brand.getBooks();
            books = Arrays.copyOf(books, books.length + 1);
            books[books.length - 1] = book;
        }
        brand.setBooks(books);
    }

    public String Publish() {
        String str = "";

        str += "<xml>\n" +
                "\t<publishingBrand>\n" +
                "\t\t<ID>" + getID() + "</ID>\n" +
                "\t\t<Name>" + getName() + "</Name>\n" +
                "\t</publishingBrand>\n";

        str += "\t<books>\n";
        StringBuilder booksStr = new StringBuilder();
        for (Book b : getBooks())
            booksStr.append("\t\t<book>\n").append(b.printBook(3)).append("\t\t</book>\n");

        str += booksStr + "\t</books>\n</xml>\n";

        return str;
    }

    /* intoarce brandul cu ID-ul dat ca parametru */
    public static PublishingBrand getBrandForID(PublishingBrand[] brands, int ID) {
        for (PublishingBrand b : brands) {
            if (b.getID() == ID)
                return b;
        }
        return null;
    }

    /* initializarea brandurilor (toate atributele mai putin vectorul de carti) */
    public static PublishingBrand[] readPublishingBrands() throws FileNotFoundException {
        File f = new File("./init/publishing-brands.in");
        Scanner s = new Scanner(f);

        PublishingBrand[] publishingBrands = new PublishingBrand[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String name = s.nextLine();
            name = name.substring(3);

            PublishingBrand publishingBrand = new PublishingBrand(ID, name);
            publishingBrands[i] = publishingBrand;
            i++;
        }
        s.close();

        return publishingBrands;
    }

    /* adaugarea listei de carti pentru fiecare brand */
    public static PublishingBrand[] readPublishingBrandBooks(Book[] books)
            throws FileNotFoundException {
        PublishingBrand[] brands = readPublishingBrands();

        File f = new File("./init/publishing-brands-books.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int brandID = s.nextInt();
            int bookID = Integer.parseInt(s.nextLine().substring(3));

            PublishingBrand brandForID = getBrandForID(brands, brandID);
            if (brandForID != null) {
                Book bookForID = Book.getBookForID(books, bookID);
                addBook(brandForID, bookForID);
            }
        }
        s.close();

        return brands;
    }

}