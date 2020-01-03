package evolutionmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.cards.DefendEvo;
import evolutionmod.cards.StrikeEvo;
import evolutionmod.orbs.AbstractGene;

import java.util.Iterator;

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
//		SkillFromDeckToHandAction;
//		if (this.duration == this.startDuration) {
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
				int i = genesNumber;
				Iterator<AbstractOrb> iterator = player.orbs.iterator();
				while (i > 0 && iterator.hasNext()) {
					AbstractOrb orb = iterator.next();
					if (orb instanceof AbstractGene) {
						i -= card.addAdaptation(((AbstractGene) orb).getAdaptation());
					}
				}

				card.modifyCostForTurn(-1);
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.isDone = true;
				this.tickDuration();
			}
		}
		//AbstractDungeon.cardRng.random([ArrayList].size() - 1)

	}
}
