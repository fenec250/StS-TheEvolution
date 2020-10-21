package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class Aphrodisiac
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Aphrodisiac";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardSkl.png";
    private static final int COST = 1;
    private static final int VULNERABLE_AMT = 1;
    private static final int UPGRADE_VULNERABLE_AMT = 3;

    public Aphrodisiac() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = VULNERABLE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!AbstractGene.isPlayerInThisForm(SuccubusGene.ID)) {
            addToBot(new ChannelAction(new SuccubusGene()));
        } else {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false)));
        }
        if (!AbstractGene.isPlayerInThisForm(LizardGene.ID)) {
            addToBot(new ChannelAction(new LizardGene()));
        } else {
            m.powers.stream()
                    .filter(pow -> pow.ID.equals(PoisonPower.POWER_ID))
                    .findAny()
                    .ifPresent(AbstractPower::atStartOfTurn);
        }
        AbstractCard status = p.hand.getRandomCard(CardType.STATUS, true);
        if (status != null) {
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(status, p.hand));
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LizardGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Aphrodisiac();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_VULNERABLE_AMT);
        }
    }
}