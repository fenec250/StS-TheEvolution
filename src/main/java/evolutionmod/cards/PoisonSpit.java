package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PoisonCoatedPower;

public class PoisonSpit
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:PoisonSpit";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardSkl.png";
    private static final int COST = 0;
    private static final int ENVENOM_AMT = 2;
    private static final int UPGRADE_ENVENOM_AMT = 1;

    public PoisonSpit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ENVENOM_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int poison = this.magicNumber;
        if (!upgraded) {
            boolean inForm = formEffect(LizardGene.ID);
            if (inForm) {
                poison += UPGRADE_ENVENOM_AMT;
            }
        } else {
            addToBot(new ChannelAction(new LizardGene()));
        }
        addToBot(new ApplyPowerAction(p, p, new PoisonCoatedPower(p, poison)));

    }

    @Override
    public AbstractCard makeCopy() {
        return new PoisonSpit();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ENVENOM_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}