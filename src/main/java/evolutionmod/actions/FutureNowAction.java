package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MayhemPower;

import java.util.ArrayList;
import java.util.List;

public class FutureNowAction extends AbstractGameAction {

	private float startingDuration;
	private int remaining;

	public FutureNowAction(int amount) {
		this(amount, amount);
	}

	private FutureNowAction(int amount, int remaining) {
		this.amount = amount;
		this.remaining = remaining;

		this.actionType = ActionType.USE;
		this.startingDuration = Settings.ACTION_DUR_FAST;
		this.duration = this.startingDuration;
	}

	public void update() {

		if (this.duration == startingDuration) {
//			while (0 < this.remaining) {
				if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
					this.isDone = true;
					return;
				}

				if (AbstractDungeon.player.drawPile.isEmpty()) {
					this.addToTop(new FutureNowAction(this.amount, this.remaining));
					this.addToTop(new EmptyDeckShuffleAction());
					this.isDone = true;
					return;
				}

				AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
				int cardCost = card.cost >= 0 ? card.cost : card.cost == -1 ? this.amount : 0;
				if (cardCost <= this.remaining) {
					addToTop(new FutureNowAction(this.amount, this.remaining - cardCost));
					AbstractMonster randomMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
					addToTop(new PlayTopCardAction(randomMonster, false));
				}
//				List<AbstractCard> cards = new ArrayList<>();
//				if (cardCost <= this.remaining) {
//					AbstractDungeon.player.drawPile.group.remove(card);
//					cards.add(card);
//				}
//				for (int i = cards.size(); i > 0; --i) {
//					AbstractDungeon.getCurrRoom().souls.remove(card);
//					card.exhaustOnUseOnce = false;
//					AbstractDungeon.player.limbo.group.add(card);
//					card.current_y = -200.0F * Settings.scale;
//					card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.xScale * (2 - remaining);
//					card.target_y = (float) Settings.HEIGHT / 2.0F;
//					card.targetAngle = 0.0F;
//					card.lighten(false);
//					card.drawScale = 0.12F;
//					card.targetDrawScale = 0.75F;
//					card.applyPowers();
//					AbstractMonster randomMonster = AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
//					this.addToTop(new NewQueueCardAction(card, randomMonster, false, true));
//					this.addToTop(new UnlimboAction(card));
//					if (!Settings.FAST_MODE) {
//						this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
//					} else {
//						this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
//					}
//				}
//				if (cardCost > 0) {
//					this.remaining -= cardCost;
//				}

//			}
			this.isDone = true;
		}
	}
}
