package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FateAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;

	Map<Predicate<AbstractCard>, Integer> cardSelectors;

	public FateAction(int numCards) {
		this.cardSelectors = new HashMap<>();
		cardSelectors.put(c -> true, numCards);
		this.amount = numCards;

		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public FateAction(Map<Predicate<AbstractCard>, Integer> cardSelectors) {
		this.cardSelectors = cardSelectors;
		this.amount = cardSelectors.values().stream().reduce(0, (a, b) -> a + b);

		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public void update() {
		// copied from ScryAction

//		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
//		} else if (this.amount <= 0) {
//			addToTop(new TriggerScryEffectsAction());
//		} else {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			List<FateAction> fates = AbstractDungeon.actionManager.actions.stream()
					.filter(a -> a instanceof FateAction)
					.filter(a -> !a.isDone)
					.map(a -> (FateAction) a)
					.collect(Collectors.toList());
			fates.add(this);
//			int total = fates.stream()
//					.mapToInt(a -> a.amount)
//					.sum();
//			fates.forEach(a -> a.amount = 0);
			Map<Predicate<AbstractCard>, Integer> aggregatedFates = fates.stream()
					.flatMap(f -> f.cardSelectors.entrySet().stream())
					.collect(Collectors.toMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(a, b) -> a + b
					));
			fates.forEach(a -> a.isDone = true);
			CardGroup drawPile = AbstractDungeon.player.drawPile;
			Set<AbstractCard> copy = new HashSet<>();
			if (!drawPile.isEmpty()) {
				aggregatedFates.forEach((k, v) -> {
					drawPile.group.stream()
							.filter(k)
							.filter(c -> !copy.contains(c))
							.collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
								Collections.shuffle(l, AbstractDungeon.cardRandomRng.random);
								return l.stream();
							}))
							.limit(v)
							.forEach(c -> {
								if (copy.add(c)) {
									drawPile.group.remove(c);
								}
							});

				});
//				for (int i = Math.min(total, drawPile.size()); i > 0; --i) {
//					AbstractCard card = drawPile.getRandomCard(true);
//					copy.add(card);
//					drawPile.group.remove(card);
//				}
				copy.forEach(drawPile::addToTop);
			}
			addToTop(new ScryAction(copy.size()));
		}
		this.isDone = true;
	}

	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}
}
