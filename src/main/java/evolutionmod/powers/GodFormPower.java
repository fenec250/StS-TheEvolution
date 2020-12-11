package evolutionmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GodFormPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:GodFormPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    private int bypassThisTurn;

    public GodFormPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ebb power 84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ebb power 32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.bypassThisTurn = 0;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        bypassThisTurn = 0;
        super.atStartOfTurn();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        this.amount -= this.bypassThisTurn;
        super.renderAmount(sb, x, y, c);
        this.amount += this.bypassThisTurn;
    }

    public static boolean canBypassRequirement() {
        if (AbstractDungeon.player.hasPower(GodFormPower.POWER_ID)) {
            GodFormPower g = (GodFormPower)AbstractDungeon.player.getPower(GodFormPower.POWER_ID);
            return g.amount > g.bypassThisTurn;
        }
        return false;
    }

    public static void afterBypassRequirement() {
        if (AbstractDungeon.player.hasPower(GodFormPower.POWER_ID)) {
            GodFormPower g = (GodFormPower) AbstractDungeon.player.getPower(GodFormPower.POWER_ID);
            ++g.bypassThisTurn;
            g.flash();
            g.updateDescription();
        }
    }

    public static boolean bypassFormRequirementOnce() {
        GodFormPower g = AbstractDungeon.player.hasPower(GodFormPower.POWER_ID)
                ? (GodFormPower)AbstractDungeon.player.getPower(GodFormPower.POWER_ID)
                : null;
        if (g != null && g.amount > g.bypassThisTurn){
            ++g.bypassThisTurn;
            g.flash();
            g.updateDescription();
            return true;
        }
        return false;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }
}
