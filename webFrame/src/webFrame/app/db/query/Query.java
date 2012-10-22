package webFrame.app.db.query;

public interface Query {

	public abstract Query where(String where);

	public abstract Query and(String where);

	public abstract Query and();

	public abstract Query or(String where);

	public abstract Query not();

	public abstract Query in(Object... result);

	public abstract Query between(Object... result);

	public abstract Query eq(Object result);

	public abstract Query notEq(Object result);

	public abstract Query gt(Object result);

	public abstract Query ge(Object result);

	public abstract Query lt(Object result);

	public abstract Query le(Object result);

	public abstract Query isNull();

	public abstract Query isNotNull();

	public abstract Query like(String result);

	public abstract Query asc(String where);

	public abstract Query desc(String where);

	public abstract Query add(String sql);

	public abstract String getSql();

	public abstract String getTableName();

	public abstract Class<?> get_class();
	

}
