import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import predict.Grade;
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

		// Scraping
		options.addOption("1", "scrape-rmp-schools", false,
				"Scrape schools from Rate My Professors");
		options.addOption("2", "scrape-teachers", false,
				"Scrape teachers from Rate My Professors");
		options.addOption("3", "scrape-school-ratings", false,
				"Scrape school ratings from Rate My Professors");
		options.addOption("4", "scrape-teacher-ratings", false,
				"Scrape teacher ratings from Rate My Professors");
		options.addOption("s", "school-id", true,
				"Only scrape a specific school for ratings or teachers");
		options.addOption("t", "teacher-id", true,
				"Only scrape a specific teacher for ratings");

		// Machine Learning
		options.addOption("X", "export-arff", false,
				"Export an arff file for use in the WEKA gui");
		options.addOption("G", "generate-model", false,
				"Generate a model file for basic regression");

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

		if (cmd.hasOption("1"))
		{
			System.out.println("Fetching schools from RMP");
			RMPSchools.fetch();
		}
		else if (cmd.hasOption("2"))
		{
			System.out.println("Fetching teachers from RMP");
			System.out.println(cmd.hasOption("s"));
			if (cmd.hasOption("s"))
			{
				RMPTeachers.fetch(cmd.getOptionValue("s"));
			}
			else
			{
				RMPTeachers.fetch();
			}
		}
		else if (cmd.hasOption("3"))
		{
			System.out.println("Fetching school ratings from RMP");
			if (cmd.hasOption("s"))
			{
				RMPSchoolRatings.fetch(cmd.getOptionValue("s"));
			}
			else
			{
				RMPSchoolRatings.fetch();
			}
		}
		else if (cmd.hasOption("4"))
		{
			System.out.println("Fetching teacher ratings from RMP");
			if (cmd.hasOption("t"))
			{
				RMPTeacherRatings.fetch(cmd.getOptionValue("t"));
			}
			else
			{
				RMPTeacherRatings.fetch();
			}
		}
		else if (cmd.hasOption("X"))
		{
			System.out.println("Generating arff file");
			Grade.generateArff();
		}
		else if (cmd.hasOption("G"))
		{
			System.out.println("Generating a model");
			Grade.generateModel();
		}
		else
		{
			// automatically generate the help statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar target/PotatoPeeler.jar", options);
		}

	}
}
