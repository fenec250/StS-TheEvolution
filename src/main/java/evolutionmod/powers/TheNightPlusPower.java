package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.Shadowbolt;
import evolutionmod.orbs.ShadowGene;

public class TheNightPlusPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:TheNightPlusPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public TheNightPlusPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = this.amount == 1
                ? DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2]
                : DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        Shadowbolt shadowbolt = new Shadowbolt();
        shadowbolt.upgrade();
        if (!(BaseEvoCard.isPlayerInThisForm(ShadowGene.ID)
				|| AbstractDungeon.player.hasPower(TheNightPower.POWER_ID))) {
            addToBot(new ChannelAction(new ShadowGene()));
			addToBot(new MakeTempCardInHandAction(shadowbolt, this.amount - 1));
		} else {
            addToBot(new MakeTempCardInHandAction(shadowbolt, this.amount));
        }
    }
}
