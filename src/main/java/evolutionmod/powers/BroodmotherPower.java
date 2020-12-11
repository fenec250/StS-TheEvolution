package evolutionmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.Drone;

public class BroodmotherPower extends AbstractPower implements DroneUpgrader {
    public static final String POWER_ID = "evolutionmod:BroodmotherPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    private int upgradedThisTurn;

    public BroodmotherPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.upgradedThisTurn = 0;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount
                + DESCRIPTIONS[1] + (this.amount > 1 ? "s" : "") + DESCRIPTIONS[2];
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
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        this.amount -= this.upgradedThisTurn;
        super.renderAmount(sb, x, y, c);
        this.amount += this.upgradedThisTurn;
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.flashWithoutSound();
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public int upgradesLeft() {
        return this.amount - this.upgradedThisTurn;
    }

    @Override
    public AbstractCard getUpgradedDrone() {
        return new Drone();
    }

    @Override
    public void consumeUpgrades(int upgradesConsumed) {
        this.upgradedThisTurn -= upgradesConsumed;
        this.flash();
        this.updateDescription();
    }
}
