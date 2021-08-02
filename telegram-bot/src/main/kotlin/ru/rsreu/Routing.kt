import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.rsreu.bot.SantaBot
import ru.tinkoff.sanata.shared_models.model.ChangeNotification

fun Application.configureRouting(bot: SantaBot) {

    routing {
        post("/") {
            val receiveBody = call.receiveText()
            bot.processUpdate(receiveBody)
            call.respond(HttpStatusCode.OK)
        }
        post("/notifications"){
            val request = call.receive<List<ChangeNotification>>()
            print(request)
            call.respond(HttpStatusCode.OK)
        }
    }
}
