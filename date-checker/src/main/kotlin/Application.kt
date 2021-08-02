import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.channels.ticker

suspend fun startTicker(config: AppConfig, client: HttpClient) {
    val ticker = ticker(60000)
    for (tick in ticker) {
        val response =
            client.get<io.ktor.client.statement.HttpResponse>(config.server.url + "lol") {
                method = HttpMethod.Get
                contentType(ContentType.Application.Json)
            }
        when (response.status) {
            HttpStatusCode.OK -> print("OK")
            HttpStatusCode.InternalServerError -> print("ISE")
        }

    }
}

suspend fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    val client = HttpClient(CIO) {
        install(JsonFeature)
        expectSuccess = false
    }
    startTicker(config, client)
}