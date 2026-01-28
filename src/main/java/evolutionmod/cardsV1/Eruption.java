package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.EruptionPower;

public class Eruption
        extends BaseEvoCard {
    public static final String cardID = "Eruption";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Eruption.png";
    private static final int COST = 2;
    private static final int ERUPTION_DAMAGE_AMT = 18;
    private static final int UPGRADE_ERUPTION_DAMAGE_AMT = 6;
//    private static final int ERUPTION_AMT = 1;

    public Eruption() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.SELF);
//        this.magicNumber = this.baseMagicNumber = ERUPTION_AMT;
        this.magicNumber = this.baseMagicNumber = ERUPTION_DAMAGE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EruptionPower(p, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Eruption();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ERUPTION_DAMAGE_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }
}