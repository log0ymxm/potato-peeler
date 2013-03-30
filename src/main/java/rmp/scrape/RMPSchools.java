package rmp.scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import models.Location;
import models.School;
import models.State;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.InvalidModelException;

public class RMPSchools
{

	private final static String urlTemplate = "http://www.ratemyprofessors.com/SelectSchool.jsp?country=0&stateselect=%s";

	public static void fetch()
	{
		ArrayList<State> states = State.findAll();
		for (State state : states)
		{
			RMPSchools.fetchByState(state);
		}
		System.out.println("All States Scraped");
	}

	public static void fetchByState(State state)
	{
		String url = String.format(RMPSchools.urlTemplate,
				state.getAbbreviation());
		Document doc = null;
		try
		{
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		Elements schoolElements = doc.select("#ratingTable .entry");
		Iterator<Element> schoolIterator = schoolElements.iterator();
		while (schoolIterator.hasNext())
		{
			Element schoolEntry = schoolIterator.next();

			String name = schoolEntry.select(".schoolName a").text();
			String rmpId = schoolEntry.select(".schoolName a").attr("title")
					.replace("school id: ", "");
			String city = schoolEntry.select(".schoolCity").text();

			try
			{
				System.out.println("---");
				System.out.println("Saving: " + name + " (" + rmpId + ")");
				Location location = Location.findOrCreate(state, city);
				School.findOrCreate(rmpId, name, location);
			}
			catch (InvalidModelException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		System.out.println(state.getAbbreviation() + " --- Saved "
				+ schoolElements.size() + " schools");
	}
}
