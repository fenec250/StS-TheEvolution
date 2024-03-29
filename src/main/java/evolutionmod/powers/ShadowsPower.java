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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Arrays;

public class ShadowsPower extends TwoAmountPower {
    public static final String POWER_ID = "evolutionmod:ShadowsPower";
    public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = cardStrings.NAME;
    public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

    public ShadowsPower(AbstractCreature owner, int initialAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/ShadowPower32.png"), 0, 0, 32, 32);
        this.loadRegion("corruption"); // use Corruption's icon
        this.type = PowerType.BUFF;
        this.amount2 = 1;
        this.amount = initialAmount;
//        if (!owner.hasPower(POWER_ID)) {
//            addToTop(new AbstractGameAction() {
//                @Override
//                public void update() {
//                    AbstractPower power = owner.getPower(POWER_ID);
//                    if (power != null) {
//                        ((ShadowsPower) power).checkThreshold();
//                    }
//                    this.isDone = true;
//                }
//            });
//        }
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount2 - amount) + DESCRIPTIONS[1] + stackDamage()
                + DESCRIPTIONS[2] + (amount2 + 1) + DESCRIPTIONS[3];
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        checkThreshold();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        checkThreshold();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        int initial = this.amount;
        this.amount = this.amount2 - this.amount;
        super.renderAmount(sb, x, y, c);
        this.amount = initial;
    }

    public void checkThreshold() {
        if (this.amount2 < 1) {
            this.amount2 = 0;
        }
        if (this.amount >= this.amount2) {
            flash();
            AbstractDungeon.getMonsters().monsters.stream()
                    .filter(mo -> !mo.isDeadOrEscaped())
                    .forEach(mo ->
                            addToBot(new ApplyPowerAction(mo, AbstractDungeon.player,
                                    new WeakPower(mo, 1, false)))
                    );
            if (this.amount > 0) {
                int[] multiDamage = new int[AbstractDungeon.getMonsters().monsters.size()];
                Arrays.fill(multiDamage, this.amount * stackDamage());
                addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage,
                        DamageInfo.DamageType.THORNS,
                        AbstractGameAction.AttackEffect.FIRE));
            }
            AbstractDungeon.player.powers.stream()
                    .filter(p -> p instanceof OnShadowsPower)
                    .forEach(p -> ((OnShadowsPower) p).onSelfShadowsTrigger(amount, amount2));
//            AbstractDungeon.getMonsters().monsters.stream()
//                    .flatMap(m -> m.powers.stream())
//                    .filter(p -> p instanceof OnShadowsPower)
//                    .forEach(p -> ((OnShadowsPower) p).onPlayerShadowsTrigger(amount, amount2));
            this.amount -= amount;
            this.amount2 += 1;
        }

        updateDescription();
    }

    public static void triggerImmediately(AbstractCreature p) {
        ShadowsPower spower = ((ShadowsPower) p.getPower(ShadowsPower.POWER_ID));
        if (spower == null) {
            spower = new ShadowsPower(p, 0);
            spower.amount2 = 0;
        }
        ShadowsPower power = spower;
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                int initial = ShadowsPower.getThreshold(p);
                power.amount2 = power.amount;
                power.checkThreshold();
                power.amount2 = initial;
                this.isDone = true;
            }
        });
    }

    public static int getThreshold(AbstractCreature p) {
        AbstractPower power = p.getPower(ShadowsPower.POWER_ID);
        if (power != null) {
            return ((TwoAmountPower) power).amount2;
        } else {
            return 1;
        }
    }

//    public static void reduceThreshold(AbstractCreature p, int magicNumber) {
//        AbstractPower power = p.getPower(ShadowsPower.POWER_ID);
//        if (power != null) {
//            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
//                @Override
//                public void update() {
//                    ((ShadowsPower) power).amount2 -= 1;
//                    ((ShadowsPower) power).checkThreshold();
//                    this.isDone = true;
//                }
//            });
//        } else {
//            AbstractDungeon.getMonsters().monsters.stream()
//                    .filter(mo -> !mo.isDeadOrEscaped())
//                    .forEach(mo -> AbstractDungeon.actionManager.addToTop((new ApplyPowerAction(mo, p,
//                            new WeakPower(mo, magicNumber, false), magicNumber))));
//        }
//    }

    private int stackDamage() {
        AbstractPower theNight = this.owner.getPower(TheNightPower.POWER_ID);
        if (theNight != null) {
            return 2 + theNight.amount;
        }
        return 2;
    }
}
