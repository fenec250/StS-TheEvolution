package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;

public class SpikeVolley
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:SpikeVolley";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantAtt.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int DAMAGE_AMT = 3;
    private static final int UPGRADE_DAMAGE_AMT = -2;
    private static final int SPIKE_DAMAGE_AMT = 3;

    public SpikeVolley() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = SPIKE_DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        int extraDamage = 0;
//        this.baseDamage = DAMAGE_AMT + extraDamage + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//        this.calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (!AbstractGene.isPlayerInThisForm(PlantGene.ID)) {
            addToBot(new ChannelAction(new PlantGene()));
        } else {
            AbstractPower brambles = p.getPower(BramblesPower.POWER_ID);
            if (brambles != null) {
                addToBot(new ApplyPowerAction(p, p, new BramblesPower(p, -1 * brambles.amount)));
            }
        }
    }

    @Override
    public void applyPowers() {
        this.calculateDamage();
        super.applyPowers();
        this.baseDamage = getBaseDamage();
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.calculateDamage();
        super.calculateCardDamage(mo);
        this.baseDamage = getBaseDamage();
        this.isDamageModified = this.damage != this.baseDamage;
    }

    public int getBaseDamage() {
        return DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
    }

    public void calculateDamage() {
        this.baseDamage = getBaseDamage();
        if (AbstractGene.isPlayerInThisForm(PlantGene.ID)) {
            AbstractPower thorns = AbstractDungeon.player.getPower(ThornsPower.POWER_ID);
            if (thorns != null) {
                this.baseDamage += thorns.amount * this.magicNumber;
            }
            AbstractPower brambles = AbstractDungeon.player.getPower(BramblesPower.POWER_ID);
            if (brambles != null) {
                this.baseDamage += brambles.amount * this.magicNumber;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpikeVolley();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }
}