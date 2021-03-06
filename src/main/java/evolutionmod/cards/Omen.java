package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.OmenAction;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;

public class Omen
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Omen";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowSkl.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 2;
    private static final int UPGRADE_FATE_AMT = 1;
    private static final int LYMEAN_FATE_AMT = 1;

    public Omen() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        boolean inForm = formEffect(LymeanGene.ID);
        if (inForm) {
            fate += LYMEAN_FATE_AMT;
        }
        if (this.upgraded) {
            addToBot(new ChannelAction(new ShadowGene()));
        } else {
            formEffect(ShadowGene.ID);
        }

        boolean applyWeak = this.upgraded || BaseEvoCard.isPlayerInThisForm(ShadowGene.ID);
        addToBot(new OmenAction(fate, m, applyWeak));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Omen();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}