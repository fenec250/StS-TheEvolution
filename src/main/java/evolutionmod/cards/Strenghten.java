package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.StrenghtenPower;

public class Strenghten
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Strenghten";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int STRENGTHEN_AMT = 2;
    private static final int UPGRADE_STRENGTHEN_AMT = 1;

    public Strenghten() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STRENGTHEN_AMT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new ApplyPowerAction(p, p,
                new StrenghtenPower(p, this.magicNumber), this.magicNumber));

        if (AbstractGene.isPlayerInThisForm(LymeanGene.ID)) {
            addToBot(new ChannelAction(new CentaurGene()));
        } else {
            addToBot(new ChannelAction(new LymeanGene()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strenghten();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTHEN_AMT);
        }
    }
}
