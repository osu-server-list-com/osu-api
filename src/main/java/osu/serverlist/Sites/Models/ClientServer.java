package osu.serverlist.Sites.Models;

import lombok.Data;

@Data
public class ClientServer {

    private int id;
    private String name;
    private String safe_name;
    private String url;
    private String devserver;
    private boolean online;

    private String image;
    private int players;
    private int votes;

    private String timestamp;

}
