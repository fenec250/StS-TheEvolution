package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.cards.Drone;
import evolutionmod.orbs.AbstractGene;

public class AdaptDronesAction extends AbstractGameAction {

	private AbstractPlayer player;
	private int droneNumber;

	public AdaptDronesAction(AbstractPlayer player, int droneNumber) {
		this.player = player;
		this.droneNumber = droneNumber;
		this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.SPECIAL;
	}

	public void update() {
		if (player.orbs == null || player.orbs.isEmpty() || !(player.orbs.get(0) instanceof AbstractGene)) {
			this.isDone = true;
			this.tickDuration();
			return;
		}
		AbstractGene gene = (AbstractGene) player.orbs.get(0);
		player.hand.group.stream()
				.filter(c -> c.cardID.equals(Drone.ID))
				.limit(droneNumber)
				.forEach(c -> ((Drone) c).tryAdaptingWith(gene, true));

		this.isDone = true;
		this.tickDuration();
		//AbstractDungeon.cardRng.random([ArrayList].size() - 1)
	}
}
