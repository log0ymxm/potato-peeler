package web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import models.TranscriptRecord;

@Path("transcripts")
public class TranscriptsPredictionJSONResource
{

	@GET
	@Path("{id}/prediction")
	@Produces("application/json")
	public String getTranscriptById(@PathParam("id") String id)
	{
		return TranscriptRecord.findById(id).toJson();
	}

}
