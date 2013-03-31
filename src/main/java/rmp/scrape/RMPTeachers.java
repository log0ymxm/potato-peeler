package rmp.scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import models.Department;
import models.School;
import models.Teacher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.InvalidModelException;

public class RMPTeachers
{

	private final static String urlTemplate = "http://www.ratemyprofessors.com/SelectTeacher.jsp?sid=%s&pageNo=%s";

	public static void fetch()
	{
		ArrayList<School> schools = School.findAll();
		System.out.println("Scraping teachers from " + schools.size()
				+ " schools");
		for (School school : schools)
		{
			RMPTeachers.fetchBySchool(school, 1);
		}
		System.out.println("All schools scraped for teachers");
	}

	public static void fetch(String optionValue)
	{
		School school = School.findById(optionValue);
		RMPTeachers.fetchBySchool(school, 1);
	}

	public static void fetchBySchool(School school, int pageNumber)
	{
		String url = String.format(RMPTeachers.urlTemplate, school.getRmpId(),
				pageNumber);
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

		// (showing 1 to 20 of 1120 total)
		String pagesText = doc.select("#commentsBookToggle .profNum").text()
				.trim();
		// int currentPageStart = Integer.parseInt(pagesText.split(" ")[1]);
		// int currentPageEnd = Integer.parseInt(pagesText.split(" ")[3]);
		int totalItems = Integer.parseInt(pagesText.split(" ")[5]);
		int totalPages = (totalItems / 20) + 1;

		Elements teacherElements = doc.select("#ratingTable .entry");
		Iterator<Element> teacherIterator = teacherElements.iterator();
		while (teacherIterator.hasNext())
		{
			Element teacherEntry = teacherIterator.next();

			String nameText = teacherEntry.select(".profName a").text();
			String[] names = nameText.split(",");

			String first_name = "";
			String last_name = "";
			if (names.length == 2)
			{
				first_name = names[1].trim();
				last_name = names[0].trim();
			}
			else
			{
				first_name = nameText;
			}
			String rmp_id = teacherEntry.select(".profName a").attr("href")
					.split("tid=")[1];
			String department_name = teacherEntry.select(".profDept").text();

			try
			{
				System.out.println("---");
				System.out.println("Saving: " + first_name + " (" + rmp_id
						+ ")");
				Department department = Department
						.findOrCreate(department_name);
				Teacher.findOrCreate(first_name, last_name, rmp_id, department);
			}
			catch (InvalidModelException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		System.out.println(school.getName() + " --- Saved "
				+ teacherElements.size() + " teachers");
		if (pageNumber < totalPages)
		{
			RMPTeachers.fetchBySchool(school, pageNumber + 1);
		}
	}
}
