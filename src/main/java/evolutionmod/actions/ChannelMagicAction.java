package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LavafolkGene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChannelMagicAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;
	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}

	private float startingDuration;
	private CardGroup fateGroup;
	private LavafolkGene lavafolkGene;

	public ChannelMagicAction(int fateAmount, LavafolkGene lavafolkGene) {
		this.amount = fateAmount;
		this.lavafolkGene = lavafolkGene;

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
				for (int i = Math.min(amount, drawPile.size()); i > 0; --i) {
					AbstractCard card = drawPile.getRandomCard(true);
					fateGroup.addToTop((AbstractCard) card);
					drawPile.removeCard(card);
				}
				if (fateGroup.isEmpty()) {
					this.isDone = true;
					return;
				}

				AbstractDungeon.gridSelectScreen.open(fateGroup, fateGroup.size(), true, TEXT[0]);
			} else {
				if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
					AbstractDungeon.gridSelectScreen.selectedCards.forEach(c -> {
						AbstractDungeon.player.drawPile.moveToDiscardPile(c);
						lavafolkGene.onEvoke();
						fateGroup.removeCard(c);
					});
					AbstractDungeon.gridSelectScreen.selectedCards.clear();
				}
				fateGroup.shuffle();
				List<AbstractCard> copy = new ArrayList<>(fateGroup.group);
				copy.forEach(c -> fateGroup.moveToDeck(c, false));
			}

			this.tickDuration();
		}
	}
}
