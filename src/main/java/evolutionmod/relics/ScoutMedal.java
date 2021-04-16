package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.FossilizedHelix;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.AbstractGene;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

public class ScoutMedal extends CustomRelic {
    public static final String ID = "evolutionmod:ScoutMedal";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/ScoutMedal.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/ScoutMedal_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);

    public ScoutMedal() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.grayscale = true;
        HashMap<Predicate<AbstractCard>, Integer> cardSelectors = new HashMap<>();
        Arrays.stream(AbstractCard.CardType.values())
                .forEach(t -> cardSelectors.put(c -> c.type == t, 1));
        this.addToBot(new FateAction(cardSelectors));
        super.atBattleStart();
    }
}