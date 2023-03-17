import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Scanner;

public class PublishingRetailer {

    private final int ID;
    private final String name;
    private IPublishingArtifact[] publishingArtifacts;
    private Country[] countries;

    public PublishingRetailer(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public IPublishingArtifact[] getPublishingArtifacts() {
        return publishingArtifacts;
    }

    public Country[] getCountries() {
        return countries;
    }

    public void setPublishingArtifacts(IPublishingArtifact[] publishingArtifacts) {
        this.publishingArtifacts = publishingArtifacts;
    }

    public void setCountries(Country[] countries) {
        this.countries = countries;
    }

    @Override
    public String toString() {
        return "PublishingRetailer { " +
                "ID = " + getID() +
                ", name = " + getName() +
                ", countries = " + Arrays.toString(getCountries()) +
                " }";
    }

    /* adauga artifact in lista artifacts a retailerului */
    public static void addArtifact(PublishingRetailer retailer, IPublishingArtifact artifact) {
        IPublishingArtifact[] artifacts;
        if (retailer.getPublishingArtifacts() == null) {
            artifacts = new IPublishingArtifact[1];
            artifacts[0] = artifact;
            retailer.setPublishingArtifacts(artifacts);
        } else {
            artifacts = retailer.getPublishingArtifacts();
            artifacts = Arrays.copyOf(artifacts, artifacts.length + 1);
            artifacts[artifacts.length - 1] = artifact;
        }
        retailer.setPublishingArtifacts(artifacts);
    }

    /* adauga country in lista de tari a retailerului */
    public static void addCountry(PublishingRetailer retailer, Country country) {
        Country[] countries;
        if (retailer.getCountries() == null) {
            countries = new Country[1];
            countries[0] = country;
        } else {
            countries = retailer.getCountries();
            countries = Arrays.copyOf(countries, countries.length + 1);
            countries[countries.length - 1] = country;
        }
        retailer.setCountries(countries);
    }

    /* intoarce retailerul cu ID-ul dat ca parametru */
    public static PublishingRetailer getRetailerForID(PublishingRetailer[] retailers, int ID) {
        for (PublishingRetailer r : retailers) {
            if (r.getID() == ID)
                return r;
        }
        return null;
    }

    /* initializarea retailerilor (toate atributele mai putin vectorul de tari si cel de artifacts) */
    public static PublishingRetailer[] readPublishingRetailers() throws FileNotFoundException {
        File f = new File("./init/publishing-retailers.in");
        Scanner s = new Scanner(f);

        PublishingRetailer[] publishingRetailers = new PublishingRetailer[Administration.getNrOfLines(f)];
        int i = 0;

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int ID = s.nextInt();
            String name = s.nextLine();
            name = name.substring(3);

            PublishingRetailer publishingRetailer = new PublishingRetailer(ID, name);
            publishingRetailers[i] = publishingRetailer;
            i++;
        }
        s.close();

        return publishingRetailers;
    }

    /* adaugarea listei de tari pentru fiecare retailer */
    public static PublishingRetailer[] readPublishingRetailersCountries() throws FileNotFoundException {
        PublishingRetailer[] retailers = readPublishingRetailers();
        Country[] countries = Country.readCountries();

        File f = new File("./init/publishing-retailers-countries.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int retailerID = s.nextInt();
            int countryID = Integer.parseInt(s.nextLine().substring(3));

            PublishingRetailer retailerForID = getRetailerForID(retailers, retailerID);
            if (retailerForID != null) {
                Country countryForID = Country.getCountryForID(countries, countryID);
                addCountry(retailerForID, countryForID);
            }
        }
        s.close();

        return retailers;
    }

    /* adaugarea listei de carti pentru fiecare retailer (in vectorul de artifacts) */
    public static void readPublishingRetailersBooks(PublishingRetailer[] retailers, Book[] books)
            throws FileNotFoundException {
        File f = new File("./init/publishing-retailers-books.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int retailerID = s.nextInt();
            int bookID = Integer.parseInt(s.nextLine().substring(3));

            PublishingRetailer retailerForID = getRetailerForID(retailers, retailerID);
            if (retailerForID != null) {
                Book bookForID = Book.getBookForID(books, bookID);
                addArtifact(retailerForID, bookForID);
            }
        }
        s.close();
    }

    /* adaugarea listei de grupuri pentru fiecare retailer (in vectorul de artifacts) */
    public static void readPublishingRetailersGroups(PublishingRetailer[] retailers, EditorialGroup[] groups)
            throws FileNotFoundException {
        File f = new File("./init/publishing-retailers-editorial-groups.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int retailerID = s.nextInt();
            int groupID = Integer.parseInt(s.nextLine().substring(3));

            PublishingRetailer retailerForID = getRetailerForID(retailers, retailerID);
            if (retailerForID != null) {
                EditorialGroup groupForID = EditorialGroup.getGroupForID(groups, groupID);
                addArtifact(retailerForID, groupForID);
            }
        }
        s.close();
    }

    /* adaugarea listei de branduri pentru fiecare retailer (in vectorul de artifacts) */
    public static void readPublishingRetailersBrands(PublishingRetailer[] retailers, PublishingBrand[] brands)
            throws FileNotFoundException {
        File f = new File("./init/publishing-retailers-publishing-brands.in");
        Scanner s = new Scanner(f);

        s.nextLine();
        s.useDelimiter("###");
        while (s.hasNext()) {
            int retailerID = s.nextInt();
            int brandID = Integer.parseInt(s.nextLine().substring(3));

            PublishingRetailer retailerForID = getRetailerForID(retailers, retailerID);
            if (retailerForID != null) {
                PublishingBrand brandForID = PublishingBrand.getBrandForID(brands, brandID);
                addArtifact(retailerForID, brandForID);
            }
        }
        s.close();
    }

    /* initializeaza retailerii apeland functiile implementate anterior */
    public static PublishingRetailer[] initializeRetailers() throws FileNotFoundException, ParseException {
        PublishingRetailer[] retailers = readPublishingRetailersCountries();
        Book[] books = Book.readBooksAuthors();
        EditorialGroup[] groups = EditorialGroup.readEditorialGroupsBooks(books);
        PublishingBrand[] brands = PublishingBrand.readPublishingBrandBooks(books);

        readPublishingRetailersBooks(retailers, books);
        readPublishingRetailersGroups(retailers, groups);
        readPublishingRetailersBrands(retailers, brands);

        return retailers;
    }

}