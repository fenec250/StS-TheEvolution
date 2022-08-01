package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.orbs.AbstractGene;

public class HumanFormPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:HumanFormPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public HumanFormPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/HumanFormPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/HumanFormPower32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
        this.priority = 6; // lower than Mastery so the EvokeAction is queued last, for Fate merging.
    }

    @Override
    public void updateDescription() {
        description = this.amount == 1
                ? DESCRIPTIONS[0]
                : DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void onChannel(AbstractOrb orb) {
        if (orb instanceof AbstractGene){
            for(int i = 1; i < this.amount; ++i) {
                ((AbstractGene) orb).getAdaptation().apply(AbstractDungeon.player, null);
            }
            this.addToTop(new EvokeSpecificOrbAction(orb));
            this.flash();
        }
    }

}
