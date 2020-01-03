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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AdaptationAction extends AbstractGameAction {

	private AbstractPlayer player;
	private int genesNumber;
	private boolean started;

	public AdaptationAction(AbstractPlayer player, int genesNumber) {
		this.player = player;
		this.genesNumber = genesNumber;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.DAMAGE;
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
			AbstractDungeon.gridSelectScreen.open(group, 1, true, "potato");
			started = true;
			this.tickDuration();
		} else {
			if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
				AdaptableEvoCard card = (AdaptableEvoCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
				int amountToAdd = genesNumber;
				Iterator<AbstractOrb> iterator = player.orbs.iterator();
				Set<AbstractOrb> orbsToRemove = new HashSet<>();
				while (amountToAdd > 0 && iterator.hasNext()) {
					AbstractOrb orb = iterator.next();
					if (orb instanceof AbstractGene) {
						int amountChange = card.addAdaptation(((AbstractGene) orb).getAdaptation());
						amountToAdd -= amountChange;
						if (amountChange > 0) {
							orbsToRemove.add(orb);
						}
					}
				}
				consumeOrbs(player, orbsToRemove);

				card.modifyCostForTurn(-1);
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.isDone = true;
				this.tickDuration();
			}
		}
		//AbstractDungeon.cardRng.random([ArrayList].size() - 1)

	}

	// FIXME: this is a duplication of AdaptableEvoCard.consumeOrbs
	protected boolean consumeOrbs(AbstractPlayer player, Collection<AbstractOrb> orbs) {
		if (player.orbs.isEmpty()) {
			return false;
		}
		boolean result = player.orbs.removeAll(orbs);
		if (result) {
			for (int i = 0; i < orbs.size(); ++i) {
				player.orbs.add(new EmptyOrbSlot(((AbstractOrb)player.orbs.get(0)).cX, ((AbstractOrb)player.orbs.get(0)).cY));
			}
			for (int i = 0; i < player.orbs.size(); ++i) {
				((AbstractOrb)player.orbs.get(i)).setSlot(i, player.maxOrbs);
			}
		}
		return result;
	}
}
