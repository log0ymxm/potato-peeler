package rmp.scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import models.School;
import models.SchoolRating;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.InvalidModelException;

public class RMPSchoolRatings
{

	private final static String urlTemplate = "http://www.ratemyprofessors.com/campusRatings.jsp?sid=%s&pageNo=%s";

	public static void fetch()
	{
		ArrayList<School> schools = School.findAll();
		System.out.println("Scraping school ratings from " + schools.size()
				+ " schools");
		for (School school : schools)
		{
			RMPSchoolRatings.fetchBySchool(school, 1);
		}
		System.out.println("All schools scraped for school ratings");
	}

	public static void fetchBySchool(School school, int pageNumber)
	{
		String url = String.format(RMPSchoolRatings.urlTemplate,
				school.getRmpId(), pageNumber);
		Document doc = null;
		try
		{
			System.out.println("Fetching: " + url);
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		// get pages
		int totalItems = Integer.parseInt(doc.select("#noRatings strong")
				.text());
		int totalPages = (totalItems / 20) + 1;

		Elements schoolRatingsElements = doc
				.select("#schoolComments .schoolRating");
		Iterator<Element> schoolRatingsIterator = schoolRatingsElements
				.iterator();
		while (schoolRatingsIterator.hasNext())
		{
			Element schoolRatingsEntry = schoolRatingsIterator.next();

			// Not all ratings display rmp_id in the same way
			String rmp_id = null;
			if (schoolRatingsEntry.select(".commentText a.flagRating")
					.attr("href").split("=").length > 1)
			{
				rmp_id = schoolRatingsEntry.select(".commentText a.flagRating")
						.attr("href").split("=")[1];
			}
			else
			{
				rmp_id = schoolRatingsEntry.select(".commentText a img").attr(
						"alt");
			}
			String date = schoolRatingsEntry.select(".date").text();
			String school_reputation = schoolRatingsEntry.select(
					".schoolRep span").text();
			String career_opportunities = schoolRatingsEntry.select(
					".schoolCareers span").text();
			String campus_grounds = schoolRatingsEntry.select(
					".schoolCampus span").text();
			String quality_of_food = schoolRatingsEntry.select(
					".schoolFood span").text();
			String social_activities = schoolRatingsEntry.select(
					".schoolActivities span").text();
			String campus_location = schoolRatingsEntry.select(
					".schoolLocation span").text();
			String condition_of_library = schoolRatingsEntry.select(
					".schoolLibrary span").text();
			String internet_speed = schoolRatingsEntry.select(
					".schoolInternet span").text();
			String clubs_and_events = schoolRatingsEntry.select(
					".schoolEvents span").text();
			String comment = schoolRatingsEntry.select(".commentText p").text();
			String school_happiness = schoolRatingsEntry.select(
					".schoolHappyness span").text();

			try
			{
				System.out.println("---");
				System.out.println("Saving rating: " + comment + " (" + rmp_id
						+ ")");
				SchoolRating.findOrCreate(rmp_id, school, date,
						school_reputation, career_opportunities,
						campus_grounds, quality_of_food, social_activities,
						campus_location, condition_of_library, internet_speed,
						clubs_and_events, comment, school_happiness);
			}
			catch (InvalidModelException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		System.out.println(school.getName() + " --- Saved "
				+ schoolRatingsElements.size() + " ratings");
		if (pageNumber < totalPages)
		{
			RMPSchoolRatings.fetchBySchool(school, pageNumber + 1);
		}
	}
}
