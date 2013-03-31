package predict;

import java.io.File;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SGDText;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Cobweb;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Resample;

public class Grade
{

	// TODO (classifier) DMNBtext
	// TODO (ensemble) RacedIncrementalLogitBoost

	private static double cobwebAcuity = 1.0;
	private static double cobwebCutoff = 0.002;
	private static int randomSeed = 42;
	private static int sampleSizePercentage = 2;

	public static void cluster()
	{
		try
		{
			Instances data = Grade.getData();

			Resample sampler = new Resample();
			sampler.setInputFormat(data);
			sampler.setSampleSizePercent(Grade.sampleSizePercentage);
			Instances sample = Filter.useFilter(data, sampler);

			System.out.println("resampled instances: " + sample.size());

			System.out.println("Building cobweb");
			String[] options = weka.core.Utils.splitOptions(String.format(
					"-A %f -C %f -S %d", Grade.cobwebAcuity,
					Grade.cobwebCutoff, Grade.randomSeed));
			Cobweb cw = new Cobweb();
			cw.setOptions(options);
			cw.buildClusterer(sample);
			System.out.println(cw);

			System.out.println("Evaluation cluster");
			ClusterEvaluation eval = new ClusterEvaluation();
			eval.setClusterer(cw);
			eval.evaluateClusterer(data);
			System.out.println("# of clusters: " + eval.getNumClusters());
			System.out.println(eval);
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
				+ "    teacher_ratings.comment, schools.name AS schools_name, teachers.last_name, "
				+ "    teachers.first_name, teacher_ratings.rmp_id, "
				+ "    teacher_ratings.easiness, teacher_ratings.helpfulness, "
				+ "    teacher_ratings.clarity, teacher_ratings.rater_interest, "
				+ "    teacher_ratings.date, classes.level, departments.name AS departments_name "
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
			System.out.println("got data instances: " + data.size());
			return data;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public void classify(Classifier classifier, Instances unlabeled)
	{
		try
		{
			// set class attribute
			unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

			// create copy
			Instances labeled = new Instances(unlabeled);

			// label instances
			for (int i = 0; i < unlabeled.numInstances(); i++)
			{
				double clsLabel = classifier.classifyInstance(unlabeled
						.instance(i));
				labeled.instance(i).setClassValue(clsLabel);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void crossValidate(Classifier classifier, Instances data)
	{
		Evaluation eval;
		try
		{
			eval = new Evaluation(data);
			eval.crossValidateModel(classifier, data, 10, new Random(1));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void trainNaiveBayes(Instances data)
	{
		// http://weka.sourceforge.net/doc.dev/weka/classifiers/bayes/NaiveBayesMultinomialText.html
		try
		{
			// TODO correct options
			String[] options = weka.core.Utils.splitOptions("");
			NaiveBayesMultinomialText bayesClassifier = new NaiveBayesMultinomialText();
			bayesClassifier.setOptions(options);
			bayesClassifier.buildClassifier(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void trainSGD(Instances data)
	{
		// http://weka.sourceforge.net/doc.dev/weka/classifiers/functions/SGDText.html
		try
		{
			// TODO correct options
			String[] options = weka.core.Utils
					.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\"");
			SGDText sgdClassifier = new SGDText();
			sgdClassifier.setOptions(options);
			sgdClassifier.buildClassifier(data);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
