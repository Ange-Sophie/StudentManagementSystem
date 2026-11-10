
package com.Student.repository;

import com.Student.model.Student;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {

    private final DataSource dataSource;

    public StudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // INSERT student
    public void save(Student student) {
        String sql = "INSERT INTO students (full_name, age, email) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getFullName());
            statement.setInt(2, student.getAge());
            statement.setString(3, student.getEmail());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    student.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not save student: " + e.getMessage(), e);
        }
    }

    // SELECT all students
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT id, full_name, age, email FROM students ORDER BY id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                students.add(mapStudent(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve students", e);
        }

        return students;
    }

    // SELECT student by ID
    public Student findById(int id) {
        String sql = "SELECT id, full_name, age, email FROM students WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapStudent(result);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find student", e);
        }

        return null;
    }

    private Student mapStudent(ResultSet result) throws SQLException {
        String[] names = result.getString("full_name").split(" ", 2);

        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";

        return new Student(
                result.getInt("id"),
                firstName,
                lastName,
                result.getString("email"),
                result.getInt("age")
        );
    }
}
