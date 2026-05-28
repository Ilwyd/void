package content.activity.fish_flingers

enum class FishFlingersHooks(val hint: String, val varcValue: Int) {
    NONE("", 0),
    STANDARD("That species is fairly normal and doesn’t need a special hook. A normal hook should catch them just fine.", 1),
    SLIM("That species has a small mouth, so you'll need to take that into account when choosing your hook.", 2),
    LARGE("That species has a wide mouth, so you'll need to take that into account when choosing your hook.", 3),
    DOUBLE("That species tends to nibble its food, so a double hook will help to catch them.", 6),
    BONE("They're clever and can spot metal a mile off. You´d better think carefully about what type of hook you use.", 4),
    WOODEN("They're clever and can spot metal a mile off. You´d better think carefully about what type of hook you use.", 5);

    companion object {
        fun getSimilarHook(hook: FishFlingersHooks): FishFlingersHooks {
            val similar = when(hook) {
                STANDARD -> SLIM
                SLIM -> STANDARD
                LARGE -> DOUBLE
                DOUBLE -> LARGE
                BONE -> WOODEN
                WOODEN -> BONE
                NONE -> NONE
            }

            return similar
        }
    }
}