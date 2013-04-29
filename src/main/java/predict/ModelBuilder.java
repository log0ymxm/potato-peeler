package predict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import models.Transcript;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SGD;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.Resample;

public class ModelBuilder
{

	private final static int folds = 10;
	private final static int seed = 42;

	/**
	 * Build several different style classifiers using data, and combine them
	 * using a vote based ensemble scheme.
	 * 
	 * @param data
	 * @param attributeIndex
	 * @return
	 */
	public static Vote buildEnsembleOnAttribute(Instances data,
			int attributeIndex)
	{
		System.out.println("Building vote ensemble classifier on attribute: "
				+ data.attribute(attributeIndex));

		try
		{
			// Try a bunch of the classifiers with capabilities that match our
			// transformed data set
			Classifier oneR = new OneR();       // 82.77%
			Classifier nb = new NaiveBayes();   // 79.802%
			Classifier log = new Logistic();    // 80.198
			Classifier j48 = new J48();         // 91.48%
			Classifier smo = new SMO();         // 100% (overfit?)
			Classifier rf = new RandomForest(); // 82.77%
			Classifier sgd = new SGD();         // 100% (overfit?)
			Classifier ibk = new IBk();         // 80.39%

			Classifier[] classifiers = new Classifier[]
			{ oneR, nb, log, j48, smo, rf, sgd, ibk };

			data.setClassIndex(attributeIndex);

			System.out.println("======================================");

			// I don't know if we need to build each classifier first, or can
			// call buildClassifier on the voter.
			for (Classifier cls : classifiers)
			{
				System.out.println("Building " + cls.getClass());
				cls.buildClassifier(data);
				System.out.println("Built classifier");
			}

			Vote voter = new Vote();
			voter.setClassifiers(classifiers);

			return voter;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Combines an array of objects into a string using glue. Similar to
	 * Array.join() in many other languages.
	 * 
	 * @param s
	 * @param glue
	 * @return
	 */
	public static String combine(Object[] s, String glue)
	{
		int k = s.length;
		if (k == 0)
		{
			return null;
		}
		StringBuilder out = new StringBuilder();
		out.append(s[0]);
		for (int x = 1; x < k; ++x)
		{
			out.append(glue).append(s[x]);
		}
		return out.toString();
	}

	/**
	 * Evaluate & print stats on the performance of a classifier.
	 * 
	 * @param data
	 * @param cls
	 */
	public static void evaluateModel(Instances data, Classifier cls)
	{
		try
		{
			System.out.println("======================================");
			System.out.println("Calculating " + ModelBuilder.folds
					+ "-fold evaluation on a dataset of " + data.size()
					+ " instances and " + data.numAttributes() + " features.");
			Evaluation eval = new Evaluation(data);
			Random rand = new Random(ModelBuilder.seed);
			eval.crossValidateModel(cls, data, ModelBuilder.folds, rand);
			System.out.println("======================================");
			System.out.println("Evaluation:\n" + eval.toSummaryString());
			System.out.println("======================================");
			System.out.println("Detailed Class Stats:\n"
					+ eval.toClassDetailsString());
			System.out.println("======================================");
			System.out.println("Confusion Matrix:\n" + eval.toMatrixString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Provides a simple representation of our training data for use in weka
	 * gui, or other experimentation.
	 */
	public static void generateArff()
	{
		try
		{
			Instances instances = ModelBuilder.getData(1);
			ArffSaver saver = new ArffSaver();
			File out = new File("data/teacher_ratings.arff");
			saver.setInstances(instances);
			saver.setFile(out);
			saver.writeBatch();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Will generate 5 file based models for predicting how easy a class will be
	 * for a reviewer. We generate one model for each discrete value of easiness
	 * (1-5). When classifying we can compare the probabilities of each
	 * classifier to select the most probable class/teacher rating.
	 */
	public static void generateModel()
	{
		try
		{
			double sampleSize = 100;
			System.out.println("Fetching instances for " + sampleSize
					+ "% of available data");
			Instances data = ModelBuilder.getBinaryData(1);
			System.out.println("Learning on " + data.numAttributes()
					+ " features");

			Vote[] voters = new Vote[5];
			for (int i = 0; i < 5; i++)
			{
				data.setClassIndex(i);
				Vote voter = ModelBuilder.buildEnsembleOnAttribute(data, i);

				String name = "data/easiness-vote-" + (i + 1) + ".model";
				ModelBuilder.saveModel(voter, name);
				voters[i] = voter;
			}

			for (int i = 0; i < 5; i++)
			{
				Vote voter = voters[i];
				data.setClassIndex(i);
				ModelBuilder.evaluateModel(data, voter);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static Instances getBinaryData(double sample)
	{
		Instances data = ModelBuilder.getData(sample);

		System.out.println("initial data num attrs: " + data.numAttributes());
		data = ModelBuilder.prepareNominalToBinary(data);
		System.out.println("prepareNominalToBinary num attrs: "
				+ data.numAttributes());
		// ModelBuilder.printAttributeNames(data);

		data = ModelBuilder.prepareNumericToBinary(data);

		return data;
	}

	public static Instances getBinaryInstance(Transcript transcript)
	{
		// should take all the fields we predict on, and generate an
		// instance that matches in attributes to what we generated our
		// model on
		String query = "SELECT "
				+ "    null as easiness, "
				+ "    schools.name AS schools_name, "
				+ "    teachers.last_name, "
				+ "    teachers.first_name, "
				+ "    AVG(DISTINCT teacher_ratings.helpfulness) as helpfulness, "
				+ "    AVG(DISTINCT teacher_ratings.clarity) as clarity, "
				+ "    AVG(DISTINCT teacher_ratings.rater_interest) as rater_interest, "
				+ "    DAYOFMONTH(transcript_records.date) AS rating_day_of_month, "
				+ "    DAYOFWEEK(transcript_records.date) AS rating_day_of_week, "
				+ "    DAYOFYEAR(transcript_records.date) AS rating_day_of_year, "
				+ "    MONTH(transcript_records.date) AS rating_month, "
				+ "    YEAR(transcript_records.date) AS rating_year, "
				+ "    QUARTER(transcript_records.date) AS rating_quarter, "
				+ "    WEEK(transcript_records.date) AS rating_week, "
				+ "    WEEKOFYEAR(transcript_records.date) AS rating_week_of_year, "
				+ "    UNIX_TIMESTAMP(transcript_records.date) AS rating_timestamp, "
				+ "    classes.level, "
				+ "    departments.name AS departments_name, "
				// +
				// "    AVG(DISTINCT school_ratings.school_reputation) as school_reputation, "
				// +
				// "    AVG(DISTINCT school_ratings.career_opportunities) as school_career_opportunities, "
				// +
				// "    AVG(DISTINCT school_ratings.campus_grounds) as school_campus_grounds, "
				// +
				// "    AVG(DISTINCT school_ratings.quality_of_food) as school_quality_of_food, "
				// +
				// "    AVG(DISTINCT school_ratings.social_activities) as school_social_activities, "
				// +
				// "    AVG(DISTINCT school_ratings.campus_location) as school_campus_location, "
				// +
				// "    AVG(DISTINCT school_ratings.condition_of_library) as school_condition_of_library, "
				// +
				// "    AVG(DISTINCT school_ratings.internet_speed) as school_internet_speed, "
				// +
				// "    AVG(DISTINCT school_ratings.clubs_and_events) as school_clubs_and_events, "
				// +
				// "    AVG(DISTINCT school_ratings.school_happiness) as school_happiness,"
				+ "    locations.name AS location_name,"
				+ "    states.name AS states_name "
				+ "FROM transcript_records "
				+ "    JOIN transcripts ON transcript_records.transcript_id = transcripts.id "
				+ "    JOIN teachers ON transcript_records.teacher_id "
				+ "    JOIN teacher_ratings ON teachers.id = teacher_ratings.teacher_id "
				+ "    JOIN schools ON teachers.school_id = schools.id "
				+ "    JOIN locations ON schools.location_id = locations.id "
				+ "    JOIN states ON locations.state_id = states.id "
				// +
				// "    JOIN school_ratings ON schools.id = school_ratings.school_id "
				+ "    JOIN classes ON transcript_records.class_id = classes.id "
				+ "    JOIN departments ON classes.department_id = departments.id"
				// I don't know if I can do prepared statements with weka
				// InstanceQuery, using a Transcript model should always return
				// safe data. Probably...
				+ "WHERE transcripts.id = " + transcript.getId() + " "
				+ "GROUP BY teacher_ratings.helpfulness, "
				+ "    teacher_ratings.clarity, "
				+ "    teacher_ratings.rater_interest "
		// + ",    school_ratings.school_reputation, "
		// + "    school_ratings.career_opportunities, "
		// + "    school_ratings.campus_grounds, "
		// + "    school_ratings.quality_of_food, "
		// + "    school_ratings.social_activities, "
		// + "    school_ratings.campus_location, "
		// + "    school_ratings.condition_of_library, "
		// + "    school_ratings.internet_speed, "
		// + "    school_ratings.clubs_and_events, "
		// + "    school_ratings.school_happiness"
		;

		try
		{
			File propsFile = new File("DatabaseUtils.props");
			System.out.println(propsFile + " " + propsFile.getAbsolutePath());

			InstanceQuery instanceQuery = new InstanceQuery();
			instanceQuery.setCustomPropsFile(propsFile);
			instanceQuery.setUsername("root");
			instanceQuery.setPassword("");
			instanceQuery.setQuery(query);
			Instances data = instanceQuery.retrieveInstances();

			// Set our class attribute
			data.setClassIndex(0);

			ModelBuilder.printAttributeNames(data);

			System.out.println("initial data num attrs: "
					+ data.numAttributes());
			data = ModelBuilder.prepareNominalToBinary(data);
			System.out.println("prepareNominalToBinary num attrs: "
					+ data.numAttributes());

			data = ModelBuilder.prepareNumericToBinary(data);

			ModelBuilder.printAttributeNames(data);

			// TODO prepare instances in the same way we're preparing binary
			// data

			System.out.println("got instances to predict on: " + data.size());
			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	public static Instances getData(double sample)
	{
		String query = "SELECT "
				+ "    teacher_ratings.easiness," // Note that this is our class
													// attribute
				+ "    schools.name AS schools_name, "
				+ "    teachers.last_name, "
				+ "    teachers.first_name, "
				+ "    teacher_ratings.helpfulness, "
				+ "    teacher_ratings.clarity, "
				+ "    teacher_ratings.rater_interest, "
				+ "    DAYOFMONTH(teacher_ratings.date) AS rating_day_of_month, "
				+ "    DAYOFWEEK(teacher_ratings.date) AS rating_day_of_week, "
				+ "    DAYOFYEAR(teacher_ratings.date) AS rating_day_of_year, "
				+ "    MONTH(teacher_ratings.date) AS rating_month, "
				+ "    YEAR(teacher_ratings.date) AS rating_year, "
				+ "    QUARTER(teacher_ratings.date) AS rating_quarter, "
				+ "    WEEK(teacher_ratings.date) AS rating_week, "
				+ "    WEEKOFYEAR(teacher_ratings.date) AS rating_week_of_year, "
				+ "    UNIX_TIMESTAMP(teacher_ratings.date) AS rating_timestamp, "
				+ "    classes.level, "
				+ "    departments.name AS departments_name, "
				// +
				// "    AVG(DISTINCT school_ratings.school_reputation) as school_reputation, "
				// +
				// "    AVG(DISTINCT school_ratings.career_opportunities) as school_career_opportunities, "
				// +
				// "    AVG(DISTINCT school_ratings.campus_grounds) as school_campus_grounds, "
				// +
				// "    AVG(DISTINCT school_ratings.quality_of_food) as school_quality_of_food, "
				// +
				// "    AVG(DISTINCT school_ratings.social_activities) as school_social_activities, "
				// +
				// "    AVG(DISTINCT school_ratings.campus_location) as school_campus_location, "
				// +
				// "    AVG(DISTINCT school_ratings.condition_of_library) as school_condition_of_library, "
				// +
				// "    AVG(DISTINCT school_ratings.internet_speed) as school_internet_speed, "
				// +
				// "    AVG(DISTINCT school_ratings.clubs_and_events) as school_clubs_and_events, "
				// +
				// "    AVG(DISTINCT school_ratings.school_happiness) as school_happiness, "
				+ "    locations.name AS location_name,"
				+ "    states.name AS states_name "
				+ "FROM teacher_ratings "
				+ "    JOIN teachers ON teacher_ratings.teacher_id = teachers.id "
				+ "    JOIN schools ON teachers.school_id = schools.id "
				+ "    JOIN classes ON teacher_ratings.class_id = classes.id "
				+ "    JOIN departments ON classes.department_id = departments.id "
				+ "    JOIN locations ON schools.location_id = locations.id "
				+ "    JOIN states ON locations.state_id = states.id "
		// + "    JOIN school_ratings ON schools.id = school_ratings.school_id "
		// + "GROUP BY school_ratings.school_reputation, "
		// + "    school_ratings.career_opportunities, "
		// + "    school_ratings.campus_grounds, "
		// + "    school_ratings.quality_of_food, "
		// + "    school_ratings.social_activities, "
		// + "    school_ratings.campus_location, "
		// + "    school_ratings.condition_of_library, "
		// + "    school_ratings.internet_speed, "
		// + "    school_ratings.clubs_and_events, "
		// + "    school_ratings.school_happiness"
		;

		System.out.println(query);
		try
		{
			File propsFile = new File("DatabaseUtils.props");
			System.out.println(propsFile + " " + propsFile.getAbsolutePath());

			InstanceQuery instanceQuery = new InstanceQuery();
			instanceQuery.setCustomPropsFile(propsFile);
			instanceQuery.setUsername("root");
			instanceQuery.setPassword("");
			instanceQuery.setQuery(query);
			Instances data = instanceQuery.retrieveInstances();

			// Convert easiness ratings to categorical values we only want to
			// predict in discrete terms
			NumericToNominal numToNom = new NumericToNominal();
			numToNom.setAttributeIndices("1");
			numToNom.setInputFormat(data);
			data = Filter.useFilter(data, numToNom);

			// Set our class attribute
			data.setClassIndex(0);

			ModelBuilder.printAttributeNames(data);

			// Sample a subset of the data
			Resample resampler = new Resample();
			resampler.setInputFormat(data);
			resampler.setRandomSeed(ModelBuilder.seed);
			resampler.setSampleSizePercent(sample);
			data = Filter.useFilter(data, resampler);

			ModelBuilder.printAttributeNames(data);

			// Remove class index so that we can convert all nominal values to
			// binary representations, including easiness
			data.setClassIndex(-1);

			System.out.println("got data instances: " + data.size());
			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static Instances getNominalData(double sample)
	{
		Instances data = ModelBuilder.getData(sample);

		System.out.println("initial data num attrs: " + data.numAttributes());
		// data = ModelBuilder.prepareCommentVector(data);
		// System.out.println("prepareCommentVector num attrs: "
		// + data.numAttributes());
		data = ModelBuilder.prepareWithoutDate(data);

		return data;
	}

	public static Classifier loadModel(String name)
	{
		try
		{
			System.out.println("======================================");
			System.out.println("Loading model: " + name);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					name));
			Classifier cls = (Classifier) ois.readObject();
			ois.close();
			return cls;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Instances prepareCommentVector(Instances data)
	{
		try
		{
			// Convert comments into a word vector
			int commentIndex = data.numAttributes();
			ModelBuilder.printAttribute(data, commentIndex - 1);

			NominalToString nomToStr = new NominalToString();
			nomToStr.setAttributeIndexes(commentIndex + "");
			nomToStr.setInputFormat(data);
			data = Filter.useFilter(data, nomToStr);

			ModelBuilder.printAttribute(data, commentIndex - 1);

			StringToWordVector strToVec = new StringToWordVector();
			strToVec.setInputFormat(data);
			strToVec.setIDFTransform(true);
			strToVec.setLowerCaseTokens(true);
			strToVec.setAttributeNamePrefix("comment");
			strToVec.setAttributeIndices(commentIndex + "");
			data = Filter.useFilter(data, strToVec);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}

	public static Instances prepareNominalToBinary(Instances data)
	{
		try
		{
			ArrayList<Integer> indices = new ArrayList<>();

			for (int i = 0; i < data.numAttributes(); i++)
			{
				Attribute attr = data.attribute(i);
				if (attr.isNominal())
				{
					indices.add(i + 1);
				}
			}

			// Convert nominal attributes into binary representations
			NominalToBinary nomToBin = new NominalToBinary();
			nomToBin.setAttributeIndices(ModelBuilder.combine(
					indices.toArray(), ","));
			nomToBin.setTransformAllValues(true);
			nomToBin.setInputFormat(data);
			data = Filter.useFilter(data, nomToBin);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

	public static Instances prepareNumericToBinary(Instances data)
	{
		try
		{
			NumericToBinary numToBin = new NumericToBinary();
			numToBin.setInputFormat(data);
			data = Filter.useFilter(data, numToBin);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}

	public static Instances prepareWithoutDate(Instances data)
	{
		// Remove date attribute for now.
		// Ideally we would want to convert date to several columns, binary
		// (year,
		// month, day, quarter, week, etc)
		data.deleteAttributeType(Attribute.DATE);

		return data;
	}

	public static void printAttribute(Instances data, int index)
	{
		System.out.println("attr: " + index + ", "
				+ data.attribute(index).name() + " ("
				+ Attribute.typeToString(data.attribute(index).type()) + ")");
	}

	public static void printAttributeNames(Instances data)
	{
		System.out.println("======================================");
		System.out.println("Attributes: " + data.numAttributes());
		for (int i = 0; i < data.numAttributes(); i++)
		{
			Attribute attr = data.attribute(i);
			System.out.println("Attr:[" + i + "] " + attr.name() + " ("
					+ Attribute.typeToString(attr.type()) + ")");
		}
	}

	public static void saveModel(Classifier cls, String name)
	{
		try
		{
			System.out.println("======================================");
			System.out.println("Saving model: " + name);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(name));
			oos.writeObject(cls);
			oos.flush();
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
