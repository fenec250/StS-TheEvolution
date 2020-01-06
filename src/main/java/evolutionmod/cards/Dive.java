package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.Set;
import java.util.stream.Collectors;

public class Dive
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Dive";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 7;
    private static final int UPGRADE_BLOCK_AMT = 3;
    private static final int MAX_ADAPT_AMT = 2;
    private static final int UPGRADE_MAX_ADAPT_AMT = 1;

    public Dive() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.adaptationMaximum = MAX_ADAPT_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
	    p.orbs.stream()
			    .filter(o -> o instanceof MerfolkGene)
                .findAny()
                .ifPresent(o-> this.addAdaptation((AbstractGene) o));
	    this.useAdaptations(p, m);
    }

    @Override
    public int addAdaptation(AbstractGene gene) {
        if (!gene.ID.equals(MerfolkGene.ID)) {
            return 0;
        }
        if (this.adaptationMap.containsKey(MerfolkGene.ID)
                && this.adaptationMaximum <= this.adaptationMap.get(MerfolkGene.ID).amount) {
            return 0;
        }
        return super.addAdaptation(gene);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeAdaptationMaximum(UPGRADE_MAX_ADAPT_AMT);
        }
    }
}