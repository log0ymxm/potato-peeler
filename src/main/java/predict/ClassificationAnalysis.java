package predict;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SGDText;
import weka.core.Instances;

public class ClassificationAnalysis
{

	// TODO (classifier) DMNBtext
	// TODO (ensemble) RacedIncrementalLogitBoost

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
