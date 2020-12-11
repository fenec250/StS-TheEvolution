package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TriggerScryEffectsAction extends AbstractGameAction {

	private static final UIStrings uiStrings;
	public static final String[] TEXT;

	public TriggerScryEffectsAction() {
		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public void update() {
		AbstractDungeon.player.powers.forEach(AbstractPower::onScry);
		Iterator<AbstractCard> var1 = AbstractDungeon.player.discardPile.group.iterator();
		while(var1.hasNext()) {
			AbstractCard c = (AbstractCard)var1.next();
			c.triggerOnScry();
		}
		this.isDone = true;
	}

	static {
		uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
		TEXT = uiStrings.TEXT;
	}
}
