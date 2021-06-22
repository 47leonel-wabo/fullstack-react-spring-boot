package com.aiwa.sm.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDataAccess {

    private final JdbcTemplate mJdbcTemplate;

    @Autowired
    public StudentDataAccess(JdbcTemplate jdbcTemplate) {
        mJdbcTemplate = jdbcTemplate;
    }

    List<Student> selectAllStudents() {
        // Selection query
        String sqlQuery = "SELECT student_id, first_name, last_name, email, gender FROM student";

        return mJdbcTemplate.query(sqlQuery, mapStudentSFromDB());
    }

    int insertStudent(UUID uuid, Student student) {
        String insertQuery = "INSERT INTO student (" +
                "student_id, " +
                "first_name, " +
                "last_name, " +
                "email, " +
                "gender) VALUES (?, ?, ?, ?, ?::gender)"; // Here, gender (the last question mark)
        // is casted according to PostgreSQL gender
        // we've defined (MIGRATION V4)
        return mJdbcTemplate.update(
                insertQuery,
                uuid,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase());
    }

    private RowMapper<Student> mapStudentSFromDB() {
        return (resultSet, index) -> {
            // Extract student's field from result set
            UUID studentId = UUID.fromString(resultSet.getString("student_id"));
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String genderStr = resultSet.getString("gender").toUpperCase();

            // Return a student with all its info
            return new Student(studentId, firstName, lastName, Gender.valueOf(genderStr), email);
        };
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isEmailAlreadyTaken(String email) {
        String selectionQuery = "SELECT EXISTS (SELECT 1 FROM student WHERE email = ?)";
        return mJdbcTemplate
                .queryForObject(
                        selectionQuery,
                        new Object[]{email},
                        (rs, rowNum) -> rs.getBoolean(1)
                );
    }

    public List<StudentCourse> fetchStudentCourses(UUID student_id) {
        String fetchStudentCoursesQuery = "SELECT " +
                "c.course_id, " +
                "s.student_id, " +
                "sc.star_date, " +
                "sc.end_date, " +
                "sc.grade, " +
                "c.course_name, " +
                "c.description, " +
                "c.department, " +
                "c.teacher_name " +
                "FROM student s " +
                "JOIN student_course sc USING(student_id) " +
                "JOIN course c USING(course_id) " +
                "WHERE s.student_id = ?";
        return mJdbcTemplate.query(
                fetchStudentCoursesQuery,
                new Object[]{student_id},
                (rs, rowNum) -> new StudentCourse(
                        UUID.fromString(rs.getString("student_id")),
                        UUID.fromString(rs.getString("course_id")),
                        rs.getDate("star_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        Optional.ofNullable(rs.getString("grade"))
                                .map(Integer::parseInt)
                                .orElse(null),
                        rs.getString("course_name"),
                        rs.getString("description"),
                        rs.getString("department"),
                        rs.getString("teacher_name")
                ));
    }
}
