package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.HeartBreakerPower;
import evolutionmod.powers.SalamanderPower;

public class HeartBreaker extends BaseEvoCard {
    public static final String ID = "evolutionmod:HeartBreaker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/DarkDesires.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int HB_AMT = 1;

    public HeartBreaker() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = HB_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new HeartBreakerPower(p, this.magicNumber)));

    }

    @Override
    public AbstractCard makeCopy() {
        return new HeartBreaker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
//            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}