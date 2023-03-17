import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Country {

    private final int ID;
    private final String countryCode;

    public Country(int ID, String countryCode) {
        this.ID = ID;
        this.countryCode = countryCode;
    }

    public int getID() {
        return ID;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String toString() {
        return getCountryCode();
    }

    /* intoarce tara cu ID-ul dat ca parametru */
    public static Country getCountryForID (Country[] countries, int ID) {
        for (Country c : countries)
            if (c.getID() == ID)
                return c;
        return null;
    }

    /* initializarea tarilor */
    public static Country[] readCountries() throws FileNotFoundException {
        File f = new File("./init/countries.in");
        Scanner s = new Scanner(f);

        Country[] countries = new Country[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String countryCode = s.nextLine();
            countryCode = countryCode.substring(3);

            Country country = new Country(ID, countryCode);
            countries[i] = country;
            i++;
        }
        s.close();

        return countries;
    }

    /* adauga (concateneaza) vectorul tarilor de adaugat (toAdd) la countries */
    public static Country[] addCountriesToArray(Country[] countries, Country[] toAdd) {
        int len = countries.length;
        countries = Arrays.copyOf(countries, countries.length + toAdd.length);
        System.arraycopy(toAdd, 0, countries, len, toAdd.length);
        return countries;
    }

}