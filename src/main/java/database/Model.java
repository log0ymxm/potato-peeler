package database;

public abstract class Model
{

	protected boolean dirty = false;
	protected boolean fresh = false;

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

}
