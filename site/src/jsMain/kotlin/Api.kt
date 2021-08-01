import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.KotlinxSerializer

import shared_models.model.Gift
import shared_models.model.Session
import shared_models.model.User
import shared_models.request.*
import shared_models.response.UserInfoAboutSessionResponse

enum class RestRoutes(val route: String) {
    AUTH("/user/authorization"),
    USER_SESSIONS("/session/user/"),
    CREATE_SESSION("/session/create"),
    JOIN_BY_GUID("/session/join"),
    MEMBER_COUNT("/session/usersNumber/"),
    LEAVE_FROM_SESSION("/session/leave"),
    CREATE_GIFT("/gift/create"),
    SIGNUP("/user/registration"),
    SESSION_INFO("/session/userInfo")
}

const val restUrl = "http://localhost:8081"

val client = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun auth(info: AuthenticationRequest): User? {
    val response = client.post<HttpResponse>(restUrl + RestRoutes.AUTH.route) {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        body = info
    }
    return when (response.status) {
        HttpStatusCode.OK -> {
            response.receive<User>()
        }
        else -> {
            null
        }
    }
}

suspend fun getUserSessions(user: User): List<Session> {
    val response = client.get<HttpResponse>(restUrl + RestRoutes.USER_SESSIONS.route + user.id) {
        method = HttpMethod.Get
        contentType(ContentType.Application.Json)
    }

    return when (response.status) {
        HttpStatusCode.OK -> {
            response.receive<List<Session>>()
        }
        else -> {
            listOf()
        }
    }
}

suspend fun getSessionInformation(request: UserSessionInfoRequest): UserInfoAboutSessionResponse? {
    val response = client.post<HttpResponse>(restUrl + RestRoutes.SESSION_INFO.route) {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        body = request
    }

    return when (response.status) {
        HttpStatusCode.OK -> {
            response.receive<UserInfoAboutSessionResponse>()
        }
        else -> {
            null
        }
    }
}

suspend fun createSession(conf: CreateSessionRequest) {
    val response = client.post<HttpResponse>(restUrl + RestRoutes.CREATE_SESSION.route) {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        body = conf
    }
}

suspend fun joinByGuid(request: JoinRequest): Session? {
    val response = client.post<HttpResponse>(restUrl + RestRoutes.JOIN_BY_GUID.route) {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        body = request
    }

    return when(response.status) {
        HttpStatusCode.OK -> {
            response.receive<Session>()
        }
        else -> {
            null
        }
    }
}

suspend fun getMemberCount(session: Session): Int {
    val response = client.get<HttpResponse>(restUrl + RestRoutes.MEMBER_COUNT.route + session.id) {
        method = HttpMethod.Get
        contentType(ContentType.Application.Json)
    }

    return when(response.status) {
        HttpStatusCode.OK -> {
            response.receive<Int>()
        }
        else -> {
            0
        }
    }
}

suspend fun removeMemberFromGame(request: LeaveRequest) {
    val response = client.delete<HttpResponse>(restUrl + RestRoutes.LEAVE_FROM_SESSION.route) {
        method = HttpMethod.Delete
        contentType(ContentType.Application.Json)
        body = request
    }
}

suspend fun createGift(request: CreateGiftRequest): Gift? {
    val response = client.post<HttpResponse>(restUrl + RestRoutes.CREATE_GIFT.route) {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        body = request
    }

    return when(response.status) {
        HttpStatusCode.OK -> {
            response.receive<Gift>()
        }
        else -> {
            null
        }
    }
}

suspend fun signup(request: RegistrationRequest): User? {
    val response = client.post<HttpResponse>(restUrl + RestRoutes.SIGNUP.route) {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        body = request
    }

    return when(response.status) {
        HttpStatusCode.Created -> response.receive<User>()
        else -> null
    }
}