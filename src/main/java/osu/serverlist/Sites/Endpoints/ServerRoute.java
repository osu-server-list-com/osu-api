package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commons.marcandreher.Engine.JsonProcessingRoute;
import commons.marcandreher.Utils.TimestampConverter;
import osu.serverlist.Input.Commands.ExceptionManager;
import osu.serverlist.Sites.Models.Server;
import osu.serverlist.Sites.Models.ServerHelper;
import spark.Request;
import spark.Response;

public class ServerRoute extends JsonProcessingRoute {

    private final String SQL_QUERY = "SELECT * FROM un_servers WHERE `id` = ?";
    private final String SQL_QUERY_CATEGORIES = "SELECT `name` FROM un_categories WHERE `id` = ?";

    public ServerRoute() {
        super();
        addRequiredParameter("id");
    }

    @Override
    public Object handle(Request request, Response response)  {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;
        Server server = null;

        ResultSet serverResultSet = mysql.Query(SQL_QUERY, request.queryParams("id"));
        try {
            while (serverResultSet.next()) {
                server = new Server();
                server.setName(serverResultSet.getString("name"));
                server.setId(serverResultSet.getInt("id"));
                server.setLogo_loc(ServerHelper.repairLogo(serverResultSet.getString("logo_loc")));
                server.setPlayers(serverResultSet.getInt("players"));
                server.setVotes(serverResultSet.getInt("votes"));
                server.setCreated(TimestampConverter.getDiffString(serverResultSet.getString("created")));
                server.setTimestamp(serverResultSet.getString("created"));
                server.setUrl(serverResultSet.getString("url"));
                server.setSafe_name(server.getName().toLowerCase().replaceAll("!", "").replaceAll(" ", ""));
                server.setSafe_categories(serverResultSet.getString("categories"));
                ArrayList<String> catList = new ArrayList<>();
                for (String categorieStr : server.getSafe_categories().split(",")) {
                    ResultSet catRs = mysql.Query(SQL_QUERY_CATEGORIES, categorieStr);
                    while (catRs.next()) {
                        catList.add(catRs.getString("name"));
                    }
                }
                server.setList_categories(catList);
                return returnResponse(server);
            }

        } catch (SQLException e) {
            ExceptionManager.addException(e);
            return internalError();
        }

        return notFound("id");

    }

}
