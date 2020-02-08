package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.GhostGene;
import evolutionmod.patches.AbstractCardEnum;

public class DrainCurse
        extends CustomCard {
    public static final String ID = "evolutionmod:DrainCurse";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/GhostForm.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 2;
    private static final int UPGRADE_BLOCK_AMT = 1;

    private int extraBlock = 0;

    public DrainCurse() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF_AND_ENEMY);
        this.block = this.baseBlock = 0;
        this.magicNumber = this.baseMagicNumber = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int block = this.block + calculateExtraBlock(m);
        if (m.hasPower(WeakPower.POWER_ID)) {
            AbstractPower power = m.getPower(WeakPower.POWER_ID);
            if (power.amount > 1) {
                int drainAmount = power.amount - 1;
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(m, p, power, drainAmount));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, drainAmount * this.magicNumber + this.block));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new GhostGene()));
    }

    private int calculateExtraBlock(AbstractMonster mo) {
        if (mo.hasPower(WeakPower.POWER_ID)) {
            AbstractPower weak = mo.getPower(WeakPower.POWER_ID);
            this.extraBlock = weak.amount * this.magicNumber - 2;
        }
        return this.extraBlock;
    }

    @Override
    public AbstractCard makeCopy() {
        return new DrainCurse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BLOCK_AMT);
        }
    }

    @Override
    public void calculateDamageDisplay(AbstractMonster mo) {
        super.calculateDamageDisplay(mo);
        calculateExtraBlock(mo);
    }

    public static class BlockAmount extends DynamicVariable {

        @Override
        public int baseValue(AbstractCard card) {
            return card.baseBlock;
        }

        @Override
        public int value(AbstractCard card) {
            DrainCurse drainCurse = (DrainCurse) card;
            return drainCurse.extraBlock + drainCurse.block;
        }

        @Override
        public boolean isModified(AbstractCard card) {
            return card.block == card.baseBlock;
        }

        @Override
        public String key() {
            return "evolutionmod:DrainCurseAmount";
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            return card.upgraded;
        }
    }
}