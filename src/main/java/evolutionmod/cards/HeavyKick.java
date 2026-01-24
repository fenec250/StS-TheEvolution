package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.CentaurGene2;
import evolutionmod.patches.EvolutionEnum;

public class HeavyKick
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:HeavyKick";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 5;
    private static final int UPGRADE_DAMAGE_AMT = 2;
//    private static final int SCALING_AMOUNT = 2;
    private static final int HITS_NB = 2;
    private static final int CENTAUR_HITS_NB = 1;
//    private static final int UPGRADE_SCALING_AMOUNT = 1;

    public HeavyKick() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = HITS_NB;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = this.magicNumber;
        if (isPlayerInThisForm(CentaurGene2.ID)) {
            hits += CENTAUR_HITS_NB;
        }
        for (int i = 0; i < hits; ++i) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        formEffect(CentaurGene2.ID);
    }
//
//    @Override
//    public void applyPowers() {
//        calculateBaseDamage();
//        super.applyPowers();
//        this.isDamageModified = this.damage != DAMAGE_AMT;
//    }
//
//    @Override
//    public void calculateCardDamage(AbstractMonster mo) {
//        calculateBaseDamage();
//        super.calculateCardDamage(mo);
//        this.isDamageModified = this.damage != DAMAGE_AMT;
//    }
//
//    private void calculateBaseDamage() {
//        this.damage = this.baseDamage = DAMAGE_AMT;
//        if (BaseEvoCard.isPlayerInThisForm(CentaurGene.ID)) {
//            AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
//            AbstractPower vigor = AbstractDungeon.player.getPower(VigorPower.POWER_ID);
//
//            if (strength != null) {
//                this.baseDamage += strength.amount * (this.magicNumber - 1);
//            }
//            if (vigor != null) {
//                this.baseDamage += vigor.amount * (this.magicNumber - 1);
//            }
//        }
//    }

    @Override
    public AbstractCard makeCopy() {
        return new HeavyKick();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
//            this.upgradeMagicNumber(UPGRADE_SCALING_AMOUNT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(CentaurGene2.ID)) {
            this.glowColor = CentaurGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}