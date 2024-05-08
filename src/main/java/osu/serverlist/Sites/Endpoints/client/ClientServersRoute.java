package osu.serverlist.Sites.Endpoints.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import commons.marcandreher.Engine.JsonProcessingRoute;
import commons.marcandreher.Utils.TimestampConverter;
import osu.serverlist.Cache.ClientKeys;
import osu.serverlist.Sites.Models.ClientServer;
import osu.serverlist.Sites.Models.ServerHelper;
import spark.Request;
import spark.Response;

public class ClientServersRoute extends JsonProcessingRoute {

    private final String SQL_QUERY = "SELECT * FROM `un_servers` WHERE `visible` = 1";

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;

        if(request.queryParams("key") == null) return customError("No key provided");

        if(!(ClientKeys.keys.contains(request.queryParams("key")))) return customError("Invalid key");

        ArrayList<ClientServer> serverList = new ArrayList<>();

        ResultSet serverResultSet = mysql.Query(SQL_QUERY);
        try {
            while (serverResultSet.next()) {
                ClientServer v = new ClientServer();
                v.setName(serverResultSet.getString("name"));
                v.setId(serverResultSet.getInt("id"));
                v.setImage(ServerHelper.repairLogo(serverResultSet.getString("logo_loc")));
                v.setPlayers(serverResultSet.getInt("players"));
                v.setVotes(serverResultSet.getInt("votes"));
                v.setTimestamp(TimestampConverter.getDiffString(serverResultSet.getString("created")));
                v.setDevserver(serverResultSet.getString("devserver"));
                v.setUrl("https://" + serverResultSet.getString("url"));

                v.setSafe_name(v.getName().toLowerCase().replaceAll(" ", ""));
               
                serverList.add(v);
            }
            return returnResponse(serverList);
        } catch (SQLException e) {
            return internalDbError();
        } catch (Exception e) {
            e.printStackTrace();
            return internalError();
        }
    }
}
