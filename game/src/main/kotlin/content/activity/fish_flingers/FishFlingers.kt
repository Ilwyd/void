package content.activity.fish_flingers

import com.github.michaelbull.logging.InlineLogger
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.client.command.adminCommand
import world.gregs.voidps.engine.data.Settings
import world.gregs.voidps.engine.entity.World
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.obj.GameObject
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.entity.obj.ObjectShape
import world.gregs.voidps.engine.get
import world.gregs.voidps.engine.map.instance.Instances
import world.gregs.voidps.engine.map.zone.DynamicZones
import world.gregs.voidps.engine.timer.Timer
import world.gregs.voidps.engine.timer.toTicks
import world.gregs.voidps.type.Region
import world.gregs.voidps.type.Tile
import java.util.concurrent.TimeUnit

class FishFlingers : Script {
    var state: FishFlingerState? = null
    var instance: Region? = null
    var fishermen: ArrayList<NPC> = arrayListOf()
    var started: Boolean = false

    val logger = InlineLogger()

    init {
        worldSpawn {
            if (Settings["events.fishFlingers.enabled", false] && !started) {
                startDownTime()
            }
        }

        worldTimerStart("fish_flingers_down_time") {
            val downTimeMinutes = Settings["events.fishFlingers.downTimeMinutes", 70]

            //TODO: Remove any players from the instance

            TimeUnit.MINUTES.toTicks(downTimeMinutes)
        }

        worldTimerTick("fish_flingers_down_time") { Timer.CANCEL }

        worldTimerStop("fish_flingers_down_time") {
            openLobby()
        }



        worldTimerStart("fish_flingers_open_lobby") {
            val openLobbyMinutes = Settings["events.fishFlingers.lobbyTimeMinutes", 5]
            TimeUnit.MINUTES.toTicks(openLobbyMinutes)
        }

        worldTimerTick("fish_flingers_open_lobby") { Timer.CANCEL }

        worldTimerStop("fish_flingers_open_lobby") {
            startFishermenDespawnTimer()
            startMatch()
        }



        worldTimerStart("fish_flingers_match") {
            val matchMinutes = Settings["events.fishFlingers.matchTimeMinutes", 5]
            TimeUnit.MINUTES.toTicks(matchMinutes)
        }

        worldTimerTick("fish_flingers_match") { Timer.CANCEL }

        worldTimerStop("fish_flingers_match") {
            endMatch()
        }



        worldTimerStart("fish_flingers_fishermen") {
            val despawnMinutes = Settings["events.fishFlingers.fishermenDespawnTimeMinutes", 10]
            TimeUnit.MINUTES.toTicks(despawnMinutes)
        }

        worldTimerTick("fish_flingers_fishermen") { Timer.CANCEL }

        worldTimerStop("fish_flingers_fishermen") {
            despawnFishermen()
        }

        objectOperate("Enter", "fish_flingers_exit_portal") {
            tele(get("fish_flingers_entry_tile", Tile(2620, 3384)))
        }

        adminCommand("gen_ff") {
            generateInstance()
            set("instance", instance!!.id)
            set("fish_flingers_entry_tile", tile)
            tele(instance!!.tile)
        }
    }

    fun startDownTime() {
        logger.info { "Fish Flingers downtime started." }
        World.timers.start("fish_flingers_down_time")
    }

    fun openLobby() {
        logger.info { "Fish Flingers lobby opening." }
        World.timers.start("fish_flingers_open_lobby")
        generateFishDetails()
//        generateInstance()
        spawnFishermen()
    }

    fun spawnFishermen() {
        FishFlingersFishermenData.entries.forEach { fisherman ->
            val npc = NPCs.add(fisherman.npcName, fisherman.tile)
            fishermen.add(npc)
        }
    }

    fun startFishermenDespawnTimer() {
        World.timers.start("fish_flingers_fishermen")
    }

    fun startMatch() {
        logger.info { "Fish Flingers match starting." }
        World.timers.start("fish_flingers_match")
    }

    fun endMatch() {
        logger.info { "Fish Flingers match ending." }
        clearHints()
        startDownTime()
        clearInstance()
    }

    fun despawnFishermen() {
        logger.info { "Fish Flingers fishermen despawning" }
        fishermen.forEach { fisherman ->
            NPCs.remove(fisherman)
        }

        fishermen.clear()
    }

    fun generateFishDetails() {
        val availableHooks = FishFlingersHooks.entries.toMutableList()
        val availableBait = FishFlingersBait.entries.toMutableList()
        val availableWeights = (1..6).toMutableList()
        val availableLocations = FishFlingersLocations.entries.toMutableList()

        // Randomly shuffle the locations. The first 2 in the list will have 2 types of fish
        availableLocations.shuffle()
        availableLocations.add(availableLocations[0])
        availableLocations.add(availableLocations[1])

        val fish = FishFlingersFish.entries.toTypedArray()

        fish.forEach { fish ->
            val hook = availableHooks.random()
            availableHooks.remove(hook)

            val bait = availableBait.random()
            availableBait.remove(bait)

            val location = availableLocations.random()
            availableLocations.remove(location)

            val weight = availableWeights.random()
            availableWeights.remove(weight)

            fishDetails[fish] = hashMapOf(
                Pair("hook", hook),
                Pair("bait", bait),
                Pair("location", location),
                Pair("weight", weight),
                Pair("prefix", FishFlingersFish.getFishPrefix(fish, location))
            )
        }

//        fishDetails.forEach { (fish, details) ->
//            val location: FishFlingersLocations = details["location"] as FishFlingersLocations
//            val hook: FishFlingersHooks = details["hook"] as FishFlingersHooks
//            val bait: FishFlingersBait = details["bait"] as FishFlingersBait
//            val weight: Int = details["weight"] as Int
//            val prefix: String = details["prefix"] as String
//
//            logger.info { "$prefix, ${fish.name}, ${location.name}, ${hook.name}, ${bait.name}, $weight" }
//        }
    }

    fun generateInstance() {
        instance = Instances.large()
        val baseTile = instance!!.tile

        // Copying the regions over to the instance area
        val regions: List<List<Region>> = listOf(
            listOf(Region(10038), Region(10294), Region(10550)),
            listOf(Region(10039), Region(10295), Region(10551)),
            listOf(Region(10040), Region(10296), Region(10552))
        )

        var yOffset = 0
        regions.forEach { row ->
            var xOffset = 0
            row.forEach { region ->
                get<DynamicZones>().copy(region, Region(instance!!.x + xOffset, instance!!.y + yOffset))
                xOffset++
            }
            yOffset++
        }

        // Opening the gates 6464, 5312, 0
        // West gate
        GameObjects.add("fish_flingers_gate_1", baseTile.add(80, 69), ObjectShape.WALL_STRAIGHT, 3)
        GameObjects.add("fish_flingers_gate_2", baseTile.add(80, 70), ObjectShape.WALL_STRAIGHT, 1)

        // North west gate
        GameObjects.add("fish_flingers_gate_1", baseTile.add(83, 76), ObjectShape.WALL_STRAIGHT, 0)
        GameObjects.add("fish_flingers_gate_2", baseTile.add(84, 76), ObjectShape.WALL_STRAIGHT, 2)

        // North east gate
        GameObjects.add("fish_flingers_gate_1", baseTile.add(89, 76), ObjectShape.WALL_STRAIGHT, 0)
        GameObjects.add("fish_flingers_gate_2", baseTile.add(90, 76), ObjectShape.WALL_STRAIGHT, 2)

        // East gate
        GameObjects.add("fish_flingers_gate_1", baseTile.add(93, 70), ObjectShape.WALL_STRAIGHT, 1)
        GameObjects.add("fish_flingers_gate_2", baseTile.add(93, 69), ObjectShape.WALL_STRAIGHT, 3)

        logger.info { "Instance generated at $baseTile" }
    }

    fun clearInstance() {
        if (instance == null) return

        Instances.free(instance!!)
        instance = null
    }

    fun teleportLobbyToInstance() {

    }

    companion object {
        var playerHints: HashMap<Player, String> = hashMapOf()
        var fishDetails: HashMap<FishFlingersFish, HashMap<String, Any>> = hashMapOf()

        fun generateHint(player: Player) {
            val fish = fishDetails.entries.random()
            val fishName = fish.key.name.lowercase()
            val prefix = (fish.value["prefix"] as String).lowercase()
            val location = (fish.value["location"] as FishFlingersLocations).name.lowercase()

            val hint = when(val detail = arrayOf("hook", "bait", "weight").random()) {
                "hook" -> (fish.value[detail] as FishFlingersHooks).hint
                "bait" -> (fish.value[detail] as FishFlingersBait).hint
                "weight" -> when(fish.value[detail] as Int) {
                    1, 2 -> "They've been spotted close to the shore, so you won't need much weight on your line to reach them."
                    3, 4 -> "They've been seen a fair distance from the shore, but not too far. You'll need to attach a few weights to your line to reach them."
                    5, 6 -> "They've been sighted far from the shore, so attach lots of weights to your line to reach them."
                    else -> return
                }
                else -> return
            }

            playerHints[player] =
                "You'll find $prefix $fishName at the $location. $hint"
        }

        fun clearHints() {
            playerHints.clear()
        }

        fun clearFishDetails() {
            fishDetails.clear()
        }
    }
}