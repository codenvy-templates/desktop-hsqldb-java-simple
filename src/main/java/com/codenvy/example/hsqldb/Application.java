package com.codenvy.example.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:test", "SA", "");
        Statement statement = connection.createStatement();

        System.out.println("Create new table 'test'.");
        statement.execute("CREATE TABLE test(property varchar(255), property_value varchar(255))");

        System.out.println("Insert values.");
        statement.execute("INSERT INTO test VALUES('first_property', 'some value')");
        statement.execute("INSERT INTO test(property, property_value) VALUES('second_property', 'some another value')");

        String parametrizedQuery = "insert into test(property, property_value) values(?, ?)";
        PreparedStatement parametrizedStatement = connection.prepareStatement(parametrizedQuery);
        parametrizedStatement.setString(1, "third_property");
        parametrizedStatement.setString(2, "awesome");
        parametrizedStatement.execute();

        System.out.println("Select all rows from 'test':");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM test");
        while (resultSet.next()) {
            System.out.println(String.format("\tproperty: %20s | value:%20s",
                                             resultSet.getString("property"),
                                             resultSet.getString("property_value")));
        }

        System.out.println("Update property 'second_property' with new value.");
        statement.execute("UPDATE test SET property_value='new value' WHERE property='second_property'");

        System.out.println("Delete from 'test' property 'third_property'.");
        statement.execute("DELETE FROM test WHERE property='third_property'");

        System.out.println("Select all rows from 'test':");
        resultSet = statement.executeQuery("SELECT * FROM test");
        while (resultSet.next()) {
            System.out.println(String.format("\tproperty: %20s | value:%20s",
                                             resultSet.getString("property"),
                                             resultSet.getString("property_value")));
        }

        statement.execute("SHUTDOWN");
        statement.close();
    }
}
