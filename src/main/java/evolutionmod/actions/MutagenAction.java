package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.Gene;
import evolutionmod.orbs.*;

import java.util.List;
import java.util.stream.Collectors;

public class MutagenAction extends AbstractGameAction {

//	private static final UIStrings uiStrings;
//	public static final String[] TEXT;
//	static {
//		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
//		TEXT = uiStrings.TEXT;
//	}

	private float startingDuration;
	private CardGroup geneGroup;

	public MutagenAction(int geneAmount) {
		this.amount = geneAmount;

		this.actionType = ActionType.CARD_MANIPULATION;
		this.startingDuration = Settings.ACTION_DUR_FAST;
		this.duration = this.startingDuration;
	}

	public void update() {
		// copied from ScryAction
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.isDone = true;
		} else {
//			Iterator var1;
			if (this.duration == this.startingDuration) {

				if (AbstractDungeon.player.maxOrbs <= 0
						|| amount <= 0) {
					this.isDone = true;
					return;
				}
				geneGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				List<String> current = AbstractDungeon.player.orbs.stream()
						.filter(o -> o != null && o.ID != null)
						.map(o -> o.ID)
						.collect(Collectors.toList());
				if (!current.contains(PlantGene2.ID)) { geneGroup.addToTop(new Gene(new PlantGene2()));}
				if (!current.contains(MerfolkGene2.ID)) { geneGroup.addToTop(new Gene(new MerfolkGene2()));}
				if (!current.contains(HarpyGene2.ID)) { geneGroup.addToTop(new Gene(new HarpyGene2()));}
				if (!current.contains(LavafolkGene2.ID)) { geneGroup.addToTop(new Gene(new LavafolkGene2()));}
				if (!current.contains(SuccubusGene2.ID)) { geneGroup.addToTop(new Gene(new SuccubusGene2()));}
				if (!current.contains(LymeanGene2.ID)) { geneGroup.addToTop(new Gene(new LymeanGene2()));}
				if (!current.contains(InsectGene2.ID)) { geneGroup.addToTop(new Gene(new InsectGene2()));}
				if (!current.contains(BeastGene2.ID)) { geneGroup.addToTop(new Gene(new BeastGene2()));}
				if (!current.contains(LizardGene2.ID)) { geneGroup.addToTop(new Gene(new LizardGene2()));}
				if (!current.contains(CentaurGene2.ID)) { geneGroup.addToTop(new Gene(new CentaurGene2()));}
				if (!current.contains(ShadowGene2.ID)) { geneGroup.addToTop(new Gene(new ShadowGene2()));}
				if (geneGroup.isEmpty()) {
					this.isDone = true;
					return;
				}

				AbstractDungeon.gridSelectScreen.open(geneGroup, amount, true,
						"Choose up to " + Math.min(this.amount, geneGroup.size()) + " genes to Channel:");
			} else {
				if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
					AbstractDungeon.gridSelectScreen.selectedCards.forEach(AbstractCard::onChoseThisOption);
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
				}
				geneGroup.clear();
			}

			this.tickDuration();
		}
	}
}
