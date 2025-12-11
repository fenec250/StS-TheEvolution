package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BroodPower;
import evolutionmod.powers.CoralPower;

public class CoralStewards extends BaseEvoCard {
    public static final String ID = "evolutionmod:CoralStewards";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Coral.png";
    private static final int COST = 2;
    private static final int POWER_AMT = 1;
    public static final int BLOCK_THRESHOLD = 12;

    public CoralStewards() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CoralPower(p, this.magicNumber)));
        addToBot(new InsectGene().getChannelAction());
        if (this.upgraded) {
            addToBot(new InsectGene().getChannelAction());
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CoralStewards();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}