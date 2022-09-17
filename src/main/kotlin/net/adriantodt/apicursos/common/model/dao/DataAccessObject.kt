package net.adriantodt.apicursos.common.model.dao

import net.adriantodt.apicursos.common.model.*
import java.security.MessageDigest
import java.util.*

interface DataAccessObject {
    fun getUser(userId: Long): User?
    fun getSite(siteId: Long): Site?
    fun getCourse(courseId: Long): Course?
    fun getCollectedData(dataId: Long): CollectedData?

    fun getUserByUsernameOrEmail(input: String): User?
    fun getSiteByHostname(hostname: String): Site?
    fun getCursoByUrl(siteId: Long, url: String): Course?

    fun allUsers(): List<User>
    fun allSites(): List<Site>
    fun allSites(userId: Long): List<Site>
    fun allCourses(siteId: Long): List<Course>
    fun allCollectedData(): List<CollectedData>
    fun allCollectedDataByUser(userId: Long): List<CollectedData>
    fun allCollectedDataBySite(siteId: Long): List<CollectedData>
    fun allCollectedDataByCourse(courseId: Long): List<CollectedData>

    fun insertUser(userType: UserType, name: String, username: String, email: String, password: String): User
    fun insertSite(hostname: String, ownerId: Long, name: String): Site
    fun insertCourse(siteId: Long, location: String, title: String): Course
    fun insertCollectedData(siteId: Long, courseId: Long?): CollectedData

    fun updateUser(user: User)
    fun updateSite(site: Site)
    fun updateCourse(course: Course)
    fun updateCollectedData(collectedData: CollectedData)

    fun removeUser(userId: Long)
    fun removeSite(siteId: Long)
    fun removeCourse(courseId: Long)
    fun removeCollectedData(dataId: Long)

    companion object {
        fun hashPassword(password: String): String {
            return Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            )
        }
    }

}