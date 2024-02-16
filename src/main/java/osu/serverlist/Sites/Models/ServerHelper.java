package osu.serverlist.Sites.Models;

import osu.serverlist.Main.Api;

public class ServerHelper {
    public static String repairLogo(String logo) {
        if(logo == null) return null;

        if(logo.startsWith("http://") || logo.startsWith("https://")) {
            return logo;
        } else {
            return Api.configObj.getDomain() + logo;
        }
    }
}
