package osu.serverlist.Sites.Endpoints;


import com.fasterxml.jackson.core.JsonProcessingException;

import commons.marcandreher.Engine.JsonProcessingRoute;
import commons.marcandreher.Utils.ListUtils;
import osu.serverlist.Cache.RefreshHeatmap;
import spark.Request;
import spark.Response;

public class ChartHeatMapRoute extends JsonProcessingRoute {

   
    public ChartHeatMapRoute() {
        super();
        addRequiredParameter("id");
    }

     @Override
    public Object handle(Request request, Response response)  {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;
        try {
            ListUtils.printHashMap(RefreshHeatmap.cacheItems);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            return returnResponse(RefreshHeatmap.cacheItems.get(Integer.parseInt(request.queryParams("id"))).dateItems);
        } catch (Exception e) {
            e.printStackTrace();
           return customError(e.getMessage());
          
        }
    
  
    }
    

    
}
