package com.aiwa.sm.student;

import com.aiwa.sm.exceptions.ApiRequestException;
import com.aiwa.sm.utils.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentDataAccess mStudentDataAccess;
    private final EmailValidator mEmailValidator;

    @Autowired
    public StudentService(StudentDataAccess studentDataAccess, EmailValidator emailValidator) {
        mStudentDataAccess = studentDataAccess;
        this.mEmailValidator = emailValidator;
    }

    List<Student> getAllStudents() {
        return mStudentDataAccess.selectAllStudents();
    }

    void addNewStudent(Student student) {
        addNewStudent(null, student);
    }

    void addNewStudent(UUID studentId, Student student) {
        UUID uuid = Optional.ofNullable(studentId).orElse(UUID.randomUUID());
        if (!mEmailValidator.test(student.getEmail())) {
            throw new ApiRequestException(student.getEmail().concat(" is not valid!"));
        }
        // TODO: Check that email is not already used
        if (mStudentDataAccess.isEmailAlreadyTaken(student.getEmail())) {
            throw new ApiRequestException(student.getEmail().concat(" is already taken!"));
        }
        mStudentDataAccess.insertStudent(uuid, student);
    }

    List<StudentCourse> getStudentCourses(final UUID student_id) {
        return mStudentDataAccess.fetchStudentCourses(student_id);
    }
}
