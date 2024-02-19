package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import commons.marcandreher.Engine.JsonProcessingRoute;
import spark.Request;
import spark.Response;

public class ChartDataVoteRoute extends JsonProcessingRoute {
    
    private final String WEEKLYVOTES_SQL = "SELECT DAYOFWEEK(d.date) AS `date`, IFNULL(COUNT(v.votetime), 0) AS count " +
			"FROM (SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) DAY AS date UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 1 DAY "
			+
			"UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 2 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 3 DAY "
			+
			"UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 4 DAY UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 5 DAY "
			+
			"UNION SELECT CURDATE() - INTERVAL WEEKDAY(CURDATE()) - 6 DAY) AS d " +
			"LEFT JOIN un_votes v ON DATE(v.votetime) = d.date AND v.srv_id = ? " +
			"GROUP BY d.date ORDER BY d.date;";

	private final String MONTHLYVOTES_SQL = "SELECT m.month AS `date`, IFNULL(COUNT(v.votetime), 0) AS count " +
			"FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 " +
			"UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 " +
			"UNION SELECT 11 UNION SELECT 12) AS m " +
			"LEFT JOIN un_votes v ON MONTH(v.votetime) = m.month AND YEAR(v.votetime) = YEAR(CURDATE()) AND v.srv_id = ? "
			+
			"GROUP BY m.month ORDER BY m.month;";


    public ChartDataVoteRoute() {
        super();
        addRequiredParameter("id");
        addRequiredParameter("type");
    }

     @Override
    public Object handle(Request request, Response response)  {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;

        ResultSet serverResultSet;

        switch(request.queryParams("type")) {
            case "WEEKLY":
                serverResultSet = mysql.Query(WEEKLYVOTES_SQL, request.queryParams("id"));
                break;
            case "MONTHLY":
                serverResultSet = mysql.Query(MONTHLYVOTES_SQL, request.queryParams("id"));
                break;
            default:
                return customError("no valid type");
        }

        List<ChartData> data = new ArrayList<>();
        try {

            while (serverResultSet.next()) {
                ChartData chartData = new ChartData(serverResultSet.getString("date"), serverResultSet.getInt("count"));
                data.add(chartData);
            }
            return returnResponse(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
