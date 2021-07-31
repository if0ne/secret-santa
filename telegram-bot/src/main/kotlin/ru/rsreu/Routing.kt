import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.rsreu.bot.SantaBot

fun Application.configureRouting(bot: SantaBot) {

    routing {
        post("/") {
            val receiveBody = call.receiveText()
            print(receiveBody)
            bot.processUpdate(receiveBody)
            call.respond(HttpStatusCode.OK)
        }
    }
}
