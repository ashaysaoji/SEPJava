package com.ashay.assignment2.service;


import com.ashay.assignment2.entity.Student;
import com.ashay.assignment2.entity.StudentTeacher;
import com.ashay.assignment2.entity.Teacher;
import com.ashay.assignment2.repository.StudentRepository;
import com.ashay.assignment2.repository.StudentTeacherRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentTeacherRepository studentTeacherRepository;

    public StudentService(StudentRepository studentRepository, StudentTeacherRepository studentTeacherRepository) {
        this.studentRepository = studentRepository;
        this.studentTeacherRepository = studentTeacherRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }


    @Transactional
    public Student update(Long id, Student updated) {
        Student existing = getById(id);
        existing.setName(updated.getName());
        existing.setLastName(updated.getLastName());
        existing.setRollNumber(updated.getRollNumber());
        existing.setEmail(updated.getEmail());
        return studentRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Teacher> getTeacherByStudentId(Long studentId) {
        return studentTeacherRepository.findTeacherByStudentId(studentId);
    }


}
