package database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Model
{

	public class ModelExclusionStrategy implements ExclusionStrategy
	{

		@Override
		public boolean shouldSkipClass(Class<?> arg0)
		{
			return false;
		}

		@Override
		public boolean shouldSkipField(FieldAttributes f)
		{
			return f.getName().equals("dirty") || f.getName().equals("fresh")
					|| f.getName().equals("fields")
					|| f.getName().equals("relations")
					|| f.getName().equals("table");
		}

	}

	protected boolean dirty = false;

	protected final ArrayList<String> fields;
	protected boolean fresh = false;
	protected final ArrayList<String> relations;

	protected final String table;

	protected Model(String table, ArrayList<String> fields,
			ArrayList<String> relations, Boolean dirty, Boolean fresh)
	{
		if (table == null)
		{
			throw new IllegalArgumentException("Models require a table");
		}
		if (fields == null)
		{
			throw new IllegalArgumentException(
					"Models require a list of fields");
		}
		this.table = table;
		this.fields = fields;
		this.relations = relations;
		this.dirty = dirty;
		this.fresh = fresh;
	}

	protected ArrayList<Model> findAll(Class<? extends Model> caller)
	{
		System.out.println("model find all");

		Connection connection = DBFactory.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<Model> collection = new ArrayList<>();

		try
		{

			String query = "SELECT " + this.fields + " FROM " + this.table;
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				Model model = caller.getConstructor(caller).newInstance(
						this.table, this.fields, this.relations);

				for (String fieldName : this.fields)
				{
					Field field = caller.getField(fieldName);
					Class<?> type = field.getType();
					Object value = resultSet.getClass()
							.getMethod("get" + type.getName())
							.invoke(resultSet, fieldName);
					field.set(model, value);
				}

				collection.add(model);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
				DBFactory.closeConnection(connection);
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				System.exit(1);
			}
		}
		return collection;
	}

	/**
	 * This means that a models properties have been changed, but we haven't
	 * saved this model since changing them.
	 * 
	 * @return
	 */
	public final boolean isDirty()
	{
		return this.dirty;
	}

	/**
	 * This means a model is brand new and no database record exists.
	 * 
	 * @return
	 */
	public final boolean isFresh()
	{
		return this.fresh;
	}

	public abstract String isValid();

	public abstract boolean save() throws InvalidModelException;

	public String toJson()
	{

		Gson gson = new GsonBuilder().setExclusionStrategies(
				new ModelExclusionStrategy()).create();
		return gson.toJson(this);
	}

}
