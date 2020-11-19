package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;

public class Firebloom
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Firebloom";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantSkl.png";
    private static final int COST = 1;
    private static final int CHANNEL_AMT = 1;
    private static final int UPGRADE_CHANNEL_AMT = 1;

    public Firebloom() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = CHANNEL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i) {
            addToBot(new ChannelAction(new LavafolkGene()));
        }
        formEffect(PlantGene.ID, () -> addToBot(new ChannelAction(new PlantGene())));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Firebloom();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CHANNEL_AMT);
            this.initializeDescription();
        }
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = this.magicNumber > 1 ? UPGRADE_DESCRIPTION : DESCRIPTION;
        super.initializeDescription();
    }
}