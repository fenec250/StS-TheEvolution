package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import evolutionmod.cards.CrystalDust;
import evolutionmod.cards.CrystalShard;
import evolutionmod.cards.CrystalStone;
import evolutionmod.orbs.LavafolkGene;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrystalShapingAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
		TEXT = uiStrings.TEXT;
	}

	private float startingDuration;
	private List<AbstractCard> others;
	private CardGroup returnGroup;
//	private String actionTitle;

	public CrystalShapingAction(int returnAmount) {
		this.amount = returnAmount;
//		this.actionTitle = actionTitle;

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
//				addToTop(new TriggerScryEffectsAction());
//				AbstractDungeon.player.powers.forEach(AbstractPower::onScry);
//				var1 = AbstractDungeon.player.powers.iterator();
//				while(var1.hasNext()) {
//					AbstractPower p = (AbstractPower)var1.next();
//					p.onScry();
//				}

				if (AbstractDungeon.player.exhaustPile.isEmpty()
						|| amount <= 0) {
					this.isDone = true;
					return;
				}
				others = AbstractDungeon.player.exhaustPile.group.stream()
						.filter(c -> !c.cardID.equals(CrystalStone.ID)) // TODO: how do we make custom tags again?
						.filter(c -> !c.cardID.equals(CrystalShard.ID))
						.filter(c -> !c.cardID.equals(CrystalDust.ID))
						.collect(Collectors.toList());
				if (AbstractDungeon.player.exhaustPile.size() <= others.size()) {
					this.isDone = true;
					return;
				}
				others.forEach(card -> {
					AbstractDungeon.player.exhaustPile.removeCard(card);
					card.stopGlowing();
					card.unhover();
					card.unfadeOut();
				});
				if (AbstractDungeon.player.exhaustPile.size() <= this.amount) {
					AbstractDungeon.player.exhaustPile.group.stream()
							.collect(Collectors.toList()) // Solidify a separate list
							.forEach(c -> {
								AbstractDungeon.player.hand.addToHand(c);
								c.unhover();
								c.fadingOut = false;
							});
					AbstractDungeon.player.exhaustPile.clear();
					AbstractDungeon.player.exhaustPile.group.addAll(others);
					this.isDone = true;
					return;
				}

				AbstractDungeon.gridSelectScreen.open(
						AbstractDungeon.player.exhaustPile, this.amount, false, TEXT[0]);
			} else {
				if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
					AbstractDungeon.gridSelectScreen.selectedCards.forEach(c -> {
						AbstractDungeon.player.exhaustPile.moveToHand(c, AbstractDungeon.player.exhaustPile);
					});
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
				}
				AbstractDungeon.player.exhaustPile.group.addAll(others);
				others.clear();
			}

			this.tickDuration();
		}
	}
}
