package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;

public class Shadowbolt
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Shadowbolt";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 12;
    private static final int UPGRADE_DAMAGE_AMT = 4;
    private static final int WEAK_DAMAGE_AMT = 2;

    public Shadowbolt() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        int extraDamage = 0;
//        this.baseDamage = DAMAGE_AMT + extraDamage + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//        this.calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (!this.upgraded && !AbstractGene.isPlayerInThisForm(ShadowGene.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(new ShadowGene()));
        }
//        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
        if (mo != null &&
                (this.upgraded || AbstractGene.isPlayerInThisForm(ShadowGene.ID))) {
            AbstractPower weak = mo.getPower(WeakPower.POWER_ID);
            if (weak != null) {
                this.baseDamage += weak.amount * this.magicNumber;
            }
        }
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
        super.calculateCardDamage(mo);
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
        this.isDamageModified = this.baseDamage != this.damage;
    }

    //    @Override
//    public void calculateDamageDisplay(AbstractMonster mo) {
//        AbstractPower weak = mo.getPower(WeakPower.POWER_ID);
//
//        if (weak != null) {
//            this.baseDamage = DAMAGE_AMT + weak.amount + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
////            this.damage += weak.amount;
////            this.isDamageModified = true;
//        }
//
//        super.calculateDamageDisplay(mo);
//
////        if (weak != null) {
////            this.damage -= weak.amount;
////        }
//    }

    @Override
    public AbstractCard makeCopy() {
        return new Shadowbolt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

//    public static class ExtraDamage extends DynamicVariable {
//
//        @Override
//        public int baseValue(AbstractCard card) {
//            return card.baseBlock;
//        }
//
//        @Override
//        public int value(AbstractCard card) {
//            Shadowbolt shadowbolt = (Shadowbolt) card;
//            return shadowbolt.extraBlock + shadowbolt.block;
//        }
//
//        @Override
//        public boolean isModified(AbstractCard card) {
//            return card.block == card.baseBlock;
//        }
//
//        @Override
//        public String key() {
//            return "evolutionmod:DrainCurseAmount";
//        }
//
//        @Override
//        public boolean upgraded(AbstractCard card) {
//            return card.upgraded;
//        }
//    }
}