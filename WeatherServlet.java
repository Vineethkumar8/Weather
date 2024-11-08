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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");

        // Assume we have latitude and longitude after calling OpenCage API
        double lat = 17.387140; // Example: Hyderabad latitude
        double lon = 78.491684; // Example: Hyderabad longitude

        // Call Open-Meteo API for weather data
        String apiUrl = 
       "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&current_weather=true";
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
        } finally {
            httpClient.close();
        }
    }
}
