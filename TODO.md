# mini ORM
## Some Class
> some idea of this ORM

### Data Source
> Aiming to implement a tools like DI(by user)
> Storage a `Map` with type(`Class`)  and data
  * register(T obj)
  * get\<T>()
  * get(Class type)

### ORM utility
> util

#### Reflection
> support base class mapping
> may use `Data Source` for configuration, like type converter and so on
> return a string to put into sql
``` java
switch(obj) {
  case Integer i
  case Double d
  ...
  case String s
  case Date date // this need a DateConverter
  case DateTime // DateTimeConverter
}
```
> get type list from a `Class`
> type dependency may not need to support

#### Sql Generator
* create table
  * using util to get type list
* select
  * easy to support but how to convert to a object
  * may use field name to get value
* update
  * as same as select
* delete
  * not need to specify, just need to implement where clause
* where clause
  * two ways to implement
  * a expression like op list which uses logic operations to bind
  * a attribution to specific sql clause (recommended)
  * a function to translate %(\\%) and \_(\\_)
  * [where](https://www.runoob.com/mysql/mysql-where-clause.html) clause

### ORM
> register a database to `Data Source`
> support database connection
> support mysql dialect
* save(T obj)
  * insert if id is null else update by id
* getAll<T>() -> List<T>
  * select without where clause
* getById<T, U>(U id) -> T
  * return null if not found
* deleteById<T>(T id) -> void
  * delete with where clause
* query(String sql, Object... args)
  * query sql use parameters args
  * may support attribution to implement if I can generate java code

## Some Node
> nothing here
$L_aT^eX$