package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.AphrodisiacPower;
import evolutionmod.powers.LustPower;

public class Aphrodisiac
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Aphrodisiac";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Aphrodisiac.png";
    private static final int COST = 1;
    private static final int VULNERABLE_AMT = 2;
    private static final int UPGRADE_VULNERABLE_AMT = 1;

    public Aphrodisiac() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = VULNERABLE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new AphrodisiacPower(m, this.magicNumber)));
        BaseEvoCard.formEffect(SuccubusGene.ID,
                () -> addToBot(new ApplyPowerAction(m, p, new LustPower(m, this.magicNumber))));
        BaseEvoCard.formEffect(LizardGene.ID, () ->
            m.powers.stream()
                    .filter(pow -> pow.ID.equals(PoisonPower.POWER_ID))
                    .findAny()
                    .ifPresent(AbstractPower::atStartOfTurn));
//        AbstractCard status = p.hand.getRandomCard(CardType.STATUS, true);
//        if (status != null) {
//            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(status, p.hand));
//        }
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

    @Override
    public int getNumberOfGlows() {
        return 2;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return true;
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        switch (glowIndex) {
            case 0:
                return isPlayerInThisForm(SuccubusGene.ID) ? SuccubusGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            case 1:
                return isPlayerInThisForm(LizardGene.ID, SuccubusGene.ID) ? LizardGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            default:
                return BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}