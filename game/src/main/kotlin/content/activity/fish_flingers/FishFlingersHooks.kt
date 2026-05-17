package content.activity.fish_flingers

enum class FishFlingersHooks(val hint: String) {
    STANDARD("That species is fairly normal and doesn’t need a special hook. A normal hook should catch them just fine."),
    SLIM("That species has a small mouth, so you'll need to take that into account when choosing your hook."),
    LARGE("That species has a wide mouth, so you'll need to take that into account when choosing your hook."),
    DOUBLE("That species tends to nibble its food, so a double hook will help to catch them."),
    BONE("They're clever and can spot metal a mile off. You´d better think carefully about what type of hook you use."),
    WOOD("They're clever and can spot metal a mile off. You´d better think carefully about what type of hook you use.");

    companion object {
        fun getSimilarHook(hook: FishFlingersHooks) {
            when(hook) {
                STANDARD -> SLIM
                SLIM -> STANDARD
                LARGE -> DOUBLE
                DOUBLE -> LARGE
                BONE -> WOOD
                WOOD -> BONE
            }
        }
    }
}