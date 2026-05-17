package content.activity.fish_flingers

enum class FishFlingersFish(val lakePrefix: String, val riverPrefix: String, val beachPrefix: String, val docksPrefix: String) {
    BASS("Shallow", "Turbulent", "Cove", "Tumult"),
    COD("Skipping", "Darter", "King", "Crested"),
    PIKE("Spotted", "Triumph", "Finder's", "Lingering"),
    TROUT("Gentle", "Thunder", "Oval", "Yearning"),
    SALMON("Jubilant", "Curtsey", "Bowline", "Flattery"),
    HERRING("Clement", "Spined", "Coral", "Drift");

    companion object {
        fun getFishPrefix(fish: FishFlingersFish, location: FishFlingersLocations): String {
            return when(location) {
                FishFlingersLocations.BEACH -> fish.beachPrefix
                FishFlingersLocations.DOCKS -> fish.docksPrefix
                FishFlingersLocations.RIVER -> fish.riverPrefix
                FishFlingersLocations.LAKE -> fish.lakePrefix
            }
        }
    }
}