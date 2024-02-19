package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import commons.marcandreher.Engine.JsonProcessingRoute;
import spark.Request;
import spark.Response;

public class ChartDataRoute extends JsonProcessingRoute {
    public final String CHECKFORCRAWLSTAT_SQL = "SELECT d.date, IFNULL(`value`, 0) AS value FROM (SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) DAY AS date UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 1 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 2 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 3 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 4 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 5 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 6 DAY) AS d LEFT JOIN un_crawler v ON DATE(v.date) = d.date AND v.srv_id = ? AND `type` = ? ORDER BY d.date;";

    public ChartDataRoute() {
        super();
        addRequiredParameter("id");
        addRequiredParameter("type");
    }

     @Override
    public Object handle(Request request, Response response)  {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;

        ResultSet serverResultSet = mysql.Query(CHECKFORCRAWLSTAT_SQL, request.queryParams("id"), request.queryParams("type"));
        List<ChartData> data = new ArrayList<>();
        try {

            while (serverResultSet.next()) {
                ChartData chartData = new ChartData(serverResultSet.getString("date"), serverResultSet.getInt("value"));
                data.add(chartData);
            }
            return returnResponse(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
