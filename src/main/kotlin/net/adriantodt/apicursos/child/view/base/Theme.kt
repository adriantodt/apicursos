package net.adriantodt.apicursos.child.view.base

import net.adriantodt.apicursos.common.model.Course
import net.adriantodt.apicursos.common.view.View

interface Theme {
    val name: String

    fun getIndexPage(courses: List<Course>): View

    fun getCoursePage(course: Course): View

    fun getCourseContentPage(course: Course): View
}