package content.activity.fish_flingers

enum class FishFlingersBait(val hint: String, val varcValue: Int) {
    NONE("", 0),
    MAGGOTS("They're known to like wriggling creatures, particularly brown ones.", 2),
    WORMS("They're known to like wriggling creatures, particularly red ones.", 1),
    CRICKETS("They're known to like leaping insects, particularly brown ones.", 3),
    LOCUSTS("They're known to like leaping insects, particularly green ones.", 4),
    GREEN_MOTH("They're known to like fluttering creatures, particularly green ones.", 7),
    GREY_MOTH("They're known to like fluttering creatures, particularly grey ones.", 8),
    CRAYFISH("They're known to like shellfish, particularly grey ones.", 5),
    SHRIMP("They're known to like shellfish, particularly red ones.", 6)
}