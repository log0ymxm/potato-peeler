package database;


public abstract class Model
{

	protected boolean dirty = false;
	protected boolean fresh = false;

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
