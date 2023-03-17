import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Author {

    private final int ID;
    private final String firstName, lastName;

    public Author(int ID, String firstName, String lastName) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    /* intoarce autorul cu ID-ul dat ca parametru */
    public static Author getAuthorForID (Author[] authors, int ID) {
        for (Author a : authors)
            if (a.getID() == ID)
                return a;
        return null;
    }

    /* initializarea autorilor */
    public static Author[] readAuthors() throws FileNotFoundException {
        File f = new File("./init/authors.in");
        Scanner s = new Scanner(f);

        Author[] authors = new Author[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String firstName = s.next();
            String lastName = s.nextLine();
            lastName = lastName.substring(3);

            Author author = new Author(ID, firstName, lastName);
            authors[i] = author;
            i++;
        }
        s.close();

        return authors;
    }

}