package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.cards.Drone;

public class SymbiotesPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:SymbiotesPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    private Boolean triggered;

    public SymbiotesPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SymbiotesPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SymbiotesPower32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
        triggered = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
//        if (!this.triggered && card.cardID.equals(Drone.ID)) {
//            addToBot(new GainBlockAction(this.owner, this.amount));
//            addToBot(new ApplyPowerAction(this.owner, this.owner, new BramblesPower(this.owner, this.amount)));
//            this.triggered = true;
//        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (power.ID.equals(GrowthPower.POWER_ID)) {
            int total = power.amount;
            if (target.hasPower(GrowthPower.POWER_ID)) {
                total += target.getPower(GrowthPower.POWER_ID).amount;
            }
            int drones = (total/GrowthPower.ENERGY_THRESHOLD) * this.amount;
            addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(AbstractDungeon.player), drones));
        }
    }

    @Override
    public void updateDescription() {
//        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        description = DESCRIPTIONS[0] + (this.amount == 1 ? DESCRIPTIONS[1] : this.amount + DESCRIPTIONS[2]);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    public void atStartOfTurn() {
        this.triggered = false;
    }
}
