package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ChargePower extends StrengthPower {
    public static final String POWER_ID = "evolutionmod:ChargePower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public ChargePower(AbstractCreature owner, int initialAmount) {
        super(owner, initialAmount);
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ebb power 84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ebb power 32.png"), 0, 0, 32, 32);
//        this.type = PowerType.BUFF;
//        this.amount = initialAmount;
//        this.decay = 0;
//        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        this.flashWithoutSound();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(this.owner, this.owner, new ChargePower(this.owner, -this.amount), -this.amount));
    }
}
