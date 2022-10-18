package ru.yandex.diplom_2.user;
import com.github.javafaker.Faker;

public class User {
    public static Faker faker = new Faker();
    public String email;
    public String password;
    public String name;

    public User () {
    }

    public User (String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public User setEmail (String email){
        this.email = email;
        return this;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    public User setName (String name) {
        this.name = name;
        return this;
    }

    public static User getRandomUser() {
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password();
        final String firstName = faker.name().firstName();
        return new User (email, password, firstName);
    }

    public static User getUserWithEmptyEmail() {
        final String email = "";
        final String password = faker.internet().password();
        final String firstName = faker.name().firstName();
        return new User (email, password, firstName);
    }

    public static User getUserWithEmptyPassword() {
        final String email = faker.internet().emailAddress();
        final String password = "";
        final String firstName = faker.name().firstName();
        return new User (email, password, firstName);
    }

    public static User getUserWithEmptyName() {
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password();;
        final String firstName = "";
        return new User (email, password, firstName);
    }

    public static User getUserRandomEmail() {

        return new User().setEmail(faker.internet().emailAddress());
    }
    public static User getUserRandomPassword() {

        return new User().setPassword(faker.internet().password());
    }
    public static User getUserRandomName() {
        return new User().setName(faker.name().firstName());
    }

    public static User getUserWithoutName () {
        return new User().setPassword(faker.internet().password()).setEmail(faker.internet().emailAddress());
    }
    public static User getUserWithoutPassword () {
        return new User().setName(faker.name().firstName()).setEmail(faker.internet().emailAddress());
    }
    public static User getUserWithoutEmail() {
        return new User().setPassword(faker.internet().password()).setName(faker.name().firstName());
    }
}
