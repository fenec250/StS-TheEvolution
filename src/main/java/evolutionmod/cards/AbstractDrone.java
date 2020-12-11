package evolutionmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.powers.BroodPower;

public abstract class AbstractDrone
        extends BaseEvoCard {

    public AbstractDrone(
            final String id, final String name, final String img, final int cost, final String rawDescription,
            final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }


    @Override
    public void applyPowers() {
        removeStrAround(super::applyPowers);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        removeStrAround(() -> super.calculateCardDamage(mo));
    }

    public void removeStrAround(Runnable supercall) {
        int baseDamage = this.baseDamage;

        AbstractPower str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
        int strAmount = 0;
        if (str != null) {
            strAmount = str.amount;
            str.amount = 0;
        }
        supercall.run();
        this.baseDamage = baseDamage;
        this.isDamageModified = this.baseDamage != this.damage;
        if (str != null) {
            str.amount = strAmount;
        }
    }

    public void removeDexAround(Runnable supercall) {
        int baseBlock = this.baseBlock;

        AbstractPower dex = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        int dexAmount = 0;
        if (dex != null) {
            dexAmount = dex.amount;
            dex.amount = 0;
        }

        supercall.run();

        this.baseBlock = baseBlock;
        this.isBlockModified = this.baseBlock != this.block;
        if (dex != null) {
            dex.amount = dexAmount;
        }
    }

    public void applyBroodPowerAround(Runnable supercall) {
        int baseDamage = this.baseDamage;
        int baseBlock = this.baseBlock;
        AbstractPower potency = AbstractDungeon.player.getPower(BroodPower.POWER_ID);
        int pot = potency == null ? 0 : potency.amount;
        this.baseDamage += pot;
        this.baseBlock += pot;

        supercall.run();

        this.baseDamage = baseDamage;
        this.isDamageModified = this.baseDamage != this.damage;

        this.baseBlock = baseBlock;
        this.isBlockModified = this.baseBlock != this.block;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }
}