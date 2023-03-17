import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class EditorialGroup implements IPublishingArtifact {

    private final int ID;
    private final String name;
    private Book[] books;

    public EditorialGroup(int ID, String name) {
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

    /* adauga book in lista books a editorial group */
    public static void addBook(EditorialGroup group, Book book) {
        Book[] books;
        if (group.getBooks() == null) {
            books = new Book[1];
            books[0] = book;
        } else {
            books = group.getBooks();
            books = Arrays.copyOf(books, books.length + 1);
            books[books.length - 1] = book;
        }
        group.setBooks(books);
    }

    public String Publish() {
        String str = "";

        str += "<xml>\n" +
                "\t<editorialGroup>\n" +
                "\t\t<ID>" + getID() + "</ID>\n" +
                "\t\t<Name>" + getName() + "</Name>\n" +
                "\t</editorialGroup>\n";

        str += "\t<books>\n";
        StringBuilder booksStr = new StringBuilder();
        for (Book b : getBooks())
            booksStr.append("\t\t<book>\n").append(b.printBook(3)).append("\t\t</book>\n");

        str += booksStr + "\t</books>\n</xml>\n";

        return str;
    }

    /* intoarce grupul cu ID-ul dat ca parametru */
    public static EditorialGroup getGroupForID(EditorialGroup[] groups, int ID) {
        for (EditorialGroup g : groups) {
            if (g.getID() == ID)
                return g;
        }
        return null;
    }

    /* initializarea grupurilor (toate atributele mai putin vectorul de carti) */
    public static EditorialGroup[] readEditorialGroups() throws FileNotFoundException {
        File f = new File("./init/editorial-groups.in");
        Scanner s = new Scanner(f);

        EditorialGroup[] editorialGroups = new EditorialGroup[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String name = s.nextLine();
            name = name.substring(3);

            EditorialGroup editorialGroup = new EditorialGroup(ID, name);
            editorialGroups[i] = editorialGroup;
            i++;
        }
        s.close();

        return editorialGroups;
    }

    /* adaugarea listei de carti pentru fiecare grup */
    public static EditorialGroup[] readEditorialGroupsBooks(Book[] books)
            throws FileNotFoundException {
        EditorialGroup[] groups = readEditorialGroups();

        File f = new File("./init/editorial-groups-books.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int groupID = s.nextInt();
            int bookID = Integer.parseInt(s.nextLine().substring(3));

            EditorialGroup groupForID = getGroupForID(groups, groupID);
            if (groupForID != null) {
                Book bookForID = Book.getBookForID(books, bookID);
                addBook(groupForID, bookForID);
            }
        }
        s.close();

        return groups;
    }

}