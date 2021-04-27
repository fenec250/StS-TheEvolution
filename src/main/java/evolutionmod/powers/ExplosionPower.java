package evolutionmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Arrays;
import java.util.Stack;

public class ExplosionPower extends TwoAmountPower {
    public static final String POWER_ID = "evolutionmod:ExplosionPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;
    public static int COUNTDOWN_AMT = 8;

    public ExplosionPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower32.png"), 0, 0, 32, 32);
        this.loadRegion("combust"); // use Corruption's icon
        this.type = PowerType.BUFF;
        this.amount2 = COUNTDOWN_AMT;
        this.amount = initialAmount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1] + amount
                + DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

//    @Override
//    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
//        int initial = this.amount;
//        this.amount = this.amount2 - this.amount;
//        super.renderAmount(sb, x, y, c);
//        this.amount = initial;
//    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        switch (info.type) {
            case NORMAL:
            case THORNS:
                this.amount2 -= 1;
                while(this.amount2 <= 0) {
					int size = AbstractDungeon.getMonsters().monsters.size();
                    long targets = AbstractDungeon.getMonsters().monsters.stream().filter(m -> !m.isDeadOrEscaped()).count();
                    this.amount2 += COUNTDOWN_AMT + targets;
					int[] multiDamage = new int[size];
                    Arrays.fill(multiDamage, this.amount);
                    addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage,
                            DamageInfo.DamageType.THORNS,
                            AbstractGameAction.AttackEffect.FIRE));
                }
                this.updateDescription();
        }
    }
}
