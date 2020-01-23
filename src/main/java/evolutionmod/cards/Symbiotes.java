package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.GhostGene;
import evolutionmod.orbs.LymeanGeneV2;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.SymbiotesPower;

public class Symbiotes
        extends CustomCard {
    public static final String ID = "evolutionmod:Symbiotes";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int SYMBIOTES_AMT = 2;
    private static final int UPGRADE_SYMBIOTES_AMT = 1;

    public Symbiotes() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = SYMBIOTES_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SymbiotesPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ChannelAction(new LymeanGeneV2()));
        addToBot(new ChannelAction(new GhostGene()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.upgradeMagicNumber(UPGRADE_SYMBIOTES_AMT);
        }
    }
}