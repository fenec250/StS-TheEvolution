package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.SuccubusGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.InsatiablePower;
import evolutionmod.powers.LustPower;

public class Seduce
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Seduce";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Seduce.png";
    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int DAMAGE_AMT = 8;
    private static final int INSATIABLE_AMT = 1;
    private static final int LUST_AMT = 4;

    public Seduce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = LUST_AMT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.LIGHTNING));
        addToBot(new ApplyPowerAction(m,p,new InsatiablePower(m,INSATIABLE_AMT)));
        if (isPlayerInThisForm(SuccubusGene2.ID))
            addToBot(new ApplyPowerAction(m,p,new LustPower(m,this.magicNumber)));
        formEffect(SuccubusGene2.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Seduce();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(SuccubusGene2.ID)) {
            this.glowColor = SuccubusGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}
