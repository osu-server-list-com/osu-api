package osu.serverlist.Sites.Endpoints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.Cache.RefreshHeatmap;
import osu.serverlist.Sites.Models.HeatMapStat;
import osu.serverlist.Sites.Models.HeatMapStats;
import spark.Request;
import spark.Response;

public class ChartHeatMapRoute extends JsonProcessingRoute {

    public ChartHeatMapRoute() {
        super();
        addRequiredParameter("id");
    }

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if (ob != null)
            return ob;

        try {
            String year = null;
            if (request.queryParams("year") != null) {
                year = request.queryParams("year");
            } else {
                year = String.valueOf(java.time.Year.now());
            }

            String nextYear = String.valueOf(Integer.parseInt(year) + 1);
            String previousYear = String.valueOf(Integer.parseInt(year) - 1);

            boolean hasNextPage = false;
            boolean hasPreviousPage = false;

            HeatMapResponse heatMapResponse = new HeatMapResponse();

            ConcurrentHashMap<String, HeatMapStats> dateItems = RefreshHeatmap.cacheItems
                    .get(Integer.parseInt(request.queryParams("id"))).dateItems;
            ConcurrentHashMap<String, HeatMapStats> responseList = new ConcurrentHashMap<>();
            for (Entry<String, HeatMapStats> stats : dateItems.entrySet()) {

                if (stats.getKey().startsWith(nextYear)) {
                    hasNextPage = true;
                }else if (stats.getKey().startsWith(previousYear)) {
                    hasPreviousPage = true;
                }

                if (stats.getKey().startsWith(year)) {
                    HeatMapStat heatMapStat = new HeatMapStat();
                    heatMapStat.dateItems.put(stats.getKey(), stats.getValue());
                    responseList.put(stats.getKey(), heatMapStat.dateItems.get(stats.getKey()));
                }
            }
            heatMapResponse.currentYear = year;
            heatMapResponse.hasNextPage = hasNextPage;
            heatMapResponse.hasPreviousPage = hasPreviousPage;
            heatMapResponse.response = responseList;

            try {
                return returnResponse(heatMapResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return customError("Invalid server id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";

    }

    public class HeatMapResponse {
        public ConcurrentHashMap<String, HeatMapStats> response;
        public boolean hasNextPage = false;
        public boolean hasPreviousPage = false;
        public String currentYear;
    }

}
