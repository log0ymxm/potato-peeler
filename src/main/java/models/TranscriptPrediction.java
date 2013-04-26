package models;

import java.util.ArrayList;

import database.InvalidModelException;
import database.Model;

public class TranscriptPrediction extends Model
{

	protected TranscriptPrediction(String table, ArrayList<String> fields,
			ArrayList<String> relations, Boolean dirty, Boolean fresh)
	{
		super(table, fields, relations, dirty, fresh);
	}

	public static TranscriptPrediction findById(String id)
	{
		TranscriptPrediction transcriptPrediction = null;
		throw new UnsupportedOperationException();

		// return transcriptPrediction;
	}

	@Override
	public String isValid()
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();

		// return null;
	}

	@Override
	public boolean save() throws InvalidModelException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();

		// return false;
	}

}
