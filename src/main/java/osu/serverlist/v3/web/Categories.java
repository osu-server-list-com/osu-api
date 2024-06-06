package osu.serverlist.v3.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.Cache.CategoriesCh;
import osu.serverlist.Sites.Models.Categorie;
import osu.serverlist.v3.models.CategorieModel;
import spark.Request;
import spark.Response;

public class Categories extends JsonProcessingRoute {

    public Categories() {
        super();
    }

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if (ob != null)
            return ob;

        try {
            List<CategorieModel> categories = new ArrayList<>();
            for (Map.Entry<Integer, Categorie> cat : CategoriesCh.catMap.entrySet()) {
                CategorieModel c = new CategorieModel();
                c.setId(cat.getValue().getId());
                c.setName(cat.getValue().getName());
                categories.add(c);
            }

            return returnResponse(categories);

        } catch (Exception e) {
            Flogger.instance.error(e);
            return internalError();
        }

    }

}
