package evolutionmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.DefendEvo;
import evolutionmod.cards.Drone;
import evolutionmod.cards.StrikeEvo;
import evolutionmod.orbs.AbstractGene;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IncubateAction extends AbstractGameAction {

	private AbstractPlayer player;
	private boolean reduceCost;
	private boolean started;

	public IncubateAction(AbstractPlayer player, boolean shouldReduceCost) {
		this.player = player;
		this.reduceCost = shouldReduceCost;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.SPECIAL;
	}

	public void update() {
		if (!started) {
			// Draw a Drone
			if (!player.drawPile.isEmpty() && player.hand.size() < BaseMod.MAX_HAND_SIZE) {
				List<AbstractCard> droneList = player.drawPile.group.stream()
						.filter(c -> c instanceof Drone)
						.collect(Collectors.toList());
				if (!droneList.isEmpty()) {
					AbstractCard drawnDrone = droneList.get(AbstractDungeon.cardRng.random(droneList.size() - 1));
					player.drawPile.removeCard(drawnDrone);
					player.hand.addToHand(drawnDrone);
				}

			}
			// Select a Drone to upgrade
			CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			player.hand.group.stream()
					.filter(c -> c instanceof Drone)
					.forEach(group::addToTop);
			if (group.isEmpty()) {
				this.isDone = true;
				this.tickDuration();
				return;
			}
			AbstractDungeon.gridSelectScreen.open(group, 1, "Choose a Drone to upgrade", false, false, false, false);
			started = true;
			this.tickDuration();
		} else {
			if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
				AdaptableEvoCard card = (AdaptableEvoCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
				if (!card.upgraded) {
					card.upgrade();
				}

				if (reduceCost) {
					card.setCostForTurn(card.costForTurn - 1);
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.isDone = true;
				this.tickDuration();
			}
		}
	}
}
