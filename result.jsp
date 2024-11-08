<html>
<head>
    <title>Weather Results</title>
</head>
<body>
    <h1>Weather for <%= request.getParameter("city") %></h1>
    <p>Temperature: <%= request.getAttribute("temperature") %> °C</p>
    <p>Wind Speed: <%= request.getAttribute("windspeed") %> m/s</p>
    <a href="index.html">Check another city</a>
</body>
</html>

