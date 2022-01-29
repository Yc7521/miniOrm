package orm.model;

import orm.sql.annotations.Id;
import orm.sql.annotations.Length;
import orm.sql.annotations.NotNull;

public class User {
    @Id
    @NotNull
    @Length(32)
    private String name;
    @NotNull
    @Length(64)
    private String password;
    private int age;

    public User() {
    }

    public User(String name, String password, int age) {
        this.name = name;
        this.password = password;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{name='%s', password='%s', age=%d}".formatted(name, password, age);
    }
}
