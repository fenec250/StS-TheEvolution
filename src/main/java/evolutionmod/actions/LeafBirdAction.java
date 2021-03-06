package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.powers.GrowthPower;

import java.util.Iterator;
import java.util.function.Supplier;

public class LeafBirdAction extends AbstractGameAction {

	private int discardAmount;
	private String message;
	private boolean harpyEffect;
	private boolean plantEffect;

	public LeafBirdAction(AbstractCreature player, int discardAmount, String message, boolean harpyEffect, boolean plantEffect) {
		this.setValues(player, player);
		this.discardAmount = discardAmount;
		this.duration = this.startDuration = 0.5f;
		this.actionType = ActionType.SPECIAL;
		this.message = message;
		this.harpyEffect = harpyEffect;
		this.plantEffect = plantEffect;
	}

	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			AbstractDungeon.handCardSelectScreen.open(this.message, this.discardAmount, true, true);
			this.addToBot(new WaitAction(0.25F));
			this.tickDuration();
		} else {
			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
				if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
					int size = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
					if (plantEffect) {
						this.addToTop(new ApplyPowerAction(this.target, this.source, new GrowthPower(this.target, size)));
					}
					if (harpyEffect) {
						this.addToTop(new DrawCardAction(this.source, size));
					}
					Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

					while(var1.hasNext()) {
						AbstractCard c = (AbstractCard)var1.next();
						AbstractDungeon.player.hand.moveToDiscardPile(c);
						GameActionManager.incrementDiscard(false);
						c.triggerOnManualDiscard();
					}
				}
				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			}
//			this.isDone = true;
			this.tickDuration();
		}
	}
}
