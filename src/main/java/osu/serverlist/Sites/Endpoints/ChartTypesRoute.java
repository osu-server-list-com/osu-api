package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import commons.marcandreher.Engine.JsonProcessingRoute;
import spark.Request;
import spark.Response;

public class ChartTypesRoute extends JsonProcessingRoute {
    public final String CHECKFORCRAWLSTATS_SQL = "SELECT `srv_id`, `type` FROM `un_crawler` WHERE `srv_id` = ? GROUP BY `type`";

    public ChartTypesRoute() {
        super();
        addRequiredParameter("id");
    }

    @Override
    public Object handle(Request request, Response response)  {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;

        ResultSet serverResultSet = mysql.Query(CHECKFORCRAWLSTATS_SQL, request.queryParams("id"));
        List<String> types = new ArrayList<>();
        try {
            while (serverResultSet.next()) {
                types.add(serverResultSet.getString("type"));
            }
            return returnResponse(types);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
