package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RefillDrawPileAction extends AbstractGameAction {

	public RefillDrawPileAction(int numCards) {
		this.amount = numCards;
		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public void update() {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			CardGroup drawPile = AbstractDungeon.player.drawPile;
			CardGroup discardPile = AbstractDungeon.player.discardPile;
			if (drawPile.group.size() < amount && !discardPile.isEmpty()) {
				discardPile.group.stream()
						.collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
							Collections.shuffle(l, AbstractDungeon.shuffleRng.random);
							return l.stream();
						}))
						.limit(amount - drawPile.group.size())
						.forEach(c -> {
							discardPile.removeCard(c);
							drawPile.addToBottom(c);
						});
			}
		}
		this.isDone = true;
	}
}
