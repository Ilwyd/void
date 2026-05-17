package content.activity.fish_flingers

import world.gregs.voidps.type.Tile

enum class FishFlingersFishermenData(val tile: Tile, val npcName: String) {
    OUTSIDE_FISHING_GUILD(tile = Tile(2620, 3385), npcName = "fish_flingers_fisherman_2"),
    INSIDE_FISHING_GUILD(tile = Tile(2591, 3410), npcName = "fish_flingers_fisherman_3"),
    CATHERBY(tile = Tile(2851, 3429), npcName = "fish_flingers_fisherman_1"),

    // Locations need to be confirmed, estimated from tip.it map screenshots
    // NPCs were based on an old wiki page, and should be right
    GUNNARSGRUNN(tile = Tile(3104, 3430), npcName = "fish_flingers_fisherman_3"),
    PISCATORIS(tile = Tile(2343, 3699), npcName = "fish_flingers_fisherman_1"),
    SHILO_VILLAGE(tile = Tile(2855, 2970), npcName = "fish_flingers_fisherman_2");
}