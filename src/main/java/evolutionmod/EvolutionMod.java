package evolutionmod;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import evolutionmod.cards.*;
import evolutionmod.character.EvolutionCharacter;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.potions.EatMe;
import evolutionmod.potions.Mutagen;
import evolutionmod.powers.GrowthPower;
import evolutionmod.relics.MagicFocus;
import evolutionmod.relics.NimbleBoots;
import evolutionmod.relics.OldOutfit;
import evolutionmod.relics.ScoutMedal;
import evolutionmod.relics.PowerFocus;
import evolutionmod.relics.StrengthFocus;
import evolutionmod.relics.Tori;
import evolutionmod.relics.TorisGift;
import evolutionmod.relics.Whip;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpireInitializer
public class EvolutionMod implements
        EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, PostInitializeSubscriber {

    private static final Color evo_green = CardHelper.getColor(53, 140, 127); // 0x358c7e
    private static final String attackCard = "evolutionmod/images/512/bg_attack_evo.png";
    private static final String skillCard = "evolutionmod/images/512/bg_skill_evo.png";
    private static final String powerCard = "evolutionmod/images/512/bg_power_evo.png";
    private static final String energyOrb = "evolutionmod/images/512/evolution_orb.png";
    private static final String attackCardPortrait = "evolutionmod/images/1024/bg_attack_evo.png";
    private static final String skillCardPortrait = "evolutionmod/images/1024/bg_skill_evo.png";
    private static final String powerCardPortrait = "evolutionmod/images/1024/bg_power_evo.png";
    private static final String energyOrbPortrait = "evolutionmod/images/1024/evolution_orb.png";
    private static final String charButton = "evolutionmod/images/charSelect/button.png";
    private static final String charPortrait = "evolutionmod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "evolutionmod/images/manaSymbol.png";
    public static ModPanel settingsPanel;


    public EvolutionMod(){
        BaseMod.subscribe(this);

        BaseMod.addColor(AbstractCardEnum.EVOLUTION_BLUE, //TODO
                evo_green, evo_green, evo_green, evo_green, evo_green, evo_green, evo_green,   //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                                        //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,                        //as above, but for card inspect view
                miniManaSymbol);                                                                                    //appears in Mystic Purple cards where you type [E]
    }

    //Used by @SpireInitializer
    public static void initialize(){
        EvolutionMod evolutionMod = new EvolutionMod();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeImg = new Texture("evolutionmod/images/badge.png");
        settingsPanel = new ModPanel();
        BaseMod.registerModBadge(badgeImg, "The Evolution Mod", "Fenec250", "Adds a new character to the game: The Evolution.", settingsPanel);

        GlowingCard.init(2);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AdaptableEvoCard.MaxAdaptationNumber());
//        BaseMod.addDynamicVariable(new PegasusDescent.PegasusHits());
//        BaseMod.addDynamicVariable(new Shadowbolt.ExtraDamage());

        List<AbstractCard> cards = new ArrayList<>();
        //Basic. 2 attacks, 2 skills
        cards.add(new LoyalCompanion());
        cards.add(new LoyalWarrior());
        cards.add(new DefendEvo());
        cards.add(new StrikeEvo());

        //Special
        cards.add(new Drone());
//        cards.add(new DroneGuard());
//        cards.add(new DroneFire());
//        cards.add(new DroneMantis());
        cards.add(new Feather());

        //Commons
        //Vex quote: i shoot for 18 commons, 10 damage 6 block 2 other as a "blueprint" and modify if anything jumps out at me
        //10 attacks
        cards.add(new ClawSlash());
        cards.add(new Shadowbolt());
        cards.add(new TalonStrike());
        cards.add(new FlameStrike());
        cards.add(new PoisonFangs());
        cards.add(new HeavyKick());

        cards.add(new ShiftingStrike());
        cards.add(new ChimeraStrike());
        cards.add(new CrystalShard());
        //8 skills
        cards.add(new Barkskin());
        cards.add(new Visions());
        cards.add(new Dive());
        cards.add(new Charm());
        cards.add(new Hatch());

        cards.add(new ChimeraDefense());
        cards.add(new ShiftingGuard());
        cards.add(new CrystalStone());

        //Uncommons
        //11 attacks
        cards.add(new PlayingRough());
        cards.add(new StripArmor());
        cards.add(new NightMare());
        cards.add(new Phoenix());
        cards.add(new PegasusDescent());
        cards.add(new SpiderBite());
        cards.add(new CursedTouch());
        cards.add(new BlackCat());
        cards.add(new SeaWolf());
        //exhaust
        //18 skills
        cards.add(new Strengthen2());
        cards.add(new Hivemind());
        cards.add(new VenomGlands());
        cards.add(new FireAnts());
        cards.add(new Firebloom());
        cards.add(new ChannelMagic());
        cards.add(new LeafBird());
//        cards.add(new Omen());
        cards.add(new Pheromones());
        cards.add(new Aphrodisiac());
        cards.add(new HeightenedSenses());
        cards.add(new DepthsLurker2());
        cards.add(new Antidote());
        cards.add(new CalmTheWaters());
        cards.add(new SeaSerpent());
        cards.add(new ShiftingPower());
        cards.add(new Adaptation());
        cards.add(new Mutate());
        //exhaust
        cards.add(new Toxin());
        cards.add(new Treasure());
        cards.add(new CrystalDust());
        //6 powers
        cards.add(new Battleborn());
        cards.add(new CurrentsDancer());
        cards.add(new Symbiotes());
        cards.add(new Salamander());
        cards.add(new DarkDesires());
        cards.add(new Mastery());
        cards.add(new Shrink());

        //Rares.
        //4 attacks
        cards.add(new Stampede());
        cards.add(new Lifesteal());
        cards.add(new Stalker());
        cards.add(new Eruption());
        cards.add(new Frenzy());
        cards.add(new FeatherStorm());
        //8 skills
        cards.add(new Drown());
        cards.add(new Ritual());
        cards.add(new Photosynthesis());
        cards.add(new TheFutureIsNow());
//        cards.add(new Aegis());
//        cards.add(new CrystalShaping());

        //6 powers
        cards.add(new Broodmother());
        cards.add(new TheNight());
        cards.add(new GodlyPowers());
        cards.add(new HumanForm());
        cards.add(new Grow());
        cards.add(new Absorption());

        cards.forEach(c -> {
            BaseMod.addCard(c);
            UnlockTracker.unlockCard(c.cardID);
        });
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new EvolutionCharacter(CardCrawlGame.playerName), charButton, charPortrait, EvolutionEnum.EVOLUTION_CLASS);
        BaseMod.addPotion(EatMe.class, Color.BROWN, Color.CLEAR, Color.CLEAR,
                EatMe.POTION_ID, EvolutionEnum.EVOLUTION_CLASS);
        BaseMod.addPotion(Mutagen.class, Color.ORANGE, Color.VIOLET, Color.GREEN,
                Mutagen.POTION_ID, EvolutionEnum.EVOLUTION_CLASS);
//                CHARGE_POTION_LIQUID, CHARGE_POTION_HYBRID, CHARGE_POTION_SPOTS, ChargePotion.POTION_ID, TheDefault.Enums.THE_DEFAULT);

    }

    @Override
    public void receiveEditKeywords() {
        String[] keywordForm = {"form"};
        BaseMod.addKeyword("Form", keywordForm, "Trigger the following effect if you have a Gene of this type, otherwise Channel one.");
        String[] keywordAdapt = {"adapt"};
        BaseMod.addKeyword("Adapt", keywordAdapt, "Consume a channeled Gene. Add its effect to the card until the end of the combat and shuffle this card in your draw pile. NL Adapted effects target the same enemy as the card if possible.");
        String[] keywordShift = {"shift"};
        BaseMod.addKeyword("Shift", keywordShift, "Recreate the gene at the back of the orb line.");
        String[] keywordRando = {"randomized"};
        BaseMod.addKeyword("Randomized", keywordRando, "Randomized Forms are selected when the card is created and vary from a card to another.");
//        String[] keywordTrait = {"trait"};
//        BaseMod.addKeyword("Trait", keywordTrait, "Trigger the following effect if you have a Gene of this type.");
//        String[] keywordPotency = {"potency"};
//        BaseMod.addKeyword("Potency", keywordPotency, "Increase the first effect of your genes and their Adaptations.");
        String[] keywordFate = {"fate"};
        BaseMod.addKeyword("Fate X", keywordFate, "Put random cards from your draw pile matching X on top of your draw pile, then Scry them.");
//        BaseMod.addKeyword("Fate", keywordFate, "Look at the top X cards of your draw pile. You may discard any of them. Discarded cards may trigger the associated effect.");
        String[] keywordGrowth = {"growth", "Plant"};
        BaseMod.addKeyword("Growth", keywordGrowth,
                "Upon reaching " + GrowthPower.ENERGY_THRESHOLD + " Growth gain 1 Energy and reduce Growth by " + GrowthPower.ENERGY_THRESHOLD);
        String[] keywordDrone = {"drone", "drones"};
        BaseMod.addKeyword("Drone", keywordDrone,
                "Drones are 0 cost attacks which benefit from Potency, are Ethereal and Exhaust when played.");
        String[] keywordRage = {"rage"};
        BaseMod.addKeyword("Rage", keywordRage,
                "Whenever you play an Attack this turn, gain this amount of Block.");
        String[] keywordSubmission = {"lust"};
        BaseMod.addKeyword("Lust", keywordSubmission,
                "Reduce the base damage of the next attack by this amount.");
//        String[] keywordCharge = {"charge", "centaur"};
//        BaseMod.addKeyword("Charge", keywordCharge,
//                "Increase the damage of your next hit then reduces by half.");
        String[] keywordVigor = {"vigor", "centaur"};
        BaseMod.addKeyword("Vigor", keywordVigor,
                "Increases the damage of your next Attack.");
        String[] keywordShadows = {"shadows"};
        BaseMod.addKeyword("Shadows", keywordShadows,
                "When #yShadows reaches its threshold it deals 2 damage per stack and applies 1 #yWeak to all enemies. The first threshold is 1.");

//        String[] keywordCantrips = {"cantrip", "cantrips"};
//        BaseMod.addKeyword(keywordCantrips, "Considered a [#5299DC]Spell[] so long as you've played fewer than 3 [#5299DC]Spells[] this turn.");
    }

    @Override
    public void receiveEditStrings() {
        String cardStrings = Gdx.files.internal("evolutionmod/strings/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String orbStrings = Gdx.files.internal("evolutionmod/strings/orbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);
        String characterStrings = Gdx.files.internal("evolutionmod/strings/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String potionStrings = Gdx.files.internal("evolutionmod/strings/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String powerStrings = Gdx.files.internal("evolutionmod/strings/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("evolutionmod/strings/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
//        String uiStrings = Gdx.files.internal("evolutionmod/strings/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
    }

    @Override
    public void receiveEditRelics() {
        //starter
        BaseMod.addRelicToCustomPool(new TorisGift(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(TorisGift.ID);
        //common
        BaseMod.addRelicToCustomPool(new NimbleBoots(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(NimbleBoots.ID);
        //uncommon
        BaseMod.addRelicToCustomPool(new PowerFocus(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(PowerFocus.ID);
        //rare
        BaseMod.addRelicToCustomPool(new MagicFocus(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(MagicFocus.ID);
        BaseMod.addRelicToCustomPool(new StrengthFocus(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(StrengthFocus.ID);
        BaseMod.addRelicToCustomPool(new ScoutMedal(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(ScoutMedal.ID);
        //shop
        BaseMod.addRelicToCustomPool(new Whip(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(Whip.ID);
        //boss
        BaseMod.addRelicToCustomPool(new OldOutfit(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(OldOutfit.ID);
        BaseMod.addRelicToCustomPool(new Tori(), AbstractCardEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(Tori.ID);
    }
}