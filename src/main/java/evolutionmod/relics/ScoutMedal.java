package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        this.addToBot(new WaitAction(0.7f));
        this.addToBot(new Action());
        super.atBattleStart();
    }

    public static class Action extends AbstractGameAction {

        public Action() {
            this.actionType = ActionType.CARD_MANIPULATION;
        }

        public void update() {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                CardGroup drawPile = AbstractDungeon.player.drawPile;
                Set<AbstractCard> copy = new HashSet<>();
                if (!drawPile.isEmpty()) {
                    Arrays.stream(AbstractCard.CardType.values())
                            .map(t ->
                                    drawPile.group.stream()
                                            .filter(c -> c.type == t)
                                            .filter(c -> !copy.contains(c))
                                            .collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
                                                Collections.shuffle(l, AbstractDungeon.cardRandomRng.random);
                                                return l.stream();
                                            }))
                                            .findFirst())
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(c -> {
                                if (copy.add(c)) {
                                    drawPile.group.remove(c);
                                }
                            });
                    copy.forEach(drawPile::addToTop);
                }
                addToTop(new ScryAction(copy.size()));
            }
            this.isDone = true;
        }
    }
}