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

import java.util.Collection;
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
    private static final int ADAPTATION_AMT = 2;
    private static final int UPGRADE_ADAPTATION_AMT = 1;

    public Dive() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = ADAPTATION_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
	    Set<AbstractOrb> orbsToConsume = p.orbs.stream()
			    .filter(o -> o instanceof MerfolkGene)
			    .limit(1)
			    .filter(o -> 0 < this.addAdaptation(((AbstractGene) o).getAdaptation()))
			    .collect(Collectors.toSet());
	    consumeOrbs(p, orbsToConsume);
	    this.useAdaptations(p, m);
    }

	@Override
	public int addAdaptation(AbstractAdaptation adaptation) {
		if (!adaptation.getGeneId().equals(MerfolkGene.ID)) {
			return 0;
		}
		int amountLeft = this.magicNumber;
		if (this.adaptationMap.containsKey(MerfolkGene.ID)) {
			amountLeft -= this.adaptationMap.get(MerfolkGene.ID).amount;
		}
		adaptation.amount = Math.min(adaptation.amount, amountLeft);
		return super.addAdaptation(adaptation);
	}

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_ADAPTATION_AMT);
        }
    }
}