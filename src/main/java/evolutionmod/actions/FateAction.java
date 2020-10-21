package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class FateAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	private float startingDuration;
	private Consumer<List<AbstractCard>> selectionConsumer;

	public FateAction(int numCards, Consumer<List<AbstractCard>> selectionConsumer) {
		this.amount = numCards;
		this.selectionConsumer = selectionConsumer;

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
//				var1 = AbstractDungeon.player.powers.iterator();
//				while(var1.hasNext()) {
//					AbstractPower p = (AbstractPower)var1.next();
//					p.onScry();
//				}

				if (AbstractDungeon.player.drawPile.isEmpty()) {
					this.isDone = true;
					return;
				}

				CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				if (this.amount != -1) {
					for(int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
						tmpGroup.addToTop((AbstractCard)AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
					}
				} else {
					Iterator var5 = AbstractDungeon.player.drawPile.group.iterator();

					while(var5.hasNext()) {
						AbstractCard c = (AbstractCard)var5.next();
						tmpGroup.addToBottom(c);
					}
				}

				AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
			} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
				selectionConsumer.accept(AbstractDungeon.gridSelectScreen.selectedCards);
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}
//			var1 = AbstractDungeon.player.discardPile.group.iterator();
//			while(var1.hasNext()) {
//				AbstractCard c = (AbstractCard)var1.next();
//				c.triggerOnScry();
//			}

			this.tickDuration();
		}
	}

	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}
}
