package database;

import java.util.ArrayList;

public abstract class Model
{

	protected boolean dirty = false;
	protected boolean fresh = false;

	public static <T extends Model> ArrayList<T> findAll()
	{
		return null;
	}

	public final boolean isDirty()
	{
		return this.dirty;
	}

	public final boolean isFresh()
	{
		return this.fresh;
	}

	public abstract String isValid();

	public abstract boolean save() throws InvalidModelException;
}
