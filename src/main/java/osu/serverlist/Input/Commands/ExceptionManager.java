package osu.serverlist.Input.Commands;

import java.util.ArrayList;

import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Commons.Flogger.Prefix;
import commons.marcandreher.Input.Command;


public class ExceptionManager implements Command {

    private static ArrayList<Exception> exceptionList = new ArrayList<>();

    @Override
    public void executeAction(String[] args, Flogger logger) {
        if (args.length < 2) {
            System.out.println(Prefix.INFO + getAlias());
            return;
        }

        if (args[1].equalsIgnoreCase("show")) {
            if (args.length < 3) {
                System.out.println(Prefix.INFO + getAlias());
                return;
            }
            int index = Integer.parseInt(args[2]);
            if(!(index < exceptionList.size())) {
                System.out.println(Prefix.ERROR + "Entry not found");
                return;
            }
            Exception e = exceptionList.get(index);
            System.out.println(Prefix.ERROR + e.getMessage());
        } else if (args[1].equalsIgnoreCase("all")) {
            for(int i = 0; i < exceptionList.size(); i++) {
                Exception e = exceptionList.get(i);
                System.out.println(Prefix.ERROR + "[" + i + "] " + e.getMessage());
            }
        } else if(args[1].equalsIgnoreCase("size")) {
            System.out.println(Prefix.INFO + "[" + exceptionList.size() + "] " + "Errors");
        } else {
            System.out.println(Prefix.INFO + getAlias());
        }

    }

    public static void addException(Exception e) {
        exceptionList.add(e);
        System.out.println(Prefix.ERROR + "Thrown | Number " +exceptionList.size());
    } 

    @Override
    public String getAlias() {
        return "-exception <all/show/size> <id?>";
    }

    @Override
    public String getDescription() {
        return "List all exceptions for debugging";
    }

    @Override
    public String getName() {
        return "exception";
    }

}
