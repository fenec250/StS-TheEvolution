package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SalamanderPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:SalamanderPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public static int LIZARD_DAMAGE = 2;
    public static int LAVAFOLK_DAMAGE = 1;

    public SalamanderPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SalamanderPower84.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SalamanderPower32.png"), 0, 0, 32, 32);
        this.type = PowerType.BUFF;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + lizardDamage() + DESCRIPTIONS[1]
                + lavafolkExtraDamage() + DESCRIPTIONS[2];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    private int lizardDamage() {
        return LIZARD_DAMAGE * this.amount;
    }

    private int lavafolkExtraDamage() {
        return LAVAFOLK_DAMAGE * this.amount;
    }

    public static int getLizardDamage() {
        if (CardCrawlGame.isInARun()) {
            if (AbstractDungeon.player.hasPower(POWER_ID)) {
                return ((SalamanderPower)AbstractDungeon.player.getPower(POWER_ID)).lizardDamage();
            }
        }
        return 0;
    }

    public static int getLavafolkExtraDamage() {
        if (CardCrawlGame.isInARun()) {
            if (AbstractDungeon.player.hasPower(POWER_ID)) {
                return ((SalamanderPower)AbstractDungeon.player.getPower(POWER_ID)).lavafolkExtraDamage();
            }
        }
        return 0;
    }
}
