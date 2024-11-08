import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {

    // Replace with your OpenCage API key
    private static final String OPEN_CAGE_API_KEY = "bb39470317a1463b899e69e656c7f690";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");

        // Get latitude and longitude for the city using OpenCage API
        double lat = 0;
        double lon = 0;

        // Call OpenCage API to get coordinates based on city name
        try {
            String geocodingApiUrl = "https://api.opencagedata.com/geocode/v1/json?q=" + city + "&key=" + OPEN_CAGE_API_KEY;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet geocodeRequest = new HttpGet(geocodingApiUrl);

            String result = EntityUtils.toString(httpClient.execute(geocodeRequest).getEntity());
            JSONObject geocodeData = new JSONObject(result);

            if (geocodeData.getJSONArray("results").length() > 0) {
                // Extract latitude and longitude from the first result
                JSONObject firstResult = geocodeData.getJSONArray("results").getJSONObject(0);
                lat = firstResult.getJSONObject("geometry").getDouble("lat");
                lon = firstResult.getJSONObject("geometry").getDouble("lng");
            } else {
                response.getWriter().write("City not found");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error fetching coordinates for city: " + city);
            return;
        }

        // Now use lat and lon to get weather data from Open-Meteo API
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&current_weather=true";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet requestWeather = new HttpGet(apiUrl);

        String result = " ";
        try {
            result = EntityUtils.toString(httpClient.execute(requestWeather).getEntity());
            JSONObject weatherData = new JSONObject(result).getJSONObject("current_weather");

            // Send data to the JSP page
            request.setAttribute("temperature", weatherData.getDouble("temperature"));
            request.setAttribute("windspeed", weatherData.getDouble("windspeed"));
            request.getRequestDispatcher("result.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error fetching weather data");
        } finally {
            httpClient.close();
        }
    }
}
