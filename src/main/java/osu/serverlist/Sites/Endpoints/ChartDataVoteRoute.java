package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import commons.marcandreher.Engine.JsonProcessingRoute;
import spark.Request;
import spark.Response;

public class ChartDataVoteRoute extends JsonProcessingRoute {

    private final String WEEKLYVOTES_SQL = "SELECT DAYOFWEEK(d.date) AS `date`, IFNULL(COUNT(v.votetime), 0) AS count FROM ( SELECT CURDATE() - INTERVAL 6 DAY AS date UNION SELECT CURDATE() - INTERVAL 5 DAY UNION SELECT CURDATE() - INTERVAL 4 DAY UNION SELECT CURDATE() - INTERVAL 3 DAY UNION SELECT CURDATE() - INTERVAL 2 DAY UNION SELECT CURDATE() - INTERVAL 1 DAY UNION SELECT CURDATE() ) AS d LEFT JOIN un_votes v ON DATE(v.votetime) = d.date AND v.srv_id = ? GROUP BY d.date ORDER BY d.date;";

    private final String YEARLYVOTES_SQL = "WITH RECURSIVE months AS (SELECT 0 AS offset UNION ALL SELECT offset + 1 FROM months WHERE offset < 11) SELECT CASE WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 1 THEN 'January' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 2 THEN 'February' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 3 THEN 'March' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 4 THEN 'April' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 5 THEN 'May' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 6 THEN 'June' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 7 THEN 'July' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 8 THEN 'August' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 9 THEN 'September' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 10 THEN 'October' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 11 THEN 'November' WHEN ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 = 12 THEN 'December' END AS `date`, IFNULL(COUNT(v.votetime), 0) AS `count` FROM months AS m LEFT JOIN un_votes v ON MONTH(v.votetime) = ((MONTH(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) - 1 + 12) % 12) + 1 AND YEAR(v.votetime) = YEAR(DATE_SUB(CURDATE(), INTERVAL m.offset MONTH)) AND v.srv_id = ? GROUP BY `date` ORDER BY FIELD(`date`, 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January');";

    public ChartDataVoteRoute() {
        super();
        addRequiredParameter("id");
        addRequiredParameter("type");
    }

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if (ob != null)
            return ob;

        ResultSet serverResultSet;

        try {

            switch (request.queryParams("type")) {
                case "WEEKLY":
                    serverResultSet = mysql.Query(WEEKLYVOTES_SQL, request.queryParams("id"));
                    break;
                case "YEARLY":
                    serverResultSet = mysql.Query(YEARLYVOTES_SQL, request.queryParams("id"));
                    break;
                default:
                    return customError("no valid type");
            }

            List<ChartData> data = new ArrayList<>();

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
