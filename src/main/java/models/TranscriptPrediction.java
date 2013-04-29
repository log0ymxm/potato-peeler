package models;

import java.util.ArrayList;

import predict.ModelBuilder;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import database.InvalidModelException;
import database.Model;

public class TranscriptPrediction extends Model
{

	private static Classifier easiness_1;
	private static Classifier easiness_2;
	private static Classifier easiness_3;
	private static Classifier easiness_4;
	private static Classifier easiness_5;

	protected TranscriptPrediction(String table, ArrayList<String> fields,
			ArrayList<String> relations, Boolean dirty, Boolean fresh)
	{
		super(table, fields, relations, dirty, fresh);
	}

	public static TranscriptPrediction findById(String id)
	{
		TranscriptPrediction transcriptPrediction = null;

		if (TranscriptPrediction.easiness_1 == null)
		{
			TranscriptPrediction.easiness_1 = ModelBuilder
					.loadModel("models/easiness-vote-1.model");
			TranscriptPrediction.easiness_2 = ModelBuilder
					.loadModel("models/easiness-vote-2.model");
			TranscriptPrediction.easiness_3 = ModelBuilder
					.loadModel("models/easiness-vote-3.model");
			TranscriptPrediction.easiness_4 = ModelBuilder
					.loadModel("models/easiness-vote-4.model");
			TranscriptPrediction.easiness_5 = ModelBuilder
					.loadModel("models/easiness-vote-5.model");
		}

		try
		{

			Transcript transcript = Transcript.findById(id);

			Instances transcript_records = ModelBuilder
					.getBinaryInstance(transcript);

			for (Instance record : transcript_records)
			{
				double[] classification_1 = TranscriptPrediction.easiness_1
						.distributionForInstance(record);
				double[] classification_2 = TranscriptPrediction.easiness_2
						.distributionForInstance(record);
				double[] classification_3 = TranscriptPrediction.easiness_3
						.distributionForInstance(record);
				double[] classification_4 = TranscriptPrediction.easiness_4
						.distributionForInstance(record);
				double[] classification_5 = TranscriptPrediction.easiness_5
						.distributionForInstance(record);

				System.out.println("---");
				System.out.println("Prediction distribution of 1: "
						+ classification_1);
				System.out.println("Prediction distribution of 2: "
						+ classification_2);
				System.out.println("Prediction distribution of 3: "
						+ classification_3);
				System.out.println("Prediction distribution of 4: "
						+ classification_4);
				System.out.println("Prediction distribution of 5: "
						+ classification_5);

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return transcriptPrediction;
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
