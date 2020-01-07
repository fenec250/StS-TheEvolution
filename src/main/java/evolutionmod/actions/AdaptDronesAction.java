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
		this.actionType = ActionType.DAMAGE;
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
				.forEach(c -> ((Drone) c).addAdaptation(gene.getAdaptation()));
		consumeOrb(player, gene);

		this.isDone = true;
		this.tickDuration();
		//AbstractDungeon.cardRng.random([ArrayList].size() - 1)
	}

	// FIXME: this is a duplication of AdaptableEvoCard.consumeOrb
	protected boolean consumeOrb(AbstractPlayer player, AbstractOrb orb) {
		if (player.orbs.isEmpty()) {
			return false;
		}
		boolean result = player.orbs.remove(orb);
		if (result) {
			player.orbs.add(new EmptyOrbSlot(((AbstractOrb)player.orbs.get(0)).cX, ((AbstractOrb)player.orbs.get(0)).cY));
			for (int i = 0; i < player.orbs.size(); ++i) {
				((AbstractOrb)player.orbs.get(i)).setSlot(i, player.maxOrbs);
			}
		}
		return result;
	}
}
