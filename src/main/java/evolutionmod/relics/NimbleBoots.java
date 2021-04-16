package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NimbleBoots extends CustomRelic {
    public static final String ID = "evolutionmod:NimbleBoots";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/NimbleBoots.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/NimbleBoots_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);
    private static final int CARDS_PLAYED_AMT = 5;
    private static final int CARD_DRAW_AMT = 1;

    public NimbleBoots() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.counter = 0;
    }

    public void atTurnStartPostDraw() {
        if (this.counter >= CARDS_PLAYED_AMT) {
            this.addToBot(new DrawCardAction(AbstractDungeon.player, CARD_DRAW_AMT));
        }

        this.counter = 0;
        this.stopPulse();
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        ++this.counter;
        if (this.counter > CARDS_PLAYED_AMT) {
            this.beginLongPulse();
        }

    }

    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }
}