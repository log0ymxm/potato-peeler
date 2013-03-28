package main.java;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import java.util.Date;

public class CLI
{
    public static void main(String[] args)
    {
        System.out.println("CLI Runner");
        
        Options options = new Options();
        options.addOption("S", "scrape-rmp-schools", false, "Scrape School IDs from Rate My Professors");
        
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        
        try 
            {
                cmd = parser.parse( options, args);
            }
        catch (ParseException e)
            {
                System.out.println( "Unexpected exception:" + e.getMessage() );
            }
        
        // TODO implement commands & what not
        if (cmd.hasOption("S")) 
            {
                System.out.println(new Date());
                // print the date and time
            }
        else 
            {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "java -jar target/PotatoPeeler.jar", options );
            }
        
    }
}
