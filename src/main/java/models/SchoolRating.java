package models;

import java.util.Date;

import database.Model;

public class SchoolRating extends Model
{

	private float campusGrounds;
	private float campusLocation;
	private float careerOpportunities;
	private float clubsAndEvents;
	private String comment;
	private float conditionOfLibrary;
	private Date date;
	private String id;
	private float internetSpeed;
	private float qualityOfFood;
	private String rmpId;
	private School school;
	private float schoolReputation;
	private float socialActivities;

	public float getCampusGrounds()
	{
		return this.campusGrounds;
	}

	public float getCampusLocation()
	{
		return this.campusLocation;
	}

	public float getCareerOpportunities()
	{
		return this.careerOpportunities;
	}

	public float getClubsAndEvents()
	{
		return this.clubsAndEvents;
	}

	public String getComment()
	{
		return this.comment;
	}

	public float getConditionOfLibrary()
	{
		return this.conditionOfLibrary;
	}

	public Date getDate()
	{
		return this.date;
	}

	public String getId()
	{
		return this.id;
	}

	public float getInternetSpeed()
	{
		return this.internetSpeed;
	}

	public float getQualityOfFood()
	{
		return this.qualityOfFood;
	}

	public String getRmpId()
	{
		return this.rmpId;
	}

	public School getSchool()
	{
		return this.school;
	}

	public float getSchoolReputation()
	{
		return this.schoolReputation;
	}

	public float getSocialActivities()
	{
		return this.socialActivities;
	}

	@Override
	public String isValid()
	{
		// TODO implement
		return "Not Implemented";
	}

	@Override
	public boolean save()
	{
		// TODO implement
		return false;
	}

	public void setCampusGrounds(float campusGrounds)
	{
		this.campusGrounds = campusGrounds;
	}

	public void setCampusLocation(float campusLocation)
	{
		this.campusLocation = campusLocation;
	}

	public void setCareerOpportunities(float careerOpportunities)
	{
		this.careerOpportunities = careerOpportunities;
	}

	public void setClubsAndEvents(float clubsAndEvents)
	{
		this.clubsAndEvents = clubsAndEvents;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public void setConditionOfLibrary(float conditionOfLibrary)
	{
		this.conditionOfLibrary = conditionOfLibrary;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setInternetSpeed(float internetSpeed)
	{
		this.internetSpeed = internetSpeed;
	}

	public void setQualityOfFood(float qualityOfFood)
	{
		this.qualityOfFood = qualityOfFood;
	}

	public void setRmpId(String rmpId)
	{
		this.rmpId = rmpId;
	}

	public void setSchool(School school)
	{
		this.school = school;
	}

	public void setSchoolReputation(float schoolReputation)
	{
		this.schoolReputation = schoolReputation;
	}

	public void setSocialActivities(float socialActivities)
	{
		this.socialActivities = socialActivities;
	}

}
