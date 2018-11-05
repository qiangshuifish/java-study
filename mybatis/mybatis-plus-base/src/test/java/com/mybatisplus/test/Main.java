package com.mybatisplus.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

class Student {
    public Student(long id, String name, int age, int grade, String major, String school) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.major = major;
        this.school = school;
    }

    public Student(String name) {
        this.name = name;
    }

    public Student() {
    }

    private List<String> attributes = Arrays.asList("111", "222", "333");
    /**
     * 学号
     */
    private long id;
    private String name;
    private int age;
    /**
     * 年级
     */
    private int grade;
    /**
     * 专业
     */
    private String major;
    /**
     * 学校
     */
    private String school;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<Student>();
        students.add(new Student(20160001, "孔明4", 20, 1, "土木工程", "武汉大学"));
        students.add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
        students.add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
        students.add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
        students.add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
        students.add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
        students.add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
        students.add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
        students.add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
        students.add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
        //mapToLong的使用demo
        double value = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .mapToLong(aaa -> aaa.getId())
                .sum();


        String[] strs = {"java8", "is", "easy", "to", "use"};
        List<String[]> distinctStrs = Arrays.stream(strs)
                .map(str -> str.split(""))
                // 映射成为Stream<String[]>
                .distinct()
                .collect(Collectors.toList());
        distinctStrs.forEach(strings -> Arrays.stream(strings).forEach(s -> System.out.println(s)));
        System.out.println("==================================map方式");



        List<String> distinctStrsByFlatMap = Arrays.stream(strs)
                .map(str -> str.split(""))  // 映射成为Stream<String[]>
                .flatMap(Arrays::stream)  // 扁平化为Stream<String>
                .distinct()
                .collect(Collectors.toList());
        distinctStrsByFlatMap.forEach(s -> System.out.println(s));
        System.out.println("==================================flatMap的方式");


        List<List<Student>> studentss = new ArrayList<>();
        studentss.add(students);
        studentss.add(students);
        System.out.println(studentss.size());
        System.out.println("经过了flatmap处理后的stream查看下尺寸是多少" + studentss.stream().flatMap(students1 -> students1.stream()).count());

        System.out.println(studentss.stream().flatMap(students1 -> students1.stream()).max((o1, o2) -> (o1.getName().length() - o2.getName().length())).get().getName());
        System.out.println("经过了flatMap进行了彻底的流处理查看下有多少流" + studentss.stream().flatMap(students1 -> students1.stream()).flatMap(student -> student.getAttributes().stream()).count());
        studentss.stream().flatMap(students1 -> students1.stream()).flatMap(student -> student.getAttributes().stream()).forEach(s -> System.out.println(s));
    }
}
