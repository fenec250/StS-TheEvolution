package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.HeavyBlade;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.MatureEggPower;
import evolutionmod.powers.PotencyPower;

public class Drone
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Drone";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectAtt.png";
    private static final int COST = 0;
    private static final int DAMAGE_AMT = 3;
    private static final int BLOCK_AMT = 2;
    private static final int UPGRADE_BOTH_AMT = 2;

    public Drone() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
        this.isEthereal = true;
    }

    public static Drone createDroneWithInteractions(AbstractPlayer player) {
        Drone drone = new Drone();
        if (player != null && player.hasPower(MatureEggPower.POWER_ID)) {
            AbstractPower eggs = player.getPower(MatureEggPower.POWER_ID);
            if (eggs.amount > 0) {
                drone.upgrade();
                eggs.stackPower(-1);
            }
        }
        return drone;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.useAdaptations(p, m);
    }

    @Override
    public void applyPowers() {
        this.baseDamage = DAMAGE_AMT + (this.upgraded ? UPGRADE_BOTH_AMT : 0);
        AbstractPower potency = AbstractDungeon.player.getPower(PotencyPower.POWER_ID);
        int pot = potency == null ? 0 : potency.amount;
        AbstractPower str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
        AbstractPower dex = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        int strAmount = 0;
        if (str != null) {
            strAmount = str.amount;
            str.amount = pot;
        } else {
            this.baseDamage += pot;
        }
        int dexAmount = 0;
        if (dex != null) {
            dexAmount = dex.amount;
            dex.amount = pot;
        } else {
            this.baseBlock += pot;
        }
        super.applyPowers();
        if (str != null) {
            str.amount = strAmount;
        } else {
            this.baseDamage -= pot;
        }
        if (dex != null) {
            dex.amount = dexAmount;
        } else {
            this.baseBlock -= pot;
        }
        this.isDamageModified = this.baseDamage != this.damage;
        this.isBlockModified = this.baseBlock != this.block;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPower potency = AbstractDungeon.player.getPower(PotencyPower.POWER_ID);
        int pot = potency == null ? 0 : potency.amount;
        AbstractPower str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
        AbstractPower dex = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        int strAmount = 0;
        if (str != null) {
            strAmount = str.amount;
            str.amount = pot;
        } else {
            this.baseDamage += pot;
        }
        int dexAmount = 0;
        if (dex != null) {
            dexAmount = dex.amount;
            dex.amount = pot;
        } else {
            this.baseBlock += pot;
        }
        super.calculateCardDamage(mo);
        if (str != null) {
            str.amount = strAmount;
        } else {
            this.baseDamage -= pot;
        }
        if (dex != null) {
            dex.amount = dexAmount;
        } else {
            this.baseBlock -= pot;
        }
        this.isDamageModified = this.baseDamage != this.damage;
        this.isBlockModified = this.baseBlock != this.block;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_BOTH_AMT);
            this.upgradeBlock(UPGRADE_BOTH_AMT);
        }
    }
}