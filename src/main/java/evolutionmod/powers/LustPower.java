package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LustPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:LustPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public int attacksCount = 0;
    public LustPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/LustPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/LustPower32.png"), 0, 0, 32, 32);
        this.type = PowerType.DEBUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = this.owner.hasPower(InsatiablePower.POWER_ID)
                ? DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + String.format ("%1$.0f", 100*(1.0-Math.pow(InsatiablePower.RETAIN_FRACTION, this.owner.getPower(InsatiablePower.POWER_ID).amount))) + DESCRIPTIONS[3]
                : DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL
                ? damage - (float)this.amount
                : damage;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        attacksCount += 1;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        if (!isPlayer
                && this.owner instanceof AbstractMonster
                && attacksCount > 0) {
            int reduction = this.amount;
            if (this.owner.hasPower(InsatiablePower.POWER_ID)) {
                reduction -= (int) Math.ceil(this.amount * Math.pow(1-Math.pow(.5, this.owner.getPower(InsatiablePower.POWER_ID).amount), attacksCount));
            }
            addToBot(new ReducePowerAction(this.owner, this.owner, this, reduction));
            attacksCount = 0;
        }
    }
}
