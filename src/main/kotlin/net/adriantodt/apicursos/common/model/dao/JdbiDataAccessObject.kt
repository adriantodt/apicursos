package net.adriantodt.apicursos.common.model.dao

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.adriantodt.apicursos.common.model.*
import net.adriantodt.apicursos.common.model.dao.DataAccessObject.Companion.hashPassword
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.*
import org.jdbi.v3.jackson2.Jackson2Config
import org.jdbi.v3.jackson2.Jackson2Plugin
import org.jdbi.v3.postgres.PostgresPlugin

class JdbiDataAccessObject(url: String) : DataAccessObject {
    val jdbi = Jdbi.create(url)
        .installPlugin(PostgresPlugin())
        .installPlugin(Jackson2Plugin())
        .installPlugin(KotlinPlugin())
        .apply { getConfig(Jackson2Config::class.java).mapper.registerKotlinModule() }

    init {
        jdbi.useHandleUnchecked {
            it.execute("""
                CREATE TABLE IF NOT EXISTS apicursos_user(
                    userId BIGSERIAL PRIMARY KEY,
                    userType TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    hashedPassword TEXT NOT NULL,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE
                )
              """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS apicursos_site(
                    siteId BIGSERIAL PRIMARY KEY,
                    hostname TEXT UNIQUE,
                    ownerId BIGINT REFERENCES apicursos_user(userId) ON DELETE CASCADE,
                    name TEXT NOT NULL,
                    hasLogo BOOLEAN NOT NULL DEFAULT FALSE
                )
              """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS apicursos_course(
                    courseId BIGSERIAL PRIMARY KEY,
                    siteId BIGINT REFERENCES apicursos_site(siteId) ON DELETE CASCADE,
                    url TEXT NOT NULL,
                    title TEXT NOT NULL,
                    teaseHtmlBefore TEXT,
                    teaseHtmlAfter TEXT,
                    teaseImgHash TEXT,
                    teaseImgExt TEXT,
                    htmlBefore TEXT,
                    htmlAfter TEXT,
                    collectName BOOLEAN NOT NULL DEFAULT FALSE,
                    collectEmail BOOLEAN NOT NULL DEFAULT FALSE,
                    collectPhone BOOLEAN NOT NULL DEFAULT FALSE,
                    collectCity BOOLEAN NOT NULL DEFAULT FALSE,
                    collectState BOOLEAN NOT NULL DEFAULT FALSE,
                    collectCountry BOOLEAN NOT NULL DEFAULT FALSE,
                    contentType TEXT,
                    contentHash TEXT,
                    contentExt TEXT,
                    UNIQUE(siteId, url)
                )
                """.trimIndent())

            it.execute("""
                CREATE TABLE IF NOT EXISTS apicursos_collecteddata(
                    dataId BIGSERIAL PRIMARY KEY,
                    
                    siteId BIGINT REFERENCES apicursos_site(siteId) ON DELETE CASCADE,
                    courseId BIGINT REFERENCES apicursos_course(courseId),
                    
                    name TEXT,
                    email TEXT,
                    phone TEXT,
                    city TEXT,
                    state TEXT,
                    country TEXT
                )
                """.trimIndent())
        }
    }

    override fun getUser(userId: Long): User? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_user WHERE userId = :userId")
                .bind("userId", userId)
                .mapTo<User>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getSite(siteId: Long): Site? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_site WHERE siteId = :siteId")
                .bind("siteId", siteId)
                .mapTo<Site>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getCourse(courseId: Long): Course? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_course WHERE courseId = :courseId")
                .bind("courseId", courseId)
                .mapTo<Course>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getCollectedData(dataId: Long): CollectedData? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_collecteddata WHERE dataId = :dataId")
                .bind("dataId", dataId)
                .mapTo<CollectedData>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getUserByUsernameOrEmail(input: String): User? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_user WHERE username = :input OR email = :input")
                .bind("input", input)
                .mapTo<User>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getSiteByHostname(hostname: String): Site? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_site WHERE hostname = :hostname")
                .bind("hostname", hostname)
                .mapTo<Site>()
                .findOne()
                .orElse(null)
        }
    }

    override fun getCursoByUrl(siteId: Long, url: String): Course? {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_course WHERE siteId = :siteId AND url = :url")
                .bind("siteId", siteId)
                .bind("url", url)
                .mapTo<Course>()
                .findOne()
                .orElse(null)
        }
    }

    override fun allUsers(): List<User> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_user")
                .mapTo<User>()
                .list()
        }
    }

    override fun allSites(): List<Site> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_site")
                .mapTo<Site>()
                .list()
        }
    }

    override fun allSites(userId: Long): List<Site> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_site WHERE ownerId = :userId")
                .bind("userId", userId)
                .mapTo<Site>()
                .list()
        }
    }

    override fun allCourses(siteId: Long): List<Course> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_course WHERE siteId = :siteId")
                .bind("siteId", siteId)
                .mapTo<Course>()
                .list()
        }
    }

    override fun allCollectedData(): List<CollectedData> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_collecteddata")
                .mapTo<CollectedData>()
                .list()
        }
    }

    override fun allCollectedDataByUser(userId: Long): List<CollectedData> {
        return jdbi.withHandleUnchecked {
            it.createQuery("""
                SELECT apicursos_collecteddata.* FROM apicursos_collecteddata
                INNER JOIN apicursos_site ON apicursos_collecteddata.siteName = apicursos_site.siteName
                WHERE apicursos_site.ownerId = :userId
                """.trimIndent())
                .bind("userId", userId)
                .mapTo<CollectedData>()
                .list()
        }
    }

    override fun allCollectedDataBySite(siteId: Long): List<CollectedData> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_collecteddata WHERE siteId = :siteId")
                .bind("siteId", siteId)
                .mapTo<CollectedData>()
                .list()
        }
    }

    override fun allCollectedDataByCourse(courseId: Long): List<CollectedData> {
        return jdbi.withHandleUnchecked {
            it.createQuery("SELECT * FROM apicursos_collecteddata WHERE courseId = :courseId")
                .bind("courseId", courseId)
                .mapTo<CollectedData>()
                .list()
        }
    }

    override fun insertUser(userType: UserType, name: String, username: String, email: String, password: String): User {
        val hashedPassword = hashPassword(password)

        //HAS SERIAL PRIMARY KEY (Long)
        val userId = jdbi.withHandleUnchecked {
            it.createUpdate("""
                INSERT INTO apicursos_user (username, hashedPassword, userType, name, email)
                VALUES (:username, :hashedPassword, :userType, :name, :email)
                """.trimIndent())
                .bind("username", username)
                .bind("hashedPassword", hashedPassword)
                .bind("userType", userType)
                .bind("name", name)
                .bind("email", email)
                .executeAndReturnGeneratedKeys()
                .mapTo<Long>()
                .one()
        }

        return User(userId, userType, username, hashedPassword, name, email)
    }

    override fun insertSite(hostname: String, ownerId: Long, name: String): Site {
        val siteId = jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO apicursos_site (hostname, ownerId, name) VALUES (:hostname, :ownerId, :name)")
                .bind("hostname", hostname)
                .bind("ownerId", ownerId)
                .bind("name", name)
                .executeAndReturnGeneratedKeys()
                .mapTo<Long>()
                .one()
        }

        return Site(siteId, hostname, ownerId, name)
    }

    override fun insertCourse(siteId: Long, url: String, title: String): Course {
        val courseId = jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO apicursos_course (siteId, url, title) VALUES (:siteId, :url, :title)")
                .bind("siteId", siteId)
                .bind("url", url)
                .bind("title", title)
                .executeAndReturnGeneratedKeys()
                .mapTo<Long>()
                .one()
        }

        return Course(courseId, siteId, url, title)
    }

    override fun insertCollectedData(siteId: Long, courseId: Long?): CollectedData {
        //HAS SERIAL PRIMARY KEY (Long)
        val dataId = jdbi.withHandleUnchecked {
            it.createUpdate("INSERT INTO apicursos_collecteddata (ownerId, siteId, courseId) VALUES (:ownerId, :siteId, :courseId)")
                .bind("siteId", siteId)
                .bind("courseId", courseId)
                .executeAndReturnGeneratedKeys()
                .mapTo<Long>()
                .one()
        }

        return CollectedData(dataId, siteId, siteId)
    }

    override fun updateUser(user: User) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE apicursos_user SET
                    userType = :userType,
                    username = :username,
                    hashedPassword = :hashedPassword,
                    name = :name,
                    email = :email
                WHERE userId = :userId
                """.trimIndent())
                .bindKotlin(user)
                .execute()
        }
    }

    override fun updateSite(site: Site) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE apicursos_site SET
                    hostname = :hostname,
                    ownerId = :ownerId,
                    name = :name,
                    hasLogo = :hasLogo
                WHERE siteId = :siteId
                """.trimIndent())
                .bindKotlin(site)
                .execute()
        }
    }

    override fun updateCourse(course: Course) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE apicursos_course SET
                    url = :url,
                    title = :title,
                    teaseHtmlBefore = :teaseHtmlBefore,
                    teaseHtmlAfter = :teaseHtmlAfter,
                    teaseImgHash = :teaseImgHash,
                    teaseImgExt = :teaseImgExt,
                    htmlBefore = :htmlBefore,
                    htmlAfter = :htmlAfter,
                    collectName = :collectName,
                    collectEmail = :collectEmail,
                    collectPhone = :collectPhone,
                    collectCity = :collectCity,
                    collectState = :collectState,
                    collectCountry = :collectCountry,
                    contentType = :contentType,
                    contentHash = :contentHash,
                    contentExt = :contentExt
                WHERE courseId = :courseId
                """.trimIndent())
                .bindKotlin(course)
                .execute()
        }
    }

    override fun updateCollectedData(collectedData: CollectedData) {
        jdbi.useHandleUnchecked {
            it.createUpdate("""
                UPDATE apicursos_collecteddata SET                    
                    name = :name,
                    email = :email,
                    phone = :phone,
                    city = :city,
                    state = :state,
                    country = :country
                WHERE dataId = :dataId
                """.trimIndent())
                .bindKotlin(collectedData)
                .execute()
        }
    }

    override fun removeUser(userId: Long) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM apicursos_user WHERE userId = ?", userId)
        }
    }

    override fun removeSite(siteId: Long) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM apicursos_site WHERE siteId = ?", siteId)
        }
    }

    override fun removeCourse(courseId: Long) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM apicursos_course WHERE courseId = ?", courseId)
        }
    }

    override fun removeCollectedData(dataId: Long) {
        jdbi.useHandleUnchecked {
            it.execute("DELETE FROM apicursos_collecteddata WHERE dataId = ?", dataId)
        }
    }
}