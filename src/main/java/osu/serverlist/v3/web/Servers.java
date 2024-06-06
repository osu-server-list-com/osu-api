package osu.serverlist.v3.web;

import java.sql.ResultSet;

import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.v3.helpers.ServerQuery;
import spark.Request;
import spark.Response;

public class Servers extends JsonProcessingRoute {

    public Servers() {
        super();
    }

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if (ob != null)
            return ob;

        try {
            String sqlQuery = "SELECT * FROM `un_servers`";

            if (request.queryParams("all") == null) {
                sqlQuery += " WHERE `visible` = 1";

                if(request.queryParams("featured") != null) {
                    if(request.queryParams("featured").equals("true")) 
                    sqlQuery += " AND `featured` = 1";
                    else return notFound("featured");
                }
            }

            if(request.queryParams("featured") != null && request.queryParams("all") != null) {
                if(request.queryParams("featured").equals("true")) 
                sqlQuery += " WHERE `featured` = 1";
                else return notFound("featured");
            }

            if (request.queryParams("sort") != null) {
                switch (request.queryParams("sort")) {
                    case "date":
                        sqlQuery += " ORDER BY `created` DESC";
                        break;
                    case "players":
                        sqlQuery += " ORDER BY `players` DESC";
                        break;
                    default:
                        return notFound("sort");
                }
            } else {
                sqlQuery += " ORDER BY `votes` DESC";
            }

            ResultSet serverResultSet = mysql.Query(sqlQuery);
            if (request.queryParams("detail") != null) {
                if (request.queryParams("detail").equals("lite")) {
                    return returnResponse(new ServerQuery().queryServerLite(mysql, serverResultSet));
                } else {
                    return notFound("detail");
                }
            } else {
                return returnResponse(new ServerQuery().queryServer(mysql, serverResultSet));
            }

        } catch (Exception e) {
            Flogger.instance.error(e);
            return internalError();
        }

    }

}
