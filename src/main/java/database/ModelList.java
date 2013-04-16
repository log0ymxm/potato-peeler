package database;

import java.util.ArrayList;

/**
 * Extend ArrayList so that we can serialize to json without sending all fields
 * 
 * @author nrub
 * 
 * @param <E>
 */
public class ModelList<E extends Model> extends ArrayList<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8543949918344813368L;

	/**
	 * This will serialize a collection of models using their toJson method,
	 * which prevents internal fields from being sent over the wire.
	 * 
	 * @return
	 */
	public String toJson()
	{
		System.out.println("list to json");
		StringBuilder str = new StringBuilder();
		str.append("[");
		Boolean first = true;
		for (E item : this)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				str.append(",");
			}
			str.append(item.toJson());
		}
		str.append("]");
		System.out.println(str.toString());
		return str.toString();
	}
}
