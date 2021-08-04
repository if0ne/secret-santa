import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.runBlocking
import ru.tinkoff.sanata.shared_models.model.ChangeNotification
import java.time.LocalDateTime

fun startTicker(config: AppConfig, client: HttpClient) {
    val ticker = ticker(config.ticker.delay)
    runBlocking {
        for (tick in ticker) {
            val response =
                client.get<io.ktor.client.statement.HttpResponse>(config.server.url + config.server.notificationRoute) {
                    method = HttpMethod.Get
                    contentType(ContentType.Application.Json)
                }
            when (response.status) {
                HttpStatusCode.OK -> {
                    client.post<io.ktor.client.statement.HttpResponse>(config.telegram.url + config.telegram.notificationRoute) {
                        method = HttpMethod.Post
                        contentType(ContentType.Application.Json)
                        body = response.receive<List<ChangeNotification>>()
                    }
                }
                else -> {
                }
            }
        }
    }
}


fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    val client = HttpClient(CIO) {
        install(JsonFeature)
        expectSuccess = false
    }
    startTicker(config, client)
}