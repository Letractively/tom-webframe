package webFrame.app.db.query;

public interface Query {
	
	/**
	 * Restrictions.eq --> equal,等于.
Restrictions.allEq --> 参数为Map对象,使用key/value进行多个等于的比对,相当于多个Restrictions.eq的效果
Restrictions.gt --> great-than > 大于
Restrictions.ge --> great-equal >= 大于等于
Restrictions.lt --> less-than, < 小于
Restrictions.le --> less-equal <= 小于等于
Restrictions.between --> 对应SQL的between子句
Restrictions.like --> 对应SQL的LIKE子句
Restrictions.in --> 对应SQL的in子句
Restrictions.and --> and 关系
Restrictions.or --> or 关系
Restrictions.isNull --> 判断属性是否为空,为空则返回true
Restrictions.isNotNull --> 与isNull相反
Restrictions.sqlRestriction --> SQL限定的查询
Order.asc --> 根据传入的字段进行升序排序
Order.desc --> 根据传入的字段进行降序排序
MatchMode.EXACT --> 字符串精确匹配.相当于"like 'value'"
MatchMode.ANYWHERE --> 字符串在中间匹配.相当于"like '%value%'"
MatchMode.START --> 字符串在最前面的位置.相当于"like 'value%'"
MatchMode.END --> 字符串在最后面的位置.相当于"like '%value'"


     =                     Restrictions.eq()                  等于
     <>                   Restrictions.not(Exprission.eq())  不等于
     >                     Restrictions.gt()                  大于
     >=                   Restrictions.ge()                  大于等于
     <                     Restrictions.lt()                  小于
     <=                   Restrictions.le()                  小于等于
     is null             Restrictions.isnull()              等于空值
     is not null      Restrictions.isNotNull()           非空值
     like                 Restrictions.like()                字符串模式匹配
     and                Restrictions.and()                 逻辑与
     and                Restrictions.conjunction()         逻辑与
     or                   Restrictions.or()                  逻辑或
     or                   Restrictions.disjunction()         逻辑或
     not                  Restrictions.not()                 逻辑非
     in(列表)          Restrictions.in()                  等于列表中的某一个值
     ont in(列表)         Restrictions.not(Restrictions.in())不等于列表中任意一个值
     between x and y      Restrictions.between()             闭区间xy中的任意值
     not between x and y  Restrictions.not(Restrictions..between()) 小于值X或者大
	 * 
	 * 
	 */
	
	
	public abstract Query where();
	public abstract Query and();
	public abstract Query or();
	public abstract Query not();
	public abstract Query in(String where, Object... result);
	public abstract Query between(String where, Object... result);
	
	public abstract Query  eq(String where,Object result);
	public abstract Query  notEq(String where,Object result);
	public abstract Query  gt(String where,Object result); 
	public abstract Query  ge(String where,Object result); 
	public abstract Query  lt(String where,Object result) ; 
	public abstract Query  le(String where,Object result) ; 
	public abstract Query  isnull(String where); 
	public abstract Query  isNotNull(String where); 
	public abstract Query  like(String where, String result); 
	public abstract Query  asc(String where); 
	public abstract Query  desc(String where); 
	
	

}
