package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StrengthenAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}

	private float startingDuration;
	private int energyOnUse;
	private CardGroup fateGroup;

	public StrengthenAction(int fateAmount, AbstractCreature source) {
		this.amount = fateAmount;
		this.source = source;
		this.energyOnUse = EnergyPanel.getCurrentEnergy();

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
				addToTop(new TriggerScryEffectsAction());
//				AbstractDungeon.player.powers.forEach(AbstractPower::onScry);
//				var1 = AbstractDungeon.player.powers.iterator();
//				while(var1.hasNext()) {
//					AbstractPower p = (AbstractPower)var1.next();
//					p.onScry();
//				}

				if (AbstractDungeon.player.drawPile.isEmpty()
						|| amount <= 0) {
					this.isDone = true;
					return;
				}
				fateGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				CardGroup drawPile = AbstractDungeon.player.drawPile;
				Set<AbstractCard> copy = new HashSet<>();
				drawPile.group.stream()
						.filter(c -> c.type == AbstractCard.CardType.ATTACK)
						.collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
							Collections.shuffle(l, AbstractDungeon.cardRng.random);
							return l.stream();
						}))
						.limit(this.amount)
						.forEach(c -> {
							if (copy.add(c)) {
								drawPile.group.remove(c);
								drawPile.addToTop(c);
								fateGroup.addToBottom(c);
							}
						});
//				for (int i = Math.min(amount, drawPile.size()); i > 0; --i) {
//					AbstractCard card = drawPile.getRandomCard(true);
//					fateGroup.addToTop((AbstractCard) card);
//					drawPile.removeCard(card);
//				}
				if (fateGroup.isEmpty()) {
					this.isDone = true;
					return;
				}
				int vigor = 2 * fateGroup.group.stream()
						.mapToInt(c -> c.cost < 0 ? (c.cost == -1 ? energyOnUse : 0) : c.cost).sum();
				addToTop(new ApplyPowerAction(source, source, new VigorPower(source, vigor)));

				AbstractDungeon.gridSelectScreen.open(fateGroup, fateGroup.size(), true, TEXT[0]);
			} else {
				if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
					AbstractDungeon.gridSelectScreen.selectedCards.forEach(c -> {
						AbstractDungeon.player.drawPile.removeCard(c);
						AbstractDungeon.player.drawPile.moveToDiscardPile(c);
					});
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
				}
			}

			this.tickDuration();
		}
	}
}
