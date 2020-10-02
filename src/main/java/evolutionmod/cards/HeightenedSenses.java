package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.HeightenedSensesAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.patches.AbstractCardEnum;

public class HeightenedSenses
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:HeightenedSenses";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BeastForm.png";
    private static final int COST = 0;
    private static final int DRAW_AMT = 3;
    private static final int UPGRADED_DRAW_AMT = 1;
//    private static final int MAX_ADAPT_AMT = 2;
//    private static final int UPGRADE_MAX_ADAPT_AMT = 1;

    public HeightenedSenses() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRAW_AMT;
//        this.adaptationMap.put(BeastGene.ID, new BeastGene.Adaptation(0, MAX_ADAPT_AMT));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // TODO: Check to do like ScrapeFollowUpAction
        AbstractDungeon.actionManager.addToBottom(new HeightenedSensesAction(p, this.magicNumber));
        if (!AbstractGene.isPlayerInThisForm(BeastGene.ID)) {
            addToBot(new ChannelAction(new BeastGene()));
        } else {
            p.orbs.stream()
                    .filter(o -> this.canAdaptWith(o) > 0)
                    .findAny()
                    .ifPresent(o -> this.tryAdaptingWith(o, true));
        }
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.getGeneId().equals(BeastGene.ID) ? adaptation.amount : 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_DRAW_AMT);
//            this.upgradeAdaptationMaximum(BeastGene.ID, UPGRADE_MAX_ADAPT_AMT);
        }
    }
}