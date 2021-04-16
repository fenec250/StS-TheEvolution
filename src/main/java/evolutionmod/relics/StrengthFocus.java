package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StrengthFocus extends CustomRelic {
    public static final String ID = "evolutionmod:StrengthFocus";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/StrengthFocus.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/StrengthFocus_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);
    private static final int DUPED_ATTACK_AMT = 1;

    public StrengthFocus() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.beginLongPulse();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.pulse && !card.purgeOnUse && card.type == AbstractCard.CardType.ATTACK) {
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            this.stopPulse();
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.stopPulse();
    }
}