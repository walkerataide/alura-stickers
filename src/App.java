import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {
    //APIs' URLs used to get the data
    private static final String TOP_MOVIES_URL = "https://raw.githubusercontent.com/alura-cursos/imersao-java/api/TopMovies.json";
    private static final String MOST_POPULAR_MOVIES_URL = "https://raw.githubusercontent.com/alura-cursos/imersao-java/api/MostPopularMovies.json";
    private static final String TOP_TVS_URL = "https://raw.githubusercontent.com/alura-cursos/imersao-java/api/TopTVs.json";
    private static final String MOST_POPULAR_TVS_URL = "https://raw.githubusercontent.com/alura-cursos/imersao-java/api/MostPopularTVs.json";

    public static void main(String[] args) throws Exception {
        // do http connection and get the json from the URLs
        String topMoviesBody = doRequest(TOP_MOVIES_URL);
        String mostPopMoviesBody = doRequest(MOST_POPULAR_MOVIES_URL);
        String topTvsBody = doRequest(TOP_TVS_URL);
        String mostPopTvsBody = doRequest(MOST_POPULAR_TVS_URL);

        // get only the data we want 
        List<Map<String, String>> topMoviesList = parseJsonToString(topMoviesBody);
        List<Map<String, String>> mostPopMoviesList = parseJsonToString(mostPopMoviesBody);
        List<Map<String, String>>  topTvsList = parseJsonToString(topTvsBody);
        List<Map<String, String>> mostPopTvsList = parseJsonToString(mostPopTvsBody);

        // shows and manipulate the data (title, image (poster) and rating)
        printImDbList("IMDB Top Movies List", topMoviesList);
        printImDbList("IMDB Most Popular Movies", mostPopMoviesList);
        printImDbList("IMDB Top Tvs Series List", topTvsList);
        printImDbList("IMDB Most Popular Tvs Series List", mostPopTvsList);
        
    }

    /** connnect to the http server and do a GET request. Returns the body of http response */
    private static String doRequest(String url) throws IOException, InterruptedException {
        var address = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(address).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();
    }

    /** parse the json to String Map using the JsonParser class*/
    private static List<Map<String, String>> parseJsonToString(String body) {
        var parser = new JsonParser();
        List<Map<String, String>> jsonAttributesList = parser.parse(body);
        return jsonAttributesList;
    }

    /** print on console the IMDB Shows List passed as argument*/
    private static void printImDbList(String title, List<Map<String, String>> imDbShowsList) {
        /* print the title of the list. The colors are set using true color 24bits. 
           SGR code:38;2;⟨red⟩;⟨green⟩;⟨blue⟩ for text color and 48;2;⟨red⟩;⟨green⟩;⟨blue⟩ for background color */
        System.out.println("\n\u001b[38;2;0;0;0;1m\u001b[48;2;42;122;228m ### " + title + " ###\u001b[m");
        for (Map<String,String> show : imDbShowsList) {
            double rating = Double.parseDouble(show.get("imDbRating"));

            /*  print the Show's Attributes. The colors are set using  8 bits SGR code.
                SGR code:38;2;⟨n⟩ for text color and 48;2;⟨n⟩ for background color. Except rating*/
            System.out.println("Title:\u001b[1m" + show.get("title") + "\u001b[m"); //
            System.out.println("Poster Link:" + show.get("image"));
            System.out.println("\u001b[48;2;255;233;6mRating:" + rating + "\u001b[m");
            for (int i = 0; i <= (int)rating; i++) {
                System.out.print("⭐️");
            }
            System.out.println("\n");
        }
    }
}
