package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Blind;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.ShadowsPower;

import java.util.Iterator;

public class ShadowWave2
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ShadowWave";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 2;
    private static final int SHADOW_DAMAGE_AMT = 3;
    private static final int WEAK_AMT = 1;

    public ShadowWave2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
		this.isMultiDamage = true;
        this.baseDamage = this.damage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster) var3.next();
            this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));
        if (this.upgraded) {
            addToBot(new ShadowGene2().getChannelAction());
        }
        else
            formEffect((ShadowGene2.ID));
    }

    @Override
    public void applyPowers() {
        alterDamageAround(super::applyPowers);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        alterDamageAround(() -> super.calculateCardDamage(mo));
    }

    private void alterDamageAround(Runnable supercall) {
        this.baseDamage = DAMAGE_AMT + (this.upgraded || isPlayerInThisForm(ShadowGene2.ID) ? SHADOW_DAMAGE_AMT : 0);
        supercall.run();
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? SHADOW_DAMAGE_AMT : 0);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowWave2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(SHADOW_DAMAGE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (!this.upgraded && isPlayerInThisForm(ShadowGene2.ID)) {
            this.glowColor = ShadowGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}