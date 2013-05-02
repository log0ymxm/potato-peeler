package web.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import models.Transcript;
import models.TranscriptPrediction;
import models.TranscriptRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.InvalidModelException;

@Path("transcripts")
public class TranscriptsJSONResource
{

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public String getPredictions(String json)
	{
		System.out.println("--- getPredictions: " + json);

		try
		{
			Transcript t = Transcript.findOrCreate();
			t.save();

			JSONObject response = new JSONObject(json);
			JSONArray schedule = response.getJSONArray("schedule");
			JSONArray transcript = response.getJSONArray("transcript");
			for (int i = 0; i < schedule.length(); i++)
			{
				JSONObject scheduleRecord = schedule.getJSONObject(i);
				String state_id = scheduleRecord.getString("state");
				String school_id = scheduleRecord.getString("school");
				String class_id = scheduleRecord.getString("class");
				String term = scheduleRecord.getString("term");
				String year = scheduleRecord.getString("year");
				System.out.println("schedule record: " + state_id + " "
						+ school_id + " " + class_id + " " + term + " " + year);

				TranscriptRecord tr = TranscriptRecord.findOrCreate(t.getId(),
						state_id, school_id, class_id);
			}
			for (int i = 0; i < transcript.length(); i++)
			{
				JSONObject transcriptRecord = transcript.getJSONObject(i);
				String state_id = transcriptRecord.getString("state");
				String school_id = transcriptRecord.getString("school");
				String class_id = transcriptRecord.getString("class");
				String term = transcriptRecord.getString("term");
				String year = transcriptRecord.getString("year");
				System.out.println("transcript record: " + state_id + " "
						+ school_id + " " + class_id + " " + term + " " + year);
			}

			return TranscriptPrediction.findById(t.getId());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		catch (InvalidModelException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
