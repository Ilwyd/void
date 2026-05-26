package content.activity.fish_flingers

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.client.command.adminCommand

class FishFlingersInterface : Script {
    init {
        interfaceOption("*", "fish_flingers_tackle:*") {

        }

        adminCommand("values") {
            interfaces.sendText("fish_flingers_recent_catches", "heaviest", "1")
            interfaces.sendText("fish_flingers_recent_catches", "total_fish", "2")
            interfaces.sendText("fish_flingers_recent_catches", "total_weight", "3")
        }
    }
}