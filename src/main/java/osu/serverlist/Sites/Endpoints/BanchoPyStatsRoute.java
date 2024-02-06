package osu.serverlist.Sites.Endpoints;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.Sites.Models.BanchoPyStats;
import spark.Request;
import spark.Response;

public class BanchoPyStatsRoute extends JsonProcessingRoute {

    private final String SQL_QUERY_STATS = "SELECT * FROM `un_endpoints` WHERE `apitype` = 'BANCHOPY' AND `type` = 'CUSTOM' AND `srv_id` = ?";

    public BanchoPyStatsRoute() {
        super();
        addRequiredParameter("id");
    }

   

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if(ob != null) return ob;
        ResultSet catRs = mysql.Query(SQL_QUERY_STATS, request.queryParams("id"));

        try {
            BanchoPyStats stats = new BanchoPyStats();
            if (catRs.next()) {
                    String baseUrl = catRs.getString("endpoint");
                    
                    CompletableFuture<String> scoresFuture = sendGetRequest(baseUrl + "/v2/scores");
                    CompletableFuture<String> mapsFuture = sendGetRequest(baseUrl + "/v2/maps");
                    CompletableFuture<String> clansFuture = sendGetRequest(baseUrl + "/v2/clans");

                    JSONObject scoresJson = parseJsonResponse(scoresFuture.get());
                    JSONObject mapsJson = parseJsonResponse(mapsFuture.get());
                    JSONObject clansJson = parseJsonResponse(clansFuture.get());

                    JSONObject scoresMeta = (JSONObject) scoresJson.get("meta");
                    JSONObject mapsMeta = (JSONObject) mapsJson.get("meta");
                    JSONObject clansMeta = (JSONObject) clansJson.get("meta");

                    stats.setPlays(((Long) scoresMeta.get("total")).intValue());
                    stats.setMaps(((Long) mapsMeta.get("total")).intValue());
                    stats.setClans(((Long) clansMeta.get("total")).intValue());
                    stats.setId(Integer.parseInt(request.queryParams("id")));

                    return returnResponse(stats);
                } else {
                   return customError("no banchopy");

                }
            } catch (Exception e) {
                return internalError();
            }

    }

    private static final HttpClient httpClient;

    static {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public static CompletableFuture<String> sendGetRequest(String apiUrl) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("User-Agent", "osu!ListBot")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    

    public static JSONObject parseJsonResponse(String jsonResponse) throws Exception {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(jsonResponse);
    }
}
