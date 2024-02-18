package osu.serverlist.Sites.Models;

public class Stats {
    
    private int score = 0;

    private int registered = 0;
    private int banned = 0;
    private int plays;
    private int players = 0;
    private int maps = 0;
    private int CLANS = 0;

    public int getCLANS() {
        return this.CLANS;
    }

    public void setCLANS(int CLANS) {
        this.CLANS = CLANS;
    }

    

    public int getplays() {
        return this.plays;
    }   

    public void setplays(int plays) {
        this.plays = plays;
    }


    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    public int getregistered() {
        return this.registered;
    }

    public void setregistered(int registered) {
        this.registered = registered;
    }

    public int getbanned() {
        return this.banned;
    }

    public void setbanned(int banned) {
        this.banned = banned;
    }

    public int getplayers() {
        return this.players;
    }

    public void setplayers(int players) {
        this.players = players;
    }

    public int getmaps() {
        return this.maps;
    }

    public void setmaps(int maps) {
        this.maps = maps;
    }
}
