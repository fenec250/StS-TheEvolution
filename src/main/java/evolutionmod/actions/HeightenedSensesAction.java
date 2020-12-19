package evolutionmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;
import java.util.stream.Collectors;

public class HeightenedSensesAction extends AbstractGameAction {

	private boolean shuffleCheck;

	public HeightenedSensesAction(AbstractPlayer player, int drawAmount) {
		this(player, drawAmount, false);
	}

	private HeightenedSensesAction(AbstractPlayer player, int drawAmount, boolean shuffleCheck) {
		this.source = player;
		this.amount = drawAmount;
		this.shuffleCheck = shuffleCheck;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DRAW;
	}

	public void update() {
		AbstractPlayer player = (AbstractPlayer) this.source;
		if (!this.shuffleCheck) {
			if (player.hasPower("No Draw")) {
				this.isDone = true;
				this.duration = 0.0F;
				this.actionType = ActionType.WAIT;
				return;
			}
			if (player.hand.size() + this.amount >= BaseMod.MAX_HAND_SIZE) {
				player.createHandIsFullDialog();
				this.amount = BaseMod.MAX_HAND_SIZE - player.hand.size();
			}
			int deckSize = player.drawPile.size();
			if (deckSize + player.discardPile.size() == 0) {
				this.isDone = true;
				return;
			} else  if (this.amount > deckSize) {
				int tmp = this.amount - deckSize;
				AbstractDungeon.actionManager.addToTop(new HeightenedSensesAction(player, tmp, false));
				AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
				this.amount = deckSize;
			}
			this.shuffleCheck = true;
		}

		List<Runnable> actions = player.drawPile.group.stream()
				.skip(player.drawPile.group.size() - this.amount)
//				.limit(this.amount)
				.map(card ->
				{
					if (card.type == AbstractCard.CardType.ATTACK) {
						return (Runnable) () -> player.drawPile.moveToHand(card, player.drawPile);
					}
					return (Runnable) () -> player.drawPile.moveToDiscardPile(card);
				}
		).collect(Collectors.toList());
		actions.forEach(Runnable::run);
		AbstractDungeon.player.hand.refreshHandLayout();
		this.isDone = true;
		this.tickDuration();
	}
}
