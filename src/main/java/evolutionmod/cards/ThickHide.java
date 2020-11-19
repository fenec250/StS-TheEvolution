package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.patches.AbstractCardEnum;

public class ThickHide
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ThickHide";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurPower.png";
    private static final int COST = 1;
    private static final int PLATED_ARMOR_AMT = 2;
    private static final int UPGRADE_PLATED_ARMOR_AMT = 1;

    public ThickHide() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = PLATED_ARMOR_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ApplyPowerAction(p, p,
                new PlatedArmorPower(p, this.magicNumber)));

        if (!BaseEvoCard.isPlayerInThisForm(BeastGene.ID)) {
            addToBot(new ChannelAction(new BeastGene()));
        } else {
            if (!this.upgraded && BaseEvoCard.isPlayerInThisForm(CentaurGene.ID)) {
                addToBot(new ApplyPowerAction(p, p,
                        new PlatedArmorPower(p, UPGRADE_PLATED_ARMOR_AMT)));
            } else {
                addToBot(new ChannelAction(new CentaurGene()));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThickHide();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLATED_ARMOR_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
