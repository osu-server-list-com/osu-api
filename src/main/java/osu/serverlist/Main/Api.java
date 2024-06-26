package osu.serverlist.Main;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import commons.marcandreher.Cache.CacheTimer;
import commons.marcandreher.Commons.Database;
import commons.marcandreher.Commons.Database.ServerTimezone;
import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Commons.Flogger.Prefix;
import commons.marcandreher.Commons.MySQL;
import commons.marcandreher.Commons.Router;
import commons.marcandreher.Commons.WebServer;
import commons.marcandreher.Engine.HealthRoute;
import commons.marcandreher.Input.CommandHandler;
import freemarker.template.Configuration;
import io.github.cdimascio.dotenv.Dotenv;
import osu.serverlist.Cache.CategoriesCh;
import osu.serverlist.Cache.ClientKeys;
import osu.serverlist.Cache.RefreshHeatmap;
import osu.serverlist.Sites.Endpoints.BanchoPyStatsRoute;
import osu.serverlist.Sites.Endpoints.CategoriesRoute;
import osu.serverlist.Sites.Endpoints.ChartDataRoute;
import osu.serverlist.Sites.Endpoints.ChartDataVoteRoute;
import osu.serverlist.Sites.Endpoints.ChartHeatMapRoute;
import osu.serverlist.Sites.Endpoints.ChartTypesRoute;
import osu.serverlist.Sites.Endpoints.ServerRoute;
import osu.serverlist.Sites.Endpoints.ServersRoute;
import osu.serverlist.Sites.Endpoints.client.ClientServersRoute;
import osu.serverlist.v3.web.Admin;
import osu.serverlist.v3.web.Categories;
import osu.serverlist.v3.web.Server;
import osu.serverlist.v3.web.Servers;
import spark.Spark;

public class Api extends Spark {

	public static String verString = "1.0-BETA";

	public static Configuration freemarkerCfg = new Configuration(Configuration.VERSION_2_3_23);
	public static Dotenv env;

	public static void main(String[] args) {
		env = Dotenv.load();
		Flogger logger = new Flogger (Integer.parseInt(env.get("LOGLEVEL")));
		
		MySQL.LOGLEVEL = 5;
		Database database = new Database();
		database.setDefaultSettings();
		database.setMaximumPoolSize(5);
		database.setConnectionTimeout(3000);
		database.connectToMySQL(env.get("DBHOST"), env.get("DBUSER"), env.get("DBPASS"), env.get("DBNAME"), ServerTimezone.UTC);

		WebServer webServer = new WebServer(logger, (short)2);
		webServer.setThreadPool(0, 20, 3000);
		webServer.setPrefix(Prefix.API);
		CommandHandler cmd = new CommandHandler(logger);

		try {
			webServer.ignite(env.get("HOST"), Integer.parseInt(env.get("APIPORT")), cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		Router router = new Router(logger);
		router.get("/api/v1/server", new ServerRoute());
		router.get("/api/v1/servers", new ServersRoute());
		router.get("/api/v1/categories", new CategoriesRoute());
		router.get("/api/v1/banchopy/stats", new BanchoPyStatsRoute());
		router.get("/api/v1/chart/types", new ChartTypesRoute());
		router.get("/api/v1/chart/data", new ChartDataRoute());
		router.get("/api/v1/chart/votes/data", new ChartDataVoteRoute());

		router.get("/api/v2/client/servers", new ClientServersRoute());

		CacheTimer ct = new CacheTimer(30, 1, TimeUnit.MINUTES);
		ct.addAction(new ClientKeys());
		ct.addAction(new RefreshHeatmap());
		ct.addAction(new CategoriesCh());

		router.get("/api/v3/server", new Server());
		router.get("/api/v3/servers", new Servers());
		router.get("/api/v3/categories", new Categories());
		router.get("/api/v3/admin", new Admin());
		router.get("/api/v3/health", new HealthRoute());

		router.get("/api/v1/heatmap", new ChartHeatMapRoute());
		cmd.registerCommand(new ExceptionManager());
		cmd.initialize();

		
		
	}
}
