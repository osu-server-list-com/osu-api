package osu.serverlist.Sites.Endpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.Sites.Models.Categorie;
import spark.Request;
import spark.Response;

public class CategoriesRoute extends JsonProcessingRoute {

    private final String SQL_QUERY_CATEGORIES = "SELECT * FROM un_categories";

    public CategoriesRoute() {
        super();
    }

    @Override
    public Object handle(Request request, Response response) {
        response.type("application/json");

        ResultSet catRs = mysql.Query(SQL_QUERY_CATEGORIES);

        ArrayList<Categorie> catList = new ArrayList<>();
        try {
            while (catRs.next()) {
                Categorie cat = new Categorie();
                cat.setColor(catRs.getString("color"));
                cat.setId(catRs.getInt("id"));
                cat.setName(catRs.getString("name"));
                catList.add(cat);
            }
            return returnResponse(catList);
        } catch (SQLException e) {
            return internalDbError();
        } catch (Exception e) {
            return internalError();
        }

    }

}
