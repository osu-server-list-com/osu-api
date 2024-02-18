package osu.serverlist.Sites.Endpoints;



import commons.marcandreher.Engine.JsonProcessingRoute;
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
            return returnResponse(RefreshHeatmap.cacheItems.get(Integer.parseInt(request.queryParams("id"))).dateItems);
        } catch (Exception e) {
            e.printStackTrace();
           return customError("Invalid server id");
          
        }
    
  
    }
    

    
}
