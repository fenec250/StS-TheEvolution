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
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import evolutionmod.character.EvolutionV1Character;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.potions.EatMe;
import evolutionmod.potions.Mutagen;
import evolutionmod.potions.MutagenV1;
import evolutionmod.potions.StolenKiss;
import evolutionmod.relics.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private static final String charButton1 = "evolutionmod/images/charSelect/button1.png";
    private static final String charButton2 = "evolutionmod/images/charSelect/button2.png";
    private static final String charPortrait = "evolutionmod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "evolutionmod/images/manaSymbol.png";
    public static ModPanel settingsPanel;


    public EvolutionMod(){
        BaseMod.subscribe(this);

        BaseMod.addColor(EvolutionEnum.EVOLUTION_V2_BLUE,
                evo_green, evo_green, evo_green, evo_green, evo_green, evo_green, evo_green,
                attackCard, skillCard, powerCard, energyOrb,
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,
                miniManaSymbol);
        BaseMod.addColor(EvolutionEnum.EVOLUTION_BLUE,
                evo_green, evo_green, evo_green, evo_green, evo_green, evo_green, evo_green,
                attackCard, skillCard, powerCard, energyOrb,
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,
                miniManaSymbol);
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

        //Special
        cards.add(new Drone());
        cards.add(new Feather());

        //Basic. 2 attacks, 2 skills
        cards.add(new LoyalCompanion());
        cards.add(new LoyalWarrior());
        cards.add(new DefendEvo());
        cards.add(new StrikeEvo());

        //Commons
        //Vex quote: i shoot for 18 commons, 10 damage 6 block 2 other as a "blueprint" and modify if anything jumps out at me
        //10 attacks
        cards.add(new ClawSlash());
        cards.add(new ShadowBolt());
        cards.add(new TalonStrike());
        cards.add(new FlameStrike());
//        cards.add(new PoisonFangs());
        cards.add(new HeavyKick());

        cards.add(new ShiftingStrike());
        cards.add(new ChimeraStrike());
        cards.add(new CrystalShard());
        //8 skills
        cards.add(new Sapling());
        cards.add(new Visions());
        cards.add(new Dive());
        cards.add(new Charm());
        cards.add(new PoisonScales());
        cards.add(new Hatch());

        cards.add(new ChimeraDefense());
        cards.add(new ShiftingGuard());
        cards.add(new CrystalShield());

        //Uncommons
        //11 attacks
        cards.add(new PlayingRough());
        cards.add(new Mossbeast());
        cards.add(new StripArmor2());
        cards.add(new Phoenix());
        cards.add(new PegasusDescent());
        cards.add(new CursedTouch2());
        cards.add(new DeathKiss());
        cards.add(new BlackCat4());
        cards.add(new SeaWolf2());
        cards.add(new Embermane());
        //exhaust
        //18 skills
        cards.add(new TrueStrike());
        cards.add(new Hivemind());
        cards.add(new VenomGlands2());
        cards.add(new FireAnts());
        cards.add(new Blazebloom());
        cards.add(new SeerSear());
        cards.add(new Promise());
        cards.add(new CurrentsDancer());
        cards.add(new Pheromones2());
        cards.add(new Heartstopper());
        cards.add(new HeightenedSenses());
        cards.add(new DepthsLurker());
        cards.add(new ReadTheWaters2());
        cards.add(new SeaSerpent());
//        cards.add(new ShiftingPower());
        cards.add(new Transform());
        cards.add(new Adaptation());
        cards.add(new Mutate());
        //exhaust
        cards.add(new Toxin2());
        cards.add(new Treasure());
        cards.add(new CrystalDust());

        //6 powers
        cards.add(new Battleborn());
//        cards.add(new CurrentsDancer());
        cards.add(new CoralStewards());
        cards.add(new LeafWings());
        cards.add(new NightMare());
        cards.add(new Salamander());
        cards.add(new Mastery());
        cards.add(new CrystalShaping2());
        cards.add(new Absorption());

        //Rares.
        //4 attacks
        cards.add(new Stampede());
        cards.add(new Seduce());
//        cards.add(new Stalker());
//        cards.add(new DeathKiss());
        cards.add(new Frenzy());
        cards.add(new FeatherStorm());
        cards.add(new SpiderBite2());
        //8 skills
        cards.add(new Drown());
//        cards.add(new Drown2());
        cards.add(new Ghost());
        cards.add(new Ritual());
        cards.add(new Photosynthesis());
        cards.add(new Heal());

        //6 powers
//        cards.add(new Broodmother());
//        cards.add(new TheNight2());
        cards.add(new Swampborn());
        cards.add(new Eruption());
        cards.add(new GodlyPowers());
        cards.add(new HumanForm());
        cards.add(new Grow());
        cards.add(new Shrink());

        // V1 cards
        //Basic. 2 attacks, 2 skills
        cards.add(new evolutionmod.cardsV1.LoyalCompanion());
        cards.add(new evolutionmod.cardsV1.LoyalWarrior());
        cards.add(new evolutionmod.cardsV1.DefendEvo());
        cards.add(new evolutionmod.cardsV1.StrikeEvo());

        //Commons
        //Vex quote: i shoot for 18 commons, 10 damage 6 block 2 other as a "blueprint" and modify if anything jumps out at me
        //10 attacks
        cards.add(new evolutionmod.cardsV1.ClawSlash());
        cards.add(new evolutionmod.cardsV1.Shadowbolt());
        cards.add(new evolutionmod.cardsV1.TalonStrike());
        cards.add(new evolutionmod.cardsV1.FlameStrike());
        cards.add(new evolutionmod.cardsV1.PoisonFangs());
        cards.add(new evolutionmod.cardsV1.HeavyKick());

        cards.add(new evolutionmod.cardsV1.ShiftingStrike());
        cards.add(new evolutionmod.cardsV1.ChimeraStrike());
        cards.add(new evolutionmod.cardsV1.CrystalShard());
        //8 skills
        cards.add(new evolutionmod.cardsV1.Barkskin());
        cards.add(new evolutionmod.cardsV1.Visions());
        cards.add(new evolutionmod.cardsV1.Dive());
        cards.add(new evolutionmod.cardsV1.Charm());
        cards.add(new evolutionmod.cardsV1.Hatch());

        cards.add(new evolutionmod.cardsV1.ChimeraDefense());
        cards.add(new evolutionmod.cardsV1.ShiftingGuard());
        cards.add(new evolutionmod.cardsV1.CrystalStone());

        //Uncommons
        //11 attacks
        cards.add(new evolutionmod.cardsV1.PlayingRough());
        cards.add(new evolutionmod.cardsV1.StripArmor());
        cards.add(new evolutionmod.cardsV1.NightMare());
        cards.add(new evolutionmod.cardsV1.Phoenix());
        cards.add(new evolutionmod.cardsV1.PegasusDescent());
        cards.add(new evolutionmod.cardsV1.SpiderBite());
        cards.add(new evolutionmod.cardsV1.CursedTouch());
        cards.add(new evolutionmod.cardsV1.BlackCat());
        cards.add(new evolutionmod.cardsV1.SeaWolf());
        //exhaust
        //18 skills
        cards.add(new evolutionmod.cardsV1.Strengthen2());
        cards.add(new evolutionmod.cardsV1.Hivemind());
        cards.add(new evolutionmod.cardsV1.VenomGlands());
        cards.add(new evolutionmod.cardsV1.FireAnts());
        cards.add(new evolutionmod.cardsV1.Firebloom());
        cards.add(new evolutionmod.cardsV1.ChannelMagic());
        cards.add(new evolutionmod.cardsV1.LeafBird());
        cards.add(new evolutionmod.cardsV1.Pheromones());
        cards.add(new evolutionmod.cardsV1.Aphrodisiac());
        cards.add(new evolutionmod.cardsV1.HeightenedSenses());
        cards.add(new evolutionmod.cardsV1.DepthsLurker());
        cards.add(new evolutionmod.cardsV1.Antidote());
        cards.add(new evolutionmod.cardsV1.CalmTheWaters());
        cards.add(new evolutionmod.cardsV1.SeaSerpent());
        cards.add(new evolutionmod.cardsV1.ShiftingPower());
        cards.add(new evolutionmod.cardsV1.Adaptation());
        cards.add(new evolutionmod.cardsV1.Mutate());
        //exhaust
        cards.add(new evolutionmod.cardsV1.Toxin());
        cards.add(new evolutionmod.cardsV1.Treasure());
        cards.add(new evolutionmod.cardsV1.CrystalDust());
        //6 powers
        cards.add(new evolutionmod.cardsV1.Battleborn());
        cards.add(new evolutionmod.cardsV1.CurrentsDancer());
        cards.add(new evolutionmod.cardsV1.Symbiotes());
        cards.add(new evolutionmod.cardsV1.Salamander());
        cards.add(new evolutionmod.cardsV1.DarkDesires());
        cards.add(new evolutionmod.cardsV1.Mastery());
        cards.add(new evolutionmod.cardsV1.Shrink());

        //Rares.
        //4 attacks
        cards.add(new evolutionmod.cardsV1.Stampede());
        cards.add(new evolutionmod.cardsV1.Lifesteal());
        cards.add(new evolutionmod.cardsV1.Stalker());
        cards.add(new evolutionmod.cardsV1.Eruption());
        cards.add(new evolutionmod.cardsV1.Frenzy());
        cards.add(new evolutionmod.cardsV1.FeatherStorm());
        //8 skills
        cards.add(new evolutionmod.cardsV1.Drown());
        cards.add(new evolutionmod.cardsV1.Ritual());
        cards.add(new evolutionmod.cardsV1.Photosynthesis());
        cards.add(new evolutionmod.cardsV1.TheFutureIsNow());

        //6 powers
        cards.add(new evolutionmod.cardsV1.Broodmother());
        cards.add(new evolutionmod.cardsV1.TheNight());
        cards.add(new evolutionmod.cardsV1.GodlyPowers());
        cards.add(new evolutionmod.cardsV1.HumanForm());
        cards.add(new evolutionmod.cardsV1.Grow());
        cards.add(new evolutionmod.cardsV1.Absorption());

        cards.forEach(c -> {
            BaseMod.addCard(c);
            UnlockTracker.unlockCard(c.cardID);
        });
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new EvolutionCharacter(CardCrawlGame.playerName), charButton2, charPortrait, EvolutionEnum.EVOLUTION_V2_CLASS);
        BaseMod.addPotion(EatMe.class, Color.BROWN, Color.CLEAR, Color.CLEAR,
                EatMe.V2_POTION_ID, EvolutionEnum.EVOLUTION_V2_CLASS);
        BaseMod.addPotion(Mutagen.class, Color.ORANGE, Color.VIOLET, Color.GREEN,
                Mutagen.POTION_ID, EvolutionEnum.EVOLUTION_V2_CLASS);
        BaseMod.addPotion(StolenKiss.class, Color.RED, Color.PINK, Color.CLEAR,
                StolenKiss.POTION_ID, EvolutionEnum.EVOLUTION_V2_CLASS);

        BaseMod.addCharacter(new EvolutionV1Character(CardCrawlGame.playerName), charButton1, charPortrait, EvolutionEnum.EVOLUTION_CLASS);
        BaseMod.addPotion(EatMe.class, Color.BROWN, Color.CLEAR, Color.CLEAR,
                EatMe.POTION_ID, EvolutionEnum.EVOLUTION_CLASS);
        BaseMod.addPotion(MutagenV1.class, Color.ORANGE, Color.VIOLET, Color.GREEN,
                MutagenV1.POTION_ID, EvolutionEnum.EVOLUTION_CLASS);

    }

    @Override
    public void receiveEditKeywords() {
//        BaseMod.addKeyword("Fate", keywordFate, "Look at the top X cards of your draw pile. You may discard any of them. Discarded cards may trigger the associated effect.");

//        String[] keywordCantrips = {"cantrip", "cantrips"};
//        BaseMod.addKeyword(keywordCantrips, "Considered a [#5299DC]Spell[] so long as you've played fewer than 3 [#5299DC]Spells[] this turn.");

        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("evolutionmod/strings" /*+ "/eng"*/ + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            BaseMod.addKeyword("evolutionmod", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
        BaseMod.addKeyword("evolutionmodv1", "Fate V1", new String[] {"fate"}, "Move random matching cards from your draw pile to its top, then #yScry them.");
    }

    @Override
    public void receiveEditStrings() {
        String cardStrings = Gdx.files.internal("evolutionmod/strings/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String oldCardStrings = Gdx.files.internal("evolutionmod/strings/cardsV1.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, oldCardStrings);
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
        BaseMod.addRelicToCustomPool(new TorisGift(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(TorisGift.ID);
        //common
        BaseMod.addRelicToCustomPool(new NimbleBoots(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(NimbleBoots.ID);
        //uncommon
        BaseMod.addRelicToCustomPool(new PowerFocus(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(PowerFocus.ID);
        //rare
        BaseMod.addRelicToCustomPool(new MagicFocus(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(MagicFocus.ID);
        BaseMod.addRelicToCustomPool(new StrengthFocus(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(StrengthFocus.ID);
        BaseMod.addRelicToCustomPool(new ScoutMedal(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(ScoutMedal.ID);
        //shop
        BaseMod.addRelicToCustomPool(new Whip(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(Whip.ID);
        //boss
        BaseMod.addRelicToCustomPool(new OldOutfit(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(OldOutfit.ID);
        BaseMod.addRelicToCustomPool(new Tori(), EvolutionEnum.EVOLUTION_V2_BLUE);
        UnlockTracker.markRelicAsSeen(Tori.ID);


        //starter
        BaseMod.addRelicToCustomPool(new TorisGiftV1(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(TorisGift.ID);
        //common
        BaseMod.addRelicToCustomPool(new NimbleBoots(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(NimbleBoots.ID);
        //uncommon
        BaseMod.addRelicToCustomPool(new PowerFocus(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(PowerFocus.ID);
        //rare
        BaseMod.addRelicToCustomPool(new MagicFocus(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(MagicFocus.ID);
        BaseMod.addRelicToCustomPool(new StrengthFocus(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(StrengthFocus.ID);
        BaseMod.addRelicToCustomPool(new ScoutMedal(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(ScoutMedal.ID);
        //shop
        BaseMod.addRelicToCustomPool(new Whip(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(Whip.ID);
        //boss
        BaseMod.addRelicToCustomPool(new OldOutfit(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(OldOutfit.ID);
        BaseMod.addRelicToCustomPool(new ToriV1(), EvolutionEnum.EVOLUTION_BLUE);
        UnlockTracker.markRelicAsSeen(ToriV1.ID);
    }
}