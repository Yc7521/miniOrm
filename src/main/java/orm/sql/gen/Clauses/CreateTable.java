package orm.sql.gen.Clauses;

public class CreateTable<T> {
    private final Class<T> entityClass;

    public CreateTable(Class<T> clazz) {
        this.entityClass = clazz;
    }

    public String generate() {
        return "";
    }
}
