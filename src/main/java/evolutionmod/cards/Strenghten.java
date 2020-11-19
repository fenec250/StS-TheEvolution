package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class Strenghten
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Strenghten";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurSkl.png";
    private static final int COST = 1;
    private static final int VIGOR_AMT = 3;
    private static final int UPGRADE_VIGOR_AMT = 2;

    public Strenghten() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = VIGOR_AMT;
    }

        @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new VigorPower(p, this.magicNumber)));

        formEffect(CentaurGene.ID, () -> formEffect(LymeanGene.ID, () -> {
//                addToBot(new StrenghtenAction(p));
//                this.exhaust = true;
                p.orbs.stream()
                        .filter(o -> this.canAdaptWith(o) > 0)
                        .findAny()
                        .ifPresent(o -> this.tryAdaptingWith(o, true));
            }));
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.getGeneId().equals(CentaurGene.ID) ? 1 : 0;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strenghten();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VIGOR_AMT);
        }
    }
}
