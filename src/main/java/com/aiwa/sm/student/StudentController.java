package com.aiwa.sm.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/students")
@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:3000/")
public class StudentController {

    private final StudentService mStudentService;

    @Autowired
    public StudentController(StudentService studentService) {
        mStudentService = studentService;
    }

    @GetMapping
    public List<Student> allStudents() {
//        throw new ApiRequestException("Oops something goes wrong!");
        return mStudentService.getAllStudents();
    }

    @PostMapping
    public void addStudent(@RequestBody @Valid final Student student) {
        mStudentService.addNewStudent(student);
    }

    @GetMapping(path = "/{student_id}/courses")
    public ResponseEntity<?> getStudentCoursesDetails(@PathVariable final UUID student_id) {
        return new ResponseEntity<>(mStudentService.getStudentCourses(student_id), OK);
    }

}
