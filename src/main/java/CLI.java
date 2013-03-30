import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import rmp.scrape.RMPSchoolRatings;
import rmp.scrape.RMPSchools;
import rmp.scrape.RMPTeacherRatings;
import rmp.scrape.RMPTeachers;

public class CLI
{

	public static void main(String[] args)
	{
		System.out.println("CLI Runner");

		Options options = new Options();
		options.addOption("S", "scrape-rmp-schools", false,
				"Scrape schools from Rate My Professors");
		options.addOption("C", "scrape-school-ratings", false,
				"Scrape school ratings from Rate My Professors");
		options.addOption("R", "scrape-teacher-ratings", false,
				"Scrape teacher ratings from Rate My Professors");
		options.addOption("T", "scrape-teachers", false,
				"Scrape teachers from Rate My Professors");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		try
		{
			cmd = parser.parse(options, args);
		}
		catch (ParseException e)
		{
			System.out.println("Unexpected exception:" + e.getMessage());
			System.exit(1);
		}

		if (cmd.hasOption("S"))
		{
			RMPSchools.fetch();
		}
		else if (cmd.hasOption("C"))
		{
			RMPSchoolRatings.fetch();
		}
		else if (cmd.hasOption("R"))
		{
			RMPTeacherRatings.fetch();
		}
		else if (cmd.hasOption("T"))
		{
			RMPTeachers.fetch();
		}
		else
		{
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar target/PotatoPeeler.jar", options);
		}

	}
}
