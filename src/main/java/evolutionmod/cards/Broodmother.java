package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BroodPower;
import evolutionmod.powers.BroodmotherPower;
import evolutionmod.powers.GodFormPower;
import evolutionmod.powers.MatureEggPower;

public class Broodmother extends BaseEvoCard {
    public static final String ID = "evolutionmod:Broodmother";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Broodmother.png";
    private static final int COST = 2;
    private static final int BROOD_POWER_AMT = 2;
    private static final int GENES_AMT = 1;
    private static final int UPGRADE_BROOD_POWER_AMT = 1;

    public Broodmother() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = BROOD_POWER_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BroodPower(p, this.magicNumber)));
//        addToBot(new ApplyPowerAction(p, p, new BroodmotherPower(p, BROODMOTHER_AMT)));
//        addToBot(new ApplyPowerAction(p, p, new MatureEggPower(p, BROODMOTHER_AMT)));
        addToBot(new ChannelAction(new InsectGene()));
//        for (int i = 0; i < this.magicNumber; ++i) {
//        if (this.upgraded) {
//            addToBot(new ChannelAction(new InsectGene()));
//        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Broodmother();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BROOD_POWER_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }
}