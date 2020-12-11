package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FateAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;

	public FateAction(int numCards) {
		this.amount = numCards;

		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public void update() {
		// copied from ScryAction
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
		} else if (this.amount <= 0) {
			addToTop(new TriggerScryEffectsAction());
		} else {
//			Optional<AbstractGameAction> next = AbstractDungeon.actionManager.actions.stream()
//					.filter(a -> !a.isDone)
//					.filter(a -> a instanceof FateAction)
//					.findAny();
//			next.ifPresent(c -> {
//				c.amount += this.amount;
//				this.amount = 0;
//			});

			List<AbstractGameAction> fates = AbstractDungeon.actionManager.actions.stream()
					.filter(a -> !a.isDone)
					.filter(a -> a instanceof FateAction)
					.collect(Collectors.toList());
			int total = this.amount + fates.stream()
					.mapToInt(a -> a.amount)
					.sum();
//			if (total != this.amount) {
				fates.forEach(a -> a.amount = 0);
//				addToBot(new FateAction(total));
//			} else {
				CardGroup drawPile = AbstractDungeon.player.drawPile;
				if (drawPile.isEmpty()) {
					addToTop(new ScryAction(total));
				} else {
					CardGroup fateGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
					for (int i = Math.min(total, drawPile.size()); i > 0; --i) {
						AbstractCard card = drawPile.getRandomCard(true);
						fateGroup.addToTop((AbstractCard) card);
						drawPile.removeCard(card);
					}
					List<AbstractCard> copy = new ArrayList<>(fateGroup.group);
					copy.forEach(c -> fateGroup.moveToDeck(c, false));
					addToTop(new ScryAction(copy.size()));
				}
//			}
		}

		this.isDone = true;
	}

	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}
}
