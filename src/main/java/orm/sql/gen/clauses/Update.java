package orm.sql.gen.clauses;

import orm.util.meta.Meta;

public class Update<T> extends Clause<T> {
    public Update(Meta<T> meta) {
        super(meta);
    }
}
