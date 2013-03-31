package rmp.scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import models.Department;
import models.SchoolClass;
import models.Teacher;
import models.TeacherRating;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.InvalidModelException;

public class RMPTeacherRatings
{

	private final static String urlTemplate = "http://www.ratemyprofessors.com/ShowRatings.jsp?tid=%s&pageNo=%s";

	public static void fetch()
	{
		ArrayList<Teacher> teachers = Teacher.findAll();
		System.out.println("Scraping teacher ratings from " + teachers.size()
				+ " teachers");
		for (Teacher teacher : teachers)
		{
			RMPTeacherRatings.fetchByTeacher(teacher, 1);
		}
		System.out.println("All teachers scraped for ratings");
	}

	public static void fetchByTeacher(Teacher teacher, int pageNumber)
	{
		String url = String.format(RMPTeacherRatings.urlTemplate,
				teacher.getRmpId(), pageNumber);
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
		System.out.println("pages -> " + doc.select("#rateNumber").size());
		if (doc.select("#rateNumber").size() > 0)
		{
			int totalItems = Integer.parseInt(doc.select("#rateNumber strong")
					.text());
			int totalPages = (totalItems / 20) + 1;

			// get department
			String departmentName = doc.select("#profInfo li").get(2)
					.select("strong").text();
			Department department = Department.findByName(departmentName);
			System.out.println("Department: " + departmentName);

			Elements teacherRatingsElements = doc.select("#ratingTable .entry");
			Iterator<Element> teacherRatingsIterator = teacherRatingsElements
					.iterator();
			while (teacherRatingsIterator.hasNext())
			{
				Element teacherRatingsEntry = teacherRatingsIterator.next();

				String rmp_id = teacherRatingsEntry.select("a").first()
						.attr("name");
				String easiness = teacherRatingsEntry.select(".rEasy span")
						.text();
				String helpfulness = teacherRatingsEntry.select(
						".rHelpful span").text();
				String clarity = teacherRatingsEntry.select(".rClarity span")
						.text();
				String rater_interest = teacherRatingsEntry.select(
						".rInterest span").text();
				String comment = teacherRatingsEntry.select(
						".comment .commentText").text();
				String classString = teacherRatingsEntry.select(".class")
						.text();
				String dateString = teacherRatingsEntry.select(".date").text();

				try
				{
					System.out.println("---");
					System.out.println("Saving rating: " + comment + " ("
							+ rmp_id + ")");
					SchoolClass classRelation = SchoolClass.findOrCreate(
							department, classString);
					TeacherRating.findOrCreate(teacher, classRelation, rmp_id,
							easiness, helpfulness, clarity, rater_interest,
							comment, dateString);
				}
				catch (InvalidModelException e)
				{
					e.printStackTrace();
					System.exit(1);
				}
			}
			System.out.println(teacher.getFirst_name() + " "
					+ teacher.getLast_name() + " --- Saved "
					+ teacherRatingsElements.size() + " ratings");
			if (pageNumber < totalPages)
			{
				RMPTeacherRatings.fetchByTeacher(teacher, pageNumber + 1);
			}
		}
		else
		{
			System.out.println("teacher has no ratings: "
					+ teacher.getFirst_name() + " " + teacher.getLast_name());
		}
	}
}
