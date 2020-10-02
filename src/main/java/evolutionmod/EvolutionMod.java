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
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import evolutionmod.cards.*;
import evolutionmod.character.EvolutionCharacter;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.relics.TorisGift;

import java.nio.charset.StandardCharsets;

@SpireInitializer
public class EvolutionMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber {

    private static final Color melodyLime = CardHelper.getColor(191, 255, 1); //0xbfff00
    private static final String attackCard = "evolutionmod/images/512/bg_attack_mystic.png"; //TODO: change these images
    private static final String skillCard = "evolutionmod/images/512/bg_skill_mystic.png";
    private static final String powerCard = "evolutionmod/images/512/bg_power_mystic.png";
    private static final String energyOrb = "evolutionmod/images/512/evolution_orb.png";
    private static final String attackCardPortrait = "evolutionmod/images/1024/bg_attack_mystic.png";
    private static final String skillCardPortrait = "evolutionmod/images/1024/bg_skill_mystic.png";
    private static final String powerCardPortrait = "evolutionmod/images/1024/bg_power_mystic.png";
    private static final String energyOrbPortrait = "evolutionmod/images/1024/evolution_orb.png";
    private static final String charButton = "evolutionmod/images/charSelect/button.png";
    private static final String charPortrait = "evolutionmod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "evolutionmod/images/manaSymbol.png";
    public static ModPanel settingsPanel;


    public EvolutionMod(){
        BaseMod.subscribe(this);

        BaseMod.addColor(AbstractCardEnum.EVOLUTION_BLUE, //TODO
                melodyLime, melodyLime, melodyLime, melodyLime, melodyLime, melodyLime, melodyLime,   //Background color, back color, frame color, frame outline color, description box color, glow color
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
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AdaptableEvoCard.MaxAdaptationNumber());
        BaseMod.addDynamicVariable(new DrainCurse.BlockAmount());
//        BaseMod.addDynamicVariable(new Shadowbolt.ExtraDamage());

        //Vex quote: i shoot for 18 commons, 10 damage 6 block 2 other as a "blueprint" and modify if anything jumps out at me
        //Basic. 2 attacks, 2 skills
        BaseMod.addCard(new LoyalCompanion());
        BaseMod.addCard(new LoyalWarrior());
        BaseMod.addCard(new DefendEvo());
        BaseMod.addCard(new StrikeEvo());

        //Special
        BaseMod.addCard(new Drone());
//
        //Commons
        //10 attacks
        BaseMod.addCard(new TalonStrike());
        BaseMod.addCard(new FlameStrike());
        BaseMod.addCard(new ClawSlash());
        BaseMod.addCard(new Shadowbolt());
        BaseMod.addCard(new PoisonFangs());

//        BaseMod.addCard(new WaterBolt());
//        BaseMod.addCard(new WaveCrash());
//        BaseMod.addCard(new GatherFood());
        BaseMod.addCard(new ShiftingStrike());
        BaseMod.addCard(new ChimeraStrike());
        BaseMod.addCard(new CrystalShard());
        //8 skills
        BaseMod.addCard(new DrainMana());
        BaseMod.addCard(new Charm());
        BaseMod.addCard(new Brambles());
        BaseMod.addCard(new Dive());
        BaseMod.addCard(new Hatch());
//        BaseMod.addCard(new Regenerate());
        BaseMod.addCard(new ChannelMagic());
//        BaseMod.addCard(new ShiftingPower());
        BaseMod.addCard(new ChimeraDefense());
        BaseMod.addCard(new ShiftingGuard());
        BaseMod.addCard(new CrystalBlock());
//        BaseMod.addCard(new ShadowShift());

        //Uncommons
        //11 attacks
        BaseMod.addCard(new Kick());
        BaseMod.addCard(new Phoenix());
        BaseMod.addCard(new SpiderBite());
        BaseMod.addCard(new Strip());
//        BaseMod.addCard(new VineLash());
        BaseMod.addCard(new CursedTouch());
        //exhaust
        BaseMod.addCard(new BlackCat());
        //18 skills
        BaseMod.addCard(new Strenghten());
        BaseMod.addCard(new Firebloom());

        BaseMod.addCard(new WindUp());
//        BaseMod.addCard(new ChannelMagic());
        BaseMod.addCard(new Purify());
        BaseMod.addCard(new HeightenedSenses());
        //exhaust
        BaseMod.addCard(new Salamander());
        BaseMod.addCard(new SirenSong());
        BaseMod.addCard(new Toxin());
        //6 powers
        BaseMod.addCard(new Grow());
        BaseMod.addCard(new Symbiotes());
        BaseMod.addCard(new Blessing());
        BaseMod.addCard(new ThickHide());
        BaseMod.addCard(new HardWorker());
        BaseMod.addCard(new CurrentsDancer());

        //Rares.
        //4 attacks
        BaseMod.addCard(new Stampede());
        BaseMod.addCard(new Seduce());
        BaseMod.addCard(new FeatherStorm());
        BaseMod.addCard(new Eruption());
//        BaseMod.addCard(new Rush());
        //8 skills
        BaseMod.addCard(new Drown());
//        BaseMod.addCard(new Constrict());
//        BaseMod.addCard(new Incubate());
        BaseMod.addCard(new Aegis());
        BaseMod.addCard(new PoisonSpit());
//        BaseMod.addCard(new DrainCurse());
        //6 powers
//        BaseMod.addCard(new GodForm());
        BaseMod.addCard(new DarkDesires());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new EvolutionCharacter(CardCrawlGame.playerName), charButton, charPortrait, EvolutionEnum.EVOLUTION_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        String[] keywordAdapt = {"adapt"};
        BaseMod.addKeyword(keywordAdapt, "Consume a channeled Gene. Add its effect to the card until the end of the combat. Shuffle this card in your draw pile.");
        String[] keywordForm = {"form"};
        BaseMod.addKeyword(keywordForm, "Trigger the following effect if you have a Gene of this type, otherwise Channel one.");
        String[] keywordTrait = {"trait"};
        BaseMod.addKeyword(keywordTrait, "Trigger the following effect if you have a Gene of this type.");
        String[] keywordPotency = {"potency"};
        BaseMod.addKeyword("Potency", keywordPotency, "Increase the first effect of your genes and their Adaptations.");

        String[] keywordBrambles = {"brambles", "bramble"};
        BaseMod.addKeyword("Bramble", keywordBrambles,
                "Whenever you are attacked this turn, deal this amount of damage back");
        String[] keywordDrone = {"drone", "drones"};
        BaseMod.addKeyword("Drone", keywordDrone,
                "Drones are 0 cost attacks which benefit from Potency, are Ethereal and Exhaust when played.");
        String[] keywordRage = {"rage", "beast"};
        BaseMod.addKeyword("Rage", keywordRage,
                "Whenever you play an Attack this turn, gain this amount of Block.");
//        String[] keywordCharge = {"charge", "centaur"};
//        BaseMod.addKeyword("Charge", keywordCharge,
//                "Increase the damage of your next hit then reduces by half.");
        String[] keywordCharge = {"vigor", "centaur"};
        BaseMod.addKeyword("Vigor", keywordCharge,
                "Increase the damage of your next hit then reduces by half.");

//        String[] keywordCentaur = {CentaurGene.NAME.toLowerCase(), "centaur"};
//        BaseMod.addKeyword(CentaurGene.COLOR + " gene[]", keywordCentaur,
//                "Orb: at the start of your turn, gain 3 Vigor.");
////                "Orb: at the start of your turn, gain 3 Charge.");
//        String[] keywordPlant = {PlantGene.NAME.toLowerCase(), PlantGene.COLOR + "Plant[]", "plant", "[#60B040]plant[]"};
//        BaseMod.addKeyword( PlantGene.COLOR + PlantGene.NAME + " gene[]", keywordPlant,
//                "Orb: gain 1 Block and 3 Brambles at the start of your turn.");
//        String[] keywordHarpy = {HarpyGene.NAME.toLowerCase(), "harpy"};
//        BaseMod.addKeyword( HarpyGene.COLOR + HarpyGene.NAME + " gene[]", keywordHarpy,
//                "Orb: draw a card at the start of your turn.");
//        String[] keywordLavafolk = {LavafolkGene.NAME.toLowerCase(), "lavafolk"};
//        BaseMod.addKeyword( LavafolkGene.COLOR + LavafolkGene.NAME + " gene[]", keywordLavafolk,
//                "Orb: deal 2 damage to a random enemy twice at the end of your turn.");
//        String[] keywordBeast = {BeastGene.NAME.toLowerCase(), "beast", BeastGene.COLOR + "beast"};
//        BaseMod.addKeyword( BeastGene.COLOR + BeastGene.NAME + " gene[]", keywordBeast,
//                "Orb: gain 1 block and 2 rage at the start of your turn.");
//        String[] keywordMerfolk = {MerfolkGene.NAME.toLowerCase(), "merfolk"};
//        BaseMod.addKeyword(MerfolkGene.COLOR + MerfolkGene.NAME + " gene", keywordMerfolk,
//                "Orb: gain 4 Block at the end of your turn.");
//        String[] keywordLymean = {LymeanGene.NAME.toLowerCase(), "lymean"};
//        BaseMod.addKeyword(LymeanGene.COLOR + LymeanGene.NAME + " gene", keywordLymean,
//                "Orb: Scry 2 at the start of your turn.");
//        String[] keywordShadow = {ShadowGene.NAME.toLowerCase(), "shadow"};
//        BaseMod.addKeyword(ShadowGene.COLOR + ShadowGene.NAME + " gene", keywordShadow,
//                "Orb: deal 1 damage and apply 2 Weak at the start of your turn.");
//        String[] keywordSuccubus = {SuccubusGene.NAME.toLowerCase(), "succubus"};
//        BaseMod.addKeyword(SuccubusGene.COLOR + SuccubusGene.NAME + " gene", keywordSuccubus,
//                "Orb: deal 1 damage and apply 2 Vulnerable at the start of your turn.");
//        String[] keywordInsect = {InsectGene.NAME.toLowerCase(), "insect"};
//        BaseMod.addKeyword(InsectGene.COLOR + InsectGene.NAME + " gene", keywordInsect,
//                "Orb: deal 1 damage and apply 2 Weak at the start of your turn.");
//        String[] keywordLizard = {LizardGene.NAME.toLowerCase(), "lizard"};
//        BaseMod.addKeyword(LizardGene.COLOR + LizardGene.NAME + " gene", keywordLizard,
//                "Orb: deal 1 damage and apply 3 Poison at the start of your turn.");

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
    }
}