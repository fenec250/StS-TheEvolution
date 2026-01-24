package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.EvolutionEnum;

import java.util.Iterator;

public class ShadowBolt
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:ShadowBolt";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowWave.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 3;
    private static final int WEAK_AMT = 1;
    private static final int TURN_SCALE_AMT = 2;

    public ShadowBolt() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = this.damage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));

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
        this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT:0) + (isPlayerInThisForm(ShadowGene2.ID) ? GameActionManager.turn * TURN_SCALE_AMT : 0);
        supercall.run();
        this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT:0);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowBolt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(ShadowGene2.ID)) {
            this.glowColor = ShadowGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}