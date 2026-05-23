package content.activity.fish_flingers

import content.entity.player.dialogue.Happy
import content.entity.player.dialogue.Neutral
import content.entity.player.dialogue.Quiz
import content.entity.player.dialogue.Sad
import content.entity.player.dialogue.type.choice
import content.entity.player.dialogue.type.npc
import content.entity.player.dialogue.type.player
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.entity.World
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.Teleport
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.area.Rectangle

class FishFlingersDialogue : Script {
    init {
        npcOperate("Talk-to", "fish_flingers_fisherman_*") {
            npc<Neutral>("Back again? It's great to see you.")
            fishermanDialogue(this)
        }

        npcOperate("Talk-to", "fish_flingers_fishermans_wife_*") {
            npc<Happy>("Hi there. You here about the Fish Flingers rewards, then?")
            wifeDialogue(this)
        }
    }

    suspend fun fishermanDialogue(player: Player) {
        player.npc<Neutral>("Fish Flingers starts in x minutes. Would you like me to teleport you to the competition?")

        player.choice {
            option<Happy>("Yes, teleport me to Fish Flingers.") {
                if (World.timers.contains("fish_flingers_open_lobby")) {
                    set("fish_flingers_entry_tile", tile)
                    Teleport.teleport(player, "fish_flingers_lobby_teleport", "modern")
                }
                else {
                    // TODO: Find the actual text for when the lobby has closed
                    npc<Sad>("The game has already started, I'm afraid.")
                }
            }

            option<Quiz>("Do you have any advice about the fish?") {
                provideHint(player)
            }

            option("Tell me about Fish Flingers and rewards.") {
                npc<Quiz>("What would you like me to tell you?")
                aboutRewards(player)
            }

            option<Quiz>("About entry tickets...") {
                aboutEntryTickets(player)
            }
        }
    }

    suspend fun provideHint(player: Player) {
        if (!FishFlingers.playerHints.containsKey(player)) {
            FishFlingers.generateHint(player)
        }

        player.npc<Neutral>(FishFlingers.playerHints[player]!!)
        fishermanDialogue(player)
    }

    suspend fun aboutEntryTickets(player: Player) {
        player.choice {
            option<Quiz>("How many entry tickets do I have left?") {
                //TODO: Track tickets
                npc<Happy>("You've got x tickets left at the moment.")
            }

            option<Quiz>("When can I next claim entry tickets?") {
                //TODO: Track days until reset
                npc<Happy>("You can claim more entry tickets in x more days.")
            }

            option<Quiz>("Is there another way of getting entry tickets?") {
                npc<Quiz>("Funny you should ask that. We had a fierce storm recently. The gale picked up a whole load of entry tickets and scattered them across the island.")
                npc<Quiz>("We picked up any we could find, but most of them ended up in the water.")
                npc<Quiz>("So, you never know. When you're taking part in a competition you might find an entry ticket while you catch fish.")
            }

            option<Quiz>("Let's talk about something else.") {
                fishermanDialogue(player)
            }

            option("Never mind.")
        }
    }

    suspend fun aboutRewards(player: Player) {
        player.choice {
            option("What is the competition about?") {
                player<Quiz>("What is Fish Flingers about?")
                // Video for this line was quite blurred. Might be wrong?
                npc<Happy>("It's a chance for you to show how well you can catch fish by choosing the best tackle and habitat.")
                npc<Happy>("Us old-timers know of a special place, Isla Anglerine, where huge schools of unusual fish seem to gather.")
                npc<Quiz>("I'm not sure what the fish find so appealing there, but it's a fisherman's paradise!")
                npc<Quiz>("I can teleport you to the island if you want to take part. Just say the word.")
                aboutRewards(player)
            }

            option<Quiz>("How do I catch fish?") {
                npc<Happy>("Once I've teleported you, wait in the starting area for the competition to begin. Then, walk to the waterfront where you would like to fish.")
                npc<Happy>("Next, select a hook and bait that you think the fish will like, add some weights to get the cast distance you need, and you're ready to cast your line.")
                npc<Quiz>("As you catch fish you'll be shown how effective your tackle is, and you'll be able to make improvements as you learn what each fish likes.")
                npc<Happy>("Remember, different species are different sizes. You'll need to try fishing in different areas to find the heaviest species.")
                npc<Quiz>("It's also worth remembering that fish are adaptable creatures, so tackle that works well in one competition might not work so well in the next.")
                player<Quiz>("Anything else I should know?")
                npc<Quiz>("Hmmm... I something get asked if summoned creatures can help out. Although you're more than welcome to bring your follower, they won't improve your performance in the competition. It's the people who are")
                npc<Quiz>("entering the competition, not their followers.")
                player<Happy>("Got it.")
                aboutRewards(player)
            }

            option("How often can I compete?") {
                npc<Happy>("Everyone is allowed 10 entry tickets a week. We hang on to the tickets for you, so there's no need to carry them about or store them in your bank.")
                npc<Happy>("If you want to know how many you have left, or you want to claim more tickets, just let me know.")
                aboutRewards(player)
            }

            option<Quiz>("Tell me about the rewards.") {
                npc<Happy>("Everyone likes to be rewarded for their efforts, of course. You can be awarded Fishing experience, raw fish and, if you get good enough, a tackle box to help your fishing supplies.")
            }

            option("Let's talk about something else.") {
                fishermanDialogue(player)
            }
        }
    }

    suspend fun wifeDialogue(player: Player) {
        player.choice {
            option<Quiz>("Tell me about the raw fish reward.") {
                npc<Quiz>("There isn't a bank on the island as it's rather small. That means you'd struggle to take any fish you caught away with you.")
                npc<Happy>("Don't worry though, we've got plenty of noted raw fish to give you instead, which is much easier to carry to the bank.")
                npc<Happy>("When you claim your noted fish you'll be given a selection of fish appropriate to your Fishing level. For example, if you'd normally be fishing swordfish, you'll be given a mix of swordfish and lobster.")
                wifeDialogue(player)
            }

            option<Quiz>("Tell me about the tackle box reward.") {
                npc<Happy>("They're wonderful pieces of kit, they truly are. The tackle box holds fishing equipment, saving you inventory and bank space. They also hold a copy of your competition history so you can review your")
                npc<Happy>("performance.")
                npc<Neutral>("Admittedly, when you first get your tackle box it won't hold much, but over time you can upgrade it so it will hold much more.")
                wifeDialogue(player)
            }

            option<Quiz>("Tell me about the fishing experience reward.") {
                npc<Happy>("You'll find that the extra skill involved in competitive fishing can earn you more experience than your everyday fishing. At the end of the competition you'll receive all the experience you've earned so there's no")
                npc<Happy>("need to speak to me to collect it.")
                npc<Quiz>("It might take you a few more goes to get used to the competition tackle, but once you've learned the best way to find and catch the biggest fish species you should notice a great improvement in the experienve you get.")
                npc<Happy>("The amount of experience you get depends on how advanced your Fishing skill is. Having a higher fishing level means you get more experience.")
                wifeDialogue(player)
            }

            option<Quiz>("Show me the reward shop.") {
                player.interfaces.open("")
            }

            option<Quiz>("Give me a copy of my competition history.") {
                if (player.inventory.contains("fish_flingers_scorecard")) {
                    npc<Quiz>("You already seem to have one in your inventory.")
                    player<Quiz>("Oh.")

                }
                else {
                    player.inventory.add("fish_flingers_scorecard")
                    npc<Happy>("Certainly. Here...")
                    player<Happy>("Thanks.")
                }

                wifeDialogue(player)
            }
        }
    }
}