package com.ashay.assignment2.repository;


import com.ashay.assignment2.entity.StudentTeacher;
import com.ashay.assignment2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTeacherRepository extends JpaRepository<StudentTeacher, Long> {

    @Query("SELECT st.teacher FROM StudentTeacher st WHERE st.student.id = :studentId")
    List<Teacher> findTeacherByStudentId(@Param("studentId") Long studentId);
}


