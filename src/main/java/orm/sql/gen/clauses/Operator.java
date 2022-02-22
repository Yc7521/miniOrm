package orm.sql.gen.clauses;

public enum Operator {
    EQUAL("="), NOT_EQUAL("<>"), GREATER(">"), LESS("<"), GREATER_EQUAL(">="), LESS_EQUAL(
      "<="), LIKE("LIKE"), IN("IN"), NOT_IN("NOT IN"), IS_NULL("IS NULL"), IS_NOT_NULL(
      "IS NOT NULL");

    private final String operator;

    Operator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return operator;
    }
}