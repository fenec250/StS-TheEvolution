package evolutionmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.powers.LoopPower;
import evolutionmod.cards.Drone;
import evolutionmod.orbs.LavafolkGene;

public class FireAntsPower extends AbstractPower {
    public static final String POWER_ID = "evolutionmod:FireAntsPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public FireAntsPower(AbstractCreature owner, int initialAmount) {
        super();
        this.name = NAME;
        this.ID = POWER_ID;
        this.type = PowerType.BUFF;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/VenomGlandsPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/VenomGlandsPower32.png"), 0, 0, 32, 32);
        this.loadRegion("flameBarrier");
        this.owner = owner;
        this.amount = initialAmount;
        this.updateDescription();

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
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (card instanceof Drone) {
            AbstractDungeon.player.orbs.stream()
                    .filter(o -> o instanceof LavafolkGene)
                    .limit(this.amount)
                    .forEach(o -> {
                        o.triggerEvokeAnimation();
                        o.onStartOfTurn();
                        o.onEndOfTurn();
                    });
        }
    }
}
