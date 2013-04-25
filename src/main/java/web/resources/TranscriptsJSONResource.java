package web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.TranscriptRecord;
import models.TranscriptPrediction;

@Path("transcripts")
public class TranscriptsJSONResource
{

	@GET
	@Path("{id}/record")
	@Produces("application/json")
	public String getTranscriptById(@PathParam("id") String id)
	{
		return TranscriptRecord.findById(id).toJson();
	}

	@GET
	@Path("{id}/prediction")
	@Produces("application/json")
	public String getPredictionById(@PathParam("id") String id)
	{
		//TODO: just place holding for now
		return TranscriptPrediction.findById(id).toJson();
	}
}
