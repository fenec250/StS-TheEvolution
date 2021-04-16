package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class GrowthPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:GrowthPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    public static final int ENERGY_THRESHOLD = 2;

    public GrowthPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/PlantPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/PlantPower32.png"), 0, 0, 32, 32);
        this.loadRegion("energized_green"); // use Silent Energized icon
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + ENERGY_THRESHOLD + DESCRIPTIONS[1] + ENERGY_THRESHOLD + DESCRIPTIONS[2];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.convertToEnergy();
    }

    @Override
    public void onInitialApplication() {
        this.convertToEnergy();
        super.onInitialApplication();
    }

    public void convertToEnergy() {
        if (this.amount >= ENERGY_THRESHOLD) {
            addToBot(new GainEnergyAction(this.amount / ENERGY_THRESHOLD));
            this.amount = this.amount % ENERGY_THRESHOLD;
        }
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }
}
