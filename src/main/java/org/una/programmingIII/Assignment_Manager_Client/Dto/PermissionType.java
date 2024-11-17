package org.una.programmingIII.Assignment_Manager_Client.Dto;

public enum PermissionType {

    GLOBAL_MAINTENANCE,//QUITAR

    //UNIVERSITIES
    CREATE_UNIVERSITIES,
    EDIT_UNIVERSITIES,
    DELETE_UNIVERSITIES,
    VIEW_UNIVERSITIES,

    //FACULTIES
    CREATE_FACULTIES,
    EDIT_FACULTIES,
    DELETE_FACULTIES,
    VIEW_FACULTIES,

    //DEPARTMENTS
    CREATE_DEPARTMENTS,
    EDIT_DEPARTMENTS,
    DELETE_DEPARTMENTS,
    VIEW_DEPARTMENTS,

    //CAREERS
    CREATE_CAREERS,
    EDIT_CAREERS,
    DELETE_CAREERS,
    VIEW_CAREERS,

    //COURSES
    CRUD_COURSES,//QUITAR
    CREATE_COURSES,
    EDIT_COURSES,
    DELETE_COURSES,
    VIEW_COURSES,

    //USERS
    MANAGE_USERS,//QUITAR
    CREATE_USERS,
    EDIT_USERS,
    DELETE_USERS,
    VIEW_USERS,
    VIEW_PROFESSORS,
    VIEW_STUDENTS,
    MANAGE_PERMISSIONS,

    //ASSIGNMENTS
    CREATE_ASSIGNMENTS,
    EDIT_ASSIGNMENTS,
    DELETE_ASSIGNMENTS,
    VIEW_ASSIGNMENTS,
    GRADE_ASSIGNMENTS,
    SUBMIT_ASSIGNMENTS,
    VIEW_GRADES,

    //MISC
    EDIT_PROFILE,

    TEACH_CLASSES,

    SUBMIT_FEEDBACK,

    TAKE_CLASSES,

    REGISTER_STUDENT_COURSES,
}
