package content.activity.fish_flingers

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.client.command.adminCommand
import world.gregs.voidps.engine.entity.character.player.Player

class FishFlingersInterface : Script {
    init {
        interfaceOption("*", "fish_flingers_tackle:bait_*") {
            val bait: FishFlingersBait = when(it.component) {
                "bait_worm" -> FishFlingersBait.WORMS
                "bait_maggot" -> FishFlingersBait.MAGGOTS
                "bait_locust" -> FishFlingersBait.LOCUSTS
                "bait_cricket" -> FishFlingersBait.CRICKETS
                "bait_crayfish" -> FishFlingersBait.CRAYFISH
                "bait_shrimp" -> FishFlingersBait.SHRIMP
                "bait_green_moth" -> FishFlingersBait.GREEN_MOTH
                "bait_grey_moth" -> FishFlingersBait.GREY_MOTH
                else -> return@interfaceOption
            }

            updateBait(this, bait)
        }

        interfaceOption("*", "fish_flingers_tackle:*_hook") {
            val hook: FishFlingersHooks = when(it.component) {
                "standard_hook" -> FishFlingersHooks.STANDARD
                "large_hook" -> FishFlingersHooks.LARGE
                "slim_hook" -> FishFlingersHooks.SLIM
                "wooden_hook" -> FishFlingersHooks.WOODEN
                "bone_hook" -> FishFlingersHooks.BONE
                "double_hook" -> FishFlingersHooks.DOUBLE
                else -> return@interfaceOption
            }

            updateHook(this, hook)
        }

        interfaceOption("*", "fish_flingers_tackle:add_*_weight") {
            val weight: FishFlingersWeights = when(it.component) {
                "add_small_weight" -> FishFlingersWeights.SMALL
                "add_medium_weight" -> FishFlingersWeights.MEDIUM
                "add_large_weight" -> FishFlingersWeights.LARGE
                else -> return@interfaceOption
            }

            addWeight(this, weight)
        }

        interfaceOption("*", "fish_flingers_tackle:remove_*_weight") {
            val weight: FishFlingersWeights = when(it.component) {
                "remove_small_weight" -> FishFlingersWeights.SMALL
                "remove_medium_weight" -> FishFlingersWeights.MEDIUM
                "remove_large_weight" -> FishFlingersWeights.LARGE
                else -> return@interfaceOption
            }

            removeWeight(this, weight)
        }

        interfaceOption("Clear line", "fish_flingers_tackle:clear_line") {
            clearLine(this)
        }

        adminCommand("values") {
            interfaces.sendText("fish_flingers_recent_catches", "heaviest", "1")
            interfaces.sendText("fish_flingers_recent_catches", "total_fish", "2")
            interfaces.sendText("fish_flingers_recent_catches", "total_weight", "3")
        }
    }

    fun updateBait(player: Player, newBait: FishFlingersBait) {
        player["current_bait"] = newBait.varcValue
    }

    fun updateHook(player: Player, newHook: FishFlingersHooks) {
        player["current_hook"] = newHook.varcValue
    }

    fun addWeight(player: Player, newWeight: FishFlingersWeights) {
        (1..5).forEach {
            if (player["current_weight_$it", 0] != 0) {
                return@forEach
            }

            player["current_weight_$it"] = newWeight.varcValue
            return
        }

        // TODO: There was probably a chat message here saying that there was no room for more weights
    }

    fun removeWeight(player: Player, weight: FishFlingersWeights) {
        (1..5).forEach {
            if (player["current_weight_$it", 0] != weight.varcValue) {
                return@forEach
            }

            player["current_weight_$it"] = FishFlingersWeights.NONE.varcValue
            return
        }

        // TODO: There was probably a chat message here saying that there was weight to remove
    }

    fun clearLine(player: Player) {
        player["current_weight_1"] = FishFlingersWeights.NONE.varcValue
        player["current_weight_2"] = FishFlingersWeights.NONE.varcValue
        player["current_weight_3"] = FishFlingersWeights.NONE.varcValue
        player["current_weight_4"] = FishFlingersWeights.NONE.varcValue
        player["current_weight_5"] = FishFlingersWeights.NONE.varcValue
        player["current_bait"] = FishFlingersBait.NONE.varcValue
        player["current_hook"] = FishFlingersHooks.NONE.varcValue
    }
}