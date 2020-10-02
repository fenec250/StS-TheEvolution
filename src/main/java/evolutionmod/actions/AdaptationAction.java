package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.DefendEvo;
import evolutionmod.cards.StrikeEvo;
import evolutionmod.orbs.AbstractGene;

import java.util.*;

public class AdaptationAction extends AbstractGameAction {


	private AbstractPlayer player;
	private int geneAmt;
	private boolean reduceCost;
	private boolean started;

	public AdaptationAction(AbstractPlayer player, int geneAmt, boolean reduceCost) {
		this.player = player;
		this.geneAmt = geneAmt;
		this.reduceCost = reduceCost;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.SPECIAL;
		this.started = false;
	}

	public void update() {
		if (!started) {
			if (player.orbs.isEmpty()) {
				this.isDone = true;
				this.tickDuration();
				return;
			}
			CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			player.hand.group.stream()
					.filter(c -> c.cardID.equals(StrikeEvo.ID) || c.cardID.equals(DefendEvo.ID))
					.forEach(group::addToTop);
			if (group.isEmpty()) {
				this.isDone = true;
				this.tickDuration();
				return;
			}
			AbstractDungeon.gridSelectScreen.open(group, 1, true, "setGene");
			started = true;
			this.tickDuration();
		} else {
			if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
				AdaptableEvoCard card = (AdaptableEvoCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
				int amountToAdd = geneAmt;
				ArrayList<AbstractOrb> orbs = new ArrayList(player.orbs);
				Iterator<AbstractOrb> iterator = orbs.iterator();
				while (amountToAdd > 0 && iterator.hasNext()) {
					AbstractOrb orb = iterator.next();
					if (orb instanceof AbstractGene) {
						int amountChange = card.tryAdaptingWith(orb, true);
						amountToAdd -= amountChange;
					}
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
