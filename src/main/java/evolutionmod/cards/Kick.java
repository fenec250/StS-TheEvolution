package evolutionmod.cards;

import basemod.abstracts.CustomCard;
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
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.ChargePower;

public class Kick
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Kick";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurForm.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 15;
    private static final int SCALING_AMOUNT = 2;
    private static final int UPGRADE_SCALING_AMOUNT = 1;

    public Kick() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = SCALING_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (!AbstractGene.isPlayerInThisForm(CentaurGene.ID)) {
        	addToBot(new ChannelAction(new CentaurGene()));
		}
    }

    @Override
    public void applyPowers() {
        calculateDamage();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        calculateDamage();
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
        super.calculateCardDamage(mo);
    }

    private void calculateDamage() {
        this.damage = this.baseDamage = DAMAGE_AMT;
        if (AbstractGene.isPlayerInThisForm(CentaurGene.ID)) {
            AbstractPower charge = AbstractDungeon.player.getPower(ChargePower.POWER_ID);
            AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
            AbstractPower vigor = AbstractDungeon.player.getPower(VigorPower.POWER_ID);

            if (charge != null) {
                this.damage += charge.amount * (this.magicNumber - 1);
                this.baseDamage += charge.amount * (this.magicNumber - 1);
            }
            if (strength != null) {
                this.damage += strength.amount * (this.magicNumber - 1);
                this.baseDamage += strength.amount * (this.magicNumber - 1);
            }
            if (vigor != null) {
                this.damage += vigor.amount * (this.magicNumber - 1);
                this.baseDamage += vigor.amount * (this.magicNumber - 1);
            }
        }
    }

//    @Override
//    public void calculateDamageDisplay(AbstractMonster mo) {
//        if (AbstractGene.isPlayerInThisForm(CentaurGene.ID)) {
//            AbstractPower charge = AbstractDungeon.player.getPower(ChargePower.POWER_ID);
//            AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
//
//            if (charge != null) {
//                this.damage += charge.amount * (this.magicNumber - 1);
//                this.baseDamage += charge.amount * (this.magicNumber - 1);
//            }
//            if (strength != null) {
//                this.damage += strength.amount * (this.magicNumber - 1);
//                this.baseDamage += strength.amount * (this.magicNumber - 1);
//            }
//
//            super.calculateDamageDisplay(mo);
//
//            if (charge != null) {
//                this.damage -= charge.amount * (this.magicNumber - 1);
//                this.baseDamage -= charge.amount * (this.magicNumber - 1);
//            }
//            if (strength != null) {
//                this.damage -= strength.amount * (this.magicNumber - 1);
//                this.baseDamage -= strength.amount * (this.magicNumber - 1);
//            }
//        } else {
//            super.calculateDamageDisplay(mo);
//        }
//    }

    @Override
    public AbstractCard makeCopy() {
        return new Kick();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_SCALING_AMOUNT);
        }
    }
}