package evolutionmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.cards.Drone;

import java.util.Arrays;

import static evolutionmod.cards.CoralStewards.BLOCK_THRESHOLD;

public class CoralPower extends TwoAmountPower {
    public static final String POWER_ID = "evolutionmod:CoralPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public CoralPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower32.png"), 0, 0, 32, 32);
        this.loadRegion("corruption"); // use Corruption's icon
        this.type = PowerType.BUFF;
        this.amount2 = 0;
        this.amount = initialAmount;
        this.updateDescription();
    }

    public void onGainedBlock(float blockAmount) {
        this.amount2 += blockAmount;
        while(this.amount2 >= BLOCK_THRESHOLD) {
            this.amount2 -= BLOCK_THRESHOLD;
            addToBot(new MakeTempCardInHandAction(new Drone()));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + BLOCK_THRESHOLD + (amount > 1
                ? DESCRIPTIONS[1]
                : DESCRIPTIONS[2] + amount + DESCRIPTIONS[3]);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        else {
            this.updateDescription();
        }
    }
}
