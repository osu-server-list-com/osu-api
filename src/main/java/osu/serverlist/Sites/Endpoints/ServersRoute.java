package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import commons.marcandreher.Engine.JsonProcessingRoute;
import commons.marcandreher.Utils.TimestampConverter;
import osu.serverlist.Sites.Models.Server;
import spark.Request;
import spark.Response;

public class ServersRoute extends JsonProcessingRoute {

    private final String SQL_QUERY = "SELECT * FROM un_servers WHERE `id` = ?";
    private final String SQL_QUERY_CATEGORIES = "SELECT `name` FROM un_categories WHERE `id` = ?";

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;

        ArrayList<Server> serverList = new ArrayList<>();

        ResultSet serverResultSet = mysql.Query(SQL_QUERY);
        try {
            while (serverResultSet.next()) {
                Server v = new Server();
                v.setName(serverResultSet.getString("name"));
                v.setId(serverResultSet.getInt("id"));
                v.setLogo_loc(serverResultSet.getString("logo_loc"));
                v.setPlayers(serverResultSet.getInt("players"));
                v.setVotes(serverResultSet.getInt("votes"));
                v.setCreated(TimestampConverter.getDiffString(serverResultSet.getString("created")));
                v.setTimestamp(serverResultSet.getString("created"));
                v.setUrl(serverResultSet.getString("url"));
                v.setSafe_name(v.getName().toLowerCase().replaceAll("!", "").replaceAll(" ", ""));
                v.setSafe_categories(serverResultSet.getString("categories"));
                ArrayList<String> catList = new ArrayList<>();
                for (String str : v.getSafe_categories().split(",")) {
                    ResultSet catRs = mysql.Query(SQL_QUERY_CATEGORIES, str);
                    while (catRs.next()) {
                        catList.add(catRs.getString("name"));
                    }
                }
                v.setList_categories(catList);
                serverList.add(v);
            }
            return returnResponse(serverList);
        } catch (SQLException e) {
            return internalDbError();
        } catch (Exception e) {
            return internalError();
        }
    }
}
