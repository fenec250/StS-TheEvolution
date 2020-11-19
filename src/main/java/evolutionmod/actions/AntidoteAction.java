package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AntidoteAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}

	private float startingDuration;

	public AntidoteAction(int amount) {
		this.amount = amount;

		this.actionType = ActionType.CARD_MANIPULATION;
		this.startingDuration = Settings.ACTION_DUR_FAST;
		this.duration = this.startingDuration;
	}

	public void update() {
		// copied from ScryAction
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.isDone = true;
		} else {
			if (AbstractDungeon.player.drawPile.isEmpty()) {
				this.isDone = true;
				return;
			}
			CardGroup statuses = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			CardGroup drawPile = AbstractDungeon.player.drawPile;
			for (int i = 0; i < Math.min(this.amount, drawPile.size()); ++i) {
				AbstractCard card = drawPile.getRandomCard(AbstractCard.CardType.STATUS, true);
				if (card == null) {
					if (statuses.isEmpty()) {
						this.isDone = true;
						return;
					}
					break;
				}
				statuses.addToTop(card);
				drawPile.removeCard(card);
			}

			statuses.group.forEach(c ->
					addToTop(new ExhaustSpecificCardAction(c, statuses)));

			this.tickDuration();
		}
	}
}
