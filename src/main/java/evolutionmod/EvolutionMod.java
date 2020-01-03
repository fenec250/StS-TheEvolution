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
import evolutionmod.cards.Adaptation;
import evolutionmod.cards.AquaticForm;
import evolutionmod.cards.Brambles;
import evolutionmod.cards.ChannelMagic;
import evolutionmod.cards.Charm;
import evolutionmod.cards.Constrict;
import evolutionmod.cards.MagicForm;
import evolutionmod.cards.Purify;
import evolutionmod.cards.VineLash;
import evolutionmod.cards.DefendEvo;
import evolutionmod.cards.Dive;
import evolutionmod.cards.Drown;
import evolutionmod.cards.Eruption;
import evolutionmod.cards.Evolution;
import evolutionmod.cards.FeatherStorm;
import evolutionmod.cards.FlameStrike;
import evolutionmod.cards.HoofKick;
import evolutionmod.cards.Seduce;
import evolutionmod.cards.Stampede;
import evolutionmod.cards.Strip;
import evolutionmod.cards.TakeOff;
import evolutionmod.cards.TalonStrike;
import evolutionmod.cards.WaterBolt;
import evolutionmod.cards.WindUp;
import evolutionmod.character.EvolutionCharacter;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.cards.StrikeEvo;

import java.nio.charset.StandardCharsets;

@SpireInitializer
public class EvolutionMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber {

    private static final Color melodyLime = CardHelper.getColor(191.0f, 255.0f, 1.0f); //0xbfff00
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
//        BaseMod.addDynamicVariable(new AbstractMelodyCard.TempoNumber());

        //Basic. 2 attacks, 2 skills
        BaseMod.addCard(new Adaptation());
        BaseMod.addCard(new Evolution());
        BaseMod.addCard(new DefendEvo());
        BaseMod.addCard(new StrikeEvo());

        //Special
//        BaseMod.addCard(new BladeBurst());
//
        //Commons
        //10 attacks
        BaseMod.addCard(new HoofKick());
        BaseMod.addCard(new TalonStrike());
        BaseMod.addCard(new WaterBolt());
        BaseMod.addCard(new FlameStrike());
        //8 skills
        BaseMod.addCard(new Charm());
        BaseMod.addCard(new Brambles());

        //Uncommons
        //11 attacks
        BaseMod.addCard(new Strip());
        BaseMod.addCard(new VineLash());
        //18 skills
        BaseMod.addCard(new TakeOff());
        BaseMod.addCard(new Dive());
        BaseMod.addCard(new WindUp());
        BaseMod.addCard(new ChannelMagic());
        BaseMod.addCard(new Purify());
        //6 powers
        BaseMod.addCard(new AquaticForm());
        BaseMod.addCard(new MagicForm());
//        BaseMod.addCard(new Hot());

        //Rares.
        //4 attacks
        BaseMod.addCard(new Stampede());
        BaseMod.addCard(new Seduce());
        BaseMod.addCard(new FeatherStorm());
        BaseMod.addCard(new Eruption());
        //8 skills
        BaseMod.addCard(new Drown());
        BaseMod.addCard(new Constrict());
        //6 powers
//        BaseMod.addCard(new BeatOfLife());
//        BaseMod.addCard(new Choreography());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new EvolutionCharacter(CardCrawlGame.playerName), charButton, charPortrait, EvolutionEnum.EVOLUTION_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        String[] keywordRhythm = {"adapt"};
        BaseMod.addKeyword(keywordRhythm, "Add the effect of a channeled gene to a card until the end of the combat.");
//        String[] keywordTempo = {"mist"};
//        BaseMod.addKeyword(keywordTempo, "At 10 Mist you gain 1 Intangible");
//        String[] keywordStep = {"step", "steps"};
//        BaseMod.addKeyword(keywordStep, "Steps are the number of consecutive dances you played.");

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
//        String relicStrings = Gdx.files.internal("evolutionmod/strings/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
//        String uiStrings = Gdx.files.internal("evolutionmod/strings/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
    }

    @Override
    public void receiveEditRelics() {
        //starter
//        BaseMod.addRelicToCustomPool(new SpellBook(), MELODY_LIME);
    }
}