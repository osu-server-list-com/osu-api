package osu.serverlist.v3.web;

import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.v3.helpers.ServerQuery;
import osu.serverlist.v3.models.ServerModel;
import spark.Request;
import spark.Response;

public class Server extends JsonProcessingRoute {

    public Server() {
        super();
        addRequiredParameter("id");
    }

    @Override
    public Object handle(Request request, Response response)  {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;
        
        try {
                ServerModel server = new ServerQuery().queryServer(Integer.parseInt(request.queryParams("id")), mysql);
                if(server == null) return notFound("id");
                return returnResponse(server);
            

        } catch (Exception e) {
            Flogger.instance.error(e);
            return internalError();
        }


    }
    
}
