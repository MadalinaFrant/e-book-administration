import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Language {

    private final int ID;
    private final String cod, name;

    public Language(int ID, String cod, String name) {
        this.ID = ID;
        this.cod = cod;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getCod() {
        return cod;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getCod() + " (" + getName() + ")";
    }

    /* intoarce limba cu ID-ul dat ca parametru */
    public static Language getLanguageForID(Language[] languages, int ID) {
        for (Language l : languages)
            if (l.getID() == ID)
                return l;
        return null;
    }

    /* initializarea limbilor */
    public static Language[] readLanguages() throws FileNotFoundException {
        File f = new File("./init/languages.in");
        Scanner s = new Scanner(f);

        Language[] languages = new Language[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String cod = s.next();
            String name = s.nextLine();
            name = name.substring(3);

            Language language = new Language(ID, cod, name);
            languages[i] = language;
            i++;
        }
        s.close();

        return languages;
    }

    /* verifica existenta limbii cu ID-ul dat ca parametru in languages */
    public static boolean languageExistsInArray(Language[] languages, int ID) {
        for (Language l : languages)
            if (l.getID() == ID)
                return true;
        return false;
    }

    /* adauga language in vectorul languages daca nu exista deja */
    public static Language[] addLanguageToArray(Language[] languages, Language language) {
        if (!languageExistsInArray(languages, language.getID())) {
            languages = Arrays.copyOf(languages, languages.length + 1);
            languages[languages.length - 1] = language;
        }
        return languages;
    }

}