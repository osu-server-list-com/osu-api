package osu.serverlist.v3.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Commons.Flogger.Prefix;
import commons.marcandreher.Engine.JsonProcessingRoute;
import osu.serverlist.v3.helpers.StatisticsHelper;
import osu.serverlist.v3.models.AdminModel;
import osu.serverlist.v3.models.ServerStat;
import spark.Request;
import spark.Response;

public class Admin extends JsonProcessingRoute {

    private String GET_LAST_VOTES_SQL = "SELECT `srv_username` FROM `un_votes` WHERE `srv_id` = ? ORDER BY `un_votes`.`votetime` DESC LIMIT 10;";
    
    public Admin() {
        super();
        addRequiredParameter("id");
        addRequiredParameter("apikey");
    }

    @Override
    public Object handle(Request request, Response response) {
        Object ob = super.handle(request, response);
        if (ob != null)
            return ob;

        try {

            String apikey = getAPIKey(request, request.queryParams("id"));

            if(apikey == null) {
                return customError("server_not_found");
            }

            if(!apikey.equals(request.queryParams("apikey"))) {
                return customError("unauthorized");
            }else {
                Flogger.instance.log(Prefix.API, "Authorized [" + request.queryParams("id") + "]", 0);
            }

            AdminModel adminModel = new AdminModel();

            List<String> last_voters = new ArrayList<>();

            ResultSet lastVotersRs = mysql.Query(GET_LAST_VOTES_SQL, request.queryParams("id"));
            while (lastVotersRs.next()) {
                last_voters.add(lastVotersRs.getString("srv_username"));
            }



            adminModel.setLast_voters(last_voters);

            StatisticsHelper sh = new StatisticsHelper(mysql, Flogger.instance);
            ServerStat stats = sh.getStatisticsForServer(Integer.parseInt(request.queryParams("id")));

            HashMap<String, Integer> stats_today = new HashMap<>();
            stats_today.put("site_clicks", stats.getTodayOsuServerLinkClicks());
            stats_today.put("discord_clicks", stats.getTodaydiscordServerLinkClicks());
            stats_today.put("vote_site_views", stats.getTodayServerVoteSiteClicks());
            stats_today.put("server_site_views", stats.getTodayServerSiteClicks());
            adminModel.setStats_today(stats_today);

            HashMap<String, Integer> stats_last14days = new HashMap<>();
            stats_last14days.put("site_clicks", stats.getLast14thDaysOsuServerLinkClicks());
            stats_last14days.put("discord_clicks", stats.getLast14thDaysdiscordServerLinkClicks());
            stats_last14days.put("vote_site_views", stats.getLast14thDaysServerVoteSiteClicks());
            stats_last14days.put("server_site_views", stats.getLast14thDaysServerSiteClicks());
            adminModel.setStats_last14days(stats_last14days);

            HashMap<String, Integer> stats_total = new HashMap<>();
            stats_total.put("site_clicks", stats.getTotalOsuServerLinkClicks());
            stats_total.put("discord_clicks", stats.getTotaldiscordServerLinkClicks());
            stats_total.put("vote_site_views", stats.getTotalServerVoteSiteClicks());
            stats_total.put("server_site_views", stats.getTotalServerSiteClicks());
            adminModel.setStats_total(stats_total);



            

            return returnResponse(adminModel);

        } catch (Exception e) {
            Flogger.instance.error(e);
            return internalError();
        }

    }

     private String getAPIKey(Request request, String id) throws SQLException {
        String sql = "SELECT * FROM un_servers WHERE `id` = ?";
        ResultSet serverResultSet = mysql.Query(sql, id);

        try {
            while (serverResultSet.next()) {

                return serverResultSet.getString("apikey");
            }
        } catch (SQLException e) {
            throw new SQLException("Error while fetching server details", e);
        }

        return null;
    }




}
