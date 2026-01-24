package evolutionmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class InsatiablePower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:InsatiablePower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public static final double RETAIN_FRACTION = 0.4;
    public InsatiablePower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower32.png"), 0, 0, 32, 32);
        this.loadRegion("demonForm");
        this.type = PowerType.DEBUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (this.owner.hasPower(LustPower.POWER_ID))
            this.owner.getPower(LustPower.POWER_ID).updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + String.format ("%1$.0f", 100*(1.0-Math.pow(RETAIN_FRACTION, this.amount)))
                + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }
}
