package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.Gene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;

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
				if (!current.contains(PlantGene.ID)) { geneGroup.addToTop(new Gene(new PlantGene()));}
				if (!current.contains(MerfolkGene.ID)) { geneGroup.addToTop(new Gene(new MerfolkGene()));}
				if (!current.contains(HarpyGene.ID)) { geneGroup.addToTop(new Gene(new HarpyGene()));}
				if (!current.contains(LavafolkGene.ID)) { geneGroup.addToTop(new Gene(new LavafolkGene()));}
				if (!current.contains(SuccubusGene.ID)) { geneGroup.addToTop(new Gene(new SuccubusGene()));}
				if (!current.contains(LymeanGene.ID)) { geneGroup.addToTop(new Gene(new LymeanGene()));}
				if (!current.contains(InsectGene.ID)) { geneGroup.addToTop(new Gene(new InsectGene()));}
				if (!current.contains(BeastGene.ID)) { geneGroup.addToTop(new Gene(new BeastGene()));}
				if (!current.contains(LizardGene.ID)) { geneGroup.addToTop(new Gene(new LizardGene()));}
				if (!current.contains(CentaurGene.ID)) { geneGroup.addToTop(new Gene(new CentaurGene()));}
				if (!current.contains(ShadowGene.ID)) { geneGroup.addToTop(new Gene(new ShadowGene()));}
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
