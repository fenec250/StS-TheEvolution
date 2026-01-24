package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.BeastGene2;
import evolutionmod.patches.EvolutionEnum;

public class ClawSlash
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:ClawSlash";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BeastAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 7;
    private static final int UPGRADE_DAMAGE_AMT = 3;

    public ClawSlash() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (isPlayerInThisForm(BeastGene2.ID) || this.upgraded) {
            addToBot(new RefundAction(this, 1));
        }
        BaseEvoCard.formEffect(BeastGene2.ID);
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
        this.baseDamage = DAMAGE_AMT + (upgraded && isPlayerInThisForm(BeastGene2.ID) ? this.magicNumber : 0);
        supercall.run();
        this.baseDamage = DAMAGE_AMT;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ClawSlash();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DAMAGE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(BeastGene2.ID)) {
            this.glowColor = BeastGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}