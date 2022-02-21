package orm.sql.gen.clauses;

import orm.util.meta.Meta;

public class Insert<T> extends Clause<T> {
    public Insert(Meta<T> meta) {
        super(meta);
    }
}
