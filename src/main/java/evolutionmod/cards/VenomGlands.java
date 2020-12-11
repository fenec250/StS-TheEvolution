package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PoisonCoatedPower;

public class VenomGlands
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:VenomGlands";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardSkl.png";
    private static final int COST = 1;
    private static final int ENVENOM_AMT = 2;
//    private static final int UPGRADE_ENVENOM_AMT = 1;

    public VenomGlands() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ENVENOM_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int poison = this.magicNumber;
        if (this.upgraded) {
            addToBot(new ApplyPowerAction(p, p, new PoisonCoatedPower(p, poison)));
        }
        formEffect(LizardGene.ID, () -> {
            if (!this.upgraded) {
                addToBot(new ApplyPowerAction(p, p, new PoisonCoatedPower(p, poison)));
            } else {
                addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p)));
            }
        });
        formEffect(InsectGene.ID, () -> addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p))));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VenomGlands();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_ENVENOM_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}