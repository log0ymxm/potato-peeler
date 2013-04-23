package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBFactory;
import database.InvalidModelException;
import database.Model;

public class TranscriptPrediction extends Model
{

	protected TranscriptPrediction( String table, ArrayList< String > fields, ArrayList< String > relations, Boolean dirty, Boolean fresh )
	{
		super( table, fields, relations, dirty, fresh );
		// TODO Auto-generated constructor stub
	}
	
	public static TranscriptPrediction findById( String id )
	{
		// TODO Kevin-generated method stub
		TranscriptPrediction transcriptPrediction = null;
		
		return transcriptPrediction;
	}

	@Override
	public String isValid()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save() throws InvalidModelException
	{
		// TODO Auto-generated method stub
		return false;
	}

}
