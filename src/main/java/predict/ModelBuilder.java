package predict;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

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
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

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
			double sampleSize = 1;
			System.out.println("Fetching instances for " + sampleSize
					+ "% of available data");
			Instances data = ModelBuilder.getBinaryData(1);
			System.out.println("Learning on " + data.numAttributes()
					+ " features");

			int index = 0;
			Vote voter = ModelBuilder.buildEnsembleOnAttribute(data, index);

			ModelBuilder.evaluateModel(data, voter);

			String name = "data/easiness-vote-" + (index + 1) + ".model";
			ModelBuilder.saveModel(voter, name);

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
		data = ModelBuilder.prepareCommentVector(data);
		System.out.println("prepareCommentVector num attrs: "
				+ data.numAttributes());
		data = ModelBuilder.prepareWithoutDate(data);

		data = ModelBuilder.prepareNumericToBinary(data);

		return data;
	}

	public static Instances getData(double sample)
	{
		String query = "SELECT "
				+ "    teacher_ratings.easiness," // Note that this is our class
													// attribute
				+ "    schools.name AS schools_name, teachers.last_name, "
				+ "    teachers.first_name, "
				+ "    teacher_ratings.helpfulness, "
				+ "    teacher_ratings.clarity, teacher_ratings.rater_interest, "
				+ "    teacher_ratings.date, "
				+ "    classes.level, departments.name AS departments_name,"
				+ "    teacher_ratings.comment "
				+ "FROM teacher_ratings "
				+ "    JOIN teachers ON teacher_ratings.teacher_id = teachers.id "
				+ "    JOIN schools ON teachers.school_id = schools.id "
				+ "    JOIN classes ON teacher_ratings.class_id = classes.id "
				+ "    JOIN departments ON classes.department_id = departments.id";

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

			// Convert ratings to categorical values since ratings are discrete.
			NumericToNominal numToNom = new NumericToNominal();
			numToNom.setAttributeIndices("1,5,6,7");
			numToNom.setInputFormat(data);
			data = Filter.useFilter(data, numToNom);

			// Set our class attribute
			data.setClassIndex(0);

			// Sample a subset of the data
			Resample resampler = new Resample();
			resampler.setInputFormat(data);
			resampler.setBiasToUniformClass(1);
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
		data = ModelBuilder.prepareCommentVector(data);
		System.out.println("prepareCommentVector num attrs: "
				+ data.numAttributes());
		data = ModelBuilder.prepareWithoutDate(data);

		return data;
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
