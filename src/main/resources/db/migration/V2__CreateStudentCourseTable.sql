create table if not exists course (
    course_id uuid not null primary key,
    course_name varchar(100) not null unique,
    description text not null,
    department varchar(100),
    teacher_name varchar(100)
);

create table if not exists student_course (
    student_id uuid not null references student(student_id),
    course_id uuid not null references course(course_id),
    star_date date not null,
    end_date date not null,
    grade integer check (grade <= 100 and grade >= 0),
--    Student should not be enrolled on the same course twice
    unique (student_id, course_id)
);