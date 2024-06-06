package osu.serverlist.Sites.Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public static String repairBanner(String banner) {
        if(banner == null) return null;

        if(banner.startsWith("http://") || banner.startsWith("https://")) {
            return banner;
        } else {
            return Api.configObj.getDomain() + "/res/banner/" + banner;
        }
    }

    public static String getDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

}
