package orm.model;

import orm.sql.annotations.Id;
import orm.sql.annotations.Length;
import orm.sql.annotations.NotNull;
import orm.sql.annotations.Table;

import java.util.Objects;

@Table(name = "user")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && Objects.equals(name, user.name) &&
               Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, age);
    }

    @Override
    public String toString() {
        return "User{name='%s', password='%s', age=%d}".formatted(name, password, age);
    }
}
