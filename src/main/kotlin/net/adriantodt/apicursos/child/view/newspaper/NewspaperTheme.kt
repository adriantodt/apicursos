package net.adriantodt.apicursos.child.view.newspaper

import net.adriantodt.apicursos.child.view.base.BootstrapFlavour
import net.adriantodt.apicursos.child.view.base.Theme
import net.adriantodt.apicursos.common.model.Course
import net.adriantodt.apicursos.common.view.View

class NewspaperTheme(val bootstrap: BootstrapFlavour) : Theme {
    override val name = "Newspaper-${bootstrap.name.toLowerCase().capitalize()}"

    override fun getIndexPage(courses: List<Course>): View {
        return NewspaperPageView(bootstrap, courses)
    }

    override fun getCoursePage(course: Course): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCourseContentPage(course: Course): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}