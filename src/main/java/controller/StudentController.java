
package com.Student.controller;

import com.Student.model.Student;
import com.Student.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Display registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    // Receive and save student
    @PostMapping("/register")
    public String registerStudent(
            @ModelAttribute("student") Student student,
            Model model) {

        if (student.getFirstName() == null ||
                student.getFirstName().isBlank() ||
                student.getLastName() == null ||
                student.getLastName().isBlank() ||
                student.getEmail() == null ||
                !student.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") ||
                student.getAge() <= 18) {

            model.addAttribute("error",
                    "Please enter valid details. Age must be greater than 18.");
            return "register";
        }

        try {
            studentService.registerStudent(student);
            model.addAttribute("student", new Student());
            model.addAttribute("success", "Student registered successfully!");
        } catch (Exception e) {
            model.addAttribute("error",
                    "Registration failed. Check whether the email already exists.");
        }

        return "register";
    }

    // Display all students
    @GetMapping("/all")
    public String showAllStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    // Bonus: Display student by ID
    @GetMapping("/{id}")
    public String showStudentById(@PathVariable int id, Model model) {

        Student student = studentService.getStudentById(id);

        if (student == null) {
            model.addAttribute("notFound",
                    "No student found with ID: " + id);
        } else {
            model.addAttribute("selectedStudent", student);
        }

        model.addAttribute("students", studentService.getAllStudents());

        return "students";
    }
}
