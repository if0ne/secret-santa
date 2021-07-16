package components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.p

class GameList: RComponent<RProps,RState>() {

    override fun RBuilder.render() {
        p {
            +"List of games"
        }
    }

}