package predict;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Grade
{

	public static void generateArff()
	{
		try
		{
			Instances instances = Grade.getData();
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

	public static void generateModel()
	{
		try
		{
			Classifier cls = new NaiveBayesMultinomialText();

			// train
			Instances data = Grade.getData();

			int seed = 42;
			int folds = 10;

			Random rand = new Random(seed);
			Instances randData = new Instances(data);
			randData.randomize(rand);
			randData.stratify(folds);

			for (int n = 0; n < folds; n++)
			{
				Instances train = randData.trainCV(folds, n);
				Instances test = randData.testCV(folds, n);

				data.setClassIndex(4); // easiness
				cls.buildClassifier(train);
				System.out.println("Classifier " + n + ":\n" + cls);
				cls.classifyInstance(test.firstInstance());

				// serialize model
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream("data/easiness-prediction-" + n
								+ ".model"));
				oos.writeObject(cls);
				oos.flush();
				oos.close();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Instances getData()
	{
		String query = "SELECT "
				// + "    teacher_ratings.comment, "
				+ "    schools.name AS schools_name, teachers.last_name, "
				+ "    teachers.first_name, "
				+ "    teacher_ratings.easiness, teacher_ratings.helpfulness, "
				+ "    teacher_ratings.clarity, teacher_ratings.rater_interest, "
				+ "    teacher_ratings.date, classes.level, departments.name AS departments_name,"
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
			numToNom.setAttributeIndices("4,5,6,7");
			numToNom.setInputFormat(data);
			data = Filter.useFilter(data, numToNom);

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

	public static void spamClassifier()
	{
		// String classString = "weka.classifiers.bayes.NaiveBayes";
		String thisClassString = "weka.classifiers.lazy.IBk";

		String[] inputText =
		{ "hey, buy this from me!", "do you want to buy?",
				"I have a party tonight!", "today it is a nice weather",
				"you are best", "I have a horse", "you are my friend",
				"buy, buy, buy!", "it is spring in the air",
				"do you want to come?" };

		String[] inputClasses =
		{ "spam", "spam", "no spam", "no spam", "spam", "no spam", "no spam",
				"spam", "no spam", "no spam" };

		String[] testText =
		{ "you want to buy from me?", "usually I run in stairs", "buy it now!",
				"buy, buy, buy!", "you are the best, buy!",
				"it is spring in the air" };

		if (inputText.length != inputClasses.length)
		{
			System.err
					.println("The length of text and classes must be the same!");
			System.exit(1);
		}

		// calculate the classValues
		HashSet classSet = new HashSet(Arrays.asList(inputClasses));
		classSet.add("?");
		String[] classValues = (String[]) classSet.toArray(new String[0]);

		//
		// create class attribute
		//
		FastVector classAttributeVector = new FastVector();
		for (String classValue : classValues)
		{
			classAttributeVector.addElement(classValue);
		}
		Attribute thisClassAttribute = new Attribute("class",
				classAttributeVector);

		//
		// create text attribute
		//
		FastVector inputTextVector = null;  // null -> String type
		Attribute thisTextAttribute = new Attribute("text", inputTextVector);
		for (String element : inputText)
		{
			thisTextAttribute.addStringValue(element);
		}

		// add test cases (to be inserted into instances)
		// just a singular test string
		/*
		 * String newTextString = newTestTextField.getText();
		 * String[] newTextArray = new String[1];
		 * newTextArray[0] = newTextString;
		 * if (!"".equals(newTextString)) {
		 * thisTextAttribute.addStringValue(newTextString);
		 * }
		 */

		// add the text of test cases
		for (String element : testText)
		{
			thisTextAttribute.addStringValue(element);
		}

		//
		// create the attribute information
		//
		FastVector thisAttributeInfo = new FastVector(2);
		thisAttributeInfo.addElement(thisTextAttribute);
		thisAttributeInfo.addElement(thisClassAttribute);

		TextClassifier classifier = new TextClassifier(inputText, inputClasses,
				thisAttributeInfo, thisTextAttribute, thisClassAttribute,
				thisClassString);

		System.out.println("DATA SET:\n");
		System.out.println(classifier.classify(thisClassString));

		System.out.println("NEW CASES:\n");
		System.out.println(classifier.classifyNewCases(testText));
	}

}
