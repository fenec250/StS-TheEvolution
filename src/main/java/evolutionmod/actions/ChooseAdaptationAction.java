package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.Gene;
import evolutionmod.orbs.AbstractGene;

import java.util.List;
import java.util.stream.Collectors;

public class ChooseAdaptationAction extends AbstractGameAction {

//	private static final UIStrings uiStrings;
//	public static final String[] TEXT;
//	static {
//		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
//		TEXT = uiStrings.TEXT;
//	}

	private float startingDuration;
	private CardGroup geneGroup;
	private AdaptableEvoCard card;

	public ChooseAdaptationAction(int adaptAmount, AdaptableEvoCard card) {
		this.amount = adaptAmount;
		this.card = card;

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
//				List<AbstractCard> current =
				AbstractDungeon.player.orbs.stream()
						.filter(o -> o instanceof AbstractGene)
						.forEach(o -> geneGroup.addToTop(new Gene((AbstractGene)o)));
				// don't copy the genes, else they won't be consumed.
				if (geneGroup.isEmpty()) {
					this.isDone = true;
					return;
				}

				AbstractDungeon.gridSelectScreen.open(geneGroup, amount, true,
						"Choose up to " + Math.min(amount, geneGroup.size()) + " genes to Adapt:");
			} else {
				if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
					List<AbstractGene> genes = AbstractDungeon.gridSelectScreen.selectedCards.stream()
							.map(g -> ((Gene) g).getGene()).collect(Collectors.toList());
					genes.forEach(card::tryAdaptingWith);
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
				}
				geneGroup.clear();
			}

			this.tickDuration();
		}
	}
}
