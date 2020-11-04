package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.SetupAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OmenAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}

	public static final int WEAK_AMT = 1;
	private float startingDuration;
	private boolean applyWeakPerDiscard;
	private CardGroup fateGroup;

	public OmenAction(int amount, AbstractMonster target, boolean applyWeakPerDiscard) {
		this.amount = amount;
		this.target = target;
		this.applyWeakPerDiscard = applyWeakPerDiscard;

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
				AbstractDungeon.player.powers.forEach(AbstractPower::onScry);

				if (AbstractDungeon.player.drawPile.isEmpty()) {
					this.isDone = true;
					return;
				}
				fateGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				CardGroup drawPile = AbstractDungeon.player.drawPile;
				for (int i = 0; i < Math.min(this.amount, drawPile.size()); ++i) {
					AbstractCard card = drawPile.getRandomCard(true);
					fateGroup.addToTop(card);
					drawPile.removeCard(card);
				}

				AbstractDungeon.gridSelectScreen.open(fateGroup, fateGroup.size(), true, TEXT[0]);
			} else {
				if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
					AbstractDungeon.gridSelectScreen.selectedCards.forEach(c -> {
//					AbstractDungeon.player.drawPile.moveToDiscardPile(c);
						fateGroup.moveToDiscardPile(c);
//					fateGroup.removeCard(c);
					});
					if (applyWeakPerDiscard) {
						int weak = WEAK_AMT * AbstractDungeon.gridSelectScreen.selectedCards.size();
						addToBot(new ApplyPowerAction(this.target, AbstractDungeon.player,
								new WeakPower(AbstractDungeon.player, weak, false), weak));
					}
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
				}
				fateGroup.shuffle();
				List<AbstractCard> copy = new ArrayList<>(fateGroup.group);
				copy.forEach(c -> fateGroup.moveToDeck(c, false));
			}
			Iterator<AbstractCard> var1 = AbstractDungeon.player.discardPile.group.iterator();
			while(var1.hasNext()) {
				AbstractCard c = (AbstractCard)var1.next();
				c.triggerOnScry();
			}

			this.tickDuration();
		}
	}
}
