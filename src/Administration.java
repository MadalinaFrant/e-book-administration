import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

public class Administration {

    /* returneaza nr. de linii din fisier (fara linia header) */
    public static int getNrOfLines(File f) throws FileNotFoundException {
        int n = 0;
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            s.nextLine();
            n++;
        }
        n--;
        s.close();
        return n;
    }

    /* intoarce toate cartile unui retailer cu ID-ul dat, astfel se vor lua cartile din
    artifacts (de tip Book si din campul books al group si brand) */
    public static Book[] getBooksForPublishingRetailerID(int publishingRetailerID)
            throws FileNotFoundException, ParseException {
        PublishingRetailer[] retailers = PublishingRetailer.initializeRetailers();
        PublishingRetailer retailer = PublishingRetailer.getRetailerForID(retailers, publishingRetailerID);
        if (retailer == null)
            return null;

        IPublishingArtifact[] artifacts = retailer.getPublishingArtifacts();
        if (artifacts == null)
            return null;

        Book[] books = new Book[0];

        for (IPublishingArtifact a : artifacts) {
            if (a instanceof Book) {
                books = Book.addBookToArray(books, (Book)a);
            }
            if (a instanceof EditorialGroup) {
                Book[] groupBooks = ((EditorialGroup)(a)).getBooks();
                for (Book b : groupBooks)
                    books = Book.addBookToArray(books, b);
            }
            if (a instanceof PublishingBrand) {
                Book[] brandBooks = ((PublishingBrand)(a)).getBooks();
                for (Book b : brandBooks)
                    books = Book.addBookToArray(books, b);
            }
        }

        return books;
    }

    /* intoarce lista de limbi pentru retailerul cu ID-ul dat, utilizand campul languageID al
    cartilor respective retailerului */
    public static Language[] getLanguagesForPublishingRetailerID(int publishingRetailerID)
            throws FileNotFoundException, ParseException {
        Book[] books = getBooksForPublishingRetailerID(publishingRetailerID);
        if (books == null)
            return null;

        Language[] languagesArray = Language.readLanguages();
        Language[] languages = new Language[0];

        for (Book b : books) {
            Language language = Language.getLanguageForID(languagesArray, b.getLanguageID());
            if (language != null)
                languages = Language.addLanguageToArray(languages, language);
        }

        return languages;
    }

    /* intoarce tarile in care a ajuns cartea cu ID-ul dat, verificand pentru fiecare
    retailer toate cartile, in cazul ID-ului corespunzator adaugand lista de tari a
    retailerului */
    public static Country[] getCountriesForBookID(int bookID)
            throws FileNotFoundException, ParseException {
        PublishingRetailer[] retailers = PublishingRetailer.initializeRetailers();
        Country[] countries = new Country[0];

        for (PublishingRetailer pr : retailers) {
            Book[] books = getBooksForPublishingRetailerID(pr.getID());
            if (books != null) {
                for (Book b : books) {
                    if (b.getID() == bookID)
                        countries = Country.addCountriesToArray(countries, pr.getCountries());
                }
            }
        }

        return countries;
    }

    /* intoarce cartile comune dintre retailerii cu ID-urile date (intersectia) */
    public static Book[] getCommonBooksForRetailerIDs(int retailerID1, int retailerID2)
            throws FileNotFoundException, ParseException {
        Book[] booksID1 = getBooksForPublishingRetailerID(retailerID1);
        Book[] booksID2 = getBooksForPublishingRetailerID(retailerID2);

        if (booksID1 == null || booksID2 == null)
            return null;

        Book[] common = new Book[0];

        for (Book b : booksID2)
            if (Book.bookExistsInArray(booksID1, b.getID()))
                common = Book.addBookToArray(common, b);

        return common;
    }

    /* intoarce toate cartile ale retailerilor cu ID-urile date (reuninunea) */
    public static Book[] getAllBooksForRetailerIDs(int retailerID1, int retailerID2)
            throws FileNotFoundException, ParseException {
        Book[] booksID1 = getBooksForPublishingRetailerID(retailerID1);
        Book[] booksID2 = getBooksForPublishingRetailerID(retailerID2);

        if (booksID1 == null || booksID2 == null)
            return null;

        Book[] union = new Book[0];

        for (Book b1 : booksID1)
            union = Book.addBookToArray(union, b1);

        for (Book b2 : booksID2)
            union = Book.addBookToArray(union, b2);

        return union;
    }

    /* metode de testare a metodelor implementate */

    public static void testGetBooksForPublishingRetailerID(int ID)
            throws FileNotFoundException, ParseException {
        Book[] books = getBooksForPublishingRetailerID(ID);
        if (books != null)
            for (Book b : books)
                System.out.println(b.Publish());
        System.out.println();
    }

    public static void testGetLanguagesForPublishingRetailerID(int ID)
            throws FileNotFoundException, ParseException {
        Language[] languages = getLanguagesForPublishingRetailerID(ID);
        if (languages != null)
            for (Language l : languages)
                System.out.println(l.toString());
        System.out.println();
    }

    public static void testGetCountriesForBookID(int ID)
            throws FileNotFoundException, ParseException {
        Country[] countries = getCountriesForBookID(ID);
        if (countries != null)
            for (Country c : countries)
                System.out.println(c.toString());
        System.out.println();
    }

    public static void testGetCommonBooksForRetailerIDs(int ID1, int ID2)
            throws FileNotFoundException, ParseException {
        Book[] common = getCommonBooksForRetailerIDs(ID1, ID2);
        if (common != null)
            for (Book b : common)
                System.out.println(b.Publish());
        System.out.println();
    }

    public static void testGetAllBooksForRetailerIDs(int ID1, int ID2)
            throws FileNotFoundException, ParseException {
        Book[] union = getAllBooksForRetailerIDs(ID1, ID2);
        if (union != null)
            for (Book b : union)
                System.out.println(b.Publish());
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        PublishingRetailer[] publishingRetailers = PublishingRetailer.initializeRetailers();
        for (PublishingRetailer p : publishingRetailers) {
            System.out.println(p.toString());
            for (IPublishingArtifact artifact : p.getPublishingArtifacts())
                System.out.println(artifact.Publish());
        }

        /* 5 teste pentru fiecare metoda */

        testGetBooksForPublishingRetailerID(1);
        testGetBooksForPublishingRetailerID(7);
        testGetBooksForPublishingRetailerID(8);
        testGetBooksForPublishingRetailerID(22);
        testGetBooksForPublishingRetailerID(33);

        testGetLanguagesForPublishingRetailerID(1);
        testGetLanguagesForPublishingRetailerID(7);
        testGetLanguagesForPublishingRetailerID(11);
        testGetLanguagesForPublishingRetailerID(24);
        testGetLanguagesForPublishingRetailerID(33);

        testGetCountriesForBookID(488);
        testGetCountriesForBookID(1115);
        testGetCountriesForBookID(5404);
        testGetCountriesForBookID(7944);
        testGetCountriesForBookID(14700);

        testGetCommonBooksForRetailerIDs(1, 3);
        testGetCommonBooksForRetailerIDs(7, 8);
        testGetCommonBooksForRetailerIDs(6, 11);
        testGetCommonBooksForRetailerIDs(5, 27);
        testGetCommonBooksForRetailerIDs(30, 33);

        testGetAllBooksForRetailerIDs(1, 3);
        testGetAllBooksForRetailerIDs(7, 8);
        testGetAllBooksForRetailerIDs(6, 11);
        testGetAllBooksForRetailerIDs(5, 27);
        testGetAllBooksForRetailerIDs(30, 33);

    }

}